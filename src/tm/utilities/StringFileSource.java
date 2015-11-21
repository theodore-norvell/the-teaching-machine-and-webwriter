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
import java.io.Reader;
import java.io.IOException;
import java.util.Hashtable;
import java.io.FileNotFoundException;
import java.io.StringReader;

public class StringFileSource extends FileSource {
    private Hashtable h = new Hashtable() ;

    public void addString( String fileName, String fileContents ) {
        h.put( fileName, fileContents ) ;
    }

    protected String readFile( String fileName ) throws IOException {
        Object o = h.get( fileName ) ;
        if( o==null ) throw new FileNotFoundException() ;
        return (String) o ;
    }

    Reader fileNameToReader(String fileName) throws IOException {
        return new StringReader( readFile( fileName ) ) ;
    }
    
    @Override
    void writeFile( String fileName, String str ) throws IOException {
        addString( fileName, str ) ;
    }
    
    @Override public BufferedImage readImage( String fileName ) throws IOException {
        Assert.apology( "Can not read images from a StringFileSource") ;
        return null ;
    }
}