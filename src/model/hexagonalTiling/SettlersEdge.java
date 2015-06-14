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
import delaunayBrute.EdgeHandle;
import model.Construction.Building;
import model.Construction.Road;
import model.hexagonalTiling.harbor.Harbor;
import player.Player;


public class SettlersEdge implements java.io.Serializable {
	/**
	 * (Eventual) building on the tile and (eventual) player who owns it. 
	 */
	Building building;
	Harbor harbor;
	
	/**
	 * coordinates of the edge
	 */
	Pnt p1;
	Pnt p2;
	
	SettlersVertex v1;
	SettlersVertex v2;
	
	EdgeHandle edgeHandle;
	
	
	SettlersEdge(SettlersVertex v1,SettlersVertex v2,EdgeHandle edgeHandle){
		this.v1 = v1;
		this.v2 = v2;
		this.edgeHandle = edgeHandle; 
		harbor = null;
	}
	
	public SettlersVertex v1(){ return v1;}
	public SettlersVertex v2(){ return v2;}
	
	public Pnt p1(){return v1.getPosition();}
	public Pnt p2(){return v2.getPosition();}
	
	public EdgeHandle edgeHandle(){return this.edgeHandle;}

	
	public Harbor harbor(){
		return harbor;
	}
	
	public void setHarbor(Harbor h){
		harbor = h;
	}
	
	public boolean hasHarbor(){
		return harbor!=null;
	}
	
	@Override
	public boolean equals(Object o){
		SettlersEdge oEdge = (SettlersEdge)o;
		return edgeHandle() == oEdge.edgeHandle();
	}
	
	/**
	 * @param building to set on the tile
	 * @param player that owns it
	 * @return true if nothing was already there
	 * @throws Exception 
	 */
	public void setBuilding(Building building) 
	{
		if(!hasBuilding())
			this.building = building ;
	}	

	public boolean hasBuilding(){
		return building != null;
	}
	
	public Building getBuilding(){
		return building;
	}
	
	public String toString(){
		String res = "";
		res+="adj tiles:";
		return res;
	}
	
}
