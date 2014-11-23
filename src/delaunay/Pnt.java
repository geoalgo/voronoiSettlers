package delaunay;

import java.util.LinkedList;
import java.util.List;

/*
 * Copyright (c) 2005, 2007 by L. Paul Chew.
 *
 * Permission is hereby granted, without written agreement and without
 * license or royalty fees, to use, copy, modify, and distribute this
 * software and its documentation for any purpose, subject to the following
 * conditions:
 *
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS
 * OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 */

/**
 * Points in Euclidean space, implemented as double[].
 *
 * Includes simple geometric operations.
 * Uses matrices; a matrix is represented as an array of Pnts.
 * Uses simplices; a simplex is represented as an array of Pnts.
 *
 * @author Paul Chew
 *
 * Created July 2005.  Derived from an earlier, messier version.
 *
 * Modified Novemeber 2007.  Minor clean up.
 */
public class Pnt  implements Comparable<Pnt> {

	private double[] coordinates;          // The point's coordinates

	/**
	 * Constructor.
	 * @param coords the coordinates
	 */
	public Pnt (double... coords) {
		// Copying is done here to ensure that Pnt's coords cannot be altered.
		// This is necessary because the double... notation actually creates a
		// constructor with double[] as its argument.
		coordinates = new double[coords.length];
		System.arraycopy(coords, 0, coordinates, 0, coords.length);
	}


	public Pnt (Pnt other) {
		// Copying is done here to ensure that Pnt's coords cannot be altered.
		// This is necessary because the double... notation actually creates a
		// constructor with double[] as its argument.
		coordinates = new double[other.dimension()];
		for(int i = 0 ; i< other.dimension(); ++i){
			coordinates[i] = other.coordinates[i];
		}
	}


	@Override
	public String toString () {
		if (coordinates.length == 0) return "Pnt()";
		String result = "Pnt(" + coordinates[0];
		for (int i = 1; i < coordinates.length; i++)
			result = result + "," + coordinates[i];
		result = result + ")";
		return result;
	}

	@Override
	public boolean equals (Object other) {
		if (!(other instanceof Pnt)) return false;
		Pnt p = (Pnt) other;
		if (this.coordinates.length != p.coordinates.length) return false;
		for (int i = 0; i < this.coordinates.length; i++)
			if (this.coordinates[i] != p.coordinates[i]) return false;
		return true;
	}

	
	public Pnt middle(Pnt other) {
		Pnt res = new Pnt(this);
		for(int i = 0 ; i< other.dimension(); ++i){
			res.coordinates[i] += other.coordinates[i];
			res.coordinates[i]/=2;
		}
		return res;
	}
	
	@Override
	public int hashCode () {
		int hash = 0;
		for (double c: this.coordinates) {
			long bits = Double.doubleToLongBits(c);
			hash = (31*hash) ^ (int)(bits ^ (bits >> 32));
		}
		return hash;
	}

	/* Pnts as vectors */

	public double getX(){
		return this.coordinates[0];
	}
	
	public double getY(){
		return this.coordinates[1];
	}
	
	/**
	 * @return the specified coordinate of this Pnt
	 * @throws ArrayIndexOutOfBoundsException for bad coordinate
	 */
	public void setCoord (int i,double val) {
		coordinates[i] = val;
	}

	/**
	 * @return this Pnt's dimension.
	 */
	public int dimension () {
		return coordinates.length;
	}

	/**
	 * Check that dimensions match.
	 * @param p the Pnt to check (against this Pnt)
	 * @return the dimension of the Pnts
	 * @throws IllegalArgumentException if dimension fail to match
	 */
	public int dimCheck (Pnt p) {
		int len = this.coordinates.length;
		if (len != p.coordinates.length)
			throw new IllegalArgumentException("Dimension mismatch");
		return len;
	}

	/**
	 * Create a new Pnt by adding additional coordinates to this Pnt.
	 * @param coords the new coordinates (added on the right end)
	 * @return a new Pnt with the additional coordinates
	 */
	public Pnt extend (double... coords) {
		double[] result = new double[coordinates.length + coords.length];
		System.arraycopy(coordinates, 0, result, 0, coordinates.length);
		System.arraycopy(coords, 0, result, coordinates.length, coords.length);
		return new Pnt(result);
	}

	/**
	 * 
	 * @param p
	 * @return distance between the two points
	 */
	public double dist(Pnt p){
		int len = dimCheck(p);
		double sum = 0;
		for (int i = 0; i < len; i++){
			double coeff = this.coordinates[i] - p.coordinates[i];
			coeff*=coeff;
			sum+=coeff;
		}
		return Math.sqrt(sum);
	}


