package jadeBehaviours;


import needAGoodName.Agency;
import needAGoodName.Requirement;
import bid.Pauseta;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

public class PAUSETARegisterBehaviour extends Behaviour{

	private static final long serialVersionUID = 6735239536403249351L;

	private Agent agent;
	private Agency agency;
	private int stage;
	private int round;
	private Pauseta pauseta;
	private Requirement requirements;

	public PAUSETARegisterBehaviour(Agent agent, Agency agency, Pauseta pauseta, Requirement requirements, int stage, int round){

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
		
		//Register this bidder in the "yellow pages"
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(agent.getAID());
		
		ServiceDescription sd = new ServiceDescription();
		sd.setType("Bidder");
		sd.setName(agent.getLocalName() + "-bidder");
		
		dfd.addServices(sd);
		
		try{
			
			DFService.register(agent, dfd);
		}catch (FIPAException fe){
			
			fe.printStackTrace();
		}
		
		//Add the Send Behaviour
		PAUSETASendBehaviour pausetaBe = new PAUSETASendBehaviour(this.agent, this.agency, this.pauseta, this.requirements, this.stage, this.round);
		this.agent.addBehaviour(pausetaBe);
	}

	@Override
	public boolean done() {

		return true;
	}

}
