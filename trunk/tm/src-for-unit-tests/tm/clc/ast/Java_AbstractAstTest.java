package tm.clc.ast;

import tm.javaLang.ast.JavaLangASTUtilities ;
import tm.javaLang.datum.DatumUtilities ;

abstract public class Java_AbstractAstTest extends AbstractAstTest {

    public Java_AbstractAstTest(String name) { super (name); }
    
    protected void languageSpecificSetUp( ) {
        vms.setProperty("ASTUtilities", new JavaLangASTUtilities() ) ;
        vms.setProperty("DatumUtilities", new DatumUtilities() ) ;
    }
}