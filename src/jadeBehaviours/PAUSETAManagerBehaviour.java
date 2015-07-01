package jadeBehaviours;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

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
			Thread.sleep(5000);
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

		System.out.println("Calculating");

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

		//Write number of messages, SCP, PAUSETA, time	

		//All resources SCP
		double startSCPTime = System.currentTimeMillis();

		List<Double> distancesAll = SCP.permutationSCP(tmp, resources, map);

		double stopSCPTime = System.currentTimeMillis();
			
		//Evaluate SCP with PAUSETA solution
		List<Resource> pausetaResources = new ArrayList<Resource>();

		for(Bid bid: cb.bids){

			for(Resource resource: bid.resources){

				pausetaResources.add(resource);
				
			}
		}
		
		List<Double> distancesPAUSETA = SCP.permutationSCP(tmp, pausetaResources, map);
		
		//Get the sum of the distances
		double minAllSCP = 0;
		
		for(Double distance: distancesAll){
			 minAllSCP += distance;
		}
		
		double minPausetaSCP = 0;
		
		for(Double distance: distancesPAUSETA){
			minPausetaSCP += distance;
		}

		System.out.println("pausetaTime: " + (stopTime-startTime) + " SCP time: " + (stopSCPTime-startSCPTime) + " allSCP: " + minAllSCP + " pausetaSCP: " + minPausetaSCP);

		try(PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("test1.csv", true)))) {

			out.println((stopTime-startTime) + ", " + (stopSCPTime-startSCPTime) + ", " + minAllSCP + ", " + minPausetaSCP + ", " + this.agent.numberOfMessages);
		}catch (IOException e) {

			System.out.println("Error logging");
		}

	}

	@Override
	public boolean done() {

		return true;
	}
}
