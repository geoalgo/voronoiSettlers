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


package delaunayBrute;


public class EdgeHandle implements Comparable<EdgeHandle>,java.io.Serializable{

	private int a;
	private int b;
	
	public EdgeHandle(int a,int b) {
		this.a = a;
		this.b = b;
		if(a>b) {
			this.a = b;
			this.b = a;
		}
	}
	
	public int first(){
		return a;
	}
	
	public int second(){
		return b;
	}
	
	public boolean contains(int v){
		return first()==v || second()==v;
	}
	
	public String toString(){
		String res = "";
		res+="("+a+","+b+")";
		return res;
	}
	
	@Override
	public boolean equals(Object other){
		EdgeHandle otherEdge = (EdgeHandle)other;
		return this.a == otherEdge.a && this.b == otherEdge.b;
	}

	@Override
	public int compareTo(EdgeHandle o) {
		if(this.a!=o.a)
			return this.a - o.a;
		else
			return this.b - o.b;
	}
	
	
	
}
