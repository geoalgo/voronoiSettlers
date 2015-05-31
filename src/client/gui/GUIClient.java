package client.gui;

import model.Model;
import player.Player;
import view.GameView;
import client.DummyClient;
import client.IClient;
import client.ISendToServer;
import client.action.ClientAction;
import client.state.ClientState;

public class GUIClient extends DummyClient{
	private ISendToServer server;
	private Player player;
	ClientState currentState;
	GameView view;
	
	
	public GUIClient(ISendToServer server,Player p){
		super(server, p);
		view = new GameView(this, 600,1000);
	}

	@Override
	public void updateModel(Model newModel){
		model = newModel;
	}
	
	@Override
	public void updateView() {
		view.updateView();
	}

}
