package tm.clc.analysis;

import junit.framework.*;

public class LFlagsTest extends TestCase {

    private final LFlags fvar = new LFlags (LFConst.VARIABLE);
    private final LFlags ffn = new LFlags (LFConst.FUNCTION);

    private LFlags lf1;

    public LFlagsTest () { super ("LFlagsTest"); }
    public LFlagsTest (String name) { super (name); }

    protected void setUp () {
		lf1 = new LFlags (LFConst.VARIABLE);
    }

    public void testMembership () {
		assertTrue (!fvar.in (LFConst.FUNCTION));
		assertTrue (!ffn.contains (fvar));
    }

    public void testGet () {
		assertTrue (lf1.get (LFConst.LELEMENT) == LFConst.VARIABLE);
    }

}
