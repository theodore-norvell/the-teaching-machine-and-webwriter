package tm.clc.ast;

import junit.framework.*;

import tm.clc.analysis.ScopedName ;
import tm.clc.datum.* ;
import tm.cpp.datum.* ;
import tm.cpp.ast.* ;
import tm.cpp.analysis.Cpp_ScopedName;
import tm.virtualMachine.* ;

/** Tests for OpArraySubscript, op_address_of, OpDeref, and OpPointerSubscript.
*/
public class OpPointerTest2 extends Cpp_AbstractAstTest {

    public OpPointerTest2() { this ("OpPointerTest2"); }
    public OpPointerTest2 (String name) { super (name); print=true;}


    public void test_arrays_etc() {
        // Construct some types
        TyInt tyInt = TyInt.get() ;

        TyArray tyA3Int= new TyArray() ;
        tyA3Int.addToEnd(tyInt) ;
        tyA3Int.setNumberOfElements(3) ;

        TyPointer tyPtrInt = new TyPointer() ;
        tyPtrInt.addToEnd(tyInt) ;

        TyRef tyRefInt = new TyRef( tyInt ) ;

        TyRef tyRefA3Int = new TyRef( tyA3Int ) ;

        // Create a stack variable "a" of type int[3]


        // Make a datum of type int[3]
        ArrayDatum a = (ArrayDatum)
            tyA3Int.makeDatum(vms, vms.getStore().getStack(), "a");
        ScopedName name_a = new Cpp_ScopedName("a") ;
        symtab.newVar( name_a, "a", a ) ;


        // Let's lookup item 2 of array
        ExpId id = new ExpId(tyRefA3Int, "a", name_a) ;
        ConstInt two = new ConstInt(tyInt, "2", 2 ) ;
        OpArraySubscript lookup
            = new OpArraySubscript(tyRefInt, "[", "]", id, two ) ;

        {
            ExpressionEvaluation ee = new ExpressionEvaluation( vms, lookup ) ;
            vms.push( ee ) ;

            advance( 1000 ) ;

            Object result0 = ee.at( lookup ) ;
            assertTrue( result0 instanceof RefDatum ) ;
            RefDatum result1 = (RefDatum) result0 ;
            int addr0 = a.getAddress() ;
            assertTrue( result1.getValue() == addr0+8 ) ;
        }

        // Let's take the address of the result and dereference that.
        // * & a[2] ;

        OpAddressOf addrof
            = new OpAddressOf(tyPtrInt, "&", lookup ) ;
        OpDeref deref
            = new OpDeref(tyRefInt, "*", addrof ) ;

        {
            ExpressionEvaluation ee
                = new ExpressionEvaluation( vms, deref ) ;
            vms.push( ee ) ;

            advance( 1000 ) ;

            Object result0 = ee.at( lookup ) ;
            assertTrue( result0 instanceof RefDatum ) ;
            RefDatum result1 = (RefDatum) result0 ;
            int addr0 = a.getAddress() ;
            assertTrue( result1.getValue() == addr0+8 ) ;

            Object result2 = ee.at( addrof ) ;
            assertTrue( result2 instanceof PointerDatum ) ;
            PointerDatum result3 = (PointerDatum) result2 ;
            assertTrue( result3.getValue() == addr0+8 ) ;

            Object result4 = ee.at( deref ) ;
            assertTrue( result4 instanceof RefDatum ) ;
            RefDatum result5 = (RefDatum) result4 ;
            assertTrue( result5.getValue() == addr0+8 ) ;
        }

        // This time we form (&a[2])[-1]

        ConstInt minusOne = new ConstInt(tyInt, "-1", -1) ;
        OpPointerSubscript ptr_subscript
            = new OpPointerSubscript(tyRefInt, "[", "]", addrof, minusOne) ;
        {
            ExpressionEvaluation ee
                = new ExpressionEvaluation( vms, ptr_subscript ) ;
            vms.push( ee ) ;

            advance( 1000 ) ;

            Object result0 = ee.at( ptr_subscript ) ;
            assertTrue( result0 instanceof RefDatum ) ;
            RefDatum result1 = (RefDatum) result0 ;
            int addr0 = a.getAddress() ;
            assertTrue( result1.getValue() == addr0+4 ) ;
        }


    }
}