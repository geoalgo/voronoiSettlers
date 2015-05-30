package test;

import static org.junit.Assert.*;
import model.InitialRules;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import player.Player;
import client.Client;
import client.IClient;
import client.action.*;
import controlor.DB;
import controlor.GameController;
import controlor.IGameController;
import server.IServer;
import server.Server;
import server.state.*;

public class TestServerState {
	IServer server;
	IGameController gc;
	
	
	int numPlayers = 3;
	IClient clients[];
	
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		gc = new GameController(new InitialRules(numPlayers, 10));
		server = new Server(gc);
		clients = new IClient[numPlayers];
		
		for(int i=0; i < numPlayers; ++i){
			clients[i] = new Client(gc.getPlayer(i));
		}
	}

	@Test
	public void testServerPlayTurn() {
		ServerState initialState = server.getCurrentState();
		if(!(initialState instanceof ServerStatePlayTurn))
			fail("First state should be ServerStatePlayTurn");
		
		if(initialState.getCurrentPlayer().getNum()!=0)
			fail("First state should starts with player 0");
		
		server.receiveAction(new ClientNextTurn(clients[1]));
		if(server.getCurrentState() != initialState)
			fail("Stated changed by another player");
		
		for(int currentPlayer = 0; currentPlayer < numPlayers; ++currentPlayer){
			resetRessources();
			server.receiveAction(new ClientNextTurn(clients[currentPlayer]));
			if(server.getCurrentState().getCurrentPlayer().getNum()!=(currentPlayer+1)%numPlayers)
				fail("Active players should have changed after next turn");	
		}
	}
	
	// to avoid 7 draws
	private void resetRessources(){
		
	}
}
