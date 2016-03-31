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


import javax.swing.JApplet;

import netscape.javascript.*; // Need plugin.jar from JDK on the CLASSPATH.


//  TMBigApplet  //
///////////////////
/** An applet that holds a TMMainPanel
*/

public class TMBigApplet extends JApplet {

	private final TMMainPanel tmMainPanel ;
	
	// CONSTRUCTORS //
	//////////////////
	public TMBigApplet() {
		tmMainPanel = new TMMainPanel() ;
	}

	public TMBigApplet(javax.swing.JMenu view, ArgPackage argPackage ) {
		tmMainPanel = new TMMainPanel( view, argPackage ) ;
	}


// OVERIDES OF APPLET //
////////////////////////

	public void start() {
		tmMainPanel.start() ;

		callBackToJavaScript(this) ; 
	}

	public void init() {
		tmMainPanel.init() ;
	}

    @Override public void destroy() {
        dispose() ;
        super.stop() ; // Not sure this does anything, but it can't hurt.
    }
    
    public void dispose() {
    	tmMainPanel.dispose() ;	
    }
    

    // Services provided to the Panel.
    
    
    /** Try to call a JS method of the window to let JS scripts know the
      * has been initialized
      * Note callBackToJavaScript should only be called if we are running as an applet.
      * This is because if relies on class netscape.JSObject which 
      * does not exist in the standard JRE
      */
    static void callBackToJavaScript(final JApplet thisApplet) {
    	if( !( thisApplet.getAppletContext() instanceof TMMainFrameAppletStub ) ) {
    		Thread t = new Thread() {
    			@Override public void run() {
    				try {
    					JSObject window = JSObject.getWindow(thisApplet);
    					System.out.println("Evaluating 'theTMIsInitialized()'") ;    
    					window.eval("theTMIsInitialized()") ;
    					System.out.println("Evaluation sucessfull.") ;  }
    				catch( Throwable jse) {
    					System.out.println("Evaluation failed.") ;
    					jse.printStackTrace(); } } } ;
    		t.start() ; }
    }
}