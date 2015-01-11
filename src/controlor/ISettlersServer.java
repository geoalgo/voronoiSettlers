package controlor;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import player.Player;

import controlor.gamestate.GameState;
import controlor.ui.UIControlor;
import delaunay.Pnt;

import model.Model;
import model.card.Card;
import model.hexagonalTiling.SettlersEdge;
import model.ressources.Ressource;
import model.ressources.Ressources;

public interface ISettlersServer  {
	Model getModel(); //xxx remove
	GameState getSet();
	void setSet(GameState state);
	
	public void mouseClicked(Pnt p,int playerId);
	public void keyPressed(KeyEvent e,int playerId);
	public void nextTurnPressed();		


	/**
	 * @param ress ressources to loose chosen by a player after a 7 draw.
	 */
	public void looseRessources(int playerLoosingRess,Ressources ress);
	/**
	 * @param playerToSteal player stealt by current player
	 * @param ress ressource stolen
	 */
	public void stealRandomEnnemyRessource(int playerToSteal);
	/**
	 * @param ress ressource monopolized by current player.
	 */
	public void monopole(Ressource ress);
	public void selectTwoFreeRoads(SettlersEdge road1,SettlersEdge road2);
	//	void playCard(Card c);
	

	
	/**
	 * @return
	 * @throws Exception if the player has not enough ressources or if their
	 * is no card left.
	 */
	Card buyCard() throws Exception;
	void playCard(Card card);
	void internalTrade(int num, Ressource ress, int i, Ressource aquiredRess);
	//call back by the UI after choosing
	
	void updateView();
	
	int getCurrentPlayerNum();
	Player getCurrentPlayer();
	Player getPlayer(int num);

	void setMessage(String txt,int player);
	/**
	 * prompt a message in a client window
	 * @param txt
	 * @param player
	 */
	void appendMessage(String txt,int player);
	/**
	 * @param txt message to be prompt in all client windows
	 */
	void appendMessage(String txt);
	
	void setActivePlayer(int player);
	int getNumPlayer();
}


