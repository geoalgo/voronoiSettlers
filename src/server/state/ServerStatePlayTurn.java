package server.state;

import player.*;
import delaunay.*;
import controlor.DB;
import controlor.IGameController;
import client.action.ClientAction;
import client.action.ClientActionClick;
import client.action.ClientBuyCard;
import client.action.ClientNextTurn;
import client.action.ClientSelection;

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
}
