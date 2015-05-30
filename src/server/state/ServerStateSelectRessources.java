package server.state;

import controlor.IGameController;
import client.action.ClientAction;

/**
 * State when waiting for the client to loose ressources.
 * @author david
 *
 */
public class ServerStateSelectRessources extends ServerState {

	public ServerStateSelectRessources(IGameController gc) {
		super(gc);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void receivesAction(ClientAction action) {
		// TODO Auto-generated method stub
		
	}

}
