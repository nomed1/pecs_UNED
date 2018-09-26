/**
 * Clase Persistencia , tiene valores y metdoos static, contiene constantes con el nombre de los ficheros persistentes con la info del servidor y repos
 * 
 * 
 * @autor Buenaventura Salcedo Santos-Olmo, xpressmoviles@gmail.com
 * @version v1.20171011
 */
package nomed.common;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Persistencia {
	
    public static final String FALMACENIDCLIENTE          = "almacenIdCliente.dat";
    public static final String FALMACENCLIENTEID          = "alamcenClienteId.dat";
    public static final String FALMACENIDREPOSITORIO      = "almacenIdRepositorio.dat";
    public static final String FALMACENREPOSITORIOID      = "almacenRepositorioId.dat";
    public static final String FALMACENCLIENTEREPOSITORIO = "almacenClienteRepositorio.dat";
    public static final String FALMACENFICHEROS           = "almacenFicheros.dat";
    public static final String FALMACENCLIENTEFICHEROS    = "almacenClienteFicheros.dat";
    public static final String FLISTACARPETAS             = "listaCarpetas.dat"; //esta es del Repositiorio

    /**
     * Funcion que guarda un objeto
     * @param objeto Object el objeto que se va a guardar en fichero
     * @param ruta String con la ruta del fichero
     */
    public static void guardar(Object objeto,String ruta)
    {
        try{
            // Serializar un objeto de datos a un archivo, el true añade al final
            ObjectOutputStream fichero = new ObjectOutputStream(new FileOutputStream(ruta));
            fichero.writeObject(objeto);
            fichero.close();
            System.out.println("fichero guardado con exito: " + ruta);
            // Serializar un objeto de datos a un arreglo de bytes

        } catch (IOException exp) {
            System.out.println("Imposible guardar en el fichero");
        }
        
    }
    
    /**
     * Funcion que lee y crea todos los objetos
     * @param ruta String la ruta del fichero a leer
     * @return Object la coleccion leida que debe ser casteada
     */
    public static Object leer(String ruta)   
    {
        Object objeto = null;
        try{
            ObjectInputStream fichero= new ObjectInputStream(new FileInputStream(ruta));
            objeto = (Object) fichero.readObject();
            fichero.close();
            System.out.println("fichero cargado con exito: " + ruta);
           }catch (IOException exp) {
               exp.printStackTrace();
        //       System.out.println ("Fichero no entontrado");
           }catch (ClassNotFoundException exp) { //hay que tratar el error sino java casca al compilar
               System.out.println("Clase no encontrada"); 
           }
           return objeto;
    }
}
