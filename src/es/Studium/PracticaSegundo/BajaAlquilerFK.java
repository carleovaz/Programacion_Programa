package es.Studium.PracticaSegundo;

import java.awt.Button;
import java.awt.Choice;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class BajaAlquilerFK implements WindowListener, ActionListener
{
	//CREAMOS EL FRAME Y SUS RESPECTIVOS OBJETOS
	Frame frameBajaAlquiler = new Frame("Baja de Pelicula");
	Label labelMensajeBajaPelicula = new Label("Selecciona al cliente y la pelicula:");
	Choice choClientes = new Choice();
	Choice choPeliculas = new Choice();
	Button botonBorrarAlquiler = new Button("Borrar");
	Dialog dialogSeguroAlquiler = new Dialog(frameBajaAlquiler, "AVISO", true);
	Label labelSeguroAlquiler = new Label("¿Está seguro de querer borrar esta pelicula?");
	Button botonSiSeguroAlquiler = new Button("Sí");
	Button botonNoSeguroAlquiler = new Button("No");
	Dialog dialogConfirmacionBajaAlquiler = new Dialog(frameBajaAlquiler, "Baja pelicula", true);
	Label labelConfirmacionBajaAlquiler = new Label("Baja de pelicula realizada");
	Label labelErrorAlquiler = new Label("Error al borrar.");

	BaseDeDatos bd;
	String sentencia = "";
	String usuario;
	FicheroLog log = new FicheroLog();
	Connection connection = null;
	Statement statement = null;
	ResultSet rs = null;

	public BajaAlquilerFK(String usuario) 
	{
		this.usuario = usuario;
		//CONECTAMOS A LA BASE DE DATOS
		bd = new BaseDeDatos();
		connection = bd.conectar();

		frameBajaAlquiler.setLayout(new FlowLayout());
		frameBajaAlquiler.add(labelMensajeBajaPelicula);
		bd = new BaseDeDatos();
		connection = bd.conectar();
		//SELECCIONAMOS LAS PELICULAS
		sentencia = "SELECT * FROM clientes";

		//CONECTAMOS A LA BASE DE DATOS
		try
		{
			//CREAMOS LA SENTENCIA
			statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			rs = statement.executeQuery(sentencia);
			choClientes.removeAll();
			choClientes.add("Selecciona un cliente");
			while(rs.next())
			{
				choClientes.add(rs.getInt("idCliente")
						+"-"+rs.getString("nombreCliente") +"-"+rs.getString("direccionCliente")
						+"-"+rs.getString("dniCliente")+"-"+rs.getString("correoCliente")+"\n");
			}
		}
		//EN EL CASO QUE FALLE
		catch (SQLException sqle)
		{
			labelErrorAlquiler.setText("Error en asignación");
		}

		bd = new BaseDeDatos();
		connection = bd.conectar();
		//BUSCAMOS EN PELICULAS
		sentencia = "SELECT * FROM Peliculas";
		try
		{
			statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			rs = statement.executeQuery(sentencia);
			choPeliculas.removeAll();
			choPeliculas.add("Selecciona una Pelicula");
			while(rs.next())
			{
				choPeliculas.add(rs.getInt("idPelicula")
						+"-"+rs.getString("nombrePelicula") +"-"+rs.getString("directorPelicula")
						+"-"+rs.getString("precioPelicula")+"-"+rs.getString("idPropietarioFK1")+"\n");
				
			}
		}
		//EN EL CASO QUE FALLE
		catch (SQLException sqle)
		{
			log.guardar(usuario, "La Pelicula no pudo ser borrada.");
			labelErrorAlquiler.setText("Error en Baja");
		}

		//AÑADIMOS EL FRAME DE DAR DE BAJA LA PELICULA
		frameBajaAlquiler.add(labelMensajeBajaPelicula);
		frameBajaAlquiler.add(choClientes);
		frameBajaAlquiler.add(choPeliculas);
		botonBorrarAlquiler.addActionListener(this);
		frameBajaAlquiler.add(botonBorrarAlquiler);

		frameBajaAlquiler.setSize(550,180);
		frameBajaAlquiler.setResizable(false);
		frameBajaAlquiler.setLocationRelativeTo(null);
		frameBajaAlquiler.addWindowListener(this);
		frameBajaAlquiler.setVisible(true);

		frameBajaAlquiler.setLayout(new FlowLayout());

	}

	@Override
	//FUNCIONALIDAD A LOS BOTONES
	public void actionPerformed(ActionEvent evento) 
	{
		if(evento.getSource().equals(botonBorrarAlquiler))
		{
			log.guardar(usuario, "Ha pulsado Borrar Pelicula");
			dialogSeguroAlquiler.setLayout(new FlowLayout());
			dialogSeguroAlquiler.addWindowListener(this);
			dialogSeguroAlquiler.setSize(270,100);
			dialogSeguroAlquiler.setResizable(false);
			dialogSeguroAlquiler.setLocationRelativeTo(null);
			dialogSeguroAlquiler.add(labelSeguroAlquiler);
			botonSiSeguroAlquiler.addActionListener(this);
			dialogSeguroAlquiler.add(botonSiSeguroAlquiler);
			botonNoSeguroAlquiler.addActionListener(this);
			dialogSeguroAlquiler.add(botonNoSeguroAlquiler);
			dialogSeguroAlquiler.setVisible(true);
		}

		else if(evento.getSource().equals(botonNoSeguroAlquiler))
		{
			log.guardar(usuario, "Ha pulsado el botón NO, ha cancelado el borrado");
			dialogSeguroAlquiler.setVisible(false);
		}

		else if(evento.getSource().equals(botonSiSeguroAlquiler))
		{
			log.guardar(usuario, "Ha pulsado el botón SI, ha borrado la pelicula");
			//CONECTAMOS A LA BASE DE DATOS
			bd = new BaseDeDatos();
			connection = bd.conectar();
			String[] elegidoClienteFK = choClientes.getSelectedItem().split("-");
			String[] elegidoPeliculaFK = choPeliculas.getSelectedItem().split("-");
			sentencia = "DELETE FROM alquileres WHERE idClientesFK2 =" + elegidoClienteFK[0] + " AND idPeliculasFK3 = "+ elegidoPeliculaFK[0];


			try
			{
				statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
						ResultSet.CONCUR_READ_ONLY);
				System.out.println(sentencia);
				log.guardar(usuario, sentencia);
				statement.executeUpdate(sentencia);
				labelConfirmacionBajaAlquiler.setText("Baja de Pelicula realizada, sino se elimino, no pertenecia a dicho Cliente.");
			}

			catch (SQLException sqle)
			{
				log.guardar(usuario, "La Pelicula no pudo ser borrada.");
				labelConfirmacionBajaAlquiler.setText("Error en Baja");
			}

			finally
			{
				dialogConfirmacionBajaAlquiler.setLayout(new FlowLayout());
				dialogConfirmacionBajaAlquiler.addWindowListener(this);
				dialogConfirmacionBajaAlquiler.setSize(450,100);
				dialogConfirmacionBajaAlquiler.setResizable(false);
				dialogConfirmacionBajaAlquiler.setLocationRelativeTo(null);
				dialogConfirmacionBajaAlquiler.add(labelConfirmacionBajaAlquiler);
				dialogConfirmacionBajaAlquiler.setVisible(true);
			}
		}

	}

	public void windowClosing(WindowEvent e) 
	{
		if(frameBajaAlquiler.isActive())
		{
			frameBajaAlquiler.setVisible(false);
		}
		else if(dialogSeguroAlquiler.isActive())
		{
			dialogSeguroAlquiler.setVisible(false);
		}
		else if(dialogConfirmacionBajaAlquiler.isActive())
		{
			dialogConfirmacionBajaAlquiler.setVisible(false);
			dialogSeguroAlquiler.setVisible(false);
			frameBajaAlquiler.setVisible(false);
		}
	}

	@Override
	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowClosed(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub

	}

}
