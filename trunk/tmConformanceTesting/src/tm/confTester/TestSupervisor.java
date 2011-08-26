/*
 * Created on Mar 25, 2005
 */
package tm.confTester;

import java.io.* ;
import java.util.regex.*;

/**
 * @author theo
 */
public class TestSupervisor {
    
    String message = "" ;
    
    int runTest(File directory, String fileName, CompilerAdapter compiler)
    throws Throwable {
        File file = new File( directory.getAbsolutePath() + File.separatorChar + fileName ) ;
        if( file.isDirectory() ) {
            message = "File "+file+" is directory" ;
            return StatusCodes.NONTEST ; }
        if( ! file.canRead() ) {
            message = "File "+file+" is can not be read" ;
            return StatusCodes.NONTEST ; }
        Reader reader = new FileReader( file ) ;
        ConfTesterParser parser = new ConfTesterParser(reader) ;
        TestDescription description = new TestDescription() ;
        parser.start( description ) ;
        
        if( ! description.getDoCompile() ) {
            message = "File "+file+" is not a test\n" ;
            return StatusCodes.NONTEST ; } ;
        
        compiler.init() ;
        compiler.compile( directory, fileName ) ;
        
        String string = compiler.getState() == CompilerAdapter.COMPILE_FAILED
                           ? compiler.getCompilerErrorOutput() 
                           : null ;
           
        int code = description.checkCompileError( string ) ;
        if( code != StatusCodes.PASS ) {
            String failureDescription = (code==StatusCodes.FAIL ? "Fail" : "Mismatch") ;
            message = "File      " + file + ".\n"
                    + "   " + failureDescription + " on compilation.\n"
                    + "   Error    " + description.format(TestDescription.EQUALS, string) + "\n"
                    + "   Expected " + description.formatExpectedCompilerError() + "\n"
                    + failureDescription +"\n";
            return code ; }
        
        if( description.getExecutionCount() == 0 ) {
            message = "File      " + file + ".\n"
                    + "   Pass on compilation.\n"
                    + "   Error    " + description.format(TestDescription.EQUALS, string) + "\n"
                    + "   Expected " + description.formatExpectedCompilerError() + "\n" ;
            return StatusCodes.PASS ; }
        
        String passes = "" ;
        for( int count = 0 ; count < description.getExecutionCount() ; ++count ) {
            String inputString = description.getInput(count) ;
            compiler.run( inputString ) ;
            
            string = compiler.getState() == CompilerAdapter.RUN_FAILED
            ? compiler.getExecuteErrorString() 
                    : null ;
            code = description.checkExecuteError( count, string ) ;                
            if( code != StatusCodes.PASS ) {
                String failureDescription = (code==StatusCodes.FAIL ? "Fail" : "Mismatch") ;
                message = "File      " + file + ".\n"
                + "   " + failureDescription + " on execution.\n"
                + passes
                + "   Input    " + description.format(TestDescription.EQUALS, inputString) + "\n"
                + "   Error    " + description.format(TestDescription.EQUALS, string) + "\n"
                + "   Expected " + description.formatExpectedExecuteError(count)  + "\n"
                + (code==StatusCodes.FAIL ? "Fail" : "Mismatch")+"\n";
                return code ; }
            
            string = compiler.getState() == CompilerAdapter.RUN_SUCCEEDED
            ? compiler.getExecuteOutputString() 
                    : null ;
            code = description.checkExecuteOutput( count, string ) ;
            if( code != StatusCodes.PASS ) {
                message = "File      " + file + ".\n"
                + "   Fail on execution.\n"
                + passes
                + "   Input    " + description.format(TestDescription.EQUALS, inputString) + "\n"
                + "   Output   " + description.format(TestDescription.EQUALS, string) + "\n"
                + "   Expected " + description.formatExpectedExecuteOutput(count)  + "\n"
                + "Fail\n";;
                return StatusCodes.FAIL ; }
            
            passes += "   Input    " + description.format(TestDescription.EQUALS, inputString) + "\n"
                    + "   Output   " + description.format(TestDescription.EQUALS, string) + "\n"
                    + "   Expected " + description.formatExpectedExecuteOutput(count)  + "\n";
            compiler.reinit() ;
        }
        message = "File      " + file + ".\n" 
            +     "   Pass on execution.\n" + passes ;
        return StatusCodes.PASS ;
    }
    
    
    /**
     * @return Returns the message.
     */
    protected String getMessage() {
        return message;
    }
}
