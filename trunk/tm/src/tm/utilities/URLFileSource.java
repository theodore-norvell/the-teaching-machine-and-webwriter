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
import java.net.URL;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.io.IOException;

import javax.imageio.ImageIO;

public class URLFileSource extends FileSource {
    protected URL base ;

    public URLFileSource( URL base ) {
        this.base = base ; }

    Reader fileNameToReader(String fileName)
    throws IOException {
        URL url = null;
        try {
            url = new URL(base, fileName);
        }
        catch (MalformedURLException ex) {
            throw new IOException( "Malformed URL " + ex.getMessage() ) ;
        }
        InputStream inStream = url.openStream() ;
        return new InputStreamReader( inStream ) ;
    }
    
    @Override public BufferedImage readImage( String fileName ) throws IOException {
        URL url = null;
        try {
            url = new URL(base, fileName); }
        catch (MalformedURLException ex) {
            throw new IOException( "Malformed URL " + ex.getMessage() ) ; }
        return ImageIO.read( url ) ;
    }
}