package server.state;

import java.util.TreeSet;

import model.card.Card;
import controlor.DB;
import controlor.IGameController;
import client.IClient;
import client.action.*;
import client.state.ClientStateSelection;

/**
 * Wait for the client to select one of its card.
 * @author david
 *
 */
public class ServerStateSelectCard extends ServerState {
	IClient client;
	public ServerStateSelectCard(IGameController gc,IClient c) {
		super(gc);
		this.client = c ;
		client.setCurrentState(new ClientStateSelection<Card>(getCardLists()));
	}
	
	private TreeSet<Card> getCardLists(){
		TreeSet<Card> res = new TreeSet<Card>();
		for(int i=0; i < gc.getCurrentPlayer().numCards(); ++i)
			res.add(gc.getCurrentPlayer().getCard(i));
		return res;
	}
	
	@Override
	public ServerState receivesClientSelect(ClientSelection c){
		try {
			Card card = (Card)(c.getSelection());
			DB.msg("play card");
			//TODO do the play card actions
		} catch (Exception e) {
			e.printStackTrace();
		}
		return this;
	}

	
	
	@Override
	public ServerState receivesClientClick(ClientActionClick c) {
		// TODO Auto-generated method stub
		c.getClient().message("You must select your card before");
		return this;
	}

	@Override
	public ServerState receivesClientNextTurn(ClientNextTurn c) {
		c.getClient().message("You must select your card before");
		return this;
	}



}
