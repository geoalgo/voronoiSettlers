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
import java.util.TreeSet;

import model.InitialRules;
import model.Model;
import model.NotEnoughRessourceException;
import model.Construction.City;
import model.Construction.Colony;
import model.Construction.VertexBuilding;
import model.card.Card;
import model.card.CardState;
import model.card.FreeRoad;
import model.card.Knight;
import model.card.CardKnightState;
import model.card.Monopole;
import model.card.MonopoleState;
import model.card.VictoryPoint;
import model.hexagonalTiling.SettlersEdge;
import model.hexagonalTiling.SettlersTile;
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
public class GameController implements IGameController {
	Model model;
	int currentPlayer;
	GameState gs;
	UIControlor uicontroller; //todo remove
	ISettlersServer server;

	public GameController(InitialRules rules) {
		model = new Model(rules);	
	}

	public Model getModel(){
		return model;
	}

	public void setUIControlor(UIControlor uicontrolor){
		this.uicontroller = uicontrolor;
	}

	public UIControlor getUIControlor(){
		return uicontroller;
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

	public GameState getState(){
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
		currentPlayer = (currentPlayer+1)%numPlayer();
	}

	@Override
	public Player getCurrentPlayer(){
		return model.getPlayer(currentPlayer);
	}
	
	@Override
	public int currentPlayerNum(){
		return currentPlayer;
	}

	public Player getPlayer(int num){
		return model.getPlayer(num);
	}

	public int numPlayer(){
		return model.numPlayers();
	}

	@Override
	public int drawRandomDices(){
		int firstDice = (int)(Math.random()*6+1);
		int secondDice = (int)(Math.random()*6+1);
		return firstDice + secondDice;
	}

	/**
	 * do the first harvest with the last settlement.
	 */
	@Override
	public void initialHarvest(){
		Iterator<Player> players = model.getPlayerIterator();
		while(players.hasNext()){
			Player p = players.next();
			VertexBuilding firstBuilding = p.getBuilding(1);
			model.harvest(firstBuilding);
		}
	}

	@Override
	public void harvest(int randomDices){
		model.harvest(randomDices);
	}
	
	@Override
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
	
	@Override
	public Card buyCard() throws Exception {
		Ressources cardCost = new Ressources();
		cardCost.addCrop(1);
		cardCost.addStone(1);
		cardCost.addSheep(1);

		boolean enoughRessource = getCurrentPlayer().getRessource().greaterThan(cardCost);
		if(!enoughRessource)
			throw new NotEnoughRessourceException();
		else{
			Card res = giveRandomCard();
			getCurrentPlayer().getRessource().remove(cardCost);
			getCurrentPlayer().addCard(res);
			return res;
		}
	}
	
	@Override
	public void consumeCard(Card c) throws Exception {
		if(!getCurrentPlayer().removeCard(c))
			throw new Exception("Card not present");
	}
	
	/**
	 * Give a random card to current player.
	 */
	private Card giveRandomCard() throws Exception{
		return model.giveRandomCard(getCurrentPlayer());
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

	@Override
	public void resetRessources() {
		for(int i = 0 ; i < numPlayer(); ++i){
			getPlayer(i).getRessource().setToZero();
		}
	}

	@Override
	public void addRessourcesToCurrentPlayer() {
		for(Ressource r : Ressource.allRessources())
			getCurrentPlayer().getRessource().add(r, 1);
	}

	@Override
	public SettlersTile locateClosestTile(Pnt p) {
		return model.board().locateClosestTile(p);
	}

	@Override
	public SettlersTile getThiefPosition() {
		return model.getThiefPosition();
	}

	@Override
	public void setThiefPosition(SettlersTile selectedTile) {
		model.setThiefPosition(selectedTile);
	}

	@Override
	public TreeSet<Player> getNeighborsEnnemies(SettlersTile selectedTile) {
		TreeSet<Player> ennemiesAroundTile = new TreeSet<Player>();
		for(SettlersVertex v : getModel().board().vertexNeighbors(selectedTile)){
			if(v.hasBuilding())
				ennemiesAroundTile.add(v.getBuilding().getPlayer());
		}
		ennemiesAroundTile.remove(getCurrentPlayer());
		return ennemiesAroundTile;
	}

	@Override
	public boolean updateBiggestArmy() {
		return model.updateBiggestArmy();
	}

	

}
