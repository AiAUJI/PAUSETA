package needAGoodName;

import java.io.Serializable;
import java.util.HashMap;

/**
 * A set of {@link Resource} that are needed for an emergency.
 */
public class Requirement implements Serializable{
	
	//A dictionary where the key is the type of the resource needed and 
	//the value is the quantity needed
	public HashMap<String, Integer> requirements;
	
	/**
	 * Default constructor.
	 */
	public Requirement(){
		
		this.requirements = new HashMap<String, Integer>();
	}
	
	/**
	 * Constructor.
	 * 
	 * @param requirements A Map of {@link Requirement}.
	 */
	public Requirement(HashMap<String, Integer> requirements){
		
		this.requirements = requirements;
	}

}
