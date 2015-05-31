package client;

import client.action.ClientAction;
import client.state.ClientState;
import player.Player;
import server.IServer;

public class Client implements IClient {
	private ISendToServer server;
	private Player player;
	ClientState currentState;
	
	public Client(ISendToServer server,Player p){
		this.player = p;
		this.server = server;
	}
	
	@Override
	public void message(String msg) {
		System.out.println("Client: "+msg);
	}
	
	@Override
	public void updateView() {
	}

	@Override
	public Player getPlayer() {
		return player;
	}

	@Override
	public ClientState getCurrentState() {
		return currentState;
	}

	@Override
	public void setCurrentState(ClientState cs) {
		currentState = cs;		
	}

	@Override
	public void sendAction(ClientAction action) {
		server.receiveAction(action);
	}

}
