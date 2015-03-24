package testsJade;


import java.util.ArrayList;
import java.util.List;

import needAGoodName.Agency;
import needAGoodName.CompleteBid;
import needAGoodName.Requirement;
import bid.Pauseta;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;

public class PAUSETAReceiveBehaviour extends Behaviour{

	private static final long serialVersionUID = 6735239536403249351L;
	private static final long TIMEOUT = 500L;

	private Agent agent;
	private Agency agency;
	private int stage;
	private int round;
	private Pauseta pauseta;
	private Requirement requirements;
	private boolean done;
	private List<ACLMessage> messages;

	public PAUSETAReceiveBehaviour(Agent agent, Agency agency, Pauseta pauseta, Requirement requirements, int stage, int round){

		super();

		this.agent = agent;
		this.agency = agency;
		this.pauseta = pauseta;
		this.requirements = requirements;
		this.done = false;
		this.messages = new ArrayList<ACLMessage>();
		this.stage = stage;
		this.round = round;
	}

	@Override
	public void action() {

		//Receive other CompleteBids
		//ACLMessage cbToReceive = this.agent.receive();
		MessageTemplate template = MessageTemplate.MatchOntology(this.stage + "#" + this.round );
		
		ACLMessage cbToReceive = this.agent.blockingReceive(template, TIMEOUT);

		CompleteBid response = null;

		//I have received a message
		if(cbToReceive != null){

			this.messages.add(cbToReceive);
			
			//Keep trying to receive
			block(TIMEOUT);

		}else if(this.messages.size() > 0){ //TIMEOUT milliseconds have passed without messages, we assume no one is sending messages for this.stage and this.round
						
			//We now process all received messages (if any)
			for(ACLMessage msg: this.messages){
				
				try {

					response = (CompleteBid) msg.getContentObject();
				} catch (UnreadableException e) {

					System.out.println("Error receiving the object.");
					e.printStackTrace();
				}
				
				//This is only because we had to initialize response to something  
				if(response != null){

					System.out.println("I am " + this.agent.getLocalName() + " and " + msg.getSender().getLocalName() + " has sent me this: ");
					System.out.println(response.toString());
					
					this.pauseta.updatePreviousHighestValue(response.value);

					//Add CompleteBid to the SAB
					this.pauseta.addCompleteBidToSAB(response);
				}
			}
			
			//We are done processing, continue with the next round
			this.agent.addBehaviour(new PAUSETASendBehaviour(this.agent, this.agency, this.pauseta, this.requirements, this.stage, this.round + 1));
			
			System.out.println("Round " + this.round + " ended");

			this.done = true;
		}else{ //We have not received any message, this stage has ended
			
			System.out.println("Next stage");
			this.agent.addBehaviour(new PAUSETASendBehaviour(this.agent, this.agency, this.pauseta, this.requirements, this.stage + 1, 0));
		}
	}

	@Override
	public boolean done() {

		return this.done;
	}
}
