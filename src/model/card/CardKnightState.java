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

import java.util.TreeSet;

import player.Player;
import model.hexagonalTiling.SettlersTile;
import model.hexagonalTiling.SettlersVertex;
import model.ressources.Ressource;
import controlor.DB;
import controlor.GameControlor;
import controlor.ISettlersServer;
import controlor.gamestate.GameState;
import controlor.ui.UIChoosePlayerToSteal;
import controlor.ui.UISelectRessourceMonopole;
import controlor.ui.UIChoosePlayerToSteal;
import delaunay.Pnt;


/**
 * This state will :
 * 1- ask stealer position
 * 2- ask which player will be stolen
 * 3- increase knight number (for largest army)
 * 4- update biggest army holder
 * 5- restore previous game state
 * @author david
 */
public class CardKnightState extends CardState {

	SettlersTile selectedTile = null;
	GameState stateToBeRestored;


	public CardKnightState(GameControlor gc, Knight card,GameState stateToBeRestored) {
		super(gc, card);
		this.stateToBeRestored = stateToBeRestored;
	}

	@Override
	public void click(Pnt click) {
		DB.msg("knight click");
		SettlersTile closestTile = gc.getModel().board().locateClosestTile(click);
		if(closestTile!=null) 
			click(closestTile,click);
	}

	private void click(SettlersTile selectedTile,Pnt click){
		if(selectedTile == gc.getModel().getThiefPosition())
			gc.getServerControlor().appendMessage("Cannot place the thief to the same place!");
		else{
			this.selectedTile = selectedTile;
			gc.getModel().setThiefPosition(selectedTile);
			gc.getServerControlor().updateView();
			stealEnnemies(selectedTile);
		}
	}

	private void stealEnnemies(SettlersTile selectedTile){
		TreeSet<Player> ennemiesAroundTile = getNeighborsEnnemies();
		if(ennemiesAroundTile.isEmpty()) end();
		if(ennemiesAroundTile.size()==1){
			apply(ennemiesAroundTile.first().getNum());
			end();
		}
		else 
			//choose the ennemy to steal from the ennemies on the clicked position
			// UIChoosePlayerToSteal calls apply(player) when chosen
			gc.setState(new UIChoosePlayerToSteal(gc.getState(),gc,ennemiesAroundTile));
	}

	private TreeSet<Player> getNeighborsEnnemies(){
		TreeSet<Player> ennemiesAroundTile = new TreeSet<Player>();
		for(SettlersVertex v : gc.getModel().board().vertexNeighbors(selectedTile)){
			if(v.hasBuilding())
				ennemiesAroundTile.add(v.getBuilding().getPlayer());
		}
		ennemiesAroundTile.remove(gc.currentPlayer());
		return ennemiesAroundTile;
	}
	
	private void steal(int playerToSteal){
		gc.getUIControlor().stealEnnemy(playerToSteal);
	}

	@Override
	public void apply(Object o) {
		int playerToSteal = (int)o;
		steal(playerToSteal);
		end();
	}
	
	// come back to original state (a play turn state)
	private void end(){
		updateLargestArmy();
		gc.setState(stateToBeRestored);
	}

	/**
	 * updates the largest army holder with the new knight card of the player.
	 */
	private void updateLargestArmy(){
		gc.currentPlayer().addKnight();
		boolean armyChanged = gc.getModel().updateBiggestArmy();
		if(armyChanged) 
			gc.getServerControlor().appendMessage(gc.currentPlayer().getName()+ " now has the biggest army.");
		gc.getServerControlor().updateView();
	}

	
}
