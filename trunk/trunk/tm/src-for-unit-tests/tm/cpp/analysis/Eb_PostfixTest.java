package tm.cpp.analysis;

import tm.clc.analysis.*;
import tm.clc.ast.*;
import tm.cpp.ast.*;
import tm.cpp.parser.ParserConstants;
import tm.utilities.Debug;

import junit.framework.*;
import java.util.*;

/**
 * Eb_Postfix tests
 */
public class Eb_PostfixTest extends ExpressionBuilderTest {

        public Eb_PostfixTest () { this ("Eb_PostfixTest"); }
        public Eb_PostfixTest (String name) {
                super (name);
                eb = cem.eb_Postfix;
        }

        public void testFunctionCall () {

                // function call expression, calling foo
                exp = cem.make_function_call_exp (new ExpFunctionName (foo_sn),
                                                                                  new NodeList ());

        }

        public void testMemberAccess () {
                // accessing c.y
                ExpressionNode c_id_exp = cem.make_id_exp (c_sn);
                assertTrue (c_id_exp != null);
                exp = cem.make_member_exp (c_id_exp, y_sn);

                // accessing cp->y
                ExpressionNode pc_id_exp = cem.make_id_exp (pc_sn);
                exp = cem.make_arrow_exp (pc_id_exp, y_sn);
        }

        public void testIncrementDecrement () {
                exp = cem.make_postfix_exp (PLUSPLUS, x);
                exp = cem.make_postfix_exp (MINUSMINUS, x);
        }

        public void testTypeid () {
                // not yet implemented
                applyBadExp (new Cpp_ScopedName (OpTable.get (TYPEID)), x);
        }

}

