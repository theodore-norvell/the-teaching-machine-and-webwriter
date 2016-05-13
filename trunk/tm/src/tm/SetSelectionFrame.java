package tm;

import java.awt.FlowLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import tm.utilities.ApologyException;
import tm.virtualMachine.Selection;
import tm.virtualMachine.SelectionParser;

@SuppressWarnings("serial")
public class SetSelectionFrame extends TMDialog {

	JLabel titleLabel = new JLabel() ;
	JLabel promptLabel = new JLabel("<html>Enter the new selection.<br>"+
                                    "Use ! for 'not', &amp for 'and', and | for 'or'.<br>"+
                                    "'s' is for scripts, 'l' is for library.</html>" ) ;
	JTextField field = new JTextField( 30 ) ;
    JLabel couldNotParse = new JLabel( "Could not parse the string." ) ;
	JPanel buttonPanel = new JPanel() ;
    JButton closeButton = new JButton("Ok") ;
    JButton cancelButton = new JButton("Cancel") ;
	
	public SetSelectionFrame( final TMMainPanel tmMainPanel ){
		super(tmMainPanel) ; 
		titleLabel.setText( "Set the selection" ); 
		
		buttonPanel.setLayout( new FlowLayout() ) ;
		buttonPanel.add( closeButton ) ;
		buttonPanel.add( cancelButton ) ;
		
		mainPanel.add( titleLabel ) ;
		mainPanel.add( promptLabel ) ;
		mainPanel.add( field ) ;
		mainPanel.add( buttonPanel ) ;
 		
		field.setText( tmMainPanel.getSelectionString() );
		
		closeButton.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
            	Selection sel = null ;
            	String str = field.getText();
            	try { sel = SelectionParser.parse( str ) ; }
    			catch(ApologyException apology) { }
    			if( sel == null ) {
    				mainPanel.add( couldNotParse) ;
    				mainPanel.revalidate(); 
    				javax.swing.Timer t = new javax.swing.Timer( 2000, new ActionListener() {
						@Override public void actionPerformed(ActionEvent e) {
						    mainPanel.remove( couldNotParse ) ;
		                    mainPanel.revalidate(); 
						}
					}) ;
    				t.setRepeats( false );
    				t.start();
    			} 
    			else {
    				tmMainPanel.setSelection( sel );
    				dismiss() ; } } } ) ;
		
		cancelButton.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                dismiss() ; }} ) ;
		
		
        this.revalidate();
	}

}
