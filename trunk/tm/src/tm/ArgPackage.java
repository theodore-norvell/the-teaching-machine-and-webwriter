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
 * Created on 6-Nov-2006 by Theodore S. Norvell. 
 */
package tm;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

public class ArgPackage {
    /** The file to be loaded.
     *  Defaults to null. */
    public URL fileToLoad ;
    /** The directory in which the application is installed.
     * Defaults to (new File(".")).toURL() */
    public URL installDirectory ;
    /** The file containing the initial configuration file.
     * Defaults to null. */
    public File initialConfiguration ;
    
    /** Return null if the args don't make sense. Otherwise return a new ArgPackage.
     * @param args
     * @return
     */
    public static ArgPackage processArgs( String[] args ) {
        ArgPackage result = new ArgPackage() ;
        int i = 0 ;
        while( i < args.length ) {
            if( args[i].equalsIgnoreCase( "-installDirectory")
                    || args[i].equalsIgnoreCase("-id") ) {
                i += 1 ;
                if( i < args.length && result.installDirectory == null ) {
                    File installDirectory = new File(args[i]+"/") ;
                    if( ! installDirectory.isDirectory() ) {
                        System.err.println("No directory "+ installDirectory ) ;
                        return null ;
                    }
                    else
                        try { result.installDirectory  = installDirectory.toURL() ; }
                        catch( MalformedURLException ex ) {
                            System.err.println("Can not convert "+ installDirectory + " to URL." ) ;
                            return null ;
                        }
                }
                else
                    return null ; }
            else if( args[i].equalsIgnoreCase( "-initialConfiguration")
                    || args[i].equalsIgnoreCase("-ic") ) {
                i += 1 ;
                if( i < args.length && result.initialConfiguration == null ) {
                    File initialConfiguration = new File(args[i]) ;
                    if( ! initialConfiguration.exists() || ! initialConfiguration.canRead() ) {
                        System.err.println("Can not read file "+ initialConfiguration ) ;
                        return null ; }
                    else {
                        result.initialConfiguration  = initialConfiguration.getAbsoluteFile() ; } }
                else
                    return null ; }
            else if( result.fileToLoad == null ){
                File fileToLoad = new File(args[i]) ;
                if( ! fileToLoad.exists() || !fileToLoad.canRead() ) {
                    System.err.println("Can not read file "+ fileToLoad ) ;
                    return null ; }
                else 
                    try { result.fileToLoad  = fileToLoad.toURL() ; }
                    catch( MalformedURLException ex ) {
                        System.err.println("Can not convert "+ fileToLoad + " to URL." ) ;
                        return null ; } }
            else {
                return null ; }
            ++i ; }
        
        // Default values.  
        
        if( result.installDirectory == null ) {
            File installDirectory = new File(".") ;
            try { result.installDirectory  = installDirectory.toURL() ; }
            catch( MalformedURLException ex ) {
                System.err.println("Can not convert "+ installDirectory + " to URL." ) ;
                return null ; } }
        
        return result ;
    }
}