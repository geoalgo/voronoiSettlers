package server.state;

import model.ressources.Ressource;
import player.Player;
import controlor.DB;
import controlor.IGameController;
import client.DummyClient;
import client.IClient;
import client.action.*;


public class ServerStateSelectRessourceMonopole extends ServerState {

	public ServerStateSelectRessourceMonopole(IGameController gc,IClient clients[]) {
		super(gc,clients);
	}

	public ServerState receivesClientSelect(ClientSelection c){
		try {
			Ressource chosenRessource = (Ressource)(c.getSelection());
			DB.msg(chosenRessource.toString());
			gc.monopole(chosenRessource);
			messageToAllPlayers(gc.getCurrentPlayer()+" plays a monopole on "+chosenRessource);
			return new ServerStatePlayTurn(gc,clients);
		} catch (Exception e) {
			e.printStackTrace();
			return this;
		}
	}
}
