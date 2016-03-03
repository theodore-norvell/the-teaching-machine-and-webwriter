package com.remoteTMproject.model.RTM;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import com.remoteTMproject.controller.GUIDMaker.GUIDMaker;
import com.remoteTMproject.model.json.JsonTransfer;
import com.remoteTMproject.model.json.Response;
import com.remoteTMproject.model.map.mapForRTM;

import tm.ArgPackage;
import tm.AttentionFrame;
import tm.SwingInputter;
import tm.cpp.CPlusPlusLangPIFactory;
import tm.evaluator.Evaluator;
import tm.evaluator.Evaluator.Refreshable;
import tm.interfaces.CodeLine;
import tm.interfaces.CommandInterface;
import tm.interfaces.SourceCoords;
import tm.interfaces.StatusConsumer;
import tm.interfaces.TMStatusCode;
import tm.javaLang.JavaLangPIFactory;
import tm.languageInterface.Language;
import tm.scripting.ScriptManager;
import tm.utilities.Assert;
import tm.utilities.ConcurUtilities;
import tm.utilities.Debug;
import tm.utilities.ResultThunk;
import tm.utilities.StringFileSource;
import tm.utilities.TMFile;
import tm.virtualMachine.SelectionParser;

public class remoteTM implements StatusConsumer{
	//private tm.interfaces.DisplayManagerInterface dispMan ;
	private String reason="";
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
	
	/**for the url calls
	 * @throws ParseException **/
  public synchronized JSONObject createRemoteTM(String guidNumberTest) throws JsonParseException, JsonMappingException, JsonGenerationException, IOException, ParseException{
	  	
	  remoteTM rtm=mapForRTM.getInstance(guidNumberTest);
	  
		if(rtm.getStatusCode()==0){
			//create the JsonTransfer object
	        JsonTransfer json1 = new JsonTransfer();
	        //create the java bean
	        Response para1=new Response(rtm.getStatusCode(),guidNumberTest,rtm.getReason());
	        //transfer the java object to json string and assign it to a String
	        JSONObject response1=json1.objecToJson(para1);

	        return response1;
		}
		
		if(rtm.getStatusCode()==7){
			 JsonTransfer json1 = new JsonTransfer();
		        //create the java bean
		        Response para1=new Response(rtm.getStatusCode(),guidNumberTest,rtm.getReason());
		        //transfer the java object to json string and assign it to a String
		        JSONObject response1=json1.objecToJson(para1);

		        return response1;
			
		}
		else {
		 JsonTransfer json1 = new JsonTransfer();
	        //create the java bean
	        Response para1=new Response(rtm.getStatusCode(),guidNumberTest,"unknown error");
	        //transfer the java object to json string and assign it to a String
	        JSONObject response1=json1.objecToJson(para1);
	        return response1;
		}
	  
	   }

  		public synchronized JSONObject retireRemoteTM(String guid) throws JsonParseException, JsonMappingException, JsonGenerationException, IOException, ParseException{
			
  			int status=mapForRTM.deleteInstance(guid);
  			
  			if (status==-4){
  				JsonTransfer json1 = new JsonTransfer();
		        //create the java bean
		        Response para1=new Response(-4,"");
		        //transfer the java object to json string and assign it to a String
		        JSONObject response1=json1.objecToJson(para1);
		        
  				return response1;
  			}
  			if (status==-2) {
  				JsonTransfer json1 = new JsonTransfer();
		        //create the java bean
		        Response para1=new Response(-2,"RETIRED");
		        //transfer the java object to json string and assign it to a String
		        JSONObject response1=json1.objecToJson(para1);
		        
  				return response1;
  				}
  			if (status==-3){
  			JsonTransfer json1 = new JsonTransfer();
	        //create the java bean
	        Response para1=new Response(-3,"Failed");
	        //transfer the java object to json string and assign it to a String
	        JSONObject response1=json1.objecToJson(para1);
	        
	        return response1;
  			}
  			
  			return null;
  			
  			
  	}
  
