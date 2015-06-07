package controlor;

import java.util.TreeSet;

import delaunay.Pnt;
import model.Model;
import model.card.Card;
import model.hexagonalTiling.*;
import model.ressources.Ressource;
import player.Player;

public interface IGameController {
	public Player getPlayer(int num);
	public int getNumPlayer();
	public Player getCurrentPlayer();
	public void nextPlayer();
	public void previousPlayer();


	/**
	 * Set all ressources of players to zero.
	 */
	public void resetRessources();
	
	//////////////////////////////////
	//For test purpose
	/**
	 * Add one of all ressources to current player (for testing).
	 */
	public void addRessourcesToCurrentPlayer();
	/**
	 * Add a card of each type to the player (for testing).
	 */
	public void addCardsToCurrentPlayer();
	
	/**
	 * Add a colony for each player around the tile 
	 */
	public void addColonyAroundTile(SettlersTile tile);
	//////////////////////////////////
	
	// return the card bought or an exception if not enough ressources
	// or no more cards
	public Card buyCard() throws Exception;

	int currentPlayerNum();

	int drawRandomDices();

	void initialHarvest();

	void harvest(int randomDices);

	void steal(Player stealer, Player screwed);
	
	/**
	 * Current player monopolize a ressource.
	 * @param ressourceToMonopolize
	 */
	void monopole(Ressource ressourceToMonopolize);
	/**
	 * Consumes a card (after it has been played).
	 * Return an exception if it was not present.
	 * @param c
	 * @throws Exception
	 */
	void consumeCard(Card c) throws Exception;

	
	public SettlersTile locateClosestTile(Pnt p);
	public SettlersTile getThiefPosition();
	public void setThiefPosition(SettlersTile selectedTile);
	public TreeSet<Player> getNeighborsEnnemies(SettlersTile selectedTile);
	public boolean updateBiggestArmy();
	public void addFreeColony(Pnt click, Player currentPlayer) throws Exception;
	public void addFirstRoad(Pnt click, Player currentPlayer) throws Exception;
	public void addSecondRoad(Pnt click, Player currentPlayer) throws Exception;
	public void addColony(Pnt click, Player currentPlayer) throws Exception;
	public void addCity(Pnt click, Player currentPlayer) throws Exception;
	public void addRoad(SettlersEdge e, Player currentPlayer) throws Exception;
	public SettlersVertex locateClosestVertex(Pnt click);
	public SettlersEdge locateClosestEdge(Pnt click);
	public Model getModel();
}
