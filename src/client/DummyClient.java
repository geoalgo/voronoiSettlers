package client;

import java.awt.event.KeyEvent;

import model.Model;
import client.action.ClientAction;
import client.action.ClientActionClick;
import client.action.ClientBuyCard;
import client.action.ClientNextTurn;
import client.action.ClientPlayCard;
import client.state.ClientState;
import client.state.ClientStateRessourcesSelection;
import client.state.ClientStateSelection;
import delaunay.Pnt;
import player.Player;
import server.IServer;

public class DummyClient extends IClient {
	private ISendToServer server;
	private Player player;
	protected Model model;
	ClientState currentState;

	public DummyClient(ISendToServer server,Player p){
		this.player = p;
		this.server = server;
	}

	@Override
	public void message(String msg) {
		System.out.println("Client: "+msg);
	}

	@Override
	public void updateView() {
	}

	@Override
	public Player getPlayer() {
		return player;
	}

	@Override
	public ClientState getCurrentState() {
		return currentState;
	}

	@Override
	public void sendAction(ClientAction action) {
		server.receiveAction(action);
	}

	@Override
	public Model getModel() {
		return server.getModel();
	}

	// maybe just updates incrementally if too slow
	// because of bandwith
	@Override
	public void updateModel(Model newModel){
		model = newModel;
	}

	@Override
	public void mouseClicked(Pnt pnt) {
		server.receiveAction(new ClientActionClick(this,pnt));
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void nextTurnPressed() {
		server.receiveAction(new ClientNextTurn(this));

	}

	@Override
	public void buyCard() {
		server.receiveAction(new ClientBuyCard(this));
	}

	@Override
	protected void askRessourcesSelection(ClientStateRessourcesSelection cs) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void askSelection(ClientStateSelection cs) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void playCard() {
		sendAction(new ClientPlayCard(this));
	}

	@Override
	public void tradePressed() {
		// TODO Auto-generated method stub
		
	}


}
