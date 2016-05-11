package tm;

import java.awt.Component;
import java.awt.Image;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.JFrame;

import tm.interfaces.Inputter;
import tm.interfaces.PlatformServicesInterface;
import tm.interfaces.TMStatusCode;

public class ApplicationPlatform implements PlatformServicesInterface {
	
	private URL docBase ;
	private URL codeBase ;

	public ApplicationPlatform(URL codeDir, URL docDir ) {
		docBase = docDir ;
		codeBase = codeDir ; }


	@Override
	public Image fetchImage(String name) {
		// Look for images relative to the tm directory.
        java.net.URL imgURL = ApplicationPlatform.class.getResource(name);
        if (imgURL != null) {
            return java.awt.Toolkit.getDefaultToolkit().createImage( imgURL ); }
        else {
            System.err.println("Couldn't find file: " + name);
            return null; } }


	@Override
	public URL makeURL(String root) {
		try {
			return new URL( getDocumentBase(), root ) ; }
		catch( MalformedURLException e ) {
			return null ; }

	}

	@Override
	public URL getDocumentBase() {
		return docBase ;
	}

	@Override
	public boolean isApplet() {
		return false;
	}

	@Override
	public URL getCodeBase() {
		return codeBase ;
	}

	@Override
	public void showDocument(TMMainPanel tmMainPanel, URL helpURL, String windowName) {
		String helpFile = helpURL.toExternalForm() ;
		//System.err.println( "HelpFile is <"+helpFile+">") ;
		String[] browserName = { 
				"rundll32 url.dll,FileProtocolHandler",
				"firefox",
				"netscape",
				"opera",
				"konqueror",
				"safari",
				"Iexplore.exe",
				"netscape.exe",
				"firefox.exe"
		} ;
		boolean success = false ;
		for( int i=0 ; !success && i<browserName.length ; ++i ) {
			success = tryLaunch( browserName[i], helpFile ) ; }
		if( !success ) {
			showMessage(tmMainPanel, "Failed", "The Teaching Machine could not launch the browser.\n" +
					"For help, paste the following URL into your browser:\n   " +
					helpFile ) ; } }

	private boolean tryLaunch( String browser, String helpFile ) {
		try {
			String launchString = browser + " " + helpFile ;
			//System.err.println("Launching: <" + launchString + ">" ) ;
			Process p = Runtime.getRuntime().exec(launchString) ;
			return true ; }
		catch( java.io.IOException e ) {
			//e.printStackTrace() ;
			return false ; } }



	@Override
	public void showMessage(TMMainPanel tmMainPanel, String title, String message, Throwable th) {
		AttentionFrame af = new AttentionFrame(title, message, th) ;
		tmMainPanel.showDialog( af );
	}

	@Override
	public void showMessage(TMMainPanel tmMainPanel, String title, String message) {
		AttentionFrame af = new AttentionFrame(title, message, (String) null) ;
		tmMainPanel.showDialog( af );
	}

	@Override
	public Inputter getInputter() {
		// TODO make sure this doesn't create a frame.
		return new SwingInputter();
	}

	@Override
	public void showTheTM(TMMainPanel tmMainPanel, boolean visible) {
		Component c = tmMainPanel.getComponent() ;
		while( c != null && ! (c instanceof JFrame) ) c = c.getParent() ;
		if( c == null ) return ;
		JFrame frame = (JFrame) c ;
        frame.setVisible( visible ) ;
        if( visible ) {
            int state = frame.getExtendedState() ;
            if( (state & JFrame.ICONIFIED) != 0 )
                frame.setExtendedState( state & ~ JFrame.ICONIFIED ) ;
            frame.toFront() ; }
	}

	@Override
	public boolean isTMShowing(TMMainPanel tmMainPanel) {
		Component c = tmMainPanel.getComponent() ;
		while( c != null && ! (c instanceof JFrame) ) c = c.getParent() ;
		if( c == null ) return false;
		JFrame frame = (JFrame) c ;
		return frame.isVisible() ;
	}

	@Override
	public void exit() {
		System.exit(0) ;
	}

}
