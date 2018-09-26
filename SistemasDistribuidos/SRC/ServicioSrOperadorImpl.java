/**
 * Clase ServicioSrOperadorImpl que implementa la interface ServicioSrOperadorInterface
 * implementa los metodos crear carpeta y bajar un fichero
 * 
 * @autor Buenaventura Salcedo Santos-Olmo, xpressmoviles@gmail.com
 * @version v1.20170628
 */
package nomed.repositorio;

import java.io.File;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nomed.common.Fichero;
import nomed.common.ServicioClOperadorInterface;
import nomed.common.ServicioDiscoClienteInterface;
import nomed.common.ServicioSrOperadorInterface;

public class ServicioSrOperadorImpl extends UnicastRemoteObject implements ServicioSrOperadorInterface{

	private static final long serialVersionUID = 4699657426709583604L;

	/**
	 * constructor por defecto
	 * @throws RemoteException
	 */
	protected ServicioSrOperadorImpl() throws RemoteException {
		super();
	}

	/** crea la carpeta del cliente, no creamos carpeta de la repo, ya que no tiene sentido
	 *  puesto que el servicio Datos es quien conoce la relacion entre repos clientes
	 *  si hay varias repos registradas nos da igual, incluso si hay varias repos autenticadas
	 *  a si que las carpetas de los clientes se crean en la carpeta actual
	 * @param int idCliente el identificardor del cliente que sera el nombre de la carpeta
	 * @return true si la carpeta de cliente se ha creado con exito, false en otro caso
	 */
	@Override
	public boolean crearCarpetaRepositorio(int idCliente) throws RemoteException {

		File carpeta = new File("" + idCliente);		
		boolean creada = carpeta.mkdir(); //creada sera true si se ha creado.
		if (creada) {
			System.out.println("Se ha creado la carpeta: " + idCliente + " en el path: " + System.getProperty("user.dir"));
			Repositorio.listaCarpetas.add(""+idCliente);
		} else System.out.println("No se ha podido crear la carpeta: " + idCliente + " en el path: " + System.getProperty("user.dir"));
		return creada;		
	}

	/**
	 * envia un fichero del repositorio en la url enviada
	 * @param URLdiscoCliente String la url a la que se envia el fichero
	 * @param nombreFichero String el fichero que vamos a descargar
	 * @param idCliente int el id unico de cliente que es la carpeta de la repo
	 */
	@Override
	public void bajarFichero(String URLdiscoCliente, String nombreFichero, int idCliente) throws MalformedURLException, RemoteException, NotBoundException {
		//conversion implicita a cadena ""+idCliente
		Fichero fichero= new Fichero(""+idCliente,nombreFichero,""+idCliente);
		ServicioDiscoClienteInterface servicioDiscoCliente =(ServicioDiscoClienteInterface)Naming.lookup(URLdiscoCliente);

		if (servicioDiscoCliente.bajarFichero(fichero,idCliente)==false)
		{
			System.out.println("Error en el envío (Checksum failed), intenta de nuevo");
		}
		else{
			System.out.println("Fichero: " + nombreFichero + " enviado");	
		}
		
	}	
}
