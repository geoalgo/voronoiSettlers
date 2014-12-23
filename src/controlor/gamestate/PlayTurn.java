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
import model.BuildException;
import model.NotEnoughRessourceException;
import model.hexagonalTiling.SettlersEdge;
import model.hexagonalTiling.SettlersTile;
import model.hexagonalTiling.SettlersVertex;
import controlor.DB;
import controlor.GameControlor;
import delaunay.Pnt;

public class PlayTurn extends GameState{
	int currentPlayer;

	public PlayTurn(GameControlor gc,int currentPlayer){
		super(gc);
		this.currentPlayer = currentPlayer;
		gc.getUIControlor().setActivePlayer(currentPlayer);		
	}
	
	@Override
	public void run(){
		int randomDices = drawDices();
		dealDices(randomDices);
		gc.getUIControlor().updateView();
	}

	int drawDices(){
		int randomDices = gc.drawRandomDices();
		String msg1 = "Draw "+randomDices;
		String msg2 = "Player "+gc.getPlayer(currentPlayer).getName()+
				", it's your turn do your move and quick";
		gc.getUIControlor().setParentWindowMsg(msg1+"\n"+msg2);
		return randomDices;
	}

	void dealDices(int randomDices){
		if(randomDices!=7)
			gc.harvest(randomDices);
		else{
			//changes the current state
			DB.msg("set loose ressource");
			gc.setSet(new LooseRessource(gc,this));
			gc.getSet().run();
			//			new LooseRessource(gc,this);
//			gc.setSet(new ThiefSelect(gc,this,currentPlayer));
			DB.msg("gc set:"+gc.getSet());
		}
	}

//	public void selectRessourcesToLoose(){
//
//	}


	@Override
	public void click(Pnt click) {
		DB.msg("click "+this);
		SettlersVertex closestVertex = gc.getModel().board().locateClosestVertex(click);
		SettlersEdge closestEdge = gc.getModel().board().locateClosestEdge(click);
		SettlersTile closestTile = gc.getModel().board().locateClosestTile(click);
		double distVertex = closestVertex.getPosition().dist(click);
		double distEdge = click.dist(closestEdge.p1(),closestEdge.p2());

		double minDist = 0.02;
		if(distVertex< minDist){
			click(closestVertex,click);
		}
		else{
			if(distEdge < minDist)
				click(closestEdge,click);
			else
				if(closestTile!=null) 
					click(closestTile,click);
		}

		DB.msg("dist v:"+distVertex);
		DB.msg("dist e:"+distEdge);
	}

	private void click(SettlersVertex v,Pnt click){
		if(!v.hasBuilding()) {
			DB.msg("no building");
			buildColony(v,click);
		}
		else{
			DB.msg("building there");
			buildCity(v,click);
		}
	}

	private void buildColony(SettlersVertex v,Pnt click){
		try {
			gc.addColony(click, gc.getPlayer(currentPlayer));
			String msg = 
					gc.getPlayer(currentPlayer).getName()+" build a colony ";
			gc.getUIControlor().appendParentWindowMsg(msg);
		} 
		catch (BuildException e){
			gc.getUIControlor().appendParentWindowMsg(e.getMsg());
		}
		catch (Exception e) {
			System.out.println("Invalid colony placement");
		}
	}

	private void buildCity(SettlersVertex v,Pnt click){
		try {
			gc.addCity(click, gc.getPlayer(currentPlayer));
			String msg = 
					gc.getPlayer(currentPlayer).getName()+" build a city";
			gc.getUIControlor().appendParentWindowMsg(msg);
		} 
		catch (BuildException e){
			gc.getUIControlor().appendParentWindowMsg(e.getMsg());
		}
		catch (Exception e) {
			System.out.println("Invalid city placement");
		}
	}

	private void click(SettlersEdge e,Pnt click){
		try {
			gc.addRoad(e, gc.getPlayer(currentPlayer));
			String msg = 
					gc.getPlayer(currentPlayer).getName()+" build a road ";
			gc.getUIControlor().appendParentWindowMsg(msg);
		}
		catch (BuildException ex){
			gc.getUIControlor().appendParentWindowMsg(ex.getMsg());
		}
		catch (Exception ex) {
			System.out.println("Invalid road placement");
		}
	}

	private void click(SettlersTile t,Pnt click){

	}

	@Override
	public void apply(Object o) {
		// todo do next turn here
		
	}
	
	@Override
	public String toString(){
		return "PlayTurn-"+currentPlayer;
	}

}
