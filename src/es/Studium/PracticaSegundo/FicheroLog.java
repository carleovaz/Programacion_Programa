/*
 * Estructura de FicheroLog
 */
package es.Studium.PracticaSegundo;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FicheroLog 
{
	public void guardar(String usuario, String mensaje)
	{
		Date fechaActual = new Date();
		SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		//FileWriter también puede lanzar una excepción
		try
		{
			// Destino de los datos
			FileWriter fw = new FileWriter("Log.log", true);
			// Buffer de escritura
			BufferedWriter bw = new BufferedWriter(fw);
			// Objeto para la escritura
			PrintWriter salida = new PrintWriter(bw);
			//Guardamos la primera línea
			salida.print("[" + formato.format(fechaActual) + "]");
			salida.print("[" + usuario + "]");
			salida.println("[" + mensaje + "]");
			//Cerrar el objeto salida, el objeto bw y el fw
			salida.close();
			bw.close();
			fw.close();
		}
		catch(IOException i)
		{
			System.out.println("Se produjo un error de Archivo");
		}
	}
}
