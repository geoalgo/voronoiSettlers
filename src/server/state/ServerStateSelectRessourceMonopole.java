package server.state;

import model.ressources.Ressource;
import player.Player;
import controlor.DB;
import controlor.IGameController;
import client.DummyClient;
import client.IClient;
import client.action.*;
import client.state.ClientStateSelection;


public class ServerStateSelectRessourceMonopole extends ServerState {

	public ServerStateSelectRessourceMonopole(IGameController gc,IClient clients[]) {
		super(gc,clients);
		clients[gc.getCurrentPlayer().getNum()].setCurrentState(new 
				ClientStateSelection<Ressource>("Select a ressource for the monopole", 
						Ressource.allRessources())
				);
	}

	public ServerState receivesClientSelect(ClientSelection c){
		try {
			Ressource chosenRessource = (Ressource)(c.getSelection());
			DB.msg(chosenRessource.toString());
			gc.monopole(chosenRessource);
			messageToAllPlayers(gc.getCurrentPlayer()+" plays a monopole on "+chosenRessource);
			updateClientsView();
			return new ServerStatePlayTurn(gc,clients);
		} catch (Exception e) {
			e.printStackTrace();
			return this;
		}
	}
}
