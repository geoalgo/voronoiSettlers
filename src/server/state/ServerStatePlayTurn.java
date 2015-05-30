package server.state;

import java.util.TreeSet;

import model.card.Card;
import player.*;
import delaunay.*;
import controlor.DB;
import controlor.IGameController;
import client.action.*;
import client.state.ClientStateSelection;

public class ServerStatePlayTurn extends ServerState{

	public ServerStatePlayTurn(IGameController gc) {
		super(gc);
		// TODO Auto-generated constructor stub
	}

	@Override
	public ServerState receivesClientClick(ClientActionClick action) {
		click(((ClientActionClick)action).getPoint());
		return this;
	}
	
	private void click(Pnt point){
	}

	@Override
	public ServerState receivesClientNextTurn(ClientNextTurn c) {
		gc.endTurn();
		return new ServerStatePlayTurn(gc);
	}
	
	@Override
	public ServerState receivesClientBuyCard(ClientBuyCard c){
		try {
			c.getClient().message("You bought a "+gc.buyCard());
			return this;
		} catch (Exception e) {
			c.getClient().message("You dont have enough ressources to buy a card!");
			return this;
		}
	}
	
	@Override
	public ServerState receivesClientPlayCard(ClientPlayCard c){
		if(gc.getCurrentPlayer().numCards()==0){
			c.getClient().message("You dont have any card");
			return this;
		}
		// set client and server states to choose card
		return new ServerStateSelectCard(gc,c.getClient());
	}
	
	
}
