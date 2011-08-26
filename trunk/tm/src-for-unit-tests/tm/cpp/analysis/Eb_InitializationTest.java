package tm.cpp.analysis;

import tm.clc.analysis.*;
import tm.clc.ast.*;
import tm.cpp.ast.*;
import tm.cpp.parser.ParserConstants;
import tm.utilities.Debug;
import tm.utilities.ApologyException;

import junit.framework.*;
import java.util.*;
import tm.clc.rtSymTab.RT_Symbol_Table;
import tm.backtrack.BTTimeManager;
import tm.virtualMachine.VMState;

/**
 * Eb_Initialization tests
 */
public class Eb_InitializationTest extends ExpressionBuilderTest {
	public Eb_InitializationTest () { this ("Eb_InitializationTest"); }
	public Eb_InitializationTest (String name) { 
		super (name);
	}

	public void setUp () { 
		super.setUp ();
		if (eb == null) eb = dm.eb_initialization;
	}
	public void testZeroInit () {
		ScopedName op = Eb_Initialization.ZERO_INIT_SN;
		// scalar
		exp = applyExp (op, x);
		// we should have :
		// OpAssign (ExpId, ConstInt)
		validate (exp, OpAssign.class, 
				  new Object [] {ExpId.class}, 
				  new Object [] {ConstInt.class}, 
				  false);
		assertTrue (exp.get_type () instanceof TyRef);
		
		// arrays
		// empty array
		// int a[];  
		tyArray.setNumberOfElements (0);
		exp = applyExp (op, a);
		assertTrue (exp == null);

		// non-empty array
		// int a[2];
		tyArray.setNumberOfElements (2);
		exp = applyExp (op, a);

		// we should have
		// ExpSequence (OpAssign (OpArraySubscript (ExpId), ConstInt), 
		//               OpAssign (OpArraySubscript (ExpId), ConstInt))
		doValidate 
			(exp, 
			 new Expect	(ExpSequence.class, new Expect []
				 {new Expect (OpAssign.class, new Expect []
					 {new Expect (OpArraySubscript.class, ExpId.class), 
					  new Expect (ConstInt.class)}),
				  new Expect (OpAssign.class, new Expect []
					 {new Expect (OpArraySubscript.class, ExpId.class), 
					  new Expect (ConstInt.class)})
				 }),
			 false);

		tyArray.setNumberOfElements (0);
		// classobj
	}

	public void testDefaultInit () {
		ScopedName op = Eb_Initialization.DEFAULT_INIT_SN;
		// scalar 
		exp = applyExp (op, x);
		// we should have :
		// OpAssign (ExpId, ConstInt)
		validate (exp, OpAssign.class, 
				  new Object [] {ExpId.class}, 
				  new Object [] {ConstInt.class}, 
				  false);
		assertTrue (exp.get_type () instanceof TyRef);
		// array 
		// empty array
		// int a[];  
		tyArray.setNumberOfElements (0);
		exp = applyExp (op, a);
		assertTrue (exp == null);

		// non-empty array
		// int a[2];
		tyArray.setNumberOfElements (2);
		exp = applyExp (op, a);

		// we should have
		// ExpSequence (OpAssign (OpArraySubscript (ExpId), ConstInt), 
		//               OpAssign (OpArraySubscript (ExpId), ConstInt))
		doValidate 
			(exp, 
			 new Expect	(ExpSequence.class, new Expect []
				 {new Expect (OpAssign.class, new Expect []
					 {new Expect (OpArraySubscript.class, ExpId.class), 
					  new Expect (ConstInt.class)}),
				  new Expect (OpAssign.class, new Expect []
					 {new Expect (OpArraySubscript.class, ExpId.class), 
					  new Expect (ConstInt.class)})
				 }),
			 false);

		tyArray.setNumberOfElements (0);
		// classobj
	}

	public void testCopyInit () {
		ScopedName op = Eb_Initialization.COPY_INIT_SN;
		// scalar
		exp = applyExp (op, x, one);
		// we should have :
		// OpAssign (ExpId, ConstInt)
		validate (exp, OpAssign.class, 
				  new Object [] {ExpId.class}, 
				  new Object [] {ConstInt.class}, 
				  false);
		assertTrue (exp.get_type () instanceof TyRef);
		// array (string literal only)
		// undefined size
		exp = applyExp (op, ac, str_const);

		// big enough
		tyArrayChar.setNumberOfElements (str_const.length ());
		exp = applyExp (op, ac, str_const);

		// bigger than
		tyArrayChar.setNumberOfElements (str_const.length () + 1);
		exp = applyExp (op, ac, str_const);

		// not big enough
		tyArrayChar.setNumberOfElements (str_const.length () - 2); 
		applyBadExp (op, ac, str_const);
		
		// classobj

		// reference (all reference initialization goes through this ruleset)
		// can't initialize non-const ref with literal 
		applyBadExp (op, rint, five);
		// but a non-const var should do fine
		exp = applyExp (op, rint, x);

		// can initialize a const ref with literal - NOT YET IMPLEMENTED
		// d.msg (">>initialize const ref with literal");
		// exp = applyExp (op, crint, five);

		// and can initialize with non-const var
		d.msg (Debug.COMPILE, ">>initialize const ref with non-const var");
		exp = applyExp (op, crint, x);
	}

	public void testDirectInit () {
		ScopedName op = Eb_Initialization.DIRECT_INIT_SN;
		// scalar
		exp = applyExp (op, x, one);
		// we should have :
		// OpAssign (ExpId, ConstInt)
		validate (exp, OpAssign.class, 
				  new Object [] {ExpId.class}, 
				  new Object [] {ConstInt.class}, 
				  false);
		assertTrue (exp.get_type () instanceof TyRef);
		// classobj
	}

	public void testAggregateInit () {
		ScopedName op = Eb_Initialization.AGGREGATE_INIT_SN;
		// NOT IMPLEMENTED
		// array
		applyBadExp (op, a);
		// aggregate class
	}

	public void testNoInit () {
		ScopedName op = Eb_Initialization.NO_INIT_SN;

		// any type that can be left uninitialized
		exp = applyExp (op, x);

		// reference 
		applyBadExp (op, rint);
	}

}			

