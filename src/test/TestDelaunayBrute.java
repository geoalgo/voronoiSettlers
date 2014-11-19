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
package test;

import static org.junit.Assert.*;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;
import java.util.Vector;

import org.junit.Test;

import delaunay.Edge;
import delaunay.Pnt;
import delaunay.Triangle;
import delaunayBrute.DelaunayBrute;
import delaunayBrute.EdgeHandle;
import delaunayBrute.TriangleHandle;

public class TestDelaunayBrute {

	Vector<Pnt> getPoints(){
		double h = Math.sqrt(3)/2.;
		Vector<Pnt> points = new Vector<Pnt>();
		points.add(new Pnt(0,0));
		points.add(new Pnt(1,0));
		points.add(new Pnt(2,0));
		points.add(new Pnt(3,0));
		points.add(new Pnt(0.5,h));
		points.add(new Pnt(1.5,h));
		points.add(new Pnt(2.5,h));
		points.add(new Pnt(1,2*h));
		points.add(new Pnt(2,2*h));
		points.add(new Pnt(1.5,3*h));
		return points;
	}
	DelaunayBrute makeDelaunay(){
		Vector<Pnt> points = getPoints();
		return new DelaunayBrute(points, 0.6);
	}

	@Test
	public void testVertices() {
		DelaunayBrute dt = makeDelaunay();
		if(dt.numVertices()!=10) 
			fail("Wrong number of vertices");
	}


	@Test(timeout=100)
	public void testEdges() {
		DelaunayBrute dt = makeDelaunay();
		if(dt.numEdges()!=18) {
			System.out.println("num edges:"+dt.numEdges());
			fail("Wrong number of edges");
		}
	}
	//
	//	@Test
	//	public void testInteriorEdges() {
	//		DelaunayBrute dt = makeDelaunay();
	//		if(dt.numInteriorEdges()!=9) 
	//			fail("Wrong number of edges");
	//	}

	@Test
	public void testFaces() {
		DelaunayBrute dt = makeDelaunay();
		if(dt.numFaces()!=9) 				
			fail("Wrong number of faces");

		Iterator<TriangleHandle> fIt =  dt.faces();

		int numFaces = 0;
		while(fIt.hasNext()){
			TriangleHandle f = fIt.next();
			numFaces++;
		}
		if(numFaces!=9) 				
			fail("Wrong number of faces (it)");


	}

	@Test
	public void testClosestVertex() {
		DelaunayBrute dt = makeDelaunay();
		Integer vertex = dt.closestVertex(new Pnt(1.07,0.87));
		if(vertex != 5)
			fail("Closest vertex failed");
	}

	@Test
	public void testClosestEdge() {
		DelaunayBrute dt = makeDelaunay();
		EdgeHandle e = dt.closestEdge(new Pnt(1.0,0.95));
		if(e.first()!= 4 || e.second()!= 5)
			fail("Closest edge failed");
	}

	@Test
	public void testClosestDualEdge() {
		DelaunayBrute dt = makeDelaunay();
		EdgeHandle e = dt.closestDualEdge(new Pnt(1.03,1.08));
		if(e.first()!= 4 || e.second()!= 5)
			fail("Closest dual edge failed");
	}

	@Test
	public void testClosestFace() {
		DelaunayBrute dt = makeDelaunay();
		TriangleHandle t = dt.closestFace(new Pnt(1.25,1.23));
		if(!t.equals(new TriangleHandle(4,5,7)))
			fail("Closest face failed");
	}

	@Test
	public void testVerticesOfTriangle() {
		DelaunayBrute dt = makeDelaunay();
		Iterator<Integer> vIt= dt.verticesOfTriangle(new TriangleHandle(4,5,7));
		LinkedList<Integer> vertices = new LinkedList<Integer>();
		while(vIt.hasNext()) vertices.add(vIt.next());
		TriangleHandle t = new TriangleHandle(vertices.get(0),vertices.get(1),vertices.get(2));
		if(!t.equals(new TriangleHandle(4,5,7)))
			fail("Not yet implemented");
	}

	@Test
	public void testNeighborsOfVertex() {

		DelaunayBrute dt = makeDelaunay();
		Iterator<Integer> vIt= dt.neighborsOfVertex(5);
		LinkedList<Integer> vertices = new LinkedList<Integer>();
		while(vIt.hasNext()) vertices.add(vIt.next());
		Collections.sort(vertices);
		if(vertices.get(0) != 1  || vertices.get(1) != 2 || vertices.get(2) != 4
				|| vertices.get(3) != 6 || vertices.get(4) != 7 || vertices.get(5) != 8)
			fail("testNeighborsOfVertex failed");
	}

	@Test
	public void testEdgesFromVertex() {
		DelaunayBrute dt = makeDelaunay();
		Iterator<EdgeHandle> eIt = dt.edges(5);
		LinkedList<EdgeHandle> edges = new LinkedList<EdgeHandle>();
		while(eIt.hasNext()) {
			EdgeHandle e = eIt.next();
			edges.add(e);
			if(!e.contains(1) &&!e.contains(2)&&!e.contains(4) &&!e.contains(6)&&!e.contains(7)&&!e.contains(8))
				fail("testEdgesFromVertex : wrong vertex");
		}
		if(edges.size()!=6)
			fail("testEdgesFromVertex");
	}

