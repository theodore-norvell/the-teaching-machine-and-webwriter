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
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import java.awt.Image;
import java.awt.event.* ;
import java.applet.* ;
import java.io.File;
import java.lang.reflect.InvocationTargetException;

import tm.interfaces.ExternalCommandInterface;
import tm.utilities.ConcurUtilities;


public class TMTinyApplet extends JApplet
                          implements ExternalCommandInterface,
                                     AppletStub {

    private TMMainFrame tmMainFrame = null ;

// OVERIDES OF APPLET //
////////////////////////

    /** Note: init should only be called if we are running as an applet.
     * This is because if relies on class netscape.JSObject which 
     * does not exist in the standard JRE
     */
     public void init() {
    	System.out.println("The TMTinyApplet has recieved an 'init' message") ;    
    	setLayout(null);	
    	TMBigApplet.setLookAndFeel( this ) ;
    }
     
    public void start() {
    	System.out.println("The TMTinyApplet has recieved a 'start' message") ;    
    	TMBigApplet.callBackToJavaScript(this) ;
    }

    public void destroy() {
        if( tmMainFrame != null ) tmMainFrame.dispose() ;
    }

// Implementing AppletStub //
/////////////////////////////

    public void appletResize(int width, int height ) { }

    
// Implementing ExternalCommandInterface //
///////////////////////////////////////////

    
    public void setTestMode( boolean turnOn ) {
        if( tmMainFrame == null ) makeTheFrame() ;
        tmMainFrame.setTestMode( turnOn ) ; }
    
    public int getStatusCode() {
        if( tmMainFrame == null ) makeTheFrame() ;
        return tmMainFrame.getStatusCode() ; }
    
    public String getStatusMessage() {
        if( tmMainFrame == null ) makeTheFrame() ;
        return tmMainFrame.getStatusMessage() ; }

    public void loadString( String fileName, String programSource) {
        if( tmMainFrame == null ) makeTheFrame() ;
        tmMainFrame.loadString( fileName, programSource ) ;
        showTM(true) ; }

    public void loadRemoteFile( String fileName) {
        if( tmMainFrame == null ) makeTheFrame() ;
        tmMainFrame.loadRemoteFile( fileName ) ;
        showTM(true) ; }
    
    public void loadRemoteFile( String root, String fileName) {
        if( tmMainFrame == null ) makeTheFrame() ;
        tmMainFrame.loadRemoteFile( root, fileName ) ;
        showTM(true) ; }
    
    public void loadLocalFile( File directory, String fileName) {
    	 if( tmMainFrame == null ) makeTheFrame() ;
    	 tmMainFrame.loadLocalFile( directory, fileName ) ; }

    public void readRemoteConfiguration( String fileName ) {
        if( tmMainFrame == null ) makeTheFrame() ;
        tmMainFrame.readRemoteConfiguration( fileName ) ; }

    public void clearRemoteDataFiles() {
        if( tmMainFrame == null ) makeTheFrame() ;
        tmMainFrame.clearRemoteDataFiles( ) ; }

    public void registerRemoteDataFile(String fileName) {
        if( tmMainFrame == null ) makeTheFrame() ;
        tmMainFrame.registerRemoteDataFile( fileName ) ; }

    public void addInputString( String input) {
        if( tmMainFrame == null ) makeTheFrame() ;
        tmMainFrame.addInputString( input ) ; }

    public void addProgramArgument(String argument) {
        if( tmMainFrame == null ) makeTheFrame() ;
        tmMainFrame.addProgramArgument( argument ) ; }
    
    public String getOutputString( ) {
        if( tmMainFrame == null ) makeTheFrame() ;
        return tmMainFrame.getOutputString() ; }
    
    public void reStart() {
        if( tmMainFrame != null ) tmMainFrame.reStart() ; }

    public void editCurrentFile() {
        if( tmMainFrame != null ) tmMainFrame.editCurrentFile() ;
    }
    public void quit() {
        if( tmMainFrame != null ) tmMainFrame.quit() ; }
    
    public void initializeTheState() {
        if( tmMainFrame != null ) tmMainFrame.initializeTheState() ; }

    public void goBack() {
        if( tmMainFrame != null ) tmMainFrame.goBack() ; }

    public void redo() {
        if( tmMainFrame != null ) tmMainFrame.redo() ; }

    public void go( String commandString ) {
    	if( tmMainFrame != null ) tmMainFrame.go( commandString ) ; }
    
    public void goForward(){
        if( tmMainFrame != null ) tmMainFrame.goForward() ; }

    public void microStep(){
        if( tmMainFrame != null ) tmMainFrame.microStep() ; }

    public void overAll(){
        if( tmMainFrame != null ) tmMainFrame.overAll() ; }

    public void intoExp(){
        if( tmMainFrame != null ) tmMainFrame.intoExp() ; }

    public void intoSub(){
        if( tmMainFrame != null ) tmMainFrame.intoSub() ; }

    public void toCursor( String fileName, int cursor ){
        if( tmMainFrame != null ) tmMainFrame.toCursor( fileName, cursor ) ; }
    
    public void toBreakPoint() {
        if( tmMainFrame != null ) tmMainFrame.toBreakPoint() ; }

    public void autoStep() {
        if( tmMainFrame != null ) tmMainFrame.autoStep() ;
    }

    public void autoStep(String fileName, int lineNo) {
        if( tmMainFrame != null ) tmMainFrame.autoStep( fileName, lineNo ) ; 
    }

    public void setAutoStepRate(int rateConstant) {
        if( tmMainFrame != null ) tmMainFrame.setAutoStepRate( rateConstant ) ;
    }

    public void autoRun() {
        if( tmMainFrame != null ) tmMainFrame.autoRun() ;
    }

    public void stopAuto() {
        if( tmMainFrame != null ) tmMainFrame.stopAuto() ;
    }

    public void showTM(boolean visible) {
        if( tmMainFrame == null ) makeTheFrame() ;
        tmMainFrame.showTM(visible) ;
    }
    
    public boolean isTMShowing() {
        if( tmMainFrame != null ) return tmMainFrame.isTMShowing() ;
        else return false ;
    }

    public void setSelectionString( String selectionString ) {
        // May throw apology.
        if( tmMainFrame != null ) tmMainFrame.setSelectionString( selectionString ) ; }
    
    public String getSelectionString(  ) {
        if( tmMainFrame != null ) return tmMainFrame.getSelectionString( ) ;
        else                      return DEFAULT_SELECTION ;}

    public Image getSnap(String plugIn, String id) {
        if( tmMainFrame != null ) return tmMainFrame.getSnap( plugIn, id ) ;
        else return null ; }

    public boolean isRunDone() {
        if( tmMainFrame != null ) return tmMainFrame.isRunDone() ;
        else return false ; }

    public int getLastSnapWidth() {
        if( tmMainFrame != null ) return tmMainFrame.getLastSnapWidth() ;
        else return 0 ; }

    public int getLastSnapHeight() {
        if( tmMainFrame != null ) return tmMainFrame.getLastSnapHeight() ;
        else return 0 ; }

	public boolean getComparison(String plugIn, int n) {
        if( tmMainFrame != null ) return tmMainFrame.getComparison(plugIn, n) ;
        else return false ; 
	}
	
	public long getLocalInt(String datumName){
        if( tmMainFrame != null ) return tmMainFrame.getLocalInt(datumName) ;
        else return 0 ; 
	}


	
// Private methods //
/////////////////////

    void makeTheFrame() {
        //Pre tmMainFrame == null
        //Post: a TMMainFrame has been created and tmMainFrame' is it.
        ActionListener exitListener = new KillTheFrame() ;
        tmMainFrame = new TMMainFrame( this, exitListener, new ArgPackage(), true ) ;
    }

// Inner Classes
    class KillTheFrame implements ActionListener {
        public void actionPerformed( ActionEvent e ) {
            try {
                ConcurUtilities.doOnSwingThread( new Runnable() {
                    @Override public void run() {
                        if( tmMainFrame != null ) {
                            tmMainFrame.dispose() ;
                            tmMainFrame = null ; } }
                } ) ;
            } catch (InvocationTargetException e1) {
                e1.printStackTrace();
            }
        }
    }
}