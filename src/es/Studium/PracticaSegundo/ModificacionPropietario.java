/*
 * Estructura de ModificacionPropietario
 */
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

public class ModificacionPropietario implements ActionListener, WindowListener
{
	//FRAME DE MODIFICACIÓN PROPIETARIO
	Frame frameModificacionPropietario = new Frame("Modificación");
	Label  labelMensajeModificarPropietario = new Label("Seleccionar el Propietario a Editar");
	Choice choModificarPropietarios = new Choice();
	Button botonModificarPropietario = new Button("Editar");
	Button botonModificarPropietarioCancelar = new Button("Cancelar");

	//FRAME DE MODIFICACIÓN EDITAR PROPIETARIO
	Frame frameModificacionPropietarioEdit = new Frame("Editar Propietario");
	Label labelModIdPropietario = new Label("Id:");
	TextField textoModIdPropietario = new TextField(20);
	Label labelModNombrePropietario = new Label("Nombre:");
	TextField textoModNombrePropietario = new TextField(20);
	Label labelModDireccionPropietario = new Label("Dirección:");
	TextField textoModDireccionPropietario = new TextField(20);
	Label labelModTelefonoPropietario = new Label("Telefono:");
	TextField textoModTelefonoPropietario = new TextField(20);
	Label labelModDNIPropietario = new Label("DNI:");
	TextField textoModDNIPropietario = new TextField(20);
	Button botonModificacionPropietarioAceptar = new Button("Aceptar");
	Button botonModificacionPropietarioCancelar = new Button("Cancelar");
	Dialog dialogConfirmarModPropietario = new Dialog(frameModificacionPropietarioEdit, "Modificación", true);
	Label labelMensajeModPropietario = new Label("Modificación de Propietario Completada");

	BaseDeDatos bd;
	String sentencia = "";
	String usuario;
	FicheroLog log = new FicheroLog();
	Connection connection = null;
	Statement statement = null;
	ResultSet rs = null;

	public ModificacionPropietario(String usuario)
	{
		this.usuario = usuario;
		bd = new BaseDeDatos();
		connection = bd.conectar();
		sentencia = "SELECT * FROM propietario";
		try
		{
			statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
					ResultSet.CONCUR_UPDATABLE);
			rs = statement.executeQuery(sentencia);
			while(rs.next())
			{
				choModificarPropietarios.add(rs.getInt("idPropietario")
						+"-"+rs.getString("nombrePropietario") +"-"+rs.getString("direccionPropietario")
						+"-"+rs.getString("telefonoPropietario")+"-"+rs.getString("dniPropietario")+"\n");
			}
		}
		catch (SQLException sqle)
		{
			labelMensajeModificarPropietario.setText("Error en MODIFICACIÓN");
		}

		frameModificacionPropietario.setLayout(new FlowLayout());
		frameModificacionPropietario.add(labelMensajeModificarPropietario);
		frameModificacionPropietario.add(choModificarPropietarios);
		botonModificarPropietario.addActionListener(this);
		botonModificarPropietarioCancelar.addActionListener(this);
		frameModificacionPropietario.add(botonModificarPropietario);
		botonModificarPropietarioCancelar.addActionListener(this);
		frameModificacionPropietario.add(botonModificarPropietarioCancelar);

