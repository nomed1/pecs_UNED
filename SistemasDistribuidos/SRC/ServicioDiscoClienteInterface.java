/**
 * Interface del Sercicio Disco Cliente
 * 
 * @autor Buenaventura Salcedo Santos-Olmo, xpressmoviles@gmail.com
 * @version v1.20170623
 */
package nomed.common;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServicioDiscoClienteInterface extends Remote {
	
	/**
	 * recibe fichero de al repo al discocliente
	 * @param fichero Fichero el fichero a enviar
	 * @param id int el idsesion del cleinte aquien enviamos el fichero para localizar el servicio discocliente
	 * @return boolean true si todo va bien, false en caso contrario
	 * @throws RemoteException
	 */
	public boolean bajarFichero(Fichero fichero,int id) throws RemoteException;

}
