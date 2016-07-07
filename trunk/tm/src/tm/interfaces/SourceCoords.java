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

package tm.interfaces;

import tm.portableDisplays.SuperSourceCoords;
import tm.utilities.StringFileSource;
import tm.utilities.TMFile;

/** Represents a line number within a file.
 */

public class SourceCoords implements SuperSourceCoords{

    private TMFile file ;

    private int lineNumber ;

    public static final SourceCoords UNKNOWN ;
    static {
        StringFileSource fs = new StringFileSource() ;
        fs.addString( "unknown", "\n" ) ;
        TMFile file = new TMFile( fs, "unknown" ) ;
        UNKNOWN = new SourceCoords( file, 0 ) ; }

    public SourceCoords( TMFile file, int ln ) {
        this.file = file ;
        this.lineNumber = ln ; }

    /** The TMFile. May be hashed or tested for equality. */
    public TMFile getFile() { return file ; }

    /** Line number counting from 1. */
    public int getLineNumber() { return lineNumber ; }

    public String toString() {
        return file.getFileName() + ": " + lineNumber ; }

    public boolean equals( Object other ) {
        if( ! (other instanceof SourceCoords ) ) return false ;
        SourceCoords otherSourceCoords = (SourceCoords) other ;
        return file.equals( otherSourceCoords.file )
            && lineNumber == otherSourceCoords.lineNumber ; }
}