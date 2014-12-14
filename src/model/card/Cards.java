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

package model.card;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import model.InitialRules;

import player.Player;

public class Cards {

	private ArrayList<Card> cards;
	private int currentCard = 0;
	
	public Cards(InitialRules rules){
		cards = new ArrayList<>();
		initCards(rules);
	}
	
	private void initCards(InitialRules rules){
		int numKnights = 10;
		int numPoints = 10;
		int numFreeRoads = 3;
		int numMonopole = 3;

		for(int i = 0; i < numKnights; ++i)
			cards.add(new Knight());
		for(int i = 0; i < numMonopole; ++i)
			cards.add(new Monopole());
		for(int i = 0; i < numPoints; ++i)
			cards.add(new VictoryPoint());
		for(int i = 0; i < numFreeRoads; ++i)
			cards.add(new FreeRoad());
		randomShuffle();
	}
	
	private void randomShuffle(){
		//todo shuffle
	}

	//throws if no cards
	public Card getRandomCard(Player p) throws Exception{
		Card res = cards.get(0);
		cards.remove(0);
		res.setPlayer(p);
		return res;
	}
	
	public void releaseCard(Card c){
		cards.add(cards.size(), c);		
	}
}

