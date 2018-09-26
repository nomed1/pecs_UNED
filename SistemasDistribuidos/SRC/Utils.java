/**
 * Clase Util, setea el CodeBase, usada en el video de fermin, en principio funciona ok
 * 
 * @autor Buenaventura Salcedo Santos-Olmo, xpressmoviles@gmail.com
 * @version v1.20171006
 */
package nomed.common;

public class Utils {
	
	public static final String CODEBASE = "java.rmi.server.codebase";
	
	// Class<?> no esta parametrizada, cualquier clase pediremos su ruta
	public static void setCodeBase(Class<?> c) {
		
		//Calculara la ruta donde este cargada la clase
		//A la clase donde esta el codigo fuente, dame la ubicacion y pasala a string
		String ruta = c.getProtectionDomain().getCodeSource().getLocation().toString();
		
		//si seteamos el codebase en otra ubicacion, antes de setearlo, mejor
		//comprobar si ya esta puesta, asi evitamos problemas si pedimos mas veces el codebase
		//esto es para no tener que lanzar desde el shell
		String path = System.getProperty(CODEBASE); //si se seteo contrendra ya algo
		
		if (path != null && !path.isEmpty()) {
			ruta = path + " " + ruta;
		}
		
		System.setProperty(CODEBASE,ruta);
	}
}