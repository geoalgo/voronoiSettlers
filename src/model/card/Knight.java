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

package model.card;

import controlor.GameController;
import controlor.ISettlersServer;
import controlor.gamestate.GameState;
import controlor.gamestate.ThiefSelect;
import player.Player;

public class Knight extends Card {

	public Knight() {
		super("Knight");
	}

	@Override
	public void apply(ISettlersServer gc) {
//		//1- UI ask stealer position
//		//2- increase knight number
//		//3- update biggest army holder
//		KnightSelect uiThief = new KnightSelect(gc, this,gc.getCurrentPlayerNum());
//		
//		p.addKnight();
//		boolean armyChanged = gc.getModel().updateBiggestArmy();
//		if(armyChanged)
//			gc.appendMessage(gc.getCurrentPlayer().getName()+ " now has the biggest army.");
//		gc.updateView();
	}

}
