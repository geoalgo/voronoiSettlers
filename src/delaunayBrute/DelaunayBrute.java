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


package delaunayBrute;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Vector;

import delaunay.Delaunay;
import delaunay.Edge;
import delaunay.Pnt;
import delaunay.Triangle;

public class DelaunayBrute implements Delaunay<Integer,EdgeHandle ,TriangleHandle> {

	private Vector<Pnt> vertices;
	private Map<Integer, List<TriangleHandle>> vertexToTriangles;
	private Vector<TriangleHandle> triangles;

	/**
	 * a triangle can access to its neighbors
	 */
	private Map<TriangleHandle, List<TriangleHandle>> trianglesToNeighbors;



	public DelaunayBrute(Vector<Pnt> points,double boundRadius) {
		vertices = new Vector<Pnt>(points);
		triangles = new Vector<TriangleHandle>();
		vertexToTriangles = new TreeMap<>();
		trianglesToNeighbors = new TreeMap<>();

		for(int i = 0 ; i < vertices.size(); ++i)
			vertexToTriangles.put(new Integer(i), new LinkedList<TriangleHandle>());

		computeTriangles(boundRadius);
		computeGraph();

	}


	private void computeTriangles(double boundRadius){
		for(int i = 0 ; i<vertices.size();++i){
			for(int j = i+1 ; j<vertices.size();++j){
				for(int k = j+1 ; k<vertices.size();++k){
					Triangle tri = new Triangle(
							pointOfVertex(i),
							pointOfVertex(j),
							pointOfVertex(k)
							);

					if(tri.getRadius()<=boundRadius){
						if(emptyBall(i, j, k)){
							registerTriangle(i, j, k);
						}
					}
				}
			}
		}

	}

	private boolean emptyBall(int i,int j,int k){
		Triangle tri = new Triangle(pointOfVertex(i),pointOfVertex(j),pointOfVertex(k)); 
		double radius_ijk = tri.getRadius();
		double eps_num = 0.01;

		Pnt center = tri.getCircumcenter();
		for(int l = 0; l < vertices.size() ; ++l){
			if(vertices.get(l).dist(center)< radius_ijk - eps_num){	
				if(l!=i&&l!=j&&l!=k)
					return false;
			}
		}
		return true;
	}

	private void computeGraph(){
		Iterator<TriangleHandle> fIt = faces();
		while(fIt.hasNext()){
			TriangleHandle f = fIt.next();
			Iterator<TriangleHandle> t = surroundingTriangles(f);
			List<TriangleHandle> triangles = new LinkedList<TriangleHandle>();
			while(t.hasNext()) triangles.add(t.next());
			trianglesToNeighbors.put(f, triangles);
		}
	}

	private void registerTriangle(int i,int j,int k){
		TriangleHandle th = new TriangleHandle(i, j, k);
		triangles.add(th);
		vertexToTriangles.get(i).add(th);
		vertexToTriangles.get(j).add(th);
		vertexToTriangles.get(k).add(th);
	}

	@Override
	public Iterator<Integer> vertices() {
		Vector<Integer> res = new Vector<Integer>(vertices.size());
		for(int i = 0; i< vertices.size(); ++i)
			res.add(i);
		return res.iterator();
	}

	@Override
	public Iterator<EdgeHandle> edges() {
		TreeSet<EdgeHandle> res = new TreeSet<EdgeHandle>();
		Iterator<TriangleHandle> fIt = faces();
		while(fIt.hasNext()){
			TriangleHandle f = fIt.next();
			res.add(new EdgeHandle(f.get(0),f.get(1)));
			res.add(new EdgeHandle(f.get(1),f.get(2)));
			res.add(new EdgeHandle(f.get(2),f.get(0)));
		}
		// TODO Auto-generated method stub
		return res.iterator();
	}

	@Override
	public Iterator<EdgeHandle> interiorEdges() {
		TreeSet<EdgeHandle> res = new TreeSet<EdgeHandle>();

		Iterator<EdgeHandle> eIt = edges();
		while(eIt.hasNext()){
			EdgeHandle e = eIt.next();
			int numSurroundingTriangles = 0;
			Iterator<TriangleHandle> triangles = surroundingTriangles(e);
			while(triangles.hasNext()){numSurroundingTriangles++;triangles.next();}
			if(numSurroundingTriangles==2)
				res.add(e);
		}
		return res.iterator();
	}


