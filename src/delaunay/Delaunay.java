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


package delaunay;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import delaunayBrute.EdgeHandle;
import delaunayBrute.TriangleHandle;

/**
 * Minimal set of operations required for settlers.
 * @author David Salinas
 *
 * @param <V> Class of vertices
 * @param <E> Class of edges
 * @param <F> Class of faces (triangles)
 */
public interface Delaunay<V,E,F> {
	Iterator<V> vertices();
	Iterator<E> edges();
	Iterator<F> faces();
	
	int numVertices();
	int numEdges();
	int numInteriorEdges();
	int numFaces();
	
	V closestVertex(Pnt p);
	E closestEdge(Pnt p);
	F closestFace(Pnt p);
	
	Iterator<V> verticesOfTriangle(F f);
	Iterator<V> neighborsOfVertex(V v);
	
	Iterator<F> surroundingTriangles(V v);
	
	Pnt pointOfVertex(V v);
	Iterator<Pnt> pointsOfEdge(E e);
	Iterator<Pnt> pointsOfFaces(F f);
	
	
	HashSet<Edge> dualEdges();
//	Edge dualEdgePoints(EdgeHandle e);
	List<Pnt> dualPoints(EdgeHandle e);

	Triangle triangle(TriangleHandle f);
	Iterator<TriangleHandle> surroundingTriangles(EdgeHandle e);
	Vector<Pnt> pointsOfFace(TriangleHandle f);
	Iterator<TriangleHandle> surroundingTriangles(TriangleHandle f);
	
	Iterator<EdgeHandle> interiorEdges();
	EdgeHandle closestDualEdge(Pnt p);
	Iterator<EdgeHandle> edges(Integer v);
	List<EdgeHandle> starEdges(Integer v);
	
	/**
	 * @param v
	 * @return true if v is on the convex hull
	 */
	boolean isBorder(int v);
	boolean isBorder(EdgeHandle eh);
	boolean isBorder(TriangleHandle th);
}