  	    public synchronized JSONObject loadString( String guid,final String fileName, final String programSource) throws JsonParseException, JsonMappingException, JsonGenerationException, IOException, ParseException {
  	       
  	    	try {
  	            ConcurUtilities.doOnSwingThread( new Runnable() {
  	                @Override public void run() {
  	                    StringFileSource fs = new StringFileSource() ;
  	                    fs.addString( fileName, programSource ) ;
  	                    loadTMFile( fileName,determineLanguage( fileName ), new TMFile( fs, fileName ) ) ; 
  	                }} ) ; }
  	        catch (InvocationTargetException e1) {
  	            e1.getTargetException().printStackTrace(); }
  	        
  			int reasonFlag=0;

			////////////////
			remoteTM rtm = mapForRTM.getInstance(guid);
  	        
			
			if(rtm.getStatusCode()!=3)
			{   
				reasonFlag=1;
				//create the JsonTransfer object
		        JsonTransfer json1 = new JsonTransfer();
		        //create the java bean
		        Response para1=new Response(rtm.getStatusCode(),reasonFlag,rtm.getReason());
		        //transfer the java object to json string and assign it to a String
		        JSONObject response1=json1.objecToJson(para1);

		        return response1;
				
			}
			if(rtm.getStatusCode()==3){
				//create the JsonTransfer object
		        JsonTransfer json1 = new JsonTransfer();
		        //create the java bean
		        Response para1=new Response(rtm.getStatusCode(),reasonFlag,"");
		        //transfer the java object to json string and assign it to a String
		        JSONObject response1=json1.objecToJson(para1);

		        return response1;
			}
			else{
  			
			//create the JsonTransfer object
	        JsonTransfer json1 = new JsonTransfer();
	        //create the java bean
	        Response para1=new Response(rtm.getStatusCode(),"Unkonwn error");
	        //transfer the java object to json string and assign it to a String
	        JSONObject response1=json1.objecToJson(para1);

	        return response1;
			}
  	        
			}
  	  public synchronized JSONObject initializeTheState(String guid,String responseWantedFlag) throws JsonParseException, JsonMappingException, JsonGenerationException, IOException, ParseException {
  	        try {
  	            ConcurUtilities.doOnSwingThread( new Runnable() {
  	                @Override public void run() {
  	                    if( getStatusCode() ==  TMStatusCode.COMPILED )
  	                        evaluator.initialize() ;
  	                }} ) ; }
  	        catch (InvocationTargetException e1) {
  	            e1.getTargetException().printStackTrace(); }
  	        
  	     
			remoteTM rtm = mapForRTM.getInstance(guid);
			if (rtm.getStatusCode()==4){
			//get coords
			SourceCoords focus = rtm.getCodeFocus();
			//get current line
			int n=rtm.getNumSelectedCodeLines(focus.getFile(), false);
			
			//get CodeLine object, which is the code field 
			CodeLine codel = rtm.getSelectedCodeLine(focus.getFile(), false, 1);

			//create the JsonTransfer object
			JsonTransfer json1 = new JsonTransfer();
			//create the java bean
			Response para1=new Response(rtm.getStatusCode(),0,"");
			//create the response object, which is a Json Object
			JSONObject response1=json1.objecToJson(para1);

			
			return response1;
			
			
			}
			
			
			else 
				return null;
  	    }
  	
  	  
  	//Code--1.chars,2.coords.3.markup
      public synchronized CodeLine getSelectedCodeLine(TMFile tmFile, boolean allowGaps, int index) {
          return evaluator.getSelectedCodeLine( tmFile, allowGaps, index ) ; }
     //Coordes || focus 
      public synchronized SourceCoords getCodeFocus() {
          return evaluator.getCodeFocus() ; }
      
      //
      public synchronized int getNumSelectedCodeLines(TMFile tmFile, boolean allowGaps) {
          return evaluator.getNumSelectedCodeLines( tmFile, allowGaps ) ;
      }
	
