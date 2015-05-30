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
package test;

import static org.junit.Assert.*;

import java.util.Iterator;
import java.util.List;

import model.InitialRules;
import model.Model;
import model.Construction.Building;
import model.Construction.City;
import model.Construction.Colony;
import model.Construction.Road;
import model.Construction.VertexBuilding;
import model.hexagonalTiling.SettlersEdge;
import model.hexagonalTiling.SettlersTile;
import model.hexagonalTiling.SettlersVertex;
import model.ressources.Ressources;

import org.junit.Test;

import player.Player;
import controlor.DB;
import controlor.GameController;
import delaunay.Pnt;
import delaunayBrute.TriangleHandle;

public class TestModel {

	Model makeModel(){
		return new Model(new InitialRules(2, 10,1));
	}


	@Test(timeout=200)
	public void testInitialization() {
		Model model = makeModel();
		if(model.board().numEdges() != 72)
			fail("testInitialization wrong number of edges");
		if(model.board().numTiles() != 19)
			fail("testInitialization wrong number of tiles");


		Iterator<SettlersTile> tileIt = model.board().tiles();
		while(tileIt.hasNext()){
			SettlersTile tile = tileIt.next();
			if( (tile.diceNumber() <2 || tile.diceNumber() >12) && tile.diceNumber()!=-1 )
				fail("Tile with wrong number");
			tile.ressource();
		}
	}


	@Test(timeout=2000)
	public void testBoard() {
		Model model = makeModel();
		Iterator<SettlersVertex> vIt = model.board().vertices();
		SettlersVertex vertex = vIt.next();
		List<SettlersVertex> vertices =model.board().vertexNeighbors(vertex);
		for(SettlersVertex v : vertices){
			if(v.hasBuilding()) fail("start with a building");
		}
	}
	
	@Test(timeout=2000)
	public void testBoardAroundVertex() {
		Model model = makeModel();
		Iterator<SettlersTile> tIt = model.board().tiles();
		SettlersTile tile = tIt.next();
		List<SettlersVertex> vertices = model.board().vertexNeighbors(tile);
		for(SettlersVertex v : vertices){
			DB.msg("p:"+v.getPosition());
		}
	}
	
	@Test(timeout=2000)
	public void testArmy() {
		Model model = makeModel();
		
		Player p0 = model.getPlayer(0);
		Player p1 = model.getPlayer(1);

		for(int i = 0;i<2;++i){
			p0.addKnight();
			if(model.updateBiggestArmy()) 
				fail("No player should have an army");
		}
		p0.addKnight();
		if(!model.updateBiggestArmy()) 
			fail("Player should have an army");
		if(p0.getScore()!=2 || p1.getScore()!=0)
			fail("Wrong score");
		
		for(int i = 0;i<3;++i){
			p1.addKnight();
			if(model.updateBiggestArmy()) 
				fail("No player should have an army");
		}
		
		p1.addKnight();
		if(!model.updateBiggestArmy()) 
			fail("Player should have an army");
		if(p0.getScore()!=0 || p1.getScore()!=2)
			fail("Wrong score");
	}
	
	
	
	@Test(timeout=2000)
	public void testBoardBorder() {
		Model model = makeModel();
		int numBorderEdges = 0;
		Iterator<SettlersEdge> eIt = model.board().edges();
		while(eIt.hasNext()){
			if(model.board().isBorder(eIt.next()))
				numBorderEdges++;
		}
		if(numBorderEdges!=30)
			fail("Wrong number of border edges, got:"+numBorderEdges+" expected 30");
		
		numBorderEdges = 0;
		eIt = model.board().borderEdges();
		while(eIt.hasNext()){
			numBorderEdges++;
			eIt.next();
		}
		if(numBorderEdges!=30)
			fail("Wrong number of border edges, got:"+numBorderEdges+" expected 30");
		
	}
	
	@Test(timeout=1000)
	public void testAddBuilding1() {
		Model model = makeModel();
		Iterator<SettlersVertex> vIt = model.board().vertices();
		SettlersVertex vertex = vIt.next();

		Player p1 = model.nextPlayer();

		boolean add1 = addCity(model,vertex,p1);
		if(add1) fail("Allowed to build an invalid city!");

		p1.getRessource().addStone(4);
		p1.getRessource().addCrop(4);

		boolean add2 = addCity(model,vertex,p1 );
		if(add2) fail("Allowed to build a city whereas no colony before!");

		boolean add3 = addCity(model,vertex,p1 );
		if(add3) 
			fail("Allowed to build a colony whereas not enough ressource!");

		p1.getRessource().addWood(2);
		p1.getRessource().addSheep(2);
		p1.getRessource().addBrick(2);

		boolean add4 = addFirstColony(model,vertex,p1 );
		if(!add4) fail("not allowed to build a valid colony");

		boolean add5 = addFirstColony(model,vertex,p1 );
		if(add5) fail("allowed to build a colony whereas one already settled");

		boolean add6 = addCity(model,vertex,p1 );
		if(!add6) fail("not allowed to build a valid city!");
	}

