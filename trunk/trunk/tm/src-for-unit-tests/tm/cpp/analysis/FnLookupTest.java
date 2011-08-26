package tm.cpp.analysis;

import java.util.Vector ;

import tm.clc.analysis.*;
import tm.clc.ast.NodeList;
import tm.cpp.ast.TyFun;
import tm.cpp.ast.TyInt;

import junit.framework.*;

/**
 * tests lookup in the following set of scopes:
 * <br><pre>
 * void f1 () { }
 * namespace N {
 *   void f1 () { }
 *   void f2 () ;
 *   void f3 () ;
 *   class C {
 *     void f1 () { }
 *     void f2 () ;
 *     friend void f3 () { }
 *   };
 *   void C::f2 () { }
 * }
 * void N::f2 () { }
 *</pre>
 */

public class FnLookupTest extends LookupTest {

    FunctionSH sgrfn, snrfn, snerfn, snifrfn; // regular functions
    Declaration dgrfn, dnrfn, dnerfn, dnifrfn;

    FunctionSH smfn, semfn; // member functions
    Declaration dmfn, demfn;

    NamespaceSH sglobal, sns; // namespaces
    Declaration dns;

    ClassSH sclass; // classes
    Declaration dclass;

    ScopedName id1;
    Declaration var1;

    public FnLookupTest () { super ("FnLookupTest"); }
    public FnLookupTest (String name) { super (name); }

    protected void setUp () {
        TyFun dummyTyFun = new TyFun (new Vector (), false);
        dummyTyFun.addToEnd (TyInt.get ());
        Cpp_SpecifierSet dummySp = new Cpp_SpecifierSet ();
        // namespaces
        // global
        sglobal = new NamespaceSH ();
        allNamespaces.addElement (sglobal);
        // namespace
        sns = new NamespaceSH (sglobal);
        dns =
            new Declaration (Cpp_LFlags.NAMESPACE_LF, new Cpp_ScopedName (NSID));
        dns.setDefinition (sns);
        allNamespaces.addElement (sns);

        // classes
        sclass = new ClassSH (sns);
        dclass =
            new Declaration (Cpp_LFlags.CLASS_LF, new Cpp_ScopedName (CLASSID));
        dclass.setDefinition (sclass);

        // functions
        // regular functions
        // global function
        sgrfn = new RegularFnSH (sglobal);
        dgrfn =
            new FunctionDeclaration (Cpp_LFlags.REG_FN_LF,
                                     new Cpp_ScopedName (F1), sgrfn, dummySp,
                                     dummyTyFun);
        sgrfn.addOwnDeclaration (dgrfn);

        // namespace member
        snrfn = new RegularFnSH (sns);
        dnrfn =
            new FunctionDeclaration (Cpp_LFlags.REG_FN_LF,
                                     new Cpp_ScopedName (F1), snrfn, dummySp,
                                     dummyTyFun);
        snrfn.addOwnDeclaration (dnrfn);

        // namespace member, defined outside ns
        snerfn = new RegularFnSH (sglobal);
        snerfn.setOwningScope (sns);
        dnerfn =
            new FunctionDeclaration (Cpp_LFlags.REG_FN_LF,
                                     new Cpp_ScopedName (F2), snerfn, dummySp,
                                     dummyTyFun);
        snerfn.addOwnDeclaration (dnerfn);

        // namespace member, inline friend
        snifrfn = new RegularFnSH (sclass);
        snifrfn.setOwningScope (sns);
        snifrfn.addFriend (sclass);
        dnifrfn =
            new FunctionDeclaration (Cpp_LFlags.REG_FN_LF,
                                     new Cpp_ScopedName (F3), snifrfn, dummySp,
                                     dummyTyFun);
        snifrfn.addOwnDeclaration (dnifrfn);

        // member functions
        // defined inline
        smfn = new MemberFnSH (sclass);
        dmfn =
            new FunctionDeclaration (Cpp_LFlags.MEM_FN_LF,
                                     new Cpp_ScopedName (F1), smfn, dummySp,
                                     dummyTyFun);
        smfn.addOwnDeclaration (dmfn);

        // defined outside
        semfn = new MemberFnSH (sns);
        ((FunctionSH) semfn).setOwningScope (sclass);
        demfn =
            new FunctionDeclaration (Cpp_LFlags.MEM_FN_LF,
                                     new Cpp_ScopedName (F2), semfn, dummySp,
                                     dummyTyFun);
        semfn.addOwnDeclaration (demfn);
        id1 = new Cpp_ScopedName (VARID);
        var1 = new Declaration (Cpp_LFlags.VARIABLE_LF, id1);
        var1.setDefinition (var1);

    }

