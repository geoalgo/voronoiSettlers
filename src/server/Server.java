package server;

import player.Player;
import model.InitialRules;
import model.Model;
import controlor.DB;
import controlor.GameController;
import controlor.IGameController;
import server.state.*;
import client.DummyClient;
import client.IClient;
import client.action.ClientAction;

public class Server implements IServer {
	private ServerState currentState;
	private IClient clients[];
	private IGameController gc;
	
	public Server(IGameController gc){
		this.gc = gc;
	}
	
	public Server(InitialRules rules){
		this.gc = new GameController(rules);
	}
	

	
	@Override
	public boolean hasClients(){
		return currentState != null;
	}
	
	@Override
	public void init(IClient clients[]){
		this.clients = clients;
		currentState = new ServerStateFirstColony(gc, clients);
	}
	
	@Override
	public void receiveAction(ClientAction action) {
		try {
			currentState = currentState.receivesAction(action);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public ServerState getCurrentState() {
		return currentState;
	}
	
	@Override
	public void setState(ServerState s){
		currentState = s;
	}

	@Override
	public Model getModel() {
		return gc.getModel();
	}

	@Override
	public Player getPlayer(int i) {
		return gc.getPlayer(i);
	}

	@Override
	public int getCurrentPlayer(){
		return gc.getCurrentPlayer().getNum();
	}
}
