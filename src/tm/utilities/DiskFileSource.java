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
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.FileNotFoundException;
import java.io.Writer;

import javax.imageio.ImageIO;

public class DiskFileSource extends FileSource {
    protected File rootDirectory ;

    public DiskFileSource( File root ) {
        rootDirectory = root ; }

    Reader fileNameToReader(String fileName)
    throws FileNotFoundException {
        File file = new File( rootDirectory, fileName ) ;
        return new FileReader( file ) ;
    }
    
    @Override
    void writeFile( String fileName, String str ) throws IOException {
        File file = new File( rootDirectory, fileName ) ;
        Writer writer = new FileWriter( file ) ;
        writer.append( str ) ;
        writer.close() ; }
    
    @Override public BufferedImage readImage( String fileName ) throws IOException  {
        return ImageIO.read( new File( rootDirectory, fileName ) ) ;
    }
}