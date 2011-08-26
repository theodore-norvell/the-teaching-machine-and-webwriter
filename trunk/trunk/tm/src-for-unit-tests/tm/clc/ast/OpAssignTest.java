package tm.clc.ast;

import junit.framework.*;

import tm.clc.analysis.ScopedName ;
import tm.clc.datum.* ;
import tm.clc.rtSymTab.RT_Symbol_Table ;
import tm.cpp.analysis.Cpp_ScopedName;
import tm.cpp.ast.* ;
import tm.cpp.datum.* ;
import tm.interfaces.* ;
import tm.virtualMachine.* ;

public class OpAssignTest extends Cpp_AbstractAstTest {

    public OpAssignTest() { super ("OpAssignTest"); }
    public OpAssignTest (String name) { super (name); }

    public void test_assign () {

        TypeNode lng = TyLong.get() ;
        TyRef refLong = new TyRef( lng ) ;

        AbstractIntDatum d = (AbstractIntDatum) lng.makeDatum(vms, vms.getStore().getStack(), "x") ;
        d.putValue(5) ;

        ScopedName name1 = new Cpp_ScopedName("x") ;
        ScopedName name2 = new Cpp_ScopedName("x") ;

        symtab.newVar( name1, "x", d ) ;

        // Make the tree
            ExpId left_operand = new ExpId(refLong, "x", name2 ) ;
            ConstInt right_operand = new ConstInt( lng, "-99", -99 ) ;

            OpAssign nd = new OpAssign( refLong, "=", left_operand, right_operand ) ;

        // Make Evaluation.
        ExpressionEvaluation ee = new ExpressionEvaluation( vms, nd ) ;
        vms.push( ee ) ;

        // Evaluate
        // Select and step twice to evaluate operands
            nd.select( vms ) ;
            vms.top().getSelected().step( vms ) ;
            nd.select( vms ) ;
            vms.top().getSelected().step( vms ) ;
        // Select
            nd.select( vms ) ;
            assertTrue( vms.top().getSelected() == nd ) ;
        // Is the target highlighted?
            assertTrue( d.getHighlight() == Datum.HIGHLIGHTED ) ;
        // Step
            vms.top().getSelected().step( vms ) ;
        // Is the target unhighlighted?
            assertTrue( d.getHighlight() == Datum.PLAIN ) ;
        // Has the value changed?
            assertTrue( d.getValue() == -99 ) ;
        // Is the node mapped correctly?
            assertTrue( vms.top().at( nd ) == vms.top().at( left_operand ) ) ;
    }
}
