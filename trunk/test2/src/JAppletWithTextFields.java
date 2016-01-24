import java.applet.AppletStub;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.KeyboardFocusManager;
import java.awt.LayoutManager;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.BoxLayout;
import javax.swing.JApplet;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;


@SuppressWarnings("serial")
public class JAppletWithTextFields extends JApplet {

	JTextField field0 = new JTextField() ;
	JTextField field1 = new JTextField() ;
	
	public JAppletWithTextFields() {
		LayoutManager lm = new BoxLayout(this.getContentPane(), BoxLayout.PAGE_AXIS) ;
		setLayout( lm );
        field0.setAlignmentX(Component.CENTER_ALIGNMENT);
        field1.setAlignmentX(Component.CENTER_ALIGNMENT);
		add(field0) ;
		add(field1) ;
		setSize( 100, 100 ) ;
	}
	
	static JFrame frame ;
	static JAppletWithTextFields applet ;
	static boolean withFrame = false ;
	static boolean lateFrame = true ;
	
	static public void main( String [] args ) throws InvocationTargetException, InterruptedException {
		System.out.println( "Running" ) ;
		
		SwingUtilities.invokeAndWait( new Runnable() {
			@Override public void run() {
				applet = makeApplet();
				System.out.println( "Applet created 0" ) ;
				if( applet == null ) return ;
				
				if( withFrame ) {
					frame = new JFrame() ;
					frame.add( applet, BorderLayout.CENTER ) ;
					frame.setVisible( true ); 
					frame.pack();
				}
				else {
				}
				KeyboardFocusManager kfm = KeyboardFocusManager.getCurrentKeyboardFocusManager() ;
				System.out.println( kfm ) ;
				System.out.println( kfm.getFocusOwner() ) ;
			}
		});
		
		System.out.println( "Applet created 1" ) ;
		
		Component source = applet ;
		
		postKeyEvent( source, KeyEvent.KEY_TYPED, 0, 0, KeyEvent.VK_UNDEFINED, 'a' ) ;
		
		Thread.sleep(10); 
		System.out.println( "Event posted 0" ) ;
		
		SwingUtilities.invokeAndWait( new Runnable() {
			@Override public void run() {
				applet.field1.requestFocus();
			}
		} ) ;
		
		Thread.sleep(10); 
		
		postKeyEvent( applet.field1, KeyEvent.KEY_TYPED, 0, 0, KeyEvent.VK_UNDEFINED, 'b') ;
		
		Thread.sleep(10); 
		
		SwingUtilities.invokeAndWait( new Runnable() {
			@Override public void run() {
				System.out.println( "Field0: " + applet.field0.getText() ) ;
				System.out.println( "Field1: " + applet.field1.getText() ) ;
			}
		});

		
		Thread.sleep(10); 
		
		SwingUtilities.invokeAndWait( new Runnable() {
			@Override public void run() {
				if( lateFrame ) {
					frame = new JFrame() ;
					frame.add( applet, BorderLayout.CENTER ) ;
					frame.setVisible( true ); 
					frame.pack();
				}
			}
		});
	}
	
	private static void postKeyEvent(
			final Component source,
			final int id,
			final long when,
			final int modifiers,
			final int keyCode,
			final char ch )
	throws InvocationTargetException, InterruptedException {
		SwingUtilities.invokeAndWait( new Runnable() {
			@Override public void run() {
				Toolkit tk = Toolkit.getDefaultToolkit() ;
				EventQueue eq = tk.getSystemEventQueue() ;
				KeyEvent event = new KeyEvent(source, id, when, modifiers, keyCode, ch) ;
				eq.postEvent( event );
			}
		});	
	}

	private static JAppletWithTextFields makeApplet() {
		JAppletWithTextFields applet = new JAppletWithTextFields() ;
		try {
			AppletStub stub = new ServerSideAppletStub(new URL("file://"), new URL("file://")) ;
			applet.setStub(stub);}
		catch (MalformedURLException e) {
			e.printStackTrace();
			return null ; }
		return applet;
	}
}
