package Visao;

import javax.swing.JFrame;

import modelo.Tabuleiro;

@SuppressWarnings("serial")
public class TelaPrincipal  extends JFrame{

	public TelaPrincipal() {
		Tabuleiro tabuleiro = new Tabuleiro(20, 30, 50);
		
		add( new PainelTabuleiro(tabuleiro));
		
		setTitle("Campo Minado Art");
		setSize(690, 430);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setVisible(true);
		
	}
	
	public static void main(String[] args) {
		
		new TelaPrincipal();
		
	}
}
