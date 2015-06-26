package needAGoodName;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import enviroment.Intersection;
import enviroment.Map;

public class SCP {

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

