package server.state;

import java.util.TreeSet;

import player.*;
import delaunay.*;
import controlor.DB;
import controlor.GameController;
import controlor.IGameController;
import client.IClient;
import client.action.*;

public class ServerStateFirstColony extends ServerState{
	public ServerStateFirstColony(IGameController gc,IClient clients[]) {
		super(gc,clients);
		messageToCurrentPlayer("Please give the position of your first colony");
	}
	

	@Override
	public ServerState receivesClientClick(ClientActionClick c){
		try {
			Pnt click = c.getPoint();
			gc.addFreeColony(click, gc.getCurrentPlayer());
			updateClientsView();
			return new ServerStateFirstRoad(gc, clients);
		} catch (Exception e) {
			System.out.println("Invalid first colony placement");
			messageToCurrentPlayer("You cant place your colony here");
			return this;
		}
	}

	
	
}
