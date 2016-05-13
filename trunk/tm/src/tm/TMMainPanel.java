//Copyright 1998--2010 Michael Bruce-Lockhart and Theodore S. Norvell
//
//Licensed under the Apache License, Version 2.0 (the "License");
//you may not use this file except in compliance with the License. 
//You may obtain a copy of the License at 
//
//http://www.apache.org/licenses/LICENSE-2.0 
//
//Unless required by applicable law or agreed to in writing, 
//software distributed under the License is distributed on an 
//"AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
//either express or implied. See the License for the specific language 
//governing permissions and limitations under the License.

package tm;

import javax.swing.JPanel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Rectangle;
import java.io.File;
import java.io.InputStream;
import java.io.Reader;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Observable;
import java.util.Observer;
import java.util.Properties;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import tm.backtrack.BTTimeManager;
import tm.configuration.ConfigurationServer;
import tm.evaluator.Evaluator;
import tm.evaluator.Evaluator.Refreshable;
import tm.interfaces.CodeLine;
import tm.interfaces.CommandInterface;
import tm.interfaces.DisplayManagerInterface;
import tm.interfaces.DisplayManagerPIFactoryIntf;
import tm.interfaces.EditorPIFactoryInterface;
import tm.interfaces.EditorPIInterface;
import tm.interfaces.PlatformServicesInterface;
import tm.interfaces.RegionInterface;
import tm.interfaces.Scriptable;
import tm.interfaces.SelectionInterface;
import tm.interfaces.SourceCoords;
import tm.interfaces.StatusConsumer;
import tm.interfaces.TMStatusCode;
import tm.languageInterface.Language;
import tm.languageInterface.LanguagePIFactoryIntf;
import tm.plugins.PlugInManager;
import tm.plugins.PlugInNotFound;
import tm.scripting.ScriptManager;
import tm.utilities.Assert;
import tm.utilities.CachingFileSource;
import tm.utilities.ConcurUtilities;
import tm.utilities.Debug;
import tm.utilities.DiskFileSource;
import tm.utilities.FileSource;
import tm.utilities.LocalResourceFileSource;
import tm.utilities.ResultThunk;
import tm.utilities.StringFileSource;
import tm.utilities.TMFile;
import tm.utilities.URLFileSource;
import tm.virtualMachine.SelectionParser;



//TMMainPanel  //
///////////////////
/** This is the central component of the Teaching Machine. 
 * 
 * <p>It provides many services to other components.
 * <ul>
 *    <li> It implements {@link tm.interfaces.ExternalCommandInterface}
 *    <li> It owns a JComponent that can be put on a JFrame, on a JApplet, or used to generate images
 *         for an image-server based implementation (server-side applet).
 *    <li> Depending on its state it may depend an Evaluator and a DisplayManager.
 *    <li> It serves as a mediator between the Evaluator and the the DisplayManager.
 *         <ul>
 *            <li> It forwards refresh methods from the Evaluator to the DisplayManager.
 *            <li> If also serves as a StatusConsumer for the Evaluator. This allows the Evaluator to report errors and statuses.
 *            <li> It provides the ExternalCommandInterface to the DisplayManager and its displays.
 *                 Most of the ExternalCommandInterface is implemented by forwarding requests to the
 *                 Evaluator.
 *         </ul>
 * </ul> 
 * 
 * <p>States {@Link TMStatusCode}
 * <pre>
 * NO_EVALUATOR
 * READY_TO_COMPILE
 * DID_NOT_COMPILE
 * COMPILED
 * READY
 * BUSY_EVALUATIING
 * EXECUTION_COMPLETE
 * EXECUTION_FAILED
 * </pre>
 * 
 * State transitions:
 * <pre>
 * init --> NO_EVALUATOR
 * 
 * any state
 * |
 * |  any load operation /
 * |  determine language;
 * |  build language object;
 * |  build evaluator;
 * |  build display manager ;
 * |  compile 
 * |
 * | if any error before compilation
 * <>---------------------------------> NO_EVALUATOR
 * |
 * | if any errors during compilation
 * <>---------------------------------> DID_NOT_COMPILE
 * |
 * | if no errors
 * V
 * COMPILED
 * 
 *               any go-forward operation,goBack, redo
 * NO_EVALUATOR ------------------------------------------> NO_EVALUATOR
 * 
 *                               go_back [if undo is possible]
 * (any state but no evaluator) ----------------------------------->(the previous state)
 * 
 *                                  go_back [if undo is not possible]
 * (any state but no evaluator) ----------------------------------->(the same state)
 * 
 *                               redo [if redo is possible]
 * (any state but no evaluator) ----------------------------------->(the next state)
 * 
 *                                  redo  [if redo is not possible]
 * (any state but no evaluator) ----------------------------------->(the same state)
 * 
 *              any go-forward operation
 * EXECUTION_COMPLETE ------------------------------->EXECUTION_COMPLETE
 * 
 *              any go-forward operation
 * EXECUTION_FAILED ---------------------------------> EXECUTION_FAILED
 * 
 *          any go-forward operation / initialize
 * COMPILED ---------------------------------------> READY or
 *                                                  EXECUTION_COMPLETE or
 *                                                  EXECUTION_FAILED
 * 
 * 
 * READY
 * |
 * | any go-forward operation / corresponding evaluator opn starts
 * |
 * V
 * BUSY_EVALUATING
 * |
 * | evaluator operation completes
 * |
 * V
 * READY or EXECUTION_COMPLETE or EXECUTION_FAILED
 * 
 * </PRE>
 */
