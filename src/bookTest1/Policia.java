package bookTest1;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import needAGoodName.Agency;
import needAGoodName.Resource;
import enviroment.Location;
import enviroment.Map;
import jade.core.Agent;
import jadeBehaviours.PAUSETARegisterBehaviour;

public class Policia extends Agent {

	private static final long serialVersionUID = 8293492758491163351L;

	protected void setup(){
		
		Map map = null;

		try {
			
			map = new Map("map/test1");
		} catch (IOException e) {
			
			System.out.println("Error reading the maps file.");
			e.printStackTrace();
		}
				
		Agency agency = new Agency("Policia", "Descripcion policia", "Policia", new Location(), new ArrayList<Resource>());
		
		int numberOfCars = 7;
		
		//Add the resources
		ArrayList<Resource> resources = new ArrayList<Resource>();
		
		//Random positions for each car
		for(int i=0; i<numberOfCars; i++){
			
			Location location = new Location();
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