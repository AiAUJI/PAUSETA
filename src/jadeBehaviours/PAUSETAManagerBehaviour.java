package jadeBehaviours;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
		
		System.out.println("Pauseta patrols");
		for(Bid bid: cb.bids){
			
			for(Resource resource: bid.resources){
				
				if(resource.type.equals("Unidad de policia")){
					
					pausetaPatrols.add(resource);
					System.out.println("ID: " + resource.id + " Position: " + resource.location.segment.origin.id);
				}
			}
		}

		//Evaluate SCP with all resources
		List<Resource> patrols = new ArrayList<Resource>();

		System.out.println("All patrols");
		for(Resource resource: resources){

			if(resource.type.equals("Unidad de policia")){

				patrols.add(resource);
				System.out.println("ID: " + resource.id + " Position: " + resource.location.segment.origin.id);
			}
		}
		
		Set<Resource> auxSet = new HashSet<Resource>();
		
		for(Resource patrullaPauseta: pausetaPatrols){
			
			auxSet.add(patrullaPauseta);
		}
		
		if(auxSet.size() != pausetaPatrols.size()){
			
			System.out.println("Hay repetidos");
		} else {
			
			System.out.println("No hay repetidos");
		}
		
		System.out.println("Locations: " + locations.size() + " PausetaPatrols: " + pausetaPatrols.size() + " allPatrols: " + patrols.size());
		
		//Write number of messages, SCP, PAUSETA, time
		double pausetaSCP = SCP.getSCP(locations, pausetaPatrols, 0, Double.MAX_VALUE, map);
		
		double startSCPTime = System.currentTimeMillis();
		
		//For every initial state (That is cycling Patrols) find the minimum
		
		
		double allSCP = SCP.getSCP(locations, patrols, 0, Double.MAX_VALUE, map);
		double stopSCPTime = System.currentTimeMillis();

		System.out.println("pausetaTime: " + (stopTime-startTime) + " SCP time: " + (stopSCPTime-startSCPTime) + " allSCP: " + allSCP + " pausetaSCP: " + pausetaSCP);
		
		if(pausetaPatrols.size() == 6){
			
			try(PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("test1.csv", true)))) {
				
			    out.println((stopTime-startTime) + ", " + (stopSCPTime-startSCPTime) + ", " + allSCP + ", " + pausetaSCP + ", " + this.agent.numberOfMessages);
			}catch (IOException e) {
				
			    System.out.println("Error logging");
			}
		}
	}

	@Override
	public boolean done() {

		return true;
	}
}
