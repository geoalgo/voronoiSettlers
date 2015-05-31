package server.state;

import java.util.TreeSet;

import player.*;
import delaunay.*;
import controlor.DB;
import controlor.GameController;
import controlor.IGameController;
import controlor.gamestate.AskSecondColony;
import controlor.gamestate.PlayTurn;
import client.IClient;
import client.action.*;

public class ServerStateSecondRoad extends ServerState{
	public ServerStateSecondRoad(IGameController gc,IClient clients[]) {
		super(gc,clients);
		messageToCurrentPlayer("Please specify the position of your second road");
	}
	
	@Override
	public ServerState receivesClientClick(ClientActionClick c){
		try {
			Pnt click = c.getPoint();
			gc.addSecondRoad(click, gc.getCurrentPlayer());

			int nextPlayer = gc.currentPlayerNum()-1;
			if( nextPlayer >= 0 ){
				gc.previousPlayer();
				return new ServerStateSelondColony(gc, clients);
			}
			else{
				try {
					gc.initialHarvest();
					return ServerStateDrawDices.drawRandomDices(gc, clients);
				} catch (Exception e) {
					DB.msg("pb with harvest");
				}
			}
		} catch (Exception e) {
			System.out.println("Invalid Second road placement");
		}
		return new ServerStatePlayTurn(gc, clients);
	}

}
