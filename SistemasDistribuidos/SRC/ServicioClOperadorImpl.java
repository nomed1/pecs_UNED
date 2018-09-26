package nomed.repositorio;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import nomed.common.Fichero;
import nomed.common.ServicioClOperadorInterface;

public class ServicioClOperadorImpl extends UnicastRemoteObject implements ServicioClOperadorInterface{


	/**
	 * 
	 */
	private static final long serialVersionUID = 2546373861790720564L;

	/**
	 * constructor de la clase
	 * @throws RemoteException
	 */
	protected ServicioClOperadorImpl() throws RemoteException {
		super();
	}

	/**
	 * sube el fichero a repositorio
	 * Vamos a usar la misma tecnica que el ejemplo del foro de la asignatura
	 * @param fichero Fichero el fichero con metadatos
	 * @return boolean true si todo ha sido correcto, false en caso contrario
	 */
	@Override
	public boolean subirFichero(Fichero fichero) throws RemoteException {
		OutputStream os;
		String nombreFichero = fichero.obtenerPropietario()+ File.separator + fichero.obtenerNombre();
		
		try {
			os = new FileOutputStream(nombreFichero); 
			if (fichero.escribirEn(os)==false)
			{
				os.close();
				return false;
			}
			os.close();
			System.out.println("Fichero " + nombreFichero +" recibido y guardado");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}	
		return true;
	}

	/**
	 * borra un fichero de la carpeta indicada
	 * @param fichero String el nombre del fichero a borrar
	 * @param carpeta String el nombre de la carpeta donde esta el fichero
	 * @return boolean true si el fichero ha sido borrado, false en caso contrario
	 */
	@Override
	public boolean borrarFichero(String fichero, String carpeta) throws RemoteException {
		File f = new File(carpeta + File.separator + fichero);
        boolean borrado = f.delete();
        if (borrado) System.out.println("Se ha borrado el fichero " + carpeta + File.separator + fichero);
        else System.out.println("No se ha podido borrar el fichero " + carpeta + File.separator + fichero);
		return borrado;
		
	}

}
