package es.Studium.PracticaSegundo;

import java.io.IOException;

public class Ayuda 
{
	public Ayuda(String usuario)
	{
		
	}
	public static void main(String[] args)
	{ 
		try 
		{ 
			//el exe es fundamental
			Runtime.getRuntime().exec("hh.exe Segundo.chm"); 
		} 
		catch (IOException e) 
		{ 
			e.printStackTrace(); 
		} 
	} 
}
