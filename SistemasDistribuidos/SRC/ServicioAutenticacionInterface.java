/**
 * Interface public con los metodos del Servicio de Autenticacion
 * Se encarga de registrar y autenticar a usuarios y a repositorios
 * 
 * @autor Buenaventura Salcedo Santos-Olmo, xpressmoviles@gmail.com
 * @version v1.20170301
 */
package nomed.common;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServicioAutenticacionInterface extends Remote {

	/**
	 * intenta autenticar a un cliente en el servidor
	 * @param nombre String el nombre del cliente
	 * @return int -2 siel cliente no esta registrado, -1 si la repo no esta online, 0 si ya esta autenticado y id sesion del cliente en otro caso
	 * @throws RemoteException
	 */
	public int autenticarCliente (String nombre) throws RemoteException;
	
	/**
	 * intenta registrar a un cliente en el servidor
	 * @param nombre String el nombre del cliente
	 * @return int -1 si la repo no esta online, 0 si ya existe ese cliente, id unico del cliente en otro caso
	 * @throws RemoteException
	 * @throws MalformedURLException
	 * @throws NotBoundException
	 */
	public int registrarCliente (String nombre) throws RemoteException, MalformedURLException, NotBoundException;
	
	/**
	 * intenta autenticar a un repositorio en el servidor
	 * @param nombre String el nombre del repositorio
	 * @return int -1 si la repo no esta registrada, 0 si ya esta autenticada, id sesion de la repo en otro caso
	 * @throws RemoteException
	 */
	public int autenticarRepositorio(String nombre) throws RemoteException;
	
	/**
	 * intenta registrar a un repositorio en el servidor
	 * @param nombre String de la repo
	 * @return 0 si la repo ya esta registrada, id unico de la repo en otro caso
	 * @throws RemoteException
	 */
	public int registrarRepositorio(String nombre) throws RemoteException;
	
	/**
	 * desconecta al cliente enviando el id sesion
	 * @param sesion int el id sesion del cliente a desconectar
	 * @throws RemoteException
	 */
	public void desconectarCliente(int sesion) throws RemoteException;
	
	
	/**
	 * desconecta la repo enviado el id sesion
	 * @param sesion int el id seion de la repo a desconectar
	 * @throws RemoteException
	 */
	public void desconectarRepositorio(int sesion) throws RemoteException;
	
	
}
