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


package model;

import java.awt.Color;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import model.hexagonalTiling.harbor.Harbor;
import model.hexagonalTiling.harbor.HarborThreeToOne;
import model.hexagonalTiling.harbor.HarbourRessource;
import model.ressources.Brick;
import model.ressources.Crop;
import model.ressources.Desert;
import model.ressources.Ressource;
import model.ressources.Sheep;
import model.ressources.Stone;
import model.ressources.Wood;
import player.Player;
import player.Players;


/**
 * Set the rule for playing, ie the number of tiles, number of players and so on.
 * @author David Salinas
 *
 */
public class InitialRules implements java.io.Serializable {
	int numPlayers;
	int scoreMax;
	/*
	 * Tiles setting
	 * oooo
	 * ooooo
	 * oooooo
	 * ooooooo
	 * oooooo
	 * ooooo
	 * oooo
	 */
	Integer[] tilesNumber = {4,5,6,7,6,5,4};
	
	
	/**
	 * The tokens number that are on tiles.
	 */
	RandomPickCollection<Integer> tokensStack;
	RandomPickCollection<Ressource> ressourcesStack;

	int numHarborThreeToOne = 4;
	
	
	public InitialRules(int numPlayers,int scoreMax,long seed) {
		this.numPlayers = numPlayers;
		this.scoreMax = scoreMax;
		initTokensStack(seed);
		initRessourcesStack(seed);
	}
	
	public InitialRules(int numPlayers,int scoreMax) {
		this.numPlayers = numPlayers;
		this.scoreMax = scoreMax;
		long seed = System.currentTimeMillis();
		initTokensStack(seed);
		initRessourcesStack(seed);
	}
	
	private void initTokensStack(long seed){
		Vector<Integer> initialtilesTokens = new Vector<Integer>();
		addTokens(initialtilesTokens,2,1);
		addTokens(initialtilesTokens,3,2);
		addTokens(initialtilesTokens,4,2);
		addTokens(initialtilesTokens,5,2);
		addTokens(initialtilesTokens,6,2);
		addTokens(initialtilesTokens,8,2);
		addTokens(initialtilesTokens,9,2);
		addTokens(initialtilesTokens,10,2);
		addTokens(initialtilesTokens,11,2);
		addTokens(initialtilesTokens,12,2);
		tokensStack = new RandomPickCollection<Integer>(initialtilesTokens,seed);
	}
	
	private void addTokens(Vector<Integer> tokens,int tokenToAdd,int nbTimesToAdd){
		for(int i = 0; i < nbTimesToAdd ; ++i)
			tokens.add(tokenToAdd);
	}
	
	private void initRessourcesStack(long seed){
		Vector<Ressource> initialRessources = new Vector<Ressource>();
		int numWoods = 4;
		int numSheeps = 4;
		int numStones = 3;
		int numBricks = 3;
		int numCrops = 4;
		int numDesert = 1;

		addRessources(initialRessources, new Wood(), numWoods);
		addRessources(initialRessources, new Sheep(), numSheeps);
		addRessources(initialRessources, new Stone(), numStones);
		addRessources(initialRessources, new Brick(), numBricks);
		addRessources(initialRessources, new Crop(), numCrops);
		addRessources(initialRessources, new Desert(), numDesert);
		
		ressourcesStack = new RandomPickCollection<>(initialRessources,seed);
	}

	private void addRessources(Vector<Ressource> ressources,Ressource ressourceToAdd,int numAdd){
		for(int i = 0 ; i < numAdd ; ++i)
			ressources.add(ressourceToAdd);
	}
	
	public Integer[] tilesNumber(){
		return tilesNumber;
	}
	
	/**
	 * @param excludedNumbers
	 * @return return a token that is not in excludedNumbers 
	 * and has not been already picked.
	 * excludedNumbers can be useful to exclude configuration such as two adjacent eights 
	 * or sixes.
	 * Useful for when putting tokens number on the board.
	 */
	public int pickRandomTokenNumberDifferentFrom(List<Integer> excludedNumbers){
		return tokensStack.randPick(excludedNumbers);
	}
	
	/**
	 * @return return a ressource that has not already been 
	 * picked.
	 * Useful for when putting ressources on the board.
	 */
	public Ressource pickRandomRessource(){
		return ressourcesStack.randPick(new LinkedList<Ressource>());
	}
	

	public int numPlayers(){
		return numPlayers;
	}
	
	public int scoreMax(){
		return scoreMax;
	}
	
	
	
	/**
	 * @return an iterator to a collection of random harbors.
	 * They are set on border edges and can be empty (null).
	 */
	public Iterator<Harbor> harbors(){
		Vector<Harbor> randomStack = new Vector<Harbor>();
		for(int i = 0; i < numHarborThreeToOne; ++i)
			randomStack.add(new HarborThreeToOne());
		
		for(Ressource r : Ressource.allRessources())
			randomStack.add(new HarbourRessource(r));
		
		RandomPickCollection<Harbor> harborsStack = new RandomPickCollection<>(randomStack);

		int[] spaceBetweenHarbors = new int[9];
		for(int i = 0; i<3; ++i){
			spaceBetweenHarbors[3*i] = 3;
			spaceBetweenHarbors[3*i+1] = 2;
			spaceBetweenHarbors[3*i+2] = 2;
		}
		
		Vector<Harbor> res = new Vector<>();
		for(int i = 0;i < harborsStack.size(); ++i){
			res.add( harborsStack.randPick());
			for(int j = 0; j < spaceBetweenHarbors[i]; ++j)
				res.add(null);
		}
		
		return res.iterator();
	}
	
	
	
	
	
}
