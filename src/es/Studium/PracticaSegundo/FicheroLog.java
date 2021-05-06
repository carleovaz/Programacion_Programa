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
		//AÑADIMOS EL OBJETO FECHA
		Date fechaActual = new Date();
		//AÑADIMOS EL FORMATO CON EL QUE QUEREMOS QUE SALGA LA FECHA
		SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		try
		{
			//LUGAR DONDE IRAN TODOS LOS MOVIMIENTOS DE LOS USUARIOS
			FileWriter fw = new FileWriter("Log.log", true);
			//BUFFER ESCRITURA
			BufferedWriter bw = new BufferedWriter(fw);
			//OBJETO PARA LA ESCRITURA
			PrintWriter salida = new PrintWriter(bw);
			//VAMOS GUARDANDO LAS LINEAS
			salida.print("[" + formato.format(fechaActual) + "]");
			salida.print("[" + usuario + "]");
			salida.println("[" + mensaje + "]");
			//CIERRES
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
