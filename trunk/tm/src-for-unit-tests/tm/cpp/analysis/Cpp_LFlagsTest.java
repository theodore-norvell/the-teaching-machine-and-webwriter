package tm.cpp.analysis;

import tm.clc.analysis.LFlags;
import junit.framework.*;

public class Cpp_LFlagsTest extends TestCase {

    private final LFlags fvar = new LFlags (Cpp_LFlags.VARIABLE);
    private final LFlags frfn = new LFlags (Cpp_LFlags.REG_FN);
    private final LFlags fclass = new LFlags (Cpp_LFlags.CLASS);
    private final LFlags ffn = new LFlags (Cpp_LFlags.FUNCTION);
    private final LFlags fscoped = new LFlags (Cpp_LFlags.SCOPED);
    

    private LFlags lf1, lf2, lf3;

    public Cpp_LFlagsTest () { super ("LFlagsTest"); }
    public Cpp_LFlagsTest (String name) { super (name); }

    protected void setUp () {
		lf1 = new LFlags (Cpp_LFlags.VARIABLE);
		lf2 = new LFlags (Cpp_LFlags.TYPEDEF);
		lf3 = new LFlags (Cpp_LFlags.CLASS);
    }

    public void testMembership () {
		assertTrue (!fvar.in (Cpp_LFlags.FUNCTION));
		assertTrue (frfn.in (Cpp_LFlags.FUNCTION));
		assertTrue (frfn.in (ffn));
		assertTrue (ffn.contains (frfn));
		assertTrue (!ffn.contains (fvar));
		assertTrue (fscoped.contains (ffn));
		assertTrue (fscoped.contains (frfn));
		assertTrue (!fscoped.contains (fvar));
		assertTrue (fclass.in (Cpp_LFlags.SCOPED));
    }

    public void testSet () {
		lf1.set (Cpp_LFlags.SCALAR);
		assertTrue (lf1.contains (Cpp_LFlags.SCALAR));
		assertTrue (lf1.contains (fvar));
		lf1.set (Cpp_LFlags.SCALAR);
		assertTrue (lf1.contains (Cpp_LFlags.SCALAR));
		assertTrue (lf1.contains (fvar));
		lf1.unset (Cpp_LFlags.SCALAR);
		assertTrue (!lf1.contains (Cpp_LFlags.SCALAR));
		assertTrue (lf1.contains (fvar));
    }

    public void testEquals () {
		assertTrue (lf3.equals (fclass));
		assertTrue (fclass.equals (lf3));
		assertTrue (lf3.equals (Cpp_LFlags.CLASS));
		assertTrue (fclass.equals (Cpp_LFlags.CLASS));
    }

    public void testGet () {
		lf1.set (Cpp_LFlags.SCALAR);
		assertTrue (lf1.get (Cpp_LFlags.LELEMENT) == Cpp_LFlags.VARIABLE);
		assertTrue (lf1.get (Cpp_LFlags.VTYPE) == Cpp_LFlags.SCALAR);
		lf1.unset (Cpp_LFlags.SCALAR);
    }

    public void testIntersects () {
		assertTrue (fclass.intersects (Cpp_LFlags.SCOPED));
    }
}
