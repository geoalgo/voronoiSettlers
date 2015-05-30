package test;

import static org.junit.Assert.*;
import model.InitialRules;
import model.card.Card;
import model.card.VictoryPoint;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import player.Player;
import client.Client;
import client.IClient;
import client.SendToServer;
import client.action.*;
import client.state.ClientStateSelection;
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
			clients[i] = new Client(new SendToServer(server),gc.getPlayer(i));
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
			gc.resetRessources();
			server.receiveAction(new ClientNextTurn(clients[currentPlayer]));
			if(server.getCurrentState().getCurrentPlayer().getNum()!=(currentPlayer+1)%numPlayers)
				fail("Active players should have changed after next turn");	
		}
	}
	
	@Test
	public void testServerPlayTurnBuyCard() {
		ServerState initialState = server.getCurrentState();
		if(server.getCurrentState().getCurrentPlayer().getNum()!=0)
			fail("Does not start with 1st player");
		
		server.receiveAction(new ClientBuyCard(clients[0]));
		if(server.getCurrentState() != initialState)
			fail("Stated should not have changed");
		if(gc.getCurrentPlayer().numCards()!=0)
			fail("Card bought without ressources!");
		
		gc.addRessourcesToCurrentPlayer();
		
		server.receiveAction(new ClientBuyCard(clients[0]));
		if(server.getCurrentState() != initialState)
			fail("Stated should not have changed");
		if(gc.getCurrentPlayer().numCards()!=1)
			fail("Card should have been bought");
		
		server.receiveAction(new ClientBuyCard(clients[0]));
		if(server.getCurrentState() != initialState)
			fail("Stated should not have changed");
		if(gc.getCurrentPlayer().numCards()!=1)
			fail("Card should not have been bought (no ressources)");
	}
	
	@Test
	public void testServerPlayCard() {
		gc.addRessourcesToCurrentPlayer();
		server.receiveAction(new ClientBuyCard(clients[0]));
		if(gc.getCurrentPlayer().numCards()!=1)
			fail("Card should have been bought");
		server.receiveAction(new ClientPlayCard(clients[0]));
		
		if(!(server.getCurrentState() instanceof ServerStateSelectCard))
			fail("Server should be in select card state");
		
		if(!((clients[0].getCurrentState()) instanceof ClientStateSelection<?>))
			fail("Client should be in select card state");
		
		server.receiveAction(new ClientSelection(clients[0],new VictoryPoint()));

	}
}