	/**
	 * 
	 * @param p
	 * @param q
	 * @return the distance between the current point and the segment [p,q]
	 */
	public double dist(Pnt v,Pnt w){
		double l2 = v.dist(w);l2*=l2;
		if (l2 == 0.0) return this.dist(v);   // v == w case
		double t = (this.subtract(v)).dot(w.subtract(v)) / l2;
		if (t < 0.0) 
			return this.dist(v);       // Beyond the 'v' end of the segment
		else 
			if (t > 1.0) return this.dist(w);  // Beyond the 'w' end of the segment
		Pnt projection = v.add((w.subtract(v)).scale(t)) ;  // Projection falls on the segment
		return this.dist(projection);
		//    	  const float l2 = length_squared(v, w);  // i.e. |w-v|^2 -  avoid a sqrt
		//    	  if (l2 == 0.0) return distance(p, v);   // v == w case
		//    	  const float t = dot(p - v, w - v) / l2;
		//    	  if (t < 0.0) return distance(p, v);       // Beyond the 'v' end of the segment
		//    	  else if (t > 1.0) return distance(p, w);  // Beyond the 'w' end of the segment
		//    	  const vec2 projection = v + t * (w - v);  // Projection falls on the segment
		//    	  return distance(p, projection);
	}


	/**
	 * Dot product.
	 * @param p the other Pnt
	 * @return dot product of this Pnt and p
	 */
	public double dot (Pnt p) {
		int len = dimCheck(p);
		double sum = 0;
		for (int i = 0; i < len; i++)
			sum += this.coordinates[i] * p.coordinates[i];
		return sum;
	}
	
	public double norm(){
		double res = 0;
		for (int i = 0; i < coordinates.length; i++)
			res+= (this.coordinates[i])*(this.coordinates[i]);
		return Math.sqrt(res);
	}

	/**
	 * Dot product.
	 * @param p the other Pnt
	 * @return dot product of this Pnt and p
	 */
	public Pnt cross(Pnt p) {
		int len = dimCheck(p);
		if (len!=3) return new Pnt(-1,-1,-1);
		double u1 = this.coordinates[0];
		double u2 = this.coordinates[1];
		double u3 = this.coordinates[2];
		double v1 = p.coordinates[0];
		double v2 = p.coordinates[1];
		double v3 = p.coordinates[2];
		return new Pnt( u2*v3 - u3*v2   ,
				u3*v1 - u1*v3,
				u1*v2 - u2*v1
				);
	}

	
	/**
	 * @param a
	 * @param b
	 * @return true iff (this,a) (this,b) is clockwise 
	 * oriented
	 */
	public boolean clockwise(Pnt a,Pnt b){
		Pnt u = a.subtract(this);
		Pnt v = b.subtract(this);
		return u.cross(v).coordinates[2] >=0;
	}
	
	/**
	 * Magnitude (as a vector).
	 * @return the Euclidean length of this vector
	 */
	public double magnitude () {
		return Math.sqrt(this.dot(this));
	}

	/**
	 * Subtract.
	 * @param p the other Pnt
	 * @return a new Pnt = this - p
	 */
	public Pnt subtract (Pnt p) {
		int len = dimCheck(p);
		double[] coords = new double[len];
		for (int i = 0; i < len; i++)
			coords[i] = this.coordinates[i] - p.coordinates[i];
		return new Pnt(coords);
	}

	/**
	 * Add.
	 * @param p the other Pnt
	 * @return a new Pnt = this + p
	 */
	public Pnt add (Pnt p) {
		int len = dimCheck(p);
		double[] coords = new double[len];
		for (int i = 0; i < len; i++)
			coords[i] = this.coordinates[i] + p.coordinates[i];
		return new Pnt(coords);
	}

	public Pnt scale(double lambda){
		double[] coords = new double[this.dimension()];
		for (int i = 0; i < this.dimension(); i++)
			coords[i]= this.coordinates[i]*lambda;
		return new Pnt(coords);
	}

	public void scale(int index,double lambda){
		setCoord(index, coordinates[index] * lambda);
	}

	
	/**
	 * Angle (in radians) between two Pnts (treated as vectors).
	 * @param p the other Pnt
	 * @return the angle (in radians) between the two Pnts
	 */
	public double angle (Pnt p) {
		return Math.acos(this.dot(p) / (this.magnitude() * p.magnitude()));
	}

	/**
	 * Perpendicular bisector of two Pnts.
	 * Works in any dimension.  The coefficients are returned as a Pnt of one
	 * higher dimension (e.g., (A,B,C,D) for an equation of the form
	 * Ax + By + Cz + D = 0).
	 * @param point the other point
	 * @return the coefficients of the perpendicular bisector
	 */
	public Pnt bisector (Pnt point) {
		dimCheck(point);
		Pnt diff = this.subtract(point);
		Pnt sum = this.add(point);
		double dot = diff.dot(sum);
		return diff.extend(-dot / 2);
	}

	/* Pnts as matrices */

	/**
	 * Create a String for a matrix.
	 * @param matrix the matrix (an array of Pnts)
	 * @return a String represenation of the matrix
	 */
	public static String toString (Pnt[] matrix) {
		StringBuilder buf = new StringBuilder("{");
		for (Pnt row: matrix) buf.append(" " + row);
		buf.append(" }");
		return buf.toString();
	}

