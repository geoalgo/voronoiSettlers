package server.state;

import controlor.IGameController;
import client.action.*;

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
	public ServerState receivesAction(ClientAction action) {
		return this;		
	}


}
