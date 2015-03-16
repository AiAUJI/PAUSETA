package tests;

import java.util.ArrayList;
import java.util.HashMap;

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
//TODO: This is the worst code ever written, please use variables so you don't create the resources twice. Att. You from the past.
public class SingleBidderPAUSETATest {

	public static void main(String[] args) {
		
		//Agency
		Agency baconAgency = new Agency("0", "A bacon test Agency", "Bacon provider", new Location(), new ArrayList<Resource>());
		Agency beggyAgency = new Agency("1", "A beggy test Agency", "Vegetables provider", new Location(), new ArrayList<Resource>());
		
		//Some test resources
		baconAgency.addResource(new Resource("Regular bacon", baconAgency, new Location(), 120));
		baconAgency.addResource(new Resource("Regular bacon", baconAgency, new Location(), 110));
		baconAgency.addResource(new Resource("Crispy bacon", baconAgency, new Location(), 160));
		baconAgency.addResource(new Resource("Crispy bacon", baconAgency, new Location(), 140));
		baconAgency.addResource(new Resource("Unbelievable crispy bacon", baconAgency, new Location(), 260));
		baconAgency.addResource(new Resource("Unbelievable crispy bacon", baconAgency, new Location(), 240));

		beggyAgency.addResource(new Resource("Brocoli", beggyAgency, new Location(), 120));
		beggyAgency.addResource(new Resource("Brocoli", beggyAgency, new Location(), 110));
		beggyAgency.addResource(new Resource("Boiled eggplant", beggyAgency, new Location(), 160));
		beggyAgency.addResource(new Resource("Boiled eggplant", beggyAgency, new Location(), 140));
		beggyAgency.addResource(new Resource("Crispy bacon", beggyAgency, new Location(), 260));
		beggyAgency.addResource(new Resource("Crispy bacon", beggyAgency, new Location(), 240));

		
		//Requirements
		HashMap<String, Integer> requirementsMap = new HashMap<String, Integer>();
		requirementsMap.put("Crispy bacon", 3);
		requirementsMap.put("Brocoli", 1);
		requirementsMap.put("Boiled eggplant", 1);
		requirementsMap.put("Unbelievable crispy bacon", 2);
				
		Requirement requirements = new Requirement(requirementsMap);
		
		//Some random synergy
		ArrayList<Resource> random = new ArrayList<Resource>();
		random.add(new Resource("Regular bacon", baconAgency, new Location(), 120));
		random.add(new Resource("Unbelievable crispy bacon", baconAgency, new Location(), 260));
		
		CompleteBid cbBaconAgency = new CompleteBid();
		cbBaconAgency.addBid(new Bid(random, baconAgency, 450));
		ArrayList<CompleteBid> cbBaconAgencyArray = new ArrayList<CompleteBid>();
		cbBaconAgencyArray.add(cbBaconAgency);

		//One pauseta for every agency
		Pauseta pausetaBaconAgency = new Pauseta(cbBaconAgencyArray);
		Pauseta pausetaBeggyAgency = new Pauseta();
		
		//This should be do with Jade, but this is a test
		//TODO: Work in improving constructors, stop this madness
		pausetaBaconAgency.addCompleteBidToSAB(new CompleteBid(new Bid(new Resource("Regular bacon", baconAgency, new Location(), 120), baconAgency)));
		pausetaBaconAgency.addCompleteBidToSAB(new CompleteBid(new Bid(new Resource("Regular bacon", baconAgency, new Location(), 110), baconAgency)));
		pausetaBaconAgency.addCompleteBidToSAB(new CompleteBid(new Bid(new Resource("Crispy bacon", baconAgency, new Location(), 160), baconAgency)));
		pausetaBaconAgency.addCompleteBidToSAB(new CompleteBid(new Bid(new Resource("Crispy bacon", baconAgency, new Location(), 140), baconAgency)));
		pausetaBaconAgency.addCompleteBidToSAB(new CompleteBid(new Bid(new Resource("Unbelievable crispy bacon", baconAgency, new Location(), 260), baconAgency)));
		pausetaBaconAgency.addCompleteBidToSAB(new CompleteBid(new Bid(new Resource("Unbelievable crispy bacon", baconAgency, new Location(), 240), baconAgency)));
		pausetaBaconAgency.addCompleteBidToSAB(new CompleteBid(new Bid(new Resource("Brocoli", beggyAgency, new Location(), 120), beggyAgency)));
		pausetaBaconAgency.addCompleteBidToSAB(new CompleteBid(new Bid(new Resource("Brocoli", beggyAgency, new Location(), 110), beggyAgency)));
		pausetaBaconAgency.addCompleteBidToSAB(new CompleteBid(new Bid(new Resource("Boiled eggplant", beggyAgency, new Location(), 160), beggyAgency)));
		pausetaBaconAgency.addCompleteBidToSAB(new CompleteBid(new Bid(new Resource("Boiled eggplant", beggyAgency, new Location(), 140), beggyAgency)));
		pausetaBaconAgency.addCompleteBidToSAB(new CompleteBid(new Bid(new Resource("Crispy bacon", beggyAgency, new Location(), 260), beggyAgency)));
		pausetaBaconAgency.addCompleteBidToSAB(new CompleteBid(new Bid(new Resource("Crispy bacon", beggyAgency, new Location(), 240), beggyAgency)));
		
		pausetaBeggyAgency.addCompleteBidToSAB(new CompleteBid(new Bid(new Resource("Brocoli", beggyAgency, new Location(), 120), beggyAgency)));
		pausetaBeggyAgency.addCompleteBidToSAB(new CompleteBid(new Bid(new Resource("Brocoli", beggyAgency, new Location(), 110), beggyAgency)));
		pausetaBeggyAgency.addCompleteBidToSAB(new CompleteBid(new Bid(new Resource("Boiled eggplant", beggyAgency, new Location(), 160), beggyAgency)));
		pausetaBeggyAgency.addCompleteBidToSAB(new CompleteBid(new Bid(new Resource("Boiled eggplant", beggyAgency, new Location(), 140), beggyAgency)));
		pausetaBeggyAgency.addCompleteBidToSAB(new CompleteBid(new Bid(new Resource("Crispy bacon", beggyAgency, new Location(), 260), beggyAgency)));
		pausetaBeggyAgency.addCompleteBidToSAB(new CompleteBid(new Bid(new Resource("Crispy bacon", beggyAgency, new Location(), 240), beggyAgency)));
		pausetaBeggyAgency.addCompleteBidToSAB(new CompleteBid(new Bid(new Resource("Regular bacon", baconAgency, new Location(), 120), baconAgency)));
		pausetaBeggyAgency.addCompleteBidToSAB(new CompleteBid(new Bid(new Resource("Regular bacon", baconAgency, new Location(), 110), baconAgency)));
		pausetaBeggyAgency.addCompleteBidToSAB(new CompleteBid(new Bid(new Resource("Crispy bacon", baconAgency, new Location(), 160), baconAgency)));
		pausetaBeggyAgency.addCompleteBidToSAB(new CompleteBid(new Bid(new Resource("Crispy bacon", baconAgency, new Location(), 140), baconAgency)));
		pausetaBeggyAgency.addCompleteBidToSAB(new CompleteBid(new Bid(new Resource("Unbelievable crispy bacon", baconAgency, new Location(), 260), baconAgency)));
		pausetaBeggyAgency.addCompleteBidToSAB(new CompleteBid(new Bid(new Resource("Unbelievable crispy bacon", baconAgency, new Location(), 240), baconAgency)));
		
		//Actually do something
		//System.out.println("1\t " + pausetaBaconAgency.greedyPausetaBid(baconAgency, 1, requirements).toString());
		System.out.println("2\t " + pausetaBeggyAgency.greedyPausetaBid(beggyAgency, 1, requirements).toString());
	}
}
