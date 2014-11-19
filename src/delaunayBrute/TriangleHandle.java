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

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import delaunay.ArraySet;

public class TriangleHandle extends ArraySet<Integer> implements Comparable<TriangleHandle>{

	
	public TriangleHandle(int a,int b,int c){
		add(a);
		add(b);
		add(c);
		if(size()!=3)
		System.err.println("Triangle with non different elts!!");
	}
	
	public TriangleHandle(TriangleHandle other){
		for(int i = 0; i<3; ++i)
			add(other.get(i));
		if(size()!=3)
		System.err.println("Triangle with non different elts!!");
	}

	
	ArraySet<Integer> intersection(TriangleHandle other){
		ArraySet<Integer> res = new ArraySet<Integer>(other);
		res.retainAll(this);
		return res;
	}
	
	public List<EdgeHandle> edges(){
		List<EdgeHandle> res = new LinkedList<EdgeHandle>();
		res.add(new EdgeHandle(this.get(0), this.get(1)));
		res.add(new EdgeHandle(this.get(1), this.get(2)));
		res.add(new EdgeHandle(this.get(2), this.get(0)));
		return res;
	}
	
//    /**
//     * Get the item at the specified index.
//     * @param index where the item is located in the ListSet
//     * @return the item at the specified index
//     * @throws IndexOutOfBoundsException if the index is out of bounds
//     */
//    public int get (int index) throws IndexOutOfBoundsException {
//        return vertices.get(index);
//    }

//    /**
//     * True iff any member of the collection is also in the ArraySet.
//     * @param collection the Collection to check
//     * @return true iff any member of collection appears in this ArraySet
//     */
//    public boolean containsAny (Collection<?> collection) {
//        for (Object item: collection)
//            if (vertices.contains(item)) return true;
//        return false;
//    }
//
//    public Iterator<Integer> iterator() {
//        return vertices.iterator();
//    }

    public String toString(){
    	String res ="";
    	res+="("+get(0)+","+get(1)+","+get(2)+")";
    	return res;
    }

	@Override
	public int compareTo(TriangleHandle o) {
		int a = get(0);
		int b = get(1);
		int c = get(2);
		
		int ao = o.get(0);
		int bo = o.get(1);
		int co = o.get(2);
		
		if(a!=ao) 
			return a - ao;
		else
			if(b!=bo) 
				return b-bo;
			else 
				return c-co;
	}
    
}
