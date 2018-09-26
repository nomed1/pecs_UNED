/**
 * Clase ServicioGestorImpl que implementa la interface ServicioGestorInterface
 * permite realizar peticiones para subir, borrar, compartir y listar
 * es la pasarela con la el servicio de datos
 * 
 * @autor Buenaventura Salcedo Santos-Olmo, xpressmoviles@gmail.com
 * @version v1.20170625
 */

package nomed.servidor;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

import nomed.common.DatosIntercambio;
import nomed.common.Interfaz;
import nomed.common.Metadatos;
import nomed.common.ServicioAutenticacionInterface;
import nomed.common.ServicioClOperadorInterface;
import nomed.common.ServicioDatosInterface;
import nomed.common.ServicioGestorInterface;
import nomed.common.ServicioSrOperadorInterface;

public class ServicioGestorImpl extends UnicastRemoteObject implements ServicioGestorInterface{

	private static final long serialVersionUID = 9190993076067019921L;

	//atributos para buscar el servicio de Datos
	private static int puerto = 7791;
	private static ServicioDatosInterface datos;
	private static String direccion = "localhost";
	
	//atributos del ServicioClOperador
	private static int puertoServicioClOperador = 7792;
	private static String direccionServicioClOperador = "localhost";
	
	//atributos del ServicioSrOperador
	private static int puertoServicioSrOperador = 7792;
	private static String direccionServicioSrOperador = "localhost";
	
	/**
	 * contructor por defecto
	 * @throws RemoteException
	 * @throws MalformedURLException
	 * @throws NotBoundException
	 */
	protected ServicioGestorImpl() throws RemoteException, MalformedURLException, NotBoundException {
		super();
		//Podriamos buscar el almacen directamente desde el constructor
    	//datos = (ServicioDatosInterface) Naming.lookup("rmi://"+ direccion + ":" + puerto + "/almacen");
		
	}

	/**
	 * permite subir el fichero
	 * @param nombreFichero String el nombre del fichero a subir
	 * @param idSesionCliente el id sesion del cliente
	 * @return DatosIntercambio contien la url del servicio clOperador, y el id unico del cliente la carpeta
	 */
    @Override
    public DatosIntercambio subirFichero(String nombreFichero,int idSesionCliente) throws RemoteException, MalformedURLException, NotBoundException {

    	//AQUI HACE FALTA CONOCER LA IP PUERTO DEL ALMACEN
    	//Enviamos los datos para que el servicio Datos almacene los metadatos        
    	datos = (ServicioDatosInterface) Naming.lookup("rmi://"+ direccion + ":" + puerto + "/almacen");
    	
    	//aqui deberiamos haber combropado un mensaje de error si idCliente < 0 podriamos saber que ha habido error
        int idCliente = datos.almacenarFichero(nombreFichero,idSesionCliente);
        int idSesionRepo = datos.dimeRepositorio(idCliente);//sesion del repositorio
        Interfaz.imprime("Se ha agregado el fichero " + nombreFichero + " en el almacen de Datos"); 
        
        //Devolvemos la URL del servicioClOperador con la carpeta donde se guardara el tema
        DatosIntercambio di = new DatosIntercambio(idCliente , "rmi://" + direccionServicioClOperador + ":" + puertoServicioClOperador + "/cloperador/"+ idSesionRepo);
        return di;
    }

    /**
     * peticion para bajar un fichero
     * @param URLdiscoCliente la url del discoCliente donde se va a enviar el fichero
     * @param idFichero int el id del fichero a descargar
     * @param idSesionCliente int el id sesion del cliente para confirmar que el fichero es de el
     * @return String el nombre del fichero que se va a descargar
     */
	@Override
	public String bajarFichero(String URLdiscoCliente, int idFichero, int idSesionCliente) throws RemoteException, MalformedURLException, NotBoundException {
		datos = (ServicioDatosInterface) Naming.lookup("rmi://" + direccion + ":" + puerto + "/almacen");
		
		//se deberia comprobar si el fichero realmente es el del cliente
		
		Metadatos m = datos.descargarFichero(idFichero,idSesionCliente);
		int idCliente = datos.sesion2id(idSesionCliente);
		int idSesionRepo = datos.dimeRepositorio(idCliente);
		if (m != null){
			
			Interfaz.imprime("Se ha notificado que el fichero " + m.getNombreFichero() + " va a ser descargado");
			String URLservicioSrOperador = "rmi://" + direccionServicioSrOperador + ":" + puertoServicioSrOperador + "/sroperador/" + idSesionRepo;
		
			//ahora buscamos el Servicio SrOperador y le pasamos la URL del DiscoCliente y el id de cliente que es el nombre de la carpeta
			ServicioSrOperadorInterface servicioSrOperador =(ServicioSrOperadorInterface)Naming.lookup(URLservicioSrOperador);
			//el tercer parametro es la carpeta donde esta el fichero real, en caso de compartido es la carpeta dequien comparte evidentemente.
			servicioSrOperador.bajarFichero(URLdiscoCliente,m.getNombreFichero(),m.getIdCliente());
			return m.getNombreFichero();
		}else return null;		
	}

