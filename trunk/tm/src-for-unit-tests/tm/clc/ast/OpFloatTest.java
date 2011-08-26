package tm.clc.ast;

import junit.framework.*;

import tm.clc.datum.* ;
import tm.cpp.datum.* ;
import tm.cpp.ast.* ;
import tm.virtualMachine.* ;

public class OpFloatTest extends Cpp_AbstractAstTest {

    public OpFloatTest() { super ("OpFloatTest"); }
    public OpFloatTest (String name) { super (name); }


    private void check_constant( int op, TypeNode ty,
                                 double x, double y, double z ) {
        ConstFloat left = new ConstFloat( ty, "", x ) ;
        ConstFloat right = new ConstFloat( ty, "", y ) ;
        OpFloat nd = new OpFloat(ty, op, "", left, right) ;

        assertTrue( ! nd.is_integral_constant() ) ;
    }

    public void test_constant_evaluation2 () {

        TypeNode dbl = TyDouble.get() ;
        check_constant( Arithmetic.ADD, dbl, 12.5, 14.6, 12.5+14.6) ;
        check_constant( Arithmetic.ADD, dbl, 12.5, -14.6, 12.5-14.6 ) ;
        check_constant( Arithmetic.EQUAL, dbl, 12.5, 12.5, 1 ) ;
        check_constant( Arithmetic.EQUAL, dbl, 12.3, 12.4, 0 ) ;
        check_constant( Arithmetic.DIVIDE, dbl, 17.0, 3.0, 17.0/3.0 ) ;
        check_constant( Arithmetic.DIVIDE, dbl, -17.0, 3, (-17.0)/3.0 ) ;
    }

    private void check_dynamic( int op, TypeNode ty,
                           double x, double y, double z ) {
        ConstFloat left = new ConstFloat( ty, "", x ) ;
        ConstFloat right = new ConstFloat( ty, "", y ) ;
        OpFloat nd = new OpFloat(ty, op, "", left, right) ;
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
        assertTrue( vms.top().at( nd ) instanceof AbstractFloatDatum ) ;
        AbstractFloatDatum d = (AbstractFloatDatum) vms.top().at( nd ) ;
        assertTrue( d.getValue() == z ) ;
    }

    private void check_dynamic_int_result( int op, TypeNode ty,
                           double x, double y, int z ) {
        ConstFloat left = new ConstFloat( ty, "", x ) ;
        ConstFloat right = new ConstFloat( ty, "", y ) ;
        OpFloat nd = new OpFloat(TyInt.get(), op, "", left, right) ;
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

        TypeNode dbl = TyDouble.get() ;
        check_dynamic( Arithmetic.ADD, dbl, 12.5, 14.6, 12.5+14.6) ;
        check_dynamic( Arithmetic.ADD, dbl, 12.5, -14.6, 12.5-14.6 ) ;
        check_dynamic_int_result( Arithmetic.EQUAL, dbl, 12.5, 12.5, 1 ) ;
        check_dynamic_int_result( Arithmetic.EQUAL, dbl, 12.3, 12.4, 0 ) ;
        check_dynamic( Arithmetic.DIVIDE, dbl, 17.0, 3.0, 17.0/3.0 ) ;
        check_dynamic( Arithmetic.DIVIDE, dbl, -17.0, 3, (-17.0)/3.0 ) ;

    }



    private void check_constant( int op, TypeNode ty,
                                 double x, double z ) {
        ConstFloat operand = new ConstFloat( ty, "", x ) ;
        OpFloat nd = new OpFloat(ty, op, "", operand) ;

        assertTrue( ! nd.is_integral_constant() ) ;
    }

    public void test_constant_evaluation1 () {

        TypeNode dbl = TyDouble.get() ;
        check_constant( Arithmetic.NEGATE, dbl, 12, -12 ) ;

    }

    private void check_dynamic( int op, TypeNode ty,
                           double x, double z ) {
        ConstFloat operand = new ConstFloat( ty, "", x ) ;
        OpFloat nd = new OpFloat(ty, op, "", operand) ;
        ExpressionEvaluation ee = new ExpressionEvaluation( vms, nd ) ;
        vms.push( ee ) ;

        // Evaluate
        nd.select( vms ) ;
        vms.top().getSelected().step( vms ) ;
        nd.select( vms ) ;
        vms.top().getSelected().step( vms ) ;

        assertTrue( vms.top().at( nd ) != null ) ;
        assertTrue( vms.top().at( nd ) instanceof AbstractFloatDatum ) ;
        AbstractFloatDatum d = (AbstractFloatDatum) vms.top().at( nd ) ;
        assertTrue( d.getValue() == z ) ;
    }

    public void test_dynamic_evaluation () {

        TypeNode dbl = TyDouble.get() ;
        check_dynamic( Arithmetic.NEGATE, dbl, 12.5, -12.5 ) ;
        check_dynamic( Arithmetic.NEGATE, dbl, 0.0, 0.0 ) ;

    }

}
