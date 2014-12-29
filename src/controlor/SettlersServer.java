package controlor;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import controlor.ui.UIControlor;
import delaunay.Pnt;

import model.Model;
import model.card.Card;
import model.ressources.Ressources;

public interface SettlersServer  {
	Model getModel();

	public void mouseClicked(Pnt p,int playerId);

	public void keyPressed(KeyEvent e,int playerId);

	public void nextTurnPressed();		

	//call back by the UI after choosing
	public void looseRessources(Ressources ress);
	public void stealEnnemy(int playerToSteal);
	void selectCard(Card c);
	void buyCard();
	//call back by the UI after choosing
	
	
}
