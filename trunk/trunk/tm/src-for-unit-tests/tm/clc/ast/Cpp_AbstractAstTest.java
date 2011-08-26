package tm.clc.ast;

import tm.cpp.ast.Cpp_ASTUtilities ;
import tm.cpp.datum.DatumUtilities ;

abstract public class Cpp_AbstractAstTest extends AbstractAstTest {

    public Cpp_AbstractAstTest(String name) { super (name); }
    
    protected void languageSpecificSetUp( ) {
        vms.setProperty("ASTUtilities", new Cpp_ASTUtilities() ) ;
        vms.setProperty("DatumUtilities", new DatumUtilities() ) ;
    }
}