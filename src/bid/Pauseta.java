package bid;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.UUID;

import needAGoodName.Agency;
import needAGoodName.Bid;
import needAGoodName.CompleteBid;
import needAGoodName.Requirement;
import needAGoodName.Resource;

/**
 * Class to hold the PAUSETA algorithm.
 *
 */
public class Pauseta implements Serializable{
	
	//Array to hold the Set Of All Bids
	private ArrayList<CompleteBid> SAB = new ArrayList<CompleteBid>();
	
	//Array to hold my bids
	private ArrayList<CompleteBid> myOwnPrivateBids = new ArrayList<CompleteBid>();
	
	//Comparator used to sort a CompleteBid List
	private Comparator<CompleteBid> completeBidComparator = new Comparator<CompleteBid>(){
	    public int compare(CompleteBid c1, CompleteBid c2){
	        
	    	if(Math.sqrt(c1.value/c1.bids.size()) > Math.sqrt(c2.value/c2.bids.size()))
	    		return 1;
	    	
	    	if(Math.sqrt(c1.value/c1.bids.size()) < Math.sqrt(c2.value/c2.bids.size()))
	    		return -1;
	    	
	    	return 0;
	    }};

	
	/**
	 * Algorithm that chooses the best bet in each step
	 * 
	 * @param bidder The identifier of the bidder.
	 * @param stage The stage of the auction.
	 * @param requirement Requirements that need to be fulfilled.
	 * @return The best bid that can be offered.
	 */
	public CompleteBid greedyPausetaBid(Agency bidder, int stage, Requirement requirement){
		
		//Array to hold the bids I can do
		ArrayList<CompleteBid> myBids = new ArrayList<CompleteBid>();
		
		//Array to hold bids that don't belong to me
		ArrayList<CompleteBid> theirBids = new ArrayList<CompleteBid>();
		
		//Array to hold the ids already used
		ArrayList<UUID> idsUsed = new ArrayList<UUID>();
		
		//Iterate over the SAB looking for bids that are from other bidders
		for(CompleteBid cb: SAB){
			
			ArrayList<Bid> bids = new ArrayList<Bid>();
			
			for(Bid b: cb.bids){
				
				if(!b.bidder.equals(bidder)){
					
					bids.add(b);
				}
				
				theirBids.add(new CompleteBid(bids));
			}
		}
		
		//myBids is calculated by using bids that involve an equal or less number
		//of type of resources than stage.
		for(CompleteBid b: myOwnPrivateBids){
			
			if(b.bids.size() <= stage){
				
				myBids.add(b);
			}
		}

		//CompleteBid to be returned, g in the original paper
		CompleteBid result = new CompleteBid();
		
		if(myBids.isEmpty()){
			
			return result;
		}
		
		//Join both lists and sort it
		ArrayList<CompleteBid> bids = new ArrayList<CompleteBid>(theirBids);
		bids.addAll(myBids);
		
		Collections.sort(bids, completeBidComparator);
		
		while(!bids.isEmpty()){
			
			CompleteBid b = bids.get(0);
			bids.remove(0);
			
			//For each bid in the complete bid
			for(Bid bid: b.bids){
				
				//If the id of the bid has been used before
				if(idsUsed.contains(bid.id)){
					continue;
				}
			}
			
			//Name taken from the original pseudocode
			ArrayList<Resource> Iresult = result.getResources();
			ArrayList<Resource> Ib = b.getResources();
			
			//If for each resource in the bid, it fulfills the requirement add it
			boolean add = true;
			
			for(Resource r: Ib){
				
				//Count all the resources in Iresult with the same type as r
				int countIresult = 0;
				
				for(Resource rIresult: Iresult){
					
					if(rIresult.type.equals(r.type))
						countIresult++;
				}
				
				//Count all the resources in Requirement with the same type as r
				int countRequirement = requirement.requirements.get(r.type);
				
				//It is bigger than the requirement
				if(countIresult > countRequirement){
					
					add = false;
				}
			}
			
			//TODO: Check if this works, maybe each CompleteBid needs its own UUID rather than adding all the UUIDs for the separated Bids
			if(add){
				
				result.addCompleteBid(b);
				idsUsed.addAll(b.getIds());
			}
		}
		
		return result;	
	}
}
