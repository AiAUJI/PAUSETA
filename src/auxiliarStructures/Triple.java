package auxiliarStructures;

import java.io.Serializable;

/**
 * Auxiliar class
 *
 */
public class Triple implements Serializable{
	
	private static final long serialVersionUID = -3760283759552267737L;
	
	public String resourceType;
	public int quantity;
	public String intersectionID;
	
	public Triple(String resourceType, int quantity, String intersectionID){
		
		this.resourceType = resourceType;
		this.quantity = quantity;
		this.intersectionID = intersectionID;
	}
}
