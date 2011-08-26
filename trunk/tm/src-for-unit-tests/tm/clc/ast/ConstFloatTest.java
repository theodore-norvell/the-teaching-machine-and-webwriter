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

public class ConstFloatTest extends Cpp_AbstractAstTest {

    public ConstFloatTest () { super ("ConstFloatTest"); }
    public ConstFloatTest (String name) { super (name); }


    public void test_constant_evaluation () {

        ConstFloat nd = new ConstFloat( TyDouble.get(), "3.145", 3.145 ) ;
        assertTrue( nd.is_integral_constant() ) ;
        assertTrue( nd.get_integral_constant_value() == 3 ) ;

    }

    public void test_dynamic_evaluation () {

        ConstFloat nd = new ConstFloat( TyDouble.get(), "3.145", 3.145 ) ;
        ExpressionEvaluation ee = new ExpressionEvaluation( vms, nd ) ;
        vms.push( ee ) ;
        nd.select( vms ) ;
        assertTrue( vms.top().getSelected() == nd ) ;
        nd.step( vms ) ;
        assertTrue( vms.top().at( nd ) instanceof DoubleDatum ) ;
        DoubleDatum ld = (DoubleDatum) vms.top().at( nd ) ;
        assertTrue( ld.getValue() == 3.145 ) ;

    }
}
