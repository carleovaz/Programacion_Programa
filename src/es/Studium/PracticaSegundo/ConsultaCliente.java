package es.Studium.PracticaSegundo;

import java.awt.Button;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ConsultaCliente implements WindowListener, ActionListener
{
	Frame frameConsultaClientes = new Frame("Consulta de Clientes");
	TextArea listadoClientes = new TextArea(4, 30);
	Button botonPdfClientes = new Button("PDF");

	BaseDeDatos bd;
	String sentencia = "";
	Connection connection = null;
	Statement statement = null;
	ResultSet rs = null;

	public ConsultaCliente()
	{
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
			listadoClientes.setEditable(false);
			frameConsultaClientes.add(listadoClientes);
			frameConsultaClientes.add(botonPdfClientes);
			frameConsultaClientes.setSize(280,160);
			frameConsultaClientes.setResizable(false);
			frameConsultaClientes.setLocationRelativeTo(null);
			frameConsultaClientes.addWindowListener(this);
			frameConsultaClientes.setVisible(true);
		}

	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub

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
