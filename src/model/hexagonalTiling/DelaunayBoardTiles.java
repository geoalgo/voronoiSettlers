/**
* Voronoi settlers- An implementation of the board game Settlers of 
* Catan.
* This file Copyright (C) 2013-2014 David Salinas <catan.100.sisisoyo@spamgourmet.com>
*
* This program is free software; you can redistribute it and/or
* modify it under the terms of the GNU General Public License
* as published by the Free Software Foundation; either version 3
* of the License, or (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program. If not, see <http://www.gnu.org/licenses/>.
*
* The maintainer of this program can be reached at catan.100.sisisoyo@spamgourmet.com
**/

package model.hexagonalTiling;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Vector;

import controlor.DB;
import model.InitialRules;
import model.hexagonalTiling.harbor.Harbor;
import model.ressources.Desert;
import model.ressources.Ressource;
import delaunay.Delaunay;
import delaunay.Edge;
import delaunay.Pnt;
import delaunay.Triangle;
import delaunayBrute.*;

public class DelaunayBoardTiles implements BoardTiles{
	double heightRatio;
	double tileSize;

	//	Delaunay<Integer,EdgeHandle ,TriangleHandle> dt;
	Delaunay<Integer, EdgeHandle, TriangleHandle> dt;
	Triangle initialTriangle;

	Map<Integer,SettlersTile> vertexToTile;
	Map<EdgeHandle,SettlersEdge> edgeToEdgeTile;
	Map<TriangleHandle,SettlersVertex> triangleToTileVertex;
	
	SettlersTile thiefPosition;


	public DelaunayBoardTiles(InitialRules rules) {
		init(rules);
		// TODO Auto-generated constructor stub

		System.out.println("num edges:"+numEdges());

		int numBorderEdges = 0;
		Iterator<SettlersEdge> eIt = borderEdges();
		while(eIt.hasNext()){
			numBorderEdges++;
			eIt.next();
		}

	}

	@Override
	public void init(InitialRules rules) {
		vertexToTile = new TreeMap<Integer,SettlersTile>();
		edgeToEdgeTile = new TreeMap<EdgeHandle,SettlersEdge>() ;
		triangleToTileVertex = new TreeMap<TriangleHandle,SettlersVertex>() ;

		heightRatio = 110./100.;
		// tileSize is s.t. the total size is 90% of the total height

		tileSize = 2./Math.sqrt(3) * heightRatio 
				/ rules.tilesNumber().length;

		// now initializes dt initialTriangle
		buildDelaunayTriangulation(rules.tilesNumber(),1,1);

		//TODO should be in the model
		registerTiles(rules);
	}

	private void registerTiles(InitialRules rules){
		registerVertices(rules);
		registerTriangles(rules);
		registerEdges(rules);
	}

	private void registerVertices(InitialRules rules){
		Iterator<Integer> vIt = dt.vertices();
		while(vIt.hasNext()){
			int v = vIt.next();
			if(!dt.isBorder(v)){
				registerPoint(rules, v);
			}
		}
	}

	private void registerEdges(InitialRules rules){
		Iterator<EdgeHandle> eIt = dt.interiorEdges();
		while(eIt.hasNext())
			registerEdge(eIt.next());
		registerHarbors(rules);
	}

	private void registerHarbors(InitialRules rules){
		Iterator<SettlersEdge> eIt = borderEdges();
		Iterator<Harbor> harbors = rules.harbors();
		while(eIt.hasNext()){
			SettlersEdge edge = eIt.next();
			edge.setHarbor(harbors.next());
		}
	}

	private void registerTriangles(InitialRules rules){
		Iterator<TriangleHandle> tIt = dt.faces();
		while(tIt.hasNext()){
			TriangleHandle t = tIt.next();
			registerTriangle(t,barycenter(t));
		}
	}

	private Pnt barycenter(TriangleHandle t){
		Pnt barycenter = new Pnt(dt.pointOfVertex(t.get(0)));
		barycenter = barycenter.add(dt.pointOfVertex(t.get(1)));
		barycenter = barycenter.add(dt.pointOfVertex(t.get(2)));
		barycenter= barycenter.scale(1./3.);
		return barycenter;
	}

