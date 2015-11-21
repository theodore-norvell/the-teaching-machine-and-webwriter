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
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.imageio.ImageIO;

public class LocalResourceFileSource extends FileSource {

    private String prefix ;
    private String suffix ;
    private Class resourceGetterClass ;

    public LocalResourceFileSource( Class resourceGetterClass, String prefix, String suffix ) {
        this.prefix = prefix ;
        this.suffix = suffix ;
        this.resourceGetterClass = resourceGetterClass ;
        //System.out.println("Construcing LocalResourceFileSource with") ;
        //System.out.println("   prefix = " + prefix ) ;
        //System.out.println("   suffix = " + suffix ) ;
        //System.out.println("   resourceGetterClass = " + resourceGetterClass ) ;
   }

    Reader fileNameToReader(String fileName) throws IOException {
        fileName = prefix+fileName+suffix ;
        //System.out.println("fileNameToReader on " + fileName );
        InputStream is = resourceGetterClass.getResourceAsStream(fileName);
        if( is == null ) {
            throw new IOException( "No resource named "+fileName ) ; }
        //System.out.println("success");
        return new InputStreamReader( is ) ;
    }
    
    @Override public BufferedImage readImage( String fileName ) throws IOException  {
        URL url =  resourceGetterClass.getResource( fileName ) ;
        return ImageIO.read( url ) ;
    }
}