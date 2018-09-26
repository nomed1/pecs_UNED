/**
 * Interface del Sercicio Cliente Operador, publica los metodos para subir y borrar ficheros
 * 
 * @autor Buenaventura Salcedo Santos-Olmo, xpressmoviles@gmail.com
 * @version v1.20170623
 */
package nomed.common;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServicioClOperadorInterface extends Remote{

	/**
	 * envia el fichero a la repo
	 * @param fichero Fichero el fichero con metadas en modo binario
	 * @return boolen true si hay exito, false en caso contrario
	 * @throws RemoteException
	 */
	public boolean subirFichero(Fichero fichero) throws RemoteException;
	
	/**
	 * borra el fichero de la repo
	 * @param fichero String con el nombre del fichero
	 * @param carpeta String con el nombre de la carpeta donde esta el fichero es el id unico del cliente
	 * @return boolean true si hay exito, false en caso contrario
	 * @throws RemoteException
	 */
	public boolean borrarFichero(String fichero, String carpeta) throws RemoteException;
	
}
