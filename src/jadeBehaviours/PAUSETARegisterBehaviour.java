package jadeBehaviours;


import java.util.List;

import enviroment.Map;
import needAGoodName.Agency;
import needAGoodName.Bid;
import needAGoodName.TMP;
import needAGoodName.UtilitiesAndSynergies;
import agentExtension.AgentWithCounter;
import bid.Pauseta;
import jade.core.behaviours.Behaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;

/**
 * This behaviour registers a biddsder in the yellow pages, then
 * proceeds to the SendBehaviour.
 *
 */
public class PAUSETARegisterBehaviour extends Behaviour{

	private static final long serialVersionUID = 6735239536403249351L;

	private AgentWithCounter agent;
	private Agency agency;
	private int stage;
	private int round;
	private Pauseta pauseta;
	private TMP tmp;
	private Map map;
	private boolean done;

	public PAUSETARegisterBehaviour(AgentWithCounter agent, Agency agency, int stage, int round, Map map){

		super();

		this.agent = agent;
		this.agency = agency;
		this.stage = stage;
		this.round = round;
		this.done = false;
		this.map = map;

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
	}

	@Override
	public void action() {

		//Wait for the requirements
		MessageTemplate template = MessageTemplate.MatchOntology("TMP");
		ACLMessage receivedMessage = this.agent.blockingReceive(template);

		//I have received a message
		if(receivedMessage != null){

			try {

				this.tmp = (TMP) receivedMessage.getContentObject();
			} catch (UnreadableException e) {

				System.out.println("Error receiving the object.");
				e.printStackTrace();
			}
			
			//Calculate synergies and utilities
			List<Bid> synergyList = UtilitiesAndSynergies.setUtilitiesAndSynergies(this.agency, this.tmp, this.map);
			
			this.pauseta = new Pauseta(synergyList);
			
			//Add the Send Behaviour
			PAUSETASendBehaviour pausetaBe = new PAUSETASendBehaviour(this.agent, this.agency, this.pauseta, this.tmp.requirementsMap, this.stage, this.round);
			this.agent.addBehaviour(pausetaBe);
			
			this.done = true;
		} else {
			
			block();
		}
	}

	@Override
	public boolean done() {

		return this.done;
	}
}
