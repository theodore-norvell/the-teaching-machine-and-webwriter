import java.applet.Applet;
import java.applet.AppletContext;
import java.applet.AppletStub;
import java.applet.AudioClip;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.Iterator;


class ServerSideAppletStub implements AppletStub, AppletContext {

    private URL docBase ;
    private URL codeBase ;

    public ServerSideAppletStub(URL codeDir, URL docDir ) {
        docBase = docDir ;
        codeBase = codeDir ; }

// Implementing AppletStub //
/////////////////////////////
    public boolean isActive() { return true ; }
    public URL getCodeBase() { return codeBase ; }
    public String getParameter( String name ) { return null ; }
    public AppletContext getAppletContext() { return this ; }
    public void appletResize( int width, int height) {}
    public URL getDocumentBase() { return docBase ; }

// Implementing AppletContext //
////////////////////////////////
    public AudioClip getAudioClip(URL url) { return null ; }
    public Applet getApplet( String name ) { return null ; }
    public Enumeration getApplets() { return null ; }

    public void showDocument( URL url ) {
    }

    public void showDocument( URL url, String target ) { showDocument(url); }
    public void showStatus( String status ) { }
    public Image getImage(URL url) {
                Toolkit tk = Toolkit.getDefaultToolkit( ) ;
                return tk.getImage( url ) ; }
    public void setStream(String key, InputStream stream) throws IOException {}
    public InputStream getStream(String key) { return null ; }
    public Iterator getStreamKeys() { return null ; }
}