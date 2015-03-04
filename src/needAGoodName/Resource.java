package needAGoodName;

import java.io.Serializable;

import enviroment.Location;

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
	
	/**
	 * Default constructor.
	 */
	public Resource(){
		
		this.id = null;
		this.type = null;
		this.owner = new Agency();
		this.location = new Location();
		this.value = 0.0;
	}
	
	/**
	 * Constructor.
	 * 
	 * @param id A unique id for this {@link Resource}.
	 * @param type The type of this {@link Resource}.
	 * @param owner The owner of this {@link Resource}.
	 * @param location The {@link Location} of this {@link Resource}.
	 * @param value The value of this {@link Resource}.
	 */
	public Resource(String id, String type, Agency owner, Location location, double value){
		
		this.id = id;
		this.type = type;
		this.owner = owner;
		this.location = location;
		this.value = value;
	}
	
}