    public void testAddDeclarations () {
    }

    public void testLookupNothing () {
        // the fun stuff..
        DeclarationSet matches;
        Declaration match;

        testAddDeclarations ();

        try {
            matches = sgrfn.lookup (id1, Cpp_LFlags.EMPTY_LF);
            resetNS (); assertTrue (matches.isEmpty ());

            matches = snrfn.lookup (id1, Cpp_LFlags.EMPTY_LF);
            resetNS (); assertTrue (matches.isEmpty ());

            matches = snerfn.lookup (id1, Cpp_LFlags.EMPTY_LF);
            resetNS (); assertTrue (matches.isEmpty ());

            matches = snifrfn.lookup (id1, Cpp_LFlags.EMPTY_LF);
            resetNS (); assertTrue (matches.isEmpty ());

            matches = smfn.lookup (id1, Cpp_LFlags.EMPTY_LF);
            resetNS (); assertTrue (matches.isEmpty ());

            matches = semfn.lookup (id1, Cpp_LFlags.EMPTY_LF);
            resetNS (); assertTrue (matches.isEmpty ());

        } catch (UndefinedSymbolException use) {
            use.printStackTrace ();
        }
    }

    public void testLookupGlobal () {
        // the fun stuff..
        DeclarationSet matches;
        Declaration match;

        testAddDeclarations ();

        sglobal.addDeclaration (var1);

        try {
            matches = sgrfn.lookup (id1, Cpp_LFlags.EMPTY_LF);
            resetNS (); assertTrue (matches.getSingleMember () == var1);

            matches = snrfn.lookup (id1, Cpp_LFlags.EMPTY_LF);
            resetNS (); assertTrue (matches.getSingleMember () == var1);

            matches = snerfn.lookup (id1, Cpp_LFlags.EMPTY_LF);
            resetNS (); assertTrue (matches.getSingleMember () == var1);

            matches = snifrfn.lookup (id1, Cpp_LFlags.EMPTY_LF);
            resetNS (); assertTrue (matches.getSingleMember () == var1);

            matches = smfn.lookup (id1, Cpp_LFlags.EMPTY_LF);
            resetNS (); assertTrue (matches.getSingleMember () == var1);

            matches = semfn.lookup (id1, Cpp_LFlags.EMPTY_LF);
            resetNS (); assertTrue (matches.getSingleMember () == var1);

        } catch (UndefinedSymbolException use) {
            use.printStackTrace ();
        }
    }

