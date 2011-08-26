package tm.clc.ast;

import junit.framework.*;

import tm.clc.datum.* ;
import tm.cpp.datum.* ;
import tm.cpp.ast.* ;
import tm.virtualMachine.* ;

public class OpIntTest extends Cpp_AbstractAstTest {

    public OpIntTest() { super ("OpIntTest"); }
    public OpIntTest (String name) { super (name); }


    private void check_constant( int op, TypeNode ty,
                                 long x, long y, long z ) {
        ConstInt left = new ConstInt( ty, "", x ) ;
        ConstInt right = new ConstInt( ty, "", y ) ;
        OpInt nd = new OpInt(ty, op, "", left, right) ;

        assertTrue( nd.is_integral_constant() ) ;
        assertTrue( nd.get_integral_constant_value() == z ) ;
    }

    public void test_constant_evaluation2 () {

        TypeNode lng = TyLong.get() ;
        check_constant( Arithmetic.ADD, lng, 12, 14, 26 ) ;
        check_constant( Arithmetic.ADD, lng, 12, -14, -2 ) ;
        check_constant( Arithmetic.BITWISE_AND, lng, 9, 28, 8 ) ;
        check_constant( Arithmetic.EQUAL, lng, 12, 12, 1 ) ;
        check_constant( Arithmetic.EQUAL, lng, 12, 11, 0 ) ;
        check_constant( Arithmetic.DIVIDE, lng, 17, 3, 5 ) ;
        check_constant( Arithmetic.DIVIDE, lng, -17, 3, -5 ) ;
        check_constant( Arithmetic.REMAINDER, lng, 17, 3, 2 ) ;
        check_constant( Arithmetic.REMAINDER, lng, -17, 3, -2 ) ;

    }

    private void check_dynamic( int op, TypeNode ty,
                           long x, long y, long z ) {
        ConstInt left = new ConstInt( ty, "", x ) ;
        ConstInt right = new ConstInt( ty, "", y ) ;
        OpInt nd = new OpInt(ty, op, "", left, right) ;
        ExpressionEvaluation ee = new ExpressionEvaluation( vms, nd ) ;
        vms.push( ee ) ;

        // Evaluate
        nd.select( vms ) ;
        vms.top().getSelected().step( vms ) ;
        nd.select( vms ) ;
        vms.top().getSelected().step( vms ) ;
        nd.select( vms ) ;
        vms.top().getSelected().step( vms ) ;
        assertTrue( vms.top().at( nd ) != null ) ;
        assertTrue( vms.top().at( nd ) instanceof AbstractIntDatum ) ;
        AbstractIntDatum d = (AbstractIntDatum) vms.top().at( nd ) ;
        assertTrue( d.getValue() == z ) ;
    }

    public void test_dynamic_evaluation2 () {

        TypeNode lng = TyLong.get() ;
        check_dynamic( Arithmetic.ADD, lng, 12, 14, 26 ) ;
        check_dynamic( Arithmetic.ADD, lng, 12, -14, -2 ) ;
        check_dynamic( Arithmetic.BITWISE_AND, lng, 9, 28, 8 ) ;
        check_dynamic( Arithmetic.EQUAL, lng, 12, 12, 1 ) ;
        check_dynamic( Arithmetic.EQUAL, lng, 12, 11, 0 ) ;
        check_dynamic( Arithmetic.DIVIDE, lng, 17, 3, 5 ) ;
        check_dynamic( Arithmetic.DIVIDE, lng, -17, 3, -5 ) ;
        check_dynamic( Arithmetic.REMAINDER, lng, 17, 3, 2 ) ;
        check_dynamic( Arithmetic.REMAINDER, lng, -17, 3, -2 ) ;

    }



    private void check_constant( int op, TypeNode ty,
                                 long x, long z ) {
        ConstInt operand = new ConstInt( ty, "", x ) ;
        OpInt nd = new OpInt(ty, op, "", operand) ;

        assertTrue( nd.is_integral_constant() ) ;
        assertTrue( nd.get_integral_constant_value() == z ) ;
    }

    public void test_constant_evaluation1 () {

        TypeNode lng = TyLong.get() ;
        check_constant( Arithmetic.NEGATE, lng, 12, -12 ) ;
        check_constant( Arithmetic.NEGATE, lng, -12, 12 ) ;
        check_constant( Arithmetic.BITWISE_NOT, lng, 0, -1 ) ;
        check_constant( Arithmetic.BITWISE_NOT, lng, 120, ~120 ) ;
        check_constant( Arithmetic.BOOLEAN_NOT, lng, 1, 0 ) ;
        check_constant( Arithmetic.BOOLEAN_NOT, lng, 0, 1 ) ;

    }

    private void check_dynamic( int op, TypeNode ty,
                           long x, long z ) {
        ConstInt operand = new ConstInt( ty, "", x ) ;
        OpInt nd = new OpInt(ty, op, "", operand) ;
        ExpressionEvaluation ee = new ExpressionEvaluation( vms, nd ) ;
        vms.push( ee ) ;

        // Evaluate
        nd.select( vms ) ;
        vms.top().getSelected().step( vms ) ;
        nd.select( vms ) ;
        vms.top().getSelected().step( vms ) ;

        assertTrue( vms.top().at( nd ) != null ) ;
        assertTrue( vms.top().at( nd ) instanceof AbstractIntDatum ) ;
        AbstractIntDatum d = (AbstractIntDatum) vms.top().at( nd ) ;
        assertTrue( d.getValue() == z ) ;
    }

    public void test_dynamic_evaluation () {

        TypeNode lng = TyLong.get() ;
        check_dynamic( Arithmetic.NEGATE, lng, 12, -12 ) ;
        check_dynamic( Arithmetic.NEGATE, lng, -12, 12 ) ;
        check_dynamic( Arithmetic.BITWISE_NOT, lng, 0, -1 ) ;
        check_dynamic( Arithmetic.BITWISE_NOT, lng, 120, ~120 ) ;
        check_dynamic( Arithmetic.BOOLEAN_NOT, lng, 1, 0 ) ;
        check_dynamic( Arithmetic.BOOLEAN_NOT, lng, 0, 1 ) ;

    }

}
