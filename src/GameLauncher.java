
import javax.swing.SwingUtilities;

import controlor.GameController;
import model.InitialRules;
import client.IClient;
import client.ISendToServer;
import client.SendToServer;
import client.gui.GUIClient;
import server.IServer;
import server.Server;


public class GameLauncher extends javax.swing.JApplet implements Runnable{

	IServer server;
	IClient[] clients;
	int numPlayers;
	
	public GameLauncher(){
		this.numPlayers = 2;
		initServer();
		initClients();
		server.init(clients);
		RegisteredGame.game1(server, clients);
		run();
	}
	
	private void initServer(){
		server = new Server(new InitialRules(numPlayers, 10));
	}
	
	private void initClients(){
		clients = new IClient[numPlayers];
		for (int i = 0; i < clients.length; i++) {
			clients[i] = new GUIClient(new SendToServer(server), server.getPlayer(i));
		}
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
	}


}

