package controlor;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import player.Player;

import controlor.ui.UIControlor;
import delaunay.Pnt;

import model.Model;
import model.card.Card;
import model.ressources.Ressource;
import model.ressources.Ressources;

public interface SettlersServer  {
	Model getModel(); //xxx remove

	public void mouseClicked(Pnt p,int playerId);
	public void keyPressed(KeyEvent e,int playerId);
	public void nextTurnPressed();		

	//call back by the UI after choosing
	public void looseRessources(Ressources ress);
	public void stealEnnemy(int playerToSteal);
	void playCard(Card c);
	
	/**
	 * @return
	 * @throws Exception if the player has not enough ressources or if their
	 * is no card left.
	 */
	Card buyCard() throws Exception;
	void internalTrade(int num, Ressource ress, int i, Ressource aquiredRess);
	//call back by the UI after choosing
	
	void updateView();
	
	int getCurrentPlayerNum();
	Player getCurrentPlayer();
	Player getPlayer(int num);

	void setMessage(String txt,int player);
	void appendMessage(String txt,int player);
	
	void setActivePlayer(int player);
}


