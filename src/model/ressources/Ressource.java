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

import java.awt.Color;
import java.util.Collection;
import java.util.Vector;

/**
 * @author David Salinas
 *
 */
//todo remove ressources cstor everywhere
public class Ressource implements java.lang.Comparable<Ressource>{
	String description;
	Color color;
	
	protected Ressource(String description) {
		this.description = description;
	}

	public static Collection<Ressource> allRessources(){
		Vector<Ressource> res = new Vector<Ressource>();
		res.add(new Crop());
		res.add(new Wood());
		res.add(new Sheep());
		res.add(new Stone());
		res.add(new Brick());
		return res;
	}

	@Override
	public int compareTo(Ressource other){
		return this.description.compareTo(other.description);
	}

	public Color getColor(){
		return color;
	}
	
	public boolean isDesert(){
		return this.compareTo(new Desert()) == 0;
	}
	
	public boolean isCrop(){
		return this.compareTo(new Crop()) == 0;
	}
	
	public boolean isWood(){
		return this.compareTo(new Wood()) == 0;
	}
	
	public boolean isSheep(){
		return this.compareTo(new Sheep()) == 0;
	}
	
	public boolean isStone(){
		return this.compareTo(new Stone()) == 0;
	}
	
	public boolean isBrick(){
		return this.compareTo(new Brick()) == 0;
	}
	
	public String toString(){
		return description;
	}
}
