package enviroment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.Set;

import org.json.JSONObject;

/**
 * Class that holds the representation of a map.
 *
 */
public class Map {

	public Intersection start;
	public Integer intersectionCount;
	public Integer segmentCount;
	private List<Intersection> intersections;

	/**
	 * Default constructor.
	 * 
	 * @throws IOException
	 */
	public Map() throws IOException{

		this("map/base");
	}

	/**
	 * Constructor that builds a Map from a folder.
	 * 
	 * @param folder Folder where the files are stored.
	 */
	public Map(String folder) throws IOException{

		this.intersectionCount = 0;
		this.segmentCount = 0;

		//Get all files from the given folder
		String url = Map.class.getClassLoader().getResource(folder).toString().split(":")[1];

		File[] files = new File(url).listFiles();

		//Check correct files
		BufferedReader intersectionsReader = null, segmentsReader = null;

		for(int i=0; i<files.length; i++){

			if(files[i].getName().equals("intersections")){

				intersectionsReader = new BufferedReader(new FileReader(files[i].getAbsolutePath()));

			}else if(files[i].getName().equals("segments")){

				segmentsReader = new BufferedReader(new FileReader(files[i].getAbsolutePath()));
			}
		}

		if(segmentsReader == null || intersectionsReader == null){

			throw new IOException("Couldn't find the files.");
		}else{

			try{

				HashMap<String, Intersection> auxiliarMap = new HashMap<String, Intersection>();
				
				String line = intersectionsReader.readLine();

				//Auxiliar structure
				this.intersections = new ArrayList<Intersection>();

				//Read  all the Intersections
				while(line != null){

					JSONObject inter = new JSONObject(line);
					
					Intersection intersection = new Intersection(inter.getString("id"), inter.getJSONObject("coordinates").getDouble("latitude"), inter.getJSONObject("coordinates").getDouble("longitude"));
					
					this.intersections.add(intersection);
					auxiliarMap.put(inter.getString("id"), intersection);

					line = intersectionsReader.readLine();
					this.intersectionCount++;
				}

				line = segmentsReader.readLine();

				//Read all the segments				
				while(line != null){

					JSONObject seg = new JSONObject(line);

					Intersection origin = null;
					Intersection destination = null;

					//Origin
					if(!seg.getString("origin").equals("null")){

						origin = auxiliarMap.get(seg.getString("origin"));
					}

					//Destination
					if(!seg.getString("destination").equals("null")){

						destination = auxiliarMap.get(seg.getString("destination"));
					}

					//Populate the map
					Segment segment = new Segment(seg.getString("id"), origin, destination, seg.getDouble("length"));

					if(origin != null){
						origin.out.add(segment);
					}

					if(destination != null){
						destination.in.add(segment);
					}

					line = segmentsReader.readLine();
					this.segmentCount++;
				}

				this.start = this.intersections.get(0);

			}catch(Exception e){

				e.printStackTrace();
			}finally{

				intersectionsReader.close();
				segmentsReader.close();
			}
		}
	}

	/**
	 * Dijkstra https://en.wikipedia.org/?title=Dijkstra%27s_algorithm#Pseudocode.
	 * 
	 * @param origin
	 * @param destination
	 * @return
	 */
	public HashMap<Intersection, Intersection> shortestPathsFrom(String originID){

		HashMap<Intersection, Double> dist = new HashMap<Intersection, Double>();
		HashMap<Intersection, Intersection> prev = new HashMap<Intersection, Intersection>();
		Queue<Intersection> q = new LinkedList<Intersection>();

		Intersection origin = this.getIntersectionByID(originID);

		dist.put(origin, 0.0);
		prev.put(origin, null);

		for(Intersection intersection: this.intersections){

			if(!intersection.equals(origin)){

				dist.put(intersection, Double.MAX_VALUE);
				prev.put(intersection, null);
			}

			q.add(intersection);
		}

		while(!q.isEmpty()){

			Intersection u = minDistance(dist, q);
						
			q.remove(u);

			for(Intersection v: getNeighbours(u)){ //Each neighbour

				if(v == null){
					
					break;
				}
				
				double alt = dist.get(u) + getLenghtToNeighbour(u, v);

				if(alt < dist.get(v)){

					dist.put(v, alt);
					prev.put(v, u);
				}
			}
		}

		return prev;	
	}


	/**
	 * Given the Dijkstra result, this returns the distance to a destination.
	 * 
	 * @param prev
	 * @param destination
	 * @return
	 */
	public double getDistance(HashMap<Intersection, Intersection> prev, String destination){
		
		return this.getDistance(prev, this.getIntersectionByID(destination));
	}

	/**
	 * Same as above using Intersections instead of ID.
	 * 
	 * @param prev
	 * @param destination
	 * @return
	 */
	public double getDistance(HashMap<Intersection, Intersection> prev, Intersection destination){

		//Get the path
		double ret = 0;

		Intersection u = destination;

		while(prev.get(u) != null){
			
			ret += this.getLenghtToNeighbour(u, prev.get(u));
			u = prev.get(u);
		}
		
		return ret;
	}

	private Intersection minDistance(HashMap<Intersection, Double> dist, Queue<Intersection> queue){

		double min = Double.MAX_VALUE;
		Intersection ret = queue.peek();

		Set<Intersection> keys = dist.keySet();

		for(Intersection intersection: keys){

			if(queue.contains(intersection) && dist.get(intersection) < min){

				min = dist.get(intersection);
				ret = intersection;
			}
		}

		return ret;
	}

	private List<Intersection> getNeighbours(Intersection intersection){

		List<Intersection> ret = new ArrayList<Intersection>();
				
		for(Segment segment: intersection.out){

			ret.add(segment.destination);
		}

		return ret;
	}

	private double getLenghtToNeighbour(Intersection origin, Intersection destination){

		double ret = Double.MAX_VALUE;

		for(Segment segment: origin.out){

			if(segment.destination.equals(destination)){

				ret = segment.length;
				break;
			}
		}

		return ret;
	}

	/**
	 * Given the id of an intersection, it returns that intersection
	 * 
	 * @param id
	 * @return
	 */
	public Intersection getIntersectionByID(String id){

		Intersection ret = null;

		for(Intersection intersection: this.intersections){

			if(intersection.id.equals(id)){

				ret = intersection;
				break;
			}
		}

		return ret;
	}
	
	/**
	 * Returns a random valid intersection id
	 * 
	 * @return
	 */
	public String getRandomIntersection(){
		
		Random rand = new Random();
		int randomNum = rand.nextInt(this.intersectionCount);
		
		return this.intersections.get(randomNum).id;
	}
}