	/**
	 * Compute the determinant of a matrix (array of Pnts).
	 * This is not an efficient implementation, but should be adequate
	 * for low dimension.
	 * @param matrix the matrix as an array of Pnts
	 * @return the determinnant of the input matrix
	 * @throws IllegalArgumentException if dimensions are wrong
	 */
	public static double determinant (Pnt[] matrix) {
		if (matrix.length != matrix[0].dimension())
			throw new IllegalArgumentException("Matrix is not square");
		boolean[] columns = new boolean[matrix.length];
		for (int i = 0; i < matrix.length; i++) columns[i] = true;
		try {return determinant(matrix, 0, columns);}
		catch (ArrayIndexOutOfBoundsException e) {
			throw new IllegalArgumentException("Matrix is wrong shape");
		}
	}

	/**
	 * Compute the determinant of a submatrix specified by starting row
	 * and by "active" columns.
	 * @param matrix the matrix as an array of Pnts
	 * @param row the starting row
	 * @param columns a boolean array indicating the "active" columns
	 * @return the determinant of the specified submatrix
	 * @throws ArrayIndexOutOfBoundsException if dimensions are wrong
	 */
	private static double determinant(Pnt[] matrix, int row, boolean[] columns){
		if (row == matrix.length) return 1;
		double sum = 0;
		int sign = 1;
		for (int col = 0; col < columns.length; col++) {
			if (!columns[col]) continue;
			columns[col] = false;
			sum += sign * matrix[row].coordinates[col] *
					determinant(matrix, row+1, columns);
			columns[col] = true;
			sign = -sign;
		}
		return sum;
	}

	/**
	 * Compute generalized cross-product of the rows of a matrix.
	 * The result is a Pnt perpendicular (as a vector) to each row of
	 * the matrix.  This is not an efficient implementation, but should
	 * be adequate for low dimension.
	 * @param matrix the matrix of Pnts (one less row than the Pnt dimension)
	 * @return a Pnt perpendicular to each row Pnt
	 * @throws IllegalArgumentException if matrix is wrong shape
	 */
	public static Pnt cross (Pnt[] matrix) {
		int len = matrix.length + 1;
		if (len != matrix[0].dimension())
			throw new IllegalArgumentException("Dimension mismatch");
		boolean[] columns = new boolean[len];
		for (int i = 0; i < len; i++) columns[i] = true;
		double[] result = new double[len];
		int sign = 1;
		try {
			for (int i = 0; i < len; i++) {
				columns[i] = false;
				result[i] = sign * determinant(matrix, 0, columns);
				columns[i] = true;
				sign = -sign;
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			throw new IllegalArgumentException("Matrix is wrong shape");
		}
		return new Pnt(result);
	}

	/* Pnts as simplices */

	/**
	 * Determine the signed content (i.e., area or volume, etc.) of a simplex.
	 * @param simplex the simplex (as an array of Pnts)
	 * @return the signed content of the simplex
	 */
	public static double content (Pnt[] simplex) {
		Pnt[] matrix = new Pnt[simplex.length];
		for (int i = 0; i < matrix.length; i++)
			matrix[i] = simplex[i].extend(1);
		int fact = 1;
		for (int i = 1; i < matrix.length; i++) fact = fact*i;
		return determinant(matrix) / fact;
	}

	/**
	 * Circumcenter of a simplex.
	 * @param simplex the simplex (as an array of Pnts)
	 * @return the circumcenter (a Pnt) of simplex
	 */
	public static Pnt circumcenter (Pnt[] simplex) {
		//todo rewrite
		int dim = simplex[0].dimension();
		if (simplex.length - 1 != dim)
			throw new IllegalArgumentException("Dimension mismatch");
		Pnt[] matrix = new Pnt[dim];
		for (int i = 0; i < dim; i++)
			matrix[i] = simplex[i].bisector(simplex[i+1]);
		Pnt hCenter = cross(matrix);      // Center in homogeneous coordinates
		double last = hCenter.coordinates[dim];
		double[] result = new double[dim];
		for (int i = 0; i < dim; i++) result[i] = hCenter.coordinates[i] / last;
		return new Pnt(result);
	}
	
	
	/**
	 * Circumcenter of a simplex.
	 * @param simplex the simplex (as an array of Pnts)
	 * @return the circumcenter (a Pnt) of simplex
	 */
	public static Pnt barycenter(List<Pnt> points) {
		Pnt res = new Pnt(0,0);
		for(Pnt p : points)
			res = res.add(p);
		return res.scale(1/(double)points.size());
	}



	/**
	 * lexicographic order
	 */
	@Override
	public int compareTo(Pnt o) {
		if(o.dimension() != 2) return -1;
		if(this.getX()!= o.getX()) 
			return (int)(this.getX() - o.getX());
		else{
			return (int)(this.getY()- o.getY());
		}
	}
}