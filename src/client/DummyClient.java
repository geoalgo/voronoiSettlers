package client;

import java.awt.event.KeyEvent;

import controlor.DB;
import model.Model;
import model.hexagonalTiling.BoardTiles;
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
	GameClientState model;
	ClientState currentState;

	public DummyClient(ISendToServer server,Player p){
		this.player = p;
		this.server = server;
		updateModel(server.getModel());
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
	public GameClientState getModel() {
		return model;
	}
	
	@Override
	public BoardTiles getBoard() {
		return server.getModel().board();
	}


	// maybe just updates incrementally if too slow
	// because of bandwith
	@Override
	public void updateModel(Model newModel){
		model = new GameClientState(newModel.board(),newModel.getPlayers());
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
		DB.msg("select ressources");
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

	@Override
	protected void askTradeSelection() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int numPlayers() {
		// TODO Auto-generated method stub
		return model.players.getNumAlive();
	}

	@Override
	public Player getPlayer(int i) {
		// TODO Auto-generated method stub
		return model.players.getPlayer(i);
	}


}
