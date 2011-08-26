package tm.clc.ast;

import junit.framework.*;

import tm.clc.datum.* ;
import tm.cpp.datum.* ;
import tm.cpp.ast.* ;
import tm.virtualMachine.* ;

public class OpLogicalTest extends Cpp_AbstractAstTest {

    public OpLogicalTest() { this ("OpLogicalTest");}
    public OpLogicalTest (String name) { super (name); print=true; }

    private void check_dynamic( int op, TypeNode ty,
                           long x, long y, long z ) {
        ConstInt left = new ConstInt( ty, x!=0?"true":"false", x ) ;
        ConstInt right = new ConstInt( ty, y!=0?"true":"false", y ) ;
        String image = op==Arithmetic.BOOLEAN_AND?"&&":"||" ;
        OpLogical nd = new OpLogical(ty, op, image, left, right) ;
        ExpressionEvaluation ee = new ExpressionEvaluation( vms, nd ) ;
        vms.push( ee ) ;

        // Evaluate
        advance(100);

        assertTrue( ee.at( nd ) != null ) ;
        assertTrue( ee.at( nd ) instanceof AbstractIntDatum ) ;
        AbstractIntDatum d = (AbstractIntDatum) ee.at( nd ) ;
        assertTrue( d.getValue() == z ) ;

        // Check shortcutting.
        boolean cutOff = op==Arithmetic.BOOLEAN_AND?x==0:x!=0 ;
        assertTrue( cutOff == ( ee.at( right ) == null ) ) ;
    }

    public void test_dynamic_evaluation () {

        TypeNode boolTy = TyBool.get() ;
        check_dynamic( Arithmetic.BOOLEAN_AND, boolTy, 0, 0, 0 ) ;
        check_dynamic( Arithmetic.BOOLEAN_AND, boolTy, 0, 1, 0 ) ;
        check_dynamic( Arithmetic.BOOLEAN_AND, boolTy, 1, 0, 0 ) ;
        check_dynamic( Arithmetic.BOOLEAN_AND, boolTy, 1, 1, 1 ) ;

        check_dynamic( Arithmetic.BOOLEAN_OR, boolTy, 0, 0, 0 ) ;
        check_dynamic( Arithmetic.BOOLEAN_OR, boolTy, 0, 1, 1 ) ;
        check_dynamic( Arithmetic.BOOLEAN_OR, boolTy, 1, 0, 1 ) ;
        check_dynamic( Arithmetic.BOOLEAN_OR, boolTy, 1, 1, 1 ) ;

    }

}
