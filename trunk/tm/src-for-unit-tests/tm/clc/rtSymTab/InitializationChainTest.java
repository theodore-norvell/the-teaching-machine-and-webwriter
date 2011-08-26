package tm.clc.rtSymTab;


import junit.framework.*;
import java.util.* ;

import tm.clc.ast.Cpp_AbstractAstTest;

import tm.cpp.ast.TyInt ;
import tm.clc.ast.* ;
import tm.clc.rtSymTab.* ;
import tm.interfaces.SourceCoords;

public class InitializationChainTest extends Cpp_AbstractAstTest {

    public InitializationChainTest () { super ("InitializationChainTest"); }
    public InitializationChainTest (String name) { super (name); }


    public void test_initialization_chains() {
        Enumeration e = symtab.getInitializationChains() ;
        assertTrue( !e.hasMoreElements() ) ;

        symtab.newInitializationChain("a") ;
        symtab.newInitializationChain("b") ;
        symtab.newInitializationChain("c") ;

        TypeNode tyInt = TyInt.get() ;
        ExpressionNode exp = new ConstInt( tyInt, "1", 1) ;

        StatDo a0 = new StatDo(SourceCoords.UNKNOWN, 0, exp) ;
        symtab.addInitializationStatement("a", a0, a0.next()) ;
        StatDo a1 = new StatDo(SourceCoords.UNKNOWN, 0, exp) ;
        symtab.addInitializationStatement("a", a1, a1.next()) ;
        StatReturn a2 = new StatReturn(SourceCoords.UNKNOWN,0) ;
        symtab.addInitializationStatement("a", a2, null) ;

        StatDo b0 = new StatDo(SourceCoords.UNKNOWN, 0, exp) ;
        symtab.addInitializationStatement("b", b0, b0.next()) ;
        StatDo b1 = new StatDo(SourceCoords.UNKNOWN, 0, exp) ;
        symtab.addInitializationStatement("b", b1, b1.next()) ;
        StatReturn b2 = new StatReturn(SourceCoords.UNKNOWN,0) ;
        symtab.addInitializationStatement("b", b2, null) ;

        StatDo c0 = new StatDo(SourceCoords.UNKNOWN, 0, exp) ;
        symtab.addInitializationStatement("c", c0, c0.next()) ;
        StatDo c1 = new StatDo(SourceCoords.UNKNOWN, 0, exp) ;
        symtab.addInitializationStatement("c", c1, c1.next()) ;
        StatReturn c2 = new StatReturn(SourceCoords.UNKNOWN,0) ;
        symtab.addInitializationStatement("c", c2, null) ;

        e = symtab.getInitializationChains() ;

        assertTrue( e.hasMoreElements() ) ;
        StatementNodeLink l = (StatementNodeLink) e.nextElement() ;
        assertTrue( l.get()==a0 ) ;

        assertTrue( e.hasMoreElements() ) ;
        l = (StatementNodeLink) e.nextElement() ;
        assertTrue( l.get()==b0 ) ;

        assertTrue( e.hasMoreElements() ) ;
        l = (StatementNodeLink) e.nextElement() ;
        assertTrue( l.get()==c0 ) ;


        assertTrue( ! e.hasMoreElements() ) ;
    }
}
