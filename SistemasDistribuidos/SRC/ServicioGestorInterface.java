/**
 * La Clase Gestor, se encarga de iniciar las tranferencias de ficheros y devolver lsitados e iniciar algunas operaciones con ficheros
 * 
 * @autor Buenaventura Salcedo Santos-Olmo, xpressmoviles@gmail.com
 * @version v1.20170610
 */
package nomed.common;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface ServicioGestorInterface extends Remote{
	/**
	 * sube un fichero al repositorio facilitando la url del cloperador, el nobmre y el id unico del cliente apra que se almacenen en la repo
	 * @param nombreFichero String el nombre del fichero
	 * @param idSesionCliente int el id sesion del cliente que quiere subir el fichero
	 * @return DatosIntercambio contiene la url del servicio cl oeprador, el id unico del cliente, el nombre del fichero y la sesion de la repo apra localizar el servicio
	 * @throws RemoteException
	 * @throws MalformedURLException
	 * @throws NotBoundException
	 */
	public DatosIntercambio subirFichero(String nombreFichero,int idSesionCliente) throws RemoteException, MalformedURLException, NotBoundException;
	
	/**
	 * permite la descarga de un fichero desde la repo a traves del sroperador y el discocliente
	 * @param URLdiscoCliente String la url del servicio discocliente
	 * @param idFichero int el id unico del fichero a descargar
	 * @param idSesionCliente int el id sesion del cliente para localizar el servicio discocliente
	 * @return String el nombre del fichero, null si no se puede bajar el fichero
	 * @throws RemoteException
	 * @throws MalformedURLException
	 * @throws NotBoundException
	 */
	public String bajarFichero(String URLdiscoCliente,int idFichero,int idSesionCliente) throws RemoteException, MalformedURLException, NotBoundException;
	
	/**
	 * solicita la lista de fichero de un cliente, compartido incluidos
	 * @param idSesionCliente int el id sesion del cliente que solicita la lista
	 * @return List<String> la lista de los ficheros con su id unico, el nombre y en caso de compatido se indica quien se lo compartio
	 * @throws RemoteException
	 * @throws MalformedURLException
	 * @throws NotBoundException
	 */
	public List<String> listarFicherosCliente(int idSesionCliente) throws RemoteException, MalformedURLException, NotBoundException;
	
	/**
	 * solicita la lista de los clientes registrado y cuales estan online
	 * @return String la lista de los cliente registrados y cuales estan autenticados (online) en formato simple de impresion
	 * @throws MalformedURLException
	 * @throws RemoteException
	 * @throws NotBoundException
	 */
	public String listarClientes() throws MalformedURLException, RemoteException, NotBoundException;
	
	/**
	 * solicita la comaprticion de un fichero
	 * @param idFichero el id unico del fichero a compatir
	 * @param nombreCliente el nombre del cliente a quien le vamos a compartir el fichero
	 * @param idSesion el id sesion de cliente que comparte el fichero, se comprueba si es suyo el fichero
	 * @return boolean true si todo va bien, false en caso contrario, por ejmplo si no fuera el fichero de cliente que quiere compartirlo
	 * @throws RemoteException
	 * @throws MalformedURLException
	 * @throws NotBoundException
	 */
	public boolean compartirFichero(int idFichero,String nombreCliente,int idSesion) throws RemoteException, MalformedURLException, NotBoundException;
	
	/**
	 * solicita el borrado de un fichero
	 * @param idFichero int el id unico del fichero a borrar, se comprueba
	 * @param idSesionCliente int el id sesion del cliente que quiere borrar le fichero, si no es suyo no lo podra borrar, si es un fichero compartido solo el propietario puede borrarlo
	 * @return DatosIntercambio facilita la url, la repo, el id unico de cliente y el nombre del fichero a borrar.
	 * @throws MalformedURLException
	 * @throws RemoteException
	 * @throws NotBoundException
	 */
	public DatosIntercambio borrarFichero(int idFichero,int idSesionCliente) throws MalformedURLException, RemoteException, NotBoundException;

}
