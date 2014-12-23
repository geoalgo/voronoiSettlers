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

package controlor.gamestate;

import controlor.GameControlor;
import delaunay.Pnt;

public class AskSecondColony extends GameState{
	int currentPlayer;

	AskSecondColony(GameControlor gc,int currentPlayer){
		super(gc);
		gc.getUIControlor().setActivePlayer(currentPlayer);		
		String msg = "Player "+gc.getPlayer(currentPlayer).getName()+
				", please specify your placement for your second colony";
		gc.getUIControlor().setParentWindowMsg(msg);
		this.currentPlayer = currentPlayer;
	}

	@Override
	public void click(Pnt click) {
		try {
			gc.addFreeColony(click, gc.getPlayer(currentPlayer));
			gc.setSet(new AskSecondRoad(gc, currentPlayer));
		} catch (Exception e) {
			System.out.println("Invalid colony placement");
		}		
	}

	@Override
	public void apply(Object o) {
		// TODO Auto-generated method stub
		
	}

}
