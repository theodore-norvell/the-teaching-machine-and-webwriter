package tm.cpp.analysis;

import java.util.Vector ;

import tm.clc.analysis.*;
import tm.clc.ast.NodeList;
import tm.cpp.ast.TyFun;
import tm.cpp.ast.TyInt;
import tm.interfaces.SourceCoords;
import tm.utilities.Debug;

import junit.framework.*;

/**
 * tests lookup in the following set of scopes:
 * <br><pre>
 * class C1 { };
 * namespace NS {
 *   class C2 ;
 *   class C3 { };
 *   class C4 : public C3 { };
 *   class C5 { class C6 { }; };
 *   class C7 : public C4, public C1 { };
 * }
 * class NS::C2 { };
 * class C8 : public NS::C4 { };
 * class C9 {
 *   void f1 () {
 *     class C10 { };
 *   }
 * class C11 : public C1 { class C12 { }; };
 * }
 *</pre>
 */

public class ClassLookupTest extends LookupTest {

    // namespaces
    NamespaceSH sglobal, sns; // namespaces
    Declaration dns;

    // classes
    ClassSH sc1, sc2, sc3, sc4, sc5, sc6, sc7, sc8, sc9, sc10, sc11, sc12;
    Declaration dc1, dc2, dc3, dc4, dc5, dc6, dc7, dc8, dc9, dc10, dc11, dc12;

    // functions
    MemberFnSH sfn;
    Declaration dfn;


    ScopedName id1, ida, idb, idc, idd, ide, idf, idg, idh, idi, idj, idk, idl,
        idm, idn, ido, idp, idq, idr, ids, idt;
    Declaration var1;

    public ClassLookupTest () { super ("ClassLookupTest"); }
    public ClassLookupTest (String name) { super (name); }

    protected void setUp () {
        TyFun dummyTyFun = new TyFun (new Vector (), false);
        dummyTyFun.addToEnd (TyInt.get ());
        Cpp_SpecifierSet dummySp = new Cpp_SpecifierSet ();
        // namespaces
        // global
        Cpp_SpecifierSet spec_set = new Cpp_SpecifierSet ();
        sglobal = new NamespaceSH ();
        allNamespaces.addElement (sglobal);

        // namespace
        sns = new NamespaceSH (sglobal);
        dns = new Declaration(Cpp_LFlags.NAMESPACE_LF,new Cpp_ScopedName (NSID));
        dns.setDefinition (sns); sns.addOwnDeclaration (dns);
        allNamespaces.addElement (sns);

        // classes
        // global class
        sc1 = new ClassSH (sglobal);
        dc1 = new Declaration (Cpp_LFlags.CLASS_LF, new Cpp_ScopedName (C1));
        dc1.setDefinition (sc1); sc1.addOwnDeclaration (dc1);

        // namespace class, defined externally
        sc2 = new ClassSH (sglobal);
        sc2.setOwningScope (sns);
        dc2 = new Declaration (Cpp_LFlags.CLASS_LF, new Cpp_ScopedName (C2));
        dc2.setDefinition (sc2); sc2.addOwnDeclaration (dc2);

        // namespace class, defined inline
        sc3 = new ClassSH (sns);
        dc3 = new Declaration (Cpp_LFlags.CLASS_LF, new Cpp_ScopedName (C3));
        dc3.setDefinition (sc3); sc3.addOwnDeclaration (dc3);
        // subclass (ns member)
        sc4 = new ClassSH (sns);
        sc4.addSuperclass (sc3, spec_set);
        dc4 = new Declaration (Cpp_LFlags.CLASS_LF, new Cpp_ScopedName (C4));
        dc4.setDefinition (sc4); sc4.addOwnDeclaration (dc4);
        // namespace class, enclosing inner class
        sc5 = new ClassSH (sns);
        dc5 = new Declaration (Cpp_LFlags.CLASS_LF, new Cpp_ScopedName (C5));
        dc5.setDefinition (sc5); sc5.addOwnDeclaration (dc5);
        // inner / local class
        sc6 = new ClassSH (sc5);
        dc6 = new Declaration (Cpp_LFlags.CLASS_LF, new Cpp_ScopedName (C6));
        dc6.setDefinition (sc6); sc6.addOwnDeclaration (dc6);
        // multiple inheritance (ns member)
        sc7 = new ClassSH (sns);
        sc7.addSuperclass (sc4, spec_set); sc7.addSuperclass (sc1, spec_set);
        dc7 = new Declaration (Cpp_LFlags.CLASS_LF, new Cpp_ScopedName (C7));
        dc7.setDefinition (sc7); sc7.addOwnDeclaration (dc7);
        // subclass, superclass defined in non-enclosing ns
        sc8 = new ClassSH (sglobal);
        sc8.addSuperclass (sc4, spec_set);
        dc8 = new Declaration (Cpp_LFlags.CLASS_LF, new Cpp_ScopedName (C8));
        dc8.setDefinition (sc8); sc8.addOwnDeclaration (dc8);
        // class enclosing fn with local class
        sc9 = new ClassSH (sglobal);
        dc9 = new Declaration (Cpp_LFlags.CLASS_LF, new Cpp_ScopedName (C9));
        dc9.setDefinition (sc9); sc9.addOwnDeclaration (dc9);

        // function enclosing local class
        sfn = new MemberFnSH (sc9);
        dfn = new FunctionDeclaration (Cpp_LFlags.MEM_FN_LF,
                                       new Cpp_ScopedName (FNID), sfn, dummySp,
                                       dummyTyFun);
        sfn.addOwnDeclaration (dfn);
        // local class, defined in function
        sc10 = new ClassSH (sfn);
        dc10 = new Declaration (Cpp_LFlags.CLASS_LF, new Cpp_ScopedName (C10));
        dc10.setDefinition (sc10); sc10.addOwnDeclaration (dc10);

        // these ids will be used in tests
        id1 = new Cpp_ScopedName (VARID);
        ida = id1;
        idb = new Cpp_ScopedName (A1);
        idc = new Cpp_ScopedName (A2);
        idd = new Cpp_ScopedName (A3);
        ide = new Cpp_ScopedName (A4);
        idf = new Cpp_ScopedName (A5);
        idg = new Cpp_ScopedName (A6);
        idh = new Cpp_ScopedName (A7);
        idi = new Cpp_ScopedName (A8);
        idj = new Cpp_ScopedName (A9);
        idk = new Cpp_ScopedName (A10);
        idl = new Cpp_ScopedName (A11);
        idm = new Cpp_ScopedName (A12);
        idn = new Cpp_ScopedName (A13);
        ido = new Cpp_ScopedName (A14);
        idp = new Cpp_ScopedName (A15);
        idq = new Cpp_ScopedName (A16);
        idr = new Cpp_ScopedName (A17);
        ids = new Cpp_ScopedName (A18);
        idt = new Cpp_ScopedName (A19);

        // subclass enclosing inner class
        sc11 = new ClassSH (sns);
        sc11.addSuperclass (sc1, spec_set);
        dc11 = new Declaration (Cpp_LFlags.CLASS_LF, new Cpp_ScopedName (C11));
        dc11.setDefinition (sc11);

        // inner class for c11
        sc12 = new ClassSH (sc11);
        dc12 = new Declaration (Cpp_LFlags.CLASS_LF, new Cpp_ScopedName (C12));
        dc12.setDefinition (sc12);

    }

