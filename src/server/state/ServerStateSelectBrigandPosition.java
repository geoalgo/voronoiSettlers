package server.state;

import java.util.TreeSet;

import model.hexagonalTiling.SettlersTile;
import model.hexagonalTiling.SettlersVertex;
import player.Player;
import controlor.DB;
import controlor.IGameController;
import controlor.ui.UIChoosePlayerToSteal;
import client.DummyClient;
import client.IClient;
import client.action.*;
import client.state.ClientStateSelection;
import delaunay.Pnt;

/**
 * State when waiting for the client to select the stealer position after a 7.
 * @author david
 *
 */
public class ServerStateSelectBrigandPosition extends ServerState {
	SettlersTile selectedTile = null;

	public ServerStateSelectBrigandPosition(IGameController gc,	IClient clients[]){
		super(gc,clients);
		messageToCurrentPlayer("Please choose a new position for the thief");
	}

	@Override
	public ServerState receivesClientClick(ClientActionClick c){
		DB.msg("knight click");
		SettlersTile closestTile = gc.locateClosestTile(c.getPoint());
		if(closestTile!=null) 
			return click(closestTile,c.getPoint());
		else return this;
	}

	private ServerState click(SettlersTile selectedTile,Pnt click){
		if(selectedTile == gc.getThiefPosition()){
			messageToCurrentPlayer("You cannot place the thief at the same position");
			return this;
		}
		else{
			this.selectedTile = selectedTile;
			gc.setThiefPosition(selectedTile);
			return stealEnnemies(selectedTile);
		}
	}

	private ServerState stealEnnemies(SettlersTile selectedTile){
		TreeSet<Player> ennemiesAroundTile = gc.getNeighborsEnnemies(selectedTile);
		if(ennemiesAroundTile.isEmpty()) 
			return new ServerStatePlayTurn(gc,clients);
		else{
			setCurrentClientState(
					new ClientStateSelection<Player>(
							"Select a player to steal",
							ennemiesAroundTile)
					);
			return new ServerStateSelectPlayer(gc,clients);
		}
			
	}


	
}