    /**
     * <pre>
     * int a;
     * int b;
     * void f1 () {
     *   int a;
     *   int c;
     *   if (true) {
     *     int c;
     *     ...b...
     *     ...a...
     *     ...c...
     *   }
     *   ...a...
     *   ...b...
     *   ...c...
     * }
     * </pre>
     */
    public void testLookupGF1 () {
        DeclarationSet matches;
        Declaration match;

        testAddDeclarations ();

        // need a couple more var ids
        ScopedName id2 = new Cpp_ScopedName (A1);
        ScopedName id3 = new Cpp_ScopedName (A2);

        // add var1 to global scope
        Declaration ga = var1;
        sglobal.addDeclaration (ga);

        // redefinition of id1 in function's outer scope
        Declaration fa = new Declaration (Cpp_LFlags.VARIABLE_LF, id1);
        sgrfn.addDeclaration (fa);

        // another global var
        Declaration gb = new Declaration (Cpp_LFlags.VARIABLE_LF, id2);
        sglobal.addDeclaration (gb);

        // another var in function's outer scope
        Declaration fc = new Declaration (Cpp_LFlags.VARIABLE_LF, id3);
        sgrfn.addDeclaration (fc);

        // an inner block in the function
        BlockSH sb = new BlockSH (sgrfn);

        // redefinition of id3 in inner block
        Declaration bc = new Declaration (Cpp_LFlags.VARIABLE_LF, id3);
        sb.addDeclaration (bc);

        try {

            // look for a, b and c in fn outer block
            matches = sgrfn.lookup (id1, Cpp_LFlags.EMPTY_LF);
            resetNS (); assertTrue (matches.getSingleMember () == fa);

            matches = sgrfn.lookup (id2, Cpp_LFlags.EMPTY_LF);
            resetNS (); assertTrue (matches.getSingleMember () == gb);

            matches = sgrfn.lookup (id3, Cpp_LFlags.EMPTY_LF);
            resetNS (); assertTrue (matches.getSingleMember () == fc);

            // look for a, b, and c in inner block
            matches = sb.lookup (id1, Cpp_LFlags.EMPTY_LF);
            resetNS (); assertTrue (matches.getSingleMember () == fa);

            matches = sb.lookup (id2, Cpp_LFlags.EMPTY_LF);
            resetNS (); assertTrue (matches.getSingleMember () == gb);

            matches = sb.lookup (id3, Cpp_LFlags.EMPTY_LF);
            resetNS (); assertTrue (matches.getSingleMember () == bc);

        } catch (UndefinedSymbolException use) {
            use.printStackTrace ();
        }
    }

    /**
     * <pre>
     * int a;
     * int b;
     * namespace N {
     *   int a;
     *   int d;
     *   void f1 () {
     *     int a;
     *     int c;
     *     if (true) {
     *       int c;
     *       ...b...
     *       ...a...
     *       ...c...
     *       ...d...
     *     }
     *     ...a...
     *     ...b...
     *     ...c...
     *     ...d...
     *   }
     *}
     * </pre>
     */
    public void testLookupNF1 () {
        DeclarationSet matches;
        Declaration match;

        testAddDeclarations ();

        // need a couple more var ids
        ScopedName id2 = new Cpp_ScopedName (A1);
        ScopedName id3 = new Cpp_ScopedName (A2);
        ScopedName id4 = new Cpp_ScopedName (A3);

        // add var1 to global scope
        Declaration ga = var1;
        sglobal.addDeclaration (ga);

        // another global var
        Declaration gb = new Declaration (Cpp_LFlags.VARIABLE_LF, id2);
        sglobal.addDeclaration (gb);

        // redefinition of id1 in namespace's outer scope
        Declaration na = new Declaration (Cpp_LFlags.VARIABLE_LF, id1);
        sns.addDeclaration (na);

        // another var in namespace's outer scope
        Declaration nd = new Declaration (Cpp_LFlags.VARIABLE_LF, id4);
        sns.addDeclaration (nd);

        // redefinition of id1 in function's outer scope
        Declaration fa = new Declaration (Cpp_LFlags.VARIABLE_LF, id1);
        snrfn.addDeclaration (fa);

        // another var in function's outer scope
        Declaration fc = new Declaration (Cpp_LFlags.VARIABLE_LF, id3);
        snrfn.addDeclaration (fc);

        // an inner block in the function
        BlockSH sb = new BlockSH (snrfn);

        // redefinition of id3 in inner block
        Declaration bc = new Declaration (Cpp_LFlags.VARIABLE_LF, id3);
        sb.addDeclaration (bc);

        try {

            // look for a, b, c and d in fn outer block
            matches = snrfn.lookup (id1, Cpp_LFlags.EMPTY_LF);
            resetNS (); assertTrue (matches.getSingleMember () == fa);

            matches = snrfn.lookup (id2, Cpp_LFlags.EMPTY_LF);
            resetNS (); assertTrue (matches.getSingleMember () == gb);

            matches = snrfn.lookup (id3, Cpp_LFlags.EMPTY_LF);
            resetNS (); assertTrue (matches.getSingleMember () == fc);

            matches = snrfn.lookup (id4, Cpp_LFlags.EMPTY_LF);
            resetNS (); assertTrue (matches.getSingleMember () == nd);

            // look for a, b, c and d in inner block
            matches = sb.lookup (id1, Cpp_LFlags.EMPTY_LF);
            resetNS (); assertTrue (matches.getSingleMember () == fa);

            matches = sb.lookup (id2, Cpp_LFlags.EMPTY_LF);
            resetNS (); assertTrue (matches.getSingleMember () == gb);

            matches = sb.lookup (id3, Cpp_LFlags.EMPTY_LF);
            resetNS (); assertTrue (matches.getSingleMember () == bc);

            matches = sb.lookup (id4, Cpp_LFlags.EMPTY_LF);
            resetNS (); assertTrue (matches.getSingleMember () == nd);

        } catch (UndefinedSymbolException use) {
            use.printStackTrace ();
        }
    }

