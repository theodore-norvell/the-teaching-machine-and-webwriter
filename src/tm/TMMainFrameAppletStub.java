//     Copyright 1998--2010 Michael Bruce-Lockhart and Theodore S. Norvell
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License. 
// You may obtain a copy of the License at 
//
//     http://www.apache.org/licenses/LICENSE-2.0 
//
// Unless required by applicable law or agreed to in writing, 
// software distributed under the License is distributed on an 
// "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
// either express or implied. See the License for the specific language 
// governing permissions and limitations under the License.

package tm;
import java.awt.* ;
import java.applet.* ;
import java.io.File;
import java.io.InputStream;
import java.io.IOException;
import java.net.URL ;
import java.util.Enumeration ;
import java.util.Iterator ;

// TMMainFrameAppletStub
// ----------------------
//   The applet stub to use for the application version.
//   See core Java 2nd ed p 363.
/////////////////////////////////////////////////////////

public class TMMainFrameAppletStub implements AppletStub, AppletContext {

    private URL docBase ;
    private URL codeBase ;

    public TMMainFrameAppletStub(URL codeDir, URL docDir ) {
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