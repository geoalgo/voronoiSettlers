package server;

import server.state.ServerState;
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
	
}
