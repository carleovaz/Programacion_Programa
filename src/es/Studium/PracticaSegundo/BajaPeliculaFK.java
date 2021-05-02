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

public class BajaPeliculaFK implements WindowListener, ActionListener
{
	//CREAMOS EL FRAME Y SUS RESPECTIVOS OBJETOS
	Frame frameBajaPelicula = new Frame("Baja de Pelicula");
	Label labelMensajeBajaPelicula = new Label("Selecciona la Pelicula y el propietario:");
	Choice choPeliculas = new Choice();
	Choice choPropietarios = new Choice();
	Button botonBorrarPelicula = new Button("Borrar");
	Dialog dialogSeguroPelicula = new Dialog(frameBajaPelicula, "AVISO", true);
	Label labelSeguroPelicula = new Label("¿Está seguro de querer borrar esta pelicula?");
	Button botonSiSeguroPelicula = new Button("Sí");
	Button botonNoSeguroPelicula = new Button("No");
	Dialog dialogConfirmacionBajaPelicula = new Dialog(frameBajaPelicula, "Baja pelicula", true);
	Label labelConfirmacionBajaPelicula = new Label("Baja de pelicula realizada");
	Label labelErrorPelicula = new Label("Error al borrar.");
	Button cancelar = new Button("cancelar");

	BaseDeDatos bd;
	String sentencia = "";
	String usuario;
	FicheroLog log = new FicheroLog();
	Connection connection = null;
	Statement statement = null;
	ResultSet rs = null;

