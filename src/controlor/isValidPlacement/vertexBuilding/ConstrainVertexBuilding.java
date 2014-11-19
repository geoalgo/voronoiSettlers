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

import model.BoardPlacementException;
import model.Model;
import model.Construction.Building;
import model.hexagonalTiling.SettlersVertex;

public abstract class ConstrainVertexBuilding {

	protected Model model;
	
	ConstrainVertexBuilding(Model model){
		this.model = model;
	}
	
	public abstract void isValid(Building b,SettlersVertex position) throws BoardPlacementException;

	
	public static ConstrainVertexBuilding makeFirstColonyConstrains(Model m){
		ConstrainVertexBuilding constrain = new ConstrainVertexBuildingEmptySpot(m);
		constrain = new ConstrainVertexBuildingTwoBlockDistance(constrain);
		return constrain;
	}
	
	public static ConstrainVertexBuilding makeSecondColonyConstrains(Model m){
		return makeFirstColonyConstrains(m);
	}
	
	public static ConstrainVertexBuilding makeColonyConstrains(Model m){
		ConstrainVertexBuilding constrain = makeFirstColonyConstrains(m); 
		constrain = new ConstrainVertexBuildingRessource(constrain);
		return constrain;
	}
	
	public static ConstrainVertexBuilding makeCityConstrains(Model m){
		ConstrainVertexBuilding constrain = new ConstrainVertexBuildingColonySpot(m);
		constrain = new ConstrainVertexBuildingRessource(constrain);
		return constrain;
	}
	
}
