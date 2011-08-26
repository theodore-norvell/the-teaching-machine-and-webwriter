/*
 * Created on Apr 8, 2005
 *
 */
package tm.confTester;

import java.awt.Color;
import java.io.File;
import java.io.IOException;

import javax.swing.SwingUtilities;

/**
 * @author theo
 *
 */
public class WorkerThread extends Thread {
    private File root ;
    private File selection ;
    private ConformanceTester frame ;
    private boolean[] poisonPill ;
    private TestSupervisor testSupervisor ;
    private TestHistory testHistory ;
    private CompilerAdapter adapter ;
    
    WorkerThread( ConformanceTester frame,
            File root,
            File selection,
            TestSupervisor testSupervisor,
            TestHistory testHistory,
            CompilerAdapter adapter,
            boolean[] poisonPill) {
        this.root = root ;
        this.selection = selection ;
        this.frame = frame ;
        this.testSupervisor = testSupervisor ;
        this.testHistory = testHistory ;
        this.adapter = adapter ;
        this.poisonPill = poisonPill ;
    }
    
    public void run() {
        try { 
            doAllTests( selection ) ; }
        catch( Throwable e ) {
            frame.putln( "Task crashed. Reason: " + e.getMessage(), ConformanceTester.DARKRED ) ; }
        finally {
            testHistory.dump(System.out) ;
            frame.putln( "Task complete", ConformanceTester.DARKBLUE ) ;
            SwingUtilities.invokeLater(
                    new Runnable() {
                        public void run() {
                            frame.exitWorkingMode() ; } }
                ) ;} }

    private void doAllTests( File file ) {
        if( poisonPill[0] ) throw new Error("Requested Stop." ) ;
        if( file.isDirectory() ) {
            File[] children = file.listFiles() ;
            for( int i=0, sz=children.length ; i < sz ; ++i ) {
                doAllTests( children[i] ) ; } }
        else {
            File localRoot = root != null ? root : file.getParentFile() ;
            if( localRoot == null ) {
                frame.putln( "File "+file+" is not under any directory.", Color.BLACK) ; }
            else {
                String fileName = extractRelativeLocation( localRoot, file ) ;

                if( fileName == null ) {
                    frame.putln( "File "+file+" is not under the root directory.", Color.BLACK) ; }
                else if( adapter.isFileNameSuitable( fileName ) )
                    doTest( localRoot, fileName ) ; }
        }
    }
    
    private String extractRelativeLocation( File localRoot, File file ) {
        try {
            String canonicalRoot = localRoot.getCanonicalPath() ;
            String canonicalFile = file.getCanonicalPath() ;
            if( canonicalRoot.equals( canonicalFile ) ) {
                return "" ; }
            else if( canonicalFile.startsWith( canonicalRoot )
                    && canonicalFile.charAt( canonicalRoot.length() ) == File.separatorChar ) {
                return canonicalFile.substring( canonicalRoot.length()+1 ) ; }
            else {
                return null ; } }
        catch( IOException e ) {
            frame.putln( "Could not convert file to path", Color.BLACK ) ;
            return null ; }
    }
    
    /** 
     * @param directory -- the root
     * @param fileName  -- the test file. 
     */
    private void doTest( File directory, String fileName ) {
        frame.putln( "Starting test " + fileName + " in " + directory, Color.BLACK ) ;
        String fullName = fileName+"@"+directory.getPath() ;
        try {
            int result = testSupervisor.runTest( directory, fileName, adapter ) ;
            String message = testSupervisor.getMessage() ;
            if( result == StatusCodes.PASS ) {
                frame.putln( message, ConformanceTester.DARKGREEN ) ;
                testHistory.record(fullName, StatusCodes.PASS)  ; }
            else if( result == StatusCodes.MISMATCH ) {
                frame.putln( message, ConformanceTester.DARKORANGE ) ;
                testHistory.record(fullName, StatusCodes.MISMATCH) ; }
            else if( result == StatusCodes.FAIL ) {
                frame.putln( message, ConformanceTester.DARKRED ) ;
                testHistory.record(fullName, StatusCodes.FAIL) ; }
            else {
                frame.putln( message, Color.BLACK ) ;
                testHistory.record(fullName, StatusCodes.NONTEST) ; } }
        catch( Throwable e ) {
            frame.putln( "File      " + fileName + ".\n"
                    + "   Error " + e.getMessage() + "\n",
                   ConformanceTester.DARKBLUE ) ;
            testHistory.record(fullName, StatusCodes.ERROR) ; }
        SwingUtilities.invokeLater(
                new Runnable() {
                    public void run() {
                        frame.updateStatus() ; } }
            ) ;
    }

}