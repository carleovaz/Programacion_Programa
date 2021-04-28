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

public class BajaPropietario implements ActionListener, WindowListener
{
	//CREAMOS EL FRAME Y SUS RESPECTIVOS OBJETOS
	Frame frameBajaPropietario = new Frame("Baja de Propietario");
	Label labelMensajeBajaPropietario = new Label("Seleccionar el propietario:");
	Choice choPropietarios = new Choice();
	Button botonBorrarPropietario = new Button("Borrar");
	Dialog dilogSeguroPropietario = new Dialog(frameBajaPropietario, "AVISO", true);
	Label labelSeguroPropietario = new Label("¿Está seguro de querer borrar este propietario?");
	Button botonSiSeguroPropietario = new Button("Sí");
	Button botonNoSeguroPropietario = new Button("No");
	Dialog dialogConfirmacionBajaPropietario = new Dialog(frameBajaPropietario, "Baja propietario", true);
	Label labelConfirmacionBajaPropietario = new Label("Baja de propietario realizada");	
	
	BaseDeDatos bd;
	String sentencia = "";
	String usuario;
	FicheroLog log = new FicheroLog();
	Connection connection = null;
	Statement statement = null;
	ResultSet rs = null;

	public BajaPropietario(String usuario)
	{
		this.usuario = usuario;
		//CONECTAMOS A LA BASE DE DATOS
		frameBajaPropietario.setLayout(new FlowLayout());
		frameBajaPropietario.add(labelMensajeBajaPropietario);
		bd = new BaseDeDatos();
		connection = bd.conectar();

		sentencia = "SELECT * FROM propietario";
		try
		{
			//CREAMOS SENENCIA
			statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			rs = statement.executeQuery(sentencia);
			choPropietarios.removeAll();
			while(rs.next())
			{
				//BUSCAMOS LOS DATOS DE LOS PROPIETARIOS
				choPropietarios.add(rs.getInt("idPropietario")
						+"-"+rs.getString("nombrePropietario") +"-"+rs.getString("direccionPropietario")
						+"-"+rs.getString("telefonoPropietario")+"-"+rs.getString("dniPropietario"));
			}
		}
		
		catch (SQLException sqle)
		{}
		//AÑADIMOS EL FRAME DE DAR DE BAJA AL PROPIETARIO
		frameBajaPropietario.add(choPropietarios);
		botonBorrarPropietario.addActionListener(this);
		frameBajaPropietario.add(botonBorrarPropietario);
		frameBajaPropietario.setSize(600,150);
		frameBajaPropietario.setResizable(false);
		frameBajaPropietario.setLocationRelativeTo(null);
		frameBajaPropietario.addWindowListener(this);
		frameBajaPropietario.setVisible(true);
	}


	@Override
	public void actionPerformed(ActionEvent evento) 
	{
		if(evento.getSource().equals(botonBorrarPropietario))
		{
			log.guardar(usuario, "Ha pulsado borrar Propietario");
			dilogSeguroPropietario.setLayout(new FlowLayout());
			dilogSeguroPropietario.addWindowListener(this);
			dilogSeguroPropietario.setSize(290,100);
			dilogSeguroPropietario.setResizable(false);
			dilogSeguroPropietario.setLocationRelativeTo(null);
			dilogSeguroPropietario.add(labelSeguroPropietario);
			botonSiSeguroPropietario.addActionListener(this);
			dilogSeguroPropietario.add(botonSiSeguroPropietario);
			botonNoSeguroPropietario.addActionListener(this);
			dilogSeguroPropietario.add(botonNoSeguroPropietario);
			dilogSeguroPropietario.setVisible(true);
		}
		//AÑADIMOS FUNCIONALIDAD AL BOTON DE SEGURO NO
		else if(evento.getSource().equals(botonNoSeguroPropietario))
		{
			log.guardar(usuario, "Ha pulsado NO, ha cancelado el borrado");
			dilogSeguroPropietario.setVisible(false);
		}
		//AÑADIMOS FUNCIONALIDAD AL BOTON DE SEGURO SI
		else if(evento.getSource().equals(botonSiSeguroPropietario))
		{
			log.guardar(usuario, "Ha pulsado SI, ha borrado el propietario.");
			bd = new BaseDeDatos();
			connection = bd.conectar();
			String[] elegido = choPropietarios.getSelectedItem().split("-");
			//BUSCAMOS EN NUESTRA BASE DE DATOS
			sentencia = "DELETE FROM propietario WHERE idPropietario = "+elegido[0];
			try
			{
				statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
						ResultSet.CONCUR_READ_ONLY);
				System.out.println(sentencia);
				log.guardar(usuario,sentencia);
				statement.executeUpdate(sentencia);
				labelConfirmacionBajaPropietario.setText("Baja de Propietario Correcta");
			}
			catch (SQLException sqle)
			{
				log.guardar(usuario, "El Propietario no puede ser borrado, tiene enlazado algún dato que debes borrar primero.");
				labelConfirmacionBajaPropietario.setText("Error en Baja");
			}
			finally
			{
				dialogConfirmacionBajaPropietario.setLayout(new FlowLayout());
				dialogConfirmacionBajaPropietario.addWindowListener(this);
				dialogConfirmacionBajaPropietario.setSize(260,100);
				dialogConfirmacionBajaPropietario.setResizable(false);
				dialogConfirmacionBajaPropietario.setLocationRelativeTo(null);
				dialogConfirmacionBajaPropietario.add(labelConfirmacionBajaPropietario);
				dialogConfirmacionBajaPropietario.setVisible(true);
			}
		}
		
	}
	
	@Override
	public void windowClosing(WindowEvent e) 
	{
		if(frameBajaPropietario.isActive())
		{
			frameBajaPropietario.setVisible(false);
		}
		else if(dilogSeguroPropietario.isActive())
		{
			dilogSeguroPropietario.setVisible(false);
		}
		else if(dialogConfirmacionBajaPropietario.isActive())
		{
			dialogConfirmacionBajaPropietario.setVisible(false);
			dilogSeguroPropietario.setVisible(false);
			frameBajaPropietario.setVisible(false);
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


