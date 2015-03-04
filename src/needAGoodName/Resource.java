package needAGoodName;

import java.io.Serializable;

/**
 * A Resource is an entity that has its own characteristics and it is useful for an emergency.
 */
public class Resource implements Serializable{

	//id of the resource
	public String id;
	
	//Type of the resource
	public String type;
	
	//Agency this resource belongs to
	public Agency owner;
	
	//Current location of the resource
	public Location location;
	
	//Value of the resource
	public double value;
	
}
