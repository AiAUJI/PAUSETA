package needAGoodName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Represents a simple or composite bid in some resources.
 */

public class Bid implements Serializable{
	
	//Unique ID of the bid
	public UUID id;

	//Resources that form this bid
	public ArrayList<Resource> resources;
	
	//Who is the bidder
	public Agency bidder;
	
	//Total value of the bid
	public double value;
	
	/**
	 * Default constructor. 
	 */
	public Bid(){
		
		this.id  = UUID.randomUUID();
		this.resources = new ArrayList<Resource>();
		this.bidder = new Agency();
		this.value = 0.0;
	}
	
	/**
	 * Constructor. 
	 *
	 * @param  resources A list of {@link Resource} to be added.
	 * @param  bidder The {@link Agency} that makes the {@link Bid}. 
	 */
	public Bid(ArrayList<Resource> resources, Agency bidder){
		
		this.id  = UUID.randomUUID();
		this.resources = resources;
		this.bidder = bidder;
		this.value = this.getValue();
	}
	
	/**
	 * Constructor. 
	 *
	 * @param  resources A list of {@link Resource} to be added.
	 * @param  bidder The {@link Agency} that makes the {@link Bid}.
	 * @param  value  	The value of the bid.
	 */
	public Bid(ArrayList<Resource> resources, Agency bidder, double value){
		
		this.id  = UUID.randomUUID();
		this.resources = resources;
		this.bidder = bidder;
		this.value = value;
	}
	
	/**
	 * Constructor. 
	 *
	 * @param  resource A {@link Resource} to be added.
	 * @param  bidder The {@link Agency} that makes the {@link Bid}. 
	 */
	public Bid(Resource resource, Agency bidder){
		
		ArrayList<Resource> resources = new ArrayList<Resource>();
		resources.add(resource);
		
		this.id  = UUID.randomUUID();
		this.resources = resources;
		this.bidder = bidder;
		this.value = this.getValue();
	}
	
	/**
	 * Adds a {@link Resource} to this {@link Bid} and uptates its value.
	 * 
	 * @param resource {@link Resource} to be added.
	 * @return A boolean whether the {@link Resource} has been added or not.
	 */
	public boolean addResource(Resource resource){
		
		boolean result = this.resources.add(resource);
		this.value = this.getValue();
		
		return result; 
	}
	
	/**
	 * Returns the total value of this {@link Bid}.
	 * 
	 * @return A double with the value of the whole {@link Bid}.
	 */
	public double getValue(){
	
		double value = 0.0;
		
		for(int i = 0; i < this.resources.size(); i++){
			
			value += this.resources.get(i).value;
		}
		
		return value;
	}
}
