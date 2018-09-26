/**
 * Implementa la interface ServicioAutenticacionInterface
 * Se encarga de registrar, autenticar, desconectar a clientes y a repositorios
 * 
 * @autor Buenaventura Salcedo Santos-Olmo, xpressmoviles@gmail.com
 * @version v1.20170301
 */

package nomed.servidor;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Random;

import nomed.common.Interfaz;
import nomed.common.ServicioAutenticacionInterface;
import nomed.common.ServicioDatosInterface;


public class ServicioAutenticacionImpl extends UnicastRemoteObject implements ServicioAutenticacionInterface{

	//necesitamos identificadores de sesion
	private static final long serialVersionUID = 123711131719L;
	private int sesion = Math.abs(new Random().nextInt()); //no quiero numeros negativos
	private int puerto = 7791;
	private ServicioDatosInterface almacen;
	
	/**
	 * Contructor necesario al extender UnicastRemoteOBject y poder utilizar Naming
	 * lo aprovecharemos tambien para buscar el almacen de datos
	 * @throws RemoteException
	 * @throws NotBoundException 
	 * @throws MalformedURLException 
	 */
	protected ServicioAutenticacionImpl() throws RemoteException, MalformedURLException, NotBoundException {
		super();

		//buscamos el objeto en el servidor gestor para autenticarnos
		String URLRegistro = "rmi://localhost:" + puerto + "/almacen";
		almacen = (ServicioDatosInterface) Naming.lookup(URLRegistro);		
	}

	/**
	 * solicita al servicio de Datos la autenticacion de un cliente,
	 * @param el nombre del cliente  que se quiere autenticar
	 * @return int el id sesion de cliente que se ha autenticado
	 */
	@Override
	public int autenticarCliente(String nombre) throws RemoteException {		
		int sesionUsuario = getSesion();
		int id = almacen.autenticarCliente(nombre, sesionUsuario);
		switch (id){
		case -2 : Interfaz.imprime("El cliente " + nombre + " no esta registrado, no se han tomado medidas");break;
		case -1 : Interfaz.imprime("No esta su repo online o no se ha podido crear la carpeta, se ha cancelado la autenticacion del cliente" + nombre); break;
		case 0  : Interfaz.imprime("El cliente " + nombre + " esta ya autenticado en el sistema, no se han tomado medidas");break;
		default : Interfaz.imprime("El cliente " + nombre + " se ha autenticado como cliente en el sistema");break;
		}		
		return id;
	}

	/**
	 * registra un cliente
	 * @param String el nombre del cliente a registrar
	 * @return int -1 si no hay repos online, 0 si el cliente ya esta registrado y el id unico del registro en caso de éxito
	 */
	@Override
	public int registrarCliente(String nombre) throws RemoteException, MalformedURLException, NotBoundException{
		int sesion = getSesion();
		int id = almacen.registrarCliente(nombre,sesion);
		switch (id){
		case -1 : Interfaz.imprime("No hay repos online, se ha cancelado el registro del cliente " + nombre); break;
		case 0  : Interfaz.imprime("El cliente " + nombre + " esta ya registrado en el sistema, no se han tomado medidas");break;
		default : Interfaz.imprime("El cliente " + nombre + " se ha registrado en el sistema");break;
		}
		return id;
	}

	/**
	 * autentica un repositorio
	 * @param String el nombre del repositorio
	 * @return int el id sesion de la repo
	 */
	@Override
	public int autenticarRepositorio(String nombre) throws RemoteException {
		int sesionRepositorio = getSesion();
		int id = almacen.autenticarRepositorio(nombre, sesionRepositorio);
		switch (id){
		case -1 : Interfaz.imprime("la repo " + nombre + " no esta registrada, se ha cancelado la autenticacion");break;
		case 0  : Interfaz.imprime("La repo " + nombre + " ya esta autenticada, no se han tomado medidas");break;
		default : Interfaz.imprime(nombre + " se ha autenticado como repo en el sistema");break;
		}
		return id;
	}

	/**
	 * registra un repositorio
	 * @String el nombre del repositorio
	 * @return int el id sesion del repositorio
	 */
	@Override
	public int registrarRepositorio(String nombre) throws RemoteException {
		int sesion = getSesion();
		int id = almacen.registrarRepositorio(nombre,sesion);
		if (id != 0)
			Interfaz.imprime("La repo " + nombre + " se ha registrado en el sistema");
		else 
			Interfaz.imprime("Se ha intentado duplicar la repo " + nombre + ", se ha cancelado la operacion");
		return id;
	}

	/**
	 * solicita al Gestor la desconexion de un cliente
	 * @param sesion  int el id sesion del cliente a desconectar
	 */
	@Override
	public void desconectarCliente(int sesion) throws RemoteException {
		String cliente = almacen.desconectarCliente(sesion);
		Interfaz.imprime("El cliente " + cliente + " se ha desconectado del sistema");	
	}

	/**
	 * solicita al Gestor la desconexion de una repo
	 * @param sesion int el id sesion de la repo a desconectar
	 */
	@Override
	public void desconectarRepositorio(int sesion) throws RemoteException { 
		String repo = almacen.desconectarRepositorio(sesion);
		Interfaz.imprime("La repo " + repo + " se ha desconectado del sistema");		
	}

	
	/**
	 * devulve un id de sesion
	 * @return int un id sesion nuevo valido para cliente o repo
	 */
	//devuelve el contador de sesiones
	public int getSesion() {
		return ++sesion;
	}
}
