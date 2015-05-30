package client.action;

import client.IClient;

public class ClientPlayerSelection extends ClientAction {
	public int chosenPlayer;
	ClientPlayerSelection(IClient client,int chosenPlayer) {
		super(client);
		this.chosenPlayer = chosenPlayer;
	}
	
	public int getChosenPlayer(){
		return chosenPlayer;
	}

}