	private void registerPoint(InitialRules rules,int vertex){
		//todo ask content to the rule
		List<Integer> excludedTokens = computeExcludedTokens(vertex);
		Ressource ressource = rules.pickRandomRessource();
		int nbToken = -1; 
		if(!ressource.isDesert())
			nbToken = rules.pickRandomTokenNumberDifferentFrom(excludedTokens);

		SettlersTile tile = new SettlersTile(
				nbToken,ressource,vertex);
		vertexToTile.put(vertex,tile); 
	}

	private List<Integer> computeExcludedTokens(int vertex){
		return new LinkedList<Integer>();
	}

	private void registerEdge(EdgeHandle eh){
		Iterator<TriangleHandle> adjTriangles = dt.surroundingTriangles(eh);
		TriangleHandle upperTriangle = adjTriangles.next();
		TriangleHandle lowerTriangle = adjTriangles.next();
		SettlersEdge edge = new SettlersEdge(
				triangleToTileVertex.get(upperTriangle),
				triangleToTileVertex.get(lowerTriangle),
				eh
				);
		edge.p1();
		edge.p2();
		edgeToEdgeTile.put(eh,edge); 
	}

	private void registerTriangle(TriangleHandle t,Pnt circum){
		SettlersVertex v = new SettlersVertex(circum,t);
		triangleToTileVertex.put(t,v); 
	}


	/**
	 * Build the whole Delaunay Triangulation.
	 * @param tilesNumber
	 * @param width
	 * @param heigth
	 */
	private void buildDelaunayTriangulation(Integer[] tilesNumber,double width,double heigth){
		Vector<Pnt> points = setCatanPoints(tilesNumber,width,heigth);
		// tileSize is s.t. the total size is 90% of the total height
		dt = new DelaunayBrute(points,tileSize*0.8);
	}

	/**
	 * Set points a on a catan grid.
	 * @param tilesNumber
	 * @param width
	 * @param height
	 */
	private Vector<Pnt> setCatanPoints(Integer[] tilesNumber,double width,double height){
		Vector<Integer> numColumns = new Vector<Integer>(Arrays.asList(tilesNumber));
		return get_catan_grid(numColumns,new Pnt(width/2,height*0.0));
	}

	/**
	 * numColumns is the number of tile for every line for ex if it is [4,5,4] then 
	 * we have the setting (where O is the origin)
	 *     
	 *     b b b b
	 *    b o o o b
	 *     b bOb b
	 * a is the length of equilateral triangles
	 * 
	 * Initializes outsidePoints   
	 * 
	 * @return the list of points ('o' in the previous ex)
	 */
	private Vector<Pnt> get_catan_grid(Vector<Integer> numColumns,Pnt origin){
		Vector<Pnt> grid = new Vector<Pnt>();
		double h = tileSize *Math.sqrt(3)/2.;

		int numLine = 0;
		for(int numColumn : numColumns){
			if(numLine%2==1){
				for(int i = 0 ; i<numColumn;++i){
					Pnt pointToAdd = origin.add(new Pnt((i-numColumn/2)*tileSize,numLine*h));
					grid.add(pointToAdd);
				}
			}
			else{
				for(int i = 0 ; i<numColumn;++i){
					Pnt pointToAdd = origin.add(new Pnt((i-numColumn/2)*tileSize+h/2.,numLine*h));
					grid.add(pointToAdd);
				}
			}
			++numLine;
		}
		return grid;
	}


	@Override
	public SettlersTile locateClosestTile(Pnt request) {
		int closest = dt.closestVertex(request);
		SettlersTile tile = vertexToTile.get(closest);
		if(tile == null)
			System.out.println("Cant locate tile");		
		return tile;
	}

	@Override
	public Pnt getPosition(SettlersTile tile) {
		return dt.pointOfVertex(tile.center());
	}

	@Override
	public SettlersEdge locateClosestEdge(Pnt request) {
		EdgeHandle edge = dt.closestDualEdge(request);
		SettlersEdge settlersEdge = edgeToEdgeTile.get(edge);
		if(settlersEdge == null)
			System.out.println("Cant locate edge");		
		return settlersEdge;
	}


	@Override
	public SettlersVertex locateClosestVertex(Pnt request) {
		TriangleHandle th = dt.closestFace(request);
		return triangleToTileVertex.get(th);
	}


	@Override
	public EdgeHandle getEdgeHandle(SettlersEdge edge) {
		// TODO Auto-generated method stub
		return edge.edgeHandle();
	}

