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

import delaunay.Pnt;
import delaunayBrute.TriangleHandle;
import model.Construction.Building;
import player.Player;

public class SettlersVertex {
	/**
	 * (Eventual) building on the tile. 
	 */
	Building building;
	Pnt position;
	TriangleHandle triangle;
	
	/**
	 * @param triangle dual Delaunay triangle which defines the vertex.
	 * @param vertex
	 */
	SettlersVertex(Pnt position,TriangleHandle triangle){
		this.position = position;
		this.triangle = triangle;
		building = null ;
	}
	
	public Pnt getPosition(){
		return position;
	}
	
	public TriangleHandle triangle(){
		return triangle;
	}
	
	public boolean hasBuilding(){
		return building != null;
	}
	
	public Building getBuilding(){
		if(hasBuilding()) return building;
		else return null;
	}
	
	/**
	 * @param building to set on the tile
	 * @param player that owns it
	 * @return true if nothing was already there
	 */
	public void setBuilding(Building building){
		this.building = building ;
	}
}
