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

import controlor.ui.UIControlor;
import controlor.ui.UIWindow;

import model.Model;
import model.InitialRules;
import model.card.Card;
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
public class WindowControlor extends javax.swing.JApplet implements Runnable,SettlersServer{
	Model model;
	GameView view;
	UIControlor uicontrolor;
	GameControlor gameControlor;

	public WindowControlor(){
		setModel();
		setView();
		gameControlor.playLevel();
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
//		setModel();
//		setView();
//		gameControlor.playLevel();
	}

	void setModel() {
		gameControlor = new GameControlor(new InitialRules(2, 10));
		uicontrolor = new UIWindow(this,gameControlor);
		gameControlor.setUIControlor(uicontrolor);
		this.model = gameControlor.getModel();
	}

	void setView(){
		this.view = new GameView(this,model, 600, 1000);
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
		uicontrolor.mousePressed(p);
	}

	@Override
	public void keyPressed(KeyEvent e, int playerId) {
	}


	@Override
	public void nextTurnPressed() {
		uicontrolor.nextTurnPressed();
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
	public void selectCard(Card c) {
		uicontrolor.selectCard(c);
	}

	@Override
	public void buyCard() {
		uicontrolor.buyCardPressed();
	}
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					WindowControlor window = new WindowControlor();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	
}
