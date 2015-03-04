package needAGoodName;

import java.io.Serializable;
import java.util.Dictionary;

/**
 * A set of {@link Resource} that are needed for an emergency.
 */
public class Requirement implements Serializable{
	
	//A dictionary where the key is the type of the resource needed and 
	//the value is the quantity needed
	public Dictionary<String, Integer> requirements;
	
	/**
	 * Default constructor.
	 */
	public Requirement(){
		
	}

}
