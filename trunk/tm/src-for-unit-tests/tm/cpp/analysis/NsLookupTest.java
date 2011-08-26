package tm.cpp.analysis;

import tm.clc.analysis.*;

import junit.framework.*;

/**
 * tests lookup in the following set of scopes:
 * <br><pre>
 * namespace N1 { }
 * namespace N2 { 
 *   namespace N3 { }
 * }
 *</pre>
 */

public class NsLookupTest extends LookupTest {

    // namespaces
    NamespaceSH sglobal, sns1, sns2, sns3; // namespaces
    Declaration dns1, dns2, dns3; 

    ScopedName id1;
    Declaration var1;

    public NsLookupTest () { super ("NsLookupTest"); }
    public NsLookupTest (String name) { super (name); }

    protected void setUp () {
        // namespaces
        // global
        sglobal = new NamespaceSH ();
        allNamespaces.addElement (sglobal);
        // namespace defined in global scope
        sns1 = new NamespaceSH (sglobal);
        dns1 = new Declaration(Cpp_LFlags.NAMESPACE_LF, new Cpp_ScopedName(NS1));
        dns1.setDefinition (sns1);
        allNamespaces.addElement (sns1);
        // namespace defined in global scope, with inner namespace
        sns2 = new NamespaceSH (sglobal); 
        dns2 = new Declaration(Cpp_LFlags.NAMESPACE_LF, new Cpp_ScopedName(NS2));
        dns2.setDefinition (sns2);
        allNamespaces.addElement (sns2);
        // namespace defined inside another namespace
        sns3 = new NamespaceSH (sns2);
        dns3 = new Declaration(Cpp_LFlags.NAMESPACE_LF, new Cpp_ScopedName(NS3));
        dns3.setDefinition (sns3);
        allNamespaces.addElement (sns3);

        // this id will be used in tests
        id1 = new Cpp_ScopedName (VARID);

    }

    public void testAddDeclarations () {

    }

    public void testLookupNothing () {
        // the fun stuff..
        DeclarationSet matches;
        Declaration match;

        testAddDeclarations ();

        try {
            matches = sns1.lookup (id1, Cpp_LFlags.EMPTY_LF);
            resetNS (); assertTrue (matches == null || matches.isEmpty ());

            matches = sns2.lookup (id1, Cpp_LFlags.EMPTY_LF);
            resetNS (); assertTrue (matches == null || matches.isEmpty ());

            matches = sns3.lookup (id1, Cpp_LFlags.EMPTY_LF);
            resetNS (); assertTrue (matches == null || matches.isEmpty ());

        } catch (UndefinedSymbolException use) {
            use.printStackTrace ();
        }

    }

    public void testLookupGlobal () {
        // the fun stuff..
        DeclarationSet matches;
        Declaration match;

        testAddDeclarations ();

        // add a var to global scope
        var1 = new Declaration (Cpp_LFlags.VARIABLE_LF, id1);

        sglobal.addDeclaration (var1);
       
        try {
            matches = sns1.lookup (id1, Cpp_LFlags.EMPTY_LF);
            resetNS (); assertTrue (matches == var1);

            matches = sns2.lookup (id1, Cpp_LFlags.EMPTY_LF);
            resetNS (); assertTrue (matches == var1);

            matches = sns3.lookup (id1, Cpp_LFlags.EMPTY_LF);
            resetNS (); assertTrue (matches == var1);

        } catch (UndefinedSymbolException use) {
            use.printStackTrace ();
        }
        
    }

