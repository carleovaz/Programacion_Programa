package es.Studium.PracticaSegundo;

import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class Programa_Gestion implements WindowListener, ActionListener
{
	//VENTANA PRINCIPAL 
	Frame ventanaPrincipal = new Frame("Videocblub");

	//MENUS Y MENU ITEM
	MenuBar barraMenu = new MenuBar();

	Menu menuClientes = new Menu ("Clientes");
	MenuItem menuItemAltaCliente = new MenuItem("Alta");
	MenuItem menuItemBajaCliente = new MenuItem("Baja");
	MenuItem menuItemModificacionCliente = new MenuItem("Modificación");
	MenuItem menuItemConsultaCliente = new MenuItem("Consulta");

	Menu menuPeliculas = new Menu ("Peliculas");
	MenuItem menuItemAltaPelicula = new MenuItem("Alta");
	MenuItem menuItemConsultaPelicula = new MenuItem("Consulta");

	Menu menuAlquileres = new Menu ("Alquileres");
	MenuItem menuItemAltaAlquiler = new MenuItem("Alta");
	MenuItem menuItemConsultaAlquiler = new MenuItem("Consulta");

	Menu menuPropietario = new Menu("Propietario");
	MenuItem menuItemAltaPropietario = new MenuItem("Alta");
	MenuItem menuItemBajaPropietario = new MenuItem("Baja");
	MenuItem menuItemModificacionPropietario = new MenuItem("Modificación");
	MenuItem menuItemConsultaPropietario = new MenuItem("Consulta");	

	//CONEXION A LA BASE DE DATOS
	BaseDeDatos bd;
	String sentencia = "";
	String usuario;
	FicheroLog log = new FicheroLog();
	Connection connection = null;
	Statement statement = null;
	ResultSet rs = null;

	//PARAMETROS Y OBJETOS DE PROGRAMA_GESTION
	public Programa_Gestion(String usuario)
	{
		//ESTABLECEMOS EL LAYOUT
		ventanaPrincipal.setLayout(new FlowLayout());
		//AÑADIMOS LOS OBJETOS Y CARACTERISTICAS AL MENU
		ventanaPrincipal.setMenuBar(barraMenu);
		if(usuario.equals("Admin"))//SI ES ADMIN
		{
			barraMenu.add(menuClientes);
			menuItemAltaCliente.addActionListener(this);
			menuClientes.add(menuItemAltaCliente);
			menuItemBajaCliente.addActionListener(this);
			menuClientes.add(menuItemBajaCliente);
			menuItemConsultaCliente.addActionListener(this);
			menuClientes.add(menuItemConsultaCliente);
			menuItemModificacionCliente.addActionListener(this);
			menuClientes.add(menuItemModificacionCliente);

			barraMenu.add(menuPeliculas);
			menuItemAltaPelicula.addActionListener(this);
			menuPeliculas.add(menuItemAltaPelicula);
			menuItemConsultaPelicula.addActionListener(this);
			menuPeliculas.add(menuItemConsultaPelicula);

			barraMenu.add(menuAlquileres);
			menuItemAltaAlquiler.addActionListener(this);
			menuAlquileres.add(menuItemAltaAlquiler);
			menuItemConsultaAlquiler.addActionListener(this);
			menuAlquileres.add(menuItemConsultaAlquiler);

			barraMenu.add(menuPropietario);
			menuItemAltaPropietario.addActionListener(this);
			menuPropietario.add(menuItemAltaPropietario);
			menuItemBajaPropietario.addActionListener(this);
			menuPropietario.add(menuItemBajaPropietario);
			menuItemConsultaPropietario.addActionListener(this);
			menuPropietario.add(menuItemConsultaPropietario);
			menuItemModificacionPropietario.addActionListener(this);
			menuPropietario.add(menuItemModificacionPropietario);
		}
		else if(usuario.equals("Usuario"))//SI ES USUARIO
		{
			barraMenu.add(menuClientes);
			menuItemAltaCliente.addActionListener(this);
			menuClientes.add(menuItemAltaCliente);

			barraMenu.add(menuPeliculas);
			menuItemAltaPelicula.addActionListener(this);
			menuPeliculas.add(menuItemAltaPelicula);

			barraMenu.add(menuAlquileres);
			menuItemAltaAlquiler.addActionListener(this);
			menuAlquileres.add(menuItemAltaAlquiler);

			barraMenu.add(menuPropietario);
			menuItemAltaPropietario.addActionListener(this);
			menuPropietario.add(menuItemAltaPropietario);
		}

		//AÑADIMOS FUNCIONALIDAD A LA VENTANA PRINCIPAL
		ventanaPrincipal.setSize(300,200);
		ventanaPrincipal.setVisible(true);
		ventanaPrincipal.setResizable(false);
		ventanaPrincipal.setLocationRelativeTo(null);
		ventanaPrincipal.addWindowListener(this);

	}
	public static void main(String[] args) 
	{
		new Login();

	}
	@Override
	public void actionPerformed(ActionEvent evento) 
	{
		//MENU ITEM CLIENTE
		//ALTA CLIENTE
		if(evento.getSource().equals(menuItemAltaCliente))
		{
			new AltaCliente();
		}
		//BAJA CLIENTE
		else if(evento.getSource().equals(menuItemBajaCliente))
		{
			new BajaCliente();
		}
		//CONSULTA CLIENTE
		else if(evento.getSource().equals(menuItemConsultaCliente))
		{
			new ConsultaCliente();
		}
		//MODIFICACIÓN CLIENTE
		else if(evento.getSource().equals(menuItemModificacionCliente))
		{
			new ModificacionCliente();
		}
		//MENU PROPIETARIO
		//ALTA PROPIETARIO		
		if(evento.getSource().equals(menuItemAltaPropietario))
		{
			new AltaPropietario();
		}
		//BAJA PROPIETARIO
		else if(evento.getSource().equals(menuItemBajaPropietario))
		{
			new BajaPropietario();
		}
		//CONSULTA PROPIETARIO
		else if(evento.getSource().equals(menuItemConsultaPropietario))
		{
			new ConsultaPropietario();
		}
		//MODIFICACION PROPIETARIO
		else if(evento.getSource().equals(menuItemModificacionPropietario))
		{
			new ModificacionPropietario();
		}
		//PELICULAS
		//ALTA PELICULAS
		else if(evento.getSource().equals(menuItemAltaPelicula))
		{
			new AltaPeliculaFK();
		}
		//CONSULTA PELICULAS
		else if(evento.getSource().equals(menuItemConsultaPelicula))
		{
			new ConsultaPeliculaFK();
		}
		//ALQUILERES
		//ALTA ALQUILER
		else if(evento.getSource().equals(menuItemAltaAlquiler))
		{
			new AltaAlquilerFK();
		}
		
		//CONSULTA ALQUILER
		else if(evento.getSource().equals(menuItemConsultaAlquiler))
		{
			new ConsultaAlquiler();
		}
	}

	@Override
	public void windowClosing(WindowEvent arg0) 

	{
		if(ventanaPrincipal.isActive())
		{
			ventanaPrincipal.setVisible(false);
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
		// TODO Auto-generated method stub

	}

}