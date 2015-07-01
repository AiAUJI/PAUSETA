package bookTest1CS22;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import agentExtension.AgentWithCounter;
import needAGoodName.Agency;
import needAGoodName.Resource;
import enviroment.Location;
import enviroment.Map;
import jadeBehaviours.PAUSETARegisterBehaviour;

public class Policia1 extends AgentWithCounter {

	private static final long serialVersionUID = 8293492758491163351L;

	protected void setup(){
		
		this.numberOfMessages = 0;
				
		Map map = null;

		try {
			
			map = new Map("map/test1");
		} catch (IOException e) {
			
			System.out.println("Error reading the maps file.");
			e.printStackTrace();
		}
		
		Location location = new Location();
		location.segment = map.getIntersectionByID("I-07").out.get(0);
		location.position = 0;
				
		Agency agency = new Agency("Policia1", "Descripcion policia", "Policia", location, new ArrayList<Resource>());
		
		int numberOfCars = 5;
		
		//Add the resources
		ArrayList<Resource> resources = new ArrayList<Resource>();
		
		//Fixed resources
		resources.add(new Resource("Unidad de policia", agency, location, 0));
		
		//Random positions for each car
		for(int i=0; i<numberOfCars; i++){
			
			location = new Location();
			Random rand = new Random();
			
			String intersectionID = map.getRandomIntersection();
			
			location.segment = map.getIntersectionByID(intersectionID).out.get(0);
			
			int randomPos= rand.nextInt((int) (location.segment.length + 1));
			location.position = randomPos;
			
			resources.add(new Resource("Unidad de policia", agency, location, 0));
		}

		for(int i = 0; i < resources.size(); i++){
			
			agency.addResource(resources.get(i));
		}

		PAUSETARegisterBehaviour behaviour = new PAUSETARegisterBehaviour(this, agency, 1, 0, map);
		
		this.addBehaviour(behaviour);
	}
}