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
import tm.utilities.*;

public class ExpUnimplementedTest extends Cpp_AbstractAstTest {

    public ExpUnimplementedTest () { super ("ExpUnimplementedTest"); }
    public ExpUnimplementedTest (String name) { super (name); }


    public void test_constant_evaluation () {

        ExpUnimplemented nd = new ExpUnimplemented( TyLong.get(), "something" ) ;
        assertTrue( ! nd.is_integral_constant() ) ;

    }

    public void test_dynamic_evaluation () {

        ExpUnimplemented nd = new ExpUnimplemented( TyLong.get(), "something" ) ;
        ExpressionEvaluation ee = new ExpressionEvaluation( vms, nd ) ;
        vms.push( ee ) ;
        nd.select( vms ) ;
        assertTrue( vms.top().getSelected() == nd ) ;
        try {
            nd.step( vms ) ;
            assertTrue( false ) ; }
        catch( ApologyException e ) {
            }
        catch( Throwable e ) {
            assertTrue( false ) ; }
    }

}
