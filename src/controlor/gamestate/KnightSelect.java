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

import java.util.LinkedList;
import java.util.TreeSet;

import player.Player;

import model.card.Card;
import model.card.CardState;
import model.hexagonalTiling.SettlersEdge;
import model.hexagonalTiling.SettlersTile;
import model.hexagonalTiling.SettlersVertex;
import controlor.DB;
import controlor.GameControlor;
import controlor.ISettlersServer;
import controlor.SettlersServer;
import delaunay.Pnt;

public class KnightSelect extends CardState {
	SettlersTile thiefOldPosition;
	SettlersTile selectedTile;

	int currentPlayer;

	public KnightSelect(GameControlor gc,
			Card card,
			int currentPlayer) {
		super(gc,card);
		this.thiefOldPosition = gc.getModel().getThiefPosition();
		selectedTile = null;
		this.currentPlayer = currentPlayer;
		gc.getServerControlor().appendMessage(gc.getPlayer(currentPlayer).getName()+" please select a new place for the thief");
	}

	@Override
	public void click(Pnt click) {
		DB.msg("theif select click");
		SettlersTile closestTile = gc.getModel().board().locateClosestTile(click);
		if(closestTile!=null) 
			click(closestTile,click);
	}

	private void click(SettlersTile selectedTile,Pnt click){
		if(selectedTile == thiefOldPosition){
			gc.getServerControlor().appendMessage("Cannot place the thief to the same place!");
		}
		else{
			this.selectedTile = selectedTile;
			gc.getModel().setThiefPosition(selectedTile);
			stealEnnemies(selectedTile);
			gc.getServerControlor().updateView();
		}
	}

	private void stealEnnemies(SettlersTile selectedTile){
		TreeSet<Player> ennemiesAroundTile = new TreeSet<Player>();
		for(SettlersVertex v : gc.getModel().board().vertexNeighbors(selectedTile)){
			if(v.hasBuilding())
				ennemiesAroundTile.add(v.getBuilding().getPlayer());
		}
		ennemiesAroundTile.remove(gc.getPlayer(currentPlayer));
		if(!ennemiesAroundTile.isEmpty()){
			if(ennemiesAroundTile.size()==1)
				apply(ennemiesAroundTile.first().getNum());
//			else
//				gc.getUIControlor().chooseEnnemyToSteal(this, ennemiesAroundTile);
		}
	}

	@Override
	public void apply(Object o) {
		int playerToSteal = (int) o;
		Player stealer = gc.getPlayer(currentPlayer);
		Player screwed = gc.getPlayer(playerToSteal);
		gc.getServerControlor().stealRandomEnnemyRessource(playerToSteal);
		gc.getServerControlor().appendMessage(stealer.getName()+" stole "+screwed.getName());
	}

	@Override
	public String toString(){
		return "ThiefSelect-"+currentPlayer;
	}


}
