package tm.cpp.ast;

import tm.clc.ast.Cpp_AbstractAstTest;
import junit.framework.*;

import tm.backtrack.BTTimeManager ;
import tm.clc.analysis.* ;
import tm.clc.ast.* ;
import tm.clc.datum.* ;
import tm.cpp.CPlusPlusLang ;
import tm.cpp.analysis.Cpp_ScopedName;
import tm.cpp.datum.* ;
import tm.interfaces.ViewableST ;
import tm.languageInterface.Language ;
import tm.virtualMachine.* ;

public class ExpNewTest extends Cpp_AbstractAstTest {

    public ExpNewTest () { this ("ExpNewTest"); }
    public ExpNewTest (String name) { super (name); print=true ; }

    private ExpressionNode id ;
    private ExpressionNode nw ;
    private ExpressionNode assign ;
    private ExpressionNode del ;
    private ExpressionNode fetch ;

    private ExpressionEvaluation buid_and_run_new(
            boolean show_initializer,
            ExpressionNode initializer,
            NodeList initialization_args) {

        // Expression: q = new ??? ;
        id = new ExpId(tyRefPtr, "q", name_q ) ;
        nw = new ExpNew(tyPtr, show_initializer,
                        initialization_args, initializer ) ;
        assign = new OpAssign(tyRefPtr, "=", id, nw) ;

        // Push an evaluation and go
        ExpressionEvaluation ee = new ExpressionEvaluation(vms, assign) ;
        vms.push( ee ) ;
        advance(100) ;

        return ee ; }

    private ExpressionEvaluation buid_and_run_delete() {

        // Expression: delete q ;
        id = new ExpId(tyRefPtr, "q", name_q ) ;
        fetch = new ExpFetch( tyPtr, id ) ;
        del = new ExpDelete(TyVoid.get(), fetch ) ;
        assign = new OpAssign(tyRefPtr, "=", id, del) ;

        // Push an evaluation and go
        ExpressionEvaluation ee = new ExpressionEvaluation(vms, assign) ;
        vms.push( ee ) ;
        advance(100) ;

        return ee ; }

    TyPointer tyPtr ;
    ScopedName name_q ;
    TyRef tyRefPtr ;
    TyInt tyInt ;

    public void setUp() {
        super.setUp() ;

        tyInt = TyInt.get() ;

        tyPtr = new TyPointer() ;
        tyPtr.addToEnd( tyInt ) ;
        tyRefPtr = new TyRef(tyPtr) ;


        // Make a datum of type pointer to int
        AbstractDatum d = (AbstractDatum)
            tyPtr.makeDatum(vms, vms.getStore().getStack(), "q");

        // Call it "q"
        name_q = new Cpp_ScopedName("q") ;
        symtab.newVar( name_q, "q", d ) ; }

    public void test_no_initiazer1() {
        NodeList empty = new NodeList() ;
        ExpressionEvaluation ee = buid_and_run_new(false, null, empty) ;

        assertTrue( ee.at( nw ) != null ) ;
        assertTrue( ee.at( nw ) instanceof AbstractPointerDatum ) ;
        AbstractPointerDatum ptr = (AbstractPointerDatum) ee.at(nw ) ;
        int address0 = ptr.getValue() ;
        assertTrue( boHeap <= address0 && address0 <= toHeap ) ;

        // Now delete and reallocate
        ee = buid_and_run_delete() ;

        ee = buid_and_run_new(false, null, empty) ;
        assertTrue( address0 == ptr.getValue() ) ;
    }


    public void test_no_initialzer2() {
        // Showing initialization this time.
        NodeList empty = new NodeList() ;
        ExpressionEvaluation ee = buid_and_run_new(true, null, empty) ;

        assertTrue( ee.at( nw ) != null ) ;
        assertTrue( ee.at( nw ) instanceof AbstractPointerDatum ) ;
        AbstractPointerDatum ptr = (AbstractPointerDatum) ee.at(nw ) ;
        assertTrue( boHeap <= ptr.getValue() && ptr.getValue() <= toHeap ) ;
    }


    public void test_initializer() {
        TyRef tyRefInt = new TyRef( tyInt ) ;

        NodeList args = new NodeList() ;
        ConstInt ten = new ConstInt(tyInt, "10", 10) ;
        args.addFirstChild(ten) ;

        OpAssign init = new OpAssign(tyRefInt, "=",
                                new ExpArgument(tyRefInt, 0),
                                ten ) ;

        ExpressionEvaluation ee = buid_and_run_new( true, init, args) ;

        assertTrue( ee.at( nw ) != null ) ;
        assertTrue( ee.at( nw ) instanceof AbstractPointerDatum ) ;
        AbstractPointerDatum ptr = (AbstractPointerDatum) ee.at(nw ) ;
        assertTrue( boHeap <= ptr.getValue() && ptr.getValue() <= toHeap ) ;
    }
}
