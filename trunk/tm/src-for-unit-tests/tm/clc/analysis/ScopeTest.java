package tm.clc.analysis;

import junit.framework.*;
import tm.cpp.analysis.Cpp_ScopedName;
import tm.interfaces.SourceCoords;

public class ScopeTest extends TestCase {

    protected Scope scope;
    protected DeclarationTest decls;
    protected Declaration dfn2;

    public ScopeTest () { super ("ScopeTest"); }
    public ScopeTest (String name) { super (name); }

    protected void setUp () {
        scope = new Scope ();
        decls = new DeclarationTest ("ScopeTestDecls");
        decls.setUp ();

        // represent an overloaded function - stub - no definition set here
        dfn2 = new Declaration (new LFlags (LFConst.FUNCTION), 
                                new Cpp_ScopedName (decls.FNID));
    }

    public void testBuildScope () { 
        scope.put (decls.dvar);
        scope.put (decls.dtype);
        scope.put (decls.dfn);
        scope.put (dfn2); // should add to existing matches
    }

    public void testGetMatches () {

        DeclarationSet matches;
        Declaration match;

        testBuildScope ();

        // variable match
        matches = scope.get (decls.VARID);
        match = matches.getSingleMember ();
        assertTrue (match != null);
        assertTrue (match == decls.dvar);		
        assertTrue (match.getPosition () == 0);

        // class match
        matches = scope.get (decls.CLASSID);
        match = matches.getSingleMember ();
        assertTrue (match != null);
        assertTrue (match == decls.dtype);		
        assertTrue (match.getPosition () == 1);

        // function matches
        matches = scope.get (decls.FNID);
        match = matches.getSingleMember ();
        assertTrue (match == null);
        DeclarationSetMulti mmatches = (DeclarationSetMulti) matches;
        assertTrue (mmatches.size () == 2);

        match = (Declaration) mmatches.elementAt (0);
        assertTrue (match == decls.dfn);		
        assertTrue (match.getPosition () == 2);

        match = (Declaration) mmatches.elementAt (1);
        assertTrue (match == dfn2);		
        assertTrue (match.getPosition () == 3);

        // no matches
        assertTrue (scope.get ("dud") == null);
    }
}