    public void testLookupNothing () {
        // the fun stuff..
        DeclarationSet matches;
        Declaration match;

        try {
            d.msg (Debug.COMPILE, "***searching C1***");
            matches = sc1.lookup (id1, Cpp_LFlags.EMPTY_LF);
            resetNS (); assertTrue (matches.isEmpty ());

            d.msg (Debug.COMPILE, "***searching C2***");
            matches = sc2.lookup (id1, Cpp_LFlags.EMPTY_LF);
            resetNS (); assertTrue (matches.isEmpty ());

            d.msg (Debug.COMPILE, "***searching C3***");
            matches = sc3.lookup (id1, Cpp_LFlags.EMPTY_LF);
            resetNS (); assertTrue (matches.isEmpty ());

            d.msg (Debug.COMPILE, "***searching C4***");
            matches = sc4.lookup (id1, Cpp_LFlags.EMPTY_LF);
            resetNS (); assertTrue (matches.isEmpty ());

            d.msg (Debug.COMPILE, "***searching C5***");
            matches = sc5.lookup (id1, Cpp_LFlags.EMPTY_LF);
            resetNS (); assertTrue (matches.isEmpty ());

            d.msg (Debug.COMPILE, "***searching C6***");
            matches = sc6.lookup (id1, Cpp_LFlags.EMPTY_LF);
            resetNS (); assertTrue (matches.isEmpty ());

            d.msg (Debug.COMPILE, "***searching C7***");
            matches = sc7.lookup (id1, Cpp_LFlags.EMPTY_LF);
            resetNS (); assertTrue (matches.isEmpty ());

            d.msg (Debug.COMPILE, "***searching C8***");
            matches = sc8.lookup (id1, Cpp_LFlags.EMPTY_LF);
            resetNS (); assertTrue (matches.isEmpty ());

            d.msg (Debug.COMPILE, "***searching C9***");
            matches = sc9.lookup (id1, Cpp_LFlags.EMPTY_LF);
            resetNS (); assertTrue (matches.isEmpty ());

            d.msg (Debug.COMPILE, "***searching C10***");
            matches = sc10.lookup (id1, Cpp_LFlags.EMPTY_LF);
            resetNS (); assertTrue (matches.isEmpty ());

        } catch (UndefinedSymbolException use) {
            use.printStackTrace ();
        }

    }


    public void testLookupGlobal () {
        // the fun stuff..
        DeclarationSet matches;
        Declaration match;

        // add a var to global scope
        var1 = new Declaration (Cpp_LFlags.VARIABLE_LF, id1);

        sglobal.addDeclaration (var1);

        try {
            matches = sc1.lookup (id1, Cpp_LFlags.EMPTY_LF);
            resetNS (); assertTrue (matches.getSingleMember () == var1);

            matches = sc2.lookup (id1, Cpp_LFlags.EMPTY_LF);
            resetNS (); assertTrue (matches.getSingleMember () == var1);

            matches = sc3.lookup (id1, Cpp_LFlags.EMPTY_LF);
            resetNS (); assertTrue (matches.getSingleMember () == var1);

            matches = sc4.lookup (id1, Cpp_LFlags.EMPTY_LF);
            resetNS (); assertTrue (matches.getSingleMember () == var1);

            matches = sc5.lookup (id1, Cpp_LFlags.EMPTY_LF);
            resetNS (); assertTrue (matches.getSingleMember () == var1);

            matches = sc6.lookup (id1, Cpp_LFlags.EMPTY_LF);
            resetNS (); assertTrue (matches.getSingleMember () == var1);

            matches = sc7.lookup (id1, Cpp_LFlags.EMPTY_LF);
            resetNS (); assertTrue (matches.getSingleMember () == var1);

            matches = sc8.lookup (id1, Cpp_LFlags.EMPTY_LF);
            resetNS (); assertTrue (matches.getSingleMember () == var1);

            matches = sc9.lookup (id1, Cpp_LFlags.EMPTY_LF);
            resetNS (); assertTrue (matches.getSingleMember () == var1);

            matches = sc10.lookup (id1, Cpp_LFlags.EMPTY_LF);
            resetNS (); assertTrue (matches.getSingleMember () == var1);


        } catch (UndefinedSymbolException use) {
            use.printStackTrace ();
        }

    }


    /**
     *<pre>
     * int a;
     * int b;
     * class C1 {
     *   int b;
     *   int c;
     * };
     * </pre>
     */
    public void testLookupC1 () {
        DeclarationSet matches;
        Declaration match;

        // need a couple more var ids
        ScopedName id2 = new Cpp_ScopedName (A1);
        ScopedName id3 = new Cpp_ScopedName (A2);

        // add two vars to global scope
        Declaration ga = new Declaration (Cpp_LFlags.VARIABLE_LF, id1);
        sglobal.addDeclaration (ga);

        Declaration gb = new Declaration (Cpp_LFlags.VARIABLE_LF, id2);
        sglobal.addDeclaration (gb);

        // add two vars to class scope
        Declaration cb = new Declaration (Cpp_LFlags.VARIABLE_LF, id2);
        sc1.addDeclaration (cb);

        Declaration cc = new Declaration (Cpp_LFlags.VARIABLE_LF, id3);
        sc1.addDeclaration (cc);


        try {
            matches = sc1.lookup (id1, Cpp_LFlags.EMPTY_LF);
            resetNS (); assertTrue (matches.getSingleMember () == ga);

            matches = sc1.lookup (id2, Cpp_LFlags.EMPTY_LF);
            resetNS (); assertTrue (matches.getSingleMember () == cb);

            matches = sc1.lookup (id3, Cpp_LFlags.EMPTY_LF);
            resetNS (); assertTrue (matches.getSingleMember () == cc);

        } catch (UndefinedSymbolException use) {
            use.printStackTrace ();
        }

    }

    /**
     *<pre>
     * int a;
     * int b;
     * int c;
     * namespace NS {
     *   int b;
     *   int d;
     *   int e;
     *   class C2 ;
     * }
     *
     * class NS::C2 {
     *   int c;
     *   int d;
     *   int f;
     *   ...a...
     *   ...b...
     *   ...c...
     *   ...d...
     *   ...e...
     *   ...f...
     * };
     * </pre>
     */