	private boolean addColony(Model model,SettlersVertex v,Player p){
		try {
			model.addColony(new Colony(v, p),v);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	private boolean addFirstColony(Model model,SettlersVertex v,Player p){
		try {
			model.addFreeColony(new Colony(v, p),v);
			return true;
		} catch (Exception e) {
			return false;
		}
	}


	private boolean addCity(Model model,SettlersVertex v,Player p){
		try {
			model.addCity(new City(v, p),v);
			return true;
		} catch (Exception e) {
			return false;
		}
	}


	// test for proximity rules
	@Test(timeout=1000)
	public void testAddBuilding2() {
		Model model = makeModel();
		Iterator<SettlersVertex> vIt = model.board().vertices();
		SettlersVertex vertex = vIt.next();

		Player p1 = model.nextPlayer();

		p1.getRessource().addStone(4);
		p1.getRessource().addCrop(4);
		p1.getRessource().addWood(4);
		p1.getRessource().addSheep(4);
		p1.getRessource().addBrick(4);

		boolean add1 = addFirstColony(model,vertex,p1 );
		if(!add1) fail("not allowed to build a valid colony!");

		List<SettlersVertex> nIt = model.board().vertexNeighbors(vertex);
		SettlersVertex vertexNeighbor = nIt.get(0);

		boolean add2 = addColony(model,vertexNeighbor,p1);
		if(add2) fail("allowed to build an invalid city!");
	}


	// test for proximity rules
	@Test(timeout=1000)
	public void testAddBuildingRoad() {
		Model model = makeModel();
		Iterator<SettlersEdge> eIt = model.board().edges();
		SettlersEdge edge = eIt.next();

		Player p1 = model.nextPlayer();
		Road road = new Road(p1,false);

		boolean add1 = addRoad(model, p1, edge);
		if(add1) fail("allowed to build an invalid road (not enough ressource!");

		p1.getRessource().addStone(8);
		p1.getRessource().addCrop(8);
		p1.getRessource().addWood(8);
		p1.getRessource().addSheep(8);
		p1.getRessource().addBrick(8);

		add1 = addRoad(model, p1, edge);
		if(add1) fail("allowed to build an invalid road (no neighbor)!");

		SettlersVertex v1 = model.board().first(edge);
		add1 = addFirstColony(model,v1,p1 );
		if(!add1) fail("not allowed to build a valid colony!");

		add1 = addRoad(model, p1, edge);
		if(!add1) fail("not allowed to build a valid road!");

		SettlersVertex v2 = model.board().second(edge);
		List<SettlersEdge> nv2 = model.board().edgeNeighbors(v2);

		for( SettlersEdge e : nv2){
			if(!e.equals(edge)){
				add1 = addRoad(model, p1, e);
				if(!add1) fail("not allowed to build an valid road!");				
			}
		}
	}

	private boolean addRoad(Model model,Player p1, SettlersEdge edge){
		boolean add1 = true;
		try {
			model.addRoad(p1,edge);
		} catch (Exception e) {
			add1 = false;
		}
		return add1;
	}	

	@Test(timeout=1000)
	public void testHarvest() {
		Model model = makeModel();
		Iterator<SettlersVertex> vIt = model.board().vertices();
		SettlersVertex vertex = vIt.next();
		vIt.next();
		SettlersVertex vertex2 = vIt.next();

		Player p1 = model.nextPlayer();

		p1.getRessource().addStone(10);
		p1.getRessource().addCrop(10);
		p1.getRessource().addWood(10);
		p1.getRessource().addSheep(10);
		p1.getRessource().addBrick(10);

		boolean add = addFirstColony(model,vertex,p1);
		if(!add) fail("not allowed valid building");
		add = addFirstColony(model,vertex2,p1);
		if(!add) fail("not allowed valid building");
		add = addCity(model,vertex2,p1 );
		if(!add) fail("not allowed valid building");
		if(!vertex2.getBuilding().isCity())
			fail("vertex should have a city");


		Ressources old_ressources = new Ressources(p1.getRessource());
		Ressources new_expected_ressources = new Ressources(p1.getRessource());
		System.out.println("Adj tile to v1:");
		for(SettlersTile t : model.board().tilesNeighbors(vertex)){
			System.out.println("t:"+t);
			new_expected_ressources.add(t.ressource(),1);
		}
		System.out.println("Adj tile to v2:");
		for(SettlersTile t : model.board().tilesNeighbors(vertex2)){
			System.out.println("t:"+t);
			new_expected_ressources.add(t.ressource(),2);
		}

		for(int i = 2 ; i <= 12; ++i)
			model.harvest(i);

		if(!p1.getRessource().equals(new_expected_ressources)){
			System.err.println("ressources of p1 before harvest:"+
					old_ressources);
			System.err.println("actual ressources of p1:"+
					p1.getRessource());
			System.err.println("expected ressources of p1:"+
					new_expected_ressources);
			fail("unexpected number of ressources after harvest");

		}
	}



	@Test(timeout=1000)
	public void testHarvest2() {
		Model model = makeModel();
		Iterator<SettlersVertex> vIt = model.board().vertices();
		SettlersVertex vertex = vIt.next();

		Player p1 = model.nextPlayer();

		p1.getRessource().addStone(10);
		p1.getRessource().addCrop(10);
		p1.getRessource().addWood(10);
		p1.getRessource().addSheep(10);
		p1.getRessource().addBrick(10);

		boolean add = addFirstColony(model,vertex,p1);
		if(!add) fail("not allowed valid building");

		Ressources old_ressources = new Ressources(p1.getRessource());
		Ressources new_expected_ressources = new Ressources(p1.getRessource());
		System.out.println("Adj tile to v1:");
		for(SettlersTile t : model.board().tilesNeighbors(vertex)){
			System.out.println("t:"+t);
			new_expected_ressources.add(t.ressource(),1);
		}

		model.harvest((VertexBuilding)vertex.getBuilding());

		if(!p1.getRessource().equals(new_expected_ressources)){
			System.err.println("ressources of p1 before harvest:"+
					old_ressources);
			System.err.println("actual ressources of p1:"+
					p1.getRessource());
			System.err.println("expected ressources of p1:"+
					new_expected_ressources);
			fail("unexpected number of ressources after harvest");
		}
	}

}
