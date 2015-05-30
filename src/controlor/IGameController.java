package controlor;

import model.card.Card;
import player.Player;

public interface IGameController {
	public Player getPlayer(int num);
	
	public Player getCurrentPlayer();

	public void endTurn();
	
	// return the card bought or an exception if not enough ressources, no more
	// cards ...
	public Card buyCard() throws Exception;

	int currentPlayerNum();

	int drawRandomDices();

	void initialHarvest();

	void harvest(int randomDices);

	void steal(Player stealer, Player screwed);

}
