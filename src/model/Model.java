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


package model;

import java.awt.Color;
import java.util.Iterator;
import java.util.List;

import controlor.DB;
import controlor.isValidPlacement.edgeBuilding.ConstrainEdgeBuilding;
import controlor.isValidPlacement.vertexBuilding.ConstrainVertexBuilding;

import delaunayBrute.EdgeHandle;

import model.Construction.Building;
import model.Construction.City;
import model.Construction.Colony;
import model.Construction.Road;
import model.Construction.VertexBuilding;
import model.card.Card;
import model.card.Cards;
import model.hexagonalTiling.BoardTiles;
import model.hexagonalTiling.DelaunayBoardTiles;
import model.hexagonalTiling.SettlersEdge;
import model.hexagonalTiling.SettlersTile;
import model.hexagonalTiling.SettlersVertex;
import model.hexagonalTiling.harbor.Harbor;
import model.ressources.Ressource;
import model.ressources.Ressources;
import player.Player;
import player.Players;

public class Model {

	InitialRules rules;
	Players players;
	Iterator<Player> currentPlayer;
	BoardTiles board;
	Cards cards;
	SettlersTile thiefPosition;
	Integer biggestArmyHolder = null;

	public Model(InitialRules rules) {
		this.rules = rules;
		initPlayers(rules.numPlayers());
		board = new DelaunayBoardTiles(rules);	
		setBoard();
		cards = new Cards(rules);

		//todo cheat
		for(Ressource r : Ressource.allRessources())
			players.getPlayer(0).getRessource().add(r,3);
	}

	public BoardTiles board(){
		return board;
	}

	void setBoard(){
		setThiefPosition();
	}

	private void setThiefPosition(){
		Iterator<SettlersTile> tilesIt = board.tiles();
		while(tilesIt.hasNext()){
			SettlersTile tile = tilesIt.next();
			if(tile.ressource().isDesert()){
				setThiefPosition(tile);
				return;
			}
		}
		System.err.println("Could not placed the thief cause no desert was found");
	}

	public SettlersTile getThiefPosition(){
		return thiefPosition;
	}

	public void setThiefPosition(SettlersTile newPosition){
		thiefPosition = newPosition ; 
	}

	private Player playerFromNumber(int i){
		switch (i) {
		case 0:
			return new Player(Color.black,"Jo",0);
		case 1:
			return new Player(Color.white,"Crok",1);
		case 2:
			return new Player(Color.red,"Zaz",2);
		default:
			return new Player(Color.cyan,"Lucas",3);
		}
	}

	public Player getPlayer(int i){
		return players.getPlayer(i);
	}

	public Iterator<Player> getPlayerIterator(){
		return players.iterator();
	}

	/** 
	 * @return next player in a cyclic way
	 */
	public Player nextPlayer(){
		return players.nextPlayer();
	}

	void initPlayers(int numPlayers){
		players = new Players();
		for(int i = 0; i < Math.min(4,numPlayers); ++i){
			players.addPlayer(playerFromNumber(i));
		}
	}

	//TODO raise exception instead	
	public boolean isOver(){
		if(players.getMaxScore()>rules.scoreMax) return true;
		else return false;
	}


	public int numPlayers(){
		return players.size();
	}

	/**
	 * @return true if the building was allowed.
	 */
	public void addColony(Colony building,SettlersVertex position)
			throws BuildException{
		(ConstrainVertexBuilding.makeColonyConstrains(this)).isValid(building, position);
		if(!vertexHasPlayerRoad(position,building.getPlayer()))
			throw new BoardPlacementException("no road nearby");
		registerBuilding(building, position);
	}

	public void addFreeColony(Colony building,SettlersVertex position)
			throws BuildException{
		if(!building.isColony()) return;
		(ConstrainVertexBuilding.makeFirstColonyConstrains(this)).isValid(building, position);
		registerFreeBuilding(building, position);
	}

	public void addCity(City building,SettlersVertex position)
			throws BuildException{
		(ConstrainVertexBuilding.makeCityConstrains(this)).isValid(building, position);
		if(building.isColony() &&
				!vertexHasPlayerRoad(position,building.getPlayer())){
			throw new BoardPlacementException("no road nearby");
		}
		registerBuilding(building, position);
	}

	/**
	 * @param p
	 * @param position
	 * @throws Exception if the road building was not allowed
	 */
	public void addRoad(Player p,SettlersEdge position) throws BuildException{
		// todo later do exception to explain the callee if not money or map problem
		Road road = new Road(p);
		(ConstrainEdgeBuilding.makeRoadConstrains(this)).isValid(road, position);
		registerBuilding(road, position);
	}

	public void addFreeRoadNearColony(
			Player p,SettlersEdge position,Colony colony) 
					throws Exception{
		// todo later do exception to explain the callee if not money or map problem
		Road road = new Road(p);
		(ConstrainEdgeBuilding.makeFirstRoadConstrains(this,colony)).isValid(road, position);
		checkFreeRoadPossible(road, position);
		registerFreeBuilding(road, position);
	}




	private void checkRoadPossible(Road building,SettlersEdge position) throws BuildException{
		if(!building.isEnough(building.getPlayer().getRessource())) 
			throw new NotEnoughRessourceException();
		checkFreeRoadPossible(building, position);
	}

	private void checkFreeRoadPossible(Road building,SettlersEdge position) throws BuildException{
		checkBoardAvailability(building,position);
	}

