package view;

import java.util.Collection;

import model.card.Card;
import model.card.CardState;
import model.card.FreeRoad;
import model.card.Knight;
import model.card.CardKnightState;
import model.card.Monopole;
import model.card.MonopoleState;
import model.card.VictoryPoint;
import model.ressources.Ressources;
import player.Player;
import controlor.DB;
import controlor.GameController;
import controlor.ISettlersServer;
import controlor.gamestate.GameState;
import controlor.gamestate.ThiefSelect;
import controlor.ui.UIChoosePlayerToSteal;
import controlor.ui.UISelectCard;
import controlor.ui.UITrade;
import delaunay.Pnt;

public class UIViewControlor {
	ISettlersServer server;
	CardState cardState = null;
	UITrade uiTrade = null;
	UISelectCard uiSelectCard = null;
	GameView view;
	GameState callBackState;

	UIViewControlor(ISettlersServer server,GameView view){
		this.server = server;
		this.view = view;
	}

	public void	tradePressed(){
		DB.msg("trade menu");
		uiTrade = new UITrade(server,server.getCurrentPlayer());
	}

	public void closePlayerWindows(){
		if(uiTrade!=null){
			uiTrade.done();
			uiTrade = null;
		}
		if(uiSelectCard!=null){
			uiSelectCard.done();
			uiSelectCard = null;
		}
	}

	public void playCardPressed(){
		Player p = server.getCurrentPlayer();
		if(p.numCards() != 0){
			uiSelectCard = new UISelectCard(this, p);
		}
		else{
			view.appendMessage("No card");
		}
	}


	
	public void buyCardPressed(){
		try{
			Card card = server.buyCard();
			view.appendMessage(server.getCurrentPlayer().getName()+" bough a card");
			view.appendMessage(server.getCurrentPlayer().getName()+" got a "+card);
			view.updateView();
		} catch (Exception e) {
			view.appendMessage("Not enough ressource to build a card. You need at least one sheep, one stone and one crop");
		}
	}

	public boolean isIncardState(){
		return cardState != null;
	}
	
	public void click(Pnt pnt){
		cardState.click(pnt);
	}

	
	public void playCard(Card card) {
		server.playCard(card);
	}
	

//	void chooseEnnemyToSteal(ThiefSelect stealState,Collection<Player> ennemies){
////		DB.msg("show chooser");
////		UIChoosePlayerToSteal chooser = new UIChoosePlayerToSteal(this,ennemies);
////		chooser.setVisible(true);
////		callBackState = stealState;
//	}
////	
//	public void stealEnnemy(int playerToSteal) {
//		callBackState.apply(playerToSteal);
//	}
}
