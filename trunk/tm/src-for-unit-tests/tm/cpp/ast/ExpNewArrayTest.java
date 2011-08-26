package tm.cpp.ast;

import tm.clc.ast.Cpp_AbstractAstTest;
import junit.framework.*;

import tm.backtrack.BTTimeManager ;
import tm.clc.analysis.* ;
import tm.clc.ast.* ;
import tm.clc.datum.* ;
import tm.cpp.analysis.Cpp_ScopedName;
import tm.cpp.CPlusPlusLang ;
import tm.cpp.datum.* ;
import tm.interfaces.Datum ;
import tm.interfaces.ViewableST ;
import tm.languageInterface.Language ;
import tm.virtualMachine.* ;

public class ExpNewArrayTest extends Cpp_AbstractAstTest {

    static final int NUMTOALLOC = 10 ;
    public ExpNewArrayTest () { this ("ExpNewArrayTest"); }
    public ExpNewArrayTest (String name) { super (name); print=true ; }

    private ExpressionNode id ;
    private ExpressionNode nw ;
    private ExpressionNode size ;
    private ExpressionNode assign ;

    private ExpressionEvaluation build_and_run_new(TypeNode ty,
            String string_name,
            ScopedName scoped_name,
            boolean show_initializer,
            ExpressionNode initializer) {
        // Expression: name = new ??? ;
        id = new ExpId(tyRefPtr, string_name, scoped_name ) ;
        TypeNode tyInt = TyInt.get() ;
        size = new ConstInt(tyInt, ""+NUMTOALLOC, NUMTOALLOC) ;
        nw = new ExpNewArray(tyPtr,
                        show_initializer,
                        size, initializer ) ;
        assign = new OpAssign(tyRefPtr, "=", id, nw) ;

        // Push an evaluation and go
        ExpressionEvaluation ee = new ExpressionEvaluation(vms, assign) ;
        vms.push( ee ) ;
        advance(100) ;

        return ee ; }

    private ExpressionEvaluation build_and_run_delete(TypeNode ty,
            String string_name,
            ScopedName scoped_name) {

        // Expression: delete name ;
        ExpressionNode id2 = new ExpId(tyRefPtr, string_name, scoped_name ) ;
        ExpressionNode fetch = new ExpFetch( tyPtr, id2 ) ;
        ExpressionNode del = new ExpDeleteArray(ty, fetch ) ;

        // Push an evaluation and go
        ExpressionEvaluation ee = new ExpressionEvaluation(vms, del) ;
        vms.push( ee ) ;
        advance(100) ;

        return ee ; }

    ScopedName name_q ;
    ScopedName name_p ;
    TypeNode tyInt ;
    TypeNode tyVoid ;
    TyPointer tyPtr ;
    TyRef tyRefPtr ;
    NodeList empty ;

    public void setUp() {
        super.setUp() ;
        empty = new NodeList() ;
        tyInt = TyInt.get() ;
        tyVoid = TyVoid.get() ;
        tyPtr = new TyPointer() ;
        tyPtr.addToEnd( tyInt ) ;
        tyRefPtr = new TyRef(tyPtr) ;

        // Make a datum of type pointer to int
        AbstractDatum d = (AbstractDatum) tyPtr.makeDatum(vms, vms.getStore().getStack(), "p");

        // Call it "p"
        name_p = new Cpp_ScopedName("p") ;
        symtab.newVar( name_p, "p", d ) ;

        // Make another datum of type pointer to int
        d = (AbstractDatum) tyPtr.makeDatum(vms, vms.getStore().getStack(), "q");
        // Call it "q"
        name_q = new Cpp_ScopedName("q") ;
        symtab.newVar( name_q, "q", d ) ;
    }

