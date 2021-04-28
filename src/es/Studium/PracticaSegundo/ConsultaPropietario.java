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
