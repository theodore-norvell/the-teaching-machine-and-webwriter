import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.* ;

public class ClickExample extends JFrame {
	private int currentValue = 0 ;
	JButton button ;
	JLabel label ;
	
	ClickExample() {
		button = new JButton("Increment" ) ;
		label = new JLabel(Integer.toString(currentValue)) ;
		add( button, BorderLayout.CENTER ) ; 
		add( label, BorderLayout.SOUTH ) ;
		
		button.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					currentValue += 1 ;
					label.setText(Integer.toString(currentValue)) ;
				}
			}
		) ;
		
		this.setDefaultCloseOperation( EXIT_ON_CLOSE ) ;
		
		pack() ;
		setVisible( true ) ;
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater( new Runnable() {
		    public void run() { new ClickExample() ; } } ) ;
	}
}
