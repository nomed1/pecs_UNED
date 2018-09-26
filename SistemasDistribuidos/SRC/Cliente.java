/**
 * La clase Cliente debe:
 * Registrar y autenticar clientes
 * Solicitar y mostrar listas de los ficheros y clientes
 * Solicitar la subida,bajada,borrado y comparticion de ficheros
 * 
 * @autor Buenaventura Salcedo Santos-Olmo, xpressmoviles@gmail.com
 * @version v1.20171007
 */
package nomed.cliente;

import java.io.File;
import java.net.MalformedURLException;
import java.rmi.ConnectException;
import java.rmi.Naming;
import java.rmi.NoSuchObjectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

import nomed.common.DatosIntercambio;
import nomed.common.Fichero;
import nomed.common.Interfaz;
import nomed.common.ServicioAutenticacionInterface;
import nomed.common.ServicioClOperadorInterface;
import nomed.common.ServicioGestorInterface;
import nomed.common.ServicioSrOperadorInterface;
import nomed.common.Utils;

public class Cliente {
	
	private static int miSesion=0;
	
	//atributos para buscar los servicios del servidor
	private static int puerto = 7791;
	private static ServicioAutenticacionInterface servidor;
	private static String direccion = "localhost";
	
	//aqui se van a guardar las URL rmi
	private static String autenticador;
	private static String gestor;
	private static String discocliente;
	
	@SuppressWarnings("unused")
	private static String nombre = ""; //el nombre del cliente de autenticado
	
	// atributos para levantar el servicio DiscoCliente
	// es de suponer que esto es necesario ya que puede ejecutarse
	// en otra maquina tendriamos que saber la ip nuestra 
	// y cambiarla por la direccionServicio
	private static int puertoServicio = 7793;
	private static Registry registryServicio;
	private static String direccionServicio = "localhost";
	

	/**
	 * main del cliente, generas las URL usadas en el programa
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception{

		//vamos a crear aqui las direcciones, que por supuesto podriamos leerlas por teclado, desde un fichero de configuracion
		//o bien pasarselas al jar
		autenticador = "rmi://" + direccion +         ":" + puerto +         "/autenticador";
		discocliente = "rmi://" + direccionServicio + ":" + puertoServicio + "/discocliente/";
		gestor       = "rmi://" + direccion +         ":" + puerto +         "/gestor";
		
		new Cliente().iniciar();
		System.exit(0);//fin del programa,return o nada deja abierto el programa
	}

	/**
	 * inicia el registro de un cliente nuevo en el sistema, solicita los datos desde teclado
	 * @throws NotBoundException 
	 * @throws MalformedURLException 
	 */
	private static void registrar() throws RemoteException, MalformedURLException, NotBoundException{
		String s = Interfaz.pideDato("Introduzca nombre cliente");
		miSesion = servidor.registrarCliente(s);
		switch (miSesion){
		case -1: System.out.println("No hay repos online, intentelo de nuevo mas tarde");break;
		case 0 : System.out.println(s + " el usuario ya existe en el sistema, puede autenticarlo opcion 2");break;
		default: System.out.println(s + " se ha registrado en el sistema, ahora puede autenticarse con opcion 2");break;
		}		
	}

	/**
	 * conecta con el utenticador y muestra el menu principal
	 * @throws Exception
	 */
	private void iniciar() throws Exception {
		
		//buscamos el objeto en el servidor gestor para autenticarnos
		String URLRegistro = autenticador;//RMI
		
		// si el servidor no esta disponible, cerramos informando de ello
		try{
			servidor = (ServicioAutenticacionInterface) Naming.lookup(URLRegistro);
		
		//mostramos menu de acceso
			int opcion = 0;	
			do{
				opcion = Interfaz.menu("Acceso de Cliente",new String[]
						{"Registrar un nuevo usuario","Autenticarse en el sistema(hacer login)"});
			
				switch (opcion){
					case 1: registrar();break;
					case 2: autenticar();break;
				}			
			}while (opcion!=3);
		}catch (ConnectException e){
			System.out.println("Error de conexion, el servidor no esta disponible, vuelva a intentarlo mas tarde");
			String st = Interfaz.pideDato("Pulse enter para finalizar...");
		}
		
	}
	
