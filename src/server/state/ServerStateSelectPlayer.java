package server.state;

import player.Player;
import model.card.Card;
import controlor.DB;
import controlor.IGameController;
import client.Client;
import client.IClient;
import client.action.*;


/**
 * Wait for the client to select a player to steal.
 * @author david
 *
 */
public class ServerStateSelectPlayer extends ServerState {
	public ServerStateSelectPlayer(IGameController gc,IClient clients[]) {
		super(gc,clients);
	}

	public ServerState receivesClientSelect(ClientSelection c){
		try {
			Player chosenPlayer = (Player)(c.getSelection());
			DB.msg(chosenPlayer.toString());
			steal(chosenPlayer);
			return new ServerStatePlayTurn(gc,clients);
		} catch (Exception e) {
			e.printStackTrace();
			return this;
		}
	}
	
	private void steal(Player stolen){
		gc.steal(gc.getCurrentPlayer(), stolen);
		messageToCurrentPlayer(gc.getCurrentPlayer()+ " stole "+stolen);
	}
	



}
