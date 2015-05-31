package controlor;

import java.util.TreeSet;

import delaunay.Pnt;
import model.card.Card;
import model.hexagonalTiling.SettlersTile;
import player.Player;

public interface IGameController {
	public Player getPlayer(int num);
	public Player getCurrentPlayer();
	public void endTurn();

	/**
	 * Set all ressources of players to zero.
	 */
	public void resetRessources();
	/**
	 * Add one of all ressources to current player (for testing).
	 */
	public void addRessourcesToCurrentPlayer();
	
	// return the card bought or an exception if not enough ressources
	// or no more cards
	public Card buyCard() throws Exception;

	int currentPlayerNum();

	int drawRandomDices();

	void initialHarvest();

	void harvest(int randomDices);

	void steal(Player stealer, Player screwed);
	
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
}
