package test;

import static org.junit.Assert.*;
import model.InitialRules;
import model.card.*;
import model.hexagonalTiling.SettlersTile;
import model.ressources.Ressource;
import model.ressources.Ressources;
import model.ressources.Wood;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import player.Player;
import client.DummyClient;
import client.IClient;
import client.SendToServer;
import client.action.*;
import client.state.ClientStateSelection;
import controlor.DB;
import controlor.GameController;
import controlor.IGameController;
import delaunay.Pnt;
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
			clients[i] = new DummyClient(new SendToServer(server),gc.getPlayer(i));
		}
		server.init(clients);
	}

	@Test
	public void testServerPlayTurn() {
		server.setState(new ServerStatePlayTurn(gc, clients));
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
			DB.msg("send next turn from "+currentPlayer);
			DB.msg("gc current "+gc.currentPlayerNum());
			server.receiveAction(new ClientNextTurn(clients[currentPlayer]));
			DB.msg("now gc is "+gc.currentPlayerNum());
			
			if(server.getCurrentState().getCurrentPlayer().getNum()!=(currentPlayer+1)%numPlayers)
				if(!(server.getCurrentState() instanceof ServerStateSelectBrigandPosition))
				fail("Either active players should have changed after next turn or should be a brigand selection (after a 7)"
						+",server current player:"+server.getCurrentState().getCurrentPlayer().getNum()
						+",should be "+(currentPlayer+1)%numPlayers
						);	
		}
	}

	@Test
	public void testDrawSeven() {
		//add 10 ressources to players 1 and 2
		// first player has 0 ressources
		gc.nextPlayer();
		gc.addRessourcesToCurrentPlayer();
		gc.addRessourcesToCurrentPlayer();

		gc.nextPlayer();
		gc.addRessourcesToCurrentPlayer();
		gc.addRessourcesToCurrentPlayer();

		gc.previousPlayer();
		gc.previousPlayer();

		server.setState(ServerStateDrawDices.drawDices(gc, clients, 7));
		
		if(!(server.getCurrentState() instanceof ServerStateSelectRessources))
			fail("Server should be in select ressources (one player has 10 ressources");

		int curPlayer = gc.getCurrentPlayer().getNum();
		int numPlayer = gc.getNumPlayer();
		ServerStateSelectRessources s = (ServerStateSelectRessources) server.getCurrentState();
		if(s.getCurrentPlayerLoosingRessources()!= 1)
			fail("Wrong player that have to select loosing ressources, current is "+s.getCurrentPlayerLoosingRessources()
					+", expected:"+1);
		
		Ressources r=new Ressources();
		for(Ressource ress : Ressource.allRessources())
			r.add(ress, 1);
		server.receiveAction(new ClientSelection(clients[1],r));

		//player 2 should now be the one selecting his ressources to dispose
		if(!(server.getCurrentState() instanceof ServerStateSelectRessources))
			fail("Server should be in select ressources (one player has 10 ressources");

		s = (ServerStateSelectRessources) server.getCurrentState();
		if(s.getCurrentPlayerLoosingRessources()!= 2)
			fail("Wrong player that have to select loosing ressources, current is "+s.getCurrentPlayerLoosingRessources()
					+", expected:"+2);
		
		server.receiveAction(new ClientSelection(clients[2],r));

		if(!(server.getCurrentState() instanceof ServerStateSelectBrigandPosition))
			fail("After ressource disposal, state should be PlayTurn");

	}

	@Test
	public void testServerPlayTurnBuyCard() {
		server.setState(new ServerStatePlayTurn(gc, clients));
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
		if(gc.getCurrentPlayer().numCards()!=1){
			fail("Card should have been bought player has "+gc.getCurrentPlayer().numCards()+" cards instead of 1.");
		}

		server.receiveAction(new ClientBuyCard(clients[0]));
		if(server.getCurrentState() != initialState)
			fail("Stated should not have changed");
		if(gc.getCurrentPlayer().numCards()!=1)
			fail("Card should not have been bought (no ressources)");
	}

	@Test
	public void testServerPlayCard() {
		server.setState(new ServerStatePlayTurn(gc, clients));
		gc.addRessourcesToCurrentPlayer();
		server.receiveAction(new ClientBuyCard(clients[0]));
		if(gc.getCurrentPlayer().numCards()!=1)
			fail("Card should have been bought");
		server.receiveAction(new ClientPlayCard(clients[0]));

		if(gc.getCurrentPlayer().numCards()!=1)
			fail("Card should have been bought");

		if(!(server.getCurrentState() instanceof ServerStateSelectCard))
			fail("Server should be in select card state");
	}

	@Test
	public void testServerVictoryPointCard() {
		server.setState(new ServerStatePlayTurn(gc, clients));
		gc.addCardsToCurrentPlayer();
		server.receiveAction(new ClientPlayCard(clients[0]));
		server.receiveAction(new ClientSelection(clients[0],new VictoryPoint()));
		if(!(server.getCurrentState() instanceof ServerStatePlayTurn))
			fail("After having played a Victory point, state should be ServerStatePlayTurn");
		if(gc.getCurrentPlayer().getScore()!=1)
			fail("Failed to add one point");
	}

	@Test
	public void testServerMonopole() {
		server.setState(new ServerStatePlayTurn(gc, clients));
		gc.addCardsToCurrentPlayer();
		gc.getPlayer(1).getRessource().addWood(2);

		server.receiveAction(new ClientPlayCard(clients[0]));
		server.receiveAction(new ClientSelection(clients[0],new Monopole()));
		if(!(server.getCurrentState() instanceof ServerStateSelectRessourceMonopole))
			fail("After having played a monopole, state should be RessourceMonopole");


		server.receiveAction(new ClientSelection(clients[0],new Wood()));
		if(gc.getPlayer(1).getRessource().numWood()!=0)
			fail("Failed to take ressource player with monopole");

		if(gc.getCurrentPlayer().getRessource().numWood()!=2)
			fail("Failed to take ressource player with monopole");
	}


	@Test
	public void testServerKnightCard() {
		server.setState(new ServerStatePlayTurn(gc, clients));
		gc.addCardsToCurrentPlayer();
		server.receiveAction(new ClientPlayCard(clients[0]));
		server.receiveAction(new ClientSelection(clients[0],new Knight()));
		if(!(server.getCurrentState() instanceof ServerStateSelectBrigandPosition))
			fail("After having played a Knight, state should be ServerStateSelectBrigandPosition");

		Pnt p1 = new Pnt(0.68,0.48);
		Pnt p2 = new Pnt(0.32,0.48);
		//to be sure that stealer wont be in the position p1, we move it to p2
		gc.setThiefPosition(gc.locateClosestTile(p2));

		//add to the brigand place
		server.receiveAction(new ClientActionClick(clients[0],new Pnt(p2)));
		if(!(server.getCurrentState() instanceof ServerStateSelectBrigandPosition))
			fail("Knight moved in a already occupied position, state should be ServerStateSelectBrigandPosition");

		//now add to a empty place
		server.receiveAction(new ClientActionClick(clients[0],new Pnt(p1)));
		//should not be any player here
		if(!(server.getCurrentState() instanceof ServerStatePlayTurn))
			fail("After having played a Knight onto an empty position, state should be ServerStateSelectBrigandPosition");

		//now add to a non empty place (other players there)
		gc.addCardsToCurrentPlayer();
		server.receiveAction(new ClientPlayCard(clients[0]));
		server.receiveAction(new ClientSelection(clients[0],new Knight()));

		gc.addColonyAroundTile(gc.locateClosestTile(p2));
		server.receiveAction(new ClientActionClick(clients[0],new Pnt(p2)));
		if(!(server.getCurrentState() instanceof ServerStateSelectPlayer))
			fail("After having played a knigth to a non empty position, state should be ServerStateSelectPlayer");

		server.receiveAction(new ClientSelection(clients[0],clients[1].getPlayer()));
		if(!(server.getCurrentState() instanceof ServerStatePlayTurn))
			fail("After having chosen a player to steal, state should be ServerStatePlayTurn");


	}

}
