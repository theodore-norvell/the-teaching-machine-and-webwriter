package tm.cpp.analysis;

import tm.clc.analysis.*;
import tm.clc.ast.*;
import tm.cpp.ast.*;
import tm.utilities.Debug;

import junit.framework.*;
import java.util.*;

/**
 * Eb_Unary tests
 */
public class Eb_UnaryTest extends ExpressionBuilderTest {

        public Eb_UnaryTest () { this ("Eb_UnaryTest"); }
        public Eb_UnaryTest (String name) {
                super (name);
                eb = cem.eb_Unary;
        }


        public void testNew () {

        }

        public void testDelete () {
        }


        public void testIncrementDecrement () {
                exp = cem.make_unary_op (PLUSPLUS, x);
                exp = cem.make_unary_op (MINUSMINUS, x);
        }

        public void testSizeof () {
                // right now logic goes through the motions but ultimately builds
                // an exp_unimplemented.
                exp = applyExp (new Cpp_ScopedName  (OpTable.get (SIZEOF)), x);
        }
}



