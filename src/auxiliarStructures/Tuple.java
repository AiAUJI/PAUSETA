package auxiliarStructures;

import java.io.Serializable;

import needAGoodName.Resource;

/**
 * Auxiliar class
 *
 */
public class Tuple implements Serializable{
		
	private static final long serialVersionUID = -7608658992906365944L;
	
	public Resource resource;
	public double distance;
	
	public Tuple(Resource resource, double distance){
		
		this.resource = resource;
		this.distance = distance;
	}
}