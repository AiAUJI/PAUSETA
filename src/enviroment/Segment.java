package enviroment;

import java.io.Serializable;

/**
 * Represents a section of a road in a single direction.
 * This section is only accessible from its origin and can only be left by its destination.
 */
public class Segment implements Serializable{

	//Where the segment is accessed from
	public Intersection origin;
	
	//Where the segment is left
	public Intersection destination;
	
	//Length in kilometers of the segment
	public double length;
	
	
	/**
	 * Default constructor. 
	 */
	public Segment(){
		
		this.origin = new Intersection();
		this.destination = new Intersection();
		this.length = 0.0;
	}
	
	/**
	 * Constructor. 
	 *
	 * @param  origin {@link Intersection} where this {@link Segment} starts.
	 * @param  destination {@link Intersection} where this {@link Segment} ends.
	 * @param  length The length of this {@link Segment} in Km.
	 */
	public Segment(Intersection origin, Intersection destination, double length){
		
		this.origin = origin;
		this.destination = destination;
		this.length = length;
	}
}
