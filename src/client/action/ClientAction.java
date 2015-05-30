package client.action;

import client.IClient;
import player.Player;

/**
 * An action taken by a client while playing.
 * @author david
 *
 */
public abstract class ClientAction {
	IClient client;
	
	ClientAction(IClient client){
		this.client = client;
	}
	
	public Player getPlayer() {
		return client.getPlayer();
	}
	public IClient getClient() {
		return client;
	}
}
