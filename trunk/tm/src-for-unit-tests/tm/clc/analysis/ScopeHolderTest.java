package tm.clc.analysis;

import junit.framework.*;

public class ScopeHolderTest extends TestCase {

    protected DeclarationTest decls;
    protected ScopeHolder block = new ScopeHolderTestImpl ();

    public ScopeHolderTest () { super ("ScopeHolderTest"); }
    public ScopeHolderTest (String name) { super (name); }

    protected void setUp () {
		decls = new DeclarationTest ();
		decls.setUp ();
		block = new ScopeHolderTestImpl (decls.shfn);
    }

    public void testEnclosing () {
		assertTrue (block.getEnclosingScope () == decls.shfn);
		assertTrue (decls.shfn.getEnclosingScope () == decls.shglobal);
		assertTrue (decls.shglobal.getEnclosingScope () == null);

		assertTrue (! decls.shglobal.enclosedBy (decls.shglobal));
		assertTrue (! decls.shglobal.enclosedBy (decls.shclass));
		assertTrue (! decls.shclass.enclosedBy (decls.shfn));
		assertTrue (decls.shclass.enclosedBy (decls.shglobal));
		assertTrue (! decls.shfn.enclosedBy (decls.shclass));
		assertTrue (! decls.shfn.enclosedBy (block));
		assertTrue (decls.shfn.enclosedBy (decls.shglobal));
		assertTrue (! block.enclosedBy (decls.shclass));
		assertTrue (block.enclosedBy (decls.shfn));
		assertTrue (block.enclosedBy (decls.shglobal));

    }
}
