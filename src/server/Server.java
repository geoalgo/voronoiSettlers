package server;

import controlor.DB;
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
		try {
			currentState = currentState.receivesAction(action);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public ServerState getCurrentState() {
		return currentState;
	}

}
