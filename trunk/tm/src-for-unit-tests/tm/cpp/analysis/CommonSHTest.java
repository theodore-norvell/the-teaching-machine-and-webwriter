package tm.cpp.analysis;

import tm.clc.analysis.*;
import tm.interfaces.SourceCoords;

import junit.framework.*;

public class CommonSHTest extends TestCase 
    implements tm.cpp.analysis.TestConstantUser {

    ScopeHolder sblock, smfn, sclass, sns, sglobal;
    Declaration dns, dclass, dmfn, dlvar, dgvar;
    ScopedName id1, id2, id3;

    public CommonSHTest () { super ("CommonSHTest"); }
    public CommonSHTest (String name) { super (name); }

    protected void setUp () {
        sglobal = new CommonSHTestImpl ();
        sns = new CommonSHTestImpl (sglobal);
        sclass = new CommonSHTestImpl (sns);
        smfn = new CommonSHTestImpl (sclass);

        // some outer block
        sblock = new CommonSHTestImpl (sglobal);

        dns = new Declaration (Cpp_LFlags.NAMESPACE_LF, new Cpp_ScopedName (NSID));
        dns.setDefinition (sns);

        dclass = new Declaration (Cpp_LFlags.CLASS_LF, new Cpp_ScopedName (CLASSID));
        dclass.setDefinition (sclass);

        dmfn = new Declaration (Cpp_LFlags.MEM_FN_LF, new Cpp_ScopedName (FNID));
        dmfn.setDefinition (smfn);

        dlvar = new Declaration (Cpp_LFlags.VARIABLE_LF, new Cpp_ScopedName (VARID));

        dgvar = new Declaration (Cpp_LFlags.VARIABLE_LF, new Cpp_ScopedName (VARID));


        id1 = new Cpp_ScopedName (VARID);

        id2 = new Cpp_ScopedName (NSID);
        id2.append (CLASSID);
        id2.append (FNID);
        id2.completed ();

        id3 = new Cpp_ScopedName (VARID);
        id3.set_absolute ();

    }

    public void testAddDeclarations () {
        sglobal.addDeclaration (dgvar);
        sglobal.addDeclaration (dns);

        assertTrue (dgvar.getEnclosingBlock () == sglobal);

        sns.addDeclaration (dclass);
        sclass.addDeclaration (dmfn);

        ScopeHolder tsh = (ScopeHolder) dmfn.getDefinition ();
        assertTrue (tsh.enclosedBy (sglobal));

        sblock.addDeclaration (dlvar);
    }

    public void testLookup () {
        // the fun stuff..
        DeclarationSet matches;
        Declaration match;

        testAddDeclarations ();

        try {
            matches = sglobal.lookup (id3, new LFlags ());
            match = matches.getSingleMember ();
            assertTrue (match == dgvar);

            matches = sblock.lookup (id1, new LFlags ());
            match = matches.getSingleMember ();
            assertTrue (match == dlvar);

            matches = sblock.lookup (id2, new LFlags ());
            match = matches.getSingleMember ();
            assertTrue (match == dmfn);
        
            matches = sclass.lookup (id1, new LFlags ());
            match = matches.getSingleMember ();
            assertTrue (match == dgvar);

        } catch (UndefinedSymbolException use) {
            use.printStackTrace ();
        }
    }
}
