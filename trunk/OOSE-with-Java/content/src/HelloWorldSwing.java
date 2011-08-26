import javax.swing.* ;

public class HelloWorldSwing extends JFrame {
	private JLabel label;
	
	HelloWorldSwing() {
		
		label = new JLabel("Hello world" ) ;
		label.setToolTipText( "Bonjour, tout le monde." ) ;
		add( label ) ;
		
		this.setDefaultCloseOperation( EXIT_ON_CLOSE ) ;
		
		pack() ;
		setVisible( true ) ;
	}

	public static void main(String[] args) {
		Runnable creator = new Creator() ;
		SwingUtilities.invokeLater(creator) ;
	}
}

class Creator implements Runnable {
	public void run() {
		new HelloWorldSwing() ;
	}
}