	/**
	 * inicia la autenticacion de un cliente en el sistema, solicita datos
	 * y entra en un bucle de peticion de opciones con un menu
	 * @throws Exception 
	 */
	private void autenticar() throws Exception{
		
		//ESTO QUIZAS DEBERIAMOS PONER UN CASE PARA -2,-1,0 y >0
		String s = Interfaz.pideDato("Introduzca nombre cliente");
		if ((miSesion = servidor.autenticarCliente(s)) > 0) {
			nombre = s; //asi la clase almacena el nombre del cliente
			levantarServicios();//levanta el servicio DiscoCliente
		}
		else 
			System.out.println("el usuario no existe en el sistema, use registrarse opcion 1");		
	}

	/**
	 * solicita la desconexion 
	 * @throws RemoteException
	 */
	private void desconectar() throws RemoteException{
		servidor.desconectarCliente(miSesion);
		//si ponemos a 0 la sesion aqui el unbind fallara
		//miSesion=0;
	}

	/**
	 * levanta el Registry y el servicio Discocliente con el id sesion actual
	 * muestra los servicios colgados (los discocliente de todos los clientes)
	 * tiene que cerrar tambien los servicios cuando se sale del menu de Servicio
	 * @throws Exception
	 */
	private void levantarServicios() throws Exception  {
		String URLRegistro;
		arrancarRegistro(puertoServicio);
		//cuidado con la linea siguiente
		Utils.setCodeBase(ServicioSrOperadorInterface.class);
		
		//Levantar SrOperador en sroperador
		ServicioDiscoClienteImpl objetoDiscoCliente = new ServicioDiscoClienteImpl();
		//ServicioDiscoClienteImpl objetoDiscoCliente2 = new ServicioDiscoClienteImpl();
		//URLRegistro = "rmi://" + direccionServicio + ":" + puertoServicio + "/discocliente";
		//String URLRegistro2 = "rmi://" + direccionServicio + ":" + puertoServicio + "/discocliente/" + miSesion;
		//Naming.rebind(URLRegistro2, objetoDiscoCliente2);
		URLRegistro = discocliente + miSesion ;//RMI
		Naming.rebind(URLRegistro, objetoDiscoCliente);
		System.out.println("Operacion: Servicio Disco Cliente preparado con exito");
				
		listRegistry("rmi://" + direccionServicio + ":" + puertoServicio);
		//menu una vez autenticado el servicio
		menuServicio();
		
		//eliminar Servidor-Operador
		System.out.println("Operacion: Servicio Disco Cliente cerrandose...");
		try {
			URLRegistro = discocliente + miSesion;//RMI
			Naming.unbind(URLRegistro);	
			System.out.println("Operacion: Servicio Disco Cliente cerrado con exito");
	
			//cerrar rmiregistry del objeto registry unico
			if (estaVacioRegistry(discocliente)) { //RMI
				UnicastRemoteObject.unexportObject(registryServicio,true);		
				System.out.println("Operacion: Registry cerrado con exito");
			} else System.out.println("Operacion: Registry todavia esta abierto porque quedan clientes conectados");

		} catch (NoSuchObjectException e) {
		    System.out.println("No se ha podido cerrar el registro, se ha forzado el cierre");
		}
		miSesion = 0;
		
	}
	
	/**
	 * Proporciona un menu mediante un bucle de espera
	 * @throws RemoteException
	 * @throws MalformedURLException
	 * @throws NotBoundException
	 */
	private void menuServicio() throws RemoteException, MalformedURLException, NotBoundException{
		int opcion = 0;	
		do{
			opcion = Interfaz.menu("Operaciones de Cliente",new String[]
					{"Subir fichero","Bajar fichero","Borrar fichero","Compartir fichero",
					"Listar ficheros","Listar clientes del sistema"});				
			switch (opcion){
				case 1: subirFichero();break;
				case 2: bajarFichero();break;
				case 3: borrarFichero();break;
				case 4: compartirFichero();break;
				case 5: listarFicheros();break;
				case 6: listarClientes();break;
			}			
		}while (opcion!=7);
		
		desconectar();
	}

	/**
	 * Muestra por pantalla la lista de los clientes registrados y cuales de ellos estan online 
	 * @throws MalformedURLException
	 * @throws RemoteException
	 * @throws NotBoundException
	 */
	private void listarClientes() throws MalformedURLException, RemoteException, NotBoundException {
		String URLRegistro = gestor;//RMI
		ServicioGestorInterface servicioGestor =  (ServicioGestorInterface) Naming.lookup(URLRegistro);
		
		String lista = servicioGestor.listarClientes();
		System.out.println(lista);
		
	}

