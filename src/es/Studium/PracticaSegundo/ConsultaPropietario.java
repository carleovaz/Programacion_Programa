/*
 * Estructura de ConsultaPropietario
 */
package es.Studium.PracticaSegundo;

import java.awt.Button;
import java.awt.Desktop;
import java.awt.FlowLayout;
import java.awt.Frame;
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

public class ConsultaPropietario implements WindowListener, ActionListener
{
	Frame frameConsultaPropietarios = new Frame("Consulta de Propietarios");
	TextArea listadoPropietarios = new TextArea(4, 30);
	Button botonPdf = new Button("PDF");
	
	BaseDeDatos bd;
	String sentencia = "";
	String usuario;
	FicheroLog log = new FicheroLog();
	Connection connection = null;
	Statement statement = null;
	ResultSet rs = null;

	public ConsultaPropietario(String usuario)
	{
		this.usuario = usuario;
		frameConsultaPropietarios.setLayout(new FlowLayout());
		bd = new BaseDeDatos();
		connection = bd.conectar();
		sentencia = "SELECT * FROM propietario";

		try
		{
			statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			rs = statement.executeQuery(sentencia);
			listadoPropietarios.selectAll();
			listadoPropietarios.setText("");
			listadoPropietarios.append("id Nombre\tDirección\tTelefono\tDNI\n");
			while(rs.next())
			{
				listadoPropietarios.append(rs.getInt("idPropietario")
						+"-"+rs.getString("nombrePropietario") +"-"+rs.getString("direccionPropietario")
						+"-"+rs.getString("telefonoPropietario")+"-"+rs.getString("dniPropietario")+"\n");
			}
		}
		catch (SQLException sqle)
		{}
		finally
		{
		}
		frameConsultaPropietarios.add(listadoPropietarios);
		frameConsultaPropietarios.add(botonPdf);
		botonPdf.addActionListener(this);
		frameConsultaPropietarios.setSize(280,160);
		frameConsultaPropietarios.setResizable(false);
		frameConsultaPropietarios.setLocationRelativeTo(null);
		frameConsultaPropietarios.addWindowListener(this);
		frameConsultaPropietarios.setVisible(true);
	}
	
	@Override
	public void windowClosing(WindowEvent evento) 
	{
		if(frameConsultaPropietarios.isActive())
		{
			frameConsultaPropietarios.setVisible(false);
		}
		
	}

	@Override
	public void actionPerformed(ActionEvent evento) 
	{
		if(evento.getSource().equals(botonPdf))
		{
			log.guardar(usuario, "Ha solicitado el pdf de consulta de Propietarios.");
			Document documentoPDF = new Document();
			try
			{
				//CREAMOS EL ARCHIVO PDF Y LE AGREGAMOS UN NOMBRE
				FileOutputStream archivoPDF = new FileOutputStream ("Consulta_Propietarios.pdf");
				//INDICAMOS EL ESPACIO ENTRE LINEAS Y LLAMAMOS A DOCUMENTOPDF Y ARCHIVOPDF
				PdfWriter.getInstance(documentoPDF, archivoPDF).setInitialLeading(20);
				//LO ABRIMOS
				documentoPDF.open();
				//AGREGAMOS EL NOMBRE QUE SALDRA EN EL ENCABEZADO
				Paragraph nombreEncabezado = new Paragraph("Consulta Propietarios",FontFactory.getFont("times new roman",18, Font.NORMAL, BaseColor.BLACK));
				nombreEncabezado.setAlignment(Element.ALIGN_CENTER);
				documentoPDF.add(nombreEncabezado);
				PdfPTable tabla = new PdfPTable(5);
				//ESTABLECEMOS EL ESPACIO ENTRE EL NOMBRE DE LA CONSULTA Y LA TABLA
				tabla.setSpacingBefore(20);
				//AÑADIMOS LAS TABLAS UNA POR UNA
				tabla.addCell("id");
				tabla.addCell("Nombre");
				tabla.addCell("Dirección");
				tabla.addCell("Telefono");
				tabla.addCell("DNI");
				//CONECTAMOS CON NUESTRA BASE DE DATOS
				bd = new BaseDeDatos();
				connection = bd.conectar();
				//SELECCIONAMOS LA TABLA EN CUESTIÓN
				sentencia = "SELECT*FROM propietario";

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
					File pathPDF = new File ("Consulta_Propietarios.pdf");
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
