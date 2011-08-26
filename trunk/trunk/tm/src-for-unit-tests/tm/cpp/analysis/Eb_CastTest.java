package tm.cpp.analysis;

import tm.clc.analysis.*;
import tm.clc.ast.*;
import tm.cpp.ast.*;
import tm.cpp.parser.ParserConstants;
import tm.utilities.Debug;

import junit.framework.*;
import java.util.*;

/**
 * Eb_Cast tests
 */
public class Eb_CastTest extends ExpressionBuilderTest {

	public Eb_CastTest () { this ("Eb_CastTest"); }
	public Eb_CastTest (String name) { 
		super (name); 
		eb = cem.eb_Cast;
	}
}			