	/**
	 * listar los ficheros de un cliente, conpartidos incluidos
	 * @param idSesionCliente int el id sesion de cliente
	 * @return List<String> la lista de los ficheros, los string llevan el id del fichero y el nombre
	 */
	@Override
	public List<String> listarFicherosCliente(int idSesionCliente) throws RemoteException, MalformedURLException, NotBoundException {		
    	datos = (ServicioDatosInterface) Naming.lookup("rmi://"+ direccion + ":" + puerto + "/almacen");
    	Interfaz.imprime("La lista de ficheros de la sesion: " + idSesionCliente + " ha sido enviada");
        return datos.listarFicherosCliente(idSesionCliente);
        
	}

	/**
	 * borra un fichero
	 * @param idFichero int el id del fichero a borrar
	 * @param idSesionCliente int el id sesion del cliente para confirmar que el fichero es suyo
	 * @return DatosIntercambio los datos del fichero a borrar, la url del servicio ClOperador, el id unico de cliente la carpeta
	 */
	@Override
	public DatosIntercambio borrarFichero(int idFichero, int idSesionCliente) throws MalformedURLException, RemoteException, NotBoundException {
		DatosIntercambio di = new DatosIntercambio(0,"","");
		
    	datos = (ServicioDatosInterface) Naming.lookup("rmi://"+ direccion + ":" + puerto + "/almacen");
    	//vamos a comprobar si ha habido algun problema al borrar el fichero
    	Metadatos m = datos.dameMetadatos(idFichero);
    	int idCliente = datos.eliminarFichero(idFichero, idSesionCliente);

		switch (idCliente){
		case -1 : Interfaz.imprime("El fichero con id " + idFichero + " no corresponde al cliente");break;
		case  0 : Interfaz.imprime("El fichero con id " + idFichero + " no se ha encontrado");break;
		default : Interfaz.imprime("Se ha borrado el fichero con id " + idFichero);
    			  int idSesionRepo = datos.dimeRepositorio(idCliente);
				  di.setUrl("rmi://" + direccionServicioClOperador + ":" + puertoServicioClOperador + "/cloperador/"+idSesionRepo);
				  di.setIdCliente(idCliente);
				  di.setNombreFichero(m.getNombreFichero());
				  break;
		}
    	return di;
	}

	/**
	 * devuelve la liata de los clientes registrados indicando los que estan online
	 * @return String la lista en formato simple como toString()
	 */
	@Override
	public String listarClientes() throws MalformedURLException, RemoteException, NotBoundException {
		datos = (ServicioDatosInterface) Naming.lookup("rmi://"+ direccion + ":" + puerto + "/almacen");
    	String lista = datos.listaClientes();
    	return lista;
    	
	}

	/**
	 * comparte un fichero
	 * @param idFichero int el id del fichero a compartir
	 * @param nombreCliente String el nombre del cliente a quien se va a comaprtir el fichero
	 * @param idSesion int el id sesion del cliente que va a compartir el fichero, debe comprobar si el fichero es suyo
	 * @return boolean true si todo ha ido bien, false en caso contrario, por ejemplo sie l ficheor no es suyo o no existes
	 */
	@Override
	public boolean compartirFichero(int idFichero, String nombreCliente, int idSesion) throws RemoteException, MalformedURLException, NotBoundException {
		datos = (ServicioDatosInterface) Naming.lookup("rmi://"+ direccion + ":" + puerto + "/almacen");
		Metadatos m = datos.dameMetadatos(idFichero);
		boolean compartido = datos.compartirFichero(idFichero,nombreCliente,idSesion);
		if (compartido) Interfaz.imprime("El fichero " + m.getNombreFichero() + " se ha compartido correctamente");
		else Interfaz.imprime("El fichero con id: "+idFichero +" no se ha podido compartir");
		return compartido;
	}
	
}
