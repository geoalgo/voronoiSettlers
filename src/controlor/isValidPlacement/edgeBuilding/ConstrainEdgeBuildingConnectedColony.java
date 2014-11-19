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

package controlor.isValidPlacement.edgeBuilding;

import player.Player;
import model.BoardPlacementException;
import model.Construction.Building;
import model.Construction.Colony;
import model.hexagonalTiling.SettlersEdge;
import model.hexagonalTiling.SettlersVertex;

public class ConstrainEdgeBuildingConnectedColony extends ConstrainEdgeBuildingDecorator {

	Colony colony;
	
	public ConstrainEdgeBuildingConnectedColony(ConstrainEdgeBuilding v,Colony colony) {
		super(v);
		this.colony = colony;
	}

	@Override
	public void isValid(Building b, SettlersEdge position) 
			throws BoardPlacementException{
		if(!isConnectedToColony(b,position))
			throw new BoardPlacementException("Not connected to the good colony");
		nextTest(b, position);
	}
	
	private boolean isConnectedToColony(Building building,SettlersEdge edge){
		//check that one neighbor is a settlement or a road
		if(vertexHasPlayerBuilding(model.board().first(edge),building.getPlayer())){
			return true;
		}
		if(vertexHasPlayerBuilding(model.board().second(edge),building.getPlayer())){
			return true;
		}
		return false;
	}
	
	private boolean vertexHasPlayerBuilding(SettlersVertex vertex, Player p){
		if(!vertex.hasBuilding()) return false;
		Building b = vertex.getBuilding();
		return b.equals(colony); 
	}
	
}
