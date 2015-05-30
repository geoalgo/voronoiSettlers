package server.state;

import controlor.IGameController;
import client.action.ClientAction;
import client.action.ClientActionClick;
import client.action.ClientNextTurn;
import client.action.ClientSelection;

/**
 * Wait for the client to select one of its card.
 * @author david
 *
 */
public class ServerStateSelectCard extends ServerState {

	public ServerStateSelectCard(IGameController gc) {
		super(gc);
		// TODO Auto-generated constructor stub
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
