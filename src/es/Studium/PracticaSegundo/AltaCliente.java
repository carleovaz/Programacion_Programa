package es.Studium.PracticaSegundo;

import java.awt.Button;
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

public class AltaCliente implements ActionListener, WindowListener
{
	//CREAMOS EL FRAME Y LOS OBJETOS DE ALTA CLIENTE
	Frame frameAltaCliente = new Frame("Alta de Cliente");
	Label labelNombreCliente = new Label("Nombre:");
	TextField textoNombreCliente = new TextField(20);
	Label labelDireccionCliente = new Label ("Dirección:");
	TextField textoDirreccionCliente = new TextField(20);
	Label labelCorreoCliente = new Label ("Correo:");
	TextField textoCorreoCliente = new TextField(20);
	Label labelDNICliente = new Label ("DNI:");
	TextField textoDNICliente = new TextField(20);
	Button botonAceptarAltaCliente = new Button("Dar Alta");
	Button botonCancelarAltaCliente = new Button ("Cancelar");
	Dialog dialogConfirmacionAltaCliente = new Dialog (frameAltaCliente, "Alta Cliente");
	Label labelMensajeAltaCliente = new Label ("Alta de Cliente realizada");
	
	BaseDeDatos bd;
	String sentencia = "";
	String usuario;
	FicheroLog log = new FicheroLog();
	Connection connection = null;
	Statement statement = null;
	ResultSet rs = null;

	public AltaCliente(String usuario)
	{
		this.usuario = usuario;
		frameAltaCliente.setLayout(new FlowLayout());

		frameAltaCliente.add(labelNombreCliente);
		textoNombreCliente.setText("");
		frameAltaCliente.add(textoNombreCliente);
		frameAltaCliente.add(labelDireccionCliente);
		textoDirreccionCliente.setText("");
		frameAltaCliente.add(textoDirreccionCliente);

		frameAltaCliente.add(labelDNICliente);
		textoDNICliente.setText("");
		frameAltaCliente.add(textoDNICliente);

		frameAltaCliente.add(labelCorreoCliente);
		textoCorreoCliente.setText("");
		frameAltaCliente.add(textoCorreoCliente);

		botonAceptarAltaCliente.addActionListener(this);
		frameAltaCliente.add(botonAceptarAltaCliente);
		botonCancelarAltaCliente.addActionListener(this);
		frameAltaCliente.add(botonCancelarAltaCliente);

		frameAltaCliente.setSize(200,300);
		frameAltaCliente.setVisible(true);
		frameAltaCliente.setResizable(false);
		frameAltaCliente.setLocationRelativeTo(null);
		frameAltaCliente.addWindowListener(this);
		textoNombreCliente.requestFocus();
	}

	@Override
	public void actionPerformed(ActionEvent evento) 
	{
		//AÑADIMOS LA FUNCIONALIDAD DL BOTON ACEPTAR ALTA CLIENTE
		if(evento.getSource().equals(botonAceptarAltaCliente))
		{
			//CONECTAMOS A LA BASE DE DATOS
			bd = new BaseDeDatos();
			connection = bd.conectar();
			try
			{
				//CREAMOS LA SENTENCIA
				statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
						ResultSet.CONCUR_READ_ONLY);
				//TOMAMOS EL TEXTO
				if(((textoNombreCliente.getText().length())!=0)
						&& 	((textoDirreccionCliente.getText().length())!=0)
						&&	((textoCorreoCliente.getText().length())!=0)
						&&	((textoDNICliente.getText().length())!=0))
				{
					//INGRESAMOS LOS DATOS DE LA TABLA CLIENTES DE LA BASE DE DATOS
					sentencia = "INSERT INTO clientes VALUES (null, '" + 
							textoNombreCliente.getText() + "','" + 
							textoDirreccionCliente.getText() + "','" + 
							textoDNICliente.getText() + "', '" +
							textoCorreoCliente.getText() + "')";
					System.out.println(sentencia);
					statement.executeUpdate(sentencia);
					labelMensajeAltaCliente.setText("Alta de Cliente Correcta");
				}
				else
				{
					labelMensajeAltaCliente.setText("FALLO: Faltan datos");
				}
			}
			catch (SQLException sqle)
			{
				labelMensajeAltaCliente.setText("Error en ALTA");
			}
			finally
			{
				dialogConfirmacionAltaCliente.setLayout(new FlowLayout());
				dialogConfirmacionAltaCliente.addWindowListener(this);
				dialogConfirmacionAltaCliente.setSize(200,100);
				dialogConfirmacionAltaCliente.setResizable(false);
				dialogConfirmacionAltaCliente.setLocationRelativeTo(null);
				dialogConfirmacionAltaCliente.add(labelMensajeAltaCliente);
				dialogConfirmacionAltaCliente.setVisible(true);
			}
		}
		//AÑADIMOS FUNCIONALIDAD AL BOTON CANCELAR
		else if (evento.getSource().equals(botonCancelarAltaCliente))
		{
			if(frameAltaCliente.isActive())
			{
				frameAltaCliente.setVisible(false);
			}
			else
			{
				System.exit(0);	
			}
		}
	}
	
	@Override
	public void windowClosing(WindowEvent arg0) 
	{
		if(frameAltaCliente.isActive())
		{
			frameAltaCliente.setVisible(false);
		}

		else if(dialogConfirmacionAltaCliente.isActive())
		{
			textoNombreCliente.setText("");
			textoDNICliente.setText("");
			textoNombreCliente.requestFocus();
			dialogConfirmacionAltaCliente.setVisible(false);
		}	
	}
	@Override
	public void windowActivated(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void windowClosed(WindowEvent arg0) {
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