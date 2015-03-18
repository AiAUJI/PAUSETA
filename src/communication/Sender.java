package communication;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

public class Sender extends Agent{

	protected void setup(){

		CyclicBehaviour cb = new CyclicBehaviour(this){

			public void action(){

				ACLMessage msg = new ACLMessage(ACLMessage.INFORM);

				msg.addReceiver(new AID("Peter", AID.ISLOCALNAME));
				msg.setLanguage("English");
				msg.setOntology("Weather-Forecast-Ontology");
				msg.setContent("Today it’s raining");

				send(msg);

			}
		};
		
		addBehaviour(cb);
	}
}
