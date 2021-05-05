/*
 * Estructura principal de Programa_Gestion
 */
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
	MenuItem menuItemBajaPelicula = new MenuItem("Baja");
	MenuItem menuItemModificacionPelicula = new MenuItem("Modificación");
	MenuItem menuItemConsultaPelicula = new MenuItem("Consulta");

	Menu menuAlquileres = new Menu ("Alquileres");
	MenuItem menuItemAltaAlquiler = new MenuItem("Alta");
	MenuItem menuItemBajaAlquiler = new MenuItem ("Baja");
	MenuItem menuItemModificacionAlquiler = new MenuItem ("Modificación");
	MenuItem menuItemConsultaAlquiler = new MenuItem("Consulta");

	Menu menuPropietario = new Menu("Propietario");
	MenuItem menuItemAltaPropietario = new MenuItem("Alta");
	MenuItem menuItemBajaPropietario = new MenuItem("Baja");
	MenuItem menuItemModificacionPropietario = new MenuItem("Modificación");
	MenuItem menuItemConsultaPropietario = new MenuItem("Consulta");
	
	Menu menuAyuda = new Menu ("Ayuda");
	MenuItem menuItemAyuda = new MenuItem("Manual");

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
		this.usuario = usuario;
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
			menuItemBajaPelicula.addActionListener(this);
			menuPeliculas.add(menuItemBajaPelicula);
			menuItemConsultaPelicula.addActionListener(this);
			menuPeliculas.add(menuItemConsultaPelicula);
			menuItemModificacionPelicula.addActionListener(this);
			menuPeliculas.add(menuItemModificacionPelicula);

			barraMenu.add(menuAlquileres);
			menuItemAltaAlquiler.addActionListener(this);
			menuAlquileres.add(menuItemAltaAlquiler);
			menuItemBajaAlquiler.addActionListener(this);
			menuAlquileres.add(menuItemBajaAlquiler);

			menuItemConsultaAlquiler.addActionListener(this);
			menuAlquileres.add(menuItemConsultaAlquiler);
			menuItemModificacionAlquiler.addActionListener(this);
			menuAlquileres.add(menuItemModificacionAlquiler);

			barraMenu.add(menuPropietario);
			menuItemAltaPropietario.addActionListener(this);
			menuPropietario.add(menuItemAltaPropietario);
			menuItemBajaPropietario.addActionListener(this);
			menuPropietario.add(menuItemBajaPropietario);
			menuItemConsultaPropietario.addActionListener(this);
			menuPropietario.add(menuItemConsultaPropietario);
			menuItemModificacionPropietario.addActionListener(this);
			menuPropietario.add(menuItemModificacionPropietario);
			
			barraMenu.add(menuAyuda);
			menuItemAyuda.addActionListener(this);
			menuAyuda.add(menuItemAyuda);
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
			
			barraMenu.add(menuAyuda);
			menuItemAyuda.addActionListener(this);
			menuAyuda.add(menuItemAyuda);
		}

		//AÑADIMOS FUNCIONALIDAD A LA VENTANA PRINCIPAL
		ventanaPrincipal.setSize(350,250);
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
			log.guardar(usuario, "Ha clicado en Alta Cliente.");
			new AltaCliente(usuario);	
		}
		//BAJA CLIENTE
		else if(evento.getSource().equals(menuItemBajaCliente))
		{
			log.guardar(usuario, "Ha clicado en Baja Cliente.");
			new BajaCliente(usuario);
		}
		//CONSULTA CLIENTE
		else if(evento.getSource().equals(menuItemConsultaCliente))
		{
			log.guardar(usuario, "Ha clicado en Consulta Cliente.");
			new ConsultaCliente(usuario);
		}
		//MODIFICACIÓN CLIENTE
		else if(evento.getSource().equals(menuItemModificacionCliente))
		{
			log.guardar(usuario, "Ha clicado en Modificación Cliente.");
			new ModificacionCliente(usuario);
		}
		
		//MENU PROPIETARIO
		//ALTA PROPIETARIO		
		if(evento.getSource().equals(menuItemAltaPropietario))
		{
			log.guardar(usuario, "Ha clicado en Alta Propietario.");
			new AltaPropietario(usuario);
		}
		//BAJA PROPIETARIO
		else if(evento.getSource().equals(menuItemBajaPropietario))
		{
			log.guardar(usuario, "Ha clicado en Baja Propietario.");
			new BajaPropietario(usuario);
		}
		//CONSULTA PROPIETARIO
		else if(evento.getSource().equals(menuItemConsultaPropietario))
		{
			log.guardar(usuario, "Ha clicado en Consulta Propietario.");
			new ConsultaPropietario(usuario);
		}
		//MODIFICACION PROPIETARIO
		else if(evento.getSource().equals(menuItemModificacionPropietario))
		{
			log.guardar(usuario, "Ha clicado en Modificación Propietario.");
			new ModificacionPropietario(usuario);
		}
		
		//PELICULAS
		//ALTA PELICULAS
		else if(evento.getSource().equals(menuItemAltaPelicula))
		{
			log.guardar(usuario, "Ha clicado en Alta Pelicula.");
			new AltaPeliculaFK(usuario);
		}
		//BAJA PELICULAS
		else if(evento.getSource().equals(menuItemBajaPelicula))
		{
			log.guardar(usuario, "Ha clicado en Baja Pelicula.");
			new BajaPeliculaFK(usuario);
		}
		//CONSULTA PELICULAS
		else if(evento.getSource().equals(menuItemConsultaPelicula))
		{
			log.guardar(usuario, "Ha clicado en Consulta Cliente.");
			new ConsultaPeliculaFK(usuario);
		}
		//MODIFICACION PELICULAS
		else if(evento.getSource().equals(menuItemModificacionPelicula))
		{
			log.guardar(usuario, "Ha clicado en Modificacion Pelicula.");
			new ModificacionPelicula(usuario);
		}
		
		//ALQUILERES
		//ALTA ALQUILER
		else if(evento.getSource().equals(menuItemAltaAlquiler))
		{
			log.guardar(usuario, "Ha clicado en Alta Alquiler.");
			new AltaAlquilerFK(usuario);
		}
		
		//BAJA ALQUILER
		else if(evento.getSource().equals(menuItemBajaAlquiler))
		{
			log.guardar(usuario, "Ha clicado en Baja Alquiler.");
			new BajaAlquilerFK(usuario);
		}
		
		//CONSULTA ALQUILER
		else if(evento.getSource().equals(menuItemConsultaAlquiler))
		{
			log.guardar(usuario, "Ha clicado en Consulta Alquiler.");
			new ConsultaAlquilerFK(usuario);
		}
		
		//MODIFICACION ALQUILER
		else if(evento.getSource().equals(menuItemModificacionAlquiler))
		{
			log.guardar(usuario, "Ha clicado en Modificacion Alquiler.");
			new ModificacionAlquiler(usuario);
		}
		
		//MENU AYUDA ACCESO MANUAL
		else if(evento.getSource().equals(menuItemAyuda))
		{
			log.guardar(usuario, "Ha accedido al manual de ayuda.");
			new Ayuda(usuario);
		}
		

	}

	@Override
	public void windowClosing(WindowEvent arg0) 

	{
		
		if(ventanaPrincipal.isActive())
		{	
			log.guardar(usuario, "Ha cerrado sesión");
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