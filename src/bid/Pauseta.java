package bid;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
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

	private static final long serialVersionUID = -2359527997110629812L;

	//Array to hold the Set Of All Bids
	private ArrayList<Bid> SAB;

	//Array to hold my bids
	private ArrayList<Bid> myOwnPrivateBids;

	//Comparator used to sort a CompleteBid List
	private Comparator<Bid> bidComparator;

	/**
	 * Default constructor. 
	 */
	public Pauseta(){

		this.SAB = new ArrayList<Bid>();
		this.myOwnPrivateBids = new ArrayList<Bid>();

		this.bidComparator  = new Comparator<Bid>(){
			public int compare(Bid b1, Bid b2){

				if(Math.sqrt(b1.value/b1.resources.size()) > Math.sqrt(b2.value/b2.resources.size()))
					return 1;

				if(Math.sqrt(b1.value/b1.resources.size()) < Math.sqrt(b2.value/b2.resources.size()))
					return -1;

				return 0;
			}};
	}

	/**
	 * Constructor.
	 * 
	 * @param myOwnPrivateBids Private bids taking in count the synergy.
	 */
	public Pauseta(ArrayList<Bid> myOwnPrivateBids){

		this.SAB = new ArrayList<Bid>();
		this.myOwnPrivateBids = myOwnPrivateBids;

		this.bidComparator  = new Comparator<Bid>(){
			public int compare(Bid b1, Bid b2){

				if(Math.sqrt(b1.value/b1.resources.size()) > Math.sqrt(b2.value/b2.resources.size()))
					return 1;

				if(Math.sqrt(b1.value/b1.resources.size()) < Math.sqrt(b2.value/b2.resources.size()))
					return -1;

				return 0;
			}};
	}

	/**
	 * Adds the bids of a CompleteBid to the SAB.
	 * 
	 * @param completeBid CompleteBid to add to the SAB.
	 * @return A boolean whether all the {@link Bid} have been added or not.
	 */
	public boolean addCompleteBidToSAB(CompleteBid completeBid){

		boolean res = true;

		for(Bid b: completeBid.bids){

			res &= this.SAB.add(b);
		}

		return res;
	}

	/**
	 * The algorithm that is described in the original paper. It gets a pretty good solution to fullfil the requirements using Composite bids.
	 * 
	 * @param bidder The identifier of the bidder.
	 * @param stage The stage of the auction.
	 * @param requirement Requirements that need to be fulfilled.
	 * @return The best CompleteBid that can be offered.
	 */
	public CompleteBid greedyPausetaBid(Agency bidder, int stage, Requirement requirement){

		//Array to hold the bids I can do
		ArrayList<Bid> myBids = new ArrayList<Bid>();

		//Array to hold bids that don't belong to me
		ArrayList<Bid> theirBids = new ArrayList<Bid>();

		//Array to hold the ids already used
		ArrayList<UUID> idsUsed = new ArrayList<UUID>();

		//Iterate over the SAB looking for bids that are from other bidders
		ArrayList<Bid> bids;

		for(Bid b: SAB){

			if(!b.bidder.id.equals(bidder.id)){

				theirBids.add(b);
			}
		}

		//myBids is calculated by using bids that involve an equal or less number
		//of type of resources than stage.
		for(Bid b: myOwnPrivateBids){

			if(b.resources.size() <= stage){

				myBids.add(b);
			}
		}

		//CompleteBid to be returned, g in the original paper
		CompleteBid result = new CompleteBid();

		if(myBids.isEmpty()){

			return result;
		}

		//Join both lists and sort it
		bids = new ArrayList<Bid>(theirBids);
		bids.addAll(myBids);

		Collections.sort(bids, bidComparator);

		while(!bids.isEmpty()){

			Bid currentBid = bids.get(0);
			bids.remove(0);

			//If the id of the bid has been used before, skip
			if(idsUsed.contains(currentBid.id)){

				continue;
			}

			//Resources to compare
			ArrayList<Resource> resourcesResult = result.getResources();
			ArrayList<Resource> resourcesCurrentBid = currentBid.resources;

			//If for each resource in the bid, it fulfills the requirement add it
			boolean add = true;
			Map<String, Integer> counter = new HashMap<String, Integer>();

			for(Resource r: resourcesCurrentBid){

				//Check if all the resource types in the currentBid are required
				add &= requirement.requirements.containsKey(r.type);

				//Count how many of each resource type are there in the currentBid
				Integer value  = counter.get(r.type);

				if(value == null){

					counter.put(r.type, 1);
				} else {

					counter.put(r.type, value + 1);
				}
			}
			
			//Add in the counter the resources that have been already added from past bids
			for(Resource r: resourcesResult){
				
				Integer value  = counter.get(r.type);

				if(value == null){

					counter.put(r.type, 1);
				} else {

					counter.put(r.type, value + 1);
				}
			}
			
			//We know that all the resources in the currentBid are in the requirements
			//Now we need to know if the counter for each resource fits within the requirements
			if(add){

				Set<String> keys = counter.keySet();
				
				for(String key: keys){

					//System.out.println("Key: " + key + " Value: " + counter.get(key) + " req: " + requirement.requirements.get(key));
					add &= (counter.get(key) <= requirement.requirements.get(key));
				}
			}


			//Everything checks out, add the bid
			if(add){

				result.addBid(currentBid);
				idsUsed.add(currentBid.id);
			}
		}

		return result;	
	}
}
