/**
 * Clase Interfaz, con metodos estatitos para imprimir menus, captar datos e imprimir/guardar mensajes en pantalla/ficheroLog
 * 
 * @autor Buenaventura Salcedo Santos-Olmo, xpressmoviles@gmail.com
 * @version v1.20171006
 */
package nomed.common;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

public class Interfaz {

	/**
	 * muestra un menu y recoge una opcion
	 * @param titulo el titulo del menu 
	 * @param opciones un vector de strings con las opciones a mostrar
	 * @return int con el número de opcion elegido
	 */
	public static int menu(String titulo,String[] opciones){
		System.out.println("\nMenu " + titulo);
		System.out.println("-----------------");
		for (int i = 0; i < opciones.length; i++) {
			System.out.println((i+1) + ".- " + opciones[i]);
		}
		System.out.println(opciones.length + 1 + ".- Salir");
		System.out.println("Selecione una opcion > ");
		Scanner opcion = new Scanner(System.in);
		return opcion.nextInt();
	}
	
	/**
	 * lectura para de un datos a traves de teclado
	 * @param titulo el mensaje a mostrar para la peticion del dato a leer
	 * @return String con el dato leido por teclado
	 */
	public static String pideDato(String titulo){
		System.out.println("\n " + titulo + " > ");
		Scanner s = new Scanner(System.in);
		return s.nextLine();	
	}
	
	/**
	 * imprime en pantalla y en el fichero del log, cada dia de ejecucion se crea el fichero si no existe usando la fecha actual
	 * @param mensaje
	 * @param fichero
	 * @throws IOException
	 */
	public static void imprime(String mensaje){
	BufferedWriter salida = null;
	System.out.println(mensaje);
	Calendar c = Calendar.getInstance();
    String dia = Integer.toString(c.get(Calendar.DATE));
    String mes = Integer.toString(c.get(Calendar.MONTH)+1);
    String anio = Integer.toString(c.get(Calendar.YEAR));
	try {   
	   	salida = new BufferedWriter(new FileWriter("log" + anio + mes + dia +".txt", true));   
	   	salida.write("" + c.get(Calendar.DATE)+ "/" + (c.get(Calendar.MONTH)+1) + "/" + c.get(Calendar.YEAR) + " " +
	   				c.get(Calendar.HOUR) + ":" + c.get(Calendar.MINUTE)+":"+ c.get(Calendar.SECOND) + " : " + mensaje + "\r\n");
	   	salida.close();
	} catch (IOException e) {   
	    System.out.println("Error al escribir en fichero Log");   
		} 
	}
}