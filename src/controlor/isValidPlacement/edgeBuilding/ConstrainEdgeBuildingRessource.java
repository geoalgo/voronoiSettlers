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

import model.BoardPlacementException;
import model.Construction.Building;
import model.hexagonalTiling.SettlersEdge;
import model.hexagonalTiling.SettlersVertex;

public class ConstrainEdgeBuildingRessource extends ConstrainEdgeBuildingDecorator {

	public ConstrainEdgeBuildingRessource(ConstrainEdgeBuilding v) {
		super(v);
	}

	@Override
	public void isValid(Building b, SettlersEdge position) 
			throws BoardPlacementException{
		if(!b.isEnough(b.getPlayer().getRessource()))
				throw new BoardPlacementException("Not enough ressource");
		nextTest(b, position);
	}

}