    /**
     *<pre>
     * int a;
     * int b;
     * namespace N1 {
     *   int b;
     *   int c;
     *   ...a...
     *   ...b...
     *   ...c...
     * }
     *</pre>
     */
    public void testLookupN1 () {
        DeclarationSet matches;
        Declaration match;

        testAddDeclarations ();

        // need a couple more var ids
        ScopedName id2 = new Cpp_ScopedName (A1);
        ScopedName id3 = new Cpp_ScopedName (A2);

        // add two vars to global scope
        Declaration ga = new Declaration (Cpp_LFlags.VARIABLE_LF, id1);
        sglobal.addDeclaration (ga);
       
        Declaration gb = new Declaration (Cpp_LFlags.VARIABLE_LF, id2);
        sglobal.addDeclaration (gb);
       
        // add two vars to ns scope
        Declaration nb = new Declaration (Cpp_LFlags.VARIABLE_LF, id2);
        sns1.addDeclaration (nb);
       
        Declaration nc = new Declaration (Cpp_LFlags.VARIABLE_LF, id3);
        sns1.addDeclaration (nc);
       
        
        try {
            matches = sns1.lookup (id1, Cpp_LFlags.EMPTY_LF);
            resetNS (); assertTrue (matches == ga);

            matches = sns1.lookup (id2, Cpp_LFlags.EMPTY_LF);
            resetNS (); assertTrue (matches == nb);

            matches = sns1.lookup (id3, Cpp_LFlags.EMPTY_LF);
            resetNS (); assertTrue (matches == nc);

        } catch (UndefinedSymbolException use) {
            use.printStackTrace ();
        }
        
    }

    /**
     *<pre>
     * int a;
     * int b;
     * int c;
     * namespace N2 {
     *   int b;
     *   int d;
     *   int e;
     *   namespace N3 { 
     *     int c;
     *     int d;
     *     int f;
     *     ...a...
     *     ...b...
     *     ...c...
     *     ...d...
     *     ...e...
     *     ...f...
     *   }
     *   ...a...
     *   ...b...
     *   ...c...
     *   ...d...
     *   ...e...
     * }
     *</pre>
     */
    public void testLookupN2N3 () {
        DeclarationSet matches;
        Declaration match;

        testAddDeclarations ();

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
       
        // add three vars to ns2 scope
        Declaration n2b = new Declaration (Cpp_LFlags.VARIABLE_LF, id2);
        sns2.addDeclaration (n2b);
       
        Declaration n2d = new Declaration (Cpp_LFlags.VARIABLE_LF, id4);
        sns2.addDeclaration (n2d);
       
        Declaration n2e = new Declaration (Cpp_LFlags.VARIABLE_LF, id5);
        sns2.addDeclaration (n2e);
       
        // add three vars to ns3 scope
        Declaration n3c = new Declaration (Cpp_LFlags.VARIABLE_LF, id3);
        sns3.addDeclaration (n3c);
       
        Declaration n3d = new Declaration (Cpp_LFlags.VARIABLE_LF, id4);
        sns3.addDeclaration (n3d);
       
        Declaration n3f = new Declaration (Cpp_LFlags.VARIABLE_LF, id6);
        sns3.addDeclaration (n3f);
       
        try {
            // N3
            matches = sns3.lookup (id1, Cpp_LFlags.EMPTY_LF);
            resetNS (); assertTrue (matches == ga);

            matches = sns3.lookup (id2, Cpp_LFlags.EMPTY_LF);
            resetNS (); assertTrue (matches == n2b);

            matches = sns3.lookup (id3, Cpp_LFlags.EMPTY_LF);
            resetNS (); assertTrue (matches == n3c);

            matches = sns3.lookup (id4, Cpp_LFlags.EMPTY_LF);
            resetNS (); assertTrue (matches == n3d);

            matches = sns3.lookup (id5, Cpp_LFlags.EMPTY_LF);
            resetNS (); assertTrue (matches == n2e);

            matches = sns3.lookup (id6, Cpp_LFlags.EMPTY_LF);
            resetNS (); assertTrue (matches == n3f);

            // N2
            matches = sns2.lookup (id1, Cpp_LFlags.EMPTY_LF);
            resetNS (); assertTrue (matches == ga);

            matches = sns2.lookup (id2, Cpp_LFlags.EMPTY_LF);
            resetNS (); assertTrue (matches == n2b);

            matches = sns2.lookup (id3, Cpp_LFlags.EMPTY_LF);
            resetNS (); assertTrue (matches == gc);

            matches = sns2.lookup (id4, Cpp_LFlags.EMPTY_LF);
            resetNS (); assertTrue (matches == n2d);

            matches = sns2.lookup (id5, Cpp_LFlags.EMPTY_LF);
            resetNS (); assertTrue (matches == n2e);

        } catch (UndefinedSymbolException use) {
            use.printStackTrace ();
        }
        
    }


}