    public void testLookupC2 () {
        DeclarationSet matches;
        Declaration match;

        // need some more var ids
        ScopedName id2 = new Cpp_ScopedName (A1);
        ScopedName id3 = new Cpp_ScopedName (A2);
        ScopedName id4 = new Cpp_ScopedName (A3);
        ScopedName id5 = new Cpp_ScopedName (A4);
        ScopedName id6 = new Cpp_ScopedName (A5);

        // add three vars to global scope
        Declaration ga = new Declaration (Cpp_LFlags.VARIABLE_LF, id1);
        sglobal.addDeclaration (ga);

        Declaration gb = new Declaration (Cpp_LFlags.VARIABLE_LF, id2);
        sglobal.addDeclaration (gb);

        Declaration gc = new Declaration (Cpp_LFlags.VARIABLE_LF, id3);
        sglobal.addDeclaration (gc);

        // add three vars to ns scope
        Declaration nb = new Declaration (Cpp_LFlags.VARIABLE_LF, id2);
        sns.addDeclaration (nb);

        Declaration nd = new Declaration (Cpp_LFlags.VARIABLE_LF, id4);
        sns.addDeclaration (nd);

        Declaration ne = new Declaration (Cpp_LFlags.VARIABLE_LF, id5);
        sns.addDeclaration (ne);

        // add three vars to C2 scope
        Declaration c2c = new Declaration (Cpp_LFlags.VARIABLE_LF, id3);
        sc2.addDeclaration (c2c);

        Declaration c2d = new Declaration (Cpp_LFlags.VARIABLE_LF, id4);
        sc2.addDeclaration (c2d);

        Declaration c2f = new Declaration (Cpp_LFlags.VARIABLE_LF, id6);
        sc2.addDeclaration (c2f);

        try {
            matches = sc2.lookup (id1, Cpp_LFlags.EMPTY_LF);
            resetNS (); assertTrue (matches.getSingleMember () == ga);

            matches = sc2.lookup (id2, Cpp_LFlags.EMPTY_LF);
            resetNS (); assertTrue (matches.getSingleMember () == nb);

            matches = sc2.lookup (id3, Cpp_LFlags.EMPTY_LF);
            resetNS (); assertTrue (matches.getSingleMember () == c2c);

            matches = sc2.lookup (id4, Cpp_LFlags.EMPTY_LF);
            resetNS (); assertTrue (matches.getSingleMember () == c2d);

            matches = sc2.lookup (id5, Cpp_LFlags.EMPTY_LF);
            resetNS (); assertTrue (matches.getSingleMember () == ne);

            matches = sc2.lookup (id6, Cpp_LFlags.EMPTY_LF);
            resetNS (); assertTrue (matches.getSingleMember () == c2f);

        } catch (UndefinedSymbolException use) {
            use.printStackTrace ();
        }

    }

    /**
     *<pre>
     * int a;
     * int b;
     * int c;
     * namespace NS {
     *   int b;
     *   int d;
     *   int e;
     *   class C3 {
     *     int c;
     *     int d;
     *     int f;
     *     ...a...
     *     ...b...
     *     ...c...
     *     ...d...
     *     ...e...
     *     ...f...
     *   };
     * }
     * </pre>
     */

    public void testLookupC3 () {
        DeclarationSet matches;
        Declaration match;

        // need some more var ids
        ScopedName id2 = new Cpp_ScopedName (A1);
        ScopedName id3 = new Cpp_ScopedName (A2);
        ScopedName id4 = new Cpp_ScopedName (A3);
        ScopedName id5 = new Cpp_ScopedName (A4);
        ScopedName id6 = new Cpp_ScopedName (A5);

        // add three vars to global scope
        Declaration ga = new Declaration (Cpp_LFlags.VARIABLE_LF, id1);
        sglobal.addDeclaration (ga);

        Declaration gb = new Declaration (Cpp_LFlags.VARIABLE_LF, id2);
        sglobal.addDeclaration (gb);

        Declaration gc = new Declaration (Cpp_LFlags.VARIABLE_LF, id3);
        sglobal.addDeclaration (gc);

        // add three vars to ns scope
        Declaration nb = new Declaration (Cpp_LFlags.VARIABLE_LF, id2);
        sns.addDeclaration (nb);

        Declaration nd = new Declaration (Cpp_LFlags.VARIABLE_LF, id4);
        sns.addDeclaration (nd);

        Declaration ne = new Declaration (Cpp_LFlags.VARIABLE_LF, id5);
        sns.addDeclaration (ne);

        // add three vars to C3 scope
        Declaration c3c = new Declaration (Cpp_LFlags.VARIABLE_LF, id3);
        sc3.addDeclaration (c3c);

        Declaration c3d = new Declaration (Cpp_LFlags.VARIABLE_LF, id4);
        sc3.addDeclaration (c3d);

        Declaration c3f = new Declaration (Cpp_LFlags.VARIABLE_LF, id6);
        sc3.addDeclaration (c3f);

        try {
            matches = sc3.lookup (id1, Cpp_LFlags.EMPTY_LF);
            resetNS (); assertTrue (matches.getSingleMember () == ga);

            matches = sc3.lookup (id2, Cpp_LFlags.EMPTY_LF);
            resetNS (); assertTrue (matches.getSingleMember () == nb);

            matches = sc3.lookup (id3, Cpp_LFlags.EMPTY_LF);
            resetNS (); assertTrue (matches.getSingleMember () == c3c);

            matches = sc3.lookup (id4, Cpp_LFlags.EMPTY_LF);
            resetNS (); assertTrue (matches.getSingleMember () == c3d);

            matches = sc3.lookup (id5, Cpp_LFlags.EMPTY_LF);
            resetNS (); assertTrue (matches.getSingleMember () == ne);

            matches = sc3.lookup (id6, Cpp_LFlags.EMPTY_LF);
            resetNS (); assertTrue (matches.getSingleMember () == c3f);

        } catch (UndefinedSymbolException use) {
            use.printStackTrace ();
        }

    }

    /**
     *<pre>
     * int a, b, c, g;
     * namespace NS {
     *   int b, d, e, h;
     *   class C3 {
     *     public:
     *     int c, d, f, i;
     *   };
     *   class C4 : public C3 {
     *     int g, h, i, j;
     *     ...use a--j...
     *   };
     * }
     * </pre>
     */

