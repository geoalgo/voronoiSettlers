package client.action;

import client.IClient;
import player.Player;

/**
 * To indicate that the player has selected a card, a player to steal, or 
 * a ressource to monopolize.
 * @author david
 *
 */
public class ClientBuyCard extends ClientAction{

	public ClientBuyCard(IClient client) {
		super(client);
	}
}
