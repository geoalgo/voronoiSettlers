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


package delaunay;

public class Vect implements Comparable<Vect>{

	public Pnt vect;
	private Pnt e1;
	
	public Vect(Pnt origin,Pnt dest){
		vect = new Pnt(dest);
		vect = vect.subtract(origin);
		e1 = new Pnt(1,0);
	}
	
	
	public double angle(){
		if(vect.norm()==0) 
			return -1;
		double dot = vect.getX() / vect.norm();
		double theta  = Math.acos(dot);
		if(vect.getY()<0)
			theta*=-1;
		return theta;
	}
	
	@Override
	public int compareTo(Vect arg0) {
		// TODO Auto-generated method stub
		if(this.angle()<arg0.angle()) 
			return -1;
		else 
			if(this.angle()==arg0.angle())
				return 0;
			else return 1;
	}
	
}