public class TMMainPanel
implements CommandInterface, StatusConsumer, Scriptable
{    
	public static final String COPYRIGHT = "(C) 1997--2016 Michael P. Bruce-Lockhart, and Theodore S. Norvell.";
	public static final String PROGRAMMERS = "Designed and coded by\n"
        + "Michael Bruce-Lockhart\n"
        + "Sun Hao\n"
        + "Theodore Norvell\n"
        + "Derek Rielly";
	private JLayeredPane layeredPane = new JLayeredPane() ;
	private TMMenuBar menuBar ;
	private DisplayManagerInterface dispMan ;
	private Evaluator evaluator ;
	private final CurrentFileManager currentFileManager = new CurrentFileManager() ;
	private boolean testMode = false ;
	private Properties properties ;
	private ArgPackage argPackage;

	private EditorPIInterface theEditor;

	// The following var is used by reConfigure
	private TMFile latestConfigurationFile = null ;

	private JLabel statusLine;

	private Refreshable refreshMole = new Refreshable() {
		public void refresh() { TMMainPanel.this.refresh() ; }
	} ;
	private PlatformServicesInterface platform;

	static final int boStatic = 0 ;
	static final int toStatic = 16384-1 ;
	static final int boHeap = toStatic+1 ;
	static final int toHeap = boHeap+16384-1 ;
	static final int boStack = toHeap+1 ;
	static final int toStack = boStack+16384-1 ;
	static final int boScratch = toStack+1 ;
	static final int toScratch = boScratch+16384-1 ;

	// CONSTRUCTORS //
	//////////////////

	public TMMainPanel(ArgPackage argPackage, PlatformServicesInterface platform ) {
		// This constructor should only be called from the Event Dispatch thread.
		Assert.check( SwingUtilities.isEventDispatchThread() );

		dispMan = null ;
		evaluator = null ;
		this.platform = platform ;
		layeredPane.setOpaque( true );
		layeredPane.setLayout( new BorderLayout() );
		layeredPane.setBackground(Color.WHITE) ;
		setUpStatusLine() ;

		this.argPackage = argPackage ;

		{ // Set up debug output
			String debugProperty ;
			try { debugProperty = System.getProperty("debug") ; }
			catch( SecurityException e ) { debugProperty = null ; }

			tm.utilities.Debug debug = tm.utilities.Debug.getInstance() ;
			if(  debugProperty == null  || debugProperty.equalsIgnoreCase( "no" ) ) {
				debug.deactivate(); }
			else if( debugProperty.equalsIgnoreCase( "yes" ) ) {
				// All 
				debug.turnOnAll() ; }
			else {
				// Split on any positive number of non-word-characters.
				String categories[] = debugProperty.split("\\W+") ;
				debug.turnOffAll() ; debug.turnOn( Debug.ALWAYS ) ;
				for( String category : categories ) {
					debug.turnOn( category ) ; } }
		}

		// Get properties from a file
		try {
			InputStream is = TMBigApplet.class.getResourceAsStream("tm.properties");
			properties = new Properties() ;
			properties.load(is) ; }
		catch( Throwable e) {
			properties = new Properties() ; }     

		// E.T. Phone Home
		Thread pingThread = new Thread( new Runnable() {
			public void run() {
				try {
					java.net.URL url = new URL("http://www.theteachingmachine.org/counters/BigAppletStarts.html") ;
					url.getContent() ; }
				catch( Throwable e ) {}                    
			}} ) ;
		pingThread.start() ;
	}
	
	public void addMenuBar( TMMenuBar menuBar ) {
		if( this.menuBar != null ) { layeredPane.remove( menuBar ) ; }
		this.menuBar = menuBar ;
		layeredPane.add( menuBar, BorderLayout.NORTH ) ;
		layeredPane.setLayer(menuBar, JLayeredPane.DEFAULT_LAYER );
		layeredPane.revalidate(); 
	}
	
	public JComponent getComponent() {
		return layeredPane ; }

    public void start() {
	        // Load the initial configuration file.
	        // If there is an initial configuaration file specified on the command line, use that,
	        // otherwise use the default.tmcfg from the .jar file
	    	System.out.println("The TMBigApplet has recieved a 'start' message") ;
	        try {
	            ConcurUtilities.doOnSwingThread( new Runnable() {
	                @Override public void run() {

	                    if( argPackage.initialConfiguration != null ) {
	                        FileSource source = new DiskFileSource( argPackage.initialConfiguration.getParentFile() ) ;
	                        TMMainPanel.this.latestConfigurationFile = new TMFile( source, argPackage.initialConfiguration.getName()) ; }
	                    else {
	                        FileSource source = new LocalResourceFileSource(this.getClass(), "", ".tmcfg") ;
	                        TMMainPanel.this.latestConfigurationFile = new TMFile( source, "default" ) ; }

	                    try {
	                        reConfigure() ; }
	                    catch( Throwable e ) {
	                        reportException( e, "The Teaching Machine could not read "
	                                + TMMainPanel.this.latestConfigurationFile
	                                + " because of ",
	                        "a problem opening the file" ) ;
	                        TMMainPanel.this.latestConfigurationFile = null ; }

	                }} ) ;
	        } catch (InvocationTargetException e1) {
	            e1.getTargetException().printStackTrace(); }
	}

	private void setUpStatusLine() {
		statusLine = new JLabel();
		statusLine.setText("Welcome to the TM.");
		statusLine.setPreferredSize(new Dimension(60,20));
		layeredPane.add(statusLine, BorderLayout.SOUTH );
		layeredPane.setLayer(statusLine, JLayeredPane.DEFAULT_LAYER );
		statusLine.setOpaque(true) ;
		statusLine.setBackground(Color.LIGHT_GRAY);
		layeredPane.revalidate(); 
	}

	void dispose() {
		if( dispMan != null ) {
			dispMan.dispose() ;
			dispMan = null ; }
	}

	public void showDialog( TMDialog dialog ) {
	    // TODO Try replacing the Layered Pane with a JDesktopPane
		layeredPane.add( BorderLayout.CENTER, dialog) ;
		layeredPane.setLayer(dialog, JLayeredPane.MODAL_LAYER);
		layeredPane.revalidate(); 
	}

    public void removeDialog( TMDialog dialog ) {
        layeredPane.remove( dialog ) ;
        layeredPane.revalidate(); 
    }

	//  Implementing StatusConsumer  //
	///////////////////////////////////

	private DataFiles dataFiles = new DataFiles() ;

	public void setStatus(int statusCode, String message) {
		if( statusCode == TMStatusCode.NO_EVALUATOR ) {
			removeTheDisplayManagerAndEvaluator() ;
			statusLine.setText(message) ;
		} else {
			evaluator.setStatusCode( statusCode ) ; 
			evaluator.setStatusMessage(message) ; 
			statusLine.setText( evaluator.getStatusMessage() ) ; } }

	public void attention(String message, Throwable th ) {
		if( ! testMode ) {
			platform.showMessage(this, "Attention", message, th) ; }

	}

	public void attention(String message ) {
		if( ! testMode ) {
			platform.showMessage(this, "Attention", message ) ; }
	}

	public int getStatusCode() {
		if( evaluator==null ) return TMStatusCode.NO_EVALUATOR ;
		else return evaluator.getStatusCode() ;
	}

	public String getStatusMessage() {
		if( evaluator==null) return statusLine.getText() ;
		else return evaluator.getStatusMessage() ; }

	//Implementing ExternalCommandInterface //
	///////////////////////////////////////////

	public void setTestMode( final boolean turnOn ) {
		try {
			ConcurUtilities.doOnSwingThread( new Runnable() {
				@Override public void run() {
					testMode = turnOn ;
				}} ) ; }
		catch (InvocationTargetException e1) {
			e1.getTargetException().printStackTrace(); }
	}

	public void loadString( final String fileName, final String programSource) {
		try {
			ConcurUtilities.doOnSwingThread( new Runnable() {
				@Override public void run() {
					StringFileSource fs = new StringFileSource() ;
					fs.addString( fileName, programSource ) ;
					loadTMFile( determineLanguage( fileName ), new TMFile( fs, fileName ) ) ; 
				}} ) ; }
		catch (InvocationTargetException e1) {
			e1.getTargetException().printStackTrace(); }}


	private void loadRemoteFile( final URL rootURL, final String fileName ) {
		try {
			ConcurUtilities.doOnSwingThread( new Runnable() {
				@Override public void run() {
					Debug d = Debug.getInstance() ;
					/*dbg */d.msg(Debug.CURRENT, "loadRemoteFile <" + fileName + "> from <" + rootURL + ">" ) ;/* */
					FileSource fs = new CachingFileSource( new URLFileSource( rootURL ) ) ;
					loadTMFile( determineLanguage( fileName ), new TMFile( fs, fileName ) ) ; 
				}} ) ; }
		catch (InvocationTargetException e1) {
			e1.getTargetException().printStackTrace(); }}

	public void loadRemoteFile( final String root, final String fileName ) {
		try {
			ConcurUtilities.doOnSwingThread( new Runnable() {
				@Override public void run() {
					/*dbg */Debug.getInstance().msg(Debug.CURRENT, "loadRemoteFile "+root+" "+fileName) ;/*dbg*/
					URL url = platform.makeURL( root ) ;
					if(  url==null ) setStatus( TMStatusCode.NO_EVALUATOR, "Malformed URL" ) ; 
					else loadRemoteFile( url, fileName ) ;
				}} ) ; }
		catch (InvocationTargetException e1) {
			e1.getTargetException().printStackTrace(); }

	}

	public void loadRemoteFile( String fileName ) {
		loadRemoteFile( platform.getDocumentBase() , fileName ) ; }

	public void loadLocalFile( final File directory, final String fileName ) {
		try {
			ConcurUtilities.doOnSwingThread( new Runnable() {
				@Override public void run() {
					Debug d = Debug.getInstance() ;
					d.msg(Debug.ALWAYS, "loadLocalFile from <" + directory + "><" + fileName + ">" ) ;
					FileSource fs = new DiskFileSource( directory ) ;
					loadTMFile( determineLanguage( fileName ), new TMFile( fs, fileName ) ) ;
				}} ) ; }
		catch (InvocationTargetException e1) {
			e1.getTargetException().printStackTrace(); } }

	private int determineLanguage( String fileName ) {
		fileName = fileName.toLowerCase() ;
		if(    fileName.endsWith(".jav")
				|| fileName.endsWith( ".java" ) )
			return JAVA_LANG ;
		else if(    fileName.endsWith( ".cpp" )
				|| fileName.endsWith( ".cxx")
				|| fileName.endsWith( ".c++" )
				|| fileName.endsWith( ".c" ))
			return CPP_LANG ;
		else
			return UNKNOWN_LANG ; }

	private void reConfigure()
			throws java.lang.Exception
	{
		if( latestConfigurationFile != null ) {
			//  showTM(true) ;
			Reader reader = latestConfigurationFile.toReader() ;
			Assert.apology( reader != null, "Can not open " + latestConfigurationFile);
			ConfigurationServer server = ConfigurationServer.getConfigurationServer();
			server.readConfiguration( reader ) ;
			//System.out.println("reconfigure with "+latestConfigurationFile.toString());server.dump();
			reader.close() ; }
	}

	void readLocalConfiguration( final File directory, final String fileName )
	{
		try {
			ConcurUtilities.doOnSwingThread( new Runnable() {
				@Override public void run() {
					try {
						TMMainPanel.this.latestConfigurationFile = new TMFile( new DiskFileSource( directory ), fileName ) ;
						reConfigure() ;}
					catch( SecurityException e) {
						reportException( e, "a security restriction" ) ; }
					catch( java.io.FileNotFoundException e ) {
						reportException( e, "a problem opening the file" ) ; }
					catch( Throwable e ) {
						reportException( e, "an exception" ) ; }
				}} ) ; }
		catch (InvocationTargetException e1) {
			e1.getTargetException().printStackTrace(); }
	}

	public void readRemoteConfiguration( final String fileName )
	{
		try {
			ConcurUtilities.doOnSwingThread( new Runnable() {
				@Override public void run() {
					try {
						TMMainPanel.this.latestConfigurationFile 
						= new TMFile( new URLFileSource( platform.getDocumentBase() ), fileName ) ;
						reConfigure() ;}
					catch( SecurityException e) {
						reportException( e, "a security restriction" ) ; }
					catch( Throwable e ) {
						reportException( e, "an exception" ) ; }
				}} ) ; }
		catch (InvocationTargetException e1) {
			e1.getTargetException().printStackTrace(); }
	}

	public void clearRemoteDataFiles() {
		try {
			ConcurUtilities.doOnSwingThread( new Runnable() {
				@Override public void run() {
					dataFiles.clearRemoteDataFiles() ;
				}} ) ; }
		catch (InvocationTargetException e1) {
			e1.getTargetException().printStackTrace(); }
	}

	public void registerRemoteDataFile(final String fileName) {
		try {
			ConcurUtilities.doOnSwingThread( new Runnable() {
				@Override public void run() {
					dataFiles.registerRemoteDataFile( fileName ) ;
				}} ) ; }
		catch (InvocationTargetException e1) {
			e1.getTargetException().printStackTrace(); }
	}

	public void addInputString( final String input) {
		try {
			ConcurUtilities.doOnSwingThread( new Runnable() {
				@Override public void run() {
					if( evaluator != null ) {
						evaluator.addInputString( input ) ; }
					refresh() ;
				}} ) ; }
		catch (InvocationTargetException e1) {
			e1.getTargetException().printStackTrace(); }}

	public void addProgramArgument(final String argument) {
		try {
			ConcurUtilities.doOnSwingThread( new Runnable() {
				@Override public void run() {
					if( evaluator != null ) 
						evaluator.addProgramArgument( argument ) ; 
				}} ) ; }
		catch (InvocationTargetException e1) {
			e1.getTargetException().printStackTrace(); } }

	/** Get output */
	public String getOutputString( ) {
		try {
			return ConcurUtilities.doOnSwingThread( new ResultThunk<String>() {
				@Override public String run() {
					if( evaluator != null ) {
						StringBuffer buf = new StringBuffer() ;
						for( int i = 0, sz = evaluator.getNumOutputLines() ; i < sz ; i++ ) {
							buf.append( evaluator.getOutputLine( i ) ) ;
							if( i != sz-1 ) buf.append( "\n" ) ; }
						return buf.toString() ; }
					else {
						return "" ; }
				}} ) ; }
		catch (InvocationTargetException e1) {
			e1.getTargetException().printStackTrace();
			return "" ; }
	}

	public void reStart() {
		try {
			ConcurUtilities.doOnSwingThread( new Runnable() {
				@Override public void run() {
					if( currentFileManager.hasCurrentFile()  ) {
						loadTMFile( currentFileManager.getLanguage(), currentFileManager.getCurrentFile() ) ; }
				}} ) ; }
		catch (InvocationTargetException e1) {
			e1.getTargetException().printStackTrace(); } }

	public void editCurrentFile() {
		try {
			ConcurUtilities.doOnSwingThread( new Runnable() {
				@Override public void run() {
					if( currentFileManager.hasCurrentFile() ) {
						if( theEditor == null ) {
							// Create the editor
							PlugInManager pim = PlugInManager.getSingleton() ;
							try {
								EditorPIFactoryInterface factory
								= pim.getFactory(TMBigAppletPIFactory.jackNameEditor,
										EditorPIFactoryInterface.class,
										true ) ;
								theEditor = factory.createEditor() ;
							} catch (PlugInNotFound e) {
								reportException( e, "Editor plug-in could not be built.") ;
								return ; }
						}
						// theEditor != null
						theEditor.startEditingFile( currentFileManager.getCurrentFile() ) ;
					} else {
						attention("There is no current file.") ;
					}
				}} ) ; }
		catch (InvocationTargetException e1) {
			e1.getTargetException().printStackTrace(); }
	}

	public Image getSnap(final String plugIn, final String id) {
		try {
			return ConcurUtilities.doOnSwingThread( new ResultThunk<Image>() {
				@Override public Image run() {
					Assert.error(isRunDone(), "Snaps should not be requested until TM has run to completion");
					return ScriptManager.getManager().getSnap(plugIn, id);
				}} ) ; }
		catch (InvocationTargetException e1) {
			e1.getTargetException().printStackTrace();
			return null ;}
	}

	public boolean isRunDone(){
		try {
			return ConcurUtilities.doOnSwingThread( new ResultThunk<Boolean>() {
				@Override public Boolean run() {
					int statusCode = getStatusCode() ;
					return  statusCode == TMStatusCode.EXECUTION_COMPLETE || statusCode == TMStatusCode.EXECUTION_FAILED ;
				}} ) ; }
		catch (InvocationTargetException e1) {
			e1.getTargetException().printStackTrace();
			return false ;}
	}

	public int getLastSnapWidth() {
		try {
			return ConcurUtilities.doOnSwingThread( new ResultThunk<Integer>() {
				@Override public Integer run() {
					return ScriptManager.getManager().getLastSnapWidth();   
				}} ) ; }
		catch (InvocationTargetException e1) {
			e1.getTargetException().printStackTrace();
			return 1 ;}	
	}

	public int getLastSnapHeight() {
		try {
			return ConcurUtilities.doOnSwingThread( new ResultThunk<Integer>() {
				@Override public Integer run() {
					return ScriptManager.getManager().getLastSnapHeight();  
				}} ) ; }
		catch (InvocationTargetException e1) {
			e1.getTargetException().printStackTrace();
			return 1 ;}	
	}

	public boolean getComparison(final String plugIn, final int n) {
		try {
			return ConcurUtilities.doOnSwingThread( new ResultThunk<Boolean>() {
				@Override public Boolean run() {
					return ScriptManager.getManager().getComparison(plugIn, n);
				}} ) ; }
		catch (InvocationTargetException e1) {
			e1.getTargetException().printStackTrace();
			return false ; }
	}

	public long getLocalInt(final String datumName){
		try {
			return ConcurUtilities.doOnSwingThread( new ResultThunk<Long>() {
				@Override public Long run() {
					return ScriptManager.getManager().getLocalInt(datumName);   
				}} ) ; }
		catch (InvocationTargetException e1) {
			e1.getTargetException().printStackTrace();
			return 0 ;}	
	}


	public void quit( ) { /*Handled in TMMainFrame */ }

	public void initializeTheState() {
		try {
			ConcurUtilities.doOnSwingThread( new Runnable() {
				@Override public void run() {
					if( getStatusCode() ==  TMStatusCode.COMPILED )
						evaluator.initialize() ;
				}} ) ; }
		catch (InvocationTargetException e1) {
			e1.getTargetException().printStackTrace(); }
	}

	public void goBack() {
		try {
			ConcurUtilities.doOnSwingThread( new Runnable() {
				@Override public void run() {
					if( evaluator != null ) {
						evaluator.goBack() ; }
				}} ) ; }
		catch (InvocationTargetException e1) {
			e1.getTargetException().printStackTrace(); } }

	public void redo() {
		try {
			ConcurUtilities.doOnSwingThread( new Runnable() {
				@Override public void run() {
					if( evaluator != null ) {
						evaluator.redo() ; }
				}} ) ; }
		catch (InvocationTargetException e1) {
			e1.getTargetException().printStackTrace(); } }

	private void go( final Command command ) {
		try {
			ConcurUtilities.doOnSwingThread( new Runnable() {
				@Override public void run() {
					if( evaluator != null ) {
						int statusCode = getStatusCode() ;
						if( statusCode == TMStatusCode.COMPILED ) {
							evaluator.initialize() ; }
						else if( statusCode == TMStatusCode.READY ){
							command.doIt() ; } }
				}} ) ; }
		catch (InvocationTargetException e1) {
			e1.getTargetException().printStackTrace(); }
	}

	public void go( final String commandString ) {
		go( new Command() {
			public void doIt() { evaluator.go(commandString); } ; } ) ; }

	public void goForward(){
		go( new Command() {
			public void doIt() { evaluator.goForward(); } ; } ) ; }

	public void microStep() {
		go( new Command() {
			public void doIt() { evaluator.microStep(); } ; } ) ; }

	public void overAll() {
		go( new Command() {
			public void doIt() { evaluator.overAll(); } ; } ) ; }

	public void intoExp() {
		go( new Command() {
			public void doIt() { evaluator.intoExp(); } ; } ) ; }

	public void intoSub() {
		go( new Command() {
			public void doIt() { evaluator.intoSub(); } ; } ) ; }

	public void toBreakPoint() {
		go( new Command() {
			public void doIt() { evaluator.toBreakPoint(); } ; } ) ; }

	public void toCursor( final String fileName, final int cursor ) {
		go( new Command() {
			public void doIt() { evaluator.toCursor( fileName, cursor ); } ; } ) ;
	}

	public void autoStep() {
		go( new Command() {
			public void doIt() { evaluator.autoStep( ) ; } } ) ;
	}

	public void autoStep(String fileName, int lineNo) {
		try {
			ConcurUtilities.doOnSwingThread( new Runnable() {
				@Override public void run() {
					// TODO
				}} ) ; }
		catch (InvocationTargetException e1) {
			e1.getTargetException().printStackTrace(); }
	}

	public void setAutoStepRate( final int rate ) {
		try {
			ConcurUtilities.doOnSwingThread( new Runnable() {
				@Override public void run() {
					if( evaluator != null ) evaluator.setAutoStepRate( rate ) ;
				}} ) ; }
		catch (InvocationTargetException e1) {
			e1.getTargetException().printStackTrace(); }
	}

	public boolean isInAuto() {
		return evaluator != null && evaluator.isInAuto() ;
	}

	public int getAutoStepRate() {
		if( evaluator != null ) return evaluator.getAutoStepRate() ;
		else return 0 ;
	}

	public void autoRun() {
		try {
			ConcurUtilities.doOnSwingThread( new Runnable() {
				@Override public void run() {
					go( new Command() {
						public void doIt() { evaluator.run( ) ; } } ) ;
				}} ) ; }
		catch (InvocationTargetException e1) {
			e1.getTargetException().printStackTrace(); }
	}

	public void stopAuto() {
		try {
			ConcurUtilities.doOnSwingThread( new Runnable() {
				@Override public void run() {
					if( evaluator != null ) evaluator.stop() ;
				}} ) ; }
		catch (InvocationTargetException e1) {
			e1.getTargetException().printStackTrace(); }
	}

	public void showTM(final boolean visible) {
		try {
			ConcurUtilities.doOnSwingThread( new Runnable() {
				@Override public void run() {
					platform.showTheTM( TMMainPanel.this, visible ) ;
				}} ) ; }
		catch (InvocationTargetException e1) {
			e1.getTargetException().printStackTrace(); }
	}

	public boolean isTMShowing() {
		try {
			return ConcurUtilities.doOnSwingThread( new ResultThunk<Boolean>() {
				@Override public Boolean run() {
					return platform.isTMShowing(TMMainPanel.this) ;
				}} ) ; }
		catch (InvocationTargetException e1) {
			e1.getTargetException().printStackTrace();
			return false ; }
	}

	private void refresh() {
		if( dispMan != null )
			dispMan.refresh() ; 
		if( evaluator != null ) 
			statusLine.setText( evaluator.getStatusMessage() ) ; }

	public void setSelection(SelectionInterface selection) {
		if( evaluator != null ) {
			evaluator.setSelection( selection );
			refresh() ; }
	}

	public SelectionInterface getSelection() {
		if( evaluator != null ) {
			return evaluator.getSelection(  ); }
		else return null ;
	}

	public void setSelectionString(final String string)  {
		try {
			ConcurUtilities.doOnSwingThread( new Runnable() {
				@Override public void run() {
					String str = string ;
					if( str.equals("all") ) str = CommandInterface.COMPLETE_SELECTION ;
					else if( str.equals("default") ) str = CommandInterface.DEFAULT_SELECTION ;
					// May throw apology.
					SelectionInterface sel = SelectionParser.parse( str ) ;
					setSelection( sel ) ;
				}} ) ; }
		catch (InvocationTargetException e1) {
			e1.getTargetException().printStackTrace(); }
	}

	public String getSelectionString() {
		try {
			return ConcurUtilities.doOnSwingThread( new ResultThunk<String>() {
				@Override public String run() {
					if( evaluator != null ) return evaluator.getSelection().toString() ;
					else                    return CommandInterface.DEFAULT_SELECTION ;
				}} ) ; }
		catch (InvocationTargetException e1) {
			e1.getTargetException().printStackTrace();
			return null ;}
	}

	@Override
	public BTTimeManager getTimeManager() {
		Assert.check( evaluator != null );
		return evaluator.getTimeManager() ;
	}
	void help() {
		URL helpURL ;
		try {
			helpURL = new URL(platform.getCodeBase(), "help/help.html" ) ; }
		catch( MalformedURLException ex ) {
			return ;
		}
		platform.showDocument(this, helpURL, "HELP" ) ;
	}

	//IMPLEMENTING THE EVALUATOR INTERFACE //
	//////////////////////////////////////////

	public int getNumSTEntries() {
		return evaluator.getNumSTEntries() ; }

	public tm.interfaces.STEntry getSymTabEntry(int index) {
		return evaluator.getSymTabEntry( index ) ; }

	public int varsInCurrentFrame() {
		return evaluator.varsInCurrentFrame() ; }

	public int varsInGlobalFrame() {
		return evaluator.varsInGlobalFrame() ; }

	public SourceCoords getCodeFocus() {
		return evaluator.getCodeFocus() ; }

	public Enumeration<TMFile> getSourceFiles() {
		return evaluator.getSourceFiles() ; }

	public int getNumSelectedCodeLines(TMFile tmFile, boolean allowGaps) {
		return evaluator.getNumSelectedCodeLines( tmFile, allowGaps ) ;
	}

	public CodeLine getSelectedCodeLine(TMFile tmFile, boolean allowGaps, int index) {
		return evaluator.getSelectedCodeLine( tmFile, allowGaps, index ) ; }

	public int getNumConsoleLines() {
		return evaluator.getNumConsoleLines() ; }

	public String getConsoleLine(int l) {
		return evaluator.getConsoleLine( l ) ; }

	public String getPCLocation() {
		return evaluator.getPCLocation() ; }

	public String getExpression() {
		return evaluator.getExpression() ; }

	public RegionInterface getStaticRegion() {
		return evaluator.getStaticRegion() ; }

	public RegionInterface getStackRegion() {
		return evaluator.getStackRegion() ; }

	public RegionInterface getHeapRegion() {
		return evaluator.getHeapRegion() ; }

	public RegionInterface getScratchRegion() {
		// The scratch region is not snooped on!
		return evaluator.getScratchRegion() ; }

	public int getLanguage() {
		return evaluator.getLanguage() ;
	}

	//Package methods //
	/////////////////////

	String getVersionString() {
		return "Built on " + properties.getProperty("build.date", "unknown date") ;
	}

	//PRIVATE AND PACKAGE METHODS //
	/////////////////////////////////

	void reportException( Throwable e, String message, String explanation ) {
		e.printStackTrace( System.out ) ;
		attention( message + explanation + ".", e ) ;
	}

	void reportException( Throwable e, String explanation ) {
		reportException( e, "The Teaching Machine could not execute your\n"
				+"request because of ", explanation) ;
	}

	/** Should be called at the start of any example
	 * @param language The name of the language or UNKNOWN_LANG. */
	private boolean startNewProject( int language) {

		try {
			// Get the appropriate factory object from the plug-in manager
			PlugInManager pim = PlugInManager.getSingleton() ;
			LanguagePIFactoryIntf languageFactory = null ;
			String languageName = null ;
			try {
				if( language == CPP_LANG ) {
					languageName = "C++" ;
					languageFactory = pim.getFactory( TMBigAppletPIFactory.jackNameForCPP, LanguagePIFactoryIntf.class, true ); }
				else if( language == JAVA_LANG ) {
					languageName = "Java" ;
					languageFactory = pim.getFactory( TMBigAppletPIFactory.jackNameForJava, LanguagePIFactoryIntf.class, true );  }
				else {
					Assert.error( "Unknown language." ) ; } }
			catch( PlugInNotFound e ) {
				String errMess = "Sorry "+languageName+" is not supported in this release." ;
				setStatus( TMStatusCode.NO_EVALUATOR, errMess ) ;
				attention( errMess, e ) ;
				System.out.println( errMess ) ;
				System.out.println( e.getMessage() ) ;
				return false ; }

			// Create the language object
			Language lang = languageFactory.createPlugIn() ;

			currentFileManager.clearCurrentFile() ;

			// Restart the script manager
			ScriptManager scriptManager = ScriptManager.getManager();
			scriptManager.reset();
			scriptManager.register(this);


			// Remove the display manager if any.
			removeTheDisplayManagerAndEvaluator() ;

			// Create the Evaluator.
			// There must be an Evaluator if there is an DisplayManager.
			// I.e. it is a class invariant that dispMan != null ==> evaluator != null.
			try {
				evaluator = new Evaluator( lang, this, refreshMole,
						SelectionParser.parse(CommandInterface.DEFAULT_SELECTION),
						platform.getInputter( this ),
						boStatic, toStatic,
						boHeap, toHeap,
						boStack, toStack,
						boScratch, toScratch ) ; }
			catch( Throwable e ) {
				setStatus( TMStatusCode.NO_EVALUATOR, "Could not build evaluator" ) ;
				reportException( e, "a failure while building the evaluator" ) ;
				return false ; }


			DisplayManagerPIFactoryIntf displayManagerPIFactory ;
			try {
				displayManagerPIFactory = pim.getFactory(TMBigAppletPIFactory.jackNameDisplayManager,
						DisplayManagerPIFactoryIntf.class,
						true ) ; 
				dispMan = displayManagerPIFactory.createPlugin( languageName,
						platform,
						this,
						menuBar.getViewMenu() ) ;        
				layeredPane.add( dispMan.getComponent(), BorderLayout.CENTER ) ;
				layeredPane.setLayer( dispMan.getComponent(), JLayeredPane.DEFAULT_LAYER );
				setStatus( TMStatusCode.READY_TO_COMPILE,
						lang.getName()+" Display and evaluator built. Ready to compile") ;}
			catch( PlugInNotFound ex ) {
				setStatus( TMStatusCode.NO_EVALUATOR, "Could not build display manager" ) ;
				reportException( ex, "a failure while building the display manager" ) ;
				return false ;
			}
			dispMan.createAllDisplays() ;
			layeredPane.revalidate();
			refresh() ;
			// This reConfigure is a bit annoying as it creates all the displays again.
			reConfigure() ;
			return true ; }
		catch( Throwable e ) {
			setStatus( TMStatusCode.NO_EVALUATOR,
					"Could not start project. Error: "+e.getMessage() ) ;
			reportException( e, "an exception" ) ;
			return false ; }
	}

	private void removeTheDisplayManagerAndEvaluator() {
		if( dispMan != null ) {
			dispMan.dispose();
			layeredPane.remove( dispMan.getComponent() ) ;
			layeredPane.revalidate(); 
			dispMan = null ; }
		evaluator = null ;
	}

	private void compile( TMFile tmFile) {
		// Precondition evaluator != null
		Assert.check( evaluator != null  ) ;
		setStatus(TMStatusCode.READY_TO_COMPILE, "Compiling" );
		evaluator.compile( tmFile );

		refresh() ; }

	/** Convenience method for projects that have only one file */
	private void loadTMFile( int language, TMFile tmFile ) {
		setStatus( TMStatusCode.NO_EVALUATOR, "Loading..." ) ;
		boolean ok = startNewProject( language ) ;
		if( ok ) {
			currentFileManager.setCurrentFile( tmFile, language ) ;
			compile( tmFile ) ; }
		refresh() ;
	}

	// Private Classes

	private interface Command {
		void doIt() ; }

	private class CurrentFileManager implements Observer {
		private TMFile currentFile = null ;
		private int currentLang = UNKNOWN_LANG ;

		boolean hasCurrentFile() { return currentFile != null ; }

		TMFile getCurrentFile() { return currentFile ; }

		int getLanguage() { return currentLang ; }

		void setCurrentFile( TMFile file, int language) {
			if( currentFile != null ) clearCurrentFile() ;
			file.addObserver( this ) ;
			currentFile = file ;
			currentLang = language ;
		}

		void clearCurrentFile() {
			if( currentFile != null ) {
				currentFile.deleteObserver( this ) ;
				currentFile = null ; }
		}

		public void update(Observable o, Object arg) {
			reStart() ;
		}
	}

	public String getId() {
		return "TMBigApplet";
	}

	public TMFile getDataFile() throws Throwable {
		return dataFiles.getDataFile(platform.getDocumentBase()) ;
	}

	static void setLookAndFeel(java.awt.Component component) {
		UIManager.LookAndFeelInfo[]lookAndFeelArray = UIManager.getInstalledLookAndFeels();
		for (int i = 0; i < lookAndFeelArray.length; i++) {
			if (lookAndFeelArray[i].getName().equals("Nimbus")) {
				try {
					UIManager.setLookAndFeel(lookAndFeelArray[i].getClassName()); }
				catch (Exception e) {
					System.err.println("Could not set the look and feel to "+lookAndFeelArray[i].getClassName()) ;
					e.printStackTrace(); }  
				if( component != null ) SwingUtilities.updateComponentTreeUI(component);
				break ; } }

	}
}
