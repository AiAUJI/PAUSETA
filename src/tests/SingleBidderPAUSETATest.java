package tests;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import bid.Pauseta;
import enviroment.Location;
import needAGoodName.Agency;
import needAGoodName.Bid;
import needAGoodName.CompleteBid;
import needAGoodName.Requirement;
import needAGoodName.Resource;


/**
 * Let's see if PAUSETA works.
 *
 */
public class SingleBidderPAUSETATest {
	
    public static final double SYNERGY = 50;

	public static void main(String[] args) {
		
		//Agency
		Agency baconAgency = new Agency("0", "A bacon test Agency", "Bacon provider", new Location(), new ArrayList<Resource>());
		Agency beggyAgency = new Agency("1", "A beggy test Agency", "Vegetables provider", new Location(), new ArrayList<Resource>());
		
		//Some test resources
		ArrayList<Resource> baconAgencyResources = new ArrayList<Resource>();
		ArrayList<Resource> beggyAgencyResources = new ArrayList<Resource>();

		//Bacon agency
		baconAgencyResources.add(new Resource("Regular bacon", baconAgency, new Location(), 120));
		baconAgencyResources.add(new Resource("Regular bacon", baconAgency, new Location(), 110));
		baconAgencyResources.add(new Resource("Crispy bacon", baconAgency, new Location(), 160));
		baconAgencyResources.add(new Resource("Crispy bacon", baconAgency, new Location(), 140));
		baconAgencyResources.add(new Resource("Unbelievable crispy bacon", baconAgency, new Location(), 260));
		baconAgencyResources.add(new Resource("Unbelievable crispy bacon", baconAgency, new Location(), 240));
		
		//Beggy agency
		beggyAgencyResources.add(new Resource("Brocoli", beggyAgency, new Location(), 120));
		beggyAgencyResources.add(new Resource("Brocoli", beggyAgency, new Location(), 110));
		beggyAgencyResources.add(new Resource("Boiled eggplant", beggyAgency, new Location(), 160));
		beggyAgencyResources.add(new Resource("Boiled eggplant", beggyAgency, new Location(), 140));
		beggyAgencyResources.add(new Resource("Crispy bacon", beggyAgency, new Location(), 260));
		beggyAgencyResources.add(new Resource("Crispy bacon", beggyAgency, new Location(), 240));
		
		//Add the resources to the baconAgency
		for(int i = 0; i < baconAgencyResources.size(); i++)
			baconAgency.addResource(baconAgencyResources.get(i));

		//Add the resources to the beggyAgency
		for(int i = 0; i < beggyAgencyResources.size(); i++)
			beggyAgency.addResource(beggyAgencyResources.get(i));

		//Requirements
		HashMap<String, Integer> requirementsMap = new HashMap<String, Integer>();
		requirementsMap.put("Crispy bacon", 3);
		requirementsMap.put("Brocoli", 1);
		requirementsMap.put("Boiled eggplant", 1);
		requirementsMap.put("Unbelievable crispy bacon", 2);
				
		Requirement requirements = new Requirement(requirementsMap);
		
		//Calculate synergys, thanks to magic
		OrderedPowerSet<Resource> baconSet = new OrderedPowerSet<Resource>(baconAgencyResources);
		ArrayList<Bid> baconSynergyList = new ArrayList<Bid>();

		for(int i = 1; i < baconAgencyResources.size() + 1; i++){
			
			//This creates a list containing all the possible combinations of i elements
			List<LinkedHashSet<Resource>> baconSynergy = baconSet.getPermutationsList(i);
			
			for(Set<Resource> s: baconSynergy){
				
				//We need to check if the set contains 2 (or more) resources of the same type
				//if it does, do not add it.
				//I could have done the permutations of the types, however after a lot of thought
				//this seems to be more efficient (?). If I just did all the possible combinations
				//of types I would have had to do all the possible combination of prices for every pair
				//of resource types, this way is "free".
				
				//Also get the total value
				
				Set<String> counter = new HashSet<String>();
				boolean add = true;
				double value = 0.0;
				
				for(Resource r: s){
					
					value += r.value;
					
					if(counter.add(r.type) != true){
						
						add = false;
					}
				}
				
				if(add){
				
					baconSynergyList.add(new Bid(new ArrayList<Resource>(s), baconAgency, value + SYNERGY));
				}
			}
		}
		
		OrderedPowerSet<Resource> beggySet = new OrderedPowerSet<Resource>(beggyAgencyResources);
		ArrayList<Bid> beggySynergyList = new ArrayList<Bid>();

		for(int i = 1; i < beggyAgencyResources.size()+1; i++){
			
			//This creates a list containing all the possible combinations of i elements
			List<LinkedHashSet<Resource>> beggySynergy = beggySet.getPermutationsList(i);
			
			for(Set<Resource> s: beggySynergy){
				
				//We need to check if the set contains 2 (or more) resources of the same type
				//if it does, do not add it.
				//I could have done the permutations of the types, however after a lot of thought
				//this seems to be more efficient (?). If I just did all the possible combinations
				//of types I would have had to do all the possible combination of prices for every pair
				//of resource types, this way is "free".
				
				//Also get the total value
				
				Set<String> counter = new HashSet<String>();
				boolean add = true;
				double value = 0.0;

				for(Resource r: s){
					
					value += r.value;
					
					if(counter.add(r.type) != true){
						
						add = false;
					}
				}
				
				if(add){
					
					beggySynergyList.add(new Bid(new ArrayList<Resource>(s), beggyAgency, value + SYNERGY));
				}
			}
		}

		//One pauseta for every agency
		Pauseta pausetaBaconAgency = new Pauseta(baconSynergyList);
		Pauseta pausetaBeggyAgency = new Pauseta(beggySynergyList);
				
		//Simulate the real communication, next time Jade
		
		CompleteBid baconSolution;
		CompleteBid beggySolution;
		
		for(int i = 1; i < 10; i++){
			
			System.out.println("ITERATION: " + i);
			
			System.out.println("Bacon agengy proposed complete solution: ");
			baconSolution = pausetaBaconAgency.greedyPausetaBid(baconAgency, i, requirements);
			System.out.println(baconSolution.toString());
			
			System.out.println("Beggy agency proposed complete solution: ");
			beggySolution = pausetaBeggyAgency.greedyPausetaBid(beggyAgency, i, requirements);
			System.out.println(beggySolution.toString());
			
			//Communicate the results to each other
			pausetaBaconAgency.addCompleteBidToSAB(beggySolution);
			pausetaBeggyAgency.addCompleteBidToSAB(baconSolution);
		}
	}
}
