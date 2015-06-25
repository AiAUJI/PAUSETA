package needAGoodName;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import enviroment.Intersection;
import enviroment.Map;
import needAGoodName.TMP.Triple;

public class UtilitiesAndSynergies {

	public static final int BASEUTILITY = 1000;
	public static final int SYNERGY = 50;
	public static final int UNITINCREMENT = 100; 

	public static List<Bid> setUtilitiesAndSynergies(Agency agency, TMP tmp, Map map){

		List<Resource> resources = agency.resources;
		List<Triple> requirements = tmp.requirements;
		HashMap<String, Integer> requirementsMap = tmp.requirementsMap.requirements;
		
		//I need to evaluate dijkstra for every resource that is in the requirements
		HashMap<String, ArrayList<Double>> distances = new HashMap<String, ArrayList<Double>>();
		HashMap<Resource, Double> resourcesAndDistance = new HashMap<Resource, Double>();

		for(Resource resource: resources){

			//If that resource is in the requirements
			if(requirementsMap.containsKey(resource.type)){

				HashMap<Intersection, Intersection> dijkstra = map.shortestPathsFrom(resource.location.segment.origin.id);

				//Get a map with every type of resource and its distances from shorter to longer
				for(Triple triple: requirements){

					//Only evaluate the distance to those Intersections that need this kind of resource
					if(triple.resource.equals(resource.type)){
						
						Double dist = map.getDistance(dijkstra, triple.intersectionID) - resource.location.position; //Deal with the offset
						
						resourcesAndDistance.put(resource, dist);
						
						if(distances.containsKey(resource.type)){

							distances.get(resource.type).add(dist);
						}else{ //Create queue

							distances.put(resource.type, new ArrayList<Double>());
							distances.get(resource.type).add(dist);
						}
					}
				}
			}
		}
		
		//Now I have for every type of resource in the requirements, a list with its distances to the intersections it has to go (Sanity check)
		//To compute the Utility value I will use the next formula (patent pending):
		//
		//		Type   |  Distances   | Utilities
		//  ------------------------------------
		//	Fast car   | 2, 4, 16     | 1299.998, 1299.996, 1299.984
		//  Motorbike  | 3, 8         | 1199.997, 1199.992
		//
		// UtilityType[i] = BASEUTILITY - Distance[i]/BASEUTILITY + UNITINCREMENT * numberOfElementsOfType(type)

		for(Resource resource: resources){

			//If that resource is in the requirements
			if(requirementsMap.containsKey(resource.type)){
				
				//Search for the distance of that specific resource
				Double dist = resourcesAndDistance.get(resource);
				
				resource.value = BASEUTILITY - dist/BASEUTILITY + UNITINCREMENT * distances.get(resource.type).size();
			}
		}

		//We have now all the Resources with their values
		//Now we calculate the Synergies (Magic)
		OrderedPowerSet<Resource> set= new OrderedPowerSet<Resource>(resources);
		ArrayList<Bid> synergyList = new ArrayList<Bid>();

		for(int i = 1; i < resources.size() + 1; i++){

			//This creates a list containing all the possible combinations of i elements
			List<LinkedHashSet<Resource>> synergy = set.getPermutationsList(i);

			for(Set<Resource> s: synergy){

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

						synergyList.add(new Bid(new ArrayList<Resource>(s), agency, value + SYNERGY * s.size()));
					}else{

						synergyList.add(new Bid(new ArrayList<Resource>(s), agency, value));
					}
				}
			}
		}
		return synergyList;
	}
}
