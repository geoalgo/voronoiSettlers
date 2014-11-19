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

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Vector;


/**
 * @author david
 *
 * @param <E>
 * given a Vector<E> it allows to pick elements in a 
 * random way with the pick method. 
 */
public class RandomPickCollection <E>{
	Vector<EltAndBoolean> elements;
    Random generator ;
	class EltAndBoolean{
		public boolean taken;
		public E elt;
		public EltAndBoolean(E elt){
			this.elt = elt;
			taken = false;
		}
	}
	
	
	RandomPickCollection(Collection<E> elts,long seed){
		generator = new Random(seed);
		elements = new Vector<EltAndBoolean>();
		for(E e : elts){
			elements.add(new EltAndBoolean(e));
		}
	}
	
	RandomPickCollection(Collection<E> elts){
		generator = new Random(System.currentTimeMillis());
		elements = new Vector<EltAndBoolean>();
		for(E e : elts){
			elements.add(new EltAndBoolean(e));
		}
	}
	
	public int size(){
		return elements.size();
	}
	
	/**
	 * @param excludedElements
	 * @return an element that has not already been picked
	 * and is not included in excludedElements.
	 */
	public E randPick(Collection<E> excludedElements){
		// todo do a random permutation
		int maxNumIteration = elements.size()*10;
		int randomPosition;
		EltAndBoolean randomElt;
		int numTries = 0;
		do{
			randomPosition = (int)(generator.nextDouble() * elements.size());
			randomElt = elements.get(randomPosition);
			++numTries;
		} while((randomElt.taken || excludedElements.contains(randomElt.elt)) && numTries <= maxNumIteration);
		if(numTries>maxNumIteration) return null;
		else {
			elements.get(randomPosition).taken = true;
			return randomElt.elt;
		}
	}
	
	/**
	 * @param excludedElements
	 * @return an element that has not already been picked
	 * and is not included in excludedElements.
	 */
	public E randPick(){
		return randPick(new LinkedList<E>());
	}

	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("pickRandom");

		Vector<Integer> v = new Vector<Integer>();
		v.add(1);
		v.add(2);
		v.add(3);
		v.add(4);

		RandomPickCollection<Integer> randPicker = new RandomPickCollection<Integer>(v);
		List<Integer> list = new LinkedList<>();
		System.out.println("pickRandom("+randPicker.randPick(list));
		System.out.println("pickRandom("+randPicker.randPick(list));
		System.out.println("pickRandom("+randPicker.randPick(list));
		System.out.println("pickRandom("+randPicker.randPick(list));
		System.out.println("pickRandom("+randPicker.randPick(list));

		
	}

}
