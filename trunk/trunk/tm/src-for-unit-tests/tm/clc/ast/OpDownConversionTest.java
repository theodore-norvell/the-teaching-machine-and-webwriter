package tm.clc.ast;

import junit.framework.*;

import tm.clc.analysis.ScopedName;
import tm.clc.datum.* ;
import tm.cpp.analysis.Cpp_ScopedName;
import tm.cpp.datum.* ;
import tm.cpp.ast.* ;
import tm.virtualMachine.* ;

public class OpDownConversionTest extends Cpp_AbstractAstTest {

    public OpDownConversionTest() { super ("OpDownConversionTest"); }
    public OpDownConversionTest (String name) { super (name); }

    private ScopedName name_A, name_A_x, name_A_y ;
    private ScopedName name_B0, name_B0_x, name_B0_y ;
    private ScopedName name_B1, name_B1_x, name_B1_y ;
    private ScopedName name_C, name_C_x, name_C_y ;
    private TyClass class_A, class_B0, class_B1, class_C ;
    private TypeNode tyInt ;
    private ScopedName name_q ;

    private void trySuperobject( int[] path,
                           TyAbstractRef intermediateType )
    {
        TyRef ref_class_C = new TyRef( class_C ) ;
        TyRef ref_int = new TyRef( tyInt ) ;
        ExpId operand = new ExpId(ref_class_C, "q", name_q ) ;
        OpUpConversion nd1 = new OpUpConversion(
            intermediateType, "static_conversion<...>",
            path,
            operand ) ;
        OpDownConversion nd = new OpDownConversion(
            ref_class_C, "(...)", path, nd1 ) ;

        // Make Evaluation.
        ExpressionEvaluation ee = new ExpressionEvaluation( vms, nd ) ;
        vms.push( ee ) ;

        // Evaluate
        // Select
            nd.select( vms ) ;
        // Step
            vms.top().getSelected().step( vms ) ;
        // Select
            nd.select( vms ) ;
        // Step
            vms.top().getSelected().step( vms ) ;
        // Select
            nd.select( vms ) ;
        // Step
            vms.top().getSelected().step( vms ) ;


        assertTrue( vms.top().at( operand ) != null ) ;
        assertTrue( vms.top().at( operand ) instanceof AbstractRefDatum ) ;
        AbstractRefDatum o1 = (AbstractRefDatum) vms.top().at( operand ) ;
        assertTrue( o1.deref() instanceof AbstractObjectDatum ) ;
        AbstractObjectDatum o2 = (AbstractObjectDatum) o1.deref() ;

        assertTrue( vms.top().at( nd ) != null ) ;
        assertTrue( vms.top().at( nd ) instanceof AbstractRefDatum ) ;
        AbstractRefDatum d1 = (AbstractRefDatum) vms.top().at( nd ) ;
        assertTrue( d1.deref() instanceof AbstractObjectDatum ) ;
        AbstractObjectDatum d2 = (AbstractObjectDatum) d1.deref() ;

        assertTrue( d2 == o2 ) ;
    }

    public void testDiamond( ) {
        /* We emulate the following diamond structure
             class A { int x, y ; }
             class B0 : A { int x, y ; }
             class B1 : A { int x, y ; }
             class C : B0, B1 { int x, y ; }
        */
        tyInt = TyInt.get() ;

        // class A { int x, y ; }
        name_A = new Cpp_ScopedName("A") ;
        name_A.set_absolute() ;

        name_A_x = new Cpp_ScopedName( name_A ) ;
        name_A_x.append("x") ;

        name_A_y = new Cpp_ScopedName( name_A ) ;
        name_A_y.append("y") ;

        class_A = new TyClass("A", name_A, null ) ;
        class_A.addField(new VarNode(name_A_x, tyInt)) ;
        class_A.addField(new VarNode(name_A_y, tyInt)) ;
        class_A.setDefined() ;


        // class B0 : A { int x, y ; }
        name_B0 = new Cpp_ScopedName("B0") ;
        name_B0.set_absolute() ;

        ScopedName name_B0_x = new Cpp_ScopedName( name_B0 ) ;
        name_B0_x.append("x") ;

        name_B0_y = new Cpp_ScopedName( name_B0 ) ;
        name_B0_y.append("y") ;

        class_B0 = new TyClass("B0", name_B0, null) ;
        class_B0.addSuperClass(class_A) ;
        class_B0.addField(new VarNode(name_B0_x, tyInt)) ;
        class_B0.addField(new VarNode(name_B0_y, tyInt)) ;
        class_B0.setDefined() ;

        // class B1 : A { int x, y ; }
        name_B1 = new Cpp_ScopedName("B1") ;
        name_B1.set_absolute() ;

        name_B1_x = new Cpp_ScopedName( name_B1 ) ;
        name_B1_x.append("x") ;

        name_B1_y = new Cpp_ScopedName( name_B1 ) ;
        name_B1_y.append("y") ;

        class_B1 = new TyClass("B1", name_B1, null) ;
        class_B1.addSuperClass(class_A) ;
        class_B1.addField(new VarNode(name_B1_x, tyInt)) ;
        class_B1.addField(new VarNode(name_B1_y, tyInt)) ;
        class_B1.setDefined() ;


        // class C : B0, B1 { int x, y ; }
        name_C = new Cpp_ScopedName("C") ;
        name_C.set_absolute() ;

        name_C_x = new Cpp_ScopedName( name_C ) ;
        name_C_x.append("x") ;

        name_C_y = new Cpp_ScopedName( name_C ) ;
        name_C_y.append("y") ;

        class_C = new TyClass("C", name_C, null) ;
        class_C.addSuperClass(class_B0) ;
        class_C.addSuperClass(class_B1) ;
        class_C.addField(new VarNode(name_C_x, tyInt)) ;
        class_C.addField(new VarNode(name_C_y, tyInt)) ;
        class_C.setDefined() ;

        // Make a datum of type C
        ObjectDatum d = (ObjectDatum)
            class_C.makeDatum(vms, vms.getStore().getStack(), "q");

        name_q = new Cpp_ScopedName("q") ;
        symtab.newVar( name_q, "q", d ) ;

        int addr = d.getAddress() ;

        int[] p00 = {0,0} ;
        trySuperobject( p00, new TyRef( class_A ) ) ;

        int[] p0 = {0 } ;
        trySuperobject( p0, new TyRef( class_B0 ) ) ;

        int[] p10 = {1, 0} ;
        trySuperobject( p10, new TyRef( class_A ) ) ;

        int[] p1 = {1} ;
        trySuperobject( p1, new TyRef( class_B1 ) ) ;

        int[] p = {} ;
        trySuperobject( p, new TyRef( class_C ) ) ;
    }
}
