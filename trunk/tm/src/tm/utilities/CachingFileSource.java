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
 * Created on 17-Sep-2005 by Theodore S. Norvell. 
 */
package tm.utilities;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;
import java.util.Hashtable;

/** A file source that caches every file it has ever read.
 * Since trying to read a file can lead to an IOException,
 * this object also caches the exceptions.
 * The purpose is to reduce the IO demands on the file source;
 * this seems to matter a lot for applets loading resources.
 * @author theo
 */
public class CachingFileSource extends FileSource {
    
    private FileSource fileSource;
    // The cached result could be a String or an IOException.
    private Hashtable<String,Object> cache = new Hashtable<String,Object>() ;

    public CachingFileSource( FileSource fileSource ) {
        this.fileSource = fileSource ;
    }
    
    /**
     * @see tm.utilities.FileSource#readFile(java.lang.String)
     */
    protected String readFile( String fileName )
    throws IOException {
        Object cachedResult ;
        if( ! cache.containsKey( fileName ) ) {
            try {
                cachedResult = fileSource.readFile( fileName ) ; }
            catch( IOException e ) {
                cachedResult = e ; }
            cache.put( fileName, cachedResult ) ; }
        else {
            cachedResult = cache.get( fileName ) ; }
        
        if( cachedResult instanceof IOException ) {
            throw (IOException)cachedResult ; }
        else {
            return (String)cachedResult ; }
    }
    
    @Override
    void writeFile( String fileName, String str ) throws IOException {
        fileSource.writeFile( fileName, str ) ;
        // If that didn't throw an exception, update the cache.
        cache.put( fileName, str ) ; 
    }
    
    @Override
    public BufferedImage readImage( String fileName ) throws IOException {
        // I can't figure out a way to use the cached result
        return fileSource.readImage( fileName ) ;
    }
}