	/**
	 * Muestra los ficheros(compartidos incluidos) que tiene este cliente
	 * @throws MalformedURLException
	 * @throws RemoteException
	 * @throws NotBoundException
	 */
	private void listarFicheros() throws MalformedURLException, RemoteException, NotBoundException {
		
		String URLRegistro = gestor;//RMI
		ServicioGestorInterface servicioGestor =  (ServicioGestorInterface) Naming.lookup(URLRegistro);
		
		//el servicio Gestor solo necesita el id de la sesion del cliente para saber quien es
		List<String> lista = servicioGestor.listarFicherosCliente(miSesion);
		System.out.println(lista);
		
		
	}

	/**
	 * comparte un fichero, pidiendo el id del fichero y el nombre a quien se compartira
	 * @throws MalformedURLException
	 * @throws RemoteException
	 * @throws NotBoundException
	 */
	private void compartirFichero() throws MalformedURLException, RemoteException, NotBoundException {
		listarFicheros();
		String s = Interfaz.pideDato("Introduzca el IDENTIFICADOR del fichero, p.e. X.- nombre.Fichero, el identificador es X");
		int idFichero = Integer.parseInt(s);	
		
		listarClientes();
		String nombreCliente = Interfaz.pideDato("Introduzca nombre del cliente");
		
		String URLRegistro = gestor; //RMI
		ServicioGestorInterface servicioGestor =  (ServicioGestorInterface) Naming.lookup(URLRegistro);
		
		boolean compartido = servicioGestor.compartirFichero(idFichero,nombreCliente,miSesion);
		
		if (compartido) System.out.println("El fichero se ha compartido correctamente");
		else System.out.println("El fichero no se ha podido compartir");
	}

	/**
	 * borrar un fichero, pide el borrado al servicio Gestor quien devuelve la url del servicio del respositorio
	 * @throws MalformedURLException
	 * @throws RemoteException
	 * @throws NotBoundException
	 */
	private void borrarFichero() throws MalformedURLException, RemoteException, NotBoundException {
		//mostramos los ficheros
		listarFicheros();
		String s = Interfaz.pideDato("Introduzca el IDENTIFICADOR del fichero, p.e. X.- nombre.Fichero, el identificador es X");
		int idFichero = Integer.parseInt(s);
		
		//Solicitamos la URL del ServicioClOperador
		String URLRegistro = gestor;//RMI
		ServicioGestorInterface servicioGestor =  (ServicioGestorInterface) Naming.lookup(URLRegistro);
		//CUIDADO CON miSesion, hay que estudiar esto, para que no se borren los compartidos
		DatosIntercambio di = servicioGestor.borrarFichero(idFichero,miSesion);
		if (di.getUrl().equals("")) 
			System.out.println("No se ha podido completar el borrado, no tiene privilegios suficientes o el fichero no es suyo");
		else {
			String URL = di.getUrl();
			String nombreFichero = di.getNombreFichero();
			String propietario = ""+di.getIdCliente();
			//System.out.println(propietario);
			//System.out.println(URLservicioClOperador);
			
			ServicioClOperadorInterface servicioClOperador =(ServicioClOperadorInterface)Naming.lookup(URL);
			
			if (servicioClOperador.borrarFichero(nombreFichero,propietario)==false)
			{
				System.out.println("Error al borrar el fichero "+ nombreFichero + ", pida que lo borran manualmente al administrador del repositorio");
			}
			else{
				System.out.println("Fichero: " + nombreFichero + " borrado");	
			}	
		}
		
	}

	/**
	 * solicita la descarga de un fichero pidiendo el id de fichero(compartidos incluidos)
	 * @throws MalformedURLException
	 * @throws RemoteException
	 * @throws NotBoundException
	 */
	private void bajarFichero() throws MalformedURLException, RemoteException, NotBoundException {
		listarFicheros();
		String s = Interfaz.pideDato("Introduzca el IDENTIFICADOR del fichero, p.e. X.- nombre.Fichero, el identificador es X");
		int idFichero = Integer.parseInt(s);
		
		String URLRegistro = gestor;//RMI
		ServicioGestorInterface servicioGestor =  (ServicioGestorInterface) Naming.lookup(URLRegistro);
		//realmente no necesitamos que nos devuelva nada en principio, pero comunicar algo siempre es importante para monitorizar el proceso
		//String URLdiscoCliente = "rmi://" + direccionServicio + ":" + puertoServicio + "/" + "discocliente";
		String URLdiscoCliente = discocliente + miSesion;//RMI
		String cadena = servicioGestor.bajarFichero(URLdiscoCliente,idFichero,miSesion);
		if (cadena==null) System.out.println("No ha sido posible bajar ese fichero");else System.out.println("Fichero bajado: " + cadena);
		
	}

