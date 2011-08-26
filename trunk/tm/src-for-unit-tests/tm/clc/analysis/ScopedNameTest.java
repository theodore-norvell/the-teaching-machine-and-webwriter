package tm.clc.analysis;

import junit.framework.*;

import java.util.Enumeration;

import tm.cpp.analysis.Cpp_ScopedName;
import tm.utilities.Debug;

public class ScopedNameTest extends AnalysisTestCase {

    private static final String NSID = "A";

    private ScopedName absolute;
    private ScopedName qualified;
    private ScopedName unqualified;
    private ScopedName part;
    private ScopedName copy;

    public ScopedNameTest () { super ("ScopedNameTest"); }
    public ScopedNameTest (String name) { super (name); }

    protected void setUp () {
        absolute = new Cpp_ScopedName (VARID);
        absolute.set_absolute ();

        unqualified = new Cpp_ScopedName (VARID2);

        part = new Cpp_ScopedName (FNID);

        qualified = new Cpp_ScopedName ();
        qualified.append (NSID);
        qualified.append (CLASSID);
        qualified.append (part);
    }

    public void testPostCreation () {
        assertTrue (!unqualified.is_qualified ());
        assertTrue (qualified.is_qualified ());
        assertTrue (absolute.is_qualified ());

        assertEquals (VARID2, unqualified.getUnqualifiedName ());
        assertEquals (FNID, qualified.getUnqualifiedName ());

        assertTrue (absolute.is_absolute ());
        assertTrue (!qualified.is_absolute ());
    }
    
    public void testNames () { 
        d.msg (Debug.COMPILE, "\n");
        d.msg (Debug.COMPILE, absolute.getName ());
        d.msg (Debug.COMPILE, unqualified.getName ());
        d.msg (Debug.COMPILE, qualified.getName ());
        d.msg (Debug.COMPILE, absolute.getUnqualifiedName ());
        d.msg (Debug.COMPILE, unqualified.getUnqualifiedName ());
        d.msg (Debug.COMPILE, qualified.getUnqualifiedName ());
    }

    public void testCopy () {
        copy = new Cpp_ScopedName (qualified);

        assertEquals (copy.is_qualified (), qualified.is_qualified ());
        assertEquals (copy.is_absolute (), qualified.is_absolute ());

        assertTrue (copy.index != qualified.index);
        assertTrue (copy.index.value () == 0);

        assertEquals (copy.getUnqualifiedName(), qualified.getUnqualifiedName ());
        assertEquals (copy.getName(), qualified.getName ());
    }

    public void testTraversal () {
        d.msg (Debug.COMPILE, "\n");
        d.msg (Debug.COMPILE, "Here are the parts of " + qualified.getName ());
        for (Enumeration e = qualified.getParts (); e.hasMoreElements (); ) 
            d.msg (Debug.COMPILE, (String) e.nextElement ());

        qualified.completed ();

        d.msg (Debug.COMPILE, "Here are the parts using index traversal");
        do d.msg (Debug.COMPILE, qualified.selectedPart ()); 
        while (qualified.index.advance ());

        d.msg (Debug.COMPILE, "Here are the parts traversing backwards");
        do d.msg (Debug.COMPILE, qualified.selectedPart ());
        while (qualified.index.reverse ());
        
    }

}
