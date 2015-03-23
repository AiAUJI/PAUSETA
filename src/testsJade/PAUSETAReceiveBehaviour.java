package testsJade;


import needAGoodName.Agency;
import needAGoodName.CompleteBid;
import needAGoodName.Requirement;
import bid.Pauseta;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

public class PAUSETAReceiveBehaviour extends Behaviour{

	private static final long serialVersionUID = 6735239536403249351L;

	private Agent agent;
	private Agency agency;
	private int stage;
	private Pauseta pauseta;
	private Requirement requirements;
	private boolean done;

	public PAUSETAReceiveBehaviour(Agent agent, Agency agency, int stage, Pauseta pauseta, Requirement requirements){

		super();

		this.agent = agent;
		this.agency = agency;
		this.stage = stage;
		this.pauseta = pauseta;
		this.requirements = requirements;
		this.done = false;
	}

	@Override
	public void action() {
			
		//Receive other CompleteBids
		//ACLMessage cbToReceive = this.agent.receive();
		ACLMessage cbToReceive = this.agent.blockingReceive(500L);
		
		CompleteBid response = null;;

		if (cbToReceive != null){

			try {

				response = (CompleteBid) cbToReceive.getContentObject();
			} catch (UnreadableException e) {

				System.out.println("Error receiving the object.");
				e.printStackTrace();
			}

			if(response != null){
				
				System.out.println("I am " + this.agent.getLocalName() + " and " + cbToReceive.getSender().getLocalName() + " has sent me this: ");
				System.out.println(response.toString());
				
				//Add CompleteBid to the SAB
				this.pauseta.addCompleteBidToSAB(response);
								
				this.agent.addBehaviour(new PAUSETASendBehaviour(this.agent, this.agency, this.stage + 1, this.pauseta, this.requirements));
								
				this.done = true;
			}
		}else{ //I have not received anything

			System.out.println("So long and thank you for the fish!");
			this.done = true;
		}
	}

	@Override
	public boolean done() {

		return this.done;
	}

}
