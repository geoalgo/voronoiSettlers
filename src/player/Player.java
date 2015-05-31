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
package player;

import java.awt.Color;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import javax.swing.text.Position;

import model.Construction.Building;
import model.Construction.VertexBuilding;
import model.card.Card;
import model.hexagonalTiling.SettlersEdge;
import model.hexagonalTiling.SettlersVertex;
import model.hexagonalTiling.harbor.Harbor;
import model.ressources.Ressources;
import model.Construction.Road;

public class Player implements Comparable<Player>  {
	Color color;
	private String name;
	private int num;
	private int score;
	boolean alive; // to allow leavers like CrokNain
	int numKnight;
	Ressources ressources;
	int numRoads;
	int numCity;
	int numColony;
	List<VertexBuilding> buildings;
	Vector<Harbor> harbors;
	Vector<Card> cards;
	
//OR map<SettlersVertex,building>
	
	public Player(Color couleur,String name,int num){
		score = 0;
		this.color = couleur;
		this.name = name;
		this.num = num;
		alive = true;
		ressources = new Ressources();
		buildings = new LinkedList<VertexBuilding>();
		harbors = new Vector<Harbor>();
		cards = new Vector<Card>();
		numRoads=15;
		numCity=4;
		numColony=5;
		numKnight = 0;
	}
	
	public int getNum(){
		return num;
	}
	
	public int getRoads(){
		return numRoads;
	}
	
	public void decrementRoad(){
		numRoads--;
	}
	
	public int getColony(){
		return numColony;
	}
	
	public void decrementColony(){
		numColony--;
	}
	
	public void incrementColony(){
		numColony++;
	}
	
	public int getCity(){
		return numCity;
	}
	
	public void decrementCity(){
		numCity++;
	}
	
	public Ressources getRessource(){
		return ressources;
	}
	
	public int numRessources(){
		return ressources.num();
	}
	
	public VertexBuilding getBuilding(int i){
		return buildings.get(i);
	}
	
	public void addBuilding(Building building){
		addFreeBuilding(building);
		ressources.remove(building.cost);
	}
	
	public void addFreeBuilding(Building building){
		if(!building.isRoad())
			buildings.add((VertexBuilding)building); 
		//xxx problem a colony will not be removed!
		if(building.isCity() || building.isColony()) 
			score+=1;
	}

	public void addHarbor(Harbor h){
		harbors.add(h);
	}
	
	public Collection<Harbor> getHarbors(){
		return harbors;
	}

	public boolean hasThreeToOneHarbor(){
		for(Harbor h : getHarbors())
			if(!h.hasRessource()) return true;
		return false;
	}
	
	public void removeBuilding(Building building){
		buildings.remove(building);
	}

	
	public int getScore() {
		return score;
	}
	
	public void addPoint() {
		score +=1;
	}
	
	public void addPoint(int num) {
		score += num;
	}

	public void removePoint(int num) {
		score -= num;
	}
	
	public void removePoint() {
		score -= 1;
	}

	
	public void addKnight(){
		numKnight++;
	}
	
	public int numKnight(){
		return numKnight;
	}
	
	public void addCard(Card c){
		cards.add(c);
	}
	
	public int numCards(){
		return cards.size();
	}
	
	public Card getCard(int i){
		return cards.get(i);
	}
	
	public boolean removeCard(Card c){
		return cards.remove(c);
	}

	
	public boolean isAlive() {
		return alive;
	}

	public void setAlive(boolean estVivant) {
		this.alive = estVivant;
	}

	public String getName(){
		return name;
	}


	public Color getCouleur() {
		return color;
	}

	@Override
	public String toString(){
		return name;
	}

	@Override
	public boolean equals(Object other){
		Player p = (Player)other;
		return this.name.equals(p.name); 
	}

	public static void main(String[] args) {
		System.out.println("Test player");
		
		Player j1 = new Player(Color.black,"Darkvador",0);
		Player j2 = new Player(Color.white,"Luke",1);
		
		System.out.println("j1:"+j1);
		
		System.out.println("j1.equals(j2):"+(j1.equals(j2)));
	}

	@Override
	public int compareTo(Player o) {
		return num - o.num;
	}

}



