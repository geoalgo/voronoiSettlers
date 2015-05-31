package server.state;

import player.Player;
import model.card.Card;
import controlor.IGameController;
import client.IClient;
import client.action.*;


/**
 * Wait for the client to select a player to steal.
 * @author david
 *
 */
public class ServerStateSelectPlayer extends ServerState {
	IClient client;
	public ServerStateSelectPlayer(IGameController gc,IClient client) {
		super(gc);
		this.client = client;
	}

	public ServerState receivesClientSelect(ClientSelection c){
		try {
			Player chosenPlayer = (Player)(c.getSelection());
			steal(chosenPlayer);
			return new ServerStatePlayTurn(gc);
		} catch (Exception e) {
			e.printStackTrace();
			return this;
		}
	}
	
	private void steal(Player stolen){
		gc.steal(gc.getCurrentPlayer(), stolen);
		client.message(gc.getCurrentPlayer()+ " stole "+stolen);
	}
	



}
