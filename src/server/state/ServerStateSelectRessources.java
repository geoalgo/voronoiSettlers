package server.state;

import player.Player;
import model.card.Card;
import model.ressources.Ressources;
import controlor.DB;
import controlor.IGameController;
import client.DummyClient;
import client.IClient;
import client.action.*;
import client.state.ClientStateRessourcesSelection;
import client.state.ClientStateSelection;

/**
 * State when waiting for the client to loose ressources.
 * @author david
 *
 */
public class ServerStateSelectRessources extends ServerState {
	int currentPlayerLoosingRessources;

	public ServerStateSelectRessources(IGameController gc,IClient clients[],int currentPlayerLoosingRessources) {
		super(gc,clients);
		messageToCurrentPlayer("You have more than 7 cards, select "+7+" cards to discard");
		Player currentPlayer = gc.getPlayer(currentPlayerLoosingRessources);
		messageToAllButCurrentPlayer(currentPlayer.getName()+" has to get rid of half his ressources.");
		clients[currentPlayerLoosingRessources].setCurrentState(new ClientStateRessourcesSelection(currentPlayer));
		this.currentPlayerLoosingRessources = currentPlayerLoosingRessources;
	}
	
	@Override
	public ServerState receivesClientSelect(ClientSelection c){
		try {
			DB.msg("receivesClientSelect");
			Ressources ressourcesToDiscard = (Ressources)(c.getSelection());
			
			DB.msg("choose:"+ressourcesToDiscard);
			DB.msg("player ressources:"+gc.getPlayer(currentPlayerLoosingRessources).getRessource());
			DB.msg("currentPlayerLoosingRessources:"+currentPlayerLoosingRessources);
			if(!gc.getPlayer(currentPlayerLoosingRessources).getRessource().greaterThan(ressourcesToDiscard)){
				System.err.println("player discards more ressource than what he had");
				return this;
			}
			if((gc.getPlayer(currentPlayerLoosingRessources).getRessource().num())/2 != ressourcesToDiscard.num()){
				System.err.println("player discards wrong number of many ressources");
				return this;
			}
			gc.getPlayer(currentPlayerLoosingRessources).getRessource().remove(ressourcesToDiscard);
			updateClientsView();
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
		int player = gc.currentPlayerNum();
		do{
			if(isLoosingRessources(gc,player))
				return new ServerStateSelectRessources(gc, clients, player);
			player = (player+1)%gc.getNumPlayer();

		} while(player != gc.getCurrentPlayer().getNum());
		return null;
	}
	
	private ServerState nextState(){
		DB.msg("init: currentPlayerLoosingRessources:"+currentPlayerLoosingRessources);
		while(!isLastPlayer(gc,currentPlayerLoosingRessources)){
			currentPlayerLoosingRessources = (currentPlayerLoosingRessources+1)%gc.getNumPlayer();
			DB.msg("loop:"+currentPlayerLoosingRessources);
			if(isLoosingRessources(gc,currentPlayerLoosingRessources)) 
				return new ServerStateSelectRessources(gc, clients, currentPlayerLoosingRessources);
		}
		return new ServerStateSelectBrigandPosition(gc, clients);
	}

	private static boolean isLoosingRessources(IGameController gc,int currentPlayerLoosingRessources){
		return gc.getPlayer(currentPlayerLoosingRessources).getRessource().num() > 7;
	}

	
	private static boolean isLastPlayer(IGameController gc,int currentPlayerLoosingRessources){
		return (currentPlayerLoosingRessources+1)%gc.getNumPlayer() 
				== gc.getCurrentPlayer().getNum();
	}
	
	public int getCurrentPlayerLoosingRessources() {
		return currentPlayerLoosingRessources;
	}
}
