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

import controlor.isValidPlacement.vertexBuilding.ConstrainVertexBuilding;
import controlor.isValidPlacement.vertexBuilding.ConstrainVertexBuildingEmptySpot;
import controlor.isValidPlacement.vertexBuilding.ConstrainVertexBuildingTwoBlockDistance;
import model.BoardPlacementException;
import model.Model;
import model.Construction.Building;
import model.Construction.Colony;
import model.hexagonalTiling.SettlersEdge;
import model.hexagonalTiling.SettlersVertex;

public abstract class ConstrainEdgeBuilding {

	protected Model model;
	
	ConstrainEdgeBuilding(Model model){
		this.model = model;
	}
	
	public abstract void isValid(Building b,SettlersEdge position) throws BoardPlacementException;
	
	public static ConstrainEdgeBuilding makeFirstRoadConstrains(Model m,Colony colony){
		ConstrainEdgeBuilding constrain = new ConstrainEdgeBuildingEmptySpot(m);
		constrain = new ConstrainEdgeBuildingConnectedColony(constrain,colony);
		return constrain;
	}
	
	public static ConstrainEdgeBuilding makeRoadConstrains(Model m){
		ConstrainEdgeBuilding constrain = new ConstrainEdgeBuildingEmptySpot(m);
		constrain = new ConstrainEdgeBuildingRessource(constrain);
		constrain = new ConstrainEdgeBuildingConnected(constrain);
		return constrain;
	}

}
