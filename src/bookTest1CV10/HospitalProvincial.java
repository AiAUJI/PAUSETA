package bookTest1CV10;

import java.io.IOException;
import java.util.ArrayList;

import agentExtension.AgentWithCounter;
import needAGoodName.Agency;
import needAGoodName.Resource;
import enviroment.Location;
import enviroment.Map;
import jadeBehaviours.PAUSETARegisterBehaviour;

public class HospitalProvincial extends AgentWithCounter {

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
		location.segment = map.getIntersectionByID("Hospital provincial").out.get(0);
		location.position = 0;
		
		Agency agency = new Agency("Hospital provincial", "Descripcion hospital", "Hospital", location, new ArrayList<Resource>());
		
		//Add the resources
		ArrayList<Resource> resources = new ArrayList<Resource>();
		
		resources.add(new Resource("Ambulancia atencion medicalizada urgente", agency, location, 0));
		resources.add(new Resource("Ambulancia atencion medicalizada urgente", agency, location, 0));
		resources.add(new Resource("Ambulancia normal", agency, location, 0));
		resources.add(new Resource("Ambulancia normal", agency, location, 0));
		resources.add(new Resource("Ambulancia normal", agency, location, 0));
		resources.add(new Resource("Medico", agency, location, 0));
		resources.add(new Resource("Medico", agency, location, 0));
		resources.add(new Resource("Medico", agency, location, 0));

		for(int i = 0; i < resources.size(); i++){
			
			agency.addResource(resources.get(i));
		}

		PAUSETARegisterBehaviour behaviour = new PAUSETARegisterBehaviour(this, agency, 1, 0, map);
		
		this.addBehaviour(behaviour);
	}
}