	@Test
	public void testStarEdges() {
		DelaunayBrute dt = makeDelaunay();
		Iterator<EdgeHandle> edges = dt.starEdges(5).iterator();
		HashSet<Integer> neighbors = new HashSet<Integer>();
		while(edges.hasNext()){
			EdgeHandle e = edges.next();
			neighbors.add(e.first());
			neighbors.add(e.second());
		}
		if(!neighbors.contains(1) || !neighbors.contains(2) || !neighbors.contains(4) || !neighbors.contains(6) || !neighbors.contains(7) 
				|| !neighbors.contains(8) || neighbors.size() != 6 )
			fail("testStarEdges");
	}

	@Test
	public void testDualEdgePoints() {
		DelaunayBrute dt = makeDelaunay();
		List<Pnt> e = dt.dualPoints(new EdgeHandle(4, 5));
		Pnt mid = e.get(0).middle(e.get(1));
		if(mid.dist(dt.pointOfVertex(4),dt.pointOfVertex(5))>0.001)
			fail("testDualEdgePoints");
	}

	@Test
	public void testSurroundingTrianglesInteger() {
		DelaunayBrute dt = makeDelaunay();
		Iterator<TriangleHandle> triangles = dt.surroundingTriangles(4);
		int numTriangles = 0;
		while(triangles.hasNext()){++numTriangles; triangles.next();}
		if( numTriangles != 3) 
			fail("testSurroundingTrianglesInteger");
	}

	@Test
	public void testSurroundingTrianglesTriangleHandle() {
		DelaunayBrute dt = makeDelaunay();
		Iterator<TriangleHandle> triangles = dt.surroundingTriangles(new TriangleHandle(1,4,5));
		int size = 0;
		while(triangles.hasNext()) {++size; triangles.next();}
		if(size != 3) 
			fail("testSurroundingTrianglesTriangleHandle");
	}

	@Test
	public void testSurroundingTrianglesEdgeHandle() {
		DelaunayBrute dt = makeDelaunay();
		Iterator<TriangleHandle> triangles = dt.surroundingTriangles(new EdgeHandle(4,5));
		int numTriangles = 0;
		while(triangles.hasNext()){numTriangles++; triangles.next();}
		if(numTriangles!=2 ) 
			fail("testSurroundingTrianglesEdgeHandle");
	}

	@Test
	public void testPointOfVertex() {
		DelaunayBrute dt = makeDelaunay();
		Vector<Pnt> points = getPoints();
		for(int i = 0; i < points.size(); ++i){
			if(!dt.pointOfVertex(i).equals(points.get(i)))
				fail("testPointOfVertex");

		}
	}

	@Test(timeout=1000)
	public void testBorderVertex() {
		DelaunayBrute dt = makeDelaunay();
		Vector<Boolean> expectedResults = new Vector<>(dt.numVertices());
		expectedResults.add(true);
		expectedResults.add(true);
		expectedResults.add(true);
		expectedResults.add(true);
		expectedResults.add(true);
		expectedResults.add(false);
		expectedResults.add(true);
		expectedResults.add(true);
		expectedResults.add(true);
		expectedResults.add(true);
		for(int i = 0; i < dt.numVertices(); ++i)
			if(dt.isBorder(i) != expectedResults.get(i))
				fail("Wrong borderVertex result for v="+i);
	}

	@Test
	public void testBorderEdge() {
		DelaunayBrute dt = makeDelaunay();
		EdgeHandle eh = new EdgeHandle(0, 1);
		if(!dt.isBorder(eh)) 
			fail("Wrong borderEdge result");
		eh = new EdgeHandle(4, 7);
		if(!dt.isBorder(eh)) 
			fail("Wrong borderEdge result");
		eh = new EdgeHandle(1, 5);
		if(dt.isBorder(eh)) 
			fail("Wrong borderEdge result");
		eh = new EdgeHandle(7, 5);
		if(dt.isBorder(eh)) 
			fail("Wrong borderEdge result");
	}

	@Test
	public void testBorderTriangle() {
		DelaunayBrute dt = makeDelaunay();
		TriangleHandle th = new TriangleHandle(0,1,4);
		if(!dt.isBorder(th)) 
			fail("Wrong borderEdge result");
		
		th = new TriangleHandle(5,1,4);
		if(dt.isBorder(th)) 
			fail("Wrong borderEdge result");
		
	}





	@Test
	public void testPointsOfEdge() {
		//		fail("Not yet implemented");
	}

	@Test
	public void testPointsOfFaces() {
		//		fail("Not yet implemented");
	}

	@Test
	public void testPointsOfFace() {
		//		fail("Not yet implemented");
	}


	//	@Test
	//	public void testDualEdges() {
	//		DelaunayBrute dt = makeDelaunay();
	//		HashSet<Edge> edges = dt.dualEdges();
	//		if(edges.size() != 9)
	//			fail("testDualEdges");
	//	}

}
