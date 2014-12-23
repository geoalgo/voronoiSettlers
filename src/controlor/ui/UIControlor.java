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

import controlor.gamestate.LooseRessource;
import controlor.gamestate.PlayTurn;
import controlor.gamestate.ThiefSelect;

import delaunay.Pnt;

import model.card.Card;
import model.hexagonalTiling.SettlersEdge;
import model.hexagonalTiling.SettlersTile;
import model.hexagonalTiling.SettlersVertex;
import model.ressources.Ressources;
import player.Player;

public interface UIControlor {
	
	void setParentWindowMsg(String s);
	void appendParentWindowMsg(String s);
	
	// called by parent window
	void mousePressed(Pnt pnt);
	
	void nextTurnPressed();
	void tradePressed();
	void buyCardPressed();
	void playCardPressed();
	void updateView();
	
	void setActivePlayer(int player);
	void setInactivePlayer(int player);
	
	//launch UI to choose ressources to loose
	void selectRessourcesToLoose(Player p, LooseRessource currentState);
	
	//call back by the UI after choosing
	void looseRessources(Ressources ress);
	
	void chooseEnnemyToSteal(ThiefSelect stealState,Collection<Player> ennemies);
	void stealEnnemy(int playerToSteal);
	void selectCard(Card c);
}
