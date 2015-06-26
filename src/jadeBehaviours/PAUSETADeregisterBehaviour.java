package jadeBehaviours;

import java.io.IOException;

import needAGoodName.Agency;
import agentExtension.AgentWithCounter;
import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;

/**
 * Deregisters a bidder from the yellow pages.
 *
 */
public class PAUSETADeregisterBehaviour extends Behaviour{

	private static final long serialVersionUID = 6735239536403249351L;

	private AgentWithCounter agent;
	private Agency agency;

	public PAUSETADeregisterBehaviour(AgentWithCounter agent, Agency agency){

		super();

		this.agent = agent;
		this.agency = agency;
	}

	@Override
	public void action() {

		//Deregister from the yellow pages
		try{

			DFService.deregister(this.agent);
		}catch(FIPAException fe){

			fe.printStackTrace();
		}
		
		//Send the number of messages sent, to the manager
		//Prepare the message
		ACLMessage message = new ACLMessage(ACLMessage.INFORM);

		//The Ontology is used to choose the function.
		message.setOntology(Integer.toString(this.agent.numberOfMessages));

		//Sets the agent to send by name
		message.addReceiver(new AID( "Manager", AID.ISLOCALNAME));

		//Actually send it
		this.agent.send(message);
		
		//Send my list of Resources
		message = new ACLMessage(ACLMessage.INFORM);
		message.setOntology("Resources");
		message.addReceiver(new AID( "Manager", AID.ISLOCALNAME));
		
		try {

			message.setContentObject(agency.resources);
		} catch (IOException e) {

			System.out.println("Error trying to serialize the object.");
			e.printStackTrace();
		}
		
		//Actually send it
		this.agent.send(message);
		
		//Send my list of Resources
		message = new ACLMessage(ACLMessage.INFORM);
		message.setOntology("CB");
		message.addReceiver(new AID( "Manager", AID.ISLOCALNAME));
		
		try {

			message.setContentObject(this.agent.lastSentBid);
		} catch (IOException e) {

			System.out.println("Error trying to serialize the object.");
			e.printStackTrace();
		}
		
		//Actually send it
		this.agent.send(message);
	}

	@Override
	public boolean done() {

		return true;
	}

}
