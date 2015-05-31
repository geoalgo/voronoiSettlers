package client;

import java.awt.event.KeyEvent;

import model.Model;
import model.card.Card;
import model.ressources.Ressource;
import client.action.ClientAction;
import client.state.ClientState;
import delaunay.Pnt;
import player.Player;
import server.IServer;

public interface IClient {
	/**
	 * Sends message to the client.
	 * @param string
	 */
	void message(String string);
	void updateView();
	ClientState getCurrentState();
	void setCurrentState(ClientState cs);
	Player getPlayer();

	//called by gui
	void mouseClicked(Pnt pnt);
	void keyPressed(KeyEvent arg0);
	void nextTurnPressed();
	void buyCard();
	
	
	//to server
	void sendAction(ClientAction a);
	Model getModel();
	void updateModel(Model newModel);
}
