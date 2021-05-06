/*
 * Estructura de ConsultaCliente
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

public class ConsultaCliente implements WindowListener, ActionListener
{
	Frame frameConsultaClientes = new Frame("Consulta de Clientes");
	TextArea listadoClientes = new TextArea(4, 30);
	Button botonPdf = new Button("PDF");

	BaseDeDatos bd;
	String sentencia = "";
	String usuario;
	FicheroLog log = new FicheroLog();
	Connection connection = null;
	Statement statement = null;
	ResultSet rs = null;

	public ConsultaCliente(String usuario)
	{
		this.usuario = usuario;
		//CONECTAMOS CON LA BASE DE DATOS
		frameConsultaClientes.setLayout(new FlowLayout());
		bd = new BaseDeDatos();
		connection = bd.conectar();
		//CREAMOS LA SENTENCIA
		sentencia = "SELECT * FROM clientes";

		try
		{
			statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			rs = statement.executeQuery(sentencia);
			listadoClientes.selectAll();
			listadoClientes.setText("");
			listadoClientes.append("id Nombre\tDirección\tDNI\tCorreo\n");
			while(rs.next())
			{
				listadoClientes.append(rs.getInt("idCliente")
						+"-"+rs.getString("nombreCliente") +"-"+rs.getString("direccionCliente")
						+"-"+rs.getString("dniCliente")+"-"+rs.getString("correoCliente")+"\n");
			}
		}
		catch (SQLException sqle)
		{

		}
		finally
		{

		}
		listadoClientes.setEditable(false);
		frameConsultaClientes.add(listadoClientes);
		frameConsultaClientes.add(botonPdf);
		botonPdf.addActionListener(this);
		frameConsultaClientes.setSize(280,160);
		frameConsultaClientes.setResizable(false);
		frameConsultaClientes.setLocationRelativeTo(null);
		frameConsultaClientes.addWindowListener(this);
		frameConsultaClientes.setVisible(true);
	}

	@Override
	//USO DEL BOTON PDF
	public void actionPerformed(ActionEvent evento) 
	{
		if(evento.getSource().equals(botonPdf))
		{
			log.guardar(usuario, "Ha solicitado el pdf de consulta de alquileres.");
			Document documentoPDF = new Document();
			try
			{
				//CREAMOS EL ARCHIVO PDF Y LE AGREGAMOS UN NOMBRE
				FileOutputStream archivoPDF = new FileOutputStream ("Consulta_Clientes.pdf");
				//INDICAMOS EL ESPACIO ENTRE LINEAS Y LLAMAMOS A DOCUMENTOPDF Y ARCHIVOPDF
				PdfWriter.getInstance(documentoPDF, archivoPDF).setInitialLeading(20);
				//LO ABRIMOS
				documentoPDF.open();
				//AGREGAMOS EL NOMBRE QUE SALDRA EN EL ENCABEZADO
				Paragraph nombreEncabezado = new Paragraph("Consulta Clientes",FontFactory.getFont("times new roman",18, Font.NORMAL, BaseColor.BLACK));
				nombreEncabezado.setAlignment(Element.ALIGN_CENTER);
				documentoPDF.add(nombreEncabezado);
				PdfPTable tabla = new PdfPTable(5);
				//ESTABLECEMOS EL ESPACIO ENTRE EL NOMBRE DE LA CONSULTA Y LA TABLA
				tabla.setSpacingBefore(20);
				//AÑADIMOS LAS TABLAS UNA POR UNA
				tabla.addCell("id");
				tabla.addCell("Nombre");
				tabla.addCell("Dirección");
				tabla.addCell("DNI");
				tabla.addCell("Correo");
				//CONECTAMOS CON NUESTRA BASE DE DATOS
				bd = new BaseDeDatos();
				connection = bd.conectar();
				//SELECCIONAMOS LA TABLA EN CUESTIÓN
				sentencia = "SELECT*FROM clientes";

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
					File pathPDF = new File ("Consulta_Clientes.pdf");
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
	public void windowClosing(WindowEvent arg0) 
	{
		log.guardar(usuario, "Ha salido de Consulta Cliente.");
		if(frameConsultaClientes.isActive())
		{
			frameConsultaClientes.setVisible(false);
		}

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
