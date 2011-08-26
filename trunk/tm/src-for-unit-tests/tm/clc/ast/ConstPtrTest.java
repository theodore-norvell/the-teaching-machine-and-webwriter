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

public class ConstPtrTest extends Cpp_AbstractAstTest {

    public ConstPtrTest () { super ("ConstPtrTest"); }
    public ConstPtrTest (String name) { super (name); }


    public void test_constant_evaluation () {
        TyPointer ty = new TyPointer() ;
        ty.addToEnd( TyBool.get() ) ;
        ConstPtr nd = new ConstPtr( ty, "1", 1 ) ;
        assertTrue( ! nd.is_integral_constant() ) ;

    }

    public void test_dynamic_evaluation () {

        TyPointer ty = new TyPointer() ;
        ty.addToEnd( TyBool.get() ) ;
        ConstPtr nd = new ConstPtr( ty, "1", 1 ) ;

        ExpressionEvaluation ee = new ExpressionEvaluation( vms, nd ) ;
        vms.push( ee ) ;
        nd.select( vms ) ;
        assertTrue( vms.top().getSelected() == nd ) ;
        nd.step( vms ) ;
        assertTrue( vms.top().at( nd ) instanceof PointerDatum ) ;
        PointerDatum ld = (PointerDatum) vms.top().at( nd ) ;
        assertTrue( ld.getValue() == 1 ) ;

    }
}