    /**
     * <pre>
     * int a;
     * int b;
     * namespace N {
     *   int a;
     *   int d;
     *   void f2 () ;
     * }
     * void N::f2 () {
     *   int a;
     *   int c;
     *   if (true) {
     *     int c;
     *     ...b...
     *     ...a...
     *     ...c...
     *     ...d...
     *   }
     *   ...a...
     *   ...b...
     *   ...c...
     *   ...d...
     * }
     *}
     * </pre>
     */
    public void testLookupNF2 () {
        DeclarationSet matches;
        Declaration match;

        testAddDeclarations ();

        // need a couple more var ids
        ScopedName id2 = new Cpp_ScopedName (A1);
        ScopedName id3 = new Cpp_ScopedName (A2);
        ScopedName id4 = new Cpp_ScopedName (A3);

        // add var1 to global scope
        Declaration ga = var1;
        sglobal.addDeclaration (ga);

        // another global var
        Declaration gb = new Declaration (Cpp_LFlags.VARIABLE_LF, id2);
        sglobal.addDeclaration (gb);

        // redefinition of id1 in namespace's outer scope
        Declaration na = new Declaration (Cpp_LFlags.VARIABLE_LF, id1);
        sns.addDeclaration (na);

        // another var in namespace's outer scope
        Declaration nd = new Declaration (Cpp_LFlags.VARIABLE_LF, id4);
        sns.addDeclaration (nd);

        // redefinition of id1 in function's outer scope
        Declaration fa = new Declaration (Cpp_LFlags.VARIABLE_LF, id1);
        snerfn.addDeclaration (fa);

        // another var in function's outer scope
        Declaration fc = new Declaration (Cpp_LFlags.VARIABLE_LF, id3);
        snerfn.addDeclaration (fc);

        // an inner block in the function
        BlockSH sb = new BlockSH (snerfn);

        // redefinition of id3 in inner block
        Declaration bc = new Declaration (Cpp_LFlags.VARIABLE_LF, id3);
        sb.addDeclaration (bc);

        try {

            // look for a, b, c and d in fn outer block
            matches = snerfn.lookup (id1, Cpp_LFlags.EMPTY_LF);
            resetNS (); assertTrue (matches.getSingleMember () == fa);

            matches = snerfn.lookup (id2, Cpp_LFlags.EMPTY_LF);
            resetNS (); assertTrue (matches.getSingleMember () == gb);

            matches = snerfn.lookup (id3, Cpp_LFlags.EMPTY_LF);
            resetNS (); assertTrue (matches.getSingleMember () == fc);

            matches = snerfn.lookup (id4, Cpp_LFlags.EMPTY_LF);
            resetNS (); assertTrue (matches.getSingleMember () == nd);

            // look for a, b, c and d in inner block
            matches = sb.lookup (id1, Cpp_LFlags.EMPTY_LF);
            resetNS (); assertTrue (matches.getSingleMember () == fa);

            matches = sb.lookup (id2, Cpp_LFlags.EMPTY_LF);
            resetNS (); assertTrue (matches.getSingleMember () == gb);

            matches = sb.lookup (id3, Cpp_LFlags.EMPTY_LF);
            resetNS (); assertTrue (matches.getSingleMember () == bc);

            matches = sb.lookup (id4, Cpp_LFlags.EMPTY_LF);
            resetNS (); assertTrue (matches.getSingleMember () == nd);

        } catch (UndefinedSymbolException use) {
            use.printStackTrace ();
        }
    }

    /**
     * <pre>
     * int a, b;
     * namespace N {
     *   int a, d, e, f;
     *   void f3 () ;
     *   class C {
     *     int f, g, h;
     *     friend void f3 () {
     *       int a, c, d, h;
     *       ... use a--h...
     *     }
     *   }
     * }
     *}
     * </pre>
     */

