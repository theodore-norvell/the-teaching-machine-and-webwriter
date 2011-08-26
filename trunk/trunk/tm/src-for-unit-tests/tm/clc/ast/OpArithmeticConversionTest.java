package tm.clc.ast;

import junit.framework.*;

import tm.clc.datum.* ;
import tm.cpp.datum.* ;
import tm.cpp.ast.* ;
import tm.virtualMachine.* ;

public class OpArithmeticConversionTest extends Cpp_AbstractAstTest {

    public OpArithmeticConversionTest() { super ("OpArithmeticConversionTest"); }
    public OpArithmeticConversionTest (String name) { super (name); }


    private AbstractDatum check_dynamic( ExpressionNode exp,
                                TypeNode ty ) {
        OpArithmeticConversion nd = new OpArithmeticConversion( ty, "", exp ) ;
        ExpressionEvaluation ee = new ExpressionEvaluation( vms, nd ) ;
        vms.push( ee ) ;

        // Evaluate
        nd.select( vms ) ;
        vms.top().getSelected().step( vms ) ;
        nd.select( vms ) ;
        vms.top().getSelected().step( vms ) ;
        assertTrue( vms.top().at( nd ) != null ) ;
        AbstractDatum d = (AbstractDatum) vms.top().at( nd ) ;
        return d ;
    }

    public void test_arithmetic_conversion () {

        TypeNode lng = TyLong.get() ;
        TypeNode in = TyInt.get() ;
        TyPointer ptr = new TyPointer() ;
        ptr.addToEnd( in ) ;
        TypeNode flt = TyFloat.get() ;
        AbstractDatum d ;

        // Pointer to int
        ConstPtr ptr_nd = new ConstPtr( ptr, "", 123 ) ;
        d = check_dynamic( ptr_nd, in ) ;
        assertTrue( d instanceof IntDatum ) ;
        assertTrue( ((IntDatum)d).getValue() == 123 ) ;

        // Int to pointer
        ConstInt int_nd = new ConstInt( in, "", 0 ) ;
        d = check_dynamic( int_nd, ptr ) ;
        assertTrue( d instanceof PointerDatum ) ;
        assertTrue( ((PointerDatum)d).getValue() == 0 ) ;


        // Int to float
        int_nd = new ConstInt( in, "", 123 ) ;
        d = check_dynamic( int_nd, flt ) ;
        assertTrue( d instanceof FloatDatum ) ;
        assertTrue( ((FloatDatum)d).getValue() == 123.0 ) ;


        // Float to int
        ConstFloat flt_nd = new ConstFloat( flt, "", 123.8 ) ;
        d = check_dynamic( flt_nd, in ) ;
        assertTrue( d instanceof IntDatum ) ;
        assertTrue( ((IntDatum)d).getValue() == 123 ) ;
    }
}
