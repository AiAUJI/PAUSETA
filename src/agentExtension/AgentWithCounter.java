package agentExtension;

import needAGoodName.CompleteBid;
import jade.core.Agent;

public class AgentWithCounter extends Agent{

	private static final long serialVersionUID = 7917033154751836206L;

	public int numberOfMessages = 0;
	public CompleteBid lastSentBid;
	
}
