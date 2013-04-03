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
import java.applet.AppletStub;
import javax.swing.* ;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import javax.swing.JFrame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FileDialog;
import java.awt.Font;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import tm.configuration.Configuration;
import tm.configuration.ConfigurationServer;
import tm.interfaces.Configurable;
import tm.interfaces.ExternalCommandInterface;
import tm.interfaces.ImageSourceInterface;
import tm.interfaces.SelectionInterface;
import tm.plugins.PlugInManager;
import tm.plugins.PlugInManagerDialog;
import tm.scripting.ScriptManager;
import tm.utilities.ApologyException;
import tm.virtualMachine.SelectionParser;


// TMMainFrame
// ------------
//   Contains the main program for the application.
//   Builds a frame containing a menu and a TMBigApplet.
/////////////////////////////////////////////////////////

public class TMMainFrame extends JFrame
                         implements ExternalCommandInterface,
                                    Configurable
{

    private static final String COPYRIGHT = "(C) 1997--2008 Michael P. Bruce-Lockhart, and Theodore S. Norvell.";
    private static final String PROGRAMMERS = "Designed and coded by\n"
                                            + "Michael Bruce-Lockhart\n"
                                            + "Sun Hao\n"
                                            + "Theodore Norvell\n"
                                            + "Derek Rielly";

    private JWindow splash ;
    private static final int SPLASH_NOT_SHOWING = 0; 
    private static final int SPLASH_SHOWING = 1; 
    private static final int SPLASH_SHOWN = 2; 
    private static int splashState = SPLASH_NOT_SHOWING ;

    private TMBigApplet tmBigApplet ;

    private String currentDirectory = null ;
    private String currentConfigFileName = null ;
    private String currentFileName = null ;
    private String currentRootDirectory = null ;
    private FileDialog fd = null ;
    private JMenuItem clearRootDirectory ;
    private java.awt.event.ActionListener exitListener ;


//===================================================================================
// Constructor
//===================================================================================
    public TMMainFrame( AppletStub appletStub,
            java.awt.event.ActionListener exitListener,
            ArgPackage argPackage,
            boolean visible ){

        super();
        this.exitListener = exitListener ;

        // Need a window listener.
            addWindowListener( new TMMainFrameWindowListener( ) ) ;

        // We'll use a simple border layout and add a menu at the top
        JMenuBar menuBar = new JMenuBar();

        setBackground(Color.BLACK) ;
        getContentPane().setBackground(Color.YELLOW) ;

        // The FILE menu
            JMenu fileMenu = new JMenu("File");
            menuBar.add(fileMenu);

            // "Load File"
                Action loadAction = new LoadFileAction() ;
                JMenuItem loadFile = new JMenuItem(loadAction);
                fileMenu.add( loadFile ) ;

            // Restart file
                Action restartAction = new RestartAction() ;
                JMenuItem restartFile = new JMenuItem( restartAction ) ;
                fileMenu.add( restartFile ) ;

            // Read Configuration
                Action readConfigurationAction = new ReadConfigurationAction() ;
                JMenuItem readConfiguration = new JMenuItem( readConfigurationAction ) ;
                fileMenu.add( readConfiguration ) ;

            // Write Configuration
                Action writeConfigurationAction = new WriteConfigurationAction() ;
                JMenuItem writeConfiguration = new JMenuItem(writeConfigurationAction ) ;
                fileMenu.add( writeConfiguration ) ;
            
//              Write Configuration
                Action editCurrentFileAction = new EditCurrentFileAction() ;
                JMenuItem editCurrentFile = new JMenuItem( editCurrentFileAction ) ;
                fileMenu.add( editCurrentFile ) ;
            
            // Plug-Ins
                Action managePlugInsAction = new ManagePlugInsAction() ;
                JMenuItem managePlugins = new JMenuItem( managePlugInsAction ) ;
                fileMenu.add( managePlugins ) ;

            // Exit
                // TODO Make exit use Actions.
                JMenuItem exit = new JMenuItem("Exit");
                exit.addActionListener( exitListener ) ;
                fileMenu.add(exit);

            //MenuItem tempItem = new MenuItem( "Test Remote" ) ;
            //tempItem.addActionListener( new ActionListener() {
            //	    public void actionPerformed( ActionEvent e ) {
            //            loadRemoteFile("C++", "cpp_test/ds_revcopy.cpp") ; } } ) ;
            //fileMenu.add( tempItem ) ;

                
        // The PROJECT menu
            JMenu projectMenu = new JMenu("Project");
            menuBar.add(projectMenu);
            // Set root directory
                Action selectRootDirectionAction = new SelectRootDirAction() ; ;
                JMenuItem selectRootDirectory = new JMenuItem( selectRootDirectionAction ) ;
                projectMenu.add( selectRootDirectory ) ;
            // Clear root directory
                Action clearRootDirectoryAction = new ClearRootDirAction() ;
                clearRootDirectory = new JMenuItem( clearRootDirectoryAction ) ;
                clearRootDirectoryAction.setEnabled( false ) ;
                projectMenu.add( clearRootDirectory ) ;
            
        // The VIEW menu
            JMenu menuView = new JMenu("View");
            menuBar.add(menuView);

            // Selections
            JMenuItem selectItem ;
            Action selectAction = new SelectAction( "Select default", ExternalCommandInterface.DEFAULT_SELECTION  ) ;
            selectItem = new JMenuItem( selectAction ) ;
            selectItem.addActionListener( selectAction ) ;
            menuView.add( selectItem ) ;

            selectAction = new SelectAction("Select all", ExternalCommandInterface.COMPLETE_SELECTION ) ;
            selectItem = new JMenuItem( selectAction ) ;
            menuView.add( selectItem ) ;
            
            selectAction = new CustomizeSelectionAction() ;
            selectItem = new JMenuItem( selectAction ) ;
            menuView.add( selectItem ) ;

            // TODO Need a pile of check boxes based on tags
//            JMenu subMenu ;
//            subMenu = new JMenu( label[i] ) ;
//            menuView.add( subMenu ) ;
//            String[] label = new String[] {"Select a-m", "Select n-z", "Select A-M", "Select N-Z"} ;
//            char[] start = new char[] { 'a', 'n', 'A', 'N' } ;
//            char[] end   = new char[] { 'm', 'z', 'M', 'Z' } ;
//            for( int i=0 ; i < 4 ; ++i ) {
//                subMenu = new JMenu( label[i] ) ;
//                menuView.add( subMenu ) ;
//                for( char ch = start[i] ; ch <= end[i] ; ++ch ) {
//                    selectAction = new SelectAction( "", selectionString ) ;
//                    selectItem = new JMenuItem( selectAction ) ;
//                    subMenu.add( selectItem ) ; } }

        // The GO menu
            JMenu menuGo = new JMenu( "Go" ) ;
            menuBar.add( menuGo ) ;

            
            JMenuItem goItem ;
            Action goAction = new goAction("Forward", GOFORWARD ) ;
            goItem = new JMenuItem( goAction ) ;
            menuGo.add( goItem ) ;

            goAction = new goAction("Undo", GOBACK ) ;
            goItem = new JMenuItem( goAction ) ;
            menuGo.add( goItem ) ;

            goAction = new goAction("Redo", REDO ) ;
            goItem = new JMenuItem( goAction ) ;
            menuGo.add( goItem ) ;

            goAction = new goAction("Micro Step", MICROSTEP ) ;
            goItem = new JMenuItem( goAction ) ;
            menuGo.add( goItem ) ;

            goAction = new goAction("Over", OVERALL ) ;
            goItem = new JMenuItem( goAction ) ;
            menuGo.add( goItem ) ;

            goAction = new goAction("Into Expression", INTOEXP ) ;
            goItem = new JMenuItem( goAction ) ;
            menuGo.add( goItem ) ;

            goAction = new goAction("Into Subroutine", INTOSUB ) ;
            goItem = new JMenuItem( goAction ) ;
            menuGo.add( goItem ) ;

            // !!! Can't do toCursor without access to the cursor !!!
            // But we could pop up a dialog to get a line number.

        // The HELP menu
            JMenu helpMenu = new JMenu("Help");
            menuBar.add(helpMenu);

            Action helpAction = new HelpAction() ;
            JMenuItem helpItem = new JMenuItem(helpAction);
            helpMenu.add( helpItem ) ;

            Action aboutAction = new AboutAction() ;
            JMenuItem aboutItem = new JMenuItem(aboutAction);
            helpMenu.add( aboutItem ) ;

       // End of menus

        /*Now, create an Applet that takes up the rest of the
        space & restricts all the display frames to operate inside it!!!!*/
        tmBigApplet = new TMBigApplet(menuView, argPackage );
        tmBigApplet.setStub( appletStub ) ;
        tmBigApplet.setJMenuBar( menuBar ) ;
        tmBigApplet.start() ;

        getContentPane().add( tmBigApplet, BorderLayout.CENTER );

        pack();
        setSize(800,600);
        setLocation(0,0);
        setTitle( "The Teaching Machine 2" ) ;
        setVisible( visible ) ;

        ConfigurationServer server = ConfigurationServer.getConfigurationServer();
        server.register(this,"mainFrame");
        
        splash = makeSplash( tmBigApplet ) ;
        flashSplash() ;
    }
    
    public void dispose() {
        tmBigApplet.disposeBigApplet() ;
        ConfigurationServer server = ConfigurationServer.getConfigurationServer();
        server.deregister(this) ;
        super.dispose();
    }

    void addApplicationOnlyMenuItems() {
        // For the moment at least all menu items appear in the applet.
    }
    
    private void clearRootDir() {
        currentRootDirectory = null ;
        clearRootDirectory.setEnabled( false ) ; }
    
    private void selectRootDir() {
        try {
            SwingBasedDirectoryChooser directoryChooser = new SwingBasedDirectoryChooser(this) ;
            directoryChooser.setTitle( "Select the root directory of the source tree" ) ;
            directoryChooser.setButtonText( "Select as root" ) ;
            if( currentRootDirectory != null )
                directoryChooser.setDirectory( currentRootDirectory ) ;
            else if( currentDirectory != null ) 
                directoryChooser.setDirectory( currentDirectory ) ;
            else
                directoryChooser.setDirectory( new File(".").getCanonicalPath() ) ;
            directoryChooser.show() ;
            String dirName = directoryChooser.getDirectory() ;
            if( dirName != null ) {
                currentRootDirectory = dirName ;
                currentDirectory = null ;
                clearRootDirectory.setEnabled( true ) ; } }
        catch( SecurityException e) {
            tmBigApplet.reportException( e, "a security restriction.\n" ) ; }
        catch( Throwable e ) {
            tmBigApplet.reportException( e, "an exception" ) ; } }

    private void prepareFileDialog( String title, int mode, String directory, String fileName ) {
        if( fd == null ) fd = new FileDialog( this ) ;
        fd.setTitle( title ) ;
        fd.setMode( mode ) ;
        if( directory != null ) fd.setDirectory( directory ) ;
        if( fileName != null ) fd.setFile( fileName ) ; }
        
    private void loadFile( ) {
        try {
            prepareFileDialog( "Select file to load",
                    FileDialog.LOAD,
                    currentDirectory != null ? currentDirectory : currentRootDirectory,
                    currentFileName ) ;
            fd.setVisible(true);
            String fileName = fd.getFile();
            String dirName = fd.getDirectory() ;
            if( fileName != null && dirName != null ) {
                currentDirectory = dirName ;
                currentFileName = fileName ;
                if( currentRootDirectory == null ) {
                    tmBigApplet.loadLocalFile( new File( dirName ), fileName ); }
                else {
                    File root = new File( currentRootDirectory ) ;
                    String fullRootName = root.getCanonicalPath() ;
                    int frnLen = fullRootName.length() ;
                    String fullFileName = new File( dirName, fileName ).getCanonicalPath() ;
                    if( fullFileName.startsWith( fullRootName )
                    && fullFileName.length() > frnLen
                    && fullFileName.charAt( frnLen ) == root.separatorChar ) {
                        fileName = fullFileName.substring( fullRootName.length() + 1 ) ;
                        tmBigApplet.loadLocalFile( root, fileName ) ; }
                    else {
                        tmBigApplet.attention( "File is not under the root directory" ) ; } } }
            else {
                /* Cancel button was pushed */ } }
        catch( SecurityException e) {
            tmBigApplet.reportException( e, "a security restriction.\n"
                      + "Most likely cause:"
                      + " You can not open a disk file in an Applet. Usually there\n"
                      + " is a way to load a remote file.  E.g. by\n"
                      + " clicking on a button or a hyper-link on a web page") ; }
        catch( Throwable e ) {
            tmBigApplet.reportException( e, "an exception" ) ;} }

    private void readConfigurationFile( )
    {
        try {
            prepareFileDialog( "Choose configuration file to load",
                    FileDialog.LOAD,
                    currentDirectory != null ? currentDirectory : currentRootDirectory,
                    currentConfigFileName ) ;
            fd.setVisible(true);
            String fileName = fd.getFile();
            String dirName = fd.getDirectory() ;
            if( fileName != null && dirName != null ) {
                currentDirectory = dirName ;
                currentConfigFileName = fileName ;
                tmBigApplet.readLocalConfiguration( new File( dirName ), fileName ) ; }
            else {
                /* Cancel button was pushed */ }  }
        catch( SecurityException e) {
            tmBigApplet.reportException( e, "a security restriction.\n"
                  + "Most likely cause:"
                  + " You can not open a disk file in an Applet.") ; }
        catch( Throwable e ) {
            tmBigApplet.reportException( e, "an exception" ) ;}
    }

    private void writeConfigurationFile( )
    {
            ConfigurationServer server = ConfigurationServer.getConfigurationServer();
            try {

                prepareFileDialog( "Save Configuration File",
                        FileDialog.SAVE,
                        currentDirectory != null ? currentDirectory : currentRootDirectory,
                        currentConfigFileName ) ;
                fd.setVisible(true);
                String fileName = fd.getFile();
                if( fileName != null ) {
                    String dirName = fd.getDirectory() ;
                    this.currentDirectory = dirName ;
                    this.currentConfigFileName = fileName ;
                    OutputStream outStream = new FileOutputStream( dirName+"/"+fileName ) ;
                    server.writeConfiguration( outStream ) ;
                    outStream.close() ; }
                else {
                    /* Cancel button was pushed */ } }
            catch( SecurityException e) {
                tmBigApplet.reportException( e, "a security restriction.\n"
                      + "Most likely cause:"
                      + " You can not open a disk file in an Applet.") ; }
            catch( java.io.FileNotFoundException e ) {
                tmBigApplet.reportException( e, "a problem opening or writing to the file" ) ; }
            catch( Throwable e ) {
                tmBigApplet.reportException( e, "an exception" ) ;}
    }

    // MAIN PROGRAM //
    //////////////////

    public static void main( String[] arg ) {
        
        final ArgPackage argPackage = ArgPackage.processArgs( arg ) ;
        if( argPackage == null ) {
            System.err.println( "Usage: java tm.TMBigApplet {arguments} [file]") ;
            System.err.println( "   Arguments:" ) ;
            System.err.println( "     -installDirectory directory | -id directory  The directory where the tm.jar file is." ) ;
            System.err.println( "     -initialConfiguration file | -ic file  The initial configuration file to use." ) ;
            return ; }
        
//        System.out.println("Initial config file: " + argPackage.initialConfiguration.toString());

        SwingUtilities.invokeLater( new Runnable() {
            public void run() {
            	TMBigApplet.setLookAndFeel( null ) ;
            	
                AppletStub appletStub = new TMMainFrameAppletStub(argPackage.installDirectory,
                        argPackage.installDirectory) ;
                ActionListener exitListener = new KillTheProcess() ;
                TMMainFrame tmMainFrame = new TMMainFrame( appletStub, exitListener, argPackage, true ) ;

                // Add extra capabilites for applications.
                tmMainFrame.addApplicationOnlyMenuItems() ;
                
                if( argPackage.fileToLoad != null ) {
                    String fileURL = argPackage.fileToLoad.toString(); ;
                    tmMainFrame.loadRemoteFile( fileURL );
                }
            } } ) ;
    }
    
    private JWindow makeSplash(ImageSourceInterface imageSource) {
        String[] messages = { "The Teaching Machine 2.", getVersionString(),
                COPYRIGHT };

        // Create a splash window.
        JWindow splash = new JWindow(this);
        JPanel panel = new JPanel();
        panel.setBackground(Color.BLUE);
        splash.add(panel, BorderLayout.CENTER);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        Image logoImage = imageSource.fetchImage("subWindowPkg/logo.gif");
        JLabel logoLabel = new JLabel(new ImageIcon(logoImage));
        panel.add(logoLabel);
        Font font = new Font("Dialog", Font.PLAIN, 20);
        for (String message : messages) {
            JLabel label = new JLabel(message);
            label.setForeground(Color.YELLOW);
            label.setFont(font);
            label.setAlignmentX(Component.CENTER_ALIGNMENT);
            panel.add(label);
        }
        panel.setBorder(BorderFactory.createRaisedBevelBorder());
        splash.pack();

        // Center the splash on the main frame.
        Rectangle frameBounds = getBounds();
        Rectangle splashBounds = splash.getBounds();
        splashBounds.x = frameBounds.x + frameBounds.width / 2
                - splashBounds.width / 2;
        splashBounds.y = frameBounds.y + frameBounds.height / 2
                - splashBounds.height / 2;
        splash.setBounds(splashBounds);

        return splash;
    }
    
    private void flashSplash() {
        if( splashState == SPLASH_NOT_SHOWING ) {
            splash.setVisible( true ) ;
            splashState = SPLASH_SHOWING ;
            
            (new Thread() {
                // @Override
                public void run() {
                    try {
                        Thread.sleep(4000) ;
                    } catch (InterruptedException e) { }
                    killSplash() ; }}
            ).start() ;  }
    }
    
    private void killSplash() {
        if( splashState == SPLASH_SHOWING ) {
            splash.setVisible( false ) ;
            splash.dispose() ;
            splash = null ;
            splashState = SPLASH_SHOWN ; }
    }
    
    void help() { tmBigApplet.help(this) ; }
    
    String getVersionString() { return tmBigApplet.getVersionString() ; }

// Implementing ExternalCommandInterface //
///////////////////////////////////////////
    
    public void setTestMode( boolean turnOn ) {
        tmBigApplet.setTestMode( turnOn ) ; }
    
    public int getStatusCode() {
        return tmBigApplet.getStatusCode() ; }
    
    public String getStatusMessage() {
        return tmBigApplet.getStatusMessage() ; }

    public void loadString( String fileName, String programSource) {
        tmBigApplet.loadString( fileName, programSource ) ; 
        killSplash() ;}

    public void loadRemoteFile( String fileName) {
        tmBigApplet.loadRemoteFile( fileName ) ; 
        killSplash() ; }
    
    public void loadRemoteFile( String root, String fileName) {
        tmBigApplet.loadRemoteFile( root, fileName ) ;
        killSplash() ; }

    public void loadLocalFile( File directory, String fileName) {
        tmBigApplet.loadLocalFile( directory, fileName ) ; 
        killSplash() ; }
    
    public void readRemoteConfiguration( String fileName )  {
        tmBigApplet.readRemoteConfiguration(fileName) ; }
    
    public void clearRemoteDataFiles() {
        tmBigApplet.clearRemoteDataFiles() ;
    }

    public void registerRemoteDataFile(String fileName) {
        tmBigApplet.registerRemoteDataFile( fileName ) ;
    }

    public void addInputString( String input) {
        tmBigApplet.addInputString( input ) ; }
    
    public void addProgramArgument(String argument) {
        tmBigApplet.addProgramArgument( argument ) ;
    }
    public String getOutputString() {
        return tmBigApplet.getOutputString() ; }
    
    public void reStart() {
        tmBigApplet.reStart() ; }

    public void editCurrentFile() {
        tmBigApplet.editCurrentFile() ; }
    
	public Image getSnap(String plugIn, String id) {
		return ScriptManager.getManager().getSnap(plugIn, id);
	}
	
	public boolean isRunDone(){
		return tmBigApplet.isRunDone();
	}

	public int getLastSnapWidth() {
		return ScriptManager.getManager().getLastSnapWidth();
	}
	
	public int getLastSnapHeight() {
		return ScriptManager.getManager().getLastSnapHeight();
	}
	
	public boolean getComparison(String plugIn, int n) {
		return ScriptManager.getManager().getComparison(plugIn, n);
	}
	
	public long getLocalInt(String datumName){
		return ScriptManager.getManager().getLocalInt(datumName);		
	}

   public void quit() {
        exitListener.actionPerformed( new ActionEvent(this, ActionEvent.ACTION_FIRST, "") ) ; }
    
    public void initializeTheState() {
        tmBigApplet.initializeTheState() ; }

    public void goBack() {
        tmBigApplet.goBack() ; }

    public void redo() {
        tmBigApplet.redo() ; }

    public void go( String commandString ) {
    	tmBigApplet.go( commandString ) ; }

    public void goForward(){
        tmBigApplet.goForward() ; }

    public void microStep(){
        tmBigApplet.microStep() ; }

    public void overAll(){
        tmBigApplet.overAll() ; }

    public void intoExp(){
        tmBigApplet.intoExp() ; }

    public void intoSub(){
        tmBigApplet.intoSub() ; }
    
    public void toBreakPoint() {
    	tmBigApplet.toBreakPoint() ; }

    public void toCursor( String fileName, int cursor ){
        tmBigApplet.toCursor( fileName, cursor ) ; }

    public void autoStep() {
        tmBigApplet.autoStep() ;
    }

    public void autoStep(String fileName, int lineNo) {
        tmBigApplet.autoStep( fileName, lineNo ) ;
    }

    public void setAutoStepRate(int rateConstant) {
        tmBigApplet.setAutoStepRate(rateConstant) ;
    }

    public void stopAuto() {
        tmBigApplet.stopAuto() ;
    }
    
    public void autoRun() {
        tmBigApplet.autoRun() ;
    }
    
    /** Show or hide the Teaching Machine's main window
     * @param visible When this is true, the TM's main window is comes to the front of the desktop.
     *                When this is false, the TM's main window is hidden
     */
    public void showTM(boolean visible){
        setVisible( visible ) ;
        if( visible ) {
            int state = getExtendedState() ;
            if( (state & JFrame.ICONIFIED) != 0 )
                setExtendedState( state & ~ JFrame.ICONIFIED ) ;
            toFront() ;
        }
    }
    
    public boolean isTMShowing() {
        return isVisible() ;
    }

    public void setSelectionString( String selectionString ) {
        // May throw Apology
        tmBigApplet.setSelectionString( selectionString ) ; }

    public String getSelectionString() {
        return tmBigApplet.getSelectionString() ; }

// LISTENERS //
///////////////

    class LoadFileAction extends AbstractAction {
        LoadFileAction() { super( "Load" ) ; }

        public void actionPerformed( ActionEvent e ) {
            loadFile(  ) ; } }

    class RestartAction extends AbstractAction {
        RestartAction() { super( "Restart" ) ; }

        public void actionPerformed( ActionEvent e ) {
            reStart( ) ; } }

    class ReadConfigurationAction extends AbstractAction {
        ReadConfigurationAction() { super( "Read Configuration File" ) ; }
        
        public void actionPerformed( ActionEvent e ) {
            readConfigurationFile( ) ; } }

    class EditCurrentFileAction extends AbstractAction {
        EditCurrentFileAction() { super( "Edit Current File" ) ; }
        
        public void actionPerformed( ActionEvent e ) {
            editCurrentFile( ) ; } }

    class WriteConfigurationAction extends AbstractAction {
        WriteConfigurationAction() { super( "Write Configuration File" ) ; }
        
        public void actionPerformed( ActionEvent e ) {
            writeConfigurationFile( ) ; } }
    
    class ManagePlugInsAction extends AbstractAction {
        ManagePlugInsAction() { super( "Manage Plug-Ins" ) ; }
        
        public void actionPerformed(ActionEvent e) {
            PlugInManager plugInManager = PlugInManager.getSingleton() ;
            PlugInManagerDialog dialog = new PlugInManagerDialog(plugInManager, tmBigApplet) ;
            dialog.setVisible(true) ;
        }
    }
    class SelectRootDirAction extends AbstractAction {
        SelectRootDirAction() { super( "Select Root Directory" ) ; }

        public void actionPerformed( ActionEvent e ) {
            selectRootDir( ) ; } }

    
    class ClearRootDirAction extends AbstractAction {
        ClearRootDirAction() { super( "Clear Root Directory" ) ; }

        public void actionPerformed( ActionEvent e ) {
            clearRootDir( ) ; } }

    public static final int GOFORWARD=0, GOBACK=1,
            OVERALL = 2, MICROSTEP = 3, INTOEXP = 4, INTOSUB = 5, REDO = 6 ;

    class goAction extends AbstractAction {

        int howMuch ;

        public goAction(String name, int _howMuch ) { super(name) ; howMuch = _howMuch ; }

        public void actionPerformed( ActionEvent e ) {
            switch( howMuch ) {
                case TMMainFrame.GOFORWARD : goForward() ; break ;
                case TMMainFrame.GOBACK : goBack() ; break ;
                case TMMainFrame.REDO : redo() ; break ;
                case TMMainFrame.OVERALL : overAll() ; break ;
                case TMMainFrame.MICROSTEP : microStep() ; break ;
                case TMMainFrame.INTOEXP : intoExp() ; break ;
                case TMMainFrame.INTOSUB : intoSub() ; break ; } }
    }

    class SelectAction extends AbstractAction {
        String selectionString ;

        public SelectAction( String name, String selectionString ) {
            super( name ) ;
            this.selectionString = selectionString ; }

        public void actionPerformed( ActionEvent e ) {
            setSelectionString( selectionString ) ; }
    }
    
    class CustomizeSelectionAction extends AbstractAction {
        public CustomizeSelectionAction() {
            super( "Customize Selection ...") ;
        }
        
        public void actionPerformed( ActionEvent e ) {
            String str = TMMainFrame.this.getSelectionString() ;
            SelectionInterface sel = null ;
            do {
                str = JOptionPane.showInputDialog(TMMainFrame.this,
                        "Enter the new selection.\n"+
                        "Use ! for 'not', & for 'and', and | for 'or'.\n"+
                        "'s' is for scripts, 'l' is for library.",
                        str ) ;
                
                if( str == null ) return ; // Cancel. No change.
                
                try { sel = SelectionParser.parse( str ) ; }
                catch(ApologyException apology) { }
                
                if( sel == null ) {
                    JOptionPane.showMessageDialog(TMMainFrame.this, "Could not parse." ) ; }
            } while( sel == null ) ;
            TMMainFrame.this.tmBigApplet.setSelection( sel ) ;
        }
    }

    class AboutAction extends AbstractAction {
        AboutAction() { super( "About" ) ; }
        
        public void actionPerformed( ActionEvent e ) {
            String message = "The Teaching Machine.\n\n"
                + getVersionString() + "\n\n"
                + COPYRIGHT + "\n\n"
                + PROGRAMMERS;
            JFrame d = new AttentionFrame( "Attention", message ) ;
            d.setVisible( true ) ; } }

    class HelpAction extends AbstractAction {
        HelpAction() { super( "Help" ) ; }
        
        public void actionPerformed( ActionEvent e ) {
            help() ; } }


    class TMMainFrameWindowListener extends WindowAdapter {

       public void windowClosing(WindowEvent e){
            exitListener.actionPerformed( new ActionEvent(this, ActionEvent.ACTION_FIRST, "") ) ; }
       //Invoked when a window is in the process of being closed.
    }

    // IMPLEMENTING THE CONFIGURABLE INTERFACE //
    /////////////////////////////////////////////
    
	public String getId(){return "mainFrame";}
	

    public void notifyOfSave(Configuration config)/*throws Exception*/{
        Rectangle r = getBounds();
        config.setValue("Position.x", Integer.toString(r.x));
        config.setValue("Position.y", Integer.toString(r.y));
        config.setValue("Width", Integer.toString(r.width));
        config.setValue("Height", Integer.toString(r.height));
    }

    public void notifyOfLoad(Configuration config)
    {
        Rectangle r = this.getBounds();
        String temp = config.getValue("Position.x");
        if (temp != null) r.x = new Integer(temp).intValue();
        temp = config.getValue("Position.y");
        if (temp != null) r.y = new Integer(temp).intValue();
        temp = config.getValue("Width");
        if (temp != null) r.width = new Integer(temp).intValue();
        temp = config.getValue("Height");
        if (temp != null) r.height = new Integer(temp).intValue();
        //System.out.println( "Setting Bounds to " +r.x+ ", " +r.y+ ", " +r.width+ ", " +r.height ) ;
        setBounds(r);
        invalidate() ;
        //System.out.println( "invalidate completed." ) ;
        validate();
        //System.out.println( "validate completed." ) ;
        
    }
}

// Priivate Classes

// KillTheProcess -- A private class
// -----------------

class KillTheProcess implements ActionListener {
    public void actionPerformed( ActionEvent e ) {
        System.exit(0) ; }
}