	/**
	 * solicita la subida de un fichero a su repo
	 * @throws MalformedURLException
	 * @throws RemoteException
	 * @throws NotBoundException
	 */
	private void subirFichero() throws MalformedURLException, RemoteException, NotBoundException {
		//debe estar en la carpeta actual
		System.out.println("¡¡OJO!! el fichero debe estar en la carpeta actual");
		String nombreFichero = Interfaz.pideDato("Introduzca el nombre del fichero");		
		File f = new File(nombreFichero);
	    if (!f.exists())
	        System.out.println("El fichero no existe, abortamos la mision");
	      else {
	    	  //Solicitamos la URL del ServicioClOperador
	    	  String URLRegistro = gestor;//RMI
	    	  ServicioGestorInterface servicioGestor =  (ServicioGestorInterface) Naming.lookup(URLRegistro);
				
	    	  //Le pasamos el nombre del fichero y el id de sesion y Gestor ya se encargara de colocar los metadatos
	    	  //Desde esta llamada podriamos pasar los metadatos como el peso o el ckecksum con los metodos de Fichero
	    	  //De todas formas el gestor va a poder conseguir el idRepo el nombreRepo,idCliente,nombreCliente con 
	    	  //tan solo miSesion con ese dato que pida los datos al servicioDatos
		
	    	  DatosIntercambio datosURL = servicioGestor.subirFichero(nombreFichero,miSesion);
		
	    	  //extraemos la carpeta donde se va a guardar el fichero, que sera el id, del cliente
	    	  //que sera al mismo tiempo el propietario del Fichero, segundo parametro
		
	    	  String URLservicioClOperador = datosURL.getUrl();
	    	  String propietario = ""+datosURL.getIdCliente();
	    	  
	    	  //HAY QUE COMPROBAR SI EXISTE EL FICHERO en la carpeta actual
	    	  Fichero fichero= new Fichero(nombreFichero,propietario);
	    	  ServicioClOperadorInterface servicioClOperador =(ServicioClOperadorInterface)Naming.lookup(URLservicioClOperador);

	    	  if (servicioClOperador.subirFichero(fichero)==false)
	    	  {
	    		  System.out.println("Error en el envío (Checksum failed), intenta de nuevo");
	    	  }
	    	  else{
	    		  System.out.println("Fichero: " + nombreFichero + " enviado");	
	    	  }
	      }		
	}

	/**
	 * Se usa para levantar el servicio del Disco Cliente
	 * @param numPuertoRMI numero de puerto del servicio DiscoCliente
	 * @throws RemoteException
	 */
	private void arrancarRegistro(int numPuertoRMI) throws RemoteException {
		try {
			registryServicio = LocateRegistry.getRegistry(numPuertoRMI);
			registryServicio.list(); // Esta llamada lanza
			// una excepcion si el registro no existe
		}
		catch (RemoteException e) {
			// Registro no valido en este puerto
			System.out.println("El registro RMI no se puede localizar en el puerto "+ numPuertoRMI);
			registryServicio =	LocateRegistry.createRegistry(numPuertoRMI);
			System.out.println("Registro RMI creado en el puerto " + numPuertoRMI);
		}
	}

	/**
	 * lista los servicios de un puerto
	 * @param registryURL la direccion del puerto a comprobar
	 * @throws RemoteException
	 * @throws MalformedURLException
	 */
	private static void listRegistry(String registryURL) throws RemoteException, MalformedURLException{
		System.out.println("Registry " + registryURL + " contiene: ");
		String[] names =Naming.list(registryURL);
		for  (int i=0; i< names.length; i++)
		{
			System.out.println(names[i]);
		}
	}

	/**
	 * comprueba si tiene colgados servicios en el la URL
	 * @param URL
	 * @return true si no hay servicios en la URL
	 * @throws RemoteException
	 * @throws MalformedURLException
	 */
	private static boolean estaVacioRegistry(String URL) throws RemoteException, MalformedURLException{
		String[] names = Naming.list(URL);
		if (names.length == 0) return true; else return false;		
	}

}