		frameModificacionPropietario.setSize(600,200);
		frameModificacionPropietario.setResizable(false);
		frameModificacionPropietario.setLocationRelativeTo(null);
		frameModificacionPropietario.addWindowListener(this);
		frameModificacionPropietario.setVisible(true);

	}

	@Override
	public void actionPerformed(ActionEvent evento) 
	{
		if(evento.getSource().equals(botonModificarPropietario))
		{
			log.guardar(usuario, "Ha pulsado modificar cliente.");
			frameModificacionPropietarioEdit.setLayout(new FlowLayout());

			frameModificacionPropietarioEdit.add(labelModIdPropietario);
			labelModIdPropietario.setFocusable(false);
			frameModificacionPropietarioEdit.add(textoModIdPropietario);
			frameModificacionPropietarioEdit.add(labelModNombrePropietario);
			frameModificacionPropietarioEdit.add(textoModNombrePropietario);
			frameModificacionPropietarioEdit.add(labelModDireccionPropietario);
			frameModificacionPropietarioEdit.add(textoModDireccionPropietario);
			frameModificacionPropietarioEdit.add(labelModTelefonoPropietario);
			frameModificacionPropietarioEdit.add(textoModTelefonoPropietario);
			frameModificacionPropietarioEdit.add(labelModDNIPropietario);
			frameModificacionPropietarioEdit.add(textoModDNIPropietario);
			botonModificacionPropietarioAceptar.addActionListener(this);
			frameModificacionPropietarioEdit.add(botonModificacionPropietarioAceptar);
			botonModificacionPropietarioCancelar.addActionListener(this);
			frameModificacionPropietarioEdit.add(botonModificacionPropietarioCancelar);

			frameModificacionPropietarioEdit.setSize(210,350);
			frameModificacionPropietarioEdit.setResizable(false);
			frameModificacionPropietarioEdit.setLocationRelativeTo(null);
			frameModificacionPropietarioEdit.addWindowListener(this);
			frameModificacionPropietarioEdit.setVisible(true);

			String[] elegidoMod = choModificarPropietarios.getSelectedItem().split("-");
			textoModIdPropietario.setEditable(false);
			textoModIdPropietario.setText(elegidoMod[0]);
			textoModNombrePropietario.setText(elegidoMod[1]);
			textoModDireccionPropietario.setText(elegidoMod[2]);
			textoModTelefonoPropietario.setText(elegidoMod[3]);
			textoModDNIPropietario.setText(elegidoMod[4]);
		}
		else if(evento.getSource().equals(botonModificacionPropietarioAceptar))
		{
			log.guardar(usuario, "Ha aceptado la modificación del propietario");
			connection = bd.conectar();
			try
			{
				statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
						ResultSet.CONCUR_READ_ONLY);
				if(((textoModNombrePropietario.getText().length())!=0)
						&& 	((textoModDireccionPropietario.getText().length())!=0)
						&&	((textoModTelefonoPropietario.getText().length())!=0)
						&&	((textoModDNIPropietario.getText().length())!=0))
				{
					sentencia = "UPDATE propietario SET "
							+ "nombrePropietario='"+textoModNombrePropietario.getText()+"', "
							+ "direccionPropietario='"+textoModDireccionPropietario.getText()+ "', "
							+ "telefonoPropietario='"+textoModTelefonoPropietario.getText()+ "', "
							+ "dniPropietario='"+textoModDNIPropietario.getText()+ "' "
							+ "WHERE idPropietario="+textoModIdPropietario.getText();
					System.out.println(sentencia);
					log.guardar(usuario, sentencia);
					statement.executeUpdate(sentencia);
				}
				else
				{
					labelMensajeModPropietario.setText("Error campo vacío");
				}
			}
			catch (SQLException sqle)
			{
				labelMensajeModPropietario.setText("Error en MODIFICACIÓN");}
			finally
			{
				dialogConfirmarModPropietario.setLayout(new FlowLayout());
				dialogConfirmarModPropietario.addWindowListener(this);
				dialogConfirmarModPropietario.setSize(250,110);
				dialogConfirmarModPropietario.setResizable(false);
				dialogConfirmarModPropietario.setLocationRelativeTo(null);
				dialogConfirmarModPropietario.add(labelMensajeModPropietario);
				dialogConfirmarModPropietario.setVisible(true);
			}
		}
		else if(evento.getSource().equals(botonModificarPropietarioCancelar))
		{
			log.guardar(usuario, "Ha cancelado la modificación del propietario.");
			frameModificacionPropietario.setVisible(false);
		}
	}

	@Override
	public void windowClosing(WindowEvent evento) 
	{
		log.guardar(usuario, "Ha salido de Modificación Propietario.");
		if(frameModificacionPropietario.isActive())
		{
			frameModificacionPropietario.setVisible(false);
		}
		else if (frameModificacionPropietarioEdit.isActive())
		{
			frameModificacionPropietarioEdit.setVisible(false);
		}
		else if(dialogConfirmarModPropietario.isActive())
		{
			dialogConfirmarModPropietario.setVisible(false);
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