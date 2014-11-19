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
			coordinates[i] = other.coord(i);
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
			res.coordinates[i] += other.coord(i);
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

	/**
	 * @return the specified coordinate of this Pnt
	 * @throws ArrayIndexOutOfBoundsException for bad coordinate
	 */
	public double coord (int i) {
		return this.coordinates[i];
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
		double u1 = this.coord(0);
		double u2 = this.coord(1);
		double u3 = this.coord(2);
		double v1 = p.coord(0);
		double v2 = p.coord(1);
		double v3 = p.coord(2);
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
		return u.cross(v).coord(2) >=0;
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
			coords[i]= this.coord(i)*lambda;
		return new Pnt(coords);
	}

	public void scale(int index,double lambda){
		setCoord(index, coord(index) * lambda);
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
	 * Relation between this Pnt and a simplex (represented as an array of
	 * Pnts). Result is an array of signs, one for each vertex of the simplex,
	 * indicating the relation between the vertex, the vertex's opposite facet,
	 * and this Pnt.
	 *
	 * <pre>
	 *   -1 means Pnt is on same side of facet
	 *    0 means Pnt is on the facet
	 *   +1 means Pnt is on opposite side of facet
	 * </pre>
	 *
	 * @param simplex an array of Pnts representing a simplex
	 * @return an array of signs showing relation between this Pnt and simplex
	 * @throws IllegalArgumentExcpetion if the simplex is degenerate
	 */
	public int[] relation (Pnt[] simplex) {
		/* In 2D, we compute the cross of this matrix:
		 *    1   1   1   1
		 *    p0  a0  b0  c0
		 *    p1  a1  b1  c1
		 * where (a, b, c) is the simplex and p is this Pnt. The result is a
		 * vector in which the first coordinate is the signed area (all signed
		 * areas are off by the same constant factor) of the simplex and the
		 * remaining coordinates are the *negated* signed areas for the
		 * simplices in which p is substituted for each of the vertices.
		 * Analogous results occur in higher dimensions.
		 */
		int dim = simplex.length - 1;
		if (this.dimension() != dim)
			throw new IllegalArgumentException("Dimension mismatch");

		/* Create and load the matrix */
		Pnt[] matrix = new Pnt[dim+1];
		/* First row */
		double[] coords = new double[dim+2];
		for (int j = 0; j < coords.length; j++) coords[j] = 1;
		matrix[0] = new Pnt(coords);
		/* Other rows */
		for (int i = 0; i < dim; i++) {
			coords[0] = this.coordinates[i];
			for (int j = 0; j < simplex.length; j++)
				coords[j+1] = simplex[j].coordinates[i];
			matrix[i+1] = new Pnt(coords);
		}

		/* Compute and analyze the vector of areas/volumes/contents */
		Pnt vector = cross(matrix);
		double content = vector.coordinates[0];
		int[] result = new int[dim+1];
		for (int i = 0; i < result.length; i++) {
			double value = vector.coordinates[i+1];
			if (Math.abs(value) <= 1.0e-6 * Math.abs(content)) result[i] = 0;
			else if (value < 0) result[i] = -1;
			else result[i] = 1;
		}
		if (content < 0) {
			for (int i = 0; i < result.length; i++)
				result[i] = -result[i];
		}
		if (content == 0) {
			for (int i = 0; i < result.length; i++)
				result[i] = Math.abs(result[i]);
		}
		return result;
	}

	/**
	 * Test if this Pnt is outside of simplex.
	 * @param simplex the simplex (an array of Pnts)
	 * @return simplex Pnt that "witnesses" outsideness (or null if not outside)
	 */
	public Pnt isOutside (Pnt[] simplex) {
		int[] result = this.relation(simplex);
		for (int i = 0; i < result.length; i++) {
			if (result[i] > 0) return simplex[i];
		}
		return null;
	}

	/**
	 * Test if this Pnt is on a simplex.
	 * @param simplex the simplex (an array of Pnts)
	 * @return the simplex Pnt that "witnesses" on-ness (or null if not on)
	 */
	public Pnt isOn (Pnt[] simplex) {
		int[] result = this.relation(simplex);
		Pnt witness = null;
		for (int i = 0; i < result.length; i++) {
			if (result[i] == 0) witness = simplex[i];
			else if (result[i] > 0) return null;
		}
		return witness;
	}

	/**
	 * Test if this Pnt is inside a simplex.
	 * @param simplex the simplex (an arary of Pnts)
	 * @return true iff this Pnt is inside simplex.
	 */
	public boolean isInside (Pnt[] simplex) {
		int[] result = this.relation(simplex);
		for (int r: result) if (r >= 0) return false;
		return true;
	}

	/**
	 * Test relation between this Pnt and circumcircle of a simplex.
	 * @param simplex the simplex (as an array of Pnts)
	 * @return -1, 0, or +1 for inside, on, or outside of circumcircle
	 */
	public int vsCircumcircle (Pnt[] simplex) {
		Pnt[] matrix = new Pnt[simplex.length + 1];
		for (int i = 0; i < simplex.length; i++)
			matrix[i] = simplex[i].extend(1, simplex[i].dot(simplex[i]));
		matrix[simplex.length] = this.extend(1, this.dot(this));
		double d = determinant(matrix);
		int result = (d < 0)? -1 : ((d > 0)? +1 : 0);
		if (content(simplex) < 0) result = - result;
		return result;
	}

	/**
	 * Circumcenter of a simplex.
	 * @param simplex the simplex (as an array of Pnts)
	 * @return the circumcenter (a Pnt) of simplex
	 */
	public static Pnt circumcenter (Pnt[] simplex) {
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
	 * Main program (used for testing).
	 */
	public static void main (String[] args) {
		Pnt p = new Pnt(1, 2, 3);
		System.out.println("Pnt created: " + p);
		Pnt[] matrix1 = {new Pnt(1,2), new Pnt(3,4)};
		Pnt[] matrix2 = {new Pnt(7,0,5), new Pnt(2,4,6), new Pnt(3,8,1)};
		System.out.print("Results should be -2 and -288: ");
		System.out.println(determinant(matrix1) + " " + determinant(matrix2));
		Pnt p1 = new Pnt(1,1); Pnt p2 = new Pnt(-1,1);
		System.out.println("Angle between " + p1 + " and " +
				p2 + ": " + p1.angle(p2));
		System.out.println(p1 + " subtract " + p2 + ": " + p1.subtract(p2));
		Pnt v0 = new Pnt(0,0), v1 = new Pnt(1,1), v2 = new Pnt(2,2);
		Pnt[] vs = {v0, new Pnt(0,1), new Pnt(1,0)};
		Pnt vp = new Pnt(.1, .1);
		System.out.println(vp + " isInside " + toString(vs) +
				": " + vp.isInside(vs));
		System.out.println(v1 + " isInside " + toString(vs) +
				": " + v1.isInside(vs));
		System.out.println(vp + " vsCircumcircle " + toString(vs) + ": " +
				vp.vsCircumcircle(vs));
		System.out.println(v1 + " vsCircumcircle " + toString(vs) + ": " +
				v1.vsCircumcircle(vs));
		System.out.println(v2 + " vsCircumcircle " + toString(vs) + ": " +
				v2.vsCircumcircle(vs));
		System.out.println("Circumcenter of " + toString(vs) + " is " +
				circumcenter(vs));

		Pnt request = new Pnt(1.5,0);
		Pnt a = new Pnt(0,0);
		Pnt b = new Pnt(2,1);
		System.out.println("request.dist(a,b):"+request.dist(a,b));


		Pnt request2 = new Pnt(0,0);
		Pnt a2 = new Pnt(1,0);
		Pnt b2 = new Pnt(0,1);
		System.out.println("request2.dist(a2,b2):"+request2.dist(a2,b2));
	
	
		List<Pnt> points= new LinkedList<>();
		a = new Pnt(2.0,4.0);
		b = new Pnt(1.0,0.0);
		points.add(a);
		points.add(b);
		System.out.println("bary:"+Pnt.barycenter(points));
	}


	/**
	 * lexicographic order
	 */
	@Override
	public int compareTo(Pnt o) {
		if(o.dimension() != 2) return -1;
		if(this.coord(0)!= o.coord(0)) 
			return (int)(this.coord(0) - o.coord(0));
		else{
			return (int)(this.coord(1)- o.coord(1));
		}
	}
}