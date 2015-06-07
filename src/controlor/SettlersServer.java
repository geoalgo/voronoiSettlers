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

package controlor;

//import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import org.hamcrest.core.IsInstanceOf;

import controlor.gamestate.GameState;
import controlor.gamestate.PlayTurn;
import controlor.ui.UIControlor;
import controlor.ui.UIWindow;

import model.Model;
import model.InitialRules;
import model.NotEnoughRessourceException;
import model.card.Card;
import model.card.CardState;
import model.card.FreeRoad;
import model.card.Knight;
import model.card.CardKnightState;
import model.card.Monopole;
import model.card.MonopoleState;
import model.card.VictoryPoint;
import model.hexagonalTiling.SettlersEdge;
import model.ressources.Ressource;
import model.ressources.Ressources;
import player.Player;
import view.BoardView;
import view.ControlPanel;
import view.GameView;
import view.InfoPanel;
import view.PlayerPanel;
import delaunay.Pnt;

/**
 * Main class.
 * 
 * @author David Salinas
 * 
 */
@SuppressWarnings("serial")
public abstract class SettlersServer extends javax.swing.JApplet implements Runnable,ISettlersServer{
//	Model model;
//	GameView view;
//	UIControlor uicontrolor;
//	GameController gc;
//
//	public SettlersServer(){
//		setModel(3);
//		setView();
//		run();
//	}
//
//	@Override
//	public void init() {
//		try {
//			SwingUtilities.invokeAndWait(this);
//		} catch (Exception e) {
//			System.err.println("Initialization failure");
//			e.printStackTrace();
//		}
//	}
//
//	@Override
//	public void run() {
//		gc.playLevel();
//	}
//
//	public GameState getSet(){
//		return gc.getState();
//	}
//	
//	void setNormalState(){
//		gc.setState(new PlayTurn(gc, getCurrentPlayerNum()));
//	}
//	
//	
//	@Override
//	public void setSet(GameState state){
//		gc.setState(state);
//	}
//	
//	void setModel(int numPlayers) {
//		gc = new GameController(new InitialRules(numPlayers, 10));
//		uicontrolor = new UIWindow(this,gc);
//		gc.setUIControlor(uicontrolor);
//		gc.setServerControlor(this);
//		this.model = gc.getModel();
//	}
//
//	void setView(){
////		this.view = new GameView(this,600, 1000);
//	}
//
//	public void updateView(){
//		view.updateView();
//	}
//
//	public void setMessage(String txt) {
//		view.setMessage(txt);
//	}
//
//	public void appendMessage(String txt) {
//		view.appendMessage(txt);
//	}
//
//	@Override
//	public Model getModel() {
//		return model;
//	}
//
//	@Override
//	public void mouseClicked(Pnt p,int playerId) {
//		gc.getState().click(p);
//	}
//
//	@Override
//	public void keyPressed(KeyEvent e) {
//		if(e.getKeyChar()=='c'){
//			DB.msg("cheat mode add ressources");
//			for(Ressource ress : Ressource.allRessources())
//				gc.getCurrentPlayer().getRessource().add(ress, 3);
//			updateView();
//		}
//		if(e.getKeyChar()=='d'){
//			DB.msg("current state"+gc.getState());
//		}
//	}
//
//	@Override
//	public void nextTurnPressed() {
//		DB.msg("next turn pressed");
//		DB.msg("current state"+gc.getState());
//		view.setInactive(gc.currentPlayerNum());
//		gc.nextPlayer();
//		view.setActive(gc.currentPlayerNum());
//	}
//
//	@Override
//	public void internalTrade(int player,Ressource tradedRessource,
//			int numTradedRessource,Ressource obtainedRessource){
//		Player p = gc.getPlayer(player);
//		p.getRessource().add(tradedRessource,numTradedRessource);
//		p.getRessource().add(obtainedRessource,1);
//	}
//
//	@Override
//	public Card buyCard() throws Exception {
//		return gc.buyCard();
//	}
//
//	@Override
//	public void playCard(Card card) {
//		if(card instanceof VictoryPoint) 
//			card.apply(null); //todo ugly case, refactor
//		else
//			gc.setState(CardState.makeCardState(gc, card));
//		getCurrentPlayer().removeCard(card);
//		view.updateView();
//	}
//
//	public void applyCardEffect(Card card,Object selection){
//		DB.msg("apply card effect");
//		gc.getState().apply(selection); // the game state is necessarily a card state
//		getCurrentPlayer().removeCard(card);
//		setNormalState();
//		view.updateView();
//	}
//
//	@Override
//	public int getNumPlayer(){
//		return gc.getNumPlayer();
//	}
//	
//	@Override
//	public int getCurrentPlayerNum() {
//		return gc.getCurrentPlayer().getNum();
//	}
//
//	@Override
//	public Player getPlayer(int num) {
//		return gc.getPlayer(num);
//	}
//
//	@Override
//	public Player getCurrentPlayer() {
//		return getPlayer(getCurrentPlayerNum());
//	}
//	
//
//	@Override
//	public void setMessage(String txt, int player) {
//		view.setMessage(txt);
//	}
//
//	@Override
//	public void appendMessage(String txt, int player) {
//		view.appendMessage(txt);		
//	}
//
//	@Override
//	public void setActivePlayer(int player){
//		for(int i = 0 ; i < gc.getNumPlayer(); ++i)
//			if(i==player) view.setActive(i);
//			else view.setInactive(i);
//	}
//
//	@Override
//	public void looseRessources(int playerLoosingRess, Ressources ress) {
//		DB.msg("num pl:"+playerLoosingRess);
//		gc.getPlayer(playerLoosingRess).getRessource().remove(ress);
//		view.appendMessage(getPlayer(playerLoosingRess).getName()+" looses half his ressources.");
//	}
//
//	@Override
//	public void monopole(Ressource ress) {
//		int currentPlayer = getCurrentPlayerNum();
//		for(int i = 0; i< getNumPlayer();++i){
//			if(i!=currentPlayer){
//				stealAllPlayerRessource(getCurrentPlayer(),gc.getPlayer(i),ress);
//			}
//		}		
//		view.appendMessage(getCurrentPlayer().getName()+" plays a monopole on "+ress);
//		setNormalState();
//	}
//
//	private void stealAllPlayerRessource(Player stealer, Player screwed, Ressource r){
//		int numRess = screwed.getRessource().getNum(r);
//		DB.msg("steal "+numRess+ " ressources of "+r);
//		screwed.getRessource().add(r, -numRess);
//		stealer.getRessource().add(r, numRess);
//		view.updateView();
//	}
//
//	@Override
//	public void stealRandomEnnemyRessource(int playerToSteal) {
//		gc.steal(getCurrentPlayer(),getPlayer(playerToSteal));
//		setNormalState();
//	}
//	
//	@Override
//	public void selectTwoFreeRoads(SettlersEdge road1, SettlersEdge road2) {
//		// TODO Auto-generated method stub
//		setNormalState();
//	}	
//
//	public static void main(String[] args) {
//		EventQueue.invokeLater(new Runnable() {
//			public void run() {
//				try {
//					SettlersServer window = new SettlersServer();
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		});
//	}

}
