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

import player.Player;
import model.Construction.Building;
import model.Construction.Colony;
import model.card.CardState;
import model.card.FreeRoad;
import controlor.GameControlor;
import controlor.ISettlersServer;
import delaunay.Pnt;

public class AskFirstFreeRoad extends CardState {
	int currentPlayer;
	GameState stateToBeRestored;

	public AskFirstFreeRoad(GameControlor gc,FreeRoad card,GameState stateToBeRestored){
		super(gc,card);
		this.stateToBeRestored = stateToBeRestored;
		this.currentPlayer = gc.currentPlayerNum();
		String msg = "Player "+gc.getPlayer(currentPlayer).getName()+
				", please specify your placement for your first free road";
		gc.getServerControlor().appendMessage(msg,currentPlayer);
	}

	@Override
	public void click(Pnt click) {
		try {
			gc.addFreeRoad(click, gc.getPlayer(currentPlayer));
			gc.setState(new AskSecondFreeRoad(gc,currentPlayer,stateToBeRestored));
		} catch (Exception e) {
			System.out.println("Invalid first free road placement");
		}

	}

	@Override
	public void apply(Object o) {
	}

}
