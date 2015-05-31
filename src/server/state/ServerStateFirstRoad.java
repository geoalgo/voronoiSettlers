package server.state;

import java.util.TreeSet;

import player.*;
import delaunay.*;
import controlor.DB;
import controlor.IGameController;
import controlor.gamestate.AskFirstColony;
import controlor.gamestate.AskSecondColony;
import client.IClient;
import client.action.*;

public class ServerStateFirstRoad extends ServerState{
	public ServerStateFirstRoad(IGameController gc,IClient clients[]) {
		super(gc,clients);
		messageToCurrentPlayer("Please give the position of your first road");
	}
	
	@Override
	public ServerState receivesClientClick(ClientActionClick c){
		try {
			Pnt click = c.getPoint();
			gc.addFirstRoad(click, gc.getCurrentPlayer());
			int nextPlayer = gc.getCurrentPlayer().getNum()+1;
			if( nextPlayer < gc.getNumPlayer() ){
				gc.nextPlayer();
				return new ServerStateFirstColony(gc, clients);
			}
			else{
				return new ServerStateSecondColony(gc, clients);
			}
		} catch (Exception e) {
			System.out.println("Invalid first colony placement");
			return this;
		}
	}

}
