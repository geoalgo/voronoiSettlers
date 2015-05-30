package client.action;

import client.IClient;
import player.Player;

/**
 * 
 * @author david
 *
 */
public abstract class ClientSelection extends ClientAction{

	ClientSelection(IClient client) {
		super(client);
	}
}
