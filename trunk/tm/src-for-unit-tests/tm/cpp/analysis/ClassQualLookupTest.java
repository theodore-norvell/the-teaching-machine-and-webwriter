package tm.cpp.analysis;

import tm.clc.analysis.*;
import tm.utilities.Debug;

import junit.framework.*;

/**
 * tests qualified class lookup using the following set of scopes:
 * <br><pre>
 * class C1 { };
 * class C2 { };
 * class C3 { 
 *   class C1 { }; // called sc4 in the tests
 * };
 * class C5 : public C2 { };
 *</pre>
 */

public class ClassQualLookupTest extends TestCase 
    implements tm.cpp.analysis.TestConstantUser {

    Debug d = Debug.getInstance ();

    // namespaces
    NamespaceSH sglobal; // namespaces

    // classes
    ClassSH sc1, sc2, sc3, sc4, sc5;
    Declaration dc1, dc2, dc3, dc4, dc5;

    ScopedName id1, ida, idb, idc, idd, ide, idf, idg, idh, idi, idj, idk, idl,
        idm, idn, ido, idp, idq, idr, ids, idt;
    Declaration var1;

    // symbol table
    Cpp_CTSymbolTable symtab;

    public ClassQualLookupTest () { super ("ClassQualLookupTest"); }
    public ClassQualLookupTest (String name) { super (name); }

    protected void setUp () {
        Cpp_SpecifierSet spec_set = new Cpp_SpecifierSet ();
        symtab = new Cpp_CTSymbolTable ();
        symtab.enterFileScope ();

        // namespaces
        // global
        sglobal = symtab.getGlobalScope ();

        // classes
        // global class
        sc1 = new ClassSH (sglobal);
        dc1 = new Declaration (Cpp_LFlags.CLASS_LF, new Cpp_ScopedName (C1));
        dc1.setDefinition (sc1);
        symtab.addDeclaration (dc1);

        // another global class
        sc2 = new ClassSH (sglobal);
        dc2 = new Declaration (Cpp_LFlags.CLASS_LF, new Cpp_ScopedName (C2));
        dc2.setDefinition (sc2);
        symtab.addDeclaration (dc2);

        // global class with inner class
        sc3 = new ClassSH (sglobal);
        dc3 = new Declaration (Cpp_LFlags.CLASS_LF, new Cpp_ScopedName (C3));
        dc3.setDefinition (sc3);
        symtab.addDeclaration (dc3);
        symtab.enterScope (dc3);

        // inner class
        sc4 = new ClassSH (sc3);
        dc4 = new Declaration (Cpp_LFlags.CLASS_LF, new Cpp_ScopedName (C4));
        dc4.setDefinition (sc4);
        symtab.addDeclaration (dc4);
        symtab.exitScope ();

        // subclass
        sc5 = new ClassSH (sglobal);
        sc5.addSuperclass (sc2, spec_set);
        dc5 = new Declaration (Cpp_LFlags.CLASS_LF, new Cpp_ScopedName (C5));
        dc5.setDefinition (sc5);
        symtab.addDeclaration (dc5);

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

    }

    /**
     * <pre>
     * int x;
     * class C1 { };
     * ...C1::x...
     * </pre>
     */
    public void testLookupGlobal () {
        DeclarationSet matches;
        Declaration match;

        // add a var to global scope
        var1 = new Declaration (Cpp_LFlags.VARIABLE_LF, id1);

        symtab.addDeclaration (var1);

        // create qualified name C1::x
        ScopedName qid = new Cpp_ScopedName (C1);
        qid.append (id1);
       
        // lookup ...
        //		matches = symtab.lookup (qid);
        //		assertTrue (matches == null);

    }

}
