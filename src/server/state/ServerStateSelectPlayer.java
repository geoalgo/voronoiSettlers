package server.state;

import controlor.IGameController;
import client.action.ClientAction;
import client.action.ClientActionClick;
import client.action.ClientNextTurn;
import client.action.ClientSelection;

/**
 * Wait for the client to select a position for the stealer.
 * @author david
 *
 */
public class ServerStateSelectPlayer extends ServerState {

	public ServerStateSelectPlayer(IGameController gc) {
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
