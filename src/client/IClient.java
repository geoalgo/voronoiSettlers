package client;

import model.card.Card;
import model.ressources.Ressource;
import client.action.ClientAction;
import client.state.ClientState;
import delaunay.Pnt;
import player.Player;
import server.IServer;

public interface IClient {
	/**
	 * Sends message to the client.
	 * @param string
	 */
	void message(String string);
	
	ClientState getCurrentState();
	void setCurrentState(ClientState cs);
	Player getPlayer();
	
	void sendAction(ClientAction a);
}
