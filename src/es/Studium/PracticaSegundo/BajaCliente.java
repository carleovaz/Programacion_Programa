/*
 * Estructura de BajaCliente
 */
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

public class BajaCliente implements ActionListener, WindowListener
{
	//CREAMOS EL FRAME Y SUS RESPECTIVOS OBJETOS
	Frame frameBajaCliente = new Frame("Baja de Cliente");
	Label labelMensajeBajaCliente = new Label("Seleccionar el cliente:");
	Choice choClientes = new Choice();
	Button botonBorrarCliente = new Button("Borrar");
	Dialog dilogSeguroCliente = new Dialog(frameBajaCliente, "AVISO", true);
	Label labelSeguroCliente = new Label("¿Está seguro de querer borrar este cliente?");
	Button botonSiSeguroCliente = new Button("Sí");
	Button botonNoSeguroCliente = new Button("No");
	Dialog dialogConfirmacionBajaCliente = new Dialog(frameBajaCliente, "Baja Cliente", true);
	Label labelConfirmacionBajaCliente = new Label("Baja de cliente realizada");

	BaseDeDatos bd;
	String sentencia = "";
	String usuario;
	FicheroLog log = new FicheroLog();
	Connection connection = null;
	Statement statement = null;
	ResultSet rs = null;

	//CONSTRUCTOR
	public BajaCliente(String usuario)
	{
		this.usuario = usuario;
		//CONECTAMOS A LA BASE DE DATOS
		bd = new BaseDeDatos();
		connection = bd.conectar();


		frameBajaCliente.setLayout(new FlowLayout());
		frameBajaCliente.add(labelMensajeBajaCliente);
		bd = new BaseDeDatos();
		connection = bd.conectar();
		//SELECCIONAMOS CLIENTES
		sentencia = "SELECT * FROM clientes";
		try
		{
			//CREAMOS SENTENCIA
			statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			rs = statement.executeQuery(sentencia);
			choClientes.removeAll();
			choClientes.add("Seleccionar un Cliente");
			while(rs.next())
			{
				//BUSCAMOS LOS DATOS DE LOS CLIENTES
				choClientes.add(rs.getInt("idCliente")
						+"-"+rs.getString("nombreCliente") +"-"+rs.getString("direccionCliente")
						+"-"+rs.getString("dniCliente")+"-"+rs.getString("correoCliente"));
			}
		}

		catch (SQLException sqle)
		{}
		
		//AÑADIMOS EL FRAME DE DAR DE BAJA AL CLIENTE
		frameBajaCliente.add(labelMensajeBajaCliente);
		frameBajaCliente.add(choClientes);
		botonBorrarCliente.addActionListener(this);
		frameBajaCliente.add(botonBorrarCliente);

		frameBajaCliente.setSize(600,150);
		frameBajaCliente.setResizable(false);
		frameBajaCliente.setLocationRelativeTo(null);
		frameBajaCliente.addWindowListener(this);
		frameBajaCliente.setVisible(true);

		frameBajaCliente.setLayout(new FlowLayout());
		
	}

	@Override
	//FUNCIONALIDAD A LOS BOTONES
	public void actionPerformed(ActionEvent evento) 
	{
		if(evento.getSource().equals(botonBorrarCliente))
		{
			log.guardar(usuario, "Ha pulsado Borrar Cliente");
			dilogSeguroCliente.setLayout(new FlowLayout());
			dilogSeguroCliente.addWindowListener(this);
			dilogSeguroCliente.setSize(270,100);
			dilogSeguroCliente.setResizable(false);
			dilogSeguroCliente.setLocationRelativeTo(null);
			dilogSeguroCliente.add(labelSeguroCliente);
			botonSiSeguroCliente.addActionListener(this);
			dilogSeguroCliente.add(botonSiSeguroCliente);
			botonNoSeguroCliente.addActionListener(this);
			dilogSeguroCliente.add(botonNoSeguroCliente);
			dilogSeguroCliente.setVisible(true);
		}

		else if(evento.getSource().equals(botonNoSeguroCliente))
		{
			log.guardar(usuario, "Ha pulsado el botón NO, ha cancelado el borrado");
			dilogSeguroCliente.setVisible(false);
		}
		
		else if(evento.getSource().equals(botonSiSeguroCliente))
		{
			log.guardar(usuario, "Ha pulsado el botón SI, ha borrado el cliente");
			//CONECTAMOS A LA BASE DE DATOS
			bd = new BaseDeDatos();
			connection = bd.conectar();
			String[] elegido = choClientes.getSelectedItem().split("-");
			//SENTENCIA DE BORRADO DENTRO DE LA TABLA CLIENTES
			sentencia = "DELETE FROM clientes WHERE idCliente = "+elegido[0];
			try
			{
				statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
						ResultSet.CONCUR_READ_ONLY);
				System.out.println(sentencia);
				log.guardar(usuario, sentencia);
				statement.executeUpdate(sentencia);
				labelConfirmacionBajaCliente.setText("Baja de Cliente Correcta");
			}
			catch (SQLException sqle)
			{
				log.guardar(usuario, "El Cliente no puede ser borrado, tiene enlazado algún dato que debes borrar primero.");
				labelConfirmacionBajaCliente.setText("Error en Baja");
			}
			finally
			{
				dialogConfirmacionBajaCliente.setLayout(new FlowLayout());
				dialogConfirmacionBajaCliente.addWindowListener(this);
				dialogConfirmacionBajaCliente.setSize(250,100);
				dialogConfirmacionBajaCliente.setResizable(false);
				dialogConfirmacionBajaCliente.setLocationRelativeTo(null);
				dialogConfirmacionBajaCliente.add(labelConfirmacionBajaCliente);
				dialogConfirmacionBajaCliente.setVisible(true);
			}
		}

	}
	public void windowClosing(WindowEvent e) 
	{
		if(frameBajaCliente.isActive())
		{
			frameBajaCliente.setVisible(false);
		}
		else if(dilogSeguroCliente.isActive())
		{
			dilogSeguroCliente.setVisible(false);
		}
		else if(dialogConfirmacionBajaCliente.isActive())
		{
			dialogConfirmacionBajaCliente.setVisible(false);
			dilogSeguroCliente.setVisible(false);
			frameBajaCliente.setVisible(false);
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