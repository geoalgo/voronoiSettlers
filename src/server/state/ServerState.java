package server.state;

import player.Player;
import controlor.IGameController;
import client.action.ClientAction;
import client.action.ClientActionClick;
import client.action.ClientBuyCard;
import client.action.ClientNextTurn;
import client.action.ClientPlayCard;
import client.action.ClientSelection;

public abstract class ServerState {
	IGameController gc;

	public ServerState(IGameController gc){
		this.gc = gc;
	}

	public Player getCurrentPlayer(){
		return gc.getCurrentPlayer();
	}

	/**
	 * Receives an action and updates model/clients if it is valid.
	 * @param action
	 */
	public ServerState receivesAction(ClientAction action){
		if(getCurrentPlayer().getNum()!=action.getPlayer().getNum()){
			action.getClient().message("It is not your turn");
			return this;
		}
		if(action instanceof ClientActionClick)
			return receivesClientClick((ClientActionClick)action);
		if(action instanceof ClientNextTurn)
			return receivesClientNextTurn((ClientNextTurn)action);
		if(action instanceof ClientSelection)
			return receivesClientSelection((ClientSelection)action);
		if(action instanceof ClientBuyCard)
			return receivesClientBuyCard((ClientBuyCard)action);
		if(action instanceof ClientPlayCard)
			return receivesClientPlayCard((ClientPlayCard)action);
		return null;
	}
	public ServerState receivesClientClick(ClientActionClick c){
		return this;
	}
	public ServerState receivesClientNextTurn(ClientNextTurn c){
		return this;
	}
	public ServerState receivesClientSelection(ClientSelection c){
		return this;
	}
	public ServerState receivesClientBuyCard(ClientBuyCard c){
		return this;
	}
	public ServerState receivesClientPlayCard(ClientPlayCard c){
		return this;
	}


}
