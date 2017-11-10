package tm.gwt.server;

import java.net.MalformedURLException;
import java.net.URL;
import tm.TMBigApplet;
import tm.TMBigAppletPIFactory;
import tm.evaluator.Evaluator;
import tm.gwt.shared.TMServiceResult;
import tm.gwt.state.MirrorState;
import tm.interfaces.CommandInterface;
import tm.interfaces.EvaluatorInterface;
import tm.interfaces.Inputter;
import tm.interfaces.TMStatusCode;
import tm.languageInterface.Language;
import tm.languageInterface.LanguagePIFactoryIntf;
import tm.plugins.PlugInManager;
import tm.plugins.PlugInNotFound;
import tm.utilities.Assert;
import tm.utilities.CachingFileSource;
import tm.utilities.FileSource;
import tm.utilities.StringFileSource;
import tm.utilities.TMFile;
import tm.utilities.URLFileSource;
import tm.virtualMachine.SelectionParser;


public class EvaluatorWrapper {
	
	private Evaluator evaluator;
	TMServiceStatusReporter statusReporter ;
	
	
	public EvaluatorWrapper(int language, TMServiceStatusReporter statusReporter ) throws Throwable {
		this.statusReporter = statusReporter ;
        // Get the appropriate factory object from the plug-in manager
        PlugInManager pim = PlugInManager.getSingleton() ;
        LanguagePIFactoryIntf languageFactory = null ;
        String languageName = null ;
        try {
            if( language == TMBigApplet.CPP_LANG ) {
                languageName = "C++" ;
                languageFactory = pim.getFactory( TMBigAppletPIFactory.jackNameForCPP, LanguagePIFactoryIntf.class, true ); }
            else if( language == TMBigApplet.JAVA_LANG ) {
                languageName = "Java" ;
                languageFactory = pim.getFactory( TMBigAppletPIFactory.jackNameForJava, LanguagePIFactoryIntf.class, true );  }
            else {
                Assert.error( "Unknown language." ) ; } }
        catch( PlugInNotFound e ) {
                String errMess = "Sorry "+languageName+" is not supported in this release." ;
                statusReporter.setStatus( TMStatusCode.NO_EVALUATOR, errMess ) ;
                statusReporter.attention( errMess, e ) ;
                throw e ; }
        

        // Create the language object
        Language lang = languageFactory.createPlugIn() ;
		
		evaluator = new Evaluator( lang, statusReporter, new NullRefresher(),
                SelectionParser.parse(CommandInterface.DEFAULT_SELECTION),
                new NullInputter(),
                TMBigApplet.boStatic, TMBigApplet.toStatic,
                TMBigApplet.boHeap, TMBigApplet.toHeap,
                TMBigApplet.boStack, TMBigApplet.toStack,
                TMBigApplet.boScratch, TMBigApplet.toScratch ) ;
	}
	
    public void loadString(TMServiceResult result, String fileName, String programSource) {
    	statusReporter.setResult( result ) ; 
        StringFileSource fs = new StringFileSource() ;
        fs.addString( fileName, programSource ) ;
        TMFile tmFile = new TMFile( fs, fileName ) ;
    	evaluator.compile( tmFile ) ;
    	result.resultState = new MirrorState() ;
    	result.resultState.update( evaluator ); 
    	statusReporter.setResult( null ) ; //One line source code
    }


    public TMServiceResult loadRemoteFile(TMServiceResult result,String root, String fileName) {
    	//Do we need to pass in URL?
    	statusReporter.setResult( result ) ;
        URL url = null;
		try {
			url = new URL(fileName);
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        URL rootURL;
        try {
        	rootURL = new URL( url, root ) ; }
        catch( MalformedURLException e ) {
        	result.statusCode = TMStatusCode.NO_EVALUATOR;
        	statusReporter.setStatus(result.statusCode, "Malformed URL");
            return result ; }
        FileSource fs = new CachingFileSource( new URLFileSource( rootURL ) ) ;
        result.statusCode = TMStatusCode.NO_EVALUATOR;
        statusReporter.setStatus(TMStatusCode.NO_EVALUATOR, "Loading..." );
        TMFile tmFile = new TMFile( fs, fileName ) ;
        evaluator.compile( tmFile ) ; 
    	result.resultState = new MirrorState() ;
    	result.resultState.update( evaluator );  
    	return result;
    }


    public TMServiceResult initializeTheState() {
        if( statusReporter.getStatusCode() ==  TMStatusCode.COMPILED )
            evaluator.initialize() ;
        	statusReporter.setStatus(TMStatusCode.READY, "Evaluator starts.");
        statusReporter.getResult().resultState = new MirrorState() ;
        statusReporter.getResult().resultState.update( evaluator ); 
        return statusReporter.getResult();
    }


    public TMServiceResult go(String commandString) {
        if( evaluator != null ) {
        	int statusCode = statusReporter.getStatusCode() ;
            if( statusCode == TMStatusCode.COMPILED ) {
                evaluator.initialize() ; }
            else if( statusCode == TMStatusCode.READY ){
            	evaluator.go(commandString);} }  
        statusReporter.getResult().resultState = new MirrorState() ;
        statusReporter.getResult().resultState.update( evaluator ); 
        return statusReporter.getResult() ;
    }


    public TMServiceResult goBack() {
    	if( evaluator != null ) {
            evaluator.goBack() ; }
        statusReporter.getResult().resultState = new MirrorState() ;
        statusReporter.getResult().resultState.update( evaluator ); 
    	return statusReporter.getResult() ;
    }


    public TMServiceResult toCursor(String fileName, int cursor) {
        evaluator.toCursor(fileName, cursor);
        statusReporter.getResult().resultState = new MirrorState() ;
        statusReporter.getResult().resultState.update( evaluator ); 
        return statusReporter.getResult() ;
    }
    
    private class NullInputter implements Inputter {

		@Override
		public void requestMoreInput(EvaluatorInterface evaluator) {
			// Do nothing
		}
    }
    
    private class NullRefresher implements Evaluator.Refreshable {

		@Override
		public void refresh() {
			// Do nothing
		}
    }
    
    private int determineLanguage( String fileName ) {
        fileName = fileName.toLowerCase() ;
        if(    fileName.endsWith(".jav")
            || fileName.endsWith( ".java" ) )
            return TMBigApplet.JAVA_LANG ;
        else if(    fileName.endsWith( ".cpp" )
                 || fileName.endsWith( ".cxx")
                 || fileName.endsWith( ".c++" )
                 || fileName.endsWith( ".c" ))
            return TMBigApplet.CPP_LANG ;
        else
            return TMBigApplet.UNKNOWN_LANG ; }

}
