package tm.clc.ast;

import junit.framework.*;

import tm.clc.datum.* ;
import tm.cpp.datum.* ;
import tm.cpp.ast.* ;
import tm.virtualMachine.* ;

public class OpIfThenElseTest extends Cpp_AbstractAstTest {

    public OpIfThenElseTest() { this ("OpIfThenElseTest");}
    public OpIfThenElseTest (String name) { super (name); print=true; }

    private void check_dynamic( TypeNode ty,
                           long w, long x, long y, long z ) {
        ConstInt zero = new ConstInt( ty, ""+w, w ) ;
        ConstInt one = new ConstInt( ty, ""+x, x ) ;
        ConstInt two = new ConstInt( ty, ""+z, y ) ;
        OpIfThenElse nd = new OpIfThenElse(ty, "?", ":", zero, one, two) ;
        ExpressionEvaluation ee = new ExpressionEvaluation( vms, nd ) ;
        vms.push( ee ) ;

        // Evaluate
        advance(100);

        assertTrue( ee.at( nd ) != null ) ;
        assertTrue( ee.at( nd ) instanceof AbstractIntDatum ) ;
        AbstractIntDatum d = (AbstractIntDatum) ee.at( nd ) ;
        assertTrue( d.getValue() == z ) ;

        // Check shortcutting.
        int nextChild = w!=0 ? 1 : 2 ;
        assertTrue( ee.at( nd.child_exp(nextChild) ) == ee.at( nd ) ) ;
        assertTrue( ee.at( nd.child_exp(3-nextChild) ) == null ) ;
    }

    public void test_dynamic_evaluation () {

        TypeNode tyInt = TyInt.get() ;
        check_dynamic( tyInt, 0, 1, 2, 2 ) ;
        check_dynamic( tyInt, 1, 1, 2, 1 ) ;
    }
}
