package server;

import player.Player;
import model.Model;
import server.state.ServerState;
import client.DummyClient;
import client.IClient;
import client.action.ClientAction;

/**
 * Server/Controller class called by clients.
 * @author david
 *
 */
public interface IServer {
	
	/**
	 * Receives an action and updates model/clients if it is valid.
	 * @param action
	 */
	void receiveAction(ClientAction action);
	
	ServerState getCurrentState();
	void setState(ServerState s);

	void init(IClient[] clients);

	boolean hasClients();
	Model getModel();

	Player getPlayer(int i);
	int getCurrentPlayer();
}
