package tm.cpp.analysis;

import tm.clc.analysis.*;
import tm.utilities.Debug;

import junit.framework.*;
import java.util.*;

import tm.clc.analysis.AnalysisTestCase;

/**
 * takes care of behind the scenes work for lookup tests
 */
public abstract class LookupTest extends AnalysisTestCase 
	implements tm.cpp.analysis.TestConstantUser {
 
	protected Vector allNamespaces = new Vector ();

    public LookupTest (String name) { super (name); }

	protected void resetNS () { 
		for (Enumeration e = allNamespaces.elements ();
			 e.hasMoreElements (); )
			((NamespaceSH) e.nextElement ()).visited (false);
	}
}			
