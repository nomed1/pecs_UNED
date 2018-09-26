/**
 * Clase DatosIntercambio, es una estructura con sus constructores para poder devolver URL, nombre de ficheros, e id unicos desde el Gestor
 * debe implementar Serializable para que funcione y pueda moverse en las invocaciones remotas
 * 
 * @autor Buenaventura Salcedo Santos-Olmo, xpressmoviles@gmail.com
 * @version v1.20171007
 */
package nomed.common;

import java.io.Serializable;

public class DatosIntercambio implements Serializable {

	private static final long serialVersionUID = -5678825599413594014L;

	int idCliente;
	int idSesionRepo;
	String url;
	String nombreFichero;
	
	/**
	 * Constructor que recibe id unico y una url y los almacena
	 * @param idCliente
	 * @param url
	 */
	public DatosIntercambio(int idCliente, String url) {
		this.idCliente = idCliente;
		this.url = url;
		nombreFichero = "";
	}
	
	/**
	 * Constructor que recibe id unico, una url y el nombre de un fichero y los almacena
	 * @param idCliente
	 * @param url
	 * @param nombreFichero
	 */
	public DatosIntercambio(int idCliente, String url, String nombreFichero) {
		this.idCliente = idCliente;
		this.url = url;
		this.nombreFichero = nombreFichero;
		this.idSesionRepo = 0;
	}

	/**
	 * Constructor que recibee el id unico de, el id sesion de la repo para adjuntar a la url para detectar los servicios de cada repo y el nombre de un fichero
	 * @param idCliente
	 * @param idSesionRepo
	 * @param url
	 * @param nombreFichero
	 */
	public DatosIntercambio(int idCliente, int idSesionRepo, String url, String nombreFichero) {
		super();
		this.idCliente = idCliente;
		this.idSesionRepo = idSesionRepo;
		this.url = url;
		this.nombreFichero = nombreFichero;
	}


	/**
	 * devuelve el id unico del cliente
	 * @return int el id unico del cliente
	 */
	public int getIdCliente() {
		return idCliente;
	}
	
	/**
	 * modifica el id unico del cliente
	 * @param int el idCliente unico de el cliente 
	 */
	public void setIdCliente(int idCliente) {
		this.idCliente = idCliente;
	}
	
	/**
	 * devuelve el id sesion de la repo para localizar los servicios de cada repo
	 * @return int el id sesion de la repo
	 */
	public int getIdSesionRepo() {
		return idSesionRepo;
	}
	
	/**
	 * modifica el idsesion de la repo
	 * @param idSesionRepo int el id de seion de la repo
	 */
	public void setIdSesionRepo(int idSesionRepo) {
		this.idSesionRepo = idSesionRepo;
	}
	
	/**
	 * devuelve la url 
	 * @return String la url
	 */
	public String getUrl() {
		return url;
	}
	
	/**
	 * modifica la url
	 * @param url String con la url
	 */
	public void setUrl(String url) {
		this.url = url;
	}	
	
	/**
	 * devuelve el nombre del fichero
	 * @return String el nombre del fichero
	 */
	public String getNombreFichero() {
		return nombreFichero;
	}
	
	/**
	 * modifica el nombre del fichero
	 * @param nombreFichero STring con el nombre del fichero
	 */
	public void setNombreFichero(String nombreFichero) {
		this.nombreFichero = nombreFichero;
	}


	/**
	 * toString() formato simple por defecto
	 * @return String el objeto en formato simple
	 */
	@Override
	public String toString() {
		return "DatosIntercambio [idCliente=" + idCliente + ", idSesionRepo=" + idSesionRepo + ", url=" + url
				+ ", nombreFichero=" + nombreFichero + "]";
	}
}
