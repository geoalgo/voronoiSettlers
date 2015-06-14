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
import java.util.Iterator;
import java.util.Vector;

import player.Player;

public class Players implements java.io.Serializable  {
	Vector<Player> players;
	int currentPlayerIndex;
	
	public Players(){
		players = new Vector<Player>();
		currentPlayerIndex = 0;
	}
	
	/** 
	 * @return next player in a cyclic way
	 */
	public Player nextPlayer(){
		return players.get((++currentPlayerIndex)%players.size()); 
	}
	
	public Player getPlayer(int i){
		return players.get(i);
	}

	public Iterator<Player> iterator(){
		return players.iterator();
	}
	
	
	public void addPlayer(Player j){
		players.addElement(j);
	}
	
	public void removePlayer(Player j){
		players.remove(j);
	}
	
	public int getMaxScore(){
		int maxScore = 0;
		for (Player j:this.players){
			maxScore = Math.max(j.getScore(),maxScore);
		}
		return maxScore;
	}

	public void setAllAlive(){
		for (Player j:this.players){
			j.setAlive(true);
		}
	}
	
	/**
	 * returns the number of players 
	 */
	public int getNumPlayers(){
		return players.size();
	}

	
	/**
	 * returns the number of players that are alive
	 */
	public int getNumAlive(){
		int res=0;
		for (Player j:this.players)
			if (j.isAlive()) res++;
		return res;
	}
	
	public boolean isOneAlive(){
		for (Player j:this.players){
			if (j.isAlive()) return true;
		}
		return false;
	}
	
	public String toString(){
		String res = "";
		for (Player j:this.players){
			res += j+",";
		}
		return res;
	}
	
}
