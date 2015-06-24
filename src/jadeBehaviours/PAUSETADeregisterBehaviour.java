package jadeBehaviours;

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

	public PAUSETADeregisterBehaviour(AgentWithCounter agent){

		super();

		this.agent = agent;
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
		message.addReceiver( new AID( "Manager", AID.ISLOCALNAME) );

		//Actually send it
		this.agent.send(message);
	}

	@Override
	public boolean done() {

		return true;
	}

}
