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


import java.util.LinkedList;
import java.util.List;

import delaunay.Pnt;
import delaunayBrute.TriangleHandle;
import player.Player;
import model.Construction.Building;
import model.ressources.Ressource;

/**
 * A tile that have a ressource and a number.
 * @author David Salinas
 *
 */
public class SettlersTile{

	/**
	 * Sum of two dices, should be into the range 2-12
	 */
	int diceNumber;
	
	/**
	 * ressource present on the tile
	 */
	Ressource ressource;
	
	int center;

	
	
	/**
	 * @param diceSum should be in 2-12
	 * @param ressource
	 */
	public SettlersTile(int diceSum,Ressource ressource,int center) {
		this.diceNumber = diceSum;
		this.ressource = ressource;
		this.center = center;
	}


	public int center(){
		return center;
	}
	
	public int diceNumber(){
		return diceNumber;
	}

	public Ressource ressource(){
		return this.ressource;
	}
	
	@Override
	public String toString(){
		String res = "Tile "+center+" of "+ressource+" dice:"+diceNumber;
		return res;
	}
//	/**
//	 * @param building to set on the tile
//	 * @param player that owns it
//	 * @return true if nothing was already there
//	 */
//	public boolean setBuilding(Building building,Player player){
//		if(building!=null || player != null ) return false;
//		else{
//			this.building = building ;
//			this.player = player ;
//			return true;
//		}
//	}

}
