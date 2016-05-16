package tm;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.HeadlessException;
import java.awt.Rectangle ;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.Scrollable ;
import javax.swing.SwingUtilities;
import javax.swing.UIManager ;

public class TestProgram extends JFrame {
	
	

	private JScrollPane scroll;
	private ScrollablePanel mainPanel;
	

	
	JLabel titleLabel = new JLabel() ;
    JTextArea topTextArea = new JTextArea(5,40) ;
    JPanel buttonPanel = new JPanel() ;
    JButton closeButton = new JButton("Close") ;
    JButton detailButton = new JButton("Show details") ;
    JTextArea bottomTextArea = new JTextArea(20,40) ;

	public TestProgram() throws HeadlessException {
		super() ;
		
		String detail = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.\n\n" ;
		detail = detail.concat( detail ) ;
		detail = detail.concat( detail ) ;
		
		topTextArea.setText( detail ) ; 
		topTextArea.setWrapStyleWord( true ) ;
		topTextArea.setLineWrap( true );
        topTextArea.setColumns( 40 );
        
		bottomTextArea.setText( detail ) ;
		bottomTextArea.setWrapStyleWord( true ) ;
		bottomTextArea.setLineWrap( true );
		bottomTextArea.setColumns( 40 );
		
		mainPanel = new ScrollablePanel() ;
		mainPanel.setLayout( new BoxLayout( mainPanel, BoxLayout.Y_AXIS ) ) ;
		
		
		titleLabel.setText( "Hello" ); 
		
		buttonPanel.setLayout( new FlowLayout() ) ;
		buttonPanel.add( closeButton ) ;
		if( detail != null ) buttonPanel.add( detailButton ) ;

		mainPanel.add( titleLabel ) ;
		mainPanel.add( topTextArea ) ;
		mainPanel.add( buttonPanel ) ;
		
		scroll = new JScrollPane(mainPanel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER) ;
		this.getContentPane().add( scroll ) ;
		


        detailButton.addActionListener( new ActionListener() {
            private boolean detailsAreShowing;

			public void actionPerformed(ActionEvent arg0) {
                if( detailsAreShowing ) {
                	mainPanel.remove(bottomTextArea) ;
                    detailButton.setText( "Show details" ) ;
                    detailsAreShowing = false ; }
                else {
                	mainPanel.add(bottomTextArea) ;
                    detailButton.setText( "Hide details" ) ;
                    detailsAreShowing = true ; }
                }} ) ;
	}
	
	@SuppressWarnings("serial")
    private static class ScrollablePanel extends JPanel implements Scrollable {
	    public Dimension getPreferredScrollableViewportSize() {
	        return super.getPreferredSize(); //tell the JScrollPane that we want to be our 'preferredSize' - but later, we'll say that vertically, it should scroll.
	    }

	    public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
	        return 16;
	    }

	    public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
	        return 16;
	    }

	    public boolean getScrollableTracksViewportWidth() {
	        return true;//track the width, and re-size as needed.
	    }

	    public boolean getScrollableTracksViewportHeight() {
	        return false; //we don't want to track the height, because we want to scroll vertically.
	    }
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater( new Runnable() {
		    @Override
		    public void run() {
		        UIManager.LookAndFeelInfo[]lookAndFeelArray = UIManager.getInstalledLookAndFeels();
		        for (int i = 0; i < lookAndFeelArray.length; i++) {
		            if (lookAndFeelArray[i].getName().equals("Nimbus")) {
		                try {
		                    UIManager.setLookAndFeel(lookAndFeelArray[i].getClassName()); }
		                catch (Exception e) {
		                    System.err.println("Could not set the look and feel to "+lookAndFeelArray[i].getClassName()) ;
		                    e.printStackTrace(); }
		                break ; } }

		        TestProgram test = new TestProgram() ;
		        test.setSize(200, 300);
		        test.pack(); 
		        test.setVisible( true ) ;
		    }
		});
	}

}
