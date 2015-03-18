package testsJade;

import java.io.IOException;

import needAGoodName.Agency;
import needAGoodName.CompleteBid;
import needAGoodName.Requirement;
import bid.Pauseta;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

public class PAUSETASendBehaviour extends Behaviour{

	private static final long serialVersionUID = 6735239536403249351L;

	private Agent agent;
	private Agency agency;
	private int stage;
	private Pauseta pauseta;
	private Requirement requirements;

	public PAUSETASendBehaviour(Agent agent, Agency agency, int stage, Pauseta pauseta, Requirement requirements){

		super();

		this.agent = agent;
		this.agency = agency;
		this.stage = stage;
		this.pauseta = pauseta;
		this.requirements = requirements;
	}

	@Override
	public void action() {
		
		System.out.println("I am " + this.agent.getLocalName() + "SENDSTAGE: " + this.stage);
		//Calculate the CompleteBid for the given stage
		CompleteBid cb = this.pauseta.greedyPausetaBid(this.agency, this.stage, this.requirements);

		//Send the CompleteBid to everyone interested
		//TODO: Find out who is interested.
		ACLMessage cbToSend = new ACLMessage(ACLMessage.INFORM);

		//Set the object to send
		try {

			cbToSend.setContentObject(cb);
		} catch (IOException e) {

			System.out.println("Error trying to serialize the object.");
			e.printStackTrace();
		}

		//Send to this people
		//TODO: FIX THIS
		if(this.agent.getLocalName().equals("Beggy"))
			cbToSend.addReceiver(new AID("Bacon", AID.ISLOCALNAME));
		else
			cbToSend.addReceiver(new AID("Beggy", AID.ISLOCALNAME));
		
		//Actually send it
		this.agent.send(cbToSend);
		
		this.agent.addBehaviour(new PAUSETAReceiveBehaviour(this.agent, this.agency, this.stage, this.pauseta, this.requirements));
	}

	@Override
	public boolean done() {

		return true;
	}

}
