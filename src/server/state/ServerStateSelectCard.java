package server.state;

import java.util.Collection;
import java.util.TreeSet;
import java.util.Vector;

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
//todo change this and put this code in PlayTurn in order to allow a player 
//to not play a card eventually
//1-client click on play card
//2-server sends back list of cards and activates gui chooser
//3-client can send selection that is catched to a card and checked if the card is valid
public class ServerStateSelectCard extends ServerState {
	public ServerStateSelectCard(IGameController gc,IClient c[]) {
		super(gc,c);
		messageToCurrentPlayer("Select a card to play");
		setCurrentClientState(
				new ClientStateSelection<Card>(
						"Select a card to play",
						getCardLists(),true)
				);
	}
	
	private Collection<Card> getCardLists(){
		Collection<Card> res = new Vector<Card>();
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
			DB.msg("try to play a card that was not present");
			return this;
		}
		
		if(c instanceof VictoryPoint){
			DB.msg("receives card victory point");
			messageToAllPlayers(getCurrentPlayer()+" plays a victory point");
			gc.getCurrentPlayer().addPoint();
			updateClientsView();
			return new ServerStatePlayTurn(gc,clients);
		}
		if(c instanceof FreeRoad){
			messageToAllPlayers(getCurrentPlayer()+" plays a two free road card");
			return new ServerStateTwoFreeRoad(gc, clients);
		}
		if(c instanceof Monopole){
			return new ServerStateSelectRessourceMonopole(gc, clients);
		}
		if(c instanceof Knight){
			messageToAllPlayers(getCurrentPlayer()+" plays a knight");
			updateLargestArmy();
			return new ServerStateSelectBrigandPosition(gc,clients);
		}
		return this;
	}
	
	@Override
	public ServerState receivesClientUndo(ClientActionUndo action) {
		return new ServerStatePlayTurn(gc,clients);
	}

	
	/**
	 * updates the largest army holder with the new knight card of the player.
	 */
	private void updateLargestArmy(){
		gc.getCurrentPlayer().addKnight();
		boolean armyChanged = gc.updateBiggestArmy();
		if(armyChanged) 
			messageToAllPlayers(gc.getCurrentPlayer().getName()+ " now has the biggest army.");
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