    public void testLookupC4 () {
        DeclarationSet matches;
        Declaration match;

        // need some more var ids
        ScopedName id2 = new Cpp_ScopedName (A1);
        ScopedName id3 = new Cpp_ScopedName (A2);
        ScopedName id4 = new Cpp_ScopedName (A3);
        ScopedName id5 = new Cpp_ScopedName (A4);
        ScopedName id6 = new Cpp_ScopedName (A5);
        ScopedName id7 = new Cpp_ScopedName (A6);
        ScopedName id8 = new Cpp_ScopedName (A7);
        ScopedName id9 = new Cpp_ScopedName (A8);
        ScopedName id10 = new Cpp_ScopedName (A9);

        // add four vars to global scope
        // add three vars to global scope
        Declaration ga = new Declaration (Cpp_LFlags.VARIABLE_LF, id1);
        sglobal.addDeclaration (ga);

        Declaration gb = new Declaration (Cpp_LFlags.VARIABLE_LF, id2);
        sglobal.addDeclaration (gb);

        Declaration gc = new Declaration (Cpp_LFlags.VARIABLE_LF, id3);
        sglobal.addDeclaration (gc);

        Declaration gg = new Declaration (Cpp_LFlags.VARIABLE_LF, id7);
        sglobal.addDeclaration (gg);

        // add four vars to ns scope
        Declaration nb = new Declaration (Cpp_LFlags.VARIABLE_LF, id2);
        sns.addDeclaration (nb);

        Declaration nd = new Declaration (Cpp_LFlags.VARIABLE_LF, id4);
        sns.addDeclaration (nd);

        Declaration ne = new Declaration (Cpp_LFlags.VARIABLE_LF, id5);
        sns.addDeclaration (ne);

        Declaration nh = new Declaration (Cpp_LFlags.VARIABLE_LF, id8);
        sns.addDeclaration (nh);

        // add four vars to C3 scope
        Declaration c3c = new Declaration (Cpp_LFlags.VARIABLE_LF, id3);
        sc3.addDeclaration (c3c);

        Declaration c3d = new Declaration (Cpp_LFlags.VARIABLE_LF, id4);
        sc3.addDeclaration (c3d);

        Declaration c3f = new Declaration (Cpp_LFlags.VARIABLE_LF, id6);
        sc3.addDeclaration (c3f);

        Declaration c3i = new Declaration (Cpp_LFlags.VARIABLE_LF, id9);
        sc3.addDeclaration (c3i);

        // add four vars to C4 scope
        Declaration c4g = new Declaration (Cpp_LFlags.VARIABLE_LF, id7);
        sc4.addDeclaration (c4g);

        Declaration c4h = new Declaration (Cpp_LFlags.VARIABLE_LF, id8);
        sc4.addDeclaration (c4h);

        Declaration c4i = new Declaration (Cpp_LFlags.VARIABLE_LF, id9);
        sc4.addDeclaration (c4i);

        Declaration c4j = new Declaration (Cpp_LFlags.VARIABLE_LF, id10);
        sc4.addDeclaration (c4j);


        try {
            d.msg(Debug.COMPILE, "looking for global a");
            matches = sc4.lookup (id1, Cpp_LFlags.EMPTY_LF);
            resetNS (); assertTrue (matches.getSingleMember () == ga);

            d.msg(Debug.COMPILE, "looking for namespace b");
            matches = sc4.lookup (id2, Cpp_LFlags.EMPTY_LF);
            resetNS (); assertTrue (matches.getSingleMember () == nb);

            matches = sc4.lookup (id3, Cpp_LFlags.EMPTY_LF);
            resetNS (); assertTrue (matches.getSingleMember () == c3c);

            matches = sc4.lookup (id4, Cpp_LFlags.EMPTY_LF);
            resetNS (); assertTrue (matches.getSingleMember () == c3d);

            matches = sc4.lookup (id5, Cpp_LFlags.EMPTY_LF);
            resetNS (); assertTrue (matches.getSingleMember () == ne);

            matches = sc4.lookup (id6, Cpp_LFlags.EMPTY_LF);
            resetNS (); assertTrue (matches.getSingleMember () == c3f);

            matches = sc4.lookup (id7, Cpp_LFlags.EMPTY_LF);
            resetNS (); assertTrue (matches.getSingleMember () == c4g);

            matches = sc4.lookup (id8, Cpp_LFlags.EMPTY_LF);
            resetNS (); assertTrue (matches.getSingleMember () == c4h);

            matches = sc4.lookup (id9, Cpp_LFlags.EMPTY_LF);
            resetNS (); assertTrue (matches.getSingleMember () == c4i);

            matches = sc4.lookup (id10, Cpp_LFlags.EMPTY_LF);
            resetNS (); assertTrue (matches.getSingleMember () == c4j);

        } catch (UndefinedSymbolException use) {
            use.printStackTrace ();
        }

    }


    /**
     *<pre>
     * int a, b, c, g;
     * namespace NS {
     *   int b, d, e, h;
     *   class C5 {
     *     public:
     *     int c, d, f, i;
     *     class C6 {
     *       int g, h, i, j;
     *       ...a--j...
     *     };
     *   };
     * }
     * </pre>
     */

