/*
 * Estructura de AltaPropietario
 */

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

public class AltaPropietario implements ActionListener, WindowListener
{
	//CREAMOS EL FRAME Y SUS RESPECTIVOS OBJETOS
	Frame frameAltaPropietario = new Frame("Alta de Propietario");
	Label labelNombrePropietario = new Label("Nombre:");
	TextField textoNombrePropietario = new TextField(20);
	Label labelDireccionPropietario = new Label ("Dirección:");
	TextField textoDirreccionPropietario = new TextField(20);
	Label labelTelefonoPropietario = new Label ("Telefono:");
	TextField textoTelefonoPropietario = new TextField(20);
	Label labelDNIPropietario = new Label ("DNI:");
	TextField textoDNIPropietario = new TextField(20);
	Button botonAceptarAltaPropietario = new Button("Dar Alta");
	Button botonCancelarAltaPropietario = new Button ("Cancelar");
	Dialog dialogConfirmacionAltaPro = new Dialog (frameAltaPropietario, "Alta Propietario");
	Label labelMensajeAltaPropietario = new Label ("Alta de Propietario realizada");
	
	BaseDeDatos bd;
	String sentencia = "";
	String usuario;
	FicheroLog log = new FicheroLog();
	Connection connection = null;
	Statement statement = null;
	ResultSet rs = null;

	//CONSTRUCTORES
	public AltaPropietario(String usuario)
	{
		this.usuario = usuario;
		frameAltaPropietario.setLayout(new FlowLayout());

		frameAltaPropietario.add(labelNombrePropietario);
		textoNombrePropietario.setText("");
		frameAltaPropietario.add(textoNombrePropietario);

		frameAltaPropietario.add(labelDireccionPropietario);
		textoDirreccionPropietario.setText("");
		frameAltaPropietario.add(textoDirreccionPropietario);

		frameAltaPropietario.add(labelTelefonoPropietario);
		textoTelefonoPropietario.setText("");
		frameAltaPropietario.add(textoTelefonoPropietario);

		frameAltaPropietario.add(labelDNIPropietario);
		textoDNIPropietario.setText("");
		frameAltaPropietario.add(textoDNIPropietario);

		botonAceptarAltaPropietario.addActionListener(this);
		frameAltaPropietario.add(botonAceptarAltaPropietario);
		botonCancelarAltaPropietario.addActionListener(this);
		frameAltaPropietario.add(botonCancelarAltaPropietario);

		frameAltaPropietario.setSize(200,300);
		frameAltaPropietario.setVisible(true);
		frameAltaPropietario.setResizable(false);
		frameAltaPropietario.setLocationRelativeTo(null);
		frameAltaPropietario.addWindowListener(this);
		textoNombrePropietario.requestFocus();
	}
	
	//FUNCIONALIDAD DE LOS BOTONES
	@Override
	public void actionPerformed(ActionEvent evento) 
	{
		if(evento.getSource().equals(botonAceptarAltaPropietario))
		{
			log.guardar(usuario, "Ha pulsado Aceptar Alta Propietario.");
			bd = new BaseDeDatos();
			connection = bd.conectar();
			try
			{
				//CREAMOS LA SENTENCIA
				statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
						ResultSet.CONCUR_READ_ONLY);
				//TOMAMOS EL TEXTO
				if(((textoNombrePropietario.getText().length())!=0)
						&& 	((textoDirreccionPropietario.getText().length())!=0)
						&&	((textoTelefonoPropietario.getText().length())!=0)
						&&	((textoDNIPropietario.getText().length())!=0))
				{
					//INGRESAMOS LOS DATOS DE LA TABLA CLIENTES DE LA BASE DE DATOS
					sentencia = "INSERT INTO propietario VALUES (null, '" + 
							textoNombrePropietario.getText() + "','" + 
							textoDirreccionPropietario.getText() + "','" + 
							textoTelefonoPropietario.getText() + "', '" +
							textoDNIPropietario.getText() + "')";
					System.out.println(sentencia);
					log.guardar(usuario, sentencia);
					statement.executeUpdate(sentencia);
				}
				else
				{
					labelMensajeAltaPropietario.setText("FALLO: Faltan datos");
				}
			}
			catch (SQLException sqle)
			{
				labelMensajeAltaPropietario.setText("Error en ALTA");
			}
			finally
			{
				dialogConfirmacionAltaPro.setLayout(new FlowLayout());
				dialogConfirmacionAltaPro.addWindowListener(this);
				dialogConfirmacionAltaPro.setSize(230,100);
				dialogConfirmacionAltaPro.setResizable(false);
				dialogConfirmacionAltaPro.setLocationRelativeTo(null);
				dialogConfirmacionAltaPro.add(labelMensajeAltaPropietario);
				dialogConfirmacionAltaPro.setVisible(true);
			}
		}
		
		if (evento.getSource().equals(botonCancelarAltaPropietario))
		{
			log.guardar(usuario, "Ha pulsado Cancelar Alta Propietario.");
			if(frameAltaPropietario.isActive())
			{
				frameAltaPropietario.setVisible(false);
			}
			else
			{
				System.exit(0);	
			}

		}
		
	}
	@Override
	public void windowClosing(WindowEvent e) 
	{
		if(frameAltaPropietario.isActive())
		{
			frameAltaPropietario.setVisible(false);
		}

		else if(dialogConfirmacionAltaPro.isActive())
		{
			dialogConfirmacionAltaPro.setVisible(false);
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