package tm.javaLang.ast;

import tm.clc.ast.Cpp_AbstractAstTest;
import junit.framework.*;

import tm.clc.datum.* ;
import tm.clc.ast.* ;
import tm.javaLang.datum.* ;
import tm.javaLang.ast.* ;
import tm.virtualMachine.* ;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class OpBooleanTest extends Cpp_AbstractAstTest {

    public OpBooleanTest() { super ("OpBooleanTest"); }
    public OpBooleanTest (String name) { super (name); }


    private void check_constant( int op, TypeNode ty,
                                 boolean x, boolean y, boolean z ) {
        ConstBool left = new ConstBool( x ) ;
        ConstBool right = new ConstBool( y ) ;
        OpInt nd = new OpInt(ty, op, "", left, right) ;

        assertTrue( nd.is_integral_constant() ) ;
        assertTrue( nd.get_integral_constant_value() == (z?1:0) ) ;
    }

    public void test_constant_evaluation2 () {

        TypeNode tyBoolean = TyBoolean.get() ;

        check_constant( Arithmetic.BITWISE_AND, tyBoolean, false, false, false ) ;
        check_constant( Arithmetic.BITWISE_AND, tyBoolean, false, true, false ) ;
        check_constant( Arithmetic.BITWISE_AND, tyBoolean, true, false, false ) ;
        check_constant( Arithmetic.BITWISE_AND, tyBoolean, true, true, true ) ;

        check_constant( Arithmetic.BITWISE_OR, tyBoolean, false, false, false ) ;
        check_constant( Arithmetic.BITWISE_OR, tyBoolean, false, true, true ) ;
        check_constant( Arithmetic.BITWISE_OR, tyBoolean, true, false, true ) ;
        check_constant( Arithmetic.BITWISE_OR, tyBoolean, true, true, true ) ;

        check_constant( Arithmetic.BITWISE_XOR, tyBoolean, false, false, false ) ;
        check_constant( Arithmetic.BITWISE_XOR, tyBoolean, false, true, true ) ;
        check_constant( Arithmetic.BITWISE_XOR, tyBoolean, true, false, true ) ;
        check_constant( Arithmetic.BITWISE_XOR, tyBoolean, true, true, false ) ;

    }

    private void check_dynamic( int op, TypeNode ty,
                           boolean x, boolean y, boolean z ) {
        ConstBool left = new ConstBool( x ) ;
        ConstBool right = new ConstBool( y ) ;
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
        assertTrue( vms.top().at( nd ) instanceof BooleanDatum ) ;
        BooleanDatum d = (BooleanDatum) vms.top().at( nd ) ;
        assertTrue( d.getValue() == (z?1:0) ) ;
    }

    public void test_dynamic_evaluation2 () {

        TypeNode tyBoolean = TyBoolean.get() ;

        check_dynamic( Arithmetic.BITWISE_AND, tyBoolean, false, false, false ) ;
        check_dynamic( Arithmetic.BITWISE_AND, tyBoolean, false, true, false ) ;
        check_dynamic( Arithmetic.BITWISE_AND, tyBoolean, true, false, false ) ;
        check_dynamic( Arithmetic.BITWISE_AND, tyBoolean, true, true, true ) ;

        check_dynamic( Arithmetic.BITWISE_OR, tyBoolean, false, false, false ) ;
        check_dynamic( Arithmetic.BITWISE_OR, tyBoolean, false, true, true ) ;
        check_dynamic( Arithmetic.BITWISE_OR, tyBoolean, true, false, true ) ;
        check_dynamic( Arithmetic.BITWISE_OR, tyBoolean, true, true, true ) ;

        check_dynamic( Arithmetic.BITWISE_XOR, tyBoolean, false, false, false ) ;
        check_dynamic( Arithmetic.BITWISE_XOR, tyBoolean, false, true, true ) ;
        check_dynamic( Arithmetic.BITWISE_XOR, tyBoolean, true, false, true ) ;
        check_dynamic( Arithmetic.BITWISE_XOR, tyBoolean, true, true, false ) ;

    }
}