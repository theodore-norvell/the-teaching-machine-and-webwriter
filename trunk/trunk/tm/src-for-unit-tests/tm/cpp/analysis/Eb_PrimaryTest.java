package tm.cpp.analysis;

import tm.clc.analysis.*;
import tm.clc.ast.*;
import tm.cpp.ast.*;
import tm.utilities.Debug;

import junit.framework.*;
import java.util.*;

/**
 * Eb_Primary tests
 */
public class Eb_PrimaryTest extends ExpressionBuilderTest {

	public Eb_PrimaryTest () { this ("Eb_PrimaryTest"); }
	public Eb_PrimaryTest (String name) { 
		super (name); 
		eb = cem.eb_Primary;
	}

	public void testIdExpression () {
		// scalar var
		exp = cem.make_id_exp (x_sn);
		// expecting ExpId
		doValidate (exp, new Expect (ExpId.class), false);
		doValidate (exp, new Expect (new TyRef (tyInt)), true);

		// reference var
		exp = cem.make_id_exp (r_sn);

		// expecting ExpFetch (ExpId)
		doValidate (exp, new Expect (ExpFetch.class, ExpId.class), false);
		doValidate (exp, new Expect (new TyRef (tyInt)), true);


	}

	public void testThisExpression () {
	}
}			