    public void testLookupNF3 () {
        DeclarationSet matches;
        Declaration match;

        testAddDeclarations ();

        // need a couple more var ids
        ScopedName id2 = new Cpp_ScopedName (A1);
        ScopedName id3 = new Cpp_ScopedName (A2);
        ScopedName id4 = new Cpp_ScopedName (A3);
        ScopedName id5 = new Cpp_ScopedName (A4);
        ScopedName id6 = new Cpp_ScopedName (A5);
        ScopedName id7 = new Cpp_ScopedName (A6);
        ScopedName id8 = new Cpp_ScopedName (A7);

        // add var1 to global scope
        Declaration ga = var1;
        sglobal.addDeclaration (ga);

        // another global var
        Declaration gb = new Declaration (Cpp_LFlags.VARIABLE_LF, id2);
        sglobal.addDeclaration (gb);

        // redefinition of id1 in namespace's outer scope
        Declaration na = new Declaration (Cpp_LFlags.VARIABLE_LF, id1);
        sns.addDeclaration (na);

        // another var in namespace's outer scope
        Declaration nd = new Declaration (Cpp_LFlags.VARIABLE_LF, id4);
        sns.addDeclaration (nd);

        // another var in namespace's outer scope
        Declaration ne = new Declaration (Cpp_LFlags.VARIABLE_LF, id5);
        sns.addDeclaration (ne);

        // another var in namespace's outer scope
        Declaration nf = new Declaration (Cpp_LFlags.VARIABLE_LF, id6);
        sns.addDeclaration (nf);

        // three vars in class scope
        Declaration cf = new Declaration (Cpp_LFlags.VARIABLE_LF, id6);
        sclass.addDeclaration (cf);

        Declaration cg = new Declaration (Cpp_LFlags.VARIABLE_LF, id7);
        sclass.addDeclaration (cg);

        Declaration ch = new Declaration (Cpp_LFlags.VARIABLE_LF, id8);
        sclass.addDeclaration (ch);

        // 4 vars in fn scope
        Declaration fa = new Declaration (Cpp_LFlags.VARIABLE_LF, id1);
        snifrfn.addDeclaration (fa);

        Declaration fc = new Declaration (Cpp_LFlags.VARIABLE_LF, id3);
        snifrfn.addDeclaration (fc);

        Declaration fd = new Declaration (Cpp_LFlags.VARIABLE_LF, id4);
        snifrfn.addDeclaration (fd);

        Declaration fh = new Declaration (Cpp_LFlags.VARIABLE_LF, id8);
        snifrfn.addDeclaration (fh);

        try {

            // look for a, b, c, d, e, f, and h in fn outer block
            matches = snifrfn.lookup (id1, Cpp_LFlags.EMPTY_LF);
            resetNS (); assertTrue (matches.getSingleMember () == fa);

            matches = snifrfn.lookup (id2, Cpp_LFlags.EMPTY_LF);
            resetNS (); assertTrue (matches.getSingleMember () == gb);

            matches = snifrfn.lookup (id3, Cpp_LFlags.EMPTY_LF);
            resetNS (); assertTrue (matches.getSingleMember () == fc);

            matches = snifrfn.lookup (id4, Cpp_LFlags.EMPTY_LF);
            resetNS (); assertTrue (matches.getSingleMember () == fd);

            matches = snifrfn.lookup (id5, Cpp_LFlags.EMPTY_LF);
            resetNS (); assertTrue (matches.getSingleMember () == ne);

            matches = snifrfn.lookup (id6, Cpp_LFlags.EMPTY_LF);
            resetNS (); assertTrue (matches.getSingleMember () == cf);

            matches = snifrfn.lookup (id7, Cpp_LFlags.EMPTY_LF);
            resetNS (); assertTrue (matches.getSingleMember () == cg);

            matches = snifrfn.lookup (id8, Cpp_LFlags.EMPTY_LF);
            resetNS (); assertTrue (matches.getSingleMember () == fh);

        } catch (UndefinedSymbolException use) {
            use.printStackTrace ();
        }
    }


}
