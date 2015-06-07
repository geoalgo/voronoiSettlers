package server.state;

import java.util.TreeSet;

import model.BuildException;
import model.hexagonalTiling.SettlersEdge;
import model.hexagonalTiling.SettlersTile;
import model.hexagonalTiling.SettlersVertex;
import player.*;
import delaunay.*;
import controlor.DB;
import controlor.IGameController;
import client.IClient;
import client.action.*;

public class ServerStatePlayTurn extends ServerState{
	public ServerStatePlayTurn(IGameController gc,IClient clients[],boolean drawDices) {
		super(gc,clients);
	}

	public ServerStatePlayTurn(IGameController gc,IClient clients[]) {
		super(gc,clients);
	}
	
	@Override
	public ServerState receivesClientClick(ClientActionClick action) {
		click(((ClientActionClick)action).getPoint());
		return this;
	}
	

	public void click(Pnt click) {
		DB.msg("\nclick "+this+" pos:\n"+click+"\n");
		messageToCurrentPlayer(click.toString());
		SettlersVertex closestVertex = gc.locateClosestVertex(click);
		SettlersEdge closestEdge = gc.locateClosestEdge(click);
//		SettlersTile closestTile = gc.locateClosestTile(click);
		double distVertex = closestVertex.getPosition().dist(click);
		double distEdge = click.dist(closestEdge.p1(),closestEdge.p2());

		double minDist = 0.02;
		if(distVertex< minDist){
			click(closestVertex,click);
		}
		else{
			if(distEdge < minDist)
				click(closestEdge,click);
//			else
//				if(closestTile!=null) ;
//					click(closestTile,click);
		}
		DB.msg("dist v:"+distVertex);
		DB.msg("dist e:"+distEdge);
	}

	private void click(SettlersVertex v,Pnt click){
		if(!v.hasBuilding()) {
			DB.msg("no building");
			buildColony(v,click);
		}
		else{
			DB.msg("building there");
			buildCity(v,click);
		}
	}

	private void buildColony(SettlersVertex v,Pnt click){
		try {
			gc.addColony(click, gc.getCurrentPlayer());
			messageToAllPlayers(gc.getCurrentPlayer()+" build a colony ");
			updateClientsView();
		} 
		catch (BuildException e){
			messageToCurrentPlayer(e.getMsg());
		}
		catch (Exception e) {
			messageToCurrentPlayer("Invalid colony placement");
		}
	}

	private void buildCity(SettlersVertex v,Pnt click){
		try {
			gc.addCity(click, gc.getCurrentPlayer());
			messageToAllPlayers(gc.getCurrentPlayer()+" build a city");
			updateClientsView();
		} 
		catch (BuildException e){
			messageToCurrentPlayer(e.getMsg());
		}
		catch (Exception e) {
			messageToCurrentPlayer("Invalid city placement");
		}
	}

	private void click(SettlersEdge e,Pnt click){
		try {
			gc.addRoad(e, gc.getCurrentPlayer());
			updateClientsView();
			messageToAllPlayers(getCurrentPlayer()+" build a road ");
		}
		catch (BuildException ex){
			messageToCurrentPlayer(ex.getMsg());
		}
		catch (Exception ex) {
			messageToCurrentPlayer("Invalid road placement");
		}
	}
	
	
	@Override
	public ServerState receivesClientNextTurn(ClientNextTurn c) {
		gc.nextPlayer();
		return ServerStateDrawDices.drawRandomDices(gc,clients);
	}
	
	@Override
	public ServerState receivesClientBuyCard(ClientBuyCard c){
		try {
			DB.msg("receives a buy card action");
			messageToCurrentPlayer("You bought a "+gc.buyCard());
			messageToAllButCurrentPlayer(gc.getCurrentPlayer()+" bought a card");
			updateClientsView();
			return this;
		} catch (Exception e) {
			c.getClient().message("You dont have enough ressources to buy a card!");
			return this;
		}
	}
	
	@Override
	public ServerState receivesClientPlayCard(ClientPlayCard c){
		if(gc.getCurrentPlayer().numCards()==0){
			messageToCurrentPlayer("You dont have any card");
			return this;
		}
		// set client and server states to choose card
		return new ServerStateSelectCard(gc,clients);
	}
	
	
}
