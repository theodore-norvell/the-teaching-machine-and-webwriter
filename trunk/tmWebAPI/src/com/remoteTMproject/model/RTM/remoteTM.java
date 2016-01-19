package com.remoteTMproject.model.RTM;

import java.lang.reflect.InvocationTargetException;

import tm.ArgPackage;
import tm.AttentionFrame;
import tm.SwingInputter;
import tm.cpp.CPlusPlusLangPIFactory;
import tm.evaluator.Evaluator;
import tm.evaluator.Evaluator.Refreshable;
import tm.interfaces.CommandInterface;
import tm.interfaces.StatusConsumer;
import tm.interfaces.TMStatusCode;
import tm.languageInterface.Language;
import tm.utilities.Assert;
import tm.utilities.ConcurUtilities;
import tm.utilities.ResultThunk;
import tm.utilities.TMFile;
import tm.virtualMachine.SelectionParser;

public class remoteTM implements StatusConsumer{
	//private tm.interfaces.DisplayManagerInterface dispMan ;
	private  Evaluator evaluator ;


	// The following var is used by reConfigure
	static final int boStatic = 0 ;
	static final int toStatic = 16384-1 ;
	static final int boHeap = toStatic+1 ;
	static final int toHeap = boHeap+16384-1 ;
	static final int boStack = toHeap+1 ;
	static final int toStack = boStack+16384-1 ;
	static final int boScratch = toStack+1 ;
	static final int toScratch = boScratch+16384-1 ;

	private Refreshable refreshMole = new Refreshable() {
		public void refresh() { remoteTM.this.refresh() ; }

	} ;
	// CONSTRUCTORS //
	//////////////////begin
	public remoteTM(){
		this(new ArgPackage() ) ;

	}

	public remoteTM(ArgPackage argPackage){
	}
	//  Implementing StatusConsumer  //
	///////////////////////////////////


	public void setStatus(int statusCode, String message) {
		if( statusCode == TMStatusCode.NO_EVALUATOR ) {
		} else {
			evaluator.setStatusCode( statusCode ) ; 
			evaluator.setStatusMessage(message) ; 
		} }

	public void attention(String message, Throwable th ) {

	}

	public void attention(String message ) {
	}

	public int getStatusCode() {
		if( evaluator==null ) return TMStatusCode.NO_EVALUATOR ;
		else return evaluator.getStatusCode() ;
	}

	public String getStatusMessage() {
		if( evaluator==null) return null ;
		else return evaluator.getStatusMessage() ; }

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




	/** Should be called at the start of any example
	 * @param language The name of the language or UNKNOWN_LANG. */
	private boolean startNewProject( int language) {
		//set the programText, filename and create an instance of 
		//the TMfile which receive a source file and its name as parameters
		String fileName="test1";

		//LanguagePIFactoryIntf languageFactory = null ;

		// Create the language object
		Language lang = CPlusPlusLangPIFactory.createInstance(fileName).createPlugIn() ;


		// Restart the script manager
		//ScriptManager scriptManager = ScriptManager.getManager();
		//scriptManager.reset();


		//create the evaluator instance.
		try {
			evaluator=new Evaluator( lang, this, refreshMole,
					SelectionParser.parse(CommandInterface.DEFAULT_SELECTION),
					new SwingInputter(),
					boStatic, toStatic,
					boHeap, toHeap,
					boStack, toStack,
					boScratch, toScratch ) ; 
		}
		catch( Throwable e ) {
			setStatus( TMStatusCode.NO_EVALUATOR, "Could not build evaluator" ) ;
			reportException( e, "a failure while building the evaluator" ) ;
			return false ; }

		return true ;
	}   

	private void compile( TMFile tmFile) {
		// Precondition evaluator != null
		Assert.check( evaluator != null  ) ;
		java.awt.Frame dialog = new AttentionFrame( "Standby",
				"The Teaching Machine is loading the file.") ;
		dialog.setVisible( true ) ;

		evaluator.compile( tmFile );

	}


	public void loadTMFile( int language, TMFile tmFile ) {
		setStatus( TMStatusCode.NO_EVALUATOR, "Loading..." ) ;
		boolean ok = startNewProject( language ) ;
		if( ok ) {
			compile( tmFile ) ; }
	}


	void reportException( Throwable e, String message, String explanation ) {
		e.printStackTrace( System.out ) ;
		attention( message + explanation + ".", e ) ;
	}

	void reportException( Throwable e, String explanation ) {
		reportException( e, "The Teaching Machine could not execute your\n"
				+"request because of ", explanation) ;
	}
	private void refresh() {
		if( evaluator != null ) 
			System.out.println( "Status is " + evaluator.getStatusMessage() ) ;
	}	
	
	public Evaluator getEvaluator(){
		return evaluator;
	}


	public static final int UNKNOWN_LANG = 0;
	public static final int CPP_LANG = 1 ;
	public static final int JAVA_LANG = 2 ;

}
