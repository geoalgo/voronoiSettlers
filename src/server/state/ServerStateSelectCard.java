package server.state;

import java.util.TreeSet;

import org.hamcrest.core.IsInstanceOf;

import model.card.*;
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
			return playCard(card,c.getClient());
			//TODO do the play card actions
		} catch (Exception e) {
			e.printStackTrace();
		}
		return this;
	}

	private ServerState playCard(Card c, IClient client){
		try {
			gc.consumeCard(c);
		} catch (Exception e) {
			return this;
		}
		
		if(c instanceof VictoryPoint){
			gc.getCurrentPlayer().addPoint();
			return this;
		}
		if(c instanceof FreeRoad){
			return this;
		}
		if(c instanceof Monopole){
			return this;
		}
		if(c instanceof Knight){
			updateLargestArmy();
			return new ServerStateSelectBrigandPosition(gc,client);
		}
		return this;
		
	}
	
	/**
	 * updates the largest army holder with the new knight card of the player.
	 */
	private void updateLargestArmy(){
		gc.getCurrentPlayer().addKnight();
		boolean armyChanged = gc.updateBiggestArmy();
		if(armyChanged) 
			client.message(gc.getCurrentPlayer().getName()+ " now has the biggest army.");
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
