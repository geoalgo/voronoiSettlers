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
package view;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;


import delaunay.Pnt;
import delaunay.Vect;

public class AngleSortPoints {
	Pnt center;
	
	static List<Pnt> angleSortPoints(List<Pnt> pointsToSort){
		Pnt center = Pnt.barycenter(pointsToSort);

		LinkedList<Vect> vecToSort = new LinkedList<Vect>();

		for(Pnt p : pointsToSort)
			vecToSort.add(new Vect(center,p));
		
		Collections.sort(vecToSort);
		
		List<Pnt> res =  new LinkedList<Pnt>();
		
		for(Vect v : vecToSort){
			res.add(v.vect.add(center));
		}
		return res;
	}
	
	
	public static void main(String[] args){
		List<Pnt> pointsToSort = new LinkedList<>();

		pointsToSort.add(new Pnt(378.0,481.0));
		pointsToSort.add(new Pnt(413.0,513.0));
		pointsToSort.add(new Pnt(475.0,483.0));
		pointsToSort.add(new Pnt(475.0,430.0));
		pointsToSort.add(new Pnt(422.0,396.0));
		pointsToSort.add(new Pnt(373.0,429.0));

		Pnt center = Pnt.barycenter(pointsToSort);
		System.out.println("center:"+center);
		
		
		List<Pnt> res = AngleSortPoints.angleSortPoints(pointsToSort);
		System.out.println(res);
	}
	
}
