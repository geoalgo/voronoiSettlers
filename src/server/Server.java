package server;

import controlor.DB;
import controlor.IGameController;
import server.state.*;
import client.Client;
import client.IClient;
import client.action.ClientAction;

public class Server implements IServer {
	private ServerState currentState;
	private IClient clients[];
	private IGameController gc;
	
	public Server(IGameController gc){
		this.gc = gc;
		currentState = null ;
	}
	
	@Override
	public boolean hasClients(){
		return currentState != null;
	}
	
	@Override
	public void init(IClient clients[]){
		this.clients = clients;
		currentState = new ServerStatePlayTurn(gc, clients);
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
	
	@Override
	public void setState(ServerState s){
		currentState = s;
	}


}
