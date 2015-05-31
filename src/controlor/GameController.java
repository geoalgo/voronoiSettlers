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

	public GameController(InitialRules rules) {
		model = new Model(rules);	
	}

	public Model getModel(){
		return model;
	}

	public void nextPlayer(){
		currentPlayer = (currentPlayer+1)%getNumPlayer();
	}
	
	public void previousPlayer(){
		currentPlayer = (currentPlayer-1)%getNumPlayer();
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

	public int getNumPlayer(){
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
	}

	void addFreeColony(SettlersVertex v,Player p) throws Exception{
		model.addFreeColony(new Colony(v,p),v);
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
	}
	
	public void addFreeRoad(Pnt pnt,Player p) throws Exception{
		SettlersEdge e = model.board().locateClosestEdge(pnt);
		addFreeRoad(e, p);
	}	

	public void addFreeRoad(SettlersEdge e,Player p) throws Exception{
		model.addFreeRoad(p,e);
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
	}

	void addSecondRoad(SettlersEdge e,Player p) throws Exception{
		Colony c = (Colony) p.getBuilding(1);
		model.addFreeRoadNearColony(p,e,c);
	}

	@Override
	public void resetRessources() {
		for(int i = 0 ; i < getNumPlayer(); ++i){
			getPlayer(i).getRessource().setToZero();
		}
	}

	@Override
	public void addRessourcesToCurrentPlayer() {
		for(Ressource r : Ressource.allRessources())
			getCurrentPlayer().getRessource().add(r, 1);
	}
	
	@Override
	public void addCardsToCurrentPlayer() {
		getCurrentPlayer().addCard(new FreeRoad());
		getCurrentPlayer().addCard(new Monopole());
		getCurrentPlayer().addCard(new Knight());
		getCurrentPlayer().addCard(new VictoryPoint());
	}
	
	public void addColonyAroundTile(SettlersTile tile){
		int currentPlayer = 0;
		for(SettlersVertex v : model.board().vertexNeighbors(tile)){
			// we add ressource to allow construction
			for(Ressource r : Ressource.allRessources())
				getPlayer(currentPlayer).getRessource().add(r, 2);
			try {
				addFreeColony(v,getPlayer(currentPlayer));
				DB.msg("added colony to "+getPlayer(currentPlayer));
				currentPlayer = (currentPlayer+1)%getNumPlayer();
			} catch (Exception e) {
				DB.msg("cant add colony to "+getPlayer(currentPlayer));
				continue;
			}
		}
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

	@Override
	public void monopole(Ressource ressourceToMonopolize) {
		int currentPlayer = getCurrentPlayer().getNum();
		for(int i = 0; i< getNumPlayer();++i){
			if(i!=currentPlayer){
				stealAllPlayerRessource(getCurrentPlayer(),getPlayer(i),ressourceToMonopolize);
			}
		}		
	}
	
	private void stealAllPlayerRessource(Player stealer, Player screwed, Ressource r){
		int numRess = screwed.getRessource().getNum(r);
		DB.msg("steal "+numRess+ " ressources of "+r);
		screwed.getRessource().add(r, -numRess);
		stealer.getRessource().add(r, numRess);
	}

	@Override
	public SettlersVertex locateClosestVertex(Pnt click) {
		return model.board().locateClosestVertex(click);
	}

	@Override
	public SettlersEdge locateClosestEdge(Pnt click) {
		return model.board().locateClosestEdge(click);
	}



	

}
