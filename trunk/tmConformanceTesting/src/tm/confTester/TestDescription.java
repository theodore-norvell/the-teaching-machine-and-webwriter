/*
 * Created on Mar 25, 2005
 *
 * 
 */
package tm.confTester;

import java.util.ArrayList;
import java.util.regex.*;

import tm.utilities.*;

/** A description of a test case
 * @author theo
 */
class TestDescription {
    public static final int NONE=0, ANY=1, EQUALS=2, MATCHES=3 ;
    
    private boolean doCompile = false ;
    private int expectedCompileError = 0 ;
    private String expectedCompileErrorMessage = "" ;
    
    private ArrayList/* <ExecutionCase> */  executions = new ArrayList() ;
    int exCount = 0  ;  // Inv: executions.size() == exCount 
    
    private ExecutionCase execution( int i ) {
        return (ExecutionCase) executions.get(i) ;
    }
    
    void setDoCompile( boolean doCompile ) { this.doCompile = doCompile ; }
    
    boolean getDoCompile() { return doCompile ; }
    
    void setExpectedCompileError( int expectedCompileError,
                                  String expectedCompileErrorMessage ) {
        this.expectedCompileError = expectedCompileError ;
        this.expectedCompileErrorMessage = expectedCompileErrorMessage ; }
    
    
    int checkCompileError( String actualCompileError ) {
        return check( expectedCompileError, expectedCompileErrorMessage, actualCompileError ) ;
    }
    
    void addExecutionCase( ) {
        this.executions.add( exCount++, new ExecutionCase() ) ; }
    
    int getExecutionCount() {
        return exCount ; }
    
    void setExpectedExecuteError( int expectedExecuteError,
                                  String expectedExecuteErrorMessage ) {
        execution(exCount-1).expectedExecuteError = expectedExecuteError ;
        execution(exCount-1).expectedExecuteErrorMessage = expectedExecuteErrorMessage ; }
    
    int checkExecuteError( int i, String actualExecuteError ) {
        int expectedExecuteError = execution(i).expectedExecuteError ;
        String expectedExecuteErrorMessage = execution(i).expectedExecuteErrorMessage ;
        return check( expectedExecuteError, expectedExecuteErrorMessage, actualExecuteError ) ;
    }
    
    void setInputString( String str ) {
        execution(exCount-1).inputString = str ; }
    
    String getInput(int i) {
        return execution(i).inputString ; }
    
    void setExpectedExecuteOutput( int expectedExecuteOutput,
                                   String expectedExecuteOutputString ) {
        execution(exCount-1).expectedExecuteOutput = expectedExecuteOutput ;
        execution(exCount-1).expectedExecuteOutputString = expectedExecuteOutputString ; }
    
    int checkExecuteOutput( int i, String actualExecuteOutput ) {
        int expectedExecuteOutput = execution(i).expectedExecuteOutput ;
        String expectedExecuteOutputString = execution(i).expectedExecuteOutputString ;
        return check( expectedExecuteOutput, expectedExecuteOutputString, actualExecuteOutput ) ; }
     
   String formatExpectedCompilerError() {
       return format( expectedCompileError, expectedCompileErrorMessage ) ;
   }
   
   String formatExpectedExecuteError(int i) {
       int expectedExecuteError = execution(i).expectedExecuteError ;
       String expectedExecuteErrorMessage = execution(i).expectedExecuteErrorMessage ;
       return format( expectedExecuteError, expectedExecuteErrorMessage ) ;
   }
   
   String formatExpectedExecuteOutput(int i) {
       int expectedExecuteOutput = execution(i).expectedExecuteOutput ;
       String expectedExecuteOutputString = execution(i).expectedExecuteOutputString ;
       return format( expectedExecuteOutput, expectedExecuteOutputString ) ;
   }
   
   String format( String message ) {
       if( message == null ) return "null" ;
       else return "\"" + ConfTesterParser.formatString( message, '"', '\\' ) + "\"" ; }
       
   String format(int code,
                         String message) {
       if( code == NONE ) return "none" ;
       else if( code == ANY ) return "any" ;
       else if( code == EQUALS )
           return "equals " + format( message ) ;
       else if( code == MATCHES )
           return "matches /" + ConfTesterParser.formatString( message, '/', '%' ) + "/" ;
       else {
           Assert.check(false) ; return null ; }
    }
    
    private int check( int expected, String pattern, String target ) {
        switch( expected ) {
        case NONE :  {
            return target == null ? StatusCodes.PASS : StatusCodes.FAIL ; }
        case ANY  :  {
            return target != null ? StatusCodes.PASS : StatusCodes.FAIL ; }
        case EQUALS : {
            return target == null           ? StatusCodes.FAIL :
                   pattern.equals( target ) ? StatusCodes.PASS :
                                              StatusCodes.MISMATCH ; }
        case MATCHES : {
            Pattern pat = Pattern.compile( pattern, Pattern.MULTILINE|Pattern.DOTALL ) ;
            return target == null                  ? StatusCodes.FAIL : 
                   pat.matcher( target ).matches() ? StatusCodes.PASS :
                                                     StatusCodes.MISMATCH ; }
        default: Assert.check(false) ;
            return StatusCodes.FAIL ;}
    }
    
    private class ExecutionCase {
        int expectedExecuteError = 0 ;
        String expectedExecuteErrorMessage = "" ;

        String inputString = "" ;
        
        int expectedExecuteOutput = 0 ;
        String expectedExecuteOutputString = "" ;
    }
}