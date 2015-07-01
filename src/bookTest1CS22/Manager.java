package bookTest1CS22;

import java.io.IOException;

import enviroment.Map;
import agentExtension.AgentWithCounter;
import needAGoodName.TMP;
import jadeBehaviours.PAUSETAManagerBehaviour;

public class Manager extends AgentWithCounter {

	private static final long serialVersionUID = 7426586151076550534L;

	protected void setup(){
		
		this.numberOfMessages = 0;
		
		Map map = null;

		try {
			
			map = new Map("map/test1");
		} catch (IOException e) {
			
			System.out.println("Error reading the maps file.");
			e.printStackTrace();
		}
				
		TMP tmp = new TMP();

		//Incident in the CS-22		
		tmp.addResource("Ambulancia atencion medicalizada urgente", 2, "I-29");
		tmp.addResource("Ambulancia normal", 2, "I-29");
		tmp.addResource("Medico", 2, "I-29");
		tmp.addResource("Unidad de bomberos", 2, "I-29");
		tmp.addResource("Grua normal", 2, "I-29");		
		tmp.addResource("Unidad de policia", 1, "I-17");
		tmp.addResource("Unidad de policia", 2, "I-24");
		tmp.addResource("Unidad de policia", 1, "I-25");
		tmp.addResource("Unidad de policia", 1, "I-26");
		tmp.addResource("Unidad de policia", 1, "I-27");
		tmp.addResource("Unidad de policia", 1, "I-28");
		tmp.addResource("Unidad de policia", 1, "I-29");
		
		//Behaviour
		PAUSETAManagerBehaviour behaviour = new PAUSETAManagerBehaviour(this, tmp, map);
		
		this.addBehaviour(behaviour);
	}
}
