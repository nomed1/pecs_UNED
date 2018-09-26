/**
 * Interface del Sercicio Servidor Operador
 * 
 * @autor Buenaventura Salcedo Santos-Olmo, xpressmoviles@gmail.com
 * @version v1.20170623
 */
package nomed.common;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface ServicioSrOperadorInterface extends Remote {
	
	/**
	 * crea la carpeta del cliente usando el id unico del cliente
	 * tambien actuliza la lista logica de las carpetas de esa repo
	 * @param idCliente int el id unico del cliente
	 * @return boolean, true si se ha podido crear la carpeta, false en caso contrario
	 * @throws RemoteException
	 */
	public boolean crearCarpetaRepositorio(int idCliente) throws RemoteException;
	
	/**
	 * envia un fichero al discocliente
	 * @param URLdiscoCliente String al url del servicio disco cliente
	 * @param nombreFichero String el nombre del fichero a bajar
	 * @param idCliente int el id del cliente
	 * @throws MalformedURLException
	 * @throws RemoteException
	 * @throws NotBoundException
	 */
	public void bajarFichero(String URLdiscoCliente,String nombreFichero,int idCliente) throws MalformedURLException, RemoteException, NotBoundException;


}
