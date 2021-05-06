package es.Studium.PracticaSegundo;

import java.io.IOException;

public class Ayuda 
{
	String usuario;
	FicheroLog log = new FicheroLog();
	
	public Ayuda(String usuario)
	{
		this.usuario = usuario;
		try 
		{ 
			//EJECUTA EL ARCHIVO DE AYUDA
			Runtime.getRuntime().exec("hh.exe Ayuda.chm"); 
		} 
		catch (IOException e) 
		{ 
			e.printStackTrace(); 
		} 
	}
 
}
