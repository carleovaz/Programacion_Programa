/*
 * Estructura de ConsultaPeliculaFK
 */
package es.Studium.PracticaSegundo;

import java.awt.Button;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Label;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ConsultaPeliculaFK implements WindowListener, ActionListener
{
	//CREAMOS EL FRAME
	Frame ventanaPeliculasConsulta = new Frame("Consulta peliculas");
	TextArea listadoPeliculasConsulta = new TextArea(5, 30);
	Button botonPdf = new Button("PDF");
	Label labelMensajePeliculasConsulta = new Label("");

	BaseDeDatos bd;
	String sentencia = "";
	String usuario;
	FicheroLog log = new FicheroLog();
	Connection connection = null;
	Statement statement = null;
	ResultSet rs = null;

	//CONSTRUCTORES
	public ConsultaPeliculaFK(String usuario)
	{
		this.usuario = usuario;
		//CONECTAMOS A LA BASE DE DATOS
		bd = new BaseDeDatos();
		connection = bd.conectar();
		sentencia = "SELECT*FROM peliculas";

		try
		{
			statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			rs = statement.executeQuery(sentencia);
			listadoPeliculasConsulta.selectAll();
			listadoPeliculasConsulta.setText("");
			listadoPeliculasConsulta.append("id\tNombre   \tDirector   \tPrecio \n");
			while(rs.next())
			{
				listadoPeliculasConsulta.append(rs.getInt("idPelicula")
						+"-"+rs.getString("nombrePelicula") +"-"+rs.getString("directorPelicula")
						+"-"+rs.getString("precioPelicula")+"-"+rs.getString("idPropietarioFK1")+"\n");
			}
		}
		//EN EL CASO QUE FALLE LA CONSULTA
		catch (SQLException sqle)
		{
			labelMensajePeliculasConsulta.setText("Error del proceso");
		}
		finally
		{
			ventanaPeliculasConsulta.setLayout(new FlowLayout());
			listadoPeliculasConsulta.setEditable(false);
			ventanaPeliculasConsulta.add(listadoPeliculasConsulta);
			ventanaPeliculasConsulta.add(botonPdf);
			ventanaPeliculasConsulta.setSize(250,200);
			ventanaPeliculasConsulta.setResizable(false);
			ventanaPeliculasConsulta.setLocationRelativeTo(null);
			ventanaPeliculasConsulta.addWindowListener(this);
			ventanaPeliculasConsulta.setVisible(true);
			bd.desconectar(connection);
		}
	}


	@Override
	public void windowClosing(WindowEvent evento) 
	{
		log.guardar(usuario, "Ha salido de Consulta Pelicula.");
		if(ventanaPeliculasConsulta.isActive())
		{
			ventanaPeliculasConsulta.setVisible(false);
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent evento) 
	{
		if(evento.getSource().equals(botonPdf))
		{
			log.guardar(usuario, "Ha solicitado el pdf de consulta de peliculas.");
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