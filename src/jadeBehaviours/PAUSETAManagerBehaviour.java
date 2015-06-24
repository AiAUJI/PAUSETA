package jadeBehaviours;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import agentExtension.AgentWithCounter;
import needAGoodName.TMP;
import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;

public class PAUSETAManagerBehaviour extends Behaviour{

	private static final long serialVersionUID = -2826285439534419110L;

	private AgentWithCounter agent;
	private TMP tmp;
	long startTime, stopTime;

	public PAUSETAManagerBehaviour(AgentWithCounter agent, TMP tmp){

		super();

		this.agent = agent;
		this.tmp = tmp;
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
		
		//Gather the total number of messages sent
		List<ACLMessage> messages = new ArrayList<ACLMessage>();
		
		ACLMessage receivedMessage = this.agent.blockingReceive(15000L);

		//I have received a message
		if(receivedMessage != null){
			
			stopTime = System.currentTimeMillis();
			messages.add(receivedMessage);

			//Keep trying to receive
			block(500L);		
		} else if(messages.size() > 0) {
			
			for(ACLMessage msg: messages){
				
				this.agent.numberOfMessages += Integer.parseInt(msg.getOntology());
			}
		}
		
		//Do SCP
		
		//Write number of messages, SCP, PAUSETA, time
		
	}

	@Override
	public boolean done() {

		return true;
	}
}
