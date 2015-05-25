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


package controlor;


import java.util.Iterator;

import model.InitialRules;
import model.Model;
import model.Construction.City;
import model.Construction.Colony;
import model.Construction.VertexBuilding;
import model.card.Card;
import model.card.FreeRoad;
import model.card.Knight;
import model.card.CardKnightState;
import model.card.Monopole;
import model.card.MonopoleState;
import model.card.VictoryPoint;
import model.card.VictoryPointState;
import model.hexagonalTiling.SettlersEdge;
import model.hexagonalTiling.SettlersVertex;
import model.ressources.Ressource;
import model.ressources.Ressources;
import player.Player;
import controlor.gamestate.AskFirstColony;
import controlor.gamestate.GameState;
import controlor.gamestate.PlayTurn;
import controlor.ui.UIControlor;
import delaunay.Pnt;


/**
 * Controlor of the game.
 * @author David Salinas
 *
 */
public class GameControlor {
	Model model;
	int currentPlayer;
	GameState gs;
	UIControlor uicontrolor; //todo remove
	ISettlersServer server;

	public GameControlor(InitialRules rules) {
		model = new Model(rules);	
	}

	public Model getModel(){
		return model;
	}

	public void setUIControlor(UIControlor uicontrolor){
		this.uicontrolor = uicontrolor;
	}

	public UIControlor getUIControlor(){
		return uicontrolor;
	}

	public void setServerControlor(ISettlersServer server){
		this.server = server;
	}

	public ISettlersServer getServerControlor(){
		return server;
	}
	
	public void setState(GameState newState){
		this.gs = newState;
	}

	public GameState getSet(){
		return gs;
	}

	/**
	 * method to be called to start the game.
	 */
	void playLevel(){
		DB.msg("set gs to AskFirstColony");
		gs = new AskFirstColony(this,0);
	}

	public void endTurn(){
		if(gs instanceof PlayTurn){
			currentPlayer = (currentPlayer+1)%numPlayer();
			gs = new PlayTurn(this, currentPlayer);
			gs.run();
		}
	}

	public Player currentPlayer(){
		return model.getPlayer(currentPlayer);
	}
	
	public int currentPlayerNum(){
		return currentPlayer;
	}

	public Player getPlayer(int num){
		return model.getPlayer(num);
	}

	public int numPlayer(){
		return model.numPlayers();
	}

	public int drawRandomDices(){
		int firstDice = (int)(Math.random()*6+1);
		int secondDice = (int)(Math.random()*6+1);
		return firstDice + secondDice;
	}

	/**
	 * do the first harvest with the last settlement.
	 */
	public void initialHarvest(){
		Iterator<Player> players = model.getPlayerIterator();
		while(players.hasNext()){
			Player p = players.next();
			VertexBuilding firstBuilding = p.getBuilding(1);
			model.harvest(firstBuilding);
		}
	}

	public void harvest(int randomDices){
		model.harvest(randomDices);
	}
	
	public void steal(Player stealer,Player screwed){
		Ressources screwedRessources=screwed.getRessource();
		Ressource stealedRessource;
		if(screwedRessources.num()>0){
			int numberOfRessource=0;
			do{
				
				stealedRessource = screwedRessources.getRandomRessource();
				numberOfRessource=screwedRessources.num(stealedRessource);
			}while(numberOfRessource<1);
			screwed.getRessource().add(stealedRessource,-1);
			stealer.getRessource().add(stealedRessource,1);
			server.updateView();
		}
	}
	
	/**
	 * Give a random card to current player.
	 */
	public Card giveRandomCard() throws Exception{
		return model.giveRandomCard(currentPlayer());
	}
	
	public void releaseCard(Card c){
		model.releaseCard(c);
	}

	/**
	 * @throws Exception if building not allowed
	 */
	void addColony(SettlersVertex v,Player p) throws Exception{
		model.addColony(new Colony(v,p),v);
		server.updateView();
	}

	void addFreeColony(SettlersVertex v,Player p) throws Exception{
		model.addFreeColony(new Colony(v,p),v);
		server.updateView();
	}

	public void addColony(Pnt pnt,Player p) throws Exception{
		SettlersVertex v = model.board().locateClosestVertex(pnt);
		addColony(v, p);
	}

	public void addFreeColony(Pnt pnt,Player p) throws Exception{
		SettlersVertex v = model.board().locateClosestVertex(pnt);
		addFreeColony(v, p);
	}

	void addCity(SettlersVertex v,Player p) throws Exception{
		model.addCity(new City(v,p),v);
		server.updateView();
	}

	public void addCity(Pnt click,Player p) throws Exception{
		SettlersVertex v = model.board().locateClosestVertex(click);
		addCity(v, p);
	}

	public void addRoad(Pnt pnt,Player p) throws Exception{
		SettlersEdge e = model.board().locateClosestEdge(pnt);
		addRoad(e, p);
	}	

	public void addRoad(SettlersEdge e,Player p) throws Exception{
		model.addRoad(p,e);
		server.updateView();
	}
	
	public void addFreeRoad(Pnt pnt,Player p) throws Exception{
		SettlersEdge e = model.board().locateClosestEdge(pnt);
		addFreeRoad(e, p);
	}	

	public void addFreeRoad(SettlersEdge e,Player p) throws Exception{
		model.addFreeRoad(p,e);
		server.updateView();
	}

	public void addFirstRoad(Pnt pnt,Player p) throws Exception{
		SettlersEdge e = model.board().locateClosestEdge(pnt);
		addFirstRoad(e, p);
	}

	public void addSecondRoad(Pnt pnt,Player p) throws Exception{
		SettlersEdge e = model.board().locateClosestEdge(pnt);
		addSecondRoad(e, p);
	}


	void addFirstRoad(SettlersEdge e,Player p) throws Exception{
		Colony c = (Colony) p.getBuilding(0);
		model.addFreeRoadNearColony(p,e,c);
		server.updateView();
	}

	void addSecondRoad(SettlersEdge e,Player p) throws Exception{
		Colony c = (Colony) p.getBuilding(1);
		model.addFreeRoadNearColony(p,e,c);
		server.updateView();
	}
	
	
//	public void selectCard(Card c){
//		server.appendMessage(currentPlayer().getName()+" plays a "+c,currentPlayerNum());
//		GameState stateToRestore = getSet();
//		DB.msg("state to restore:"+stateToRestore);
//		applyCard(c);
//		currentPlayer().removeCard(c);
//		releaseCard(c);
//	}

//	//xxx should be on the view
//	public void applyCard(Card card){
//		GameState stateToRestore = getSet();
//		if(card instanceof Monopole){
//			//aaa do static cleaner
//			new MonopoleState(this, stateToRestore, (Monopole)card);
//		}
//		if(card instanceof Knight){
//			//aaa do static cleaner
//			new KnightState(this, stateToRestore, (Knight)card);
//		}
//		if(card instanceof VictoryPoint){
//			new VictoryPointState(this, stateToRestore, card);
//		}
//		if(card instanceof FreeRoad){
//			card.apply(this,null);
//		}
//	}


}