	public void setStatus(int statusCode, String message) {
		if( statusCode == TMStatusCode.NO_EVALUATOR ) {
			reason = message;
		} else {
			evaluator.setStatusCode( statusCode ) ; 
			evaluator.setStatusMessage(message) ; 
			reason = message;
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


    
    public synchronized JSONObject goBack(String guid) throws JsonParseException, JsonMappingException, JsonGenerationException, IOException, ParseException {
        try {
            ConcurUtilities.doOnSwingThread( new Runnable() {
                @Override public void run() {
                    if( evaluator != null ) {
                        evaluator.goBack() ; }
                }} ) ; }
        catch (InvocationTargetException e1) {
            e1.getTargetException().printStackTrace(); }
        
     // 1.the console response 
     		int consoleline = evaluator.getNumConsoleLines();
     		String [] consolearray=new String [consoleline];
     		for( int i = 0 ; i < consoleline ; ++ i ) {
     			String consolestring= evaluator.getConsoleLine(i);
     			consolearray[i]=consolestring;
     			}
     		
     		
     		//2.the output response
     		 int outputline = evaluator.getNumOutputLines();
     		 //System.out.println("output response is--------");
     		 String[] outputarray= new String[outputline] ;
     			for( int i = 0 ; i < outputline ; ++ i ) {
     				String outputstring= evaluator.getOutputLine(i);
     				outputarray[i] =outputstring;
     			}
     		
     		//3.focus
     		SourceCoords focus = evaluator.getCodeFocus();

     		
     		//get current line
     		int n=evaluator.getNumSelectedCodeLines(focus.getFile(), false);
     		//4.code
     		CodeLine[] codelinestore=new CodeLine[n];
     		for( int i = 0 ; i < n ; ++ i ) {
     			CodeLine codeline= evaluator.getSelectedCodeLine(focus.getFile(), false, i);
     			codelinestore[i] =codeline;
     			 }
        
		 /**get the current exp**/
		String exp = evaluator.getExpression() ;
		//create the JsonTransfer object
		JsonTransfer json1 = new JsonTransfer();
		//create the java bean
		Response para1=new Response("",mapForRTM.getInstance(guid).getStatusCode(),exp,outputarray,consolearray,codelinestore,focus);
		//create the response object, which is a Json String
		JSONObject response1;
		response1 = json1.objecToJson(para1);
		
		return response1;
    
    }

    public synchronized JSONObject redo(String guid) {
    	remoteTM rtm = mapForRTM.getInstance(guid);
        try {
            ConcurUtilities.doOnSwingThread( new Runnable() {
                @Override public void run() {
                    if( evaluator != null ) {
                        evaluator.redo() ; }
                }} ) ; }
        catch (InvocationTargetException e1) {
            e1.getTargetException().printStackTrace(); } 
        
        
        return null;
 
    }

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
    
    public synchronized JSONObject go(String guid,final String commandString ) throws JsonParseException, JsonMappingException, JsonGenerationException, IOException, ParseException {
        go( new Command() {
            public void doIt() { evaluator.go(commandString); } ; } ) ;
        //get the current expression
		String exp = evaluator.getExpression() ;
		
		// 1.the console response 
				int consoleline = evaluator.getNumConsoleLines();
				String [] consolearray=new String [consoleline];
				for( int i = 0 ; i < consoleline ; ++ i ) {
					String consolestring= evaluator.getConsoleLine(i);
					consolearray[i]=consolestring;
					}
				
				
				//2.the output response
				 int outputline = evaluator.getNumOutputLines();
				 //System.out.println("output response is--------");
				 String[] outputarray= new String[outputline] ;
					for( int i = 0 ; i < outputline ; ++ i ) {
						String outputstring= evaluator.getOutputLine(i);
						outputarray[i] =outputstring;
					}
				
				//3.focus
				SourceCoords focus = evaluator.getCodeFocus();

				
				//get current line
				int n=evaluator.getNumSelectedCodeLines(focus.getFile(), false);
				//4.code
				CodeLine[] codelinestore=new CodeLine[n];
				for( int i = 0 ; i < n ; ++ i ) {
					CodeLine codeline= evaluator.getSelectedCodeLine(focus.getFile(), false, i);
					codelinestore[i] =codeline;
					 }
    
		//create the JsonTransfer object
        JsonTransfer json1 = new JsonTransfer();
        //create the java bean

        
        Response para1=new Response("",mapForRTM.getInstance(guid).getStatusCode(),exp,outputarray,consolearray,codelinestore,focus);
        //create the response object, which is a Json String
        JSONObject response1;
        response1 = json1.objecToJson(para1);
        return response1;

        
        
    }

    public synchronized JSONObject goForward() throws JsonParseException, JsonMappingException, JsonGenerationException, IOException, ParseException{
        go( new Command() {
                public void doIt() { evaluator.goForward(); } ; } ) ; 
		 /**get the current exp**/
		String exp = evaluator.getExpression() ;
		
		// 1.the console response 
		int consoleline = evaluator.getNumConsoleLines();
		String [] consolearray=new String [consoleline];
		for( int i = 0 ; i < consoleline ; ++ i ) {
			String consolestring= evaluator.getConsoleLine(i);
			consolearray[i]=consolestring;
			}
		
		
		//2.the output response
		 int outputline = evaluator.getNumOutputLines();
		 //System.out.println("output response is--------");
		 String[] outputarray= new String[outputline] ;
			for( int i = 0 ; i < outputline ; ++ i ) {
				String outputstring= evaluator.getOutputLine(i);
				outputarray[i] =outputstring;
			}
		
		//3.focus
		SourceCoords focus = evaluator.getCodeFocus();

		
		//get current line
		int n=evaluator.getNumSelectedCodeLines(focus.getFile(), false);
		//4.code
		CodeLine[] codelinestore=new CodeLine[n];
		for( int i = 0 ; i < n ; ++ i ) {
			CodeLine codeline= evaluator.getSelectedCodeLine(focus.getFile(), false, i);
			codelinestore[i] =codeline;
			 }
		
		
		//create the JsonTransfer object
       JsonTransfer json1 = new JsonTransfer();
       //create the java bean
       //Response para1=new Response("", evaluator.getStatusCode(),exp,outputarray,consolearray,codelinestore,focus);
       Response para1=new Response("", evaluator.getStatusCode(),exp,outputarray,consolearray,codelinestore,focus);
       //create the response object, which is a Json Object
       JSONObject response1;
       response1 = json1.objecToJson(para1);
       
       return response1;
    
    }

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

	/** Should be called at the start of any example
	 * @param language The name of the language or UNKNOWN_LANG. */
    private boolean startNewJavaProject(String filename, int language) {
		//set the programText, filename and create an instance of 
		//the TMfile which receive a source file and its name as parameters
		//String fileName="test.java";

		//LanguagePIFactoryIntf languageFactory = null ;

		// Create the language object
		Language lang = JavaLangPIFactory.createInstance(filename).createPlugIn() ;


		// Restart the script manager
		ScriptManager scriptManager = ScriptManager.getManager();
		scriptManager.reset();


		//create the evaluator instance.
		try {
			evaluator = new Evaluator( lang, this, refreshMole,
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
	
	private boolean startNewCPlusPlusProject(String filename, int language) {
		//set the programText, filename and create an instance of 
		//the TMfile which receive a source file and its name as parameters
		//String fileName="CPlusPlus.cpp";

		//LanguagePIFactoryIntf languageFactory = null ;

		// Create the language object
		Language lang = CPlusPlusLangPIFactory.createInstance(filename).createPlugIn() ;


		// Restart the script manager
		ScriptManager scriptManager = ScriptManager.getManager();
		scriptManager.reset();


		//create the evaluator instance.
		try {
			evaluator = new Evaluator( lang, this, refreshMole,
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
		//Assert.check( evaluator != null  ) ;
		//java.awt.Frame dialog = new AttentionFrame( "Standby",
			//	"The Teaching Machine is loading the file.") ;
		//dialog.setVisible( true ) ;

		evaluator.compile( tmFile );

	}


	public void loadTMFile(String filename, int language, TMFile tmFile ) {
		setStatus( TMStatusCode.NO_EVALUATOR, "Loading..." ) ;
		if (language==JAVA_LANG){
		boolean ok = startNewJavaProject( filename,language ) ;
		if( ok ) {
			compile( tmFile ) ; }}
		if (language==CPP_LANG){
			boolean ok = startNewCPlusPlusProject( filename,language ) ;
			if( ok ) {
				compile( tmFile ) ; }}
	}

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

    private interface Command {
        void doIt() ; }
    
    public String getReason(){
    	return reason;
    	}
    

    
	public static final int UNKNOWN_LANG = 0;
	public static final int CPP_LANG = 1 ;
	public static final int JAVA_LANG = 2 ;

}