	@Override
	public Iterator<SettlersVertex> vertices(){
		return triangleToTileVertex.values().iterator();
	}


	@Override
	public Iterator<SettlersEdge> edges() {
		return edgeToEdgeTile.values().iterator();
	}

	@Override
	public Iterator<SettlersTile> tiles() {
		return vertexToTile.values().iterator();
	}

	@Override
	public List<SettlersTile> tilesNeighbors(SettlersTile tile) {
		Iterator<Integer> vIt = dt.neighborsOfVertex(tile.center);
		List<SettlersTile> list = new LinkedList<SettlersTile>();
		while(vIt.hasNext()){
			int v = vIt.next();
			list.add(vertexToTile.get(v));
		}
		return list;
	}

	@Override
	public List<SettlersTile> tilesNeighbors(SettlersVertex vertex){
		List<SettlersTile> res = new LinkedList<SettlersTile>();
		for(int i : vertex.triangle()){
			SettlersTile tile = vertexToTile.get(i);
			if(tile!=null) res.add(tile);
		}
		return res;
	}

	public List<SettlersEdge> edgeNeighbors(SettlersTile tile) {
		Iterator<EdgeHandle> eIt = dt.edges(tile.center);
		List<SettlersEdge> list = new LinkedList<SettlersEdge>();
		while(eIt.hasNext()){
			list.add(edgeToEdgeTile.get(eIt.next()));
		}
		return list;
	}

	@Override
	public List<SettlersVertex> vertexNeighbors(SettlersTile tile) {
		Iterator<TriangleHandle> trianglesAroundTile = dt.surroundingTriangles(tile.center);
		List<SettlersVertex> res = new LinkedList<SettlersVertex>();
		while(trianglesAroundTile.hasNext()){
			TriangleHandle t = trianglesAroundTile.next();
			res.add(triangleToTileVertex.get(t));
		}		
		return res;
	}

	@Override
	public List<SettlersVertex> vertexNeighbors(SettlersVertex vertex){
		List<SettlersVertex> res = new LinkedList<SettlersVertex>();
		TriangleHandle th = vertexToTriangle(vertex);
		Iterator<TriangleHandle> trianglesAroundVertex = dt.surroundingTriangles(th);
		while(trianglesAroundVertex.hasNext()){
			res.add(triangleToTileVertex.get(trianglesAroundVertex.next()));
		}
		return res;
	}

	@Override
	public List<SettlersEdge> edgeNeighbors(SettlersVertex vertex){
		List<SettlersEdge> res = new LinkedList<SettlersEdge>();
		TriangleHandle t = vertex.triangle();
		for(int i = 0; i <3; ++i)
			for(int j = i+1; j <3; ++j){
				EdgeHandle eh = new EdgeHandle(t.get(i),t.get(j));
				SettlersEdge edge = edgeToEdgeTile.get(eh);
				if(edge != null) // null if border
					//				System.out.println("edgeToEdgeTile.get(eh):"+edgeToEdgeTile.get(eh));
					res.add(edge);
			}
		return res;
	}


	private TriangleHandle vertexToTriangle(SettlersVertex vertex){
		return dt.closestFace(vertex.getPosition());
	}

//	/**
//	 * 
//	 * @param tile
//	 * @return a list of ordered points around the tile
//	 */
//	@Override
//	public List<Pnt> pointsAroundTile(SettlersTile tile) {
//		Iterator<Integer> verticesAround = dt.neighborsOfVertex(tile.center);
//		List<Pnt> res = new LinkedList<Pnt>();
//		HashSet<SettlersVertex> seen = new HashSet<>();
//		
//		while(verticesAround.hasNext()){
//			int v = verticesAround.next(); 
//			SettlersEdge dualEdge = getEdge(new EdgeHandle(tile.center,v));
//			assert(dualEdge!=null);
//			if(seen.contains(dualEdge.v1())){
//				res.add(dualEdge.p2());
//				seen.add(dualEdge.v2());
//			}
//			else{ 
//				res.add(dualEdge.p1());
//				seen.add(dualEdge.v1());
//			}
//		}
//		return res;
//	}



	@Override
	public int numEdges() {
		return edgeToEdgeTile.values().size();
	}

