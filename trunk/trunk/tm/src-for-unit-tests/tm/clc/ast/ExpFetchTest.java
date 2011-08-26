package tm.clc.ast;

import junit.framework.*;

import tm.backtrack.BTTimeManager ;
import tm.clc.analysis.* ;
import tm.clc.datum.* ;
import tm.cpp.analysis.Cpp_ScopedName ;
import tm.cpp.CPlusPlusLang ;
import tm.cpp.ast.* ;
import tm.cpp.datum.* ;
import tm.interfaces.* ;
import tm.languageInterface.Language ;
import tm.virtualMachine.* ;

public class ExpFetchTest extends Cpp_AbstractAstTest {

    public ExpFetchTest() { super ("ExpFetchTest"); }
    public ExpFetchTest (String name) { super (name); }

    public void test_id_fetch () {

        TypeNode lng = TyLong.get() ;
        TyRef refLong = new TyRef( lng ) ;

        AbstractIntDatum d = (AbstractIntDatum) lng.makeDatum(vms, vms.getStore().getStack(), "x") ;
        d.putValue(5) ;

        ScopedName name1 = new Cpp_ScopedName("x") ;
        ScopedName name2 = new Cpp_ScopedName("x") ;

        symtab.newVar( name1, "x", d ) ;

        ExpId operand = new ExpId(refLong, "x", name2 ) ;
        operand.set_integral_constant_value( 5 ) ;

        ExpFetch nd = new ExpFetch( lng, operand ) ;

        assertTrue( nd.is_integral_constant() ) ;
        assertTrue( nd.get_integral_constant_value() == 5 ) ;

        // Make Evaluation.
        ExpressionEvaluation ee = new ExpressionEvaluation( vms, nd ) ;
        vms.push( ee ) ;

        // Evaluate
        // Select
            nd.select( vms ) ;
        // Is the operand selected?
            assertTrue( vms.top().getSelected() == operand ) ;
        // Is the st entry highlighted
            assertTrue( symtab.getEntry( 0 ).getHighlight()
                        == STEntry.HIGHLIGHTED ) ;
        // Step
            vms.top().getSelected().step( vms ) ;
        // Is it highlight gone?
            assertTrue( symtab.getEntry( 0 ).getHighlight()
                        == STEntry.PLAIN ) ;
        // Is refdatum mapped?
            assertTrue( vms.top().at( operand ) != null ) ;
            assertTrue( vms.top().at( operand ) instanceof RefDatum ) ;
        // Select
        nd.select( vms ) ;
        // Is the datum highlighted?
            assertTrue( d.getHighlight() == Datum.HIGHLIGHTED ) ;
        // Step
            vms.top().getSelected().step( vms ) ;
        // Is highlight gone?
            assertTrue( d.getHighlight() == Datum.PLAIN ) ;
        // Check that nd is mapped to the right datum.
        assertTrue( vms.top().at( nd ) != null ) ;
        assertTrue( vms.top().at( nd ) instanceof AbstractIntDatum ) ;
        AbstractIntDatum d1 = (AbstractIntDatum) vms.top().at( nd ) ;
        assertTrue( d1 == d ) ;
    }

}
