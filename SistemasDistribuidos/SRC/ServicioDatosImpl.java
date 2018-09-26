/**
 * La clase ServicioDatosImpl implementa la interface ServicioDatosInterface
 * Almacena y gestiona los datos como si de una base de datos se tratase
 * De esta forma el acoplamiento es minimo, y podemos rescribirla utilizando
 * cualquier otro mecanismo de almacenamiento
 * 
 * Recordemos operaciones con HashMap: put y containsKey
 * 
 * @autor Buenaventura Salcedo Santos-Olmo, xpressmoviles@gmail.com
 * @version v1.20170228
 */
package nomed.servidor;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import nomed.common.Interfaz;
import nomed.common.Metadatos;
import nomed.common.Persistencia;
import nomed.common.ServicioDatosInterface;
import nomed.common.ServicioSrOperadorInterface;

public class ServicioDatosImpl extends UnicastRemoteObject implements ServicioDatosInterface{
	
	private static final long serialVersionUID = 123711131719L;
	
	//atributos para buscar el servicio Servidor Operador del Repositorio
	private static int puertoSrOperador = 7792;
	//private static ServicioSrOperadorInterface servidorSrOperador;
	private static String direccionSrOperador = "localhost";
	private static String nombreSrOperador="sroperador";
	
	//Estructuras que mantienen las autenticaciones VOLATILES
	private Map<Integer, String> sesionCliente = new HashMap<Integer, String>();
	private Map<String, Integer> clienteSesion = new HashMap<String, Integer>();
	private Map<Integer, String> sesionRepositorio = new HashMap<Integer, String>();
	private Map<String, Integer> repositorioSesion = new HashMap<String, Integer>();

	//Estructuras que mantiene el almacen de Clientes y Repositorios registrados PERSISTENTES
	private Map<Integer, String> almacenIdCliente;// = new HashMap<Integer, String>();
	private Map<String, Integer> almacenClienteId;// = new HashMap<String, Integer>();
	private Map<Integer, String> almacenIdRepositorio;// = new HashMap<Integer, String>();
	private Map<String, Integer> almacenRepositorioId;// = new HashMap<String, Integer>();
	private Map<Integer,Integer> almacenClienteRepositorio;// = new HashMap<Integer,Integer>();
	
	//en los metadatos esta a quien se le ha compartido cada fichero.
	private List<Metadatos> almacenFicheros;// = new ArrayList<Metadatos>();
	//necesitamos tambien a cada cliente quien le ha compartido
	private Map<Integer,List<Integer>> almacenClienteFicheros;// = new HashMap<Integer,List<Integer>>();
	
	//carpeta donde se mantendra la persistencia
	private static String carpetaPersistencia = "persistencia";
	
	//Metodos
	/**
	 * Contructor necesario al extender UnicastRemoteOBject y poder utilizar Naming
	 * @throws RemoteException
	 */

	protected ServicioDatosImpl() throws RemoteException {
		super();
		
		//cargamos los datos persistentes
		File persistencia = new File (carpetaPersistencia);
		if (persistencia.exists()){
			Interfaz.imprime("la carpeta de persistencia de datos existe vamos a recuperar los datos");
			leerDatosPersistentes();
			
			
		}else{
			Interfaz.imprime("la carpeta " + carpetaPersistencia + " no existe");
			inicializarTablas();
			persistencia.mkdir();			
			guardarDatosPersistentes();			
		}
	}
	
	/**
	 * inicializa las tablas para ser guardadas
	 */
	public void inicializarTablas(){
		almacenIdCliente = new HashMap<Integer, String>();
		almacenClienteId = new HashMap<String, Integer>();
		almacenIdRepositorio = new HashMap<Integer, String>();
		almacenRepositorioId = new HashMap<String, Integer>();
		almacenClienteRepositorio = new HashMap<Integer,Integer>();
		almacenFicheros = new ArrayList<Metadatos>();
		almacenClienteFicheros = new HashMap<Integer,List<Integer>>();
	}
	
