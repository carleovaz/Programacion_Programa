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

public class AltaPeliculaFK implements WindowListener, ActionListener
{
	//CREAMOS EL FRAME Y SUS RESPECTIVOS OBJETOS
	Frame FrameAltaPelicula = new Frame ("Alta Peliculas");
	Label labelNombrePelicula = new Label("Pelicula");
	Label labelDirectorPelicula = new Label ("Director");
	Label labelPrecioPelicula = new Label ("Precio");
	Label labelPropietario = new Label ("Propietario");
	Choice choPropietarios = new Choice();
	TextField textoNombrePelicula = new TextField(20);
	TextField textoDirectorPelicula = new TextField(20);
	TextField textoPrecioPelicula = new TextField(20);
	Button botonAceptar = new Button ("Aceptar");
	Button botonCancelar = new Button ("Cancelar");
	Dialog dialogoMensajeAltaPelicula = new Dialog(FrameAltaPelicula, "Confirmación", true);
	Label labelMensaje = new Label ("Asignación de Pelicula exitosa");

	BaseDeDatos bd;
	String sentencia = "";
	String usuario;
	FicheroLog log = new FicheroLog();
	Connection connection = null;
	Statement statement = null;
	ResultSet rs = null;

	//CONSTRUCTOR
	public AltaPeliculaFK(String usuario)
	{
		this.usuario = usuario;
		FrameAltaPelicula.setLayout(new FlowLayout());

		FrameAltaPelicula.add(labelNombrePelicula);
		FrameAltaPelicula.add(textoNombrePelicula);
		FrameAltaPelicula.add(labelDirectorPelicula);
		FrameAltaPelicula.add(textoDirectorPelicula);
		FrameAltaPelicula.add(labelPrecioPelicula);
		FrameAltaPelicula.add(textoPrecioPelicula);
		
		FrameAltaPelicula.add(choPropietarios);
		botonAceptar.addActionListener(this);
		FrameAltaPelicula.add(botonAceptar);
		botonCancelar.addActionListener(this);
		FrameAltaPelicula.add(botonCancelar);
		FrameAltaPelicula.setSize(800,180);
		FrameAltaPelicula.setVisible(true);
		FrameAltaPelicula.setLocationRelativeTo(null);
		FrameAltaPelicula.addWindowListener(this);

		bd = new BaseDeDatos();
		connection = bd.conectar();

		sentencia = "SELECT * FROM propietario";
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
	}

	@Override
	public void actionPerformed(ActionEvent evento) 
	{
		if(evento.getSource().equals(botonAceptar))
		{
			log.guardar(usuario, "Ha pulsado Aceptar Alta Cliente.");
			bd = new BaseDeDatos();
			connection = bd.conectar();
			try
			{
				sentencia="INSERT INTO peliculas VALUES(null,'"
						+textoNombrePelicula.getText()+"','"
						+textoDirectorPelicula.getText()+"','"
						+textoPrecioPelicula.getText()+"',"
						//SELECICONAMOS EL PROPIETARIO, SACAMOS EL ITEM ELEGIDO Y NOS QUEDAMOS CON UNICO ELEMENTO
						+choPropietarios.getSelectedItem().split("-")[0]
								+")";
				//CREAMOS SENTENCIA
				statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
						ResultSet.CONCUR_READ_ONLY);
				log.guardar(usuario, sentencia);
				statement.executeUpdate(sentencia);
				
			}
			catch (SQLException sqle)
			{
				labelMensaje.setText("Error en asignación");
			}
			
			finally
			{
				bd.desconectar(connection);
				dialogoMensajeAltaPelicula.setLayout(new FlowLayout());
				dialogoMensajeAltaPelicula.addWindowListener(this);
				dialogoMensajeAltaPelicula.setSize(280,100);
				dialogoMensajeAltaPelicula.setResizable(false);
				dialogoMensajeAltaPelicula.setLocationRelativeTo(null);
				dialogoMensajeAltaPelicula.add(labelMensaje);
				dialogoMensajeAltaPelicula.setVisible(true);
			}
		}

		else if (evento.getSource().equals(botonCancelar)) 
		{
			log.guardar(usuario, "Ha pulsado Cancelar Alta Pelicula.");
			FrameAltaPelicula.setVisible(false);
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
	public void windowClosing(WindowEvent arg0) 
	{
		FrameAltaPelicula.setVisible(false);

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