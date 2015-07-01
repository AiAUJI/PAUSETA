package needAGoodName;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import auxiliarStructures.Triple;
import enviroment.Intersection;
import enviroment.Map;

public class SCP {

	public static List<Double> permutationSCP(TMP tmp, List<Resource> resources, Map map){

		//To hold the minimum value for each type
		List<Double> distances = new ArrayList<Double>();
		List<String> types = new ArrayList<String>();
		
		for(String type: tmp.requirementsMap.requirements.keySet()){

			if(!types.contains(type)){

				types.add(type);
			}
		}
		
		//Get the locations for every type of resource
		HashMap<String, List<String>> locationsByType = new HashMap<String, List<String>>();

		for(Triple triple: tmp.requirements){

			if(!locationsByType.containsKey(triple.resourceType)){

				locationsByType.put(triple.resourceType, new ArrayList<String>());
			}

			for(int i=0; i<triple.quantity; i++){

				locationsByType.get(triple.resourceType).add(triple.intersectionID);
			}
		}
		
		//List of resources by type
		HashMap<String, List<Resource>> resourcesByType = new HashMap<String, List<Resource>>();
		
		for(Resource resource: resources){
			
			if(!resourcesByType.containsKey(resource.type)){
				
				resourcesByType.put(resource.type, new ArrayList<Resource>());
			}
			
			resourcesByType.get(resource.type).add(resource);
		}
		
		//Do the SCP
		for(String type: types){
						
			double localMin = Double.MAX_VALUE;
			
			List<Resource> localResources = resourcesByType.get(type);
			List<String> locations = locationsByType.get(type);
			
			for(int i=0; i<localResources.size(); i++){

				//For every initial state (That is cycling Patrols) find the minimum
				double res = SCP.getSCP(locations, localResources, 0, Double.MAX_VALUE, map);

				//Move the last element to the first position
				Resource aux = localResources.remove(localResources.size()-1);
				localResources.add(0, aux);

				if(res < localMin){

					localMin = res;
				}
			}
			
			distances.add(localMin);			
		}

		return distances;
	}

	public static double getSCP(List<String> locations, List<Resource> patrols, double actualMin, double minABS, Map map) {

		if (locations.size() == 1) { // Estamos en una hoja, se recorren todas sus opciones

			String ua = locations.get(0); // Sólo hay una ubicación en U

			double min = Double.MAX_VALUE;
			// Analizamos todas las opciones para esa ubicacion con las patrullas
			//    disponibles

			for(Resource p: patrols){

				HashMap<Intersection, Intersection> dijkstra = map.shortestPathsFrom(p.location.segment.origin.id);
				double distance = map.getDistance(dijkstra, ua);

				if (min > distance){ 
					min = distance;
				}
			}

			return actualMin + min; // Esta es la respuesta mínima para todas esas hojas
		} else {

			for(String u: locations){  // Hay que analizar todas las opciones

				Resource p = patrols.get(0); // De forma sencilla es coger la primera

				List<Resource> patrolsCopy = new ArrayList<Resource>(patrols);
				patrolsCopy.remove(0);

				List<String> locationsCopy = new ArrayList<String>(locations);
				locationsCopy.remove(u);

				//Distance
				HashMap<Intersection, Intersection> dijkstra = map.shortestPathsFrom(p.location.segment.origin.id);
				double distance = map.getDistance(dijkstra, u);

				// Ahora se analizan los siguientes caminos al descontar u y p
				double v = getSCP(locationsCopy, patrolsCopy, actualMin + distance, minABS, map);

				// Si un camino es mejor que el actual me quedo con el coste mejor
				if (v < minABS){

					minABS = v;
				}
			}
			return minABS; // Devuelvo el mejor coste encontrado en todo el arbol
		}
	}
}

