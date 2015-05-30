package server.state;

import player.Player;
import controlor.IGameController;
import client.action.ClientAction;

public abstract class ServerState {
	IGameController gc;
	
	public ServerState(IGameController gc){
		this.gc = gc;
	}
	
	public Player getCurrentPlayer(){
		return gc.getCurrentPlayer();
	}
	
	public abstract void receivesAction(ClientAction action);
}
