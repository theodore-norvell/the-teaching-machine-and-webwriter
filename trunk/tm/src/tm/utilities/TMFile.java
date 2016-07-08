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
import java.util.Observable;

import tm.portableDisplays.TMFileI;

/**
 * A representation of a Teaching Machine file
 *
 * @author TN
 */
public class TMFile extends Observable implements TMFileI{
    static int fileCount = 0 ;
    private FileSource fileSource ;	// the location of the file
    private String fileName ;
    private int uniqueFileNumber ;

    /** Create a TM file.
     * A TMFile is basically a relative file name and a file source.
     * The name can contain forward slashes (/) which may or may
     * not be interpreted as separating directories.
     * 
     * @param fileSource
     * @param fileName
     */
    public TMFile( FileSource fileSource, String fileName ) {
        this.fileSource = fileSource ;
        this.fileName = fileName ;
        this.uniqueFileNumber = fileCount++ ; }

    /** Check for equality of TMFile objects.
     * Equality means object identity.
     * 
     */
    public boolean equals( Object obj ) {
        if( !( obj instanceof TMFile ) ) return false ;
        TMFile file = (TMFile) obj ;
        return uniqueFileNumber == file.uniqueFileNumber ; }

    /** A hash code that is typically unique to the object */
    public int hashCode() {
        return uniqueFileNumber ; }

    /** A unique identifier, that is unique to the object. */
    public int getUniqueNumber() { return uniqueFileNumber ; }

    /** A short user friendly string identifying the the file. */
    public String toString() { return fileName ; }

    /** The underlying file source */
    public FileSource getFileSource() { return fileSource ; }

    /** The file name */
    public String getFileName() { return fileName ; }
    
    /** Can the file be read */
    public boolean isReadable(){
        Reader reader = toReader();
        if (reader != null) {
            try{
                reader.close();
                return true; }
            catch(IOException e){
                Assert.check(false) ; } }
        return false;
    }
    
    /** Returns a reader or null.
     * Null is returned if the reader can not be created for some reason. */
    public Reader toReader( ) {
        try{
            Reader reader = fileSource.fileNameToReader( fileName );
            return reader;
        }
        catch(IOException e) {
            return null;
        }
    }
    
    /** Read the entire file into a string */
    public String readFile() throws IOException {
        return fileSource.readFile( fileName ) ;
    }

    /** Overwrite the entire file contents with a single string. */
    public void writeFile( String str ) throws IOException {
        fileSource.writeFile( fileName, str ) ;
        // If that worked, notify the observers.
        setChanged() ;
        notifyObservers() ;
    }
    
    public BufferedImage readImage() throws IOException {
        return fileSource.readImage( fileName ) ;
    }
}