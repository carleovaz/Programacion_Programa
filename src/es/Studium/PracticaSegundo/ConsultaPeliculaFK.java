/*
 * Estructura de ConsultaPeliculaFK
 */
package es.Studium.PracticaSegundo;

import java.awt.Button;
import java.awt.Desktop;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Label;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

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
		sentencia = "SELECT idPelicula , nombrePropietario, nombrePelicula, directorPelicula, precioPelicula FROM peliculas JOIN propietario ON peliculas.IdPropietarioFK1 = propietario.idPropietario";

		try
		{
			statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			rs = statement.executeQuery(sentencia);
			listadoPeliculasConsulta.selectAll();
			listadoPeliculasConsulta.setText("");
			listadoPeliculasConsulta.append("id\tNombre   \tDirector   \tPrecio   \tNombre Propietario \n");
			while(rs.next())
			{
				listadoPeliculasConsulta.append(rs.getInt("idPelicula")
						+"-"+rs.getString("nombrePelicula") +"-"+rs.getString("directorPelicula")
						+"-"+rs.getString("precioPelicula")+"-"+rs.getString("nombrePropietario")+"\n");
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
			botonPdf.addActionListener(this);
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
			Document documentoPDF = new Document();
			try
			{
				//CREAMOS EL ARCHIVO PDF Y LE AGREGAMOS UN NOMBRE
				FileOutputStream archivoPDF = new FileOutputStream ("Consulta_Peliculas.pdf");
				//INDICAMOS EL ESPACIO ENTRE LINEAS Y LLAMAMOS A DOCUMENTOPDF Y ARCHIVOPDF
				PdfWriter.getInstance(documentoPDF, archivoPDF).setInitialLeading(20);
				//LO ABRIMOS
				documentoPDF.open();
				//AGREGAMOS EL NOMBRE QUE SALDRA EN EL ENCABEZADO
				Paragraph nombreEncabezado = new Paragraph("Consulta Peliculas",FontFactory.getFont("times new roman",18, Font.NORMAL, BaseColor.BLACK));
				nombreEncabezado.setAlignment(Element.ALIGN_CENTER);
				documentoPDF.add(nombreEncabezado);
				PdfPTable tabla = new PdfPTable(5);
				//ESTABLECEMOS EL ESPACIO ENTRE EL NOMBRE DE LA CONSULTA Y LA TABLA
				tabla.setSpacingBefore(20);
				//AÑADIMOS LAS TABLAS UNA POR UNA
				tabla.addCell("id");
				tabla.addCell("Nombre Propietario");
				tabla.addCell("Nombre Pelicula");
				tabla.addCell("Director Pelicula");
				tabla.addCell("Precio Pelicula");
				//CONECTAMOS CON NUESTRA BASE DE DATOS
				bd = new BaseDeDatos();
				connection = bd.conectar();
				//SELECCIONAMOS LA TABLA EN CUESTIÓN
				sentencia = "SELECT idPelicula , nombrePropietario, nombrePelicula, directorPelicula, precioPelicula FROM peliculas JOIN propietario ON peliculas.IdPropietarioFK1 = propietario.idPropietario";;

				try
				{
					statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
							ResultSet.CONCUR_READ_ONLY); 
					rs = statement.executeQuery(sentencia);
					while(rs.next())
					{
						tabla.addCell(rs.getString(1));
						tabla.addCell(rs.getString(2));
						tabla.addCell(rs.getString(3));
						tabla.addCell(rs.getString(4));
						tabla.addCell(rs.getString(5));
					}

				}
				catch (SQLException sqle)
				{

				}
				bd.desconectar(connection);
				documentoPDF.add(tabla);
				documentoPDF.close();

				try
				{
					File pathPDF = new File ("Consulta_Peliculas.pdf");
					Desktop.getDesktop().open(pathPDF);
				}

				catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

			catch (DocumentException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			catch (FileNotFoundException e1) 
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
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