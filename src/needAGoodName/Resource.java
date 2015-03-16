package needAGoodName;

import java.io.Serializable;
import java.util.UUID;

import enviroment.Location;

/**
 * A Resource is an entity that has its own characteristics and it is useful for an emergency.
 */
public class Resource implements Serializable{

	//id of the resource
	public UUID id;
	
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
		
		this.id = UUID.randomUUID();
		this.type = null;
		this.owner = new Agency();
		this.location = new Location();
		this.value = 0.0;
	}
	
	/**
	 * Constructor.
	 * 
	 * @param type The type of this {@link Resource}.
	 * @param owner The owner of this {@link Resource}.
	 * @param location The {@link Location} of this {@link Resource}.
	 * @param value The value of this {@link Resource}.
	 */
	public Resource(String type, Agency owner, Location location, double value){
		
		this.id = UUID.randomUUID();
		this.type = type;
		this.owner = owner;
		this.location = location;
		this.value = value;
	}
	
}
