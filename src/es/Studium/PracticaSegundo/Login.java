package es.Studium.PracticaSegundo;

import java.awt.Button;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Label;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Login implements WindowListener, ActionListener //IMPLEMENTACIÓN DEL WINDOW LISTENER Y ACTION LISTENER
{
	//CREACION DE LA VENTANA DEL LOGIN
	Frame login = new Frame ("Login");
	Dialog dialogoLogin = new Dialog(login, "Error", true);

	Label labelUsuario = new Label ("Usuario");
	Label labelClave = new Label ("Clave");
	Label labelError = new Label ("ERROR: creedenciales incorrectas");

	TextField textoUsuario = new TextField (20);
	TextField textoClave = new TextField (20);

	Button botonEntrar = new Button ("Entrar");
	Button botonBorrar = new Button ("Borrar");

	//ELEMENTOS NECESARIOS PARA LA CONEXIÓN CON NUESTRA BASE DE DATOS
	String driver = "com.mysql.cj.jdbc.Driver";
	String url = "jdbc:mysql://localhost:3306/videoclub_programa?serverTimezone=UTC";//ESPECIFICAMOS LA URL
	String Login = "root";
	String password = "Studium2020;";
	String sentencia = "";
	String usuario;
	FicheroLog log = new FicheroLog();
	Connection connection = null;
	Statement statement = null;
	ResultSet rs = null;

	//AÑDIMOS LOS OBJETOS DEL LOGIN Y SUS CARACTERISTICAS
	public Login()
	{

		login.setLayout(new FlowLayout());

		login.add(labelUsuario);
		login.add(textoUsuario);
		login.add(labelClave);
		textoClave.setEchoChar('*');
		login.add(textoClave);
		textoUsuario.setText("Admin");
		textoClave.setText("Studium2021;");
		botonEntrar.addActionListener(this);
		login.add(botonEntrar);
		botonBorrar.addActionListener(this);
		login.add(botonBorrar);
		login.addWindowListener(this);

		login.setLocationRelativeTo(null);
		login.setSize(280,160);
		login.setResizable(false);
		login.setVisible(true);
	}

	public static void main(String[] args) 
	{}

	//AÑADIMOS FUNCIONALIDAD A LOS BOTONES CON EL ACTION PERFORMED
	public void actionPerformed(ActionEvent botonPulsado) 
	{
		//SI EL BOTON PULSADO ES ENTRAR:
		if(botonPulsado.getSource().equals(botonEntrar))
		{
			//SE CONECTARA CON NUESTRA BASE DE DATOS Y BUSCARA CON LA SENTENCIA ESPECIFICA
			connection = conectar();
			sentencia = "SELECT * FROM usuarios WHERE nombreUsuario='"
					+textoUsuario.getText()+"' AND contraseñaUsuario = SHA2('"
					+textoClave.getText()+"',256);";		
			try
			{

				statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
						ResultSet.CONCUR_READ_ONLY);
				rs=statement.executeQuery(sentencia);
				if(rs.next())
				{
					//SI EXISTE EN NUESTRA BASE DE DATOS EL USUARIO BUSCADO, SE MOSTRARA EL MENU PRINCIPAL
					String usuario = rs.getString("nombreUsuario");
					new Programa_Gestion(usuario);
				}
				else
				{
					//SI EL USUARIO NO EXISTE, MOSTRAMOS EL DIALOGO DE EROR

					dialogoLogin.setLayout(new FlowLayout());
					dialogoLogin.add(labelError);
					dialogoLogin.addWindowListener(this);
					dialogoLogin.setSize(250,100);
					dialogoLogin.setLocationRelativeTo(null);
					dialogoLogin.setResizable(false);
					dialogoLogin.setVisible(true);
				}

			}

			catch (SQLException sqle)
			{
				
			}

		}

		//DAMOS FUNCIONALIDAD AL BOTON DE CERRAR

		if (botonPulsado.getSource().equals(botonBorrar))
		{
			textoUsuario.selectAll();
			textoUsuario.setText("");
			textoClave.selectAll();
			textoClave.setText("");
			textoUsuario.requestFocus();
		}

	}

	//CERRAR EL DIALOGO DEL ERROR
	public void windowClosing(WindowEvent arg0) 
	{
		if(dialogoLogin.isActive())
		{
			dialogoLogin.setVisible(false);
		}
		else
		{
			System.exit(0);	
		}


	}

	@Override
	public void windowActivated(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowClosed(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowDeactivated(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowDeiconified(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowIconified(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowOpened(WindowEvent arg0) {

	}
	public Connection conectar()
	{
		try
		{
			//Cargar los controladores para el acceso a la BD
			Class.forName(driver);
			//Establecer la conexión con la BD Empresa
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