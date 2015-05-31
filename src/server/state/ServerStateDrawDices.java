package server.state;

import client.IClient;
import controlor.DB;
import controlor.IGameController;
import controlor.gamestate.LooseRessource;

public class ServerStateDrawDices {
	public static ServerState drawRandomDices(IGameController gc,IClient clients[]){
		return dealDices(gc,clients,gc.drawRandomDices());
	}
	
	public static ServerState drawDices(IGameController gc,IClient clients[],int dices){
		return dealDices(gc, clients, dices);
	}
	
	private static ServerState dealDices(IGameController gc,IClient clients[],int dices){
		ServerState res;
		if(dices!=7){
			gc.harvest(dices);
			res = new ServerStatePlayTurn(gc, clients);
		}
		else{
			ServerState statePlayerLoosingRessources = ServerStateSelectRessources.firstPlayerLoosingRessources(gc, clients);
			if(statePlayerLoosingRessources!=null) 
				res = statePlayerLoosingRessources;
			else 
				res = new ServerStatePlayTurn(gc, clients);
		}
		res.messageToAllPlayers(gc.getCurrentPlayer()+" draws "+dices);
		return res;
	}

}
