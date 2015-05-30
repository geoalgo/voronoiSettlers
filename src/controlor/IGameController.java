package controlor;

import model.card.Card;
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

}
