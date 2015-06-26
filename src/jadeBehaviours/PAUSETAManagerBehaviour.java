package jadeBehaviours;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import bid.Pauseta;
import enviroment.Map;
import agentExtension.AgentWithCounter;
import auxiliarStructures.Triple;
import needAGoodName.Bid;
import needAGoodName.CompleteBid;
import needAGoodName.Resource;
import needAGoodName.SCP;
import needAGoodName.TMP;
import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

public class PAUSETAManagerBehaviour extends Behaviour{

	private static final long serialVersionUID = -2826285439534419110L;

	private AgentWithCounter agent;
	private TMP tmp;
	private Map map;
	long startTime, stopTime = -1;

	public PAUSETAManagerBehaviour(AgentWithCounter agent, TMP tmp, Map map){

		super();

		this.agent = agent;
		this.tmp = tmp;
		this.map = map;
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

		//Sets the agents to send
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

		//Add receiver agents
		for(AID aid: bidderAgents){

			//Check so I don't send it to myself
			if(!aid.getLocalName().equals(this.agent.getAID().getLocalName()))
				message.addReceiver(aid);
		}

		//Actually send it
		this.agent.send(message);

		//Measure time
		startTime = System.currentTimeMillis();

		//Gather the total number of messages sent and the resources used
		List<ACLMessage> messages = new ArrayList<ACLMessage>();
		List<Resource> resources = new ArrayList<Resource>();
		CompleteBid cb = new CompleteBid();

		ACLMessage receivedMessage = this.agent.blockingReceive(15000L);

		System.out.println("RECEIVING");

		//I have received a message
		while(receivedMessage != null){

			if(stopTime == -1){

				stopTime = System.currentTimeMillis();
			}

			messages.add(receivedMessage);

			//Keep trying to receive
			receivedMessage = this.agent.blockingReceive(15000L);
		}

		if(messages.size() > 0) { //We have received everything

			for(ACLMessage msg: messages){

				if(msg.getOntology().equals("Resources")){

					try {

						@SuppressWarnings("unchecked")
						List<Resource> aux = (List<Resource>) msg.getContentObject();

						resources.addAll(aux);

					} catch (UnreadableException e) {

						System.out.println("Error getting the resources.");
						e.printStackTrace();
					}
					
				
				} else if(msg.getOntology().equals("CB")){
					
					try {

						CompleteBid aux = (CompleteBid) msg.getContentObject();
						
						if(aux.getValue() > cb.getValue()){
							
							cb = aux;
						}	

					} catch (UnreadableException e) {

						System.out.println("Error getting the resources.");
						e.printStackTrace();
					}
					
				} else {

					this.agent.numberOfMessages += Integer.parseInt(msg.getOntology());
				}
			}
		}
		
		//Locations (Needed for SCP)
		List<String> locations = new ArrayList<String>();

		for(Triple triple: tmp.requirements){

			if(triple.resourceType.equals("Unidad de policia")){
				for(int i=0; i<triple.quantity; i++){

					locations.add(triple.intersectionID);
				}
			}
		}
		
		//Evaluate SCP with PAUSETA solution
		List<Resource> pausetaPatrols = new ArrayList<Resource>();
		
		for(Bid bid: cb.bids){
			
			for(Resource resource: bid.resources){
				
				if(resource.type.equals("Unidad de policia")){
					
					pausetaPatrols.add(resource);
				}
			}
		}

		//Evaluate SCP with all resources
		List<Resource> patrols = new ArrayList<Resource>();

		for(Resource resource: resources){

			if(resource.type.equals("Unidad de policia")){

				patrols.add(resource);
			}
		}

		//Write number of messages, SCP, PAUSETA, time
		double pausetaSCP = SCP.getSCP(locations, pausetaPatrols, 0, Double.MAX_VALUE, map);
		double allSCP = SCP.getSCP(locations, patrols, 0, Double.MAX_VALUE, map);

		System.out.println("allSCP: " + allSCP + " pausetaSCP: " + pausetaSCP);
	}

	@Override
	public boolean done() {

		return true;
	}
}
