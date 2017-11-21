package tm.gwt.server;

import java.io.Reader;
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
import tm.interfaces.StateInterface ;
import tm.interfaces.TMStatusCode;
import tm.languageInterface.Language;
import tm.languageInterface.LanguagePIFactoryIntf;
import tm.plugins.PlugInManager;
import tm.plugins.PlugInNotFound;
import tm.utilities.Assert;
import tm.utilities.FileSource;
import tm.utilities.LocalResourceFileSource;
import tm.utilities.StringFileSource;
import tm.utilities.TMFile;
import tm.utilities.URLFileSource;
import tm.virtualMachine.SelectionParser;


public class EvaluatorWrapper {

    final private Evaluator evaluator ;
    final private TMServiceStatusReporter statusReporter ;


    public EvaluatorWrapper(int language, TMServiceResult result ) throws Throwable {
        this.statusReporter = new TMServiceStatusReporter( result ) ;
        
        // Read the configuration file.
        FileSource source = new LocalResourceFileSource(this.getClass(), "", ".tmcfg") ;
        TMFile configFile = new TMFile( source, "default" ) ;

        Reader reader = configFile.toReader() ;
        Assert.apology( reader != null, "Can not open " + configFile);
        tm.configuration.ConfigurationServer server = tm.configuration.ConfigurationServer.getConfigurationServer();
        server.readConfiguration( reader ) ;
        //        System.out.println("reconfigure with "+configFile.toString());server.dump();
        reader.close() ;

        // Get the appropriate factory object from the plug-in manager
        PlugInManager pim = PlugInManager.getSingleton() ;
        LanguagePIFactoryIntf languageFactory = null ;
        String languageName = null ;
        try {
            if( language == StateInterface.CPP_LANG ) {
                languageName = "C++" ;
                languageFactory = pim.getFactory( TMBigAppletPIFactory.jackNameForCPP, LanguagePIFactoryIntf.class, true ); }
            else if( language == StateInterface.JAVA_LANG ) {
                languageName = "Java" ;
                languageFactory = pim.getFactory( TMBigAppletPIFactory.jackNameForJava, LanguagePIFactoryIntf.class, true );  }
            else {
                Assert.error( "Unknown language." ) ; } }
        catch( PlugInNotFound e ) {
            String errMess = "Sorry "+languageName+" is not supported in this release." ;
            statusReporter.setStatus( TMStatusCode.NO_EVALUATOR, errMess ) ;
            statusReporter.attention( errMess, e ) ;
            evaluator = null ;
            return ; }


        // Create the language object
        Language lang = languageFactory.createPlugIn() ;

        evaluator = new Evaluator( lang, statusReporter, new NullRefresher(),
                SelectionParser.parse(CommandInterface.DEFAULT_SELECTION),
                new NullInputter(),
                TMBigApplet.boStatic, TMBigApplet.toStatic,
                TMBigApplet.boHeap, TMBigApplet.toHeap,
                TMBigApplet.boStack, TMBigApplet.toStack,
                TMBigApplet.boScratch, TMBigApplet.toScratch ) ;
        statusReporter.setEvaluator( evaluator );
        statusReporter.setStatus( TMStatusCode.READY_TO_COMPILE, "Evaluator is built. Ready to compile.") ;
    }

    public synchronized void loadString(TMServiceResult result, String fileName, String programSource) {
        statusReporter.setResult( result ) ; 
        StringFileSource fs = new StringFileSource() ;
        fs.addString( fileName, programSource ) ;
        TMFile tmFile = new TMFile( fs, fileName ) ;
        if(evaluator.getStatusCode() == TMStatusCode.READY_TO_COMPILE){
            evaluator.compile( tmFile ) ;
            result.statusCode = evaluator.getStatusCode();
            result.statusMessage = evaluator.getStatusMessage();
        }
        result.resultState = new MirrorState() ;
        result.resultState.update( evaluator ); 
        statusReporter.setResult( null ) ; //One line source code
    }


    public synchronized TMServiceResult loadRemoteFile(TMServiceResult result,String urlInString, String fileName) {
        result.resultState = new MirrorState() ;
        statusReporter.setResult( result ) ;
        URL url ;
        try {			
            url = new URL(urlInString);
        } catch (MalformedURLException e1) {
            statusReporter.setStatus(TMStatusCode.NO_EVALUATOR, "Malformed URL");
            return result;
        }
        FileSource fs =  new URLFileSource( url )  ;
        statusReporter.setStatus(TMStatusCode.NO_EVALUATOR, "Loading..." );
        TMFile tmFile = new TMFile( fs, fileName ) ;
        if(result.statusCode == TMStatusCode.READY_TO_COMPILE){
            evaluator.compile( tmFile ) ;
            result.statusCode = evaluator.getStatusCode();
            result.statusMessage = evaluator.getStatusMessage();
        } 
        result.resultState.update( evaluator );  
        return result;
    }


    public synchronized TMServiceResult initializeTheState(TMServiceResult result) {
        statusReporter.getResult().resultState = new MirrorState() ;
        statusReporter.setResult( result ) ;
        if( result.statusCode ==  TMStatusCode.COMPILED ){
            evaluator.initialize() ;
            result.statusCode = evaluator.getStatusCode();
            result.statusMessage = evaluator.getStatusMessage();
        }
        result.resultState.update( evaluator );  
        return statusReporter.getResult();
    }


    public synchronized TMServiceResult go(TMServiceResult result, String commandString) {
        if( evaluator != null ) {
            int statusCode = statusReporter.getStatusCode() ;
            if( statusCode == TMStatusCode.COMPILED ) {
                evaluator.initialize() ;
                result.statusCode = evaluator.getStatusCode();
                result.statusMessage = evaluator.getStatusMessage();

            }
            else if( statusCode == TMStatusCode.READY ){
                evaluator.go(commandString);
                result.statusCode = evaluator.getStatusCode();
                result.statusMessage = evaluator.getStatusMessage();          	
            } }  
        statusReporter.getResult().resultState.update( evaluator ); 
        return statusReporter.getResult() ;
    }


    public synchronized TMServiceResult goBack(TMServiceResult result) {
        if( evaluator != null ) {
            evaluator.goBack() ;
            result.statusCode = evaluator.getStatusCode();
            result.statusMessage = evaluator.getStatusMessage();
        }
        statusReporter.getResult().resultState.update( evaluator ); 
        return statusReporter.getResult() ;
    }


    public synchronized TMServiceResult toCursor(TMServiceResult result, String fileName, int cursor) {
        evaluator.toCursor(fileName, cursor);
        result.statusCode = evaluator.getStatusCode();
        result.statusMessage = evaluator.getStatusMessage();
        statusReporter.getResult().resultState.update( evaluator ); 
        return statusReporter.getResult() ;
    }

    public synchronized  TMServiceResult getResult(){
        return statusReporter.getResult();
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
}
