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

package controlor.isValidPlacement.vertexBuilding;

import java.util.List;

import model.BoardPlacementException;
import model.Construction.Building;
import model.hexagonalTiling.SettlersVertex;

public class ConstrainVertexBuildingTwoBlockDistance extends
		ConstrainVertexBuildingDecorator {

	public ConstrainVertexBuildingTwoBlockDistance(ConstrainVertexBuilding v) {
		super(v);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void isValid(Building b, SettlersVertex position) 
	throws BoardPlacementException{
		if(!checkFreeNeighbors(position))
			throw new BoardPlacementException("Cant build too close from other building");
		nextTest(b, position);
	}
	
	/**
	 * @param position
	 * @return true if no player has a building at one distance
	 * (useful to see if a building is allowed in the rule).
	 */
	private boolean checkFreeNeighbors(SettlersVertex position){
		List<SettlersVertex> tileNeighbors = model.board().vertexNeighbors(position);
		for(SettlersVertex v : tileNeighbors){
			if(v.hasBuilding()) return false;
		}
		return true; // and neighbors free
	}

}
