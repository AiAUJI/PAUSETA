package tests;

import java.util.ArrayList;
import java.util.HashMap;
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
 * Let's see if PAUSETA works
 *
 */
public class SingleBidderPAUSETATest {

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
			
			List<LinkedHashSet<Resource>> baconSynergy = baconSet.getPermutationsList(i);
			
			for(Set<Resource> s: baconSynergy){
				
				baconSynergyList.add(new Bid(new ArrayList<Resource>(s), baconAgency, 200));
			}
		}
		
		OrderedPowerSet<Resource> beggySet = new OrderedPowerSet<Resource>(beggyAgencyResources);
		ArrayList<Bid> beggySynergyList = new ArrayList<Bid>();

		for(int i = 1; i < beggyAgencyResources.size()+1; i++){
			
			List<LinkedHashSet<Resource>> beggySynergy = beggySet.getPermutationsList(i);
			
			for(Set<Resource> s: beggySynergy){
				
				beggySynergyList.add(new Bid(new ArrayList<Resource>(s), beggyAgency, 200));
			}
		}


		//One pauseta for every agency
		Pauseta pausetaBaconAgency = new Pauseta(baconSynergyList);
		Pauseta pausetaBeggyAgency = new Pauseta(beggySynergyList);
		
		//This should be do with Jade, but this is a test
		//This piece of code "sends" the resources of each agency to the others
		//TODO: Work in improving constructors, stop this madness
		for(int i = 0; i < baconAgencyResources.size(); i++)
			pausetaBaconAgency.addCompleteBidToSAB(new CompleteBid(new Bid(baconAgencyResources.get(i), baconAgency)));

		for(int i = 0; i < baconAgencyResources.size(); i++)
			pausetaBeggyAgency.addCompleteBidToSAB(new CompleteBid(new Bid(baconAgencyResources.get(i), baconAgency)));
		
		for(int i = 0; i < beggyAgencyResources.size(); i++)
			pausetaBaconAgency.addCompleteBidToSAB(new CompleteBid(new Bid(beggyAgencyResources.get(i), beggyAgency)));

		for(int i = 0; i < beggyAgencyResources.size(); i++)
			pausetaBeggyAgency.addCompleteBidToSAB(new CompleteBid(new Bid(beggyAgencyResources.get(i), beggyAgency)));
		

		//Actually do something
		System.out.println("Bacon: ");
		System.out.println(pausetaBaconAgency.greedyPausetaBid(baconAgency, 6, requirements).toString());
		System.out.println("Beggy: ");
		System.out.println(pausetaBeggyAgency.greedyPausetaBid(beggyAgency, 6, requirements).toString());
		
		//pausetaBaconAgency.greedyPausetaBid(baconAgency, 1, requirements);
	}
}
