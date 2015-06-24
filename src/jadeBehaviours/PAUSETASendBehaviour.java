package jadeBehaviours;

import java.io.IOException;
import java.util.Vector;

import needAGoodName.Agency;
import needAGoodName.CompleteBid;
import needAGoodName.Requirement;
import agentExtension.AgentWithCounter;
import bid.Pauseta;
import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;

/**
 * This behaviour computes the PAUSETA algorithm for each round of each step,
 * then if the value is bigger than the previous maximum, it broadcasts the
 * CompleteBid. Then proceeds to the ReceiveBehaviour.
 *
 */
public class PAUSETASendBehaviour extends Behaviour{

	private static final long serialVersionUID = 6735239536403249351L;

	private AgentWithCounter agent;
	private Agency agency;
	private int stage;
	private int round;
	private Pauseta pauseta;
	private Requirement requirements;

	public PAUSETASendBehaviour(AgentWithCounter agent, Agency agency, Pauseta pauseta, Requirement requirements, int stage, int round){

		super();

		this.agent = agent;
		this.agency = agency;
		this.pauseta = pauseta;
		this.requirements = requirements;
		this.stage = stage;
		this.round = round;
	}

	@Override
	public void action() {

		//Calculate the CompleteBid for the given stage
		CompleteBid cb = this.pauseta.greedyPausetaBid(this.agency, this.stage, this.requirements);
		
		//Update the highest value
		boolean improved = this.pauseta.updatePreviousHighestValue(cb.getValue());
		
		//If my CompleteBid value is higher than the previous higher value
		if(improved){

			//Find who is interested, for that we use a template and the yellow pages
			Vector<AID> bidderAgents = new Vector<AID>();

			DFAgentDescription template = new DFAgentDescription();
			ServiceDescription sd = new ServiceDescription();
			
			//Filter agents
			sd.setType("Bidder");
				
			template.addServices(sd);

			//Ask the yellow pages
			try{

				DFAgentDescription[] result = DFService.search(this.agent, template);

				for(DFAgentDescription agent: result){

					bidderAgents.add(agent.getName());
				}
			}catch (FIPAException fe){

				fe.printStackTrace();
			}

			//Prepare the message
			ACLMessage cbToSend = new ACLMessage(ACLMessage.INFORM);
			
			//We need to keep track of the stage and the round
			cbToSend.setOntology(this.stage + "#" + this.round);

			//Set the object to send
			try {

				cbToSend.setContentObject(cb);
			} catch (IOException e) {

				System.out.println("Error trying to serialize the object.");
				e.printStackTrace();
			}
			
			//Add receiver agents
			for(AID aid: bidderAgents){
				
				//Check so I don't send it to myself
				if(!aid.getLocalName().equals(this.agent.getAID().getLocalName())){
					
					cbToSend.addReceiver(aid);
					this.agent.numberOfMessages++;
				}

			}
			
			//Actually send it
			this.agent.send(cbToSend);
		}
			
		this.agent.addBehaviour(new PAUSETAReceiveBehaviour(this.agent, this.agency, this.pauseta, this.requirements, this.stage, this.round));
	}

	@Override
	public boolean done() {

		return true;
	}
}
