package es.Studium.PracticaSegundo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class BaseDeDatos 
{
	String driver = "com.mysql.cj.jdbc.Driver";
    String url = "jdbc:mysql://localhost:3306/videoclub_programa?serverTimezone=UTC";//ESPECIFICAMOS LA URL
    String Login = "root";
    String password = "Studium2020;";
	String sentencia = "";
	Connection connection = null;
	Statement statement = null;
	ResultSet rs = null;

	public BaseDeDatos()
	{

	}

	public Connection conectar()
	{
		try
		{
			//CARGAMOS LOS CONTROLADORES PARA EL ACCESO A LA BASE DE DATOS
			Class.forName(driver);
			//ESTABLECEMOS LA CONEXIÓN
			connection = DriverManager.getConnection(url, Login, password);
		}
		catch (ClassNotFoundException cnfe)
		{
			System.out.println("Error 1-"+cnfe.getMessage());
		}
		catch (SQLException sqle)
		{
			System.out.println("Error 2-"+sqle.getMessage());
		}
		return connection;
	}

	public void desconectar(Connection con)
	{
		try
		{
			con.close();
		} 
		catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}