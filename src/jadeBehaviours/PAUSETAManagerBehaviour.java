package jadeBehaviours;

import java.io.IOException;

import needAGoodName.TMP;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;

public class PAUSETAManagerBehaviour extends Behaviour{

	private static final long serialVersionUID = -2826285439534419110L;

	private Agent agent;
	private TMP tmp;

	public PAUSETAManagerBehaviour(Agent agent, TMP tmp){

		super();

		this.agent = agent;
		this.tmp = tmp;
	}

	@Override
	public void action() {

		System.out.println("SLEEPING");

		try {
			Thread.sleep(15000);
		} catch (InterruptedException e1) {

			e1.printStackTrace();
		}

		System.out.println("DONE SLEEPING");

		//Prepare the message
		ACLMessage message = new ACLMessage(ACLMessage.INFORM);

		//The Ontology is used to choose the function.
		message.setOntology("TMP");
		
		//Set the object to send
		try {

			message.setContentObject(this.tmp);
		} catch (IOException e) {

			System.out.println("Error trying to serialize the object.");
			e.printStackTrace();
		}

		//Sets the agent to send
		//TODO: Search bidders
		message.addReceiver( new AID( "GUI", AID.ISLOCALNAME) );

		//Actually send it
		this.agent.send(message);
	}

	@Override
	public boolean done() {

		return true;
	}
}
