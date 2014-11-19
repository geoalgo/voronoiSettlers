package delaunay;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class Edge extends ArraySet<Pnt> {

    private int idNumber;                   // The id number
    private Pnt circumcenter = null;        // The triangle's circumcenter

    private static int idGenerator = 0;     // Used to create id numbers
    public static boolean moreInfo = true; // True iff more info in toString

    /**
     * @param vertices the vertices of the Triangle.
     * @throws IllegalArgumentException if there are not three distinct vertices
     */
    public Edge (Pnt... vertices) {
        this(Arrays.asList(vertices));
    }

    /**
     * @param collection a Collection holding the Simplex vertices
     * @throws IllegalArgumentException if there are not three distinct vertices
     */
    public Edge (Collection<? extends Pnt> collection) {
        super(collection);
        idNumber = idGenerator++;
        if (this.size() != 2)
            throw new IllegalArgumentException("Edge must have 2 vertices");
    }

    @Override
    public String toString () {
        if (!moreInfo) return "Edge" + idNumber;
        return "Edge" + idNumber + super.toString();
    }

    /**
     * Get arbitrary vertex of this triangle, but not any of the bad vertices.
     * @param badVertices one or more bad vertices
     * @return a vertex of this triangle, but not one of the bad vertices
     * @throws NoSuchElementException if no vertex found
     */
    public Pnt getVertexButNot (Pnt... badVertices) {
        Collection<Pnt> bad = Arrays.asList(badVertices);
        for (Pnt v: this) if (!bad.contains(v)) return v;
        throw new NoSuchElementException("No vertex found");
    }

//    /**
//     * True iff triangles are neighbors. Two triangles are neighbors if they
//     * share a facet.
//     * @param triangle the other Triangle
//     * @return true iff this Triangle is a neighbor of triangle
//     */
//    public boolean isNeighbor (Triangle triangle) {
//        int count = 0;
//        for (Pnt vertex: this)
//            if (!triangle.contains(vertex)) count++;
//        return count == 1;
//    }
//
//    /**
//     * Report the facet opposite vertex.
//     * @param vertex a vertex of this Triangle
//     * @return the facet opposite vertex
//     * @throws IllegalArgumentException if the vertex is not in triangle
//     */
//    public ArraySet<Pnt> facetOpposite (Pnt vertex) {
//        ArraySet<Pnt> facet = new ArraySet<Pnt>(this);
//        if (!facet.remove(vertex))
//            throw new IllegalArgumentException("Vertex not in triangle");
//        return facet;
//    }

    /**
     * @return the triangle's circumcenter
     */
    public Pnt getCircumcenter () {
        if (circumcenter == null)
            circumcenter = Pnt.circumcenter(this.toArray(new Pnt[0]));
        return circumcenter;
    }
    
    
    public double dist(Pnt p){
    	return p.dist(this.get(0), this.get(1));
    }
    
    /**
     * @return the triangle's radius
     */
    public double getRadius() {
    	Pnt center = getCircumcenter();
    	double radius = 0;
    	for(Pnt p : this){
    		if(p.dist(center)>radius) radius =  p.dist(center);
    	}
    	return radius;
    }

    /* The following two methods ensure that a Triangle is immutable */

    @Override
    public boolean add (Pnt vertex) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public Iterator<Pnt> iterator () {
        return new Iterator<Pnt>() {
            private Iterator<Pnt> it = Edge.super.iterator();
            public boolean hasNext() {return it.hasNext();}
            public Pnt next() {return it.next();}
            public void remove() {throw new UnsupportedOperationException();}
        };
    }

    /* The following two methods ensure that all triangles are different. */

    @Override
    public int hashCode () {
        return (int)(idNumber^(idNumber>>>32));
    }

    @Override
    public boolean equals (Object o) {    	
        return (super.equals(o));
    }
}
