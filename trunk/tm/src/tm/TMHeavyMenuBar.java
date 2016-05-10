package tm;

import java.awt.FileDialog;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.JMenuItem;

import tm.configuration.ConfigurationServer;
import tm.interfaces.PlatformServicesInterface;
import tm.plugins.PlugInManager;
import tm.plugins.PlugInManagerDialog;

/** A heavy Menu bar includes actions that require Dialogs and frames etc that require frames and such.
 * 
 * @author Theodore Norvell
 *
 */
public class TMHeavyMenuBar extends TMMenuBar {


    private String currentDirectory = null ;
    private String currentConfigFileName = null ;
    private String currentFileName = null ;
    private String currentRootDirectory = null ;
    private FileDialog fd = null ;
    private JFrame frame ;
	private JMenuItem clearRootDirectory;

	public TMHeavyMenuBar(TMMainPanel tmMainPanel,
			PlatformServicesInterface platform, JFrame frame) {
		super(tmMainPanel, platform);
		this.frame = frame ;

		// "Load File"
		Action loadAction = new LoadFileAction() ;
		JMenuItem loadFile = new JMenuItem(loadAction);
		fileMenu.add( loadFile, 0 ) ;

		// Read Configuration
		Action readConfigurationAction = new ReadConfigurationAction() ;
		JMenuItem readConfiguration = new JMenuItem( readConfigurationAction ) ;
		fileMenu.add( readConfiguration, 2 ) ;

		// Write Configuration
		Action writeConfigurationAction = new WriteConfigurationAction() ;
		JMenuItem writeConfiguration = new JMenuItem(writeConfigurationAction ) ;
		fileMenu.add( writeConfiguration, 3 ) ;

		//  Edit current file
		Action editCurrentFileAction = new EditCurrentFileAction() ;
		JMenuItem editCurrentFile = new JMenuItem( editCurrentFileAction ) ;
		fileMenu.add( editCurrentFile, 4 ) ;

		// Plug-Ins
		Action managePlugInsAction = new ManagePlugInsAction() ;
		JMenuItem managePlugins = new JMenuItem( managePlugInsAction ) ;
		fileMenu.add( managePlugins, 5 ) ;
		
		// Project menu
		
		// Set root directory
		Action selectRootDirectionAction = new SelectRootDirAction() ; ;
		JMenuItem selectRootDirectory = new JMenuItem( selectRootDirectionAction ) ;
		projectMenu.add( selectRootDirectory ) ;
		// Clear root directory
		Action clearRootDirectoryAction = new ClearRootDirAction() ;
		clearRootDirectory = new JMenuItem( clearRootDirectoryAction ) ;
		clearRootDirectoryAction.setEnabled( false ) ;
		projectMenu.add( clearRootDirectory ) ;
		
		// View menu

		Action selectAction = new CustomizeSelectionAction() ;
		JMenuItem selectItem = new JMenuItem( selectAction ) ;
		menuView.add( selectItem ) ;
	}



    private void prepareFileDialog( String title, int mode, String directory, String fileName ) {
        if( fd == null ) fd = new FileDialog( frame ) ;
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
                	tmMainPanel.loadLocalFile( new File( dirName ), fileName ); }
                else {
                    File root = new File( currentRootDirectory ) ;
                    String fullRootName = root.getCanonicalPath() ;
                    int frnLen = fullRootName.length() ;
                    String fullFileName = new File( dirName, fileName ).getCanonicalPath() ;
                    if( fullFileName.startsWith( fullRootName )
                    && fullFileName.length() > frnLen
                    && fullFileName.charAt( frnLen ) == root.separatorChar ) {
                        fileName = fullFileName.substring( fullRootName.length() + 1 ) ;
                        tmMainPanel.loadLocalFile( root, fileName ) ; }
                    else {
                        tmMainPanel.attention( "File is not under the root directory" ) ; } } }
            else {
                /* Cancel button was pushed */ } }
        catch( SecurityException e) {
            tmMainPanel.reportException( e, "a security restriction.\n"
                      + "Most likely cause:"
                      + " You can not open a disk file in an Applet. Usually there\n"
                      + " is a way to load a remote file.  E.g. by\n"
                      + " clicking on a button or a hyper-link on a web page") ; }
        catch( Throwable e ) {
            tmMainPanel.reportException( e, "an exception" ) ;} }

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
                tmMainPanel.readLocalConfiguration( new File( dirName ), fileName ) ; }
            else {
                /* Cancel button was pushed */ }  }
        catch( SecurityException e) {
            tmMainPanel.reportException( e, "a security restriction.\n"
                  + "Most likely cause:"
                  + " You can not open a disk file in an Applet.") ; }
        catch( Throwable e ) {
            tmMainPanel.reportException( e, "an exception" ) ;}
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
                tmMainPanel.reportException( e, "a security restriction.\n"
                      + "Most likely cause:"
                      + " You can not open a disk file in an Applet.") ; }
            catch( java.io.FileNotFoundException e ) {
                tmMainPanel.reportException( e, "a problem opening or writing to the file" ) ; }
            catch( Throwable e ) {
                tmMainPanel.reportException( e, "an exception" ) ;}
    }



	class LoadFileAction extends AbstractAction {
		LoadFileAction() { super( "Load" ) ; }

		public void actionPerformed( ActionEvent e ) {
			loadFile(  ) ; } }
	
	class ReadConfigurationAction extends AbstractAction {
		ReadConfigurationAction() { super( "Read Configuration File" ) ; }

		public void actionPerformed( ActionEvent e ) {
			readConfigurationFile( ) ; } }

	class EditCurrentFileAction extends AbstractAction {
		EditCurrentFileAction() { super( "Edit Current File" ) ; }

		public void actionPerformed( ActionEvent e ) {
			tmMainPanel.editCurrentFile( ) ; } }

	class WriteConfigurationAction extends AbstractAction {
		WriteConfigurationAction() { super( "Write Configuration File" ) ; }

		public void actionPerformed( ActionEvent e ) {
			writeConfigurationFile( ) ; } }

	class ManagePlugInsAction extends AbstractAction {
		ManagePlugInsAction() { super( "Manage Plug-Ins" ) ; }

		public void actionPerformed(ActionEvent e) {
			PlugInManager plugInManager = PlugInManager.getSingleton() ;
			PlugInManagerDialog dialog = new PlugInManagerDialog(plugInManager, platform) ;
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


    
    private void clearRootDir() {
        currentRootDirectory = null ;
        clearRootDirectory.setEnabled( false ) ; }
    
    private void selectRootDir() {
        try {
            SwingBasedDirectoryChooser directoryChooser = new SwingBasedDirectoryChooser(frame) ;
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
            tmMainPanel.reportException( e, "a security restriction.\n" ) ; }
        catch( Throwable e ) {
            tmMainPanel.reportException( e, "an exception" ) ; } }

}
