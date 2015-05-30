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

package controlor.ui;


import java.awt.event.MouseEvent;
import java.util.Collection;
import java.util.List;

import com.sun.corba.se.spi.ior.MakeImmutable;

import model.card.Card;
import model.card.CardState;
import model.card.FreeRoad;
import model.card.Knight;
import model.card.CardKnightState;
import model.card.Monopole;
import model.card.MonopoleState;
import model.card.VictoryPoint;
import model.ressources.Ressource;
import model.ressources.Ressources;
import player.Player;
import controlor.DB;
import controlor.GameController;
import controlor.SettlersServer;
import controlor.gamestate.GameState;
import controlor.gamestate.LooseRessource;
import controlor.gamestate.PlayTurn;
import controlor.gamestate.ThiefSelect;
import delaunay.Pnt;


public class UIWindow implements UIControlor {

	SettlersServer parentWindow;
	GameController gc;

	// state that is waiting a callback with done()
	GameState callBackState;
	UITrade uiTrade;
	UISelectCard uiSelectCard = null;

	public UIWindow(SettlersServer parentWindow,
			GameController gc ){
		this.parentWindow = parentWindow;
		this.gc = gc;
		callBackState = null;
		uiTrade = null;
	}

	@Override
	public void selectRessourcesToLoose(
			Player p,LooseRessource currentState){
		callBackState = currentState;
		UILooseRessource.createAndShowGUI(this,p);
	}

	@Override
	public void looseRessources(Ressources ress){
		//move to done method
		callBackState.apply(ress);
	}

	@Override
	public void stealEnnemy(int playerToSteal) {
		Player stealer = gc.getCurrentPlayer();
		Player screwed = gc.getPlayer(playerToSteal);
		gc.steal(stealer, screwed);
		gc.getServerControlor().appendMessage(
				stealer.getName()+" stole "+gc.getPlayer(playerToSteal).getName());
		gc.setState(callBackState);
	}

	@Override
	public void setActivePlayer(int player) {
//		parentWindow.setActive(player);
	}

	@Override
	public void setInactivePlayer(int player) {
//		parentWindow.setInactive(player);
	}

	
}
