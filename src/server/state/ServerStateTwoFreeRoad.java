package server.state;

import java.util.TreeSet;

import player.*;
import delaunay.*;
import controlor.DB;
import controlor.IGameController;
import client.IClient;
import client.action.*;

public class ServerStateTwoFreeRoad extends ServerState{
	//the number of the road placed
	private int currentRoad=1;
	
	public ServerStateTwoFreeRoad(IGameController gc,IClient clients[]) {
		super(gc,clients);
		messageToCurrentPlayer("Please give the position of your first free road");
	}
	
	@Override
	public ServerState receivesClientClick(ClientActionClick c){
		try {
			Pnt click = c.getPoint();
			gc.addFreeRoad(click, gc.getCurrentPlayer());
			updateClientsView();

			if(currentRoad==2)
				return new ServerStatePlayTurn(gc, clients);
			else{
				++currentRoad;
				messageToCurrentPlayer("Please give the position of your second free road");
				return this;
			}
		} catch (Exception e) {
			System.out.println("Invalid first road placement");
			messageToCurrentPlayer("You cant place your road here");
			return this;
		}
	}

}
