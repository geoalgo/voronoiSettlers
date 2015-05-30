package server.state;

import controlor.IGameController;
import client.action.ClientAction;

/**
 * State when waiting for the client to select the stealer position after a 7.
 * @author david
 *
 */
public class ServerStateSelectThief extends ServerState {

	public ServerStateSelectThief(IGameController gc) {
		super(gc);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void receivesAction(ClientAction action) {
		// TODO Auto-generated method stub
		
	}

}
