package enviroment;

import java.io.Serializable;

/**
 * A location is a specific point in the road network.
 */

public class Location implements Serializable{
	
	private static final long serialVersionUID = -4639648081136970198L;

	//Offset from the beginning of the origin in km
	public double position;
	
	//Segment
	public Segment segment;
	
	/**
	 * Default constructor. 
	 */
	public Location(){
		
		this.position = 0.0;
		this.segment = new Segment();
	}
	
	/**
	 * Constructor. 
	 *
	 * @param  position Offset from the beginning of the {@link Segment} in Km. 
	 * @param  segment  {@link Segment} where the position is located.
	 */
	public Location(double position, Segment segment){
		
		this.position = position;
		this.segment = segment;
	}
}
