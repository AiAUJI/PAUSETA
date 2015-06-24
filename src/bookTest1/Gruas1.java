package bookTest1;

import java.io.IOException;
import java.util.ArrayList;

import needAGoodName.Agency;
import needAGoodName.Resource;
import enviroment.Location;
import enviroment.Map;
import jade.core.Agent;
import jadeBehaviours.PAUSETARegisterBehaviour;

public class Gruas1 extends Agent {

	private static final long serialVersionUID = 8293492758491163351L;

	protected void setup(){
		
		Map map = null;

		try {
			
			map = new Map("map/test1");
		} catch (IOException e) {
			
			System.out.println("Error reading the maps file.");
			e.printStackTrace();
		}
		
		Location location = new Location();
		location.segment = map.getIntersectionByID("Gruas 1").out.get(0);
		location.position = 0;
		
		Agency agency = new Agency("Gruas 1", "Descripcion gruas", "Gruas", location, new ArrayList<Resource>());
		
		//Add the resources
		ArrayList<Resource> resources = new ArrayList<Resource>();
		
		resources.add(new Resource("Grua alto tonelaje", agency, location, 0));
		resources.add(new Resource("Grua normal", agency, location, 0));

		for(int i = 0; i < resources.size(); i++){
			
			agency.addResource(resources.get(i));
		}

		PAUSETARegisterBehaviour behaviour = new PAUSETARegisterBehaviour(this, agency, 1, 0, map);
		
		this.addBehaviour(behaviour);
	}
}