	@Override
	public Iterator<TriangleHandle> faces() {
		// TODO Auto-generated method stub
		return triangles.iterator();
	}
	
	@Override
	public boolean isBorder(int v){
		Iterator<EdgeHandle> eIt = edges(v);
		while(eIt.hasNext()){
			if(isBorder(eIt.next())) 
				return true;
		}
		return false;
	}
	
	@Override
	public boolean isBorder(EdgeHandle eh){
		Iterator<TriangleHandle> adjTriangles = surroundingTriangles(eh);
		int numTriangles = 0;
		while(adjTriangles.hasNext()){
			adjTriangles.next();
			numTriangles++;
		}
		return (numTriangles==1);
	}

	@Override
	public boolean isBorder(TriangleHandle th){
		for(EdgeHandle eh : th.edges())
			if(isBorder(eh)) return true;
		return false;
	}
	
	@Override
	public Integer closestVertex(Pnt p) {
		double dMin = 10000000;
		int current = 0;
		for(int i = 0 ; i<vertices.size(); ++i){
			if(p.dist(pointOfVertex(i))<dMin){
				dMin = p.dist(pointOfVertex(i));
				current = i;
			}
		}
		return current;
	}

	@Override
	public EdgeHandle closestEdge(Pnt p) {
		double dMin = 10000000;
		EdgeHandle current = null;
		Iterator<EdgeHandle> eIt = edges();
		while(eIt.hasNext()){
			EdgeHandle e = eIt.next();
			Edge edge = new Edge(pointOfVertex(e.first()),pointOfVertex(e.second()));
			if(edge.dist(p)<dMin){
				dMin = edge.dist(p);
				current = e;
			}
		}
		return current;
	}

	@Override
	public EdgeHandle closestDualEdge(Pnt p) {
		double dMin = 10000000;
		EdgeHandle current = null;
		Iterator<EdgeHandle> eIt = interiorEdges();
		while(eIt.hasNext()){
			EdgeHandle e = eIt.next();
			Edge edge = new Edge(dualPoints(e));
			if(edge!=null && edge.dist(p)<dMin){
				dMin = edge.dist(p);
				current = e;
			}
		}
		return current;
	}

	/**
	 * @param e
	 * @return the points of the dual edge of e (or the circumcenter
	 * of the triangle adjacent to e if e is on the border)
	 * otherwise return null
	 */
	@Override
	public List<Pnt> dualPoints(EdgeHandle e){
		List<Pnt> res = new LinkedList<>();
		Iterator<TriangleHandle> neighbors_e = surroundingTriangles(e);
		while(neighbors_e.hasNext()){
			Triangle t1 = new Triangle(pointsOfFace(neighbors_e.next()));
			res.add(t1.getCircumcenter());
		}
		return res;
	}



	@Override
	public TriangleHandle closestFace(Pnt p) {
		// the triangle whose barycenter is the closest from p
		Iterator<TriangleHandle> fIt = faces();
		double dMin = 1e10;
		TriangleHandle closestTriangle = null;

		while(fIt.hasNext()){
			TriangleHandle f = fIt.next();
			if(triangle(f).getCircumcenter().dist(p) < dMin ) {
				dMin = triangle(f).getCircumcenter().dist(p);
				closestTriangle = f;
			}
		}
		return closestTriangle;
	}

	@Override
	public Iterator<Integer> verticesOfTriangle(TriangleHandle f) {
		// TODO Auto-generated method stub
		Vector<Integer> res = new Vector<Integer>();
		res.add(f.get(0));
		res.add(f.get(1));
		res.add(f.get(2));
		return res.iterator();
	}

	@Override
	public Iterator<Integer> neighborsOfVertex(Integer v) {
		HashSet<Integer> res = new HashSet<Integer>();
		for(TriangleHandle t : vertexToTriangles.get(v)){
			res.addAll(t);
		}
		res.remove(new Integer(v));
		return res.iterator();
	}

	@Override
	public Iterator<EdgeHandle> edges(Integer v) {
		Iterator<Integer> neighbors = neighborsOfVertex(v);
		HashSet<EdgeHandle> res = new HashSet<EdgeHandle>();
		while(neighbors.hasNext())
			res.add(new EdgeHandle(v, neighbors.next()));
		return res.iterator();
	}

