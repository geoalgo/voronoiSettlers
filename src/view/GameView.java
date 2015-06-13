package view;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import javax.swing.JFrame;
import javax.swing.JPanel;

import client.IClient;
import player.Player;
import controlor.DB;
import controlor.IWindowController;
import delaunay.Pnt;

public class GameView implements IWindowController{
	private JFrame frame;
	IClient client;
	private BoardView boardView;
	private InfoPanel infoPanel = new InfoPanel();
	private JPanel playerPanelLeft;
	private JPanel playerPanelRight;
	private PlayerPanel[] playerPanel;
	private ControlPanel buildPanel;

	public GameView(IClient client,String name,int width,int height){
		this.client = client;
		this.boardView = new BoardView(client);
		
		boardView.addMouseListener(this);
		
		setView(name,width,height);
		
		frame.addKeyListener(this);
		frame.setFocusable(true);
		frame.requestFocusInWindow();
		
		infoPanel.setFocusable(false);
	}
	
	private void setView(String name,int width,int height){
		frame = new JFrame("Voronoi Settlers, player "+name);
		frame.setLayout(new BorderLayout());
		frame.setSize(width, height);
		frame.add(infoPanel, "North");
		frame.add(boardView, "Center");
		buildPanel = new ControlPanel(this);
		frame.add(buildPanel, "South");
		setPlayerView();
		frame.setVisible(true);
	}


	void setPlayerView(){
		playerPanelLeft = new JPanel();
		frame.add(playerPanelLeft,"West");
		playerPanelLeft.setLayout(new GridLayout(2,1));

		playerPanelRight = new JPanel();
		frame.add(playerPanelRight,"East");
		playerPanelRight.setLayout(new GridLayout(2,1));

		int numPlayers = client.getModel().numPlayers();
		playerPanel = new PlayerPanel[numPlayers];

		for(int i = 0; i<numPlayers; ++i){
			Player p = client.getModel().getPlayer(i);
			playerPanel[i] = new PlayerPanel(p,p.equals(client.getPlayer()));
			if(i<2)
				playerPanelLeft.add(playerPanel[i]);
			else 
				playerPanelRight.add(playerPanel[i]);
		}
	}

	/**
	 * If mouse has been pressed inside the delaunayPanel then add a new site.
	 */
	public void mousePressed(MouseEvent e) {
		Pnt pnt = convertToCatanCoord(new Pnt(e.getX(), e.getY()));
			if(!buildPanel.isButton(e.getSource()))
				client.mouseClicked(pnt);
			else
				handleButton(e.getSource());
			boardView.repaint();
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
		client.keyPressed(arg0);
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
		frame.setFocusable(true);
		frame.requestFocusInWindow();
	}

	@Override
	public void setInactive(int player) {
		playerPanel[player].endTurn();
//		uiViewControlor.closePlayerWindows();
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

	private void handleButton(Object button){
		DB.msg("button pressed");
		if(buildPanel.isNextTurnButton(button)){
			client.nextTurnPressed();		
			return;
		}
		if(buildPanel.isTradeButton(button)){
			client.tradePressed();
			return;
		}
		if(buildPanel.isBuyCardButton(button)){
			try {
				client.buyCard();
			} catch (Exception e) {
				appendMessage("Not enough ressources to buy a card.");
			}		
			return;
		}
		if(buildPanel.isPlayCardButton(button)){
			DB.msg("play card");
			client.playCard();
			return;
		}
	}






}
