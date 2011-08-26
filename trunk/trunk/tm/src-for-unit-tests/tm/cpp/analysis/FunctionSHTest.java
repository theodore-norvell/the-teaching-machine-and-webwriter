package tm.cpp.analysis;

import tm.clc.analysis.*;
import tm.interfaces.SourceCoords;

import junit.framework.*;

public class FunctionSHTest extends TestCase 
    implements tm.cpp.analysis.TestConstantUser {

    ScopeHolder sgrfn, sglobal;
    Declaration dgrfn;
    ScopedName id1;

    public FunctionSHTest () { super ("FunctionSHTest"); }
    public FunctionSHTest (String name) { super (name); }

    protected void setUp () {
        sglobal = new CommonSHTestImpl ();
        sgrfn = new RegularFnSH (sglobal);

        dgrfn = new Declaration (Cpp_LFlags.REG_FN_LF, new Cpp_ScopedName (FNID));
        dgrfn.setDefinition (sgrfn);

        id1 = new Cpp_ScopedName (VARID);

    }

    public void testAddDeclarations () {
        sglobal.addDeclaration (dgrfn);

        assertTrue (dgrfn.getEnclosingBlock () == sglobal);

        ScopeHolder tsh = (ScopeHolder) dgrfn.getDefinition ();
        assertTrue (tsh.enclosedBy (sglobal));
    }

    public void testLookup () {
        // the fun stuff..
        DeclarationSet matches;
        Declaration match;

        testAddDeclarations ();

        try {
            matches = sglobal.lookup (id1, Cpp_LFlags.EMPTY_LF);
            assertTrue (matches == null || matches.isEmpty ());
        
            matches = sgrfn.lookup (id1, Cpp_LFlags.EMPTY_LF);
            assertTrue (matches == null || matches.isEmpty ());

        } catch (UndefinedSymbolException use) {
            use.printStackTrace ();
        }
    }
}
