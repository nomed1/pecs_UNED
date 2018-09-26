/**
 * Esta interface permite realizar una abstraccion del almacenamiento de los datos, publicando los metodos
 * lo que nos permitira usar un motor de base de datos o bien mas sencillamente
 * en esta practica las estructuras List y HashMap de java
 * 
 * @autor Buenaventura Salcedo Santos-Olmo, xpressmoviles@gmail.com
 * @version v1.20170227
 */
package nomed.common;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;


public interface ServicioDatosInterface extends Remote{
	
	/**
	 * autentica a un cliente
	 * @param nombre String el nombre del cliente
	 * @param id int el id sesion del cliente
	 * @return int -2 si el cliente ya esta registrado, -1 si su repo no esta online, 0 si ya esta autentica, id sesion del cliente en caso contrario
	 * @throws RemoteException
	 */
	public int autenticarCliente(String nombre,int id) throws RemoteException;
	
	/**
	 * registra a un cliente
	 * @param nombre String el nombre del cliente
	 * @param id int el id unico del cliente a registrar
	 * @return int -1 si no hay repos online, 0 si ya esta registrado, id unico en otro caso
	 * @throws RemoteException
	 * @throws MalformedURLException
	 * @throws NotBoundException
	 */
	public int registrarCliente(String nombre,int id) throws RemoteException, MalformedURLException, NotBoundException;
	
	/**
	 * desconecta a un cliente, borra la sesion actual del cliente
	 * @param sesion int el id sesion del cliente a cerrar
	 * @return String el nombre del cliente de la sesion que se ha cerrado
	 * @throws RemoteException
	 */
	public String desconectarCliente(int sesion) throws RemoteException;
	
	/**
	 * autentica una repo
	 * @param nombre String el nombre del repositorio
	 * @param id int id sesion de la repo
	 * @return -1 la repo no esta registrada, 0 la repo ya esta autenticada, id sesion de la repo en otro caso
	 * @throws RemoteException
	 */
	public int autenticarRepositorio(String nombre,int id) throws RemoteException;
	
	/**
	 * registra una repo
	 * @param nombre String el nombre de la repo
	 * @param id int el id unico de la repo a registrar
	 * @return int 0 si la repo ya esta registrada, id unico de la repo en otro caso
	 * @throws RemoteException
	 */
	public int registrarRepositorio(String nombre,int id) throws RemoteException;
	
	/**
	 * desconecta la repo, borra la sesion actual de la repo
	 * @param sesion int el id seseion de la repo a cerrar
	 * @return String el nombre de la repo de la sesion que se ha cerrado
	 * @throws RemoteException
	 */
	public String desconectarRepositorio(int sesion) throws RemoteException;
	
	/**
	 * devuvle la lista de cliente , todos los registrado indicando cual de ellos esta online y cual offline
	 * @return String la lista de cliente registrado indicando cual esta online u offline
	 * @throws RemoteException
	 */
	public String listaClientes() throws RemoteException;
	
	/**
	 * devuelve la lsita de repos, todas las registradas indicando cual de ellas esta online y cual offline
	 * @return String la lista de repos registradas indicando cual esta online u offline
	 * @throws RemoteException
	 */
	public String listaRepositorios() throws RemoteException;
	
	/** 
	 * devuelve la lista de los emparejamientos cliente y repositorio
	 * @return String la lista para imprimir en formato simple de los emparejamiento cliente y repositorio
	 * @throws RemoteException
	 */
	public String listaClientesRepositorios() throws RemoteException;
	
	/**
	 * devulve el repositorio al que pertenece el cliente
	 * @param idCliente int el id unico del cliente
	 * @return int el id unico de la repo a la que pertenece
	 * @throws RemoteException
	 */
	public int dimeRepositorio(int idCliente) throws RemoteException;
	
	/**
	 * devuelve el id unico a partir del id sesion del cliente
	 * @param idsesion itn el id sesion del cliente
	 * @return int id unico del cliente
	 * @throws RemoteException
	 */
	public int sesion2id(int idsesion) throws RemoteException;
	
	/**
	 * almacena un fichero en las tablas
	 * genera un metadato a partir del nombre de fichero y del id sesion de cliente, debe consultar la repo y el id uncio del fichero
	 * @param nombreFichero String el nombre del fichero
	 * @param idSesionCliente int el id sesion del cliente propietario del fichero
	 * @return int el id unico del cliente
	 * @throws RemoteException
	 */
	public int almacenarFichero(String nombreFichero,int idSesionCliente) throws RemoteException;
	
	/**
	 * elimina de la tablas un fichero
	 * @param idFichero int el id unico del fichero
	 * @param idSesionCliente int el id sesion del cliente para averiguar si el fichero es suyo o no, si es compartido no puede borrar salvo el propietario
	 * @return int -1 o 0 si hay error, el id unico del cliente en otro caso
	 * @throws RemoteException
	 */
	public int eliminarFichero(int idFichero,int idSesionCliente) throws RemoteException;
	
	/**
	 * autoriza la descarga de un fichero facilitando sus metadatos
	 * @param idFichero int el id unico del fichero a descargar
	 * @param idSesionCliente int el id sesion del cliente que quiere descargar el fichero, hay que revisar si es un fichero compartido
	 * @return Metadatos los metadatos del fichero a descargar, null en caso contrario
	 * @throws RemoteException
	 */
	public Metadatos descargarFichero(int idFichero,int idSesionCliente) throws RemoteException;
	
	/**
	 * la lista de fichero de un cliente
	 * @param idSesionCliente el id sesion del cliente, averiguamos a traves de este dato el id y el nombre del cliente
	 * @return List<String> lista formateada con el id unico del ficehro el fichero y si el fichero es compartido por alguien
	 * @throws RemoteException
	 */
	public List<String> listarFicherosCliente(int idSesionCliente) throws RemoteException;
	
	/**
	 * comaprtir un fichero con un cliente
	 * @param idFichero int el id del fichero a comaprtir
	 * @param nombreCliente String nombre del cliente con queire queremos compartir fichero, case-sentive
	 * @param idSesion int el id sesion del cliente que quiere compartir el fichero, se comprueba sie s suyo el fichero
	 * @return boolean true si todo va bien, false en caso cotnrario, proe ejmplo si no es suyo o el fichero no existe
	 * @throws RemoteException
	 */
	public boolean compartirFichero(int idFichero, String nombreCliente, int idSesion) throws RemoteException;
	
	/**
	 * devuelve los metadatos de un fichero
	 * @param idFichero el id unico del fichero
	 * @return Metadatos los metadatos del fichero
	 * @throws RemoteException
	 */
	public Metadatos dameMetadatos(int idFichero) throws RemoteException;
	
	/**
	 * guarda los almacenes a fichero,	establece la persistencia del servidor, IMPORTANTE SI SE CIERRA DIRECTAMENTE LA VENTANA SIN SALIR A TRAVES DEL MENU LOS DATOS SE PIERDEN.
	 * @throws RemoteException
	 */
	public void guardarDatosPersistentes() throws RemoteException;

}
