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

public class ModificacionCliente implements ActionListener, WindowListener
{
	//FRAME DE MODIFICACIÓN CLIENTE
	Frame frameModificacionCliente = new Frame("Modificación");
	Label  labelMensajeModificarCliente = new Label("Seleccionar el cliente a Editar");
	Choice choModificarClientes = new Choice();
	Button botonModificarCliente = new Button("Editar");
	Button botonModificarClienteCancelar = new Button("Cancelar");

	//FRAME DE MODIFICACIÓN EDITAR CLIENTE
	Frame frameModificacionClienteEdit = new Frame("Editar Cliente");
	Label labelModIdCliente = new Label("Id:");
	TextField textoModIdCliente = new TextField(20);
	Label labelModNombreCliente = new Label("Nombre:");
	TextField textoModNombreCliente = new TextField(20);
	Label labelModDireccionCliente = new Label("Dirección:");
	TextField textoModDireccionCliente = new TextField(20);
	Label labelModCorreoCliente = new Label("Correo:");
	TextField textoModCorreoCliente = new TextField(20);
	Label labelModDNICliente = new Label("DNI:");
	TextField textoModDNICliente = new TextField(20);
	Button botonModificacionClienteAceptar = new Button("Aceptar");
	Button botonModificacionClienteCancelar = new Button("Cancelar");
	Dialog dialogConfirmarModCliente = new Dialog(frameModificacionClienteEdit, "Modificación", true);
	Label labelMensajeModCliente = new Label("Modificación de Cliente Completada");
	
	BaseDeDatos bd;
	String sentencia = "";
	String usuario;
	FicheroLog log = new FicheroLog();
	Connection connection = null;
	Statement statement = null;
	ResultSet rs = null;

	public ModificacionCliente(String usuario)
	{
		this.usuario = usuario;
		//CONECTAMOS A LA BASE DE DATOS
		bd = new BaseDeDatos();
		connection = bd.conectar();
		//CREAMOS LA SENTENCIA
		sentencia = "SELECT * FROM clientes";
		try
		{
			statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
					ResultSet.CONCUR_UPDATABLE);
			rs = statement.executeQuery(sentencia);
			while(rs.next())
			{
				choModificarClientes.add(rs.getInt("idCliente")
						+"-"+rs.getString("nombreCliente") +"-"+rs.getString("direccionCliente")
						+"-"+rs.getString("dniCliente")+"-"+rs.getString("correoCliente")+"\n");
			}
		}
		//EN EL CASO QUE FALLE
		catch (SQLException sqle)
		{
			labelMensajeModificarCliente.setText("Error en MODIFICACIÓN");
		}

		frameModificacionCliente.setLayout(new FlowLayout());
		frameModificacionCliente.add(labelMensajeModificarCliente);
		frameModificacionCliente.add(choModificarClientes);
		botonModificarCliente.addActionListener(this);
		botonModificarClienteCancelar.addActionListener(this);
		frameModificacionCliente.add(botonModificarCliente);
		botonModificarClienteCancelar.addActionListener(this);
		frameModificacionCliente.add(botonModificarClienteCancelar);

