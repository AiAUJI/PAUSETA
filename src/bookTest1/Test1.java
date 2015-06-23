package bookTest1;

import java.io.IOException;
import java.util.HashMap;

import enviroment.Intersection;
import enviroment.Map;

/**
 * Incidente en la CV-10
 *
 */
public class Test1 {

	public static void main(String[] args) {
		
		//Mapa con los hospitales, bomberos y gruas
		Map map = null;

		try {
			
			map = new Map("map/test1");
		} catch (IOException e) {
			
			System.out.println("Error al leer el fichero del mapa.");
			e.printStackTrace();
		}
		
		HashMap<Intersection, Intersection> police01 = map.shortestPathsFrom("I-09");

		System.out.println(map.getDistance(police01, "I-19"));
		
		

	}

}
