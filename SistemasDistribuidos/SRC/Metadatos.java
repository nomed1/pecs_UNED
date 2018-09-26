/**
 * La clase Metadatos almacena el id cliente, la repo nombre y con quien esta compartido un fichero
 * el peso y checksum no los usamos realmente
 * 
 * @autor Buenaventura Salcedo Santos-Olmo, xpressmoviles@gmail.com
 * @version v1.20171006
 */
package nomed.common;

import java.io.Serializable;
import java.util.List;

public class Metadatos implements Serializable{
	
	
    private static int cont;//contador de instancias, para tener ids unicos de los metadatos
	private int id; //id de la coleccion

	private int idCliente;			//identificador del propietario original del fichero
	private int idRepositorio;
	private String nombreFichero;   //nombre del fichero
	private long peso;				//peso del fichero en bytes
	private long checksum;			//suma de chequeo de los bytes del fichero
	private List<Integer> compartidoCon;

	/**
	 * Constructor de la clase Metadatos, almacena los metadatos
	 * @param idCliente int el id unico del propietario del fichero
	 * @param idRepositorio int el id unico del repositorio en el quee sta almacenado el fichero
	 * @param nombreFichero String el nombre del fichero
	 * @param compartidoCon List<Integer> la lista de id unicos de los clientes a quien se ha compartido el fichero
	 */
	public Metadatos(int idCliente, int idRepositorio, String nombreFichero, List<Integer> compartidoCon) {
		super();
        cont++;//incrementamos el contador de instancias, para conseguir ids unicos
        id = cont;
		//this.id = id;
		this.idCliente = idCliente;
		this.idRepositorio = idRepositorio;
		this.nombreFichero = nombreFichero;
		this.compartidoCon = compartidoCon;
	}	
	

	/**
	 * devuelve el peso del fichero
	 * @return log el peso
	 */
	public long getPeso() {
		return peso;
	}

	/**
	 * modifica el peso del fichero
	 * @param peso long el peso
	 */
	public void setPeso(long peso) {
		this.peso = peso;
	}

	/**
	 * devuelve la suma de verificacion
	 * @return long la suma de verificacion
	 */
	public long getChecksum() {
		return checksum;
	}

	/**
	 * modifica la suma de verificacion
	 * @param checksum long la suma de verificacion
	 */
	public void setChecksum(long checksum) {
		this.checksum = checksum;
	}

	/**
	 * el id unico del fichero, con esto solucionamos el problema de los nombres duplicado en los ficheros
	 * @return int el id unico del fichero
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * modifica el id unico del fichero
	 * @param id int el id unico del fichero
	 */
	public void setId(int id) {
		this.id = id;
	}
	
	/**
	 * devuelve el id unico del cliente propietario del fichero
	 * @return int el id unico del cliente propietario del fichero
	 */
	public int getIdCliente() {
		return idCliente;
	}
	
	/**
	 * modifica el id unico del cliente propietario del fichero
	 * @param idCliente int el id unico del cliente propietario del fichero
	 */
	public void setIdCliente(int idCliente) {
		this.idCliente = idCliente;
	}
	
	/**
	 * devuelve el id unico del repositorio donde esta guardado el fichero
	 * @return int el id unico del repositorio donde esta guardado el fichero
	 */
	public int getIdRepositorio() {
		return idRepositorio;
	}
	
	/**
	 * modifica el id unico del repositorio donde esta guardado el fichero
	 * @param idRepositorio int el id unico del repositorio donde esta guardado el fichero
	 */
	public void setIdRepositorio(int idRepositorio) {
		this.idRepositorio = idRepositorio;
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
	 * @param nombreFichero String el nombre del fichero
	 */
	public void setNombreFichero(String nombreFichero) {
		this.nombreFichero = nombreFichero;
	}
	
	/**
	 * devuelve la lista de id unico de los cliente con quien se ha compartido el fichero
	 * @return List<Integer> la lsita de id unicos de los clientes con quien se ha comaprtido el fichero
	 */
	public List<Integer> getCompartidoCon() {
		return compartidoCon;
	}
	
	/**
	 * modifica la lista de id unicos de los clientes con quien se ha compartido el fichero
	 * @param compartidoCon List<Integer> la lista de id unicos de los clientes con quien se ha compartdio el fichero
	 */
	public void setCompartidoCon(List<Integer> compartidoCon) {
		this.compartidoCon = compartidoCon;
	}
	
	/**
	 * toSTring() en formato simple de los metadatos del fichero
	 * @return String en formato simple de los metadatos del fichero
	 */
	@Override
	public String toString() {
		return "Metadatos [id=" + id + ", idCliente=" + idCliente + ", idRepositorio=" + idRepositorio
				+ ", nombreFichero=" + nombreFichero + ", compartidoCon="
				+ compartidoCon + "]";
	}
}