    public void test_no_initiazer1() {
        ExpressionEvaluation ee = build_and_run_new(tyInt, "p", name_p, false, null) ;

        assertTrue( ee.at( nw ) != null ) ;
        assertTrue( ee.at( nw ) instanceof AbstractPointerDatum ) ;
        AbstractPointerDatum ptr0 = (AbstractPointerDatum) ee.at(nw ) ;

        int address0 = ptr0.getValue() ;

        assertTrue( boHeap <= address0 && address0 <= toHeap ) ;
        assertTrue( vms.getMemory().getByte( address0-4 ) == NUMTOALLOC ) ;
        assertTrue( vms.getMemory().getByte( address0-3 ) == 0 ) ;
        assertTrue( vms.getMemory().getByte( address0-2 ) == 0 ) ;
        assertTrue( vms.getMemory().getByte( address0-1 ) == 0 ) ;

        ee=build_and_run_new(tyInt, "q", name_q, false, null) ;

        assertTrue( ee.at( nw ) != null ) ;
        assertTrue( ee.at( nw ) instanceof AbstractPointerDatum ) ;
        AbstractPointerDatum ptr1 = (AbstractPointerDatum) ee.at(nw ) ;
        int address1 = ptr1.getValue() ;

        assertTrue( boHeap <= address1 && address1 <= toHeap ) ;

        System.out.println( "address0 is "+address0+" address1 is "+address1 ) ;
        // 4*NUMTOALLOC for the first array + 4 for the size of the second array.
        assertTrue( address0 + 4+4*NUMTOALLOC == address1 ) ;

        assertTrue( vms.getMemory().getByte( address1-4 ) == NUMTOALLOC ) ;
        assertTrue( vms.getMemory().getByte( address1-3 ) == 0 ) ;
        assertTrue( vms.getMemory().getByte( address1-2 ) == 0 ) ;
        assertTrue( vms.getMemory().getByte( address1-1 ) == 0 ) ;

        // Now delete p and reallocate p
        ee = build_and_run_delete(tyVoid, "p", name_p ) ;

        ee = build_and_run_new(tyInt, "p", name_p, false, null) ;

        assertTrue( ee.at( nw ) != null ) ;
        assertTrue( ee.at( nw ) instanceof AbstractPointerDatum ) ;
        AbstractPointerDatum ptr2 = (AbstractPointerDatum) ee.at(nw ) ;

        int address2 = ptr2.getValue() ;

        assertTrue( address2==address0 ) ;
    }


    public void test_no_initialzer2() {
        // The difference here is syntax.
        ExpressionEvaluation ee = build_and_run_new(tyInt, "p", name_p, true, null) ;

        assertTrue( ee.at( nw ) != null ) ;
        assertTrue( ee.at( nw ) instanceof AbstractPointerDatum ) ;
        AbstractPointerDatum ptr = (AbstractPointerDatum) ee.at(nw ) ;
        assertTrue( boHeap <= ptr.getValue() && ptr.getValue() <= toHeap ) ;
    }


    public void test_initializer() {
        final int VAL = 1234567 ;
        TyRef tyRefInt = new TyRef( tyInt ) ;

        ConstInt val = new ConstInt(tyInt, ""+VAL, VAL) ;

        OpAssign init = new OpAssign(tyRefInt, "=",
                                new ExpArgument(tyRefInt, 0),
                                val ) ;

        ExpressionEvaluation ee = build_and_run_new(tyInt, "p", name_p, true, init) ;

        assertTrue( ee.at( nw ) != null ) ;
        assertTrue( ee.at( nw ) instanceof AbstractPointerDatum ) ;
        AbstractPointerDatum ptr = (AbstractPointerDatum) ee.at(nw ) ;
        assertTrue( boHeap <= ptr.getValue() && ptr.getValue() <= toHeap ) ;

        // Check initialization
        Datum d ;
        for( int i=0 ; i < NUMTOALLOC ; ++i ) {
            d = ptr.deref() ;
            assertTrue( d != null & d instanceof IntDatum ) ;
            assertTrue( ((IntDatum)d).getValue() == VAL ) ;
            ptr.putValue ( ptr.getValue() + tyInt.getNumBytes() ) ; }

    }
}