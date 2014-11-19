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

import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

import delaunay.Pnt;
import model.InitialRules;

import delaunayBrute.*;

public interface BoardTiles {


	/**
	 * Init the board.
	 * @param rules
	 */
	void init(InitialRules rules);


	SettlersTile locateClosestTile(Pnt request);
	Pnt getPosition(SettlersTile tile);


	SettlersEdge locateClosestEdge(Pnt request);
	EdgeHandle getEdgeHandle(SettlersEdge edge);

	SettlersVertex locateClosestVertex(Pnt request);

	
	
	// needed for the view
	int numEdges();
	int numTiles();

	Iterator<SettlersVertex> vertices();
	Iterator<SettlersEdge> edges();
	Iterator<SettlersTile> tiles();
//	Iterator<SettlersVertex> borderPoints();
	Iterator<SettlersEdge> borderEdges();


	List<SettlersTile> tilesNeighbors(SettlersTile tile);
	List<SettlersTile> tilesNeighbors(SettlersVertex vertex);

	List<SettlersVertex> vertexNeighbors(SettlersTile tile);
	List<SettlersVertex> vertexNeighbors(SettlersVertex vertex);
	List<SettlersEdge> edgeNeighbors(SettlersVertex vertex);
//	List<Pnt> pointsAroundTile(SettlersTile tile);

	SettlersVertex first(SettlersEdge edge);
	SettlersVertex second(SettlersEdge edge);


	boolean isBorder(SettlersTile tile);
	boolean isBorder(SettlersEdge edge);
	boolean isBorder(SettlersVertex v);


	SettlersVertex getVertex(TriangleHandle th);
	SettlersEdge getEdge(EdgeHandle eh);
	SettlersTile getTile(int v);

	
	
}
