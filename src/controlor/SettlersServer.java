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

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import controlor.gamestate.GameState;
import controlor.ui.UIControlor;
import controlor.ui.UIWindow;

import model.Model;
import model.InitialRules;
import model.NotEnoughRessourceException;
import model.card.Card;
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
public class SettlersServer extends javax.swing.JApplet implements Runnable,ISettlersServer{
	Model model;
	GameView view;
	UIControlor uicontrolor;
	GameControlor gc;

	public SettlersServer(){
		setModel();
		setView();
		run();
	}

	@Override
	public void init() {
		try {
			SwingUtilities.invokeAndWait(this);
		} catch (Exception e) {
			System.err.println("Initialization failure");
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		gc.playLevel();
	}

	public GameState getSet(){
		return gc.getSet();
	}
	
	void setModel() {
		gc = new GameControlor(new InitialRules(2, 10));
		uicontrolor = new UIWindow(this,gc);
		gc.setUIControlor(uicontrolor);
		gc.setServerControlor(this);
		this.model = gc.getModel();
	}

	void setView(){
		this.view = new GameView(this,600, 1000);
	}

	public void updateView(){
		view.updateView();
	}

	public void setMessage(String txt) {
		view.setMessage(txt);
	}

	public void appendMessage(String txt) {
		view.appendMessage(txt);
	}

	@Override
	public Model getModel() {
		return model;
	}

	@Override
	public void mouseClicked(Pnt p,int playerId) {
		if(playerId == gc.currentPlayerNum())
			gc.getSet().click(p);
	}

	@Override
	public void keyPressed(KeyEvent e, int playerId) {
	}

	@Override
	public void nextTurnPressed() {
		DB.msg("next turn pressed");
		view.setInactive(gc.currentPlayerNum());
		gc.endTurn();
		view.setActive(gc.currentPlayerNum());
	}


	
	@Override
	public void looseRessources(Ressources ress) {
		uicontrolor.looseRessources(ress);		
	}

	@Override
	public void stealEnnemy(int playerToSteal) {
		uicontrolor.stealEnnemy(playerToSteal);
	}

	@Override
	public void playCard(Card c) {
		view.appendMessage(gc.currentPlayer().getName()+" plays a "+c);
		GameState stateToRestore = gc.getSet();
		DB.msg("state to restore:"+stateToRestore);
		gc.applyCard(c);
		view.appendMessage(gc.currentPlayer().getName()+" releases the card "+c);
	}

	@Override
	public void internalTrade(int player,Ressource tradedRessource,
			int numTradedRessource,Ressource obtainedRessource){
		Player p = gc.getPlayer(player);
		p.getRessource().add(tradedRessource,numTradedRessource);
		p.getRessource().add(obtainedRessource,1);
	}

	@Override
	public Card buyCard() throws Exception {
		Ressources cardCost = new Ressources();
		cardCost.addCrop(1);
		cardCost.addStone(1);
		cardCost.addSheep(1);

		boolean enoughRessource = gc.currentPlayer().getRessource().greaterThan(cardCost);
		if(!enoughRessource)
			throw new NotEnoughRessourceException();
		else{
			Card res = gc.giveRandomCard();
			gc.currentPlayer().getRessource().remove(cardCost);
			gc.currentPlayer().addCard(res);
			return res;
		}
	}


	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SettlersServer window = new SettlersServer();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	@Override
	public int getCurrentPlayerNum() {
		return gc.currentPlayer().getNum();
	}

	@Override
	public Player getPlayer(int num) {
		return gc.getPlayer(num);
	}

	@Override
	public Player getCurrentPlayer() {
		return getPlayer(getCurrentPlayerNum());
	}

	@Override
	public void setMessage(String txt, int player) {
		view.setMessage(txt);
	}

	@Override
	public void appendMessage(String txt, int player) {
		view.appendMessage(txt);		
	}

	@Override
	public void setActivePlayer(int player){
		for(int i = 0 ; i < gc.numPlayer(); ++i)
			if(i==player) view.setActive(i);
			else view.setInactive(i);
	}	


}
