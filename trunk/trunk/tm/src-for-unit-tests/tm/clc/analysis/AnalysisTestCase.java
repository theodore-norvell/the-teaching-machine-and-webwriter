package tm.clc.analysis;

import tm.utilities.Debug;

import junit.framework.*;
import java.util.*;

/**
 * takes care of behind the scenes work for analysis tests
 */
public abstract class AnalysisTestCase extends TestCase
	implements TestConstantUser {

	public static final String EX_AP = ">>EXPECTED APOLOGY : ";
    
    static Debug d = Debug.getInstance() ;
    // d.inactivate() // Comment 
 
    public AnalysisTestCase (String name) { super (name); }
}			
