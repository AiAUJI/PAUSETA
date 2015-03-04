package enviroment;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Represents the union of several {@link Segment}. A roundabout, a crossroad, a change of route...
 */
public class Intersection implements Serializable{
	
	//In segments
	public ArrayList<Segment> in;
	
	//Out segments
	public ArrayList<Segment> out;
	
	/**
	 * Default constructor. 
	 */
	public Intersection(){
		
		this.in = new ArrayList<Segment>();
		this.out = new ArrayList<Segment>();
	}
	
	/**
	 * Constructor. 
	 *
	 * @param  in   A list of {@link Segment} that go into this intersection.
	 * @param  out  A list of {@link Segment} that leave this intersection.
	 */
	public Intersection(ArrayList<Segment> in, ArrayList<Segment> out){
		
		this.in = in;
		this.out = out;
	}
	
	/**
	 * Adds an in {@link Segment} to the Intersection. 
	 *
	 * @param  segment  {@link Segment} to be added.
	 * @return A boolean whether the collection has been modified or not.
	 */
	public boolean addInSegment(Segment segment){
		
		return this.in.add(segment);
	}
	
	/**
	 * Adds an out {@link Segment} to the Intersection. 
	 *
	 * @param  segment  {@link Segment} to be added.
	 * @return A boolean whether the {@link Segment} has been added or not.
	 */
	public boolean addOutSegment(Segment segment){
		
		return this.out.add(segment);
	}
}
