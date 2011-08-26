package tm.clc.analysis;

import junit.framework.*;
import tm.cpp.analysis.Cpp_ScopedName;
import tm.interfaces.SourceCoords;

public class DeclarationTest extends TestCase implements TestConstantUser {

    protected Declaration dtype, dvar, dfn;
    protected ScopeHolder shglobal, shclass, shfn;

    public DeclarationTest () { super ("DeclarationTest"); }
    public DeclarationTest (String name) { super (name); }

    protected void setUp () {
        dvar = new Declaration (new LFlags (LFlags.VARIABLE),
                                new Cpp_ScopedName (VARID), null, null, null);
        dfn = new Declaration (new LFlags (LFlags.FUNCTION),
                               new Cpp_ScopedName (FNID), null, null, null);
        dtype = new Declaration (new LFlags (LFlags.CLASS),
                               new Cpp_ScopedName (CLASSID), null, null, null);

        shglobal = new ScopeHolderTestImpl (); 
        shfn = new ScopeHolderTestImpl (shglobal);
        shclass = new ScopeHolderTestImpl (shglobal);

    }
    /* very simple test of valid use of 'setDefinition' */
    public void testSetValidDefinitions () {

        dvar.setDefinition (dvar);
        dfn.setDefinition (shfn);
        dtype.setDefinition (shclass);
    }

    /* some straightforward attribute - accessor/mutator checks */
    public void testAccessors () {
        testSetValidDefinitions ();

        assertTrue (!dvar.isScopeHolder ());
        assertTrue (dfn.isScopeHolder ());

        //		assert (dvar.isDefining ());
        
        assertTrue (dvar.getUnqualifiedName().equals (VARID));
    }
}
