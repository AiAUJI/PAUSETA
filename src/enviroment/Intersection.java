package enviroment;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Represents the union of several {@link Segment}. A roundabout, a crossroad, a change of route...
 */
public class Intersection implements Serializable{
	

	private static final long serialVersionUID = 3885620456847709732L;

	//Unique id
	public String id;
	
	//In segments
	public ArrayList<Segment> in;
	
	//Out segments
	public ArrayList<Segment> out;
	
	//Coordinates
	public double[] coordinates;
	
	/**
	 * Default constructor. 
	 */
	public Intersection(){
		
		this.id = "";
		this.in = new ArrayList<Segment>();
		this.out = new ArrayList<Segment>();
		this.coordinates = new double[2];
	}
	
	/**
	 * Constructor. 
	 *
	 * @param  in   A list of {@link Segment} that go into this intersection.
	 * @param  out  A list of {@link Segment} that leave this intersection.
	 * @param coordinates A array with the coordinates.
	 */
	public Intersection(String id, ArrayList<Segment> in, ArrayList<Segment> out, double[] coordinates){
		
		this.id = id;
		this.in = in;
		this.out = out;
		this.coordinates = coordinates;
	}
	
	/**
	 * Constructor. 
	 *
	 * @param  in   A list of {@link Segment} that go into this intersection.
	 * @param  out  A list of {@link Segment} that leave this intersection.
	 * @param longitude A double with the longitude.
	 * @param latitude A double with the latitude.
	 */
	public Intersection(String id, double latitude, double longitude){
		
		this.id = id;
		this.in = new ArrayList<Segment>();
		this.out = new ArrayList<Segment>();
		this.coordinates = new double[]{longitude, latitude};
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
