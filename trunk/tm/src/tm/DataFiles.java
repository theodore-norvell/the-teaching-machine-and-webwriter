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

/*
 * Created on 2009-06-24 by Theodore S. Norvell. 
 */
package tm;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import tm.utilities.DiskFileSource;
import tm.utilities.FastFileChooser;
import tm.utilities.FileSource;
import tm.utilities.TMFile;
import tm.utilities.URLFileSource;

public class DataFiles {
    ArrayList<String> theList = new ArrayList<String>() ;
    //    { theList.add("Sunset40x30.jpg") ;
    //      theList.add("Sunset50x38.jpg") ;
    //    }
    
    FastFileChooser fd = null ;

    public void clearRemoteDataFiles() {
        theList.clear() ;
    }

    public void registerRemoteDataFile(String fileName) {
        if( !theList.contains( fileName ) )
            theList.add( fileName ) ; 
    }

    /** Return a file of the user's choice or null if the user cancels. */
    public TMFile getDataFile(URL base) throws Throwable {
        switch( theList.size() ) {
        case 0:
            return chooseTMFileWithFileChooser() ;
        case 1:
            return new TMFile( new URLFileSource( base ), theList.get(0) ) ;
        default:
            return chooseRemoteFile(base) ;
        }
    }

    private TMFile chooseRemoteFile(URL base) {
        final Object [] options = theList.toArray() ;
        final AtomicReference<String> fileNameAR  = new  AtomicReference<String>() ;
        // Just in case we're not on the swing thread...
        try {
            invokeAndWait( new Runnable() {
                public void run() {
                    Object value = JOptionPane.showInputDialog(
                                    null, 
                                    "Select file", 
                                    "Select", 
                                    JOptionPane.PLAIN_MESSAGE,
                                    null, 
                                    options, 
                                    options[0] ) ;
                    fileNameAR.set( (String) value ) ;  
                }} ) ; }
        catch (InterruptedException e) {}
        catch (InvocationTargetException e) { }
        
        String fileName = fileNameAR.get();
        if( fileName == null ) return null ;
        else return new TMFile( new URLFileSource(base), fileName ) ;
    }

    private TMFile chooseTMFileWithFileChooser() throws Throwable {
        final AtomicReference<File> fileAR  = new  AtomicReference<File>() ;
        // Just in case we're not on the swing thread...
        try {
            invokeAndWait( new Runnable() {
                public void run() {
                    if( fd == null ) fd = new FastFileChooser(new File(".")) ;
                    int value = fd.showOpenDialog(null) ;
                    if( value == fd.APPROVE_OPTION )
                        fileAR.set( fd.getSelectedFile() ) ;
                }} ) ; }
        catch (InterruptedException e) {}
        catch (InvocationTargetException e) {
            Throwable t = e.getCause() ;
            if( t!=null) throw t ;
        }
        
        File file = fileAR.get() ;
        if( file == null ) return null ;
        file = file.getAbsoluteFile() ;
        return new TMFile( new DiskFileSource(file.getParentFile()), file.getName() );
    }
    
    static void invokeAndWait( Runnable r ) throws InterruptedException, InvocationTargetException 
    // TODO Move this to some class in tm.utilities.
    {
        if( SwingUtilities.isEventDispatchThread() ) r.run();
        else SwingUtilities.invokeAndWait(r) ;
    }
 
}