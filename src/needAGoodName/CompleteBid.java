package needAGoodName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;

/**
 * A CompleteBid is a list of {@link Bid} that completes a {@link Requirement}.
 */
public class CompleteBid implements Serializable{
	
	//Bids that form the complete bid
	public ArrayList<Bid> bids;
	
	//Total value of the CompleteBid
	public double value;
	
	/**
	 * Default constructor.
	 */
	public CompleteBid(){
		
		this.bids = new ArrayList<Bid>();
		this.value = 0.0;
	}
	
	/**
	 * Constructor.
	 * 
	 * @param bids A list of {@link Bid} that form this {@link CompleteBid}.
	 */
	public CompleteBid(ArrayList<Bid> bids){
		
		this.bids = bids;
		this.value = this.getValue();
	}
	
	/**
	 * Constructor.
	 * 
	 * @param bid A {@link Bid} that forms this {@link CompleteBid}.
	 */
	public CompleteBid(Bid bid){
		
		this.bids = new ArrayList<Bid>();
		this.bids.add(bid);
		
		this.value = this.getValue();
	}
	
	/**
	 * Adds a {@link bid} to this {@link CompleteBid}.
	 * 
	 * @param bid {@link Bid} to be added.
	 * @return A boolean whether the {@link Bid} has been added or not.
	 */
	public boolean addBid(Bid bid){
		
		boolean result = this.bids.add(bid);
		this.value = this.getValue();
		
		return result;
	}
	
	/**
	 * Adds a {@link CompleteBid} to this {@link CompleteBid}.
	 * 
	 * @param bid {@link CompleteBid} to be added.
	 * @return A boolean whether the {@link CompleteBid} has been added or not.
	 */
	public boolean addCompleteBid(CompleteBid bid){
		
		boolean result = this.bids.addAll(bid.bids);
		this.value = this.getValue();
		
		return result;
	}
	
	/**
	 * Returns the total value of this {@link CompleteBid}.
	 * 
	 * @return A double with the value of the whole {@link CompleteBid}.
	 */
	public double getValue(){
	
		double value = 0.0;
		
		for(int i = 0; i < this.bids.size(); i++){
			
			value += this.bids.get(i).getValue();
		}
		
		return value;
	}
	
	/**
	 * Returns an Array containing all the resources on this CompleteBid.
	 * 
	 * @return An Array with all the resources on this CompleteBid.
	 */
	public ArrayList<Resource> getResources(){
		
		ArrayList<Resource> result = new ArrayList<Resource>();
		
		for(Bid b: this.bids){
			for(Resource r: b.resources){
				
				result.add(r);
			}
		}
		
		return result;
	}
	
	/**
	 * Returns an Array containing all the ids of the bids on this CompleteBid.
	 * 
	 * @return An Array with all the ids on this CompleteBid.
	 */
	public ArrayList<UUID> getIds(){
		
		ArrayList<UUID> result = new ArrayList<UUID>();
		
		for(Bid b: this.bids){
			
			result.add(b.id);
		}
		
		return result;
	}
	
	/**
	 * Returns the information of the CompleteBid as a String
	 * 
	 * @return A string.
	 */
	public String toString(){
		
		String res = "";
		
		for(Bid b: this.bids){
			
			res += "Bid " + b.id + ":\n";
			
			for(Resource r: b.resources){
				
				res += "\tResource: " + r.type + " Value: " + r.value + "\n";
			}
		}
		
		return res;
	}
	
}
