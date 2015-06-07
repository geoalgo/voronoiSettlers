package server.state;

import java.util.TreeSet;

import player.*;
import delaunay.*;
import controlor.DB;
import controlor.GameController;
import controlor.IGameController;
import client.IClient;
import client.action.*;

public class ServerStateSecondColony extends ServerState{
	public ServerStateSecondColony(IGameController gc,IClient clients[]) {
		super(gc,clients);
		messageToCurrentPlayer("Please give the position of your second colony");
	}

	@Override
	public ServerState receivesClientClick(ClientActionClick c){
		try {
			Pnt click = c.getPoint();
			gc.addFreeColony(click, gc.getCurrentPlayer());
			updateClientsView();
			return new ServerStateSecondRoad(gc, clients);
		} catch (Exception e) {
			System.out.println("Invalid first colony placement");
			return this;
		}
	}

	
	
}