		frameModificacionCliente.setSize(600,200);
		frameModificacionCliente.setResizable(false);
		frameModificacionCliente.setLocationRelativeTo(null);
		frameModificacionCliente.addWindowListener(this);
		frameModificacionCliente.setVisible(true);
	}
	
	@Override
	//FUNCIONALIDAD A LOS BOTONES
	public void actionPerformed(ActionEvent evento) 
	{
		if(evento.getSource().equals(botonModificarCliente))
		{
			log.guardar(usuario, "Ha pulsado Modificar Cliente");
			frameModificacionClienteEdit.setLayout(new FlowLayout());

			frameModificacionClienteEdit.add(labelModIdCliente);
			labelModIdCliente.setFocusable(false);
			frameModificacionClienteEdit.add(textoModIdCliente);
			frameModificacionClienteEdit.add(labelModNombreCliente);
			frameModificacionClienteEdit.add(textoModNombreCliente);
			frameModificacionClienteEdit.add(labelModDireccionCliente);
			frameModificacionClienteEdit.add(textoModDireccionCliente);
			frameModificacionClienteEdit.add(labelModDNICliente);
			frameModificacionClienteEdit.add(textoModDNICliente);
			frameModificacionClienteEdit.add(labelModCorreoCliente);
			frameModificacionClienteEdit.add(textoModCorreoCliente);
			botonModificacionClienteAceptar.addActionListener(this);
			frameModificacionClienteEdit.add(botonModificacionClienteAceptar);
			botonModificacionClienteCancelar.addActionListener(this);
			frameModificacionClienteEdit.add(botonModificacionClienteCancelar);

			frameModificacionClienteEdit.setSize(210,350);
			frameModificacionClienteEdit.setResizable(false);
			frameModificacionClienteEdit.setLocationRelativeTo(null);
			frameModificacionClienteEdit.addWindowListener(this);
			frameModificacionClienteEdit.setVisible(true);

			String[] elegidoMod = choModificarClientes.getSelectedItem().split("-");
			textoModIdCliente.setEditable(false);
			textoModIdCliente.setText(elegidoMod[0]);
			textoModNombreCliente.setText(elegidoMod[1]);
			textoModDireccionCliente.setText(elegidoMod[2]);
			textoModDNICliente.setText(elegidoMod[3]);
			textoModCorreoCliente.setText(elegidoMod[4]);
		}
		
		else if(evento.getSource().equals(botonModificacionClienteAceptar))
		{
			log.guardar(usuario, "Ha aceptado la modificación del cliente.");
			connection = bd.conectar();
			try
			{
				//GUARDAMOS EL TEXTO MODIFICADO
				statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
						ResultSet.CONCUR_READ_ONLY);
				if(((textoModNombreCliente.getText().length())!=0)
						&& 	((textoModDireccionCliente.getText().length())!=0)
						&&	((textoModDNICliente.getText().length())!=0)
						&&	((textoModCorreoCliente.getText().length())!=0))
				{
					//CREAMOS LA SENTENCIA PARA ACTUALIZAR LA BASE DE DATOS
					sentencia = "UPDATE clientes SET "
							+ "nombreCliente='"+textoModNombreCliente.getText()+"', "
							+ "direccionCliente='"+textoModDireccionCliente.getText()+ "', "
							+ "dniCliente='"+textoModDNICliente.getText()+ "', "
							+ "correoCliente='"+textoModCorreoCliente.getText()+ "' "
							+ "WHERE idCliente="+textoModIdCliente.getText();
					System.out.println(sentencia);
					log.guardar(usuario, sentencia);
					statement.executeUpdate(sentencia);
				}
				else
				{
					labelMensajeModCliente.setText("Error campo vacío");
				}
			}
			catch (SQLException sqle)
			{
				labelMensajeModCliente.setText("Error en MODIFICACIÓN");
			}
			finally
			{
				dialogConfirmarModCliente.setLayout(new FlowLayout());
				dialogConfirmarModCliente.addWindowListener(this);
				dialogConfirmarModCliente.setSize(250,110);
				dialogConfirmarModCliente.setResizable(false);
				dialogConfirmarModCliente.setLocationRelativeTo(null);
				dialogConfirmarModCliente.add(labelMensajeModCliente);
				dialogConfirmarModCliente.setVisible(true);
			}
		}
		else if(evento.getSource().equals(botonModificarClienteCancelar))
		{
			log.guardar(usuario, "Ha cancelado la modificación del cliente.");
			frameModificacionCliente.setVisible(false);
		}
	}
	@Override
	public void windowClosing(WindowEvent e) 
	{
		if(frameModificacionCliente.isActive())
		{
			frameModificacionCliente.setVisible(false);
		}

		else if (frameModificacionClienteEdit.isActive())
		{
			frameModificacionClienteEdit.setVisible(false);
		}

		else if(dialogConfirmarModCliente.isActive())
		{
			dialogConfirmarModCliente.setVisible(false);
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
