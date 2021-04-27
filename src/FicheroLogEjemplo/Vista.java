package FicheroLogEjemplo;

import java.awt.Button;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.TextField;

public class Vista
{
	Frame ventana = new Frame("Fichero de log");
	TextField txtUsuario = new TextField(30);
	Button btnMision = new Button("Misión");
	Button btnVision = new Button("Visión");
	Button btnValores = new Button("Valores");
	
	public Vista()
	{
		ventana.setLayout(new FlowLayout());
		
		ventana.add(txtUsuario);
		ventana.add(btnMision);
		ventana.add(btnVision);
		ventana.add(btnValores);
		
		ventana.setSize(264,104);
		ventana.setResizable(false);
		ventana.setLocationRelativeTo(null);
		ventana.setVisible(true);
	}
}
