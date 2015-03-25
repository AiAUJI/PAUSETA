package jadeBehaviours;

import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;


public class PAUSETADeregisterBehaviour extends Behaviour{

	private static final long serialVersionUID = 6735239536403249351L;

	private Agent agent;

	public PAUSETADeregisterBehaviour(Agent agent){

		super();

		this.agent = agent;
	}

	@Override
	public void action() {
		
		//Deregister from the yellow pages
		try{
			
			DFService.deregister(this.agent);
		}catch(FIPAException fe){
			
			fe.printStackTrace();
		}
	}

	@Override
	public boolean done() {

		return true;
	}

}
