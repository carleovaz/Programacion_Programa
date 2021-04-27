package FicheroLogEjemplo;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class Controlador implements ActionListener, WindowListener, ComponentListener
{
	Vista vista;
	Modelo modelo;
	
	public Controlador(Vista v, Modelo m)
	{
		this.vista = v;
		this.modelo = m;
		
		this.vista.ventana.addWindowListener(this);
		this.vista.ventana.addComponentListener(this);
		this.vista.btnMision.addActionListener(this);
		this.vista.btnVision.addActionListener(this);
		this.vista.btnValores.addActionListener(this);
	}

	@Override
	public void componentHidden(ComponentEvent arg0)
	{}

	@Override
	public void componentMoved(ComponentEvent arg0)
	{}

	@Override
	public void componentResized(ComponentEvent arg0)
	{
		System.out.println("Altura: " + this.vista.ventana.getHeight());
		System.out.println("Achura: " + this.vista.ventana.getWidth());
	}

	@Override
	public void componentShown(ComponentEvent arg0)
	{}

	@Override
	public void windowActivated(WindowEvent arg0)
	{}

	@Override
	public void windowClosed(WindowEvent arg0)
	{}

	@Override
	public void windowClosing(WindowEvent arg0)
	{
		System.exit(0);
	}

	@Override
	public void windowDeactivated(WindowEvent arg0)
	{}

	@Override
	public void windowDeiconified(WindowEvent arg0)
	{}

	@Override
	public void windowIconified(WindowEvent arg0)
	{}

	@Override
	public void windowOpened(WindowEvent arg0)
	{}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if(vista.btnMision.equals(e.getSource()))
		{
			modelo.guardar(vista.txtUsuario.getText(),"Has pulsado Misión");
		}
		else if (vista.btnVision.equals(e.getSource()))
		{
			modelo.guardar(vista.txtUsuario.getText(),"Has pulsado Visión");
		}
		else if (vista.btnValores.equals(e.getSource()))
		{
			modelo.guardar(vista.txtUsuario.getText(),"Has pulsado Valores");
		}
	}
}
