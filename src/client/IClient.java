package client;

import java.awt.event.KeyEvent;

import model.Model;
import model.card.Card;
import model.ressources.Ressource;
import client.action.ClientAction;
import client.state.ClientState;
import client.state.ClientStateRessourcesSelection;
import client.state.ClientStateSelection;
import delaunay.Pnt;
import player.Player;
import server.IServer;

public abstract class IClient {
	/**
	 * Sends message to the client.
	 * @param string
	 */
	public abstract void message(String string);
	public abstract void updateView();
	public abstract ClientState getCurrentState();

	
	// called by server
	public void setCurrentState(ClientState cs) {
		if(cs instanceof ClientStateSelection)
			askSelection((ClientStateSelection)cs);
		if(cs instanceof ClientStateRessourcesSelection)
			askRessourcesSelection((ClientStateRessourcesSelection)cs);
	}
	
	abstract protected void askRessourcesSelection(ClientStateRessourcesSelection cs);
	abstract protected void askSelection(ClientStateSelection cs);
	
	public abstract Player getPlayer();

	//called by gui
	public abstract void mouseClicked(Pnt pnt);
	public abstract void keyPressed(KeyEvent arg0);
	public abstract void nextTurnPressed();
	public abstract void buyCard();
	public abstract void playCard();
	public abstract void tradePressed();
	
	//to server
	public abstract void sendAction(ClientAction a);
	public abstract Model getModel();
	abstract void updateModel(Model newModel);
		// TODO Auto-generated method stub
	protected abstract void askTradeSelection();
		
}
