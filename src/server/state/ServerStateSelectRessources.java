package server.state;

import model.card.Card;
import model.ressources.Ressources;
import controlor.DB;
import controlor.IGameController;
import client.DummyClient;
import client.IClient;
import client.action.*;

/**
 * State when waiting for the client to loose ressources.
 * @author david
 *
 */
public class ServerStateSelectRessources extends ServerState {
	int currentPlayerLoosingRessources;

	public ServerStateSelectRessources(IGameController gc,IClient clients[],int currentPlayerLoosingRessources) {
		super(gc,clients);
		this.currentPlayerLoosingRessources = currentPlayerLoosingRessources;
	}
	
	
	public ServerState receivesClientSelect(ClientSelection c){
		try {
			Ressources ressourcesToDiscard = (Ressources)(c.getSelection());
			if(ressourcesToDiscard.greaterThan(gc.getPlayer(currentPlayerLoosingRessources).getRessource())){
				System.err.println("player discards more ressource than what he had");
				return this;
			}
			if((gc.getPlayer(currentPlayerLoosingRessources).getRessource().num())/2 != ressourcesToDiscard.num()){
				System.err.println("player discards wrong number of many ressources");
				return this;
			}
			gc.getPlayer(currentPlayerLoosingRessources).getRessource().remove(ressourcesToDiscard);
			return nextState();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return this;
	}
	
	/**
	 * @return the first player loosing ressources or null if none
	 */
	public static ServerState firstPlayerLoosingRessources(IGameController gc,IClient clients[]){
		int player = gc.getCurrentPlayer().getNum();
		do{
			if(isLoosingRessources(gc,player)) 
				return new ServerStateSelectRessources(gc, clients, player);
			player = (player+1)%gc.getNumPlayer();

		} while(player != gc.getCurrentPlayer().getNum());
		return null;
	}
	
	private ServerState nextState(){
		while(!isLastPlayer(gc,currentPlayerLoosingRessources)){
			currentPlayerLoosingRessources = (currentPlayerLoosingRessources+1)%gc.getNumPlayer();
			if(isLoosingRessources(gc,currentPlayerLoosingRessources)) 
				return new ServerStateSelectRessources(gc, clients, currentPlayerLoosingRessources+1);
		}
		return new ServerStatePlayTurn(gc, clients);
	}

	private static boolean isLoosingRessources(IGameController gc,int currentPlayerLoosingRessources){
		return 7<gc.getPlayer(currentPlayerLoosingRessources).getRessource().num();
	}

	
	private static boolean isLastPlayer(IGameController gc,int currentPlayerLoosingRessources){
		return (currentPlayerLoosingRessources+1)%gc.getNumPlayer() 
				== gc.getCurrentPlayer().getNum();
	}
	
	public int getCurrentPlayerLoosingRessources() {
		return currentPlayerLoosingRessources;
	}
}
