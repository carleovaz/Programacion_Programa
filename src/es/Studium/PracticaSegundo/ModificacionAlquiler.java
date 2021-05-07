package es.Studium.PracticaSegundo;

import java.awt.Button;
import java.awt.Choice;
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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ModificacionAlquiler implements ActionListener, WindowListener
{
	//FRAME DE MODIFICACIÓN ALQUILER
	Frame frameModificacionAlquiler = new Frame("Modificación");
	Label  labelMensajeModificarAlquiler = new Label("Selecciona el alquiler a Editar");
	Choice choModificarAlquiler = new Choice();
	Button botonModificarAlquiler = new Button("Editar");
	Button cancelar = new Button("Cancelar");

	//FRAME DE MODIFICACIÓN EDITAR PELICULA
	Frame frameModificacionAlquilerEdit = new Frame("Editar Cliente");
	Label labelModIdAlquiler = new Label("Id:");
	TextField textoModIdAlquiler = new TextField(20);
	Label labelModIdClientesFK2 = new Label("idClientesFK2:");
	TextField textoIdClientesFK2 = new TextField(20);
	Label labelModIdPeliculasFK3 = new Label("idPeliculasFK3:");
	TextField textoIdPeliculasFK3 = new TextField(20);
	Button botonModificacionPeliculaAceptar = new Button("Aceptar");
	Button botonModificacionPeliculaCancelar = new Button("Cancelar");
	Dialog dialogConfirmarModPelicula = new Dialog(frameModificacionAlquilerEdit, "Modificación", true);
	Label labelMensajeModPelicula = new Label("Modificación de Alquiler Completada");

	BaseDeDatos bd;
	String sentencia = "";
	String usuario;
	FicheroLog log = new FicheroLog();
	Connection connection = null;
	Statement statement = null;
	ResultSet rs = null;

	public ModificacionAlquiler(String usuario)
	{
		this.usuario = usuario;
		bd = new BaseDeDatos();
		connection = bd.conectar();
		sentencia = "SELECT idAlquiler, nombreCliente, nombrePelicula FROM alquileres JOIN  clientes ON alquileres.idClientesFK2 = clientes.idCliente JOIN peliculas ON alquileres.idPeliculasFK3 = peliculas.idPelicula";
		try
		{
			statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
					ResultSet.CONCUR_UPDATABLE);
			rs = statement.executeQuery(sentencia);
			while(rs.next())
			{
				choModificarAlquiler.add(rs.getInt("idAlquiler")
						+"-"+rs.getString("nombreCliente") +"-"+rs.getString("nombrePelicula")+"\n");
			}
		}
		catch (SQLException sqle)
		{
			labelMensajeModificarAlquiler.setText("Error en MODIFICACIÓN");
		}

		frameModificacionAlquiler.setLayout(new FlowLayout());
		frameModificacionAlquiler.add(labelMensajeModificarAlquiler);
		frameModificacionAlquiler.add(choModificarAlquiler);
		botonModificarAlquiler.addActionListener(this);
		cancelar.addActionListener(this);
		frameModificacionAlquiler.add(botonModificarAlquiler);
		cancelar.addActionListener(this);
		frameModificacionAlquiler.add(cancelar);

		frameModificacionAlquiler.setSize(600,200);
		frameModificacionAlquiler.setResizable(false);
		frameModificacionAlquiler.setLocationRelativeTo(null);
		frameModificacionAlquiler.addWindowListener(this);
		frameModificacionAlquiler.setVisible(true);

	}

	@Override
	public void actionPerformed(ActionEvent evento) 
	{
		if(evento.getSource().equals(botonModificarAlquiler))
		{
			log.guardar(usuario, "Ha pulsado modificar cliente.");
			frameModificacionAlquilerEdit.setLayout(new FlowLayout());

			frameModificacionAlquilerEdit.add(labelModIdAlquiler);
			labelModIdAlquiler.setFocusable(false);
			frameModificacionAlquilerEdit.add(textoModIdAlquiler);
			
			frameModificacionAlquilerEdit.add(labelModIdClientesFK2);
			frameModificacionAlquilerEdit.add(textoIdClientesFK2);
			
			frameModificacionAlquilerEdit.add(labelModIdPeliculasFK3);
			frameModificacionAlquilerEdit.add(textoIdPeliculasFK3);
			botonModificacionPeliculaAceptar.addActionListener(this);
			frameModificacionAlquilerEdit.add(botonModificacionPeliculaAceptar);
			botonModificacionPeliculaCancelar.addActionListener(this);
			frameModificacionAlquilerEdit.add(botonModificacionPeliculaCancelar);

			frameModificacionAlquilerEdit.setSize(210,350);
			frameModificacionAlquilerEdit.setResizable(false);
			frameModificacionAlquilerEdit.setLocationRelativeTo(null);
			frameModificacionAlquilerEdit.addWindowListener(this);
			frameModificacionAlquilerEdit.setVisible(true);

			String[] elegidoMod = choModificarAlquiler.getSelectedItem().split("-");
			textoModIdAlquiler.setEditable(false);
			textoModIdAlquiler.setText(elegidoMod[0]);
			textoIdClientesFK2.setText(elegidoMod[1]);
			textoIdPeliculasFK3.setText(elegidoMod[2]);
		}

		else if(evento.getSource().equals(botonModificacionPeliculaAceptar))
		{
			log.guardar(usuario, "Ha aceptado la modificación de la Pelicula");
			connection = bd.conectar();
			try
			{
				statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
						ResultSet.CONCUR_READ_ONLY);
				if(((textoModIdAlquiler.getText().length())!=0)
						&& 	((textoIdClientesFK2.getText().length())!=0)
						&&	((textoIdPeliculasFK3.getText().length())!=0))
				{
					sentencia = "UPDATE alquileres SET "
							+ "idAlquiler='"+textoModIdAlquiler.getText()+"', "
							+ "idClientesFK2='"+textoIdClientesFK2.getText()+ "', "
							+ "idPeliculasFK3='"+textoIdPeliculasFK3.getText()+ "' "
							+ "WHERE idAlquiler="+textoModIdAlquiler.getText();;
					
					System.out.println(sentencia);
					log.guardar(usuario, sentencia);
					statement.executeUpdate(sentencia);
				}
				else
				{
					labelMensajeModPelicula.setText("Error campo vacío");
				}
			}
			catch (SQLException sqle)
			{
				labelMensajeModPelicula.setText("Error en MODIFICACIÓN");}
			finally
			{
				dialogConfirmarModPelicula.setLayout(new FlowLayout());
				dialogConfirmarModPelicula.addWindowListener(this);
				dialogConfirmarModPelicula.setSize(250,110);
				dialogConfirmarModPelicula.setResizable(false);
				dialogConfirmarModPelicula.setLocationRelativeTo(null);
				dialogConfirmarModPelicula.add(labelMensajeModPelicula);
				dialogConfirmarModPelicula.setVisible(true);
			}
		}
		else if(evento.getSource().equals(cancelar))
		{
			log.guardar(usuario, "Ha cancelado la modificación del Alquiler.");
			frameModificacionAlquiler.setVisible(false);
		}
	}

	@Override
	public void windowClosing(WindowEvent evento) 
	{
		log.guardar(usuario, "Ha salido de Modificación Alquiler.");
		if(frameModificacionAlquiler.isActive())
		{
			frameModificacionAlquiler.setVisible(false);
		}

		else if (frameModificacionAlquilerEdit.isActive())
		{
			frameModificacionAlquilerEdit.setVisible(false);
		}

		else if(dialogConfirmarModPelicula.isActive())
		{
			dialogConfirmarModPelicula.setVisible(false);
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
