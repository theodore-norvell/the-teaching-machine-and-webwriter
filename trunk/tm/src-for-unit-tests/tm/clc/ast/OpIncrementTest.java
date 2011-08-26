package tm.clc.ast;

import junit.framework.*;

import tm.clc.analysis.ScopedName ;
import tm.clc.datum.* ;
import tm.cpp.datum.* ;
import tm.cpp.ast.* ;
import tm.cpp.analysis.Cpp_ScopedName;
import tm.virtualMachine.* ;

public class OpIncrementTest extends Cpp_AbstractAstTest {

    public OpIncrementTest() { this ("OpIncrementTest"); }
    public OpIncrementTest (String name) { super (name); }

    void check_int(boolean pre, boolean up) {
        // Add variable to store
        // Make a datum of type C
        TypeNode ty = TyInt.get() ;
        TyRef tyRef = new TyRef( ty ) ;

        AbstractIntDatum d = (AbstractIntDatum)
            ty.makeDatum(vms, vms.getStore().getStack(), "q");
        d.putValue(100);
        ScopedName name_q = new Cpp_ScopedName("q") ;
        symtab.newVar( name_q, "q", d ) ;

        ExpressionNode id = new ExpId(tyRef, "q", name_q) ;
        String name = up ? "++" : "--" ;
        ExpressionNode nd = new OpIncrement(ty, pre, up, name, id) ;

        // Make Evaluation and go.
        ExpressionEvaluation ee = new ExpressionEvaluation( vms, nd ) ;
        vms.push( ee ) ;
        advance(100) ;

        assertTrue( d.getValue() == 100 + (up?1:-1) ) ;
        assertTrue( ee.at( nd ) != null ) ;
        Object r = ee.at( nd ) ;
        assertTrue( r instanceof AbstractIntDatum ) ;
        AbstractIntDatum ir = (AbstractIntDatum) r ;
        if( pre ) assertTrue( r == d ) ;
        else assertTrue( ir.getValue() == 100 ) ;
    }

    public void test_int() {
        check_int(true, true) ;
        check_int(true, false) ;
        check_int(false, true) ;
        check_int(false, false) ;
    }

    void check_float(boolean pre, boolean up) {
        TypeNode ty = TyDouble.get() ;
        TyRef tyRef = new TyRef( ty ) ;

        AbstractFloatDatum d = (AbstractFloatDatum)
            ty.makeDatum(vms, vms.getStore().getStack(), "q");
        d.putValue(100.5);
        ScopedName name_q = new Cpp_ScopedName("q") ;
        symtab.newVar( name_q, "q", d ) ;

        ExpressionNode id = new ExpId(tyRef, "q", name_q) ;
        String name = up ? "++" : "--" ;
        ExpressionNode nd = new OpIncrement(ty, pre, up, name, id) ;

        // Make Evaluation and go.
        ExpressionEvaluation ee = new ExpressionEvaluation( vms, nd ) ;
        vms.push( ee ) ;
        advance(100) ;

        assertTrue( d.getValue() == 100.5 + (up?1:-1) ) ;
        assertTrue( ee.at( nd ) != null ) ;
        Object r = ee.at( nd ) ;
        assertTrue( r instanceof AbstractFloatDatum ) ;
        AbstractFloatDatum fr = (AbstractFloatDatum) r ;
        if( pre ) assertTrue( r == d ) ;
        else assertTrue( fr.getValue() == 100.5 ) ;
    }

    public void test_float() {
        check_float(true, true) ;
        check_float(true, false) ;
        check_float(false, true) ;
        check_float(false, false) ;
    }

    void check_ptr(boolean pre, boolean up) {
        TypeNode ty = new TyPointer() ;
        ty.addToEnd( TyInt.get() ) ;
        TyRef tyRef = new TyRef( ty ) ;

        AbstractPointerDatum d = (AbstractPointerDatum)
            ty.makeDatum(vms, vms.getStore().getStack(), "q");
        d.putValue(100);
        ScopedName name_q = new Cpp_ScopedName("q") ;
        symtab.newVar( name_q, "q", d ) ;

        ExpressionNode id = new ExpId(tyRef, "q", name_q) ;
        String name = up ? "++" : "--" ;
        ExpressionNode nd = new OpIncrement(ty, pre, up, name, id) ;

        // Make Evaluation and go.
        ExpressionEvaluation ee = new ExpressionEvaluation( vms, nd ) ;
        vms.push( ee ) ;
        advance(100) ;

        assertTrue( d.getValue() == 100 + (up?4:-4) ) ;
        assertTrue( ee.at( nd ) != null ) ;
        Object r = ee.at( nd ) ;
        assertTrue( r instanceof AbstractPointerDatum ) ;
        AbstractPointerDatum pr = (AbstractPointerDatum) r ;
        if( pre ) assertTrue( r == d ) ;
        else assertTrue( pr.getValue() == 100 ) ;
    }

    public void test_ptr() {
        check_ptr(true, true) ;
        check_ptr(true, false) ;
        check_ptr(false, true) ;
        check_ptr(false, false) ;
    }
}
