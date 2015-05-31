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
import model.ressources.Ressources;
import controlor.DB;
import controlor.GameController;
import delaunay.Pnt;

public class LooseRessource extends GameState{
	int currentPlayerLoosingRessource;

	// the state when player throw the 7
	// must be restored when the player 
	// picks his ressources to throw
	PlayTurn stateBefore;

	public LooseRessource(GameController gc,
			PlayTurn stateBefore){
		super(gc);
		DB.msg("loose state");
		this.stateBefore = stateBefore;
		currentPlayerLoosingRessource = 0;
	}
	
	@Override
	public void run(){
		looseRessourceIfFull();
	}
	
	
	@Override
	public void click(Pnt click) {
		DB.msg("click "+this);
		gc.getServerControlor().appendMessage("Please select ressource to throw before.");
	}
	
	private void looseRessourceIfFull(){
		Player p = gc.getPlayer(currentPlayerLoosingRessource);
		if(p.numRessources()>7){
			String msg = "Player "+p.getName()+" loose half his ressources";
			gc.getServerControlor().appendMessage(msg);
			DB.msg(msg);
			gc.getUIControlor().selectRessourcesToLoose(
					gc.getPlayer(currentPlayerLoosingRessource),
					this);
		}
		else nextPlayer();
	}
	
	private void nextPlayer(){
		++currentPlayerLoosingRessource;
		if(currentPlayerLoosingRessource<gc.getNumPlayer())
			looseRessourceIfFull();
		else {
			gc.setState(new ThiefSelect(gc, stateBefore, stateBefore.currentPlayer));
//			ThiefSelect ts = new ThiefSelect(gc, stateBefore, stateBefore.currentPlayer);
		}
	}
	
	//callback by gui gc.getUIControlor().selectRessourcesToLoose
	@Override
	public void apply(Object o) {
		Ressources ress = (Ressources)o;
		DB.msg("num pl:"+currentPlayerLoosingRessource);
		gc.getPlayer(currentPlayerLoosingRessource).getRessource().remove(ress);
		gc.getServerControlor().updateView();
		nextPlayer();
	}
	
	@Override
	public String toString(){
		return "LooseRessource";
	}

	
}
