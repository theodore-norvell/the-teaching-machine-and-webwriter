package tm.clc.ast;

import junit.framework.*;

import tm.backtrack.BTTimeManager ;
import tm.clc.datum.* ;
import tm.cpp.CPlusPlusLang ;
import tm.cpp.ast.* ;
import tm.cpp.datum.* ;
import tm.interfaces.ViewableST ;
import tm.languageInterface.Language ;
import tm.virtualMachine.* ;

public class ConstIntTest extends Cpp_AbstractAstTest {

    public ConstIntTest () { super ("ConstIntTest"); }
    public ConstIntTest (String name) { super (name); }


    public void test_constant_evaluation () {

        ConstInt nd = new ConstInt( TyLong.get(), "3", 3 ) ;
        assertTrue( nd.is_integral_constant() ) ;
        assertTrue( nd.get_integral_constant_value() == 3 ) ;

    }

    public void test_dynamic_evaluation () {

        ConstInt nd = new ConstInt( TyLong.get(), "3", 3 ) ;
        ExpressionEvaluation ee = new ExpressionEvaluation( vms, nd ) ;
        vms.push( ee ) ;
        nd.select( vms ) ;
        assertTrue( vms.top().getSelected() == nd ) ;
        nd.step( vms ) ;
        assertTrue( vms.top().at( nd ) instanceof LongDatum ) ;
        LongDatum ld = (LongDatum) vms.top().at( nd ) ;
        assertTrue( ld.getValue() == 3 ) ;

    }

}
