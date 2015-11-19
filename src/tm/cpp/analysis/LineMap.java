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

package tm.cpp.analysis;

import java.util.Vector;

import tm.interfaces.SourceCoords;
import tm.utilities.Assert;
import tm.utilities.TMFile;


/**
 * A utility class which provides a mapping between a Token's beginLine value and its
 * position within the original source file.
 * @author Derek Reilly
 */

public class LineMap {
    protected Vector map = new Vector ();

    LineMap( TMFile file ) {
        // Add a sentinel to mark the beginning of the map.
        add( 0, 0, file ) ;
    }

    /**
     * Add line directive information to the map. This method makes no
     * assumptions as to the order in which the line directives are added.
     * @param tokenLineNumber The line number that the parser sees.
     * @param sourceLineNumber The line number in the source file
     * @param sourceFileName The name of the source file
     */
    public void add (int tokenLineNumber, int sourceLineNumber, TMFile file)  {
        map.addElement (new IntPair( tokenLineNumber, sourceLineNumber, file) );
    }

    /**
     * Given any Token line number, returns the corresponding source coordinates.
     * @param tline the line number
     * @return the corresponding source coordinates.
     */
    public SourceCoords getCoords( int tLine ) {
        int i = map.size() - 1;
        IntPair p = null ;
        while ( true ) {
            Assert.check( i >= 0 ) ;
            p = (IntPair)map.elementAt(i) ;
            if( p.tLine <= tLine ) break ;
            --i ; }
        return new SourceCoords( p.file, p.sLine + (tLine-p.tLine)  ); }

    /*
     * Class representing a pair of integers and a file name.
     */
    class IntPair {
        int tLine = -1;
        int sLine = -1;
        TMFile file ;

        IntPair (int v1, int v2, TMFile fn) {
            tLine = v1;
            sLine = v2;
            file = fn ;
        }
    }
}