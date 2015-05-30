package server;

import controlor.IGameController;
import server.state.*;
import client.action.ClientAction;

public class Server implements IServer {
	private ServerState currentState;
	
	private IGameController gc;
	
	public Server(IGameController gc){
		this.gc = gc;
		currentState = new ServerStatePlayTurn(gc);
	}
	
	@Override
	public void receiveAction(ClientAction action) {
		currentState = currentState.receivesAction(action);
	}

	@Override
	public ServerState getCurrentState() {
		return currentState;
	}

}
