package tm.clc.ast;

import junit.framework.*;

import tm.clc.analysis.ScopedName;
import tm.clc.datum.* ;
import tm.cpp.analysis.Cpp_ScopedName;
import tm.cpp.datum.* ;
import tm.cpp.ast.* ;
import tm.virtualMachine.* ;

public class ExpThisMemberTest extends Cpp_AbstractAstTest {

    public ExpThisMemberTest() { super ("ExpThisMemberTest"); }
    public ExpThisMemberTest (String name) { super (name); }

    private ScopedName name_A, name_A_x, name_A_y ;
    private ScopedName name_B0, name_B0_x, name_B0_y ;
    private ScopedName name_B1, name_B1_x, name_B1_y ;
    private ScopedName name_C, name_C_x, name_C_y ;
    private TyClass class_A, class_B0, class_B1, class_C ;
    private TypeNode tyInt ;
    private ScopedName name_q ;

    private void tryField( String f_name,
                           int[] path,
                           ScopedName f_scoped_name,
                           int address )
    {
        TyRef ref_class_C = new TyRef( class_C ) ;
        TyRef ref_int = new TyRef( tyInt ) ;
        ExpThisMember nd = new ExpThisMember(
            ref_int,
            f_name,
            path,
            f_scoped_name ) ;

        // Make Evaluation.
        ExpressionEvaluation ee = new ExpressionEvaluation( vms, nd ) ;
        vms.push( ee ) ;

        // Evaluate
        // Select
            nd.select( vms ) ;
        // Step
            vms.top().getSelected().step( vms ) ;

        assertTrue( vms.top().at( nd ) != null ) ;
        assertTrue( vms.top().at( nd ) instanceof AbstractRefDatum ) ;
        AbstractRefDatum d1 = (AbstractRefDatum) vms.top().at( nd ) ;
        assertTrue( d1.deref() instanceof IntDatum ) ;
        IntDatum d2 = (IntDatum) d1.deref() ;
        int address2 = d2.getAddress() ;
        assertTrue( address == address2 ) ;
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

        // Put that datum in the symbol table as "this"
        symtab.enterFunction( d ) ;

        int addr = d.getAddress() ;

        int[] p00 = {0,0} ;
        tryField( "x", p00, name_A_x, addr+0 ) ;
        tryField( "y", p00, name_A_y, addr+4 ) ;
        int[] p0 = {0 } ;
        tryField( "x", p0,  name_B0_x, addr+8 ) ;
        tryField( "y", p0,  name_B0_y, addr+12 ) ;

        int[] p10 = {1, 0} ;
        tryField( "x", p10, name_A_x, addr+16 ) ;
        tryField( "y", p10, name_A_y, addr+20 ) ;
        int[] p1 = {1} ;
        tryField( "x", p1,  name_B1_x, addr+24 ) ;
        tryField( "y", p1,  name_B1_y, addr+28 ) ;

        int[] p = {} ;
        tryField( "x", p,   name_C_x, addr+32 ) ;
        tryField( "y", p,   name_C_y, addr+36 ) ;

    }
}