	/**
	 * almacena las tablas para que sean persistentes
	 */
	public void guardarDatosPersistentes(){		
		Persistencia.guardar(almacenIdCliente, carpetaPersistencia + File.separator + Persistencia.FALMACENIDCLIENTE);
		Persistencia.guardar(almacenClienteId, carpetaPersistencia + File.separator + Persistencia.FALMACENCLIENTEID);
		Persistencia.guardar(almacenIdRepositorio, carpetaPersistencia + File.separator + Persistencia.FALMACENIDREPOSITORIO);
		Persistencia.guardar(almacenRepositorioId, carpetaPersistencia + File.separator + Persistencia.FALMACENREPOSITORIOID);
		Persistencia.guardar(almacenClienteRepositorio, carpetaPersistencia + File.separator + Persistencia.FALMACENCLIENTEREPOSITORIO);
		Persistencia.guardar(almacenFicheros, carpetaPersistencia + File.separator + Persistencia.FALMACENFICHEROS);
		Persistencia.guardar(almacenClienteFicheros, carpetaPersistencia + File.separator + Persistencia.FALMACENCLIENTEFICHEROS);	
	}
	
	/**
	 * lee las tablas de los ficheros para que sean persistentes
	 */
	@SuppressWarnings("unchecked")
	public void leerDatosPersistentes(){
		almacenIdCliente = (HashMap) Persistencia.leer(carpetaPersistencia + File.separator + Persistencia.FALMACENIDCLIENTE);
		almacenClienteId = (HashMap) Persistencia.leer(carpetaPersistencia + File.separator + Persistencia.FALMACENCLIENTEID);
		almacenIdRepositorio = (HashMap) Persistencia.leer(carpetaPersistencia + File.separator + Persistencia.FALMACENIDREPOSITORIO);
		almacenRepositorioId = (HashMap) Persistencia.leer(carpetaPersistencia + File.separator + Persistencia.FALMACENREPOSITORIOID);
		almacenClienteRepositorio = (HashMap) Persistencia.leer(carpetaPersistencia + File.separator + Persistencia.FALMACENCLIENTEREPOSITORIO);
		almacenFicheros = (ArrayList) Persistencia.leer(carpetaPersistencia + File.separator + Persistencia.FALMACENFICHEROS);
		almacenClienteFicheros = (HashMap) Persistencia.leer(carpetaPersistencia + File.separator + Persistencia.FALMACENCLIENTEFICHEROS);
	}
	
	/**
	 * autentica un cliente devolviendo el id
	 * @param nombre el nombre del cliente para autenticar
	 * @param id el entero de sesion
	 * @return int entero con el id
	 */
	@Override
	public int autenticarCliente(String nombre,int id) {
		if (clienteSesion.containsKey(nombre)) return clienteSesion.get(nombre);//podriamos no haber hecho nada y retornar 0 al estar autenticado,
																				//en la repo no dejamos que se vuelva a autorizar
		else {
			if (almacenClienteId.containsKey(nombre)){

				int repo = almacenClienteRepositorio.get(almacenClienteId.get(nombre)) ;
				
				if(sesionRepositorio.containsValue(almacenIdRepositorio.get(repo))) { 
					clienteSesion.put(nombre, id);
					sesionCliente.put(id, nombre);
					return id; //autenticado recibe el id sesion
				} else return -1; //su repo no esta online no se autentica

			} else return -2; //el cliente no esta registrado
		}
	}
	
	/**
	 * registra un cliente devolviendo un id
	 * @param nombre el nombre del cliente a registrar
	 * @param id la sesion
	 * @return int entero con el id
	 * @throws RemoteException 
	 * @throws NotBoundException 
	 * @throws MalformedURLException 
	 */
	public int registrarCliente(String nombre, int id) throws RemoteException, MalformedURLException, NotBoundException{

		if (almacenClienteId.containsKey(nombre)) return 0;//ya esta registrado
		else {
			int repo = dameRepositorio();
			if (repo != 0){
				//buscamos el objeto en el servidor gestor para autenticar la repo
				int idSesionRepo = repositorioSesion.get(almacenIdRepositorio.get(repo));
				String URLRegistro = "rmi://" + direccionSrOperador + ":" + puertoSrOperador + "/" + nombreSrOperador + "/" + idSesionRepo;
				ServicioSrOperadorInterface servidorSrOperador =  (ServicioSrOperadorInterface) Naming.lookup(URLRegistro);
				
				if (servidorSrOperador.crearCarpetaRepositorio(id) ){
			
					almacenClienteId.put(nombre, id);
					almacenIdCliente.put(id, nombre);
					almacenClienteRepositorio.put(id,repo);
					List<Integer> listaFicheros = new ArrayList<Integer>();
					almacenClienteFicheros.put(id,listaFicheros);
					return id;
				} else return -1;
			} else return -1;//no hay repos disponible estan offline
		}
		
	}

