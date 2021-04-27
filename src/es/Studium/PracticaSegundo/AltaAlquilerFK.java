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

public class AltaAlquilerFK implements WindowListener, ActionListener
{
	//CREAMOS EL FRAME Y SUS RESPECTIVOS OBJETOS
	Frame frameAltaAlquiler = new Frame("Alta Alquiler");
	Label labelCliente = new Label ("Cliente");
	Choice choClientes = new Choice();
	Label labelPelicula = new Label ("Pelicula");
	Choice choPeliculas = new Choice();
	Button botonAceptarAltaAlquiler = new Button ("Aceptar");
	Button botonCancelarAltaAlquiler = new Button ("Cancelar");
	Dialog dialogoMensajeAltaAlquiler = new Dialog(frameAltaAlquiler, "Confirmación", true);
	Label labelMensajeAlta = new Label ("Asignación de Alquiler exitosa");

	BaseDeDatos bd;
	String sentencia = "";
	String usuario;
	FicheroLog log = new FicheroLog();
	Connection connection = null;
	Statement statement = null;
	ResultSet rs = null;

	//CONSTRUCTOR, INCLUIMOS LOS ELEMENTOS
	public AltaAlquilerFK(String usuario)
	{
		this.usuario = usuario;
		frameAltaAlquiler.setLayout(new FlowLayout());
		
		frameAltaAlquiler.add(labelCliente);
		frameAltaAlquiler.add(choClientes);
		frameAltaAlquiler.add(labelPelicula);
		frameAltaAlquiler.add(choPeliculas);
		botonAceptarAltaAlquiler.addActionListener(this);
		frameAltaAlquiler.add(botonAceptarAltaAlquiler);
		botonCancelarAltaAlquiler.addActionListener(this);
		frameAltaAlquiler.add(botonCancelarAltaAlquiler);
		
		frameAltaAlquiler.setSize(620,180);
		frameAltaAlquiler.setResizable(false);
		frameAltaAlquiler.setVisible(true);
		frameAltaAlquiler.setLocationRelativeTo(null);
		frameAltaAlquiler.addWindowListener(this);

		bd = new BaseDeDatos();
		connection = bd.conectar();
		//BUSCAMOS EN CLIENTES
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
			labelMensajeAlta.setText("Error en asignación");
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
			labelMensajeAlta.setText("Error en asignación");
		}
		
		finally
		{	}

	}

	@Override
	//FUNCIONALIDAD DE LOS BOTONES
	public void actionPerformed(ActionEvent evento) 
	{
		if(botonAceptarAltaAlquiler.equals(evento.getSource()))
		{
			bd = new BaseDeDatos();
			connection = bd.conectar();
			try
			{
				statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
						ResultSet.CONCUR_READ_ONLY);
				sentencia="INSERT INTO Alquileres VALUES(null,'"
						+choClientes.getSelectedItem().split("-")[0]+"',"
						+choPeliculas.getSelectedItem().split("-")[0]
								+")";
				statement.executeUpdate(sentencia);
				labelMensajeAlta.setText("¡Alta de Alquiler Completada!");
			}
			
			catch (SQLException sqle)
			{
				labelMensajeAlta.setText("Error en asignación");
			}

			finally
			{
				dialogoMensajeAltaAlquiler.setLayout(new FlowLayout());
				dialogoMensajeAltaAlquiler.addWindowListener(this);
				dialogoMensajeAltaAlquiler.setSize(280,100);
				dialogoMensajeAltaAlquiler.setResizable(false);
				dialogoMensajeAltaAlquiler.setLocationRelativeTo(null);
				dialogoMensajeAltaAlquiler.add(labelMensajeAlta);
				dialogoMensajeAltaAlquiler.setVisible(true);
				bd.desconectar(connection);
			}
		}
		
		else if (evento.getSource().equals(botonCancelarAltaAlquiler)) 
		{
			labelMensajeAlta.setText("Faltan datos");
		}

		else if (evento.getSource().equals(botonCancelarAltaAlquiler)) 
		{
			frameAltaAlquiler.setVisible(false);
		}

	}

	@Override
	public void windowClosing(WindowEvent e) 
	{
		frameAltaAlquiler.setVisible(false);

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

	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub

	}
}