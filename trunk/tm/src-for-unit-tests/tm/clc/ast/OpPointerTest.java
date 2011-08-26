package tm.clc.ast;

import junit.framework.*;

import tm.clc.datum.* ;
import tm.cpp.datum.* ;
import tm.cpp.ast.* ;
import tm.virtualMachine.* ;

public class OpPointerTest extends Cpp_AbstractAstTest {

    public OpPointerTest() { super ("OpPointerTest"); }
    public OpPointerTest (String name) { super (name); }


    private void check_dynamic_ptr_result( int op, TyPointer ty,
                           int x, int y, int z ) {
        ConstPtr left = new ConstPtr( ty, "", x ) ;
        ConstInt right = new ConstInt( TyInt.get(), "", y ) ;
        OpPointer nd = new OpPointer(ty, op, "", left, right) ;
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
        assertTrue( vms.top().at( nd ) instanceof AbstractPointerDatum ) ;
        AbstractPointerDatum d = (AbstractPointerDatum) vms.top().at( nd ) ;
        assertTrue( d.getValue() == z ) ;
    }
    private void check_dynamic_int_result( int op, TypeNode ptr, TypeNode tyint,
                           int x, int y, int z ) {
        ConstPtr left = new ConstPtr( ptr, "", x ) ;
        ConstPtr right = new ConstPtr( ptr, "", y ) ;
        OpPointer nd = new OpPointer(tyint, op, "", left, right) ;
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

        TypeNode tyint = TyInt.get() ;
        TyPointer ptr = new TyPointer() ;
        ptr.addToEnd(tyint) ;
        check_dynamic_ptr_result( Arithmetic.ADD_POINTER_INT, ptr, 20, 3, 32) ;
        check_dynamic_ptr_result( Arithmetic.SUBTRACT_POINTER_INT, ptr, 20, 3, 8) ;

        check_dynamic_int_result( Arithmetic.SUBTRACT_POINTER_POINTER, ptr, tyint, 20, 8, 3) ;
        check_dynamic_int_result( Arithmetic.EQUAL, ptr, tyint, 20, 20, 1) ;
        check_dynamic_int_result( Arithmetic.EQUAL, ptr, tyint, 20, 16, 0) ;
        check_dynamic_int_result( Arithmetic.NOT_EQUAL, ptr, tyint, 20, 20, 0) ;
        check_dynamic_int_result( Arithmetic.NOT_EQUAL, ptr, tyint, 20, 16, 1) ;
        check_dynamic_int_result( Arithmetic.LESS, ptr, tyint, 20, 20, 0) ;
        check_dynamic_int_result( Arithmetic.LESS, ptr, tyint, 20, 16, 0) ;
        check_dynamic_int_result( Arithmetic.LESS, ptr, tyint, 20, 24, 1) ;
        check_dynamic_int_result( Arithmetic.GREATER, ptr, tyint, 20, 20, 0) ;
        check_dynamic_int_result( Arithmetic.GREATER, ptr, tyint, 20, 16, 1) ;
        check_dynamic_int_result( Arithmetic.GREATER, ptr, tyint, 20, 24, 0) ;
        check_dynamic_int_result( Arithmetic.LESS_OR_EQUAL, ptr, tyint, 20, 20, 1) ;
        check_dynamic_int_result( Arithmetic.LESS_OR_EQUAL, ptr, tyint, 20, 16, 0) ;
        check_dynamic_int_result( Arithmetic.LESS_OR_EQUAL, ptr, tyint, 20, 24, 1) ;
        check_dynamic_int_result( Arithmetic.GREATER_OR_EQUAL, ptr, tyint, 20, 20, 1) ;
        check_dynamic_int_result( Arithmetic.GREATER_OR_EQUAL, ptr, tyint, 20, 16, 1) ;
        check_dynamic_int_result( Arithmetic.GREATER_OR_EQUAL, ptr, tyint, 20, 24, 0) ;
    }
}
