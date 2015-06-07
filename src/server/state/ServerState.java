package server.state;

import model.card.Monopole;
import model.ressources.Ressource;
import player.Player;
import controlor.DB;
import controlor.IGameController;
import client.DummyClient;
import client.IClient;
import client.action.*;
import client.state.ClientState;

/**
 * TODO
 * should have list of clients (to update their views)
 * @author david
 *
 */
public abstract class ServerState {
	IGameController gc;
	IClient clients[];

	public ServerState(IGameController gc,IClient clients[]){
		this.gc = gc;
		this.clients = clients;
	}

	public Player getCurrentPlayer(){
		return gc.getCurrentPlayer();
	}

	public void messageToCurrentPlayer(String msg){
		clients[gc.getCurrentPlayer().getNum()].message(msg);
	}

	public void messageToAllPlayers(String msg){
		for (int i = 0; i < clients.length; i++) 
			clients[i].message(msg);
	}

	public void messageToAllButCurrentPlayer(String msg){
		for (int i = 0; i < clients.length; i++) 
			if(i!=gc.getCurrentPlayer().getNum())
				clients[i].message(msg);
	}

	protected void updateClientsView(){
		for (int i = 0; i < clients.length; i++) 
			clients[i].updateView();
	}

	protected void setCurrentClientState(ClientState cs){
		clients[gc.getCurrentPlayer().getNum()].setCurrentState(cs);
	}

	/**
	 * Receives an action and updates model/clients if it is valid.
	 * @param action
	 */
	public ServerState receivesAction(ClientAction action) throws Exception{
		if(action instanceof ClientSelection)
			return receivesClientSelect((ClientSelection) action);
		if(getCurrentPlayer().getNum()!=action.getPlayer().getNum()){
			action.getClient().message("It is not your turn");
			return this;
		}
		if(action instanceof ClientActionClick)
			return receivesClientClick((ClientActionClick)action);
		if(action instanceof ClientNextTurn)
			return receivesClientNextTurn((ClientNextTurn)action);
		if(action instanceof ClientBuyCard)
			return receivesClientBuyCard((ClientBuyCard)action);
		if(action instanceof ClientPlayCard)
			return receivesClientPlayCard((ClientPlayCard)action);
		if(action instanceof ClientActionKey)
			return receivesClientKey((ClientActionKey)action);
		
		if(action instanceof ClientActionUndo)
			return receivesClientUndo((ClientActionUndo)action);


		throw new Exception("Unknown Client Action");
	}
	
	
	public ServerState receivesClientUndo(ClientActionUndo action) {
		return this;
	}

	public ServerState receivesClientClick(ClientActionClick c){
		return this;
	}
	public ServerState receivesClientNextTurn(ClientNextTurn c){
		return this;
	}
	public ServerState receivesClientBuyCard(ClientBuyCard c){
		return this;
	}
	public ServerState receivesClientPlayCard(ClientPlayCard c){
		return this;
	}
	public ServerState receivesClientSelect(ClientSelection c){
		return this;
	}
	public ServerState receivesClientKey(ClientActionKey c){
		if(c.getKey().getKeyChar()=='c'){
			DB.msg("cheat mode add ressources");
			for(Ressource ress : Ressource.allRessources())
				gc.getCurrentPlayer().getRessource().add(ress, 3);
			gc.getCurrentPlayer().addCard(new Monopole());
			updateClientsView();
		}
		return this;
	}

}
