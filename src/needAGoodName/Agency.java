package needAGoodName;

import java.io.Serializable;
import java.util.ArrayList;

import enviroment.Location;
import enviroment.Segment;

/**
 * An Agency is a privat or public company that possesses several resources.
 */
public class Agency implements Serializable{
	
	//Id of the agency
	public String id;
	
	//Description of the agency
	public String description;
	
	//Type of the agency
	public String type;
	
	//Location of the agency
	public Location location;
	
	//Available resources in this agency
	public ArrayList<Resource> resources;
	
	/**
	 * Default constructor. 
	 */
	public Agency(){
		
		this.id = null;
		this.description = null;
		this.type = null;
		this.location = null;
		this.resources = new ArrayList<Resource>();
	}
	
	/**
	 * Constructor. 
	 *
	 * @param  id Unique id of the {@link Agency}. 
	 * @param  description A description of the {@link Agency}.
	 * @param  type The type of the {@link Agency}.
	 * @param  location The location of the {@link Agency}.
	 * @param  resources A list of {@link Resource} in the {@link Agency}. 
	 */
	public Agency(String id, String description, String type, Location location, ArrayList<Resource> resources){
		
		this.id = id;
		this.description = description;
		this.type = type;
		this.location = location;
		this.resources = resources;
	}
	
	/**
	 * Adds a {@link Resource} to this {@link Agency}.
	 * 
	 * @param resource {@link Resource} to be added.
	 * @return A boolean whether the {@link Resource} has been added or not.
	 */
	public boolean addResource(Resource resource){
		
		return this.resources.add(resource);
	}
	
	
}