	/**
	 * @param v
	 * @return a list of edges e around v such that ev is a
	 * triangle
	 */
	@Override
	public List<EdgeHandle> starEdges(Integer v){
		Iterator<TriangleHandle> triangles = surroundingTriangles(v);
		List<EdgeHandle> res = new LinkedList<EdgeHandle>();
		while(triangles.hasNext()){
			TriangleHandle t = triangles.next();
			for(EdgeHandle e : t.edges()){
				if(!e.contains(v)) res.add(e);
			}
		}
		return res;
	}

	/**
	 * triangles that are adjacent to v
	 */
	@Override
	public Iterator<TriangleHandle> surroundingTriangles(Integer v) {
		return vertexToTriangles.get(v).iterator();
	}

	/**
	 * @param f
	 * @return triangles that shares an edge with f
	 */
	@Override
	public Iterator<TriangleHandle> surroundingTriangles(TriangleHandle f) {
		HashSet<TriangleHandle> res = new HashSet<>();

		for(Integer v : f){
			Iterator<TriangleHandle> triangles = surroundingTriangles(v);
			while(triangles.hasNext()){
				res.add(triangles.next());
			}
		}

		List<TriangleHandle> toRemove = new LinkedList<>();
		for(TriangleHandle t : res){
			if(f.intersection(t).size() !=2)
				toRemove.add(t);	
		}
		res.removeAll(toRemove);
		return res.iterator();
	}

	/**
	 * @param f
	 * @return triangles that shares the edge e
	 */
	@Override
	public Iterator<TriangleHandle> surroundingTriangles(EdgeHandle e) {
		List<TriangleHandle> res = new LinkedList<>();

		Iterator<TriangleHandle> triangles = surroundingTriangles(e.first());
		while(triangles.hasNext()){
			TriangleHandle t = triangles.next();
			if(t.contains(e.second()))
				res.add(t);
		}
		return res.iterator();
	}


	@Override
	public Pnt pointOfVertex(Integer v) {
		return vertices.elementAt(v);
	}

	@Override
	public Iterator<Pnt> pointsOfEdge(EdgeHandle e) {
		Vector<Pnt> res = new Vector<Pnt>();
		res.add(pointOfVertex(e.first()));
		res.add(pointOfVertex(e.second()));
		return res.iterator();
	}

	@Override
	public Iterator<Pnt> pointsOfFaces(TriangleHandle f) {
		Vector<Pnt> res = new Vector<Pnt>();
		res.add(pointOfVertex(f.get(0)));
		res.add(pointOfVertex(f.get(1)));
		res.add(pointOfVertex(f.get(2)));
		return res.iterator();
	}


	@Override
	public Vector<Pnt> pointsOfFace(TriangleHandle f) {
		Vector<Pnt> res = new Vector<Pnt>();
		res.add(pointOfVertex(f.get(0)));
		res.add(pointOfVertex(f.get(1)));
		res.add(pointOfVertex(f.get(2)));
		return res;
	}

	@Override
	public Triangle triangle(TriangleHandle f){
		return new Triangle(
				pointOfVertex(f.get(0)),
				pointOfVertex(f.get(1)),
				pointOfVertex(f.get(2))
				);
	}

	@Override
	public HashSet<Edge> dualEdges() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String toString(){
		String res = "Vertices: \n";

		Iterator<Integer> vIt = vertices();
		while(vIt.hasNext()){
			int v = vIt.next();
			res+= "["+v+" ("+ vertices.elementAt(v)+"]  ";
		}

		res+="\n";

		res+="Edges: \n";
		Iterator<EdgeHandle> eIt = edges();
		while(eIt.hasNext()){
			EdgeHandle e = eIt.next();
			res+=e+" ";
		}

		res+="\n";

		res+="Faces: \n";

		Iterator<TriangleHandle> fIt = faces();
		while(fIt.hasNext()){
			TriangleHandle f = fIt.next();
			res+=f+" ";
		}


		return res;
	}

	@Override
	public int numVertices() {
		return vertices.size();
	}

	@Override
	public int numEdges() {
		int res = 0;
		Iterator<EdgeHandle> eIt = edges();
		while(eIt.hasNext()) {
			EdgeHandle e = eIt.next();
			++res;
		}
		return res;
	}

	@Override
	public int numFaces() {
		return triangles.size();
	}

	@Override
	public int numInteriorEdges() {
		int res = 0;
		Iterator<EdgeHandle> edges = interiorEdges();
		for ( ; edges.hasNext() ; ++res ) edges.next();
		return res;
	}


}