	private void checkBoardAvailability(Road building,SettlersEdge edge) throws BuildException{
		if(edge.hasBuilding()) {
			System.out.println("Already a road");
			throw new BoardPlacementException("Already a road");
		}
		//check that one neighbor is a settlement or a road

		if(vertexHasPlayerBuilding(board.first(edge),building.getPlayer())){
			return;
		}
		if(vertexHasPlayerBuilding(board.second(edge),building.getPlayer())){
			return;
		}
		boolean res = vertexHasPlayerRoad(board.first(edge),building.getPlayer())
				|| vertexHasPlayerRoad(board.second(edge),building.getPlayer());
		if(!res){
			System.out.println("No adjacent building");
			throw new BoardPlacementException("No adjacent building");
		}
		//todo also check that not crossing opponent building
	}

	private boolean vertexHasPlayerBuilding(SettlersVertex vertex, Player p){
		if(!vertex.hasBuilding()) return false;
		Building b = vertex.getBuilding();
		return b.getPlayer().equals(p); 
	}

	private boolean vertexHasPlayerRoad(SettlersVertex vertex, Player p){
		for(SettlersEdge edge : board.edgeNeighbors(vertex)){
			if(edge.hasBuilding() && 
					edge.getBuilding().getPlayer().equals(p)) 
				return true;
		}
		return false;
	}

	private void registerBuilding(Road road,SettlersEdge position){
		road.getPlayer().addBuilding(road);
		position.setBuilding(road);
	}


	private void registerFreeBuilding(Road road,SettlersEdge position){
		road.getPlayer().addFreeBuilding(road);
		position.setBuilding(road);
	}


	private void registerBuilding(Building building,SettlersVertex position){
		building.getPlayer().addBuilding(building);
		position.setBuilding(building);
		registerHarbor(building,position);
	}

	private void registerFreeBuilding(Building building,SettlersVertex position){
		building.getPlayer().addFreeBuilding(building);
		position.setBuilding(building);
		registerHarbor(building,position);
	}

	/**
	 * add eventual harbor to a player if he builds 
	 * a colony adjacent to an harbour edge. 
	 */
	private void registerHarbor(Building building,SettlersVertex position){
		if(building.isColony()){
			Harbor h = adjacentHarbor(position);
			if(h!=null) {
				building.getPlayer().addHarbor(h);
				DB.msg("Player has a new harbor!");
			}
		}
	}

	//null if none
	private Harbor adjacentHarbor(SettlersVertex v){
		for(SettlersEdge e : board().edgeNeighbors(v)){
			if(e.hasHarbor()) return e.harbor();
		}
		return null;
	}

	/**
	 * Harvest ressources for all players
	 */
	public void harvest(int diceNumber){
		assert(2<=diceNumber && diceNumber <=12);
		Iterator<SettlersTile> tilesIt = board().tiles();
		while(tilesIt.hasNext()){
			SettlersTile tile = tilesIt.next();
			if(tile!=thiefPosition)
				harvestTile(tile,diceNumber);
		}
	}


	/** 
	 * Harvest one building.
	 * useful for the first harvest.
	 * @param building
	 */
	public void harvest(VertexBuilding building){
		SettlersVertex position = building.getPosition();
		for(SettlersTile tile : board().tilesNeighbors(position)){
			harvestBuilding(building,tile.ressource());
		}
	}

	private void harvestTile(SettlersTile tile,int diceNumber){
		if(tile.diceNumber() != diceNumber) return;
		for(SettlersVertex v : board().vertexNeighbors(tile)){
			if(v.hasBuilding()) harvestBuilding(v.getBuilding(),tile.ressource());
		}
	}

	private void harvestBuilding(Building building,Ressource ressource){
		int numHarvest = building.numHarvest();
		building.getPlayer().getRessource().add(ressource,numHarvest);
	}

	/**
	 * Player p receives a random card.
	 * @return the card that was given.
	 * @throws if no more cards
	 */
	public Card giveRandomCard(Player p) throws Exception{
		return cards.getRandomCard(p);
	}

	public void reputCard(Card c){
		cards.releaseCard(c);
	}

	/**
	 * to be called when a new knight is affected
	 * return true if the player with the biggest army changed.
	 */
	public boolean updateBiggestArmy(){
		int newBiggestArmyHolder = getMaxArmyPlayer();

		boolean changed = 
				// there was none and now there is one
				((biggestArmyHolder==null) && ((newBiggestArmyHolder!=-1))) 
				||
				// there was one but one becomes bigger
				((biggestArmyHolder!=null) && newBiggestArmyHolder!=biggestArmyHolder);

		if(changed){ 
			if(biggestArmyHolder!=null)
				getPlayer(biggestArmyHolder).removePoint(2);
			getPlayer(newBiggestArmyHolder).addPoint(2);
			biggestArmyHolder = newBiggestArmyHolder;
		}
		return changed;
	}

	//return -1 if no player has more than 3 knights
	private int getMaxArmyPlayer(){
		int newBiggestArmyHolder = -1;
		for(int i = 0; i < numPlayers(); ++i){
			if(getPlayer(i).numKnight()>=3){
				if(newBiggestArmyHolder==-1|| 
						getPlayer(newBiggestArmyHolder).numKnight() < getPlayer(i).numKnight()) 
					newBiggestArmyHolder = i;
			}
		}
		return newBiggestArmyHolder;
	}
}
