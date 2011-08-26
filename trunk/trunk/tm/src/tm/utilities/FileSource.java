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

package tm.utilities;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.Reader;
import java.io.FileNotFoundException;
import java.io.StringReader;
import java.io.Writer;

/** A place where files can be found
 * Objects of this abstract class represent places where files
 * can be found. For example, directories on a local
 * disk, directories on remote computers, etc.
 */

abstract public class FileSource {


    /** Return the file as a Reader.
     */
    Reader fileNameToReader(String fileName) throws IOException {
        return new StringReader( readFile( fileName ) ) ; }

    /** Convenience method for reading files into strings.
     * Either this method or fileNameToReader must be overridden. */
    protected String readFile( String fileName )
    throws IOException {
        StringBuffer sb = new StringBuffer() ;
        char[] cb = new char[1024] ;
        Reader r = fileNameToReader(fileName) ;
        int numRead ;
        while( -1 != (numRead = r.read( cb ) ) ) {
               sb.append(cb, 0, numRead) ; }
        return sb.toString() ; }
    
    void writeFile( String fileName, String str ) throws IOException {
        throw new IOException( "File "+fileName+" can not be written by "+toString()) ;
    }
    
    abstract public BufferedImage readImage( String fileName ) throws IOException ;
}