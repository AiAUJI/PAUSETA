package needAGoodName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Class that encapsules a Traffic Management Plan
 *
 */
public class TMP implements Serializable{

	private static final long serialVersionUID = -8381166099540747939L;
	
	/**
	 * Auxiliar class
	 *
	 */
	public class Triple{
		
		public String resource;
		public int quantity;
		public String intersectionID;
		
		Triple(String resource, int quantity, String intersectionID){
			
			this.resource = resource;
			this.quantity = quantity;
			this.intersectionID = intersectionID;
		}
	}
	
	public List<Triple> requirements;
	public Requirement requirementsMap;
	
	/**
	 * Default constructor
	 */
	public TMP(){
		
		this.requirements = new ArrayList<Triple>();
		this.requirementsMap = new Requirement();
	}
	
	/**
	 * Adds a resource to the requirements
	 * 
	 * @param name
	 * @param quantity
	 * @param intersectionID
	 */
	public void addResource(String name, int quantity, String intersectionID){
		
		this.requirements.add(new Triple(name, quantity, intersectionID));
		
		if(this.requirementsMap.requirements.containsKey(name)){
			
			this.requirementsMap.requirements.put(name, this.requirementsMap.requirements.get(name) + quantity);
		} else {
			
			this.requirementsMap.requirements.put(name, quantity);
		}
	}
}