	@Override
	public int numTiles() {
		return vertexToTile.values().size();
	}

//	@Override
//	public Iterator<Pnt> borderPoints() {
////		return outsidePoints.iterator();
//		return null;
//	}


	@Override
	public boolean isBorder(SettlersTile tile){
		// the tile is on the border if the dual point is linked
		// to a border dual point
		Iterator<EdgeHandle> eIt = dt.edges(tile.center);
		while(eIt.hasNext()){
			EdgeHandle eh = eIt.next();
			int oppVertex = eh.first();
			if(eh.first()==tile.center)
				oppVertex = eh.second();
			if(dt.isBorder(oppVertex))
				return true;
		}
		return false;
	}

	@Override
	public boolean isBorder(SettlersEdge edge){
		EdgeHandle eh = edge.edgeHandle();
		return dt.isBorder(eh.first()) ^ dt.isBorder(eh.second());
	}
	
	@Override
	public boolean isBorder(SettlersVertex vertex){
		TriangleHandle th = vertex.triangle();
		for(int v : th){
			if(dt.isBorder(v)) return true;
		}
		return false;
	}

	
	/**
	 * @param v
	 * @return edges adjacent to v that are border edges
	 */
	private Collection<SettlersEdge> adjacentBorderEdges(SettlersEdge e){
		Vector<SettlersEdge> res = new Vector<>();
		EdgeHandle eh = e.edgeHandle();

		Iterator<TriangleHandle> adjTriangles = dt.surroundingTriangles(eh);
		while(adjTriangles.hasNext()){
			TriangleHandle t = adjTriangles.next();
			for(EdgeHandle et : t.edges()){
				SettlersEdge edge_et = getEdge(et);
				if(
						edge_et!=null && //edge found
						!edge_et.edgeHandle().equals(eh) && 
						isBorder(edge_et))
				{
					res.add(edge_et);
				}
			}
		}
		return res;
	}

	@Override
	public Iterator<SettlersEdge> borderEdges(){
		Iterator<SettlersEdge> eIt = edges();
		SettlersEdge currentEdge = findBorderEdge();

		TreeSet<EdgeHandle> seen = new TreeSet<EdgeHandle>();
		Vector<SettlersEdge> res = new Vector<SettlersEdge>();

		boolean foundNew = false;
		do{
			seen.add(currentEdge.edgeHandle());
			res.add(currentEdge);
			Vector<SettlersEdge> adjEdges = new Vector<>();
			adjEdges.addAll(adjacentBorderEdges(currentEdge));
			foundNew = false;
			for(SettlersEdge e : adjEdges){
				if(!seen.contains(e.edgeHandle()) && isBorder(e)) {
					foundNew = true;
					currentEdge = e;
					break;
				}
			}
		} while(foundNew);

		return res.iterator();
	}

	private SettlersEdge findBorderEdge(){
		Iterator<SettlersEdge> eIt = edges();
		SettlersEdge currentEdge = null;
		while(eIt.hasNext()){
			SettlersEdge e = eIt.next();
			if(isBorder(e))
				return e;
		}
		return null;
	}

	@Override
	public SettlersEdge getEdge(EdgeHandle eh){
		return edgeToEdgeTile.get(eh);
	}

	@Override
	public SettlersTile getTile(int v){
		return vertexToTile.get(v);
	}

	@Override
	public SettlersVertex getVertex(TriangleHandle th){
		return triangleToTileVertex.get(th);
	}

	@Override
	public SettlersVertex first(SettlersEdge edge) {
		EdgeHandle eh = getEdgeHandle(edge);
		Iterator<TriangleHandle> adjTriangles = dt.surroundingTriangles(eh);
		assert(adjTriangles.hasNext());
		return triangleToTileVertex.get(adjTriangles.next());
	}

	@Override
	public SettlersVertex second(SettlersEdge edge) {
		EdgeHandle eh = getEdgeHandle(edge);
		Iterator<TriangleHandle> adjTriangles = dt.surroundingTriangles(eh);
		assert(adjTriangles.hasNext()); 
		adjTriangles.next();
		assert(adjTriangles.hasNext());
		return triangleToTileVertex.get(adjTriangles.next());
	}

	@Override
	public
	SettlersTile getThiefPosition(){
		return thiefPosition;
	}
	
	@Override
	public
	void setThiefPosition(SettlersTile newPosition){
		thiefPosition = newPosition;
	}


}
