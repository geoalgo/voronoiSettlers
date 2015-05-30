package server.state;

import controlor.IGameController;
import client.action.*;


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



}
