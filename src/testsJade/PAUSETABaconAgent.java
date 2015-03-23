package testsJade;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import testsNoJade.OrderedPowerSet;
import enviroment.Location;
import needAGoodName.Agency;
import needAGoodName.Bid;
import needAGoodName.Requirement;
import needAGoodName.Resource;
import bid.Pauseta;
import jade.core.Agent;


public class PAUSETABaconAgent extends Agent{
	
	private static final long serialVersionUID = 8424587017625617092L;
	
	public static final double SYNERGY = 50;

	protected void setup(){
		
		Agency baconAgency = new Agency("0", "A bacon test Agency", "Bacon provider", new Location(), new ArrayList<Resource>());
		
		ArrayList<Resource> baconAgencyResources = new ArrayList<Resource>();

		//Bacon agency
		baconAgencyResources.add(new Resource("Regular bacon", baconAgency, new Location(), 120));
		baconAgencyResources.add(new Resource("Regular bacon", baconAgency, new Location(), 110));
		baconAgencyResources.add(new Resource("Crispy bacon", baconAgency, new Location(), 160));
		baconAgencyResources.add(new Resource("Crispy bacon", baconAgency, new Location(), 140));
		baconAgencyResources.add(new Resource("Unbelievable crispy bacon", baconAgency, new Location(), 260));
		baconAgencyResources.add(new Resource("Unbelievable crispy bacon", baconAgency, new Location(), 240));

		//Add the resources to the baconAgency
		for(int i = 0; i < baconAgencyResources.size(); i++)
			baconAgency.addResource(baconAgencyResources.get(i));
		
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
				
					if(i != 1){
						
						baconSynergyList.add(new Bid(new ArrayList<Resource>(s), baconAgency, value + SYNERGY));
					}else{
						
						baconSynergyList.add(new Bid(new ArrayList<Resource>(s), baconAgency, value));
					}
				}
			}
		}
		
		Pauseta pausetaBaconAgency = new Pauseta(baconSynergyList);

		PAUSETASendBehaviour pausetaBe = new PAUSETASendBehaviour(this, baconAgency, 1, pausetaBaconAgency, requirements);
		
		addBehaviour(pausetaBe);
	}
}
