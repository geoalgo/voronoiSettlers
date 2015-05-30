package server.state;

import controlor.IGameController;
import client.action.ClientAction;
import client.action.ClientActionClick;
import client.action.ClientNextTurn;
import client.action.ClientSelection;

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

	@Override
	public ServerState receivesClientClick(ClientActionClick c) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ServerState receivesClientNextTurn(ClientNextTurn c) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ServerState receivesClientSelection(ClientSelection c) {
		// TODO Auto-generated method stub
		return null;
	}

}