    public void testLookupC6 () {
        DeclarationSet matches;
        Declaration match;

        // need some more var ids
        ScopedName id2 = new Cpp_ScopedName (A1);
        ScopedName id3 = new Cpp_ScopedName (A2);
        ScopedName id4 = new Cpp_ScopedName (A3);
        ScopedName id5 = new Cpp_ScopedName (A4);
        ScopedName id6 = new Cpp_ScopedName (A5);
        ScopedName id7 = new Cpp_ScopedName (A6);
        ScopedName id8 = new Cpp_ScopedName (A7);
        ScopedName id9 = new Cpp_ScopedName (A8);
        ScopedName id10 = new Cpp_ScopedName (A9);

        // add four vars to global scope

        Declaration ga = new Declaration (Cpp_LFlags.VARIABLE_LF, id1);
        sglobal.addDeclaration (ga);

        Declaration gb = new Declaration (Cpp_LFlags.VARIABLE_LF, id2);
        sglobal.addDeclaration (gb);

        Declaration gc = new Declaration (Cpp_LFlags.VARIABLE_LF, id3);
        sglobal.addDeclaration (gc);

        Declaration gg = new Declaration (Cpp_LFlags.VARIABLE_LF, id7);
        sglobal.addDeclaration (gg);

        // add four vars to ns scope
        Declaration nb = new Declaration (Cpp_LFlags.VARIABLE_LF, id2);
        sns.addDeclaration (nb);

        Declaration nd = new Declaration (Cpp_LFlags.VARIABLE_LF, id4);
        sns.addDeclaration (nd);

        Declaration ne = new Declaration (Cpp_LFlags.VARIABLE_LF, id5);
        sns.addDeclaration (ne);

        Declaration nh = new Declaration (Cpp_LFlags.VARIABLE_LF, id8);
        sns.addDeclaration (nh);

        // add four vars to C5 scope
        Declaration c5c = new Declaration (Cpp_LFlags.VARIABLE_LF, id3);
        sc5.addDeclaration (c5c);

        Declaration c5d = new Declaration (Cpp_LFlags.VARIABLE_LF, id4);
        sc5.addDeclaration (c5d);

        Declaration c5f = new Declaration (Cpp_LFlags.VARIABLE_LF, id6);
        sc5.addDeclaration (c5f);

        Declaration c5i = new Declaration (Cpp_LFlags.VARIABLE_LF, id9);
        sc5.addDeclaration (c5i);

        // add four vars to C6 scope
        Declaration c6g = new Declaration (Cpp_LFlags.VARIABLE_LF, id7);
        sc6.addDeclaration (c6g);

        Declaration c6h = new Declaration (Cpp_LFlags.VARIABLE_LF, id8);
        sc6.addDeclaration (c6h);

        Declaration c6i = new Declaration (Cpp_LFlags.VARIABLE_LF, id9);
        sc6.addDeclaration (c6i);

        Declaration c6j = new Declaration (Cpp_LFlags.VARIABLE_LF, id10);
        sc6.addDeclaration (c6j);


        try {
            matches = sc6.lookup (id1, Cpp_LFlags.EMPTY_LF);
            resetNS (); assertTrue (matches.getSingleMember () == ga);

            matches = sc6.lookup (id2, Cpp_LFlags.EMPTY_LF);
            resetNS (); assertTrue (matches.getSingleMember () == nb);

            matches = sc6.lookup (id3, Cpp_LFlags.EMPTY_LF);
            resetNS (); assertTrue (matches.getSingleMember () == c5c);

            matches = sc6.lookup (id4, Cpp_LFlags.EMPTY_LF);
            resetNS (); assertTrue (matches.getSingleMember () == c5d);

            matches = sc6.lookup (id5, Cpp_LFlags.EMPTY_LF);
            resetNS (); assertTrue (matches.getSingleMember () == ne);

            matches = sc6.lookup (id6, Cpp_LFlags.EMPTY_LF);
            resetNS (); assertTrue (matches.getSingleMember () == c5f);

            matches = sc6.lookup (id7, Cpp_LFlags.EMPTY_LF);
            resetNS (); assertTrue (matches.getSingleMember () == c6g);

            matches = sc6.lookup (id8, Cpp_LFlags.EMPTY_LF);
            resetNS (); assertTrue (matches.getSingleMember () == c6h);

            matches = sc6.lookup (id9, Cpp_LFlags.EMPTY_LF);
            resetNS (); assertTrue (matches.getSingleMember () == c6i);

            matches = sc6.lookup (id10, Cpp_LFlags.EMPTY_LF);
            resetNS (); assertTrue (matches.getSingleMember () == c6j);

        } catch (UndefinedSymbolException use) {
            use.printStackTrace ();
        }

    }

    /**
     *<pre>
     * int a, b, c, g, k, p;
     * class C1 {
     *   public:
     *   int k, l, m, n, o, q;
     * };
     * namespace NS {
     *   int b, d, e, h, m, r;
     *   class C3 {
     *     public:
     *     int c, d, f, i, n, s;
     *   };
     *   class C4 : public C3 {
     *     public:
     *     int g, h, i, j, o, t;
     *   };
     *   class C7 : public C4, public C1 {
     *     int p, q, r, s, t;
     *     ...ids a-t referenced here...
     *   };
     * }
     * </pre>
     */

