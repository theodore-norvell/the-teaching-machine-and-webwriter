package tm.clc.ast;

import junit.framework.*;

import tm.clc.datum.* ;
import tm.cpp.datum.* ;
import tm.cpp.ast.* ;
import tm.virtualMachine.* ;

public class OpParenthesesTest extends Cpp_AbstractAstTest {

    public OpParenthesesTest() { super ("OpParenthesesTest"); }
    public OpParenthesesTest (String name) { super (name); }


    private void check_constant( int op, TypeNode ty,
                                 long x, long y, long z ) {

    }

    public void test_evaluation () {

        // Build the tree
            TypeNode lng = TyLong.get() ;
            ConstInt operand = new ConstInt( lng, "", 42 ) ;
            OpParentheses nd = new OpParentheses(lng, "(", ")", operand) ;

        // Check constant evaluation
            assertTrue( nd.is_integral_constant() ) ;
            assertTrue( nd.get_integral_constant_value() == 42 ) ;


        // Build and push an evaluation
            ExpressionEvaluation ee = new ExpressionEvaluation( vms, nd ) ;
            vms.push( ee ) ;

        // Evaluate
            nd.select( vms ) ;
            vms.top().getSelected().step( vms ) ;
            nd.select( vms ) ;
            vms.top().getSelected().step( vms ) ;

        // Check
            assertTrue( vms.top().at( nd ) != null ) ;
            assertTrue( vms.top().at( nd ) instanceof AbstractIntDatum ) ;
            AbstractIntDatum d = (AbstractIntDatum) vms.top().at( nd ) ;
            assertTrue( d.getValue() == 42 ) ;
    }
}
