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

public class ModificacionPelicula implements ActionListener, WindowListener
{
	//FRAME DE MODIFICACIÓN PELICULA
	Frame frameModificacionPelicula = new Frame("Modificación");
	Label  labelMensajeModificarPelicular = new Label("Seleccionar la pelicula a Editar");
	Choice choModificarPelicula = new Choice();
	Button botonModificarPelicula = new Button("Editar");
	Button cancelar = new Button("Cancelar");

	//FRAME DE MODIFICACIÓN EDITAR PELICULA
	Frame frameModificacionPeliculaEdit = new Frame("Editar Cliente");
	Label labelModIdPelicula = new Label("Id:");
	TextField textoModIdPelicula = new TextField(20);
	Label labelModNombrePelicula = new Label("Nombre:");
	TextField textoNombrePelicula = new TextField(20);
	Label labelModDirectorPelicula = new Label("Director:");
	TextField textoModDirectorPelicula = new TextField(20);
	Label labelModPrecioPelicula = new Label("precio:");
	TextField textoModPrecioPelicula = new TextField(20);
	Label labelModIdPropietarioFK = new Label("idPropietarioFK");
	TextField textoModIdPropietarioFK = new TextField(20);
	Button botonModificacionPeliculaAceptar = new Button("Aceptar");
	Button botonModificacionPeliculaCancelar = new Button("Cancelar");
	Dialog dialogConfirmarModPelicula = new Dialog(frameModificacionPeliculaEdit, "Modificación", true);
	Label labelMensajeModPelicula = new Label("Modificación de Pelicula Completada");

	BaseDeDatos bd;
	String sentencia = "";
	String usuario;
	FicheroLog log = new FicheroLog();
	Connection connection = null;
	Statement statement = null;
	ResultSet rs = null;

	public ModificacionPelicula(String usuario)
	{
		this.usuario = usuario;
		bd = new BaseDeDatos();
		connection = bd.conectar();
		sentencia = "SELECT idPelicula , nombrePropietario, nombrePelicula, directorPelicula, precioPelicula FROM peliculas JOIN propietario ON peliculas.IdPropietarioFK1 = propietario.idPropietario";
		try
		{
			statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
					ResultSet.CONCUR_UPDATABLE);
			rs = statement.executeQuery(sentencia);
			while(rs.next())
			{
				choModificarPelicula.add(rs.getInt("idPelicula")
						+"-"+rs.getString("nombrePelicula") +"-"+rs.getString("directorPelicula")
						+"-"+rs.getString("precioPelicula")+"-"+rs.getString("nombrePropietario")+"\n");
			}
		}
		catch (SQLException sqle)
		{
			labelMensajeModificarPelicular.setText("Error en MODIFICACIÓN");
		}

		frameModificacionPelicula.setLayout(new FlowLayout());
		frameModificacionPelicula.add(labelMensajeModificarPelicular);
		frameModificacionPelicula.add(choModificarPelicula);
		botonModificarPelicula.addActionListener(this);
		cancelar.addActionListener(this);
		frameModificacionPelicula.add(botonModificarPelicula);
		cancelar.addActionListener(this);
		frameModificacionPelicula.add(cancelar);

		frameModificacionPelicula.setSize(600,200);
		frameModificacionPelicula.setResizable(false);
		frameModificacionPelicula.setLocationRelativeTo(null);
		frameModificacionPelicula.addWindowListener(this);
		frameModificacionPelicula.setVisible(true);
	}
	@Override
	public void actionPerformed(ActionEvent evento) 
	{
		if(evento.getSource().equals(botonModificarPelicula))
		{
			log.guardar(usuario, "Ha pulsado modificar pelicula.");
			frameModificacionPeliculaEdit.setLayout(new FlowLayout());

			frameModificacionPeliculaEdit.add(labelModIdPelicula);
			labelModIdPelicula.setFocusable(false);
			frameModificacionPeliculaEdit.add(textoModIdPelicula);
			frameModificacionPeliculaEdit.add(labelModNombrePelicula);
			frameModificacionPeliculaEdit.add(textoNombrePelicula);
			frameModificacionPeliculaEdit.add(labelModDirectorPelicula);
			frameModificacionPeliculaEdit.add(textoModDirectorPelicula);
			frameModificacionPeliculaEdit.add(labelModPrecioPelicula);
			frameModificacionPeliculaEdit.add(textoModPrecioPelicula);
			frameModificacionPeliculaEdit.add(labelModIdPropietarioFK);
			frameModificacionPeliculaEdit.add(textoModIdPropietarioFK);
			
			botonModificacionPeliculaAceptar.addActionListener(this);
			frameModificacionPeliculaEdit.add(botonModificacionPeliculaAceptar);
			botonModificacionPeliculaCancelar.addActionListener(this);
			frameModificacionPeliculaEdit.add(botonModificacionPeliculaCancelar);
			frameModificacionPeliculaEdit.setSize(210,350);
			frameModificacionPeliculaEdit.setResizable(false);
			frameModificacionPeliculaEdit.setLocationRelativeTo(null);
			frameModificacionPeliculaEdit.addWindowListener(this);
			frameModificacionPeliculaEdit.setVisible(true);
			
			String[] elegidoMod = choModificarPelicula.getSelectedItem().split("-");
			textoModIdPelicula.setEditable(false);
			textoModIdPelicula.setText(elegidoMod[0]);
			textoNombrePelicula.setText(elegidoMod[1]);
			textoModDirectorPelicula.setText(elegidoMod[2]);
			textoModPrecioPelicula.setText(elegidoMod[3]);
			textoModIdPropietarioFK.setText(elegidoMod[4]);
		}
		else if(evento.getSource().equals(botonModificacionPeliculaAceptar))
		{
			log.guardar(usuario, "Ha aceptado la modificación de la Pelicula");
			connection = bd.conectar();
			try
			{
				statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
						ResultSet.CONCUR_READ_ONLY);
				if(((textoNombrePelicula.getText().length())!=0)
						&& 	((textoModDirectorPelicula.getText().length())!=0)
						&&	((textoModPrecioPelicula.getText().length())!=0)
						&&	((textoModIdPropietarioFK.getText().length())!=0))
				{
					sentencia = "UPDATE peliculas SET "
							+ "nombrePelicula='"+textoNombrePelicula.getText()+"', "
							+ "directorPelicula='"+textoModDirectorPelicula.getText()+ "', "
							+ "precioPelicula='"+textoModPrecioPelicula.getText()+ "', "
							+ "idPropietarioFK1='"+textoModIdPropietarioFK.getText()+ "' "
							+ "WHERE idPelicula="+textoModIdPelicula.getText();
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
			log.guardar(usuario, "Ha cancelado la modificación de la Pelicula");
			frameModificacionPelicula.setVisible(false);
		}
	}
	@Override
	public void windowClosing(WindowEvent evento) 
	{
		log.guardar(usuario, "Ha salido de Modificación Pelicula.");
		if(frameModificacionPelicula.isActive())
		{
			frameModificacionPelicula.setVisible(false);
		}

		else if (frameModificacionPeliculaEdit.isActive())
		{
			frameModificacionPeliculaEdit.setVisible(false);
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
