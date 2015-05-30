package server.state;

import controlor.IGameController;
import client.action.ClientAction;

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
	public void receivesAction(ClientAction action) {
		// TODO Auto-generated method stub
		
	}

}