	public BajaPeliculaFK(String usuario) 
	{
		this.usuario = usuario;
		//CONECTAMOS A LA BASE DE DATOS
		bd = new BaseDeDatos();
		connection = bd.conectar();

		frameBajaPelicula.setLayout(new FlowLayout());
		frameBajaPelicula.add(labelMensajeBajaPelicula);
		bd = new BaseDeDatos();
		connection = bd.conectar();
		//SELECCIONAMOS EL PROPIETARIO
		sentencia = "SELECT * FROM Propietario";
		try
		{
			//CREAMOS SENTENCIA
			statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			rs = statement.executeQuery(sentencia);
			choPropietarios.removeAll();
			choPropietarios.add("Seleccionar un propietario");
			while(rs.next())
			{
				//BUSCAMOS LOS DATOS DE LOS PROPIETARIOS
				choPropietarios.add(rs.getInt("idPropietario")
						+"-"+rs.getString("nombrePropietario") +"-"+rs.getString("direccionPropietario")
						+"-"+rs.getString("telefonoPropietario")+"-"+rs.getString("dniPropietario"));
			}
		}

		catch (SQLException sqle)
		{		
			
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

		catch (SQLException sqle)
		{
			log.guardar(usuario, "La Pelicula no pudo ser borrada.");
			labelConfirmacionBajaPelicula.setText("Error en Baja");
		}

		//AÑADIMOS EL FRAME DE DAR DE BAJA LA PELICULA
		frameBajaPelicula.add(labelMensajeBajaPelicula);
		frameBajaPelicula.add(choPeliculas);
		frameBajaPelicula.add(choPropietarios);
		botonBorrarPelicula.addActionListener(this);
		frameBajaPelicula.add(botonBorrarPelicula);
		frameBajaPelicula.add(cancelar);
		cancelar.addActionListener(this);

		frameBajaPelicula.setSize(450,180);
		frameBajaPelicula.setResizable(false);
		frameBajaPelicula.setLocationRelativeTo(null);
		frameBajaPelicula.addWindowListener(this);
		frameBajaPelicula.setVisible(true);

		frameBajaPelicula.setLayout(new FlowLayout());

	}

	@Override
	//FUNCIONALIDAD A LOS BOTONES
	public void actionPerformed(ActionEvent evento) 
	{
		if(evento.getSource().equals(botonBorrarPelicula))
		{
			log.guardar(usuario, "Ha pulsado Borrar Pelicula");
			dialogSeguroPelicula.setLayout(new FlowLayout());
			dialogSeguroPelicula.addWindowListener(this);
			dialogSeguroPelicula.setSize(270,100);
			dialogSeguroPelicula.setResizable(false);
			dialogSeguroPelicula.setLocationRelativeTo(null);
			dialogSeguroPelicula.add(labelSeguroPelicula);
			botonSiSeguroPelicula.addActionListener(this);
			dialogSeguroPelicula.add(botonSiSeguroPelicula);
			botonNoSeguroPelicula.addActionListener(this);
			dialogSeguroPelicula.add(botonNoSeguroPelicula);
			dialogSeguroPelicula.setVisible(true);
			
		}

		else if(evento.getSource().equals(botonNoSeguroPelicula))
		{
			log.guardar(usuario, "Ha pulsado el botón NO, ha cancelado el borrado");
			dialogSeguroPelicula.setVisible(false);
		}

		else if(evento.getSource().equals(botonSiSeguroPelicula))
		{
			log.guardar(usuario, "Ha pulsado el botón SI, ha borrado la pelicula");
			//CONECTAMOS A LA BASE DE DATOS
			bd = new BaseDeDatos();
			connection = bd.conectar();
			String[] elegidoPro = choPropietarios.getSelectedItem().split("-");
			String[] elegidoPeli = choPeliculas.getSelectedItem().split("-");

			//SENTENCIA DE BORRADO DENTRO DE LA TABLA PELCULAS:
			/*
			 * AÑADIR EL NOMBRE DE LA PELICULA PARA QUE NO SE ELIMINEN TODAS LAS PELICULAS ENLAZADAS AL PROPIETARIO
			 * YA QUE DE LO CONTRARIO, SI SOLO ENLAZAMOS SU FK, NOS ELIMINARA AUTOMATICAMENTE, TODAS LAS PELICULAS DE DICHO PROPIETARIO
			 */
			sentencia = "DELETE FROM peliculas WHERE nombrePelicula =" + "\"" + elegidoPeli[1] + "\"" + " AND idPropietarioFK1 = "+elegidoPro[0];
			try
			{
				statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
						ResultSet.CONCUR_READ_ONLY);
				System.out.println(sentencia);
				log.guardar(usuario, sentencia);
				statement.executeUpdate(sentencia);
				labelConfirmacionBajaPelicula.setText("Baja de Pelicula realizada, sino se elimino, no pertenecia a dicho propietario.");
			}

			catch (SQLException sqle)
			{
				log.guardar(usuario, "La Pelicula no pudo ser borrada.");
				labelConfirmacionBajaPelicula.setText("Error en Baja");
			}

			finally
			{
				dialogConfirmacionBajaPelicula.setLayout(new FlowLayout());
				dialogConfirmacionBajaPelicula.addWindowListener(this);
				dialogConfirmacionBajaPelicula.setSize(450,100);
				dialogConfirmacionBajaPelicula.setResizable(false);
				dialogConfirmacionBajaPelicula.setLocationRelativeTo(null);
				dialogConfirmacionBajaPelicula.add(labelConfirmacionBajaPelicula);
				dialogConfirmacionBajaPelicula.setVisible(true);
			}
		}
		else if (evento.getSource().equals(cancelar))
		{
			log.guardar(usuario, "Ha pulsado Cancelar Baja Pelicula.");
			if(frameBajaPelicula.isActive())
			{
				frameBajaPelicula.setVisible(false);
			}
			else
			{
				System.exit(0);	
			}
		}


	}
	
	public void windowClosing(WindowEvent e) 
	{
		log.guardar(usuario, "Ha salido de Baja Pelicula.");
		if(frameBajaPelicula.isActive())
		{
			frameBajaPelicula.setVisible(false);
		}
		else if(dialogSeguroPelicula.isActive())
		{
			dialogSeguroPelicula.setVisible(false);
		}
		else if(dialogConfirmacionBajaPelicula.isActive())
		{
			dialogConfirmacionBajaPelicula.setVisible(false);
			dialogSeguroPelicula.setVisible(false);
			frameBajaPelicula.setVisible(false);
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
