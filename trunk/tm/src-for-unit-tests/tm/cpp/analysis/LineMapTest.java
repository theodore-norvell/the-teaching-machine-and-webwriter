package tm.cpp.analysis;

import tm.clc.analysis.*;
import tm.cpp.parser.*;
import tm.interfaces.SourceCoords;
import tm.utilities.*;

import junit.framework.*;
import java.util.*;

import tm.clc.analysis.AnalysisTestCase;

/**
 * tests the line map utility class 
 */
public class LineMapTest extends AnalysisTestCase 
    implements tm.cpp.analysis.TestConstantUser, ParserConstants {
 
    public LineMapTest (String name) { super (name); }
    public LineMapTest () { super ("LineMap test"); }

    public void testLine_map () {
        FileSource fileSource = new tm.utilities.StringFileSource() ;
        TMFile file_cpp = new TMFile( fileSource, "file.cpp" ) ;
        TMFile header_h = new TMFile( fileSource, "header.h" ) ;
        LineMap lm = new LineMap (file_cpp);
        int result = -1;

        assertTrue (lm.getCoords(2).getLineNumber() == 2);

        lm.add (5, 10, header_h);

        //assertTrue (lm.sourceLine (0) == 0)
        assertTrue( lm.getCoords(1).getFile() == header_h );
        assertTrue (lm.getCoords (1).getLineNumber() == 1);
        assertTrue (lm.getCoords (2).getLineNumber() == 2);
        assertTrue (lm.getCoords (3).getLineNumber() == 3);
        assertTrue (lm.getCoords (4).getLineNumber() == 4);
        assertTrue (lm.getCoords (6).getLineNumber() == 10);
        assertTrue (lm.getCoords (7).getLineNumber() == 11);
        assertTrue (lm.getCoords (8).getLineNumber() == 12);
        assertTrue( lm.getCoords(8).getFile() == header_h );


        TMFile source_cpp = new TMFile( fileSource, "source.cpp" ) ;
        lm.add (8, 14, source_cpp );

        //assertTrue (lm.getCoords (0) == 0);
        
        assertTrue( lm.getCoords(1).getFile() == header_h );
        assertTrue (lm.getCoords (1).getLineNumber() == 1);
        assertTrue (lm.getCoords (2).getLineNumber() == 2);
        assertTrue (lm.getCoords (3).getLineNumber() == 3);
        assertTrue (lm.getCoords (4).getLineNumber() == 4);
        assertTrue (lm.getCoords (6).getLineNumber() == 10);
        assertTrue( lm.getCoords(7).getFile() == header_h );
        assertTrue (lm.getCoords (7).getLineNumber() == 11);
        assertTrue( lm.getCoords(9).getFile() == source_cpp );
        assertTrue (lm.getCoords (9).getLineNumber() == 14);
        assertTrue (lm.getCoords (10).getLineNumber() == 15);
        assertTrue( lm.getCoords(11).getFile() == source_cpp );
        assertTrue (lm.getCoords (11).getLineNumber() == 16);
        
        // There was a test here where the token line number
        // decreased.  There is no need to support this case.
    }

}			
