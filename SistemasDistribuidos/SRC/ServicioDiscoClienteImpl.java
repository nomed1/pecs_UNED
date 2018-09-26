/**
 * Clase ServicioDiscoClienteImpl que implementa la interface ServicioDiscoClienteInterface
 * se encarga de recibir un fichero
 * 
 * @autor Buenaventura Salcedo Santos-Olmo, xpressmoviles@gmail.com
 * @version v1.20171007
 */
package nomed.cliente;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import nomed.common.Fichero;
import nomed.common.ServicioDiscoClienteInterface;

public class ServicioDiscoClienteImpl extends UnicastRemoteObject implements ServicioDiscoClienteInterface{

	private static final long serialVersionUID = 5636870803726135096L;

	/**
	 * Constructor por defecto
	 * @throws RemoteException
	 */
	protected ServicioDiscoClienteImpl() throws RemoteException {
		super();
	}

	/**
	 * recibe un fichero, el uso es el que explico el profesor en el foro con un ejemplo
	 * @param Fichero el fichero bieno con algunos metadatos
	 * @param int el id de la sesion de cliente para pegarlo al final del fichero para distintguirlo por si trabajamos en una sola carpeta
	 * @return boolean true si todo va bien, false en otro caso.
	 */
	@Override
	public boolean bajarFichero(Fichero fichero,int id) throws RemoteException {
		OutputStream os;
		String nombreFichero =fichero.obtenerNombre() + "." + id;//en el repositorio no hacia falta, asi sabemos realmente dequien es el fichero
		
		try {
			os = new FileOutputStream(nombreFichero); 
			if (fichero.escribirEn(os)==false)
			{
				os.close();
				return false;
			}
			os.close();
			System.out.println("Fichero " + nombreFichero +" recibido y guardado");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}	
		return true;
	}

}
