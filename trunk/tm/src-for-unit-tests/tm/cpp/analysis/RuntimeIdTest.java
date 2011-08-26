package tm.cpp.analysis;

import tm.clc.analysis.*;
import tm.clc.ast.NodeList;
import tm.utilities.Debug;

import junit.framework.*;

/**
 * tests assignment of runtimeIds for different entities
 */

public class RuntimeIdTest extends TestCase 
    implements tm.cpp.analysis.TestConstantUser {

    static Cpp_CTSymbolTable st = new Cpp_CTSymbolTable ();
    static LFlags lookupFlags = Cpp_LFlags.EMPTY_LF.writable ();

    NamespaceSH sglobal;
    ClassSH sca, scb1, scb2, scc;
    Declaration dns, df1, df2, dca, dcb1, dcb2, dcc;

    public RuntimeIdTest () { super ("RuntimeIdTest"); }
    public RuntimeIdTest (String name) { super (name); }

    protected void setUp () {
        st.enterFileScope ();
        // namespaces
        // global
        sglobal = st.getGlobalScope ();
    }

    protected void tearDown () {
        
    }

    /**
     * This test simulates the lookup of a non-static data member via a subclass of
     * the class in which the data member is defined. The runtimeId associated with
     * the data member should consist of the 'index' of the superclass in relation 
     * to the subclass, followed by a <code>0</code> indicating the data member is
     * defined in the current class, and finally the unqualified id of the data member.
     * <br>For example:
     * <pre>
     * class A { public: int a; };
     * class B : public A { };
     * ...
     * B b;
     * b.a = 5;
     * ...
     * </pre>
     * should yield a runtimeId of <code>1,0,a</code> for the <code>a</code> referenced
     * in the statement <code>b.a = 5</code>
     */
    public void testNonStaticIDInSuperclass () {
        ScopeHolder origScope = st.getCurrentScope ();
        dca = st.addDefiningDeclaration (new Cpp_ScopedName (C1), Cpp_LFlags.CLASS_LF, 
                                         null);
        st.enterScope (dca);
        ScopedName a = new Cpp_ScopedName (VARID);
        Declaration da = new Declaration (Cpp_LFlags.VARIABLE_LF, new Cpp_ScopedName (VARID));
        st.addDeclaration (da);

        // lookup a from inside A
        Declaration match;
        match = lookupMember (null, a);

        // lookup A::a from inside A
        ScopedName quala = new Cpp_ScopedName (C1);
        quala.append (VARID);
        match = lookupMember (null, quala);

        // lookup ::A::a from inside A
        ScopedName gquala = new Cpp_ScopedName (quala);
        gquala.set_absolute ();
        match = lookupMember (null, gquala);

        st.exitScope ();

        dcb1 = st.addDefiningDeclaration (new Cpp_ScopedName (C2), Cpp_LFlags.CLASS_LF, 
                                          null);
        st.enterScope (dcb1);
        ClassSH scb1 = (ClassSH) dcb1.getDefinition ();
        scb1.addSuperclass ((ClassSH) dca.getDefinition (), 
                            new Cpp_SpecifierSet ());

        // lookup a from inside B
        match = lookupMember (null, a);

        // lookup A::a from inside B
        match = lookupMember (null, quala);

        // lookup ::A::a from inside B
        match = lookupMember (null, gquala);


        // lookup B::a from inside B
        ScopedName bquala = new Cpp_ScopedName (C2);
        bquala.append (VARID);
        match = lookupMember (null, bquala);

        // lookup ::B::a from inside B
        ScopedName bgquala = new Cpp_ScopedName (bquala);
        bgquala.set_absolute ();
        match = lookupMember (null, bgquala);

        st.exitScope ();

        // given A myA
        ScopedName myA = new Cpp_ScopedName ("myA");
        Declaration myAd = new Declaration (Cpp_LFlags.VARIABLE_LF, 
                                            new Cpp_ScopedName ("myA"), 
                                            dca.getDefinition (), null, null);
        st.addDeclaration (myAd);
        
        // lookup myA.a
        match = lookupMember(myA, a);

        // lookup myA.A::a
        match = lookupMember(myA, quala);

        // lookup myA.::A::a
        match = lookupMember(myA, gquala);

        // given B myB
        ScopedName myB = new Cpp_ScopedName ("myB");
        Declaration myBd = new Declaration (Cpp_LFlags.VARIABLE_LF, 
                                            new Cpp_ScopedName ("myB"), 
                                            dcb1.getDefinition (), null, null);
        st.addDeclaration (myBd);
        
        // lookup myB.a
        match = lookupMember(myB, a);

        // lookup myB.A::a
        match = lookupMember(myB, quala);


        // lookup myB.B::a
        match = lookupMember(myB, bquala);


        // lookup myB.::A::a
        match = lookupMember(myB, gquala);


        // lookup myB.::B::a
        match = lookupMember(myB, bgquala);
        
    }

    private Declaration lookupMember (ScopedName nclass, 
                                      ScopedName nmember) {
        Declaration match;
        if (nclass == null) {
            match = st.lookup (nmember).getSingleMember ();
        } else {
            st.lookup (nclass);
            d.msg (Debug.COMPILE, "looking up member after class" + nclass.getName ());
            match = st.lookup(nmember, Cpp_LFlags.CLASSREF_LF).getSingleMember ();
        }
        if (match.getRuntimeId () instanceof ScopedName)
            d.msg (Debug.COMPILE, ((ScopedName) match.getRuntimeId ()).getName ());
        return match;
    }



}