	/**
	 * autentica un repositorio
	 * @param String el nombre del repositorio
	 * @param int el idsesion que le pasamos
	 * @return int -1 si la repo no estra registrada, 0 si ya esta autenticado, el idsesion si es correcto
	 */
	@Override
	public int autenticarRepositorio(String nombre, int id) throws RemoteException {
		if (repositorioSesion.containsKey(nombre)) return 0;//ya esta autenticado
		else {//seria conveniente comprobar si esta registrada la repo
			if (almacenRepositorioId.containsKey(nombre)) {
				repositorioSesion.put(nombre, id);
				sesionRepositorio.put(id, nombre);
				return id;
			} else return -1;//la repo no esta registrada			
		}
	}

	/**
	 * registra un repositiorio
	 * @param String el nombre del repositorio
	 * @param int el id sesion del repositorio
	 * @return int 0 si ya esta registrada con ese nombre, el id sesion en caso contrario
	 */
	@Override
	public int registrarRepositorio(String nombre, int id) throws RemoteException {
		if (almacenRepositorioId.containsKey(nombre)) return 0;//ya esta registrada
		else {
			almacenRepositorioId.put(nombre, id);
			almacenIdRepositorio.put(id, nombre);
		}
		return id;
	}

	/**
	 * devulve la lsita de lcientes con un formato basico de presentacion de datos similar a toString()
	 * se muestran todos los clientes registrado y se indican si estan online o no.
	 * @return String la lista de los cliente formateada
	 */
    @Override
    public String listaClientes() throws RemoteException {
         String clientes = "";
         for (String nombre : almacenIdCliente.values()) {
        	 int id = almacenClienteId.get(nombre);
        	 int estado = 0;
        	 if (clienteSesion.containsKey(nombre)) estado = 1;
     		String s = "";
    		if (estado == 0 ) s = "[OFFLINE]";else s = "[ ONLINE]";
    		clientes = clientes + "Cliente ["+ s + " id=" + id + ", nombre=" + nombre + "] ";
         }              
         return clientes;
    }

    /**
     * devuelve la lista de repositorios con un formato basico de presentacion
     * se muestran todas las repos registradas y si estan online o no
     * @return String la lista de las repos formateada
     */
	@Override
	public String listaRepositorios() throws RemoteException {
		String lista = "";
		
		Iterator it = almacenIdRepositorio.entrySet().iterator();
		
		while (it.hasNext()){
			Map.Entry e = (Map.Entry)it.next();
			int id = (Integer) e.getKey();
			String nombre = (String) e.getValue();
       	 	int estado = 0;
       	 	if (repositorioSesion.containsKey(nombre)) estado = 1;
    		String s = "";
    		if (estado == 0 ) s = "[OFFLINE]";else s = "[ ONLINE]";
    		lista = lista + "Repositorio ["+ s + " id=" + id + ", nombre=" + nombre + "] ";
		}
		return lista;
		
	}

