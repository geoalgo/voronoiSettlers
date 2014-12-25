/**
* Voronoi settlers- An implementation of the board game Settlers of 
* Catan.
* This file Copyright (C) 2013-2014 David Salinas <catan.100.sisisoyo@spamgourmet.com>
*
* This program is free software; you can redistribute it and/or
* modify it under the terms of the GNU General Public License
* as published by the Free Software Foundation; either version 3
* of the License, or (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program. If not, see <http://www.gnu.org/licenses/>.
*
* The maintainer of this program can be reached at catan.100.sisisoyo@spamgourmet.com
**/

package controlor.ui;


import java.awt.event.MouseEvent;
import java.util.Collection;
import java.util.List;

import com.sun.corba.se.spi.ior.MakeImmutable;

import model.card.Card;
import model.card.CardState;
import model.card.FreeRoad;
import model.card.Knight;
import model.card.KnightState;
import model.card.Monopole;
import model.card.MonopoleState;
import model.card.VictoryPoint;
import model.card.VictoryPointState;
import model.ressources.Ressources;
import player.Player;
import controlor.DB;
import controlor.GameControlor;
import controlor.WindowControlor;
import controlor.gamestate.GameState;
import controlor.gamestate.LooseRessource;
import controlor.gamestate.PlayTurn;
import controlor.gamestate.ThiefSelect;
import delaunay.Pnt;


public class UIWindow implements UIControlor {

	WindowControlor parentWindow;
	GameControlor gc;

	// state that is waiting a callback with done()
	GameState callBackState;
	UITrade uiTrade;
	UISelectCard uiSelectCard = null;

	public UIWindow(WindowControlor parentWindow,
			GameControlor gc ){
		this.parentWindow = parentWindow;
		this.gc = gc;
		callBackState = null;
		uiTrade = null;
	}

	@Override
	public void mousePressed(Pnt pnt) {
		gc.getSet().click(pnt);
	}

	@Override
	public void setParentWindowMsg(String s) {
		parentWindow.setMessage(s+"\n");
	}

	@Override
	public void appendParentWindowMsg(String s) {
		parentWindow.appendMessage(s+"\n");
	}

	@Override
	public void updateView() {
		parentWindow.updateView();
	}

	@Override
	public void nextTurnPressed() {
		DB.msg("next turn pressed");
		closePlayerWindows();
		setInactivePlayer(gc.currentPlayerNum());
		gc.endTurn();
		setActivePlayer(gc.currentPlayerNum());
	}

	private void closePlayerWindows(){
		if(uiTrade!=null){
			uiTrade.done();
			uiTrade = null;
		}
		if(uiSelectCard!=null){
			uiSelectCard.done();
			uiSelectCard = null;
		}
	}

	@Override
	public void	tradePressed(){
		uiTrade = new UITrade(gc.currentPlayer(),gc);
	}

	@Override
	public void buyCardPressed(){
		Ressources cardCost = new Ressources();
		cardCost.addCrop(1);
		cardCost.addStone(1);
		cardCost.addSheep(1);
		boolean enoughRessource = gc.currentPlayer().getRessource().greaterThan(cardCost);
		if(!enoughRessource)
			appendParentWindowMsg("Not enough ressource to build a card. You need at least one sheep, one stone and one crop");
		else{
			try {
				Card card = gc.giveRandomCard();
				gc.getUIControlor().appendParentWindowMsg(gc.currentPlayer().getName()+" bough a card");
				gc.getUIControlor().appendParentWindowMsg(gc.currentPlayer().getName()+" got a "+card);
				gc.currentPlayer().getRessource().remove(cardCost);
				gc.currentPlayer().addCard(card);
				gc.getUIControlor().updateView();
			} catch (Exception e) {
				gc.getUIControlor().appendParentWindowMsg("No more cards...");
			}
		}
	}

	@Override
	public void playCardPressed(){
		Player p = gc.currentPlayer();
		if(p.numCards() != 0){
			uiSelectCard = new UISelectCard(this, p);
		}
		else{
			appendParentWindowMsg("No card");
		}
	}

	@Override
	public void selectRessourcesToLoose(
			Player p,LooseRessource currentState){
		callBackState = currentState;
		UILooseRessource.createAndShowGUI(this,p);
	}

	@Override
	public void looseRessources(Ressources ress){
		//move to done method
		callBackState.apply(ress);
	}

	@Override
	public void chooseEnnemyToSteal(ThiefSelect state,Collection<Player> ennemies) {
		DB.msg("show chooser");
		UIChoosePlayerToSteal chooser = new UIChoosePlayerToSteal(this,ennemies);
		chooser.setVisible(true);
		callBackState = state;
	}

	@Override
	public void stealEnnemy(int playerToSteal) {
		callBackState.apply(playerToSteal);
	}

	//call back when choosing a card
	@Override
	public void selectCard(Card c){
		appendParentWindowMsg(gc.currentPlayer().getName()+" plays a "+c);
		GameState stateToRestore = gc.getSet();
		DB.msg("state to restore:"+stateToRestore);
		applyCard(c);
//		gc.setSet(CardState.makeCardState(gc, stateToRestore, c));
		gc.currentPlayer().removeCard(c);
		gc.reputCard(c);
		appendParentWindowMsg(gc.currentPlayer().getName()+" releases the card "+c);
	}

	private void applyCard(Card card){
		GameState stateToRestore = gc.getSet();
		if(card instanceof Monopole){
			//aaa do static cleaner
			new MonopoleState(gc, stateToRestore, (Monopole)card);
		}
		if(card instanceof Knight){
			//aaa do static cleaner
			new KnightState(gc, stateToRestore, (Knight)card);
		}
		if(card instanceof VictoryPoint){
			card.apply(gc,null);
		}
		if(card instanceof FreeRoad){
			card.apply(gc,null);
		}
	}
	
	@Override
	public void setActivePlayer(int player) {
		parentWindow.setTurn(player);
	}

	@Override
	public void setInactivePlayer(int player) {
		parentWindow.setEndTurn(player);
	}

}