    public void testLookupC7 () {
        DeclarationSet matches;
        Declaration match;

        // need some more var ids

        // add vars to global scope
        Declaration ga = new Declaration (Cpp_LFlags.VARIABLE_LF, ida);
        sglobal.addDeclaration (ga);

        Declaration gb = new Declaration (Cpp_LFlags.VARIABLE_LF, idb);
        sglobal.addDeclaration (gb);

        Declaration gc = new Declaration (Cpp_LFlags.VARIABLE_LF, idc);
        sglobal.addDeclaration (gc);

        Declaration gg = new Declaration (Cpp_LFlags.VARIABLE_LF, idg);
        sglobal.addDeclaration (gg);

        Declaration gk = new Declaration (Cpp_LFlags.VARIABLE_LF, idk);
        sglobal.addDeclaration (gk);

        Declaration gp = new Declaration (Cpp_LFlags.VARIABLE_LF, idp);
        sglobal.addDeclaration (gp);

        // add vars to C1 scope
        Declaration c1k = new Declaration (Cpp_LFlags.VARIABLE_LF, idk);
        sc1.addDeclaration (c1k);

        Declaration c1l = new Declaration (Cpp_LFlags.VARIABLE_LF, idl);
        sc1.addDeclaration (c1l);

        Declaration c1m = new Declaration (Cpp_LFlags.VARIABLE_LF, idm);
        sc1.addDeclaration (c1m);

        Declaration c1n = new Declaration (Cpp_LFlags.VARIABLE_LF, idn);
        sc1.addDeclaration (c1n);

        Declaration c1o = new Declaration (Cpp_LFlags.VARIABLE_LF, ido);
        sc1.addDeclaration (c1o);

        Declaration c1q = new Declaration (Cpp_LFlags.VARIABLE_LF, idq);
        sc1.addDeclaration (c1q);


        // add vars to ns scope
        Declaration nb = new Declaration (Cpp_LFlags.VARIABLE_LF, idb);
        sns.addDeclaration (nb);

        Declaration nd = new Declaration (Cpp_LFlags.VARIABLE_LF, idd);
        sns.addDeclaration (nd);

        Declaration ne = new Declaration (Cpp_LFlags.VARIABLE_LF, ide);
        sns.addDeclaration (ne);

        Declaration nh = new Declaration (Cpp_LFlags.VARIABLE_LF, idh);
        sns.addDeclaration (nh);

        Declaration nm = new Declaration (Cpp_LFlags.VARIABLE_LF, idm);
        sns.addDeclaration (nm);

        Declaration nr = new Declaration (Cpp_LFlags.VARIABLE_LF, idr);
        sns.addDeclaration (nr);


        // add vars to C3 scope
        Declaration c3c = new Declaration (Cpp_LFlags.VARIABLE_LF, idc);
        sc3.addDeclaration (c3c);

        Declaration c3d = new Declaration (Cpp_LFlags.VARIABLE_LF, idd);
        sc3.addDeclaration (c3d);

        Declaration c3f = new Declaration (Cpp_LFlags.VARIABLE_LF, idf);
        sc3.addDeclaration (c3f);

        Declaration c3i = new Declaration (Cpp_LFlags.VARIABLE_LF, idi);
        sc3.addDeclaration (c3i);

        Declaration c3n = new Declaration (Cpp_LFlags.VARIABLE_LF, idn);
        sc3.addDeclaration (c3n);

        Declaration c3s = new Declaration (Cpp_LFlags.VARIABLE_LF, ids);
        sc3.addDeclaration (c3s);

        // add to C4 scope
        Declaration c4g = new Declaration (Cpp_LFlags.VARIABLE_LF, idg);
        sc4.addDeclaration (c4g);

        Declaration c4h = new Declaration (Cpp_LFlags.VARIABLE_LF, idh);
        sc4.addDeclaration (c4h);

        Declaration c4i = new Declaration (Cpp_LFlags.VARIABLE_LF, idi);
        sc4.addDeclaration (c4i);

        Declaration c4j = new Declaration (Cpp_LFlags.VARIABLE_LF, idj);
        sc4.addDeclaration (c4j);

        Declaration c4o = new Declaration (Cpp_LFlags.VARIABLE_LF, ido);
        sc4.addDeclaration (c4o);

        Declaration c4t = new Declaration (Cpp_LFlags.VARIABLE_LF, idt);
        sc4.addDeclaration (c4t);

        // add to C7 scope
        Declaration c7p = new Declaration (Cpp_LFlags.VARIABLE_LF, idp);
        sc7.addDeclaration (c7p);

        Declaration c7q = new Declaration (Cpp_LFlags.VARIABLE_LF, idq);
        sc7.addDeclaration (c7q);

        Declaration c7r = new Declaration (Cpp_LFlags.VARIABLE_LF, idr);
        sc7.addDeclaration (c7r);

        Declaration c7s = new Declaration (Cpp_LFlags.VARIABLE_LF, ids);
        sc7.addDeclaration (c7s);

        Declaration c7t = new Declaration (Cpp_LFlags.VARIABLE_LF, idt);
        sc7.addDeclaration (c7t);


        try {
            matches = sc7.lookup (ida, Cpp_LFlags.EMPTY_LF);
            resetNS (); assertTrue (matches.getSingleMember () == ga);

            matches = sc7.lookup (idb, Cpp_LFlags.EMPTY_LF);
            resetNS (); assertTrue (matches.getSingleMember () == nb);

            matches = sc7.lookup (idc, Cpp_LFlags.EMPTY_LF);
            resetNS (); assertTrue (matches.getSingleMember () == c3c);

            matches = sc7.lookup (idd, Cpp_LFlags.EMPTY_LF);
            resetNS (); assertTrue (matches.getSingleMember () == c3d);

            matches = sc7.lookup (ide, Cpp_LFlags.EMPTY_LF);
            resetNS (); assertTrue (matches.getSingleMember () == ne);

            matches = sc7.lookup (idf, Cpp_LFlags.EMPTY_LF);
            resetNS (); assertTrue (matches.getSingleMember () == c3f);

            matches = sc7.lookup (idg, Cpp_LFlags.EMPTY_LF);
            resetNS (); assertTrue (matches.getSingleMember () == c4g);

            matches = sc7.lookup (idh, Cpp_LFlags.EMPTY_LF);
            resetNS (); assertTrue (matches.getSingleMember () == c4h);

            matches = sc7.lookup (idi, Cpp_LFlags.EMPTY_LF);
            resetNS (); assertTrue (matches.getSingleMember () == c4i);

            matches = sc7.lookup (idj, Cpp_LFlags.EMPTY_LF);
            resetNS (); assertTrue (matches.getSingleMember () == c4j);

            matches = sc7.lookup (idk, Cpp_LFlags.EMPTY_LF);
            resetNS (); assertTrue (matches.getSingleMember () == c1k);

            matches = sc7.lookup (idl, Cpp_LFlags.EMPTY_LF);
            resetNS (); assertTrue (matches.getSingleMember () == c1l);

            matches = sc7.lookup (idm, Cpp_LFlags.EMPTY_LF);
            resetNS (); assertTrue (matches.getSingleMember () == c1m);

            matches = sc7.lookup (idn, Cpp_LFlags.EMPTY_LF);
            resetNS (); assertTrue (matches.getSingleMember () == null);

            matches = sc7.lookup (ido, Cpp_LFlags.EMPTY_LF);
            resetNS (); assertTrue (matches.getSingleMember () == null);

            matches = sc7.lookup (idp, Cpp_LFlags.EMPTY_LF);
            resetNS (); assertTrue (matches.getSingleMember () == c7p);

            matches = sc7.lookup (idq, Cpp_LFlags.EMPTY_LF);
            resetNS (); assertTrue (matches.getSingleMember () == c7q);

            matches = sc7.lookup (idr, Cpp_LFlags.EMPTY_LF);
            resetNS (); assertTrue (matches.getSingleMember () == c7r);

            matches = sc7.lookup (ids, Cpp_LFlags.EMPTY_LF);
            resetNS (); assertTrue (matches.getSingleMember () == c7s);

            matches = sc7.lookup (idt, Cpp_LFlags.EMPTY_LF);
            resetNS (); assertTrue (matches.getSingleMember () == c7t);

        } catch (UndefinedSymbolException use) {
            use.printStackTrace ();
        }

    }

    /**
     *<pre>
     * int a, b, c, g, k;
     * namespace NS {
     *   int b, d, e, h, l;
     *   class C3 {
     *     public:
     *     int c, d, f, i, m;
     *   };
     *   class C4 : public C3 {
     *     int g, h, i, j, n;
     *   };
     * }
     * class C8 : public NS::C4 {
     *   int k, l, m, n, o;
     *   ...use a--0...
     * };
     * </pre>
     */

