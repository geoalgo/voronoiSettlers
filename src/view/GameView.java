package view;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;

import player.Player;

import model.Model;

import controlor.DB;
import controlor.IWindowControlor;
import controlor.SettlersServer;
import controlor.ui.UIControlor;
import controlor.ui.UITrade;
import delaunay.Pnt;

public class GameView extends JFrame implements IWindowControlor{
	SettlersServer settlersServer;
	UIViewControlor uiViewControlor;
	private Model model;
	private BoardView boardView;
	private InfoPanel infoPanel = new InfoPanel();
	private JPanel playerPanelLeft;
	private JPanel playerPanelRight;
	private PlayerPanel[] playerPanel;
	private ControlPanel buildPanel;

	public GameView(SettlersServer sserver,Model model,int width,int height){
		this.settlersServer = sserver;
		this.model = model;
		
		uiViewControlor = new UIViewControlor(sserver,this);
		
		this.boardView = new BoardView(model);
		boardView.addMouseListener(this);
		setLayout(new BorderLayout());
		setSize(width, height);
		this.add(infoPanel, "North");
		this.add(boardView, "Center");
		buildPanel = new ControlPanel(this);
		this.add(buildPanel, "South");
		setPlayerView();
		this.setVisible(true);
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

	@Override
	public void mouseClicked(MouseEvent arg0) {
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
	}

	@Override
	public void setMessage(String txt) {
		infoPanel.setMessage(txt);
	}

	@Override
	public void appendMessage(String txt) {
		infoPanel.appendMessage(txt);
	}

	@Override
	public void updateView() {
		boardView.repaint();
		for(int i = 0; i < playerPanel.length; ++i)
			playerPanel[i].update();
	}

	@Override
	public void setInactive(int player) {
		playerPanel[player].endTurn();
		uiViewControlor.closePlayerWindows();
	}

	@Override
	public void setActive(int player) {
		playerPanel[player].setTurn();
	}
	
	private Pnt convertToCatanCoord(Pnt o){
		Pnt res = new Pnt(o); 
		res.scale(0,1./boardView.getWidth());
		res.scale(1,1./boardView.getHeight());
		return res;
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
	 * If mouse has been pressed inside the delaunayPanel then add a new site.
	 */
	public void mousePressed(MouseEvent e) {
		Pnt pnt = convertToCatanCoord(new Pnt(e.getX(), e.getY()));

		if(!buildPanel.isButton(e.getSource()))
			settlersServer.mouseClicked(pnt,0);
		else{
			handleButton(e.getSource());
		}
		boardView.repaint();
	}
	
	private void handleButton(Object button){
		DB.msg("button pressed");
		if(buildPanel.isNextTurnButton(button)){
			settlersServer.nextTurnPressed();		
			return;
		}
		if(buildPanel.isTradeButton(button)){
			uiViewControlor.tradePressed();
			return;
		}
		if(buildPanel.isBuyCardButton(button)){
			try {
				settlersServer.buyCard();
				updateView();
			} catch (Exception e) {
				appendMessage("Not enough ressources to buy a card.");
			}		
			return;
		}
		if(buildPanel.isPlayCardButton(button)){
//			uicontrolor.playCardPressed();		
			uiViewControlor.playCardPressed();
			return;
		}
	}
	
	

	
	
	
}
