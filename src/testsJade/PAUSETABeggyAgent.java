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


public class PAUSETABeggyAgent extends Agent{
	
	private static final long serialVersionUID = 4802158321187376555L;
	
	public static final double SYNERGY = 50;

	protected void setup(){
		
		Agency beggyAgency = new Agency("1", "A beggy test Agency", "Vegetables provider", new Location(), new ArrayList<Resource>());
		
		ArrayList<Resource> beggyAgencyResources = new ArrayList<Resource>();

		//beggy agency
		beggyAgencyResources.add(new Resource("Brocoli", beggyAgency, new Location(), 120));
		beggyAgencyResources.add(new Resource("Brocoli", beggyAgency, new Location(), 110));
		beggyAgencyResources.add(new Resource("Boiled eggplant", beggyAgency, new Location(), 160));
		beggyAgencyResources.add(new Resource("Boiled eggplant", beggyAgency, new Location(), 140));
		beggyAgencyResources.add(new Resource("Crispy bacon", beggyAgency, new Location(), 260));
		beggyAgencyResources.add(new Resource("Crispy bacon", beggyAgency, new Location(), 240));

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
		OrderedPowerSet<Resource> beggySet = new OrderedPowerSet<Resource>(beggyAgencyResources);
		ArrayList<Bid> beggySynergyList = new ArrayList<Bid>();

		for(int i = 1; i < beggyAgencyResources.size() + 1; i++){
			
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
					
					if(i != 1){
						
						beggySynergyList.add(new Bid(new ArrayList<Resource>(s), beggyAgency, value + SYNERGY));
					}else{
						
						beggySynergyList.add(new Bid(new ArrayList<Resource>(s), beggyAgency, value));
					}
				}
			}
		}
		
		Pauseta pausetabeggyAgency = new Pauseta(beggySynergyList);

		PAUSETASendBehaviour pausetaBe = new PAUSETASendBehaviour(this, beggyAgency, 1, pausetabeggyAgency, requirements);
		
		addBehaviour(pausetaBe);
	}
}