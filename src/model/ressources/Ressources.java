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
package model.ressources;

import java.util.TreeMap;
import java.util.Map;
import java.util.Vector;



public class Ressources {

	Map<Ressource,Integer> ressourceNumber;

	public Ressources() {
		ressourceNumber = new TreeMap<Ressource,Integer>();
		for(Ressource r : Ressource.allRessources())
			ressourceNumber.put(r,0);
	}
	
	public Ressources(Ressources other) {
		ressourceNumber = new TreeMap<Ressource,Integer>(other.ressourceNumber);
	}

	/**
	 * add nb ressources of ressource
	 */
	public void add(Ressource ressource,int nb){
		assert(num(ressource)+nb>=0);
		ressourceNumber.put(ressource,num(ressource)+nb);
	}
	
	public void addWood(int nbToAdd){
		add(new Wood(),nbToAdd);
	}

	public void addSheep(int nbToAdd){
		add(new Sheep(),nbToAdd);
	}

	public void addStone(int nbToAdd){
		add(new Stone(),nbToAdd);
	}

	public void addCrop(int nbToAdd){
		add(new Crop(),nbToAdd);
	}

	public void addBrick(int nbToAdd){
		add(new Brick(),nbToAdd);
	}
	
	public int getNum(Ressource ress){
		return ressourceNumber.get(ress);
	}

	/**
	 * @param other is removed from the current ressources
	 */
	public void remove(Ressources other){
		assert(greaterThan(other));
		addWood(-other.numWood());
		addSheep(-other.numSheep());
		addStone(-other.numStone());
		addCrop(-other.numCrop());
		addBrick(-other.numBrick());
	}

	
	public Ressource getRandomRessource(){
		int randomRessource = (int)(Math.random()*num());
		return getIthRessource(randomRessource);
	}
	
	private Ressource getIthRessource(int i){
		int current = 0;
		if( i <numWood()) return new Wood();
		current+=numWood();
		
		if(current<=i&&i <current+numSheep()) return new Sheep();
		current+= numSheep();
	
		if(current<=i&&i <current+numStone()) return new Stone();
		current+= numStone();
		
		if(current<=i&&i <current+numCrop()) return new Crop();
		current+= numCrop();
		
//		if(current<=i&&i <current+numBrick()) 
			return new Sheep();
	}
	
	/**
	 * @param ressource
	 * @return the current number of ressource
	 */
	public int num(Ressource ressource){
		return 	ressourceNumber.get(ressource);
	}
	
	public int numWood(){return num(new Wood());}
	public int numSheep(){return num(new Sheep());}
	public int numStone(){return num(new Stone());}
	public int numCrop(){return num(new Crop());}
	public int numBrick(){return num(new Brick());}
	public int num(){
		return numWood() + numSheep() + 
				numStone() + numCrop() + numBrick();
	}

	public boolean greaterThan(Ressources other){
		return this.numWood()>= other.numWood() && 
				this.numSheep()>= other.numSheep()&&
				this.numStone()>= other.numStone() &&
				this.numCrop()>= other.numCrop() &&
				this.numBrick()>= other.numBrick();
	}

	@Override
	public boolean equals(Object other){
		Ressources o = (Ressources) other;
		return numWood() == o.numWood()&&
				numSheep() == o.numSheep()&&
				numStone() == o.numStone()&&
				numCrop() == o.numCrop()&&
				numBrick() == o.numBrick();
				
		
	}

	public String toString(){
		String res = "";
		res+="Num wood/sheep/stone/crop/brick : "
				+num(new Wood())
				+"/"+num(new Sheep())
				+"/"+num(new Stone())
				+"/"+num(new Crop())
				+"/"+num(new Brick());
		return res;
	}
	
	public String toNiceString(){
		String res = "";
		boolean first = true;
		for(Ressource ress : Ressource.allRessources()){
			if(first) first = false;
			else res+=", ";
			res+=num(ress)+" "+ress.toString();
		}
		return res;
	}


	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("Test ressources");
		Ressources ressources = new Ressources();

		ressources.add(new Wood(),2);
		ressources.add(new Wood(),-1);
		ressources.add(new Sheep(),2);
		ressources.add(new Stone(),3);
		ressources.add(new Stone(),6);
		ressources.add(new Brick(),2);
		System.out.println("Ressources:"+ressources);

		Ressources ressources2 = new Ressources(ressources);
		ressources2.add(new Brick(),2);
		System.out.println("Ressources2:"+ressources2);
		
		ressources2.remove(ressources);
		System.out.println("Ressources2:"+ressources2);
		
	}

}
