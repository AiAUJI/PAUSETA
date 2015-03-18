package communication;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

public class Receiver extends Agent{
	
	private static final long serialVersionUID = 1452224978426378982L;

	protected void setup(){
		
		System.out.println("My body is ready.");
    	
        addBehaviour(new CyclicBehaviour(this){
        	
			private static final long serialVersionUID = -6651916270600124533L;

			public void action(){
            	 
                ACLMessage msg = receive();
                
                if (msg != null){
                	
                    System.out.println( " - " + myAgent.getLocalName() + " <- " + msg.getContent() );
                }
                
                block();
             }
        });
    }
}