package client;

import client.state.ClientState;
import player.Player;

public interface IClient {

	/**
	 * Sends message to the client.
	 * @param string
	 */
	void message(String string);
	
	ClientState getCurrentState();
	void setCurrentState(ClientState cs);
	
	Player getPlayer();
}
