/*
 * Estructura de ConsultaAlquiler
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

public class ConsultaAlquilerFK implements WindowListener, ActionListener
{
	Frame ventanaAlquilerConsulta = new Frame("Consulta");
	TextArea listadoAlquilerConsulta = new TextArea(5, 30);
	Button botonPdf = new Button("PDF");
	Label labelMensajeAlquierConsulta = new Label("");

	BaseDeDatos bd;
	String sentencia = "";
	String usuario;
	FicheroLog log = new FicheroLog();
	Connection connection = null;
	Statement statement = null;
	ResultSet rs = null;

	public ConsultaAlquilerFK(String usuario)
	{
		this.usuario = usuario;
		bd = new BaseDeDatos();
		connection = bd.conectar();
		sentencia = "SELECT idAlquiler, nombreCliente, nombrePelicula FROM alquileres JOIN  clientes ON alquileres.idClientesFK2 = clientes.idCliente JOIN peliculas ON alquileres.idPeliculasFK3 = peliculas.idPelicula";

		try
		{
			//CREAMOS LA SENTENCIA
			statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			//CREAMOS EL RESULT SET
			rs = statement.executeQuery(sentencia);
			listadoAlquilerConsulta.selectAll();
			listadoAlquilerConsulta.setText("");
			listadoAlquilerConsulta.append("id\tNombre Cliente\tNombre Pelicula\n");
			while(rs.next())
			{
				listadoAlquilerConsulta.append(rs.getInt("idAlquiler")
						+"-"+rs.getString("nombreCliente") +"-"+rs.getString("nombrePelicula")+"\n");
			}
		}
		//EN EL CASO QUE FALLE
		catch (SQLException sqle)
		{
			labelMensajeAlquierConsulta.setText("Error");
		}
		finally
		{
			ventanaAlquilerConsulta.setLayout(new FlowLayout());
			listadoAlquilerConsulta.setEditable(false);
			ventanaAlquilerConsulta.add(listadoAlquilerConsulta);
			ventanaAlquilerConsulta.add(botonPdf);
			botonPdf.addActionListener(this);
			ventanaAlquilerConsulta.setSize(250,200);
			ventanaAlquilerConsulta.setResizable(false);
			ventanaAlquilerConsulta.setLocationRelativeTo(null);
			ventanaAlquilerConsulta.addWindowListener(this);
			ventanaAlquilerConsulta.setVisible(true);
			bd.desconectar(connection);
		}

	}

	public void windowClosing(WindowEvent e)
	{
		log.guardar(usuario, "Ha salido de Consulta Alquiler.");
		if(ventanaAlquilerConsulta.isActive())
		{
			ventanaAlquilerConsulta.setVisible(false);
		}
	}
	public void actionPerformed(ActionEvent evento) 
	{
		if(evento.getSource().equals(botonPdf))
		{
			log.guardar(usuario, "Ha solicitado el pdf de consulta de alquileres.");
			Document documentoPDF = new Document();
			try
			{
				//CREAMOS EL ARCHIVO PDF Y LE AGREGAMOS UN NOMBRE
				FileOutputStream archivoPDF = new FileOutputStream ("Consulta_Alquileres.pdf");
				//INDICAMOS EL ESPACIO ENTRE LINEAS Y LLAMAMOS A DOCUMENTOPDF Y ARCHIVOPDF
				PdfWriter.getInstance(documentoPDF, archivoPDF).setInitialLeading(20);
				//LO ABRIMOS
				documentoPDF.open();
				//AGREGAMOS EL NOMBRE QUE SALDRA EN EL ENCABEZADO
				Paragraph nombreEncabezado = new Paragraph("Consulta Alquileres",FontFactory.getFont("times new roman",18, Font.NORMAL, BaseColor.BLACK));
				nombreEncabezado.setAlignment(Element.ALIGN_CENTER);
				documentoPDF.add(nombreEncabezado);
				PdfPTable tabla = new PdfPTable(3);
				//ESTABLECEMOS EL ESPACIO ENTRE EL NOMBRE DE LA CONSULTA Y LA TABLA
				tabla.setSpacingBefore(20);
				//AÑADIMOS LAS TABLAS UNA POR UNA
				tabla.addCell("id");
				tabla.addCell("Nombre Cliente");
				tabla.addCell("Nombre Pelicula");
				//CONECTAMOS CON NUESTRA BASE DE DATOS
				bd = new BaseDeDatos();
				connection = bd.conectar();
				//SELECCIONAMOS LA TABLA EN CUESTIÓN
				sentencia = "SELECT idAlquiler, nombreCliente, nombrePelicula FROM alquileres JOIN  clientes ON alquileres.idClientesFK2 = clientes.idCliente JOIN peliculas ON alquileres.idPeliculasFK3 = peliculas.idPelicula";
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
					File pathPDF = new File ("Consulta_Alquileres.pdf");
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

	public void windowActivated(WindowEvent we) 
	{

	}
	public void windowClosed(WindowEvent we) 
	{

	}
	public void windowDeactivated(WindowEvent we) 
	{

	}
	public void windowDeiconified(WindowEvent we) 
	{

	}
	public void windowIconified(WindowEvent we) 
	{

	}
	public void windowOpened(WindowEvent we) 
	{

	}
}