    public void testLookupC8 () {
        DeclarationSet matches;
        Declaration match;

        // add five vars to global scope
        Declaration ga = new Declaration (Cpp_LFlags.VARIABLE_LF, ida);
        sglobal.addDeclaration (ga);

        Declaration gb = new Declaration (Cpp_LFlags.VARIABLE_LF, idb);
        sglobal.addDeclaration (gb);

        Declaration gc = new Declaration (Cpp_LFlags.VARIABLE_LF, idc);
        sglobal.addDeclaration (gc);

        Declaration gg = new Declaration (Cpp_LFlags.VARIABLE_LF, idg);
        sglobal.addDeclaration (gg);

        Declaration gk = new Declaration (Cpp_LFlags.VARIABLE_LF, idk);
        sglobal.addDeclaration (gk);

        // add five vars to ns scope
        Declaration nb = new Declaration (Cpp_LFlags.VARIABLE_LF, idb);
        sns.addDeclaration (nb);

        Declaration nd = new Declaration (Cpp_LFlags.VARIABLE_LF, idd);
        sns.addDeclaration (nd);

        Declaration ne = new Declaration (Cpp_LFlags.VARIABLE_LF, ide);
        sns.addDeclaration (ne);

        Declaration nh = new Declaration (Cpp_LFlags.VARIABLE_LF, idh);
        sns.addDeclaration (nh);

        Declaration nl = new Declaration (Cpp_LFlags.VARIABLE_LF, idl);
        sns.addDeclaration (nl);

        // add five vars to C3 scope
        Declaration c3c = new Declaration (Cpp_LFlags.VARIABLE_LF, idc);
        sc3.addDeclaration (c3c);

        Declaration c3d = new Declaration (Cpp_LFlags.VARIABLE_LF, idd);
        sc3.addDeclaration (c3d);

        Declaration c3f = new Declaration (Cpp_LFlags.VARIABLE_LF, idf);
        sc3.addDeclaration (c3f);

        Declaration c3i = new Declaration (Cpp_LFlags.VARIABLE_LF, idi);
        sc3.addDeclaration (c3i);

        Declaration c3m = new Declaration (Cpp_LFlags.VARIABLE_LF, idm);
        sc3.addDeclaration (c3m);

        // add five vars to C4 scope
        Declaration c4g = new Declaration (Cpp_LFlags.VARIABLE_LF, idg);
        sc4.addDeclaration (c4g);

        Declaration c4h = new Declaration (Cpp_LFlags.VARIABLE_LF, idh);
        sc4.addDeclaration (c4h);

        Declaration c4i = new Declaration (Cpp_LFlags.VARIABLE_LF, idi);
        sc4.addDeclaration (c4i);

        Declaration c4j = new Declaration (Cpp_LFlags.VARIABLE_LF, idj);
        sc4.addDeclaration (c4j);

        Declaration c4n = new Declaration (Cpp_LFlags.VARIABLE_LF, idn);
        sc4.addDeclaration (c4n);

        // add five vars to C8 scope
        Declaration c8k = new Declaration (Cpp_LFlags.VARIABLE_LF, idk);
        sc8.addDeclaration (c8k);

        Declaration c8l = new Declaration (Cpp_LFlags.VARIABLE_LF, idl);
        sc8.addDeclaration (c8l);

        Declaration c8m = new Declaration (Cpp_LFlags.VARIABLE_LF, idm);
        sc8.addDeclaration (c8m);

        Declaration c8n = new Declaration (Cpp_LFlags.VARIABLE_LF, idn);
        sc8.addDeclaration (c8n);

        Declaration c8o = new Declaration (Cpp_LFlags.VARIABLE_LF, ido);
        sc8.addDeclaration (c8o);


        try {
            matches = sc8.lookup (ida, Cpp_LFlags.EMPTY_LF);
            resetNS (); assertTrue (matches.getSingleMember () == ga);

            matches = sc8.lookup (idb, Cpp_LFlags.EMPTY_LF);
            resetNS (); assertTrue (matches.getSingleMember () == gb);

            matches = sc8.lookup (idc, Cpp_LFlags.EMPTY_LF);
            resetNS (); assertTrue (matches.getSingleMember () == c3c);

            matches = sc8.lookup (idd, Cpp_LFlags.EMPTY_LF);
            resetNS (); assertTrue (matches.getSingleMember () == c3d);

            matches = sc8.lookup (ide, Cpp_LFlags.EMPTY_LF);
            resetNS (); assertTrue (matches.getSingleMember () == null || matches.getSingleMember () == null);

            matches = sc8.lookup (idf, Cpp_LFlags.EMPTY_LF);
            resetNS (); assertTrue (matches.getSingleMember () == c3f);

            matches = sc8.lookup (idg, Cpp_LFlags.EMPTY_LF);
            resetNS (); assertTrue (matches.getSingleMember () == c4g);

            matches = sc8.lookup (idh, Cpp_LFlags.EMPTY_LF);
            resetNS (); assertTrue (matches.getSingleMember () == c4h);

            matches = sc8.lookup (idi, Cpp_LFlags.EMPTY_LF);
            resetNS (); assertTrue (matches.getSingleMember () == c4i);

            matches = sc8.lookup (idj, Cpp_LFlags.EMPTY_LF);
            resetNS (); assertTrue (matches.getSingleMember () == c4j);

            matches = sc8.lookup (idk, Cpp_LFlags.EMPTY_LF);
            resetNS (); assertTrue (matches.getSingleMember () == c8k);

            matches = sc8.lookup (idl, Cpp_LFlags.EMPTY_LF);
            resetNS (); assertTrue (matches.getSingleMember () == c8l);

            matches = sc8.lookup (idm, Cpp_LFlags.EMPTY_LF);
            resetNS (); assertTrue (matches.getSingleMember () == c8m);

            matches = sc8.lookup (idn, Cpp_LFlags.EMPTY_LF);
            resetNS (); assertTrue (matches.getSingleMember () == c8n);

            matches = sc8.lookup (ido, Cpp_LFlags.EMPTY_LF);
            resetNS (); assertTrue (matches.getSingleMember () == c8o);

        } catch (UndefinedSymbolException use) {
            use.printStackTrace ();
        }

    }

    /**
     *<pre>
     * int a, b, c, g;
     * class C9 {
     *   public:
     *   int b, d, e, h;
     *   void f1 () {
     *     int c, d, f, i;
     *     class C10 {
     *       int g, h, i, j;
     *       ...ids a-j referenced here...
     *     };
     *   }
     * };
     * </pre>
     */

