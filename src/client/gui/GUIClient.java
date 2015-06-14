package client.gui;

import java.awt.event.KeyEvent;

import org.hamcrest.core.IsInstanceOf;

import controlor.DB;
import model.Model;
import model.ressources.Ressources;
import player.Player;
import view.GameView;
import client.DummyClient;
import client.IClient;
import client.ISendToServer;
import client.action.ClientAction;
import client.action.ClientActionClick;
import client.action.ClientActionKey;
import client.action.ClientRessourcesSelection;
import client.action.ClientSelection;
import client.state.ClientState;
import client.state.ClientStatePositionSelection;
import client.state.ClientStateRessourcesSelection;
import client.state.ClientStateSelection;

public class GUIClient extends DummyClient{
	private ISendToServer server;
	private Player player;
	ClientState currentState;
	GameView view;
	
	GUIInternalTrade guiInternalTrade;
	
	public GUIClient(ISendToServer server,Player p){
		super(server, p);
		view = new GameView(this,p.getName(),600,1000);
	}

	@Override
	public void updateView() {
		view.updateView();
	}

	@Override
	public void message(String msg) {
		System.out.println("Client: "+msg);
		view.appendMessage(msg);
	}

	
	@Override
	protected void askTradeSelection() {
		guiInternalTrade = new GUIInternalTrade(this);
	}
	
	@Override
	protected void askRessourcesSelection(ClientStateRessourcesSelection cs) {
		DB.msg("select ressources GUI");
		new GUILooseRessource(this,getPlayer());
	}

	@Override
	protected void askSelection(ClientStateSelection cs) {
		new GUISelection(cs,this,cs.canBeCanceled);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		sendAction(new ClientActionKey(this, e));
	}


	
}
