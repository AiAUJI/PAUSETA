package needAGoodName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * A CompleteBid is a list of {@link Bid} that complete a {@link Requirement}.
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
	
}
