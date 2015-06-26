package bookTest1;

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

		//Incident in the CV-10
		tmp.addResource("Unidad de bomberos", 2, "I-02");
		tmp.addResource("Grua alto tonelaje", 1, "I-03");
		tmp.addResource("Grua normal", 2, "I-03");
		tmp.addResource("Unidad de mantenimiento", 2, "I-03");
		tmp.addResource("Ambulancia atencion urgente", 1, "I-03");
		tmp.addResource("Ambulancia de ayuda y evacuacion", 3, "I-03");
		tmp.addResource("Medico", 3, "I-03");
		tmp.addResource("Unidad de policia", 1, "I-02");
		tmp.addResource("Unidad de policia", 1, "I-03");
		tmp.addResource("Unidad de policia", 1, "I-04");
		tmp.addResource("Unidad de policia", 1, "I-13");
		tmp.addResource("Unidad de policia", 1, "I-12");
		tmp.addResource("Unidad de policia", 1, "I-11");
		
		//Behaviour
		PAUSETAManagerBehaviour behaviour = new PAUSETAManagerBehaviour(this, tmp, map);
		
		this.addBehaviour(behaviour);
	}
}
