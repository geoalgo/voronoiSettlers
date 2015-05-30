package server.state;

import controlor.IGameController;
import client.action.ClientAction;

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
	public void receivesAction(ClientAction action) {
		// TODO Auto-generated method stub
		
	}

}
