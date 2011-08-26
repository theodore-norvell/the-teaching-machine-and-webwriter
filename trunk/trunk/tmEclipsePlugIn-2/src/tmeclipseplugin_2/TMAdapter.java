/*
 * Created on Mar 20, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package tmeclipseplugin_2;

import java.applet.AppletStub;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;

import tm.ArgPackage;
import tm.TMMainFrame;
import tm.TMMainFrameAppletStub;

/** Create the Teaching Machine Frame and control it.
 * @author Theo
 */
public class TMAdapter {

    private static TMMainFrame theMainFrame = null ;
    

    /** load a resource into the Teaching Machine
     * 
     * @param resource The resource to load.
     */
    public static void load(IResource resource) {
        if( theMainFrame == null ) { initTheMainFrame(); }
        if( theMainFrame == null ) return ;
        theMainFrame.showTM(true) ;
        
        String message = null ;
        IPath  location = resource.getLocation() ;
        if( location != null ) {
            try {
                File file1 = location.toFile() ;
                File file2 = file1.getAbsoluteFile() ;
                File directory = file2.getParentFile() ;
                String fName = file2.getName() ;
                
                theMainFrame.loadLocalFile( directory, fName ) ;
            }
            catch( Throwable e ) {
                e.printStackTrace( System.out ) ;
                message = "Exception on launch. Exception is "+e.getLocalizedMessage() ;
            } }
        else {
            message = "Could not obtain path from resource" ;  }
        
        if( message != null ) {
            Shell shell = new Shell() ;
            MessageDialog.openError( shell, "TmEclipsePlugIn Plug-in", message ) ; }
    }
    
    /** Initialize the theMainFrame
     * Precondition: theMainFrame == null
     * Postcondition: If the TMMainFrame can be built without
     * exception the theMainFrame references the new frame.
     * Else a message will have been displayed and theMainFrame == null
     */
    private static void initTheMainFrame() {
        try {
            ArgPackage argPackage = ArgPackage.processArgs( new String[0] ) ;
            if( argPackage == null ) {
                System.out.println("Could not start TM. Arguments can not be processed.") ;
                return ; }
            
            AppletStub appletStub = new TMMainFrameAppletStub(argPackage.installDirectory,
                                                              argPackage.installDirectory) ;
            ActionListener exitListener = new KillTheFrame() ;
            theMainFrame = new TMMainFrame(appletStub, exitListener, argPackage, true ) ;
        }
        catch (Throwable e) {
            e.printStackTrace( System.out ) ;
            Shell shell = new Shell() ;
            MessageDialog.openError(
                    shell,
                    "TmEclipsePlugIn Plug-in",
                    "Could not create the Teaching Machine Window. Exception is "+e.getLocalizedMessage() ) ;
        }
    }
    
    private static class KillTheFrame implements ActionListener {
        public void actionPerformed( ActionEvent e ) {
            if( theMainFrame != null ) {
                theMainFrame.dispose() ;
                theMainFrame = null ; } }
    }


}
