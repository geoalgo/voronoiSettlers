package client;

import client.state.ClientState;
import player.Player;

public class Client implements IClient {
	private Player player;
	ClientState currentState;
	
	public Client(Player p){
		this.player = p;
	}
	
	@Override
	public void message(String string) {
		
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

}