    public void testLookupC10 () {
        DeclarationSet matches;
        Declaration match;

        // need some more var ids

        // add vars to global scope
        Declaration ga = new Declaration (Cpp_LFlags.VARIABLE_LF, ida);
        sglobal.addDeclaration (ga);

        Declaration gb = new Declaration (Cpp_LFlags.VARIABLE_LF, idb);
        sglobal.addDeclaration (gb);

        Declaration gc = new Declaration (Cpp_LFlags.VARIABLE_LF, idc);
        sglobal.addDeclaration (gc);

        Declaration gg = new Declaration (Cpp_LFlags.VARIABLE_LF, idg);
        sglobal.addDeclaration (gg);

        // add vars to c9 scope
        Declaration c9b = new Declaration (Cpp_LFlags.VARIABLE_LF, idb);
        sc9.addDeclaration (c9b);

        Declaration c9d = new Declaration (Cpp_LFlags.VARIABLE_LF, idd);
        sc9.addDeclaration (c9d);

        Declaration c9e = new Declaration (Cpp_LFlags.VARIABLE_LF, ide);
        sc9.addDeclaration (c9e);

        Declaration c9h = new Declaration (Cpp_LFlags.VARIABLE_LF, idh);
        sc9.addDeclaration (c9h);


        // add vars to fn scope
        Declaration fnc = new Declaration (Cpp_LFlags.VARIABLE_LF, idc);
        sfn.addDeclaration (fnc);

        Declaration fnd = new Declaration (Cpp_LFlags.VARIABLE_LF, idd);
        sfn.addDeclaration (fnd);

        Declaration fnf = new Declaration (Cpp_LFlags.VARIABLE_LF, idf);
        sfn.addDeclaration (fnf);

        Declaration fni = new Declaration (Cpp_LFlags.VARIABLE_LF, idi);
        sfn.addDeclaration (fni);

        // add to C10 scope
        Declaration c10g = new Declaration (Cpp_LFlags.VARIABLE_LF, idg);
        sc10.addDeclaration (c10g);

        Declaration c10h = new Declaration (Cpp_LFlags.VARIABLE_LF, idh);
        sc10.addDeclaration (c10h);

        Declaration c10i = new Declaration (Cpp_LFlags.VARIABLE_LF, idi);
        sc10.addDeclaration (c10i);

        Declaration c10j = new Declaration (Cpp_LFlags.VARIABLE_LF, idj);
        sc10.addDeclaration (c10j);

        try {
            matches = sc10.lookup (ida, Cpp_LFlags.EMPTY_LF);
            resetNS (); assertTrue (matches.getSingleMember () == ga);

            matches = sc10.lookup (idb, Cpp_LFlags.EMPTY_LF);
            resetNS (); assertTrue (matches.getSingleMember () == c9b);

            matches = sc10.lookup (idc, Cpp_LFlags.EMPTY_LF);
            resetNS (); assertTrue (matches.getSingleMember () == fnc);

            matches = sc10.lookup (idd, Cpp_LFlags.EMPTY_LF);
            resetNS (); assertTrue (matches.getSingleMember () == fnd);

            matches = sc10.lookup (ide, Cpp_LFlags.EMPTY_LF);
            resetNS (); assertTrue (matches.getSingleMember () == c9e);

            matches = sc10.lookup (idf, Cpp_LFlags.EMPTY_LF);
            resetNS (); assertTrue (matches.getSingleMember () == fnf);

            matches = sc10.lookup (idg, Cpp_LFlags.EMPTY_LF);
            resetNS (); assertTrue (matches.getSingleMember () == c10g);

            matches = sc10.lookup (idh, Cpp_LFlags.EMPTY_LF);
            resetNS (); assertTrue (matches.getSingleMember () == c10h);

            matches = sc10.lookup (idi, Cpp_LFlags.EMPTY_LF);
            resetNS (); assertTrue (matches.getSingleMember () == c10i);

            matches = sc10.lookup (idj, Cpp_LFlags.EMPTY_LF);
            resetNS (); assertTrue (matches.getSingleMember () == c10j);

        } catch (UndefinedSymbolException use) {
            use.printStackTrace ();
        }

    }


    /**
     *<pre>
     * int a, b, c, g;
     * class C1 {
     *   public:
     *   int b, d, e, h;
     * };
     * class C11 : public C1 {
     *   public:
     *   int c, d, f, i;
     *   class C12 {
     *     int g, h, i, j;
     *     ...a--j...
     *   };
     * };
     * </pre>
     */

    public void testLookupC12 () {
        DeclarationSet matches;
        Declaration match;


        // add four vars to global scope
        Declaration ga = new Declaration (Cpp_LFlags.VARIABLE_LF, ida);
        sglobal.addDeclaration (ga);

        Declaration gb = new Declaration (Cpp_LFlags.VARIABLE_LF, idb);
        sglobal.addDeclaration (gb);

        Declaration gc = new Declaration (Cpp_LFlags.VARIABLE_LF, idc);
        sglobal.addDeclaration (gc);

        Declaration gg = new Declaration (Cpp_LFlags.VARIABLE_LF, idg);
        sglobal.addDeclaration (gg);

        // add four vars to C1 scope
        Declaration c1b = new Declaration (Cpp_LFlags.VARIABLE_LF, idb);
        sc1.addDeclaration (c1b);

        Declaration c1d = new Declaration (Cpp_LFlags.VARIABLE_LF, idd);
        sc1.addDeclaration (c1d);

        Declaration c1e = new Declaration (Cpp_LFlags.VARIABLE_LF, ide);
        sc1.addDeclaration (c1e);

        Declaration c1h = new Declaration (Cpp_LFlags.VARIABLE_LF, idh);
        sc1.addDeclaration (c1h);

        // add four vars to C11 scope
        Declaration c11c = new Declaration (Cpp_LFlags.VARIABLE_LF, idc);
        sc11.addDeclaration (c11c);

        Declaration c11d = new Declaration (Cpp_LFlags.VARIABLE_LF, idd);
        sc11.addDeclaration (c11d);

        Declaration c11f = new Declaration (Cpp_LFlags.VARIABLE_LF, idf);
        sc11.addDeclaration (c11f);

        Declaration c11i = new Declaration (Cpp_LFlags.VARIABLE_LF, idi);
        sc11.addDeclaration (c11i);

        // add four vars to C12 scope
        Declaration c12g = new Declaration (Cpp_LFlags.VARIABLE_LF, idg);
        sc12.addDeclaration (c12g);

        Declaration c12h = new Declaration (Cpp_LFlags.VARIABLE_LF, idh);
        sc12.addDeclaration (c12h);

        Declaration c12i = new Declaration (Cpp_LFlags.VARIABLE_LF, idi);
        sc12.addDeclaration (c12i);

        Declaration c12j = new Declaration (Cpp_LFlags.VARIABLE_LF, idj);
        sc12.addDeclaration (c12j);


        try {
            matches = sc12.lookup (ida, Cpp_LFlags.EMPTY_LF);
            resetNS (); assertTrue (matches.getSingleMember () == ga);

            matches = sc12.lookup (idb, Cpp_LFlags.EMPTY_LF);
            resetNS (); assertTrue (matches.getSingleMember () == c1b);

            matches = sc12.lookup (idc, Cpp_LFlags.EMPTY_LF);
            resetNS (); assertTrue (matches.getSingleMember () == c11c);

            matches = sc12.lookup (idd, Cpp_LFlags.EMPTY_LF);
            resetNS (); assertTrue (matches.getSingleMember () == c11d);

            matches = sc12.lookup (ide, Cpp_LFlags.EMPTY_LF);
            resetNS (); assertTrue (matches.getSingleMember () == c1e);

            matches = sc12.lookup (idf, Cpp_LFlags.EMPTY_LF);
            resetNS (); assertTrue (matches.getSingleMember () == c11f);

            matches = sc12.lookup (idg, Cpp_LFlags.EMPTY_LF);
            resetNS (); assertTrue (matches.getSingleMember () == c12g);

            matches = sc12.lookup (idh, Cpp_LFlags.EMPTY_LF);
            resetNS (); assertTrue (matches.getSingleMember () == c12h);

            matches = sc12.lookup (idi, Cpp_LFlags.EMPTY_LF);
            resetNS (); assertTrue (matches.getSingleMember () == c12i);

            matches = sc12.lookup (idj, Cpp_LFlags.EMPTY_LF);
            resetNS (); assertTrue (matches.getSingleMember () == c12j);

        } catch (UndefinedSymbolException use) {
            use.printStackTrace ();
        }

    }


}