	/**
	 * devuelve un String con los emparejameintos entre clientes y repos
	 * recordemos un lciente solo esta en una repo
	 * se devulve el ide cliente el id repositiorio el nombre cliente y el nombre de la repo
	 * @return la lsita de las parejas
	 */
	@Override
	public String listaClientesRepositorios() {
		String lista = "";
		//System.out.println(almacenIdRepositorio);
		//System.out.println(almacenClienteRepositorio);
		Iterator it = almacenClienteRepositorio.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry e = (Map.Entry)it.next();			
			int idCliente = (Integer) e.getKey();
			int idRepositorio = (Integer) e.getValue();
			String nombreCliente = almacenIdCliente.get(idCliente);
			String nombreRepositorio = almacenIdRepositorio.get(idRepositorio);
			lista = lista + "Pareja Cliente - Repositorio [cliente=" + idCliente + ", repositorio=" + idRepositorio + ", nombreCliente="
			+ nombreCliente + ", nombreRepositorio=" + nombreRepositorio + "] ";
		}
		//System.out.println("Todavia listaClientesRepositorio: " + lista);
		return lista;
	}

	/**
	 * Elimina a un cliente de las sesion activas
	 * ojo!!!!! no lo borra del almacen de usuarios registrados, solo cierra la sesion
	 * podriamos devolver algun codigo de error, pero pasamos de momento
	 * @param sesion el identificador de la sesion actual
	 * @return int devuelve 0 sin error otro valor si hay error
	 */
	@Override
	public String desconectarCliente(int sesion) throws RemoteException {
		String cliente = sesionCliente.get(sesion);
		sesionCliente.remove(sesion);
		clienteSesion.remove(cliente);
		return cliente;
	}

	/**
	 * borra la entrada de sesion de una repo, es decir desconecta la repo
	 * @param int el id sesion de la repo
	 * @return String el nombre de la repo desconectada
	 */
	@Override
	public String desconectarRepositorio(int sesion) throws RemoteException {
		String repo = sesionRepositorio.get(sesion);
		sesionRepositorio.remove(sesion);
		repositorioSesion.remove(repo);
		return repo;
	}

	/**
	 * busca la priemra repo online que encuentra y devulve su id unico
	 * @return int el id unico de la primera repo online encontrada
	 */
	public int dameRepositorio() {
		if (repositorioSesion.isEmpty()) {
			return 0;
		} else{
			Iterator it = repositorioSesion.entrySet().iterator();
			String nombre="";
			if (it.hasNext()) {
				Map.Entry e = (Map.Entry)it.next();
				nombre = (String) e.getKey();
			}
			return almacenRepositorioId.get(nombre);
		}
	}
	
	/**
	 * devuelve el repositorio de un cliente
	 * @param int el id unico del cliente
	 * @return int el id sesion de la repo
	 */
	public int dimeRepositorio(int idCliente){		
		return repositorioSesion.get(almacenIdRepositorio.get(almacenClienteRepositorio.get(idCliente)));
	}

	/**
	 * almacena en las tablas un fichero
	 * @param String el nombre del fichero que queremos subir a la repo
	 * @param int el id sesion del cliente que va a subir el fichero
	 * @return el id del cliente que es la carpeta donde se va a subir el fichero
	 */
	public int almacenarFichero(String nombreFichero, int idSesionCliente) {
		//Construimos los metadatos y los almacenamos
		String nombreCliente = sesionCliente.get(idSesionCliente);
		int idCliente = almacenClienteId.get(nombreCliente);
		int idRepositorio = almacenClienteRepositorio.get(idCliente);		
		List<Integer> l = new ArrayList<Integer>();//la lista de con quien se ha compartido		
		Metadatos m = new Metadatos(idCliente,idRepositorio,nombreFichero,l);

		//ahora vamos a agregar el fichero al almacenClienteFicheros
		//conseguimos la lista de ficheros de este cliente:
		List<Integer> listaFicheros = almacenClienteFicheros.get(idCliente);
		if(! listaFicheros.contains(m.getId())){
			listaFicheros.add(m.getId());
			almacenFicheros.add(m);
			//esta linea hay que borrarla
			//System.out.println(almacenFicheros);
		}

		else 
			Interfaz.imprime("El fichero ya estaba en la lista, no se han tomado medidas");
				
		return idCliente;
	}
	
	/**
	 * elimina un fichero de las tablas
	 * @param int idFichero el identificador del fichero
	 * @param int idSesionCliente el identificador de sesion del cliente
	 * @return int devuelve -1 si el fichero no es del cliente, 0 si no se ha encontrado, el idCliente en otro caso
	 */
	@Override
	public int eliminarFichero(int idFichero, int idSesionCliente) throws RemoteException {
		String nombreCliente = sesionCliente.get(idSesionCliente);
		int idCliente = almacenClienteId.get(nombreCliente);
		int retorno = 0;
		boolean encontrado = false;
		Iterator<Metadatos> it = almacenFicheros.iterator();
		while(it.hasNext() && !encontrado){
			Metadatos m = (Metadatos) it.next();
			if (m.getId() == idFichero) {
				encontrado = true;
				if (m.getIdCliente() == idCliente) {//es el fichero del cliente					
					//hay que borrar tambien del almacenClientesFicheros, todos los clientes con quien esta compartido
					borraDeAlmacenClienteFicheros(idFichero,m.getCompartidoCon());//pasamos la lista con quien esta compartido
					List<Integer> lista = almacenClienteFicheros.get(idCliente);//hay que borrar su propia entrada
					lista.remove(lista.indexOf(idFichero));
					System.out.println(almacenClienteFicheros);//vemos la lista ya no tiene el idFichero por ningun sitio
					it.remove();//borramos del metadatos el fichero
					retorno = idCliente;
				} else retorno = -1;
			}
		}
		return retorno;
	}

	/**
	 * borra todas las entradas de un fichero para los clientes que le pasamos
	 * @param idFichero el id del fichero a borrar
	 * @param listaClientes la lista de id donde estan los ficheros
	 */
	private void borraDeAlmacenClienteFicheros(int idFichero, List<Integer> listaClientes){
		System.out.println(almacenClienteFicheros);//vemos la lista antes del borrado
		for (Integer i : listaClientes){
			List<Integer> lista = almacenClienteFicheros.get(i);
			lista.remove(lista.indexOf(idFichero));
		}
		System.out.println(almacenClienteFicheros);//vemos la lista despues del borrado, queda la propia entrada de quien llama
	}

	/**
	 * devuelve la lsita de los ficheros de un cliente
	 * @param int el id sesion de cliente
	 * @return List<String> la lista de los ficheros
	 */
	@Override
	public List<String> listarFicherosCliente(int idSesionCliente) throws RemoteException {
		List<String> l = new ArrayList<String>();
		int idCliente = almacenClienteId.get(sesionCliente.get(idSesionCliente));//el id del cliente
		List<Integer> listaFicheros = almacenClienteFicheros.get(idCliente);
		for (Integer i: listaFicheros){
			//buscamos en la lista de Metadatos el fichero que tiene ese id
			//es lento pero no funciona y como no nos imponen restricciones
			for (Metadatos m: almacenFicheros){
				if (i == m.getId()){
					String s="";
					s = i + ".- " + m.getNombreFichero();
					if(m.getIdCliente() != idCliente) s = s + " - COMPARTIDO por " + almacenIdCliente.get(m.getIdCliente());
					l.add(s);
				}			
			}
			
		}
		

		return l;
	}

	/**
	 * comprueba si el fichero es del cliente y devuelve su metadatos
	 * @param int el id del fichero a descargar
	 * @param int el id sesion del cliente que lo queire bajar
	 * @return Metadatos los metadatos del fichero
	 */
	@Override
	public Metadatos descargarFichero(int idFichero, int idSesionCliente) throws RemoteException {
		int idCliente = almacenClienteId.get(sesionCliente.get(idSesionCliente));//quien va a recibir el fichero
		Metadatos md = null;
		List<Integer> listaFicheros = almacenClienteFicheros.get(idCliente);
		if (listaFicheros.contains(idFichero)){
			boolean encontrado = false;
			Iterator<Metadatos> it = almacenFicheros.iterator();
			while(it.hasNext() && ! encontrado){
				Metadatos m = it.next();
				if(m.getId() == idFichero) md = m;
			}						
		}
		return md;
	}

	/**
	 * devuelve el id unico de un cliente a partir del id sesion
	 * @param int el id sesion del cliente
	 * @return int el id unico del cliente
	 */
	@Override
	public int sesion2id(int idsesion){
		return almacenClienteId.get(sesionCliente.get(idsesion));
	}

	/**
	 * devueve los metadatos del id fichero que le pasamos
	 * @param int el id del fichero de quein queremos los metadatos
	 * @return Metadatos los metadatos del fichero
	 */
	@Override
	public Metadatos dameMetadatos(int idFichero){
		Metadatos m = null;
		boolean encontrado = false;
		Iterator<Metadatos>	it = almacenFicheros.iterator();
		while(it.hasNext() && !encontrado){
			m = it.next();
			if (idFichero == m.getId()){
				encontrado = true;
			}
		}
		return m;
	}
	
	/**
	 * realiza la comparticion de un fichero
	 * @param int el id del fichero que queremos comaprtir
	 * @param el nombre de cliente a quien queremos comaprtirle el fichero
	 * @param int el id sesion del cliente que quiere comartir el fichero
	 * @return boolean true si todo ha ido bien, el fichero es del cliente
	 */
	@Override
	public boolean compartirFichero(int idFichero, String nombreCliente, int idSesion) throws RemoteException {
		if (! almacenClienteId.containsKey(nombreCliente)) return false;
		int idClientePropietario = almacenClienteId.get(sesionCliente.get(idSesion));
		int idClienteCompartido = almacenClienteId.get(nombreCliente);
		
		boolean encontrado = false;
		Iterator<Metadatos> it = almacenFicheros.iterator();
		while (it.hasNext() && !encontrado){
			Metadatos m = (Metadatos) it.next();
			if(idFichero == m.getId()){
				encontrado = true;
				if(m.getIdCliente() != idClientePropietario){
					encontrado = false;//significa que ese fichero no es el del Propietario que quiere compartirlo
				}else {
					
					List<Integer> listaFicheros = almacenClienteFicheros.get(idClienteCompartido);
					if(! listaFicheros.contains(m.getId())){
						m.getCompartidoCon().add(idClienteCompartido);
						listaFicheros.add(m.getId());
					}						
					else Interfaz.imprime("El fichero " + m.getNombreFichero() +" ya estaba en la lista, no se han tomado medidas");
				}				
			}			
		}	
		return encontrado;
	}
		
}
