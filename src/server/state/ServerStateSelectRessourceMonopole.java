package server.state;

import controlor.IGameController;
import client.action.*;


public class ServerStateSelectRessourceMonopole extends ServerState {

	public ServerStateSelectRessourceMonopole(IGameController gc) {
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


}
