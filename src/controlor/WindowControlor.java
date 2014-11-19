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
import player.Player;
import view.BoardView;
import view.ControlPanel;
import view.InfoPanel;
import view.PlayerPanel;
import delaunay.Pnt;

/**
 * Show the game.
 * 
 * @author David Salinas
 * 
 */
@SuppressWarnings("serial")
public class WindowControlor extends javax.swing.JApplet 
implements IWindowControlor{
	
	Model model;
	UIControlor uicontrolor;
	GameControlor gameControlor;

	
	private int width = 1300;
	private int heigth = 1000;

	private boolean debug = true; // Used for debugging
	private Component currentSwitch = null; // Entry-switch that mouse is in

	private static String windowTitle = "Settlers";
	private BoardView boardView;

	private InfoPanel infoPanel = new InfoPanel();
	
	private JPanel playerPanelLeft;
	private JPanel playerPanelRight;

	private PlayerPanel[] playerPanel;
	
	private ControlPanel buildPanel;

	/**
	 * Main program (used when run as application instead of applet).
	 */
	public static void main(String[] args) {
		initApplication(new WindowControlor());
	}

	public static void initApplication(WindowControlor windowControlor){
//		System.out.println("Create the window controlor");
//		System.out.println("Init the window controlor");
		windowControlor.init(); // Applet initialization
//		System.out.println("Done init the window controlor");
//
//		System.out.println("Init the window UI");
		JFrame dWindow = new JFrame(); // Create window
//		dWindow.setSize(windowControlor.width(), windowControlor.heigth()); // Set
																			// size
		dWindow.setTitle(windowTitle); // Set window title
		dWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		System.out.println("Added window controlor");

		dWindow.add(windowControlor); // Place applet into window
		dWindow.setVisible(true); // Show the window
//		dWindow.setSize(800, 1000);
//		dWindow.setResizable(false);
		dWindow.pack();
		windowControlor.run();
		
		windowControlor.repaint();
	}
	
	public int width() {
		return width;
	}

	public int heigth() {
		return heigth;
	}

	/**
	 * Initialize the applet. As recommended, the actual use of Swing components
	 * takes place in the event-dispatching thread.
	 */
	public void init() {
		try {
			SwingUtilities.invokeAndWait(this);
		} catch (Exception e) {
			System.err.println("Initialization failure");
			e.printStackTrace();
		}
	}

	/**
	 * Set up the applet's GUI. 
	 * As recommended, the init method executes this in
	 * the event-dispatching thread.
	 */
	public void run() {
		setModel();
		setView();
		gameControlor.playLevel();
	}

	void setModel() {
		gameControlor = new GameControlor(new InitialRules(3, 10));
		uicontrolor = new UIWindow(this,gameControlor);
		gameControlor.setUIControlor(uicontrolor);
		this.model = gameControlor.getModel();
		this.boardView = new BoardView(this, gameControlor.getModel());
		boardView.addMouseListener(this);
	}

	void setView() {
		setLayout(new BorderLayout());
//		setPreferredSize(new Dimension(1000,1000));
		setSize(600, 1000);
		this.add(infoPanel, "North");
		this.add(boardView, "Center");
		buildPanel = new ControlPanel(this);
		this.add(buildPanel, "South");
		setPlayerView();
	}
	
	void setPlayerView(){
		playerPanelLeft = new JPanel();
		add(playerPanelLeft,"West");
		playerPanelLeft.setLayout(new GridLayout(2,1));
		
		playerPanelRight = new JPanel();
		add(playerPanelRight,"East");
		playerPanelRight.setLayout(new GridLayout(2,1));

		int numPlayers = model.numPlayers();
		playerPanel = new PlayerPanel[numPlayers];

		for(int i = 0; i<numPlayers; ++i){
			Player p = model.getPlayer(i);
			playerPanel[i] = new PlayerPanel(p);
			if(i<2)
				playerPanelLeft.add(playerPanel[i]);
			else 
				playerPanelRight.add(playerPanel[i]);
		}
	}

	/**
	 * A button has been pressed; redraw the picture.
	 */
	public void actionPerformed(ActionEvent e) {
		if(buildPanel.isButton(e.getSource()))
			handleButton(e.getSource());
		boardView.repaint();
	}

	/**
	 * If entering a mouse-entry switch then redraw the picture.
	 */
	public void mouseEntered(MouseEvent e) {
		currentSwitch = e.getComponent();
		if (currentSwitch instanceof JLabel)
			boardView.repaint();
		else
			currentSwitch = null;
	}

	/**
	 * If exiting a mouse-entry switch then redraw the picture.
	 */
	public void mouseExited(MouseEvent e) {
		currentSwitch = null;
		if (e.getComponent() instanceof JLabel)
			boardView.repaint();
	}

	private Pnt convertToCatanCoord(Pnt o){
		Pnt res = new Pnt(o); 
		res.scale(0,1./boardView.getWidth());
		res.scale(1,1./boardView.getHeight());
		return res;
	}

	
	/**
	 * If mouse has been pressed inside the delaunayPanel then add a new site.
	 */
	public void mousePressed(MouseEvent e) {
		Pnt pnt = convertToCatanCoord(new Pnt(e.getX(), e.getY()));

		if(!buildPanel.isButton(e.getSource()))
			uicontrolor.mousePressed(pnt);
		else{
			handleButton(e.getSource());
		}
		boardView.repaint();
	}
	
	private void handleButton(Object button){
		DB.msg("button pressed");
		if(buildPanel.isNextTurnButton(button)){
			uicontrolor.nextTurnPressed();		
			return;
		}
		if(buildPanel.isTradeButton(button)){
			uicontrolor.tradePressed();		
			return;
		}
		if(buildPanel.isBuyCardButton(button)){
			uicontrolor.buyCardPressed();		
			return;
		}
		if(buildPanel.isPlayCardButton(button)){
			uicontrolor.playCardPressed();		
			return;
		}
	}

	/**
	 * Not used, but needed for MouseListener.
	 */
	public void mouseReleased(MouseEvent e) {
	}

	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void updateView(){
		boardView.repaint();
		for(int i = 0; i < playerPanel.length; ++i)
			playerPanel[i].update();
	}
	
	@Override
	public void setMessage(String txt) {
		infoPanel.setMessage(txt);
	}
	
	@Override
	public void appendMessage(String txt) {
		infoPanel.appendMessage(txt);
	}

}
