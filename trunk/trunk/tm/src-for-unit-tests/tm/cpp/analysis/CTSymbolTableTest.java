package tm.cpp.analysis;

import java.util.Vector ;

import tm.clc.analysis.*;
import tm.clc.ast.NodeList;
import tm.cpp.ast.TyFun;
import tm.utilities.Debug;

import junit.framework.*;

/**
 * tests C++ implementation of CTSymbolTable (compile-time symbol table)
 */

public class CTSymbolTableTest extends TestCase 
    implements tm.cpp.analysis.TestConstantUser {

    static Cpp_CTSymbolTable st = new Cpp_CTSymbolTable ();
    static LFlags lookupFlags = LFConst.EMPTY_LF.writable ();

    NamespaceSH sglobal;
    Declaration dns, df1, df2, dca;

    public CTSymbolTableTest () { super ("CTSymbolTableTest"); }
    public CTSymbolTableTest (String name) { super (name); }

    protected void setUp () {
        st.enterFileScope ();
        // namespaces
        // global
        sglobal = st.getGlobalScope ();
    }

    public void testAddDefiningDeclaration () {
        d.msg (Debug.COMPILE, "Test: add defining declaration");
        ScopeHolder origScope = st.getCurrentScope ();
        ScopedName nsname = new Cpp_ScopedName (NS1);
        dns = st.addDefiningDeclaration (nsname, Cpp_LFlags.NAMESPACE_LF, null); 

        st.enterScope (dns);
        ScopeHolder newScope = (ScopeHolder) dns.getDefinition ();
        assertTrue (newScope == st.getCurrentScope ());
        assertTrue (newScope != origScope);
        assertTrue (newScope.getEnclosingScope () == origScope);
        try {
            // should be able to look up self in scope
            Declaration directMatch = 
                newScope.lookup(nsname, new LFlags ()).getSingleMember ();
            Declaration stMatch = st.lookup (nsname).getSingleMember ();
            assertTrue (directMatch == stMatch);
        } catch (UndefinedSymbolException ude) {
        }
    }

    public void testNewFunctionDeclaration () {
        d.msg (Debug.COMPILE, "Test: new function declaration");
        ScopeHolder origScope = st.getCurrentScope ();
        ScopedName f1Name = new Cpp_ScopedName (F1);
        ScopedName f2Name = new Cpp_ScopedName (F2);
        Declaration matchDf1, matchDf2;
        TyFun ft = new TyFun (new Vector (), false);

        df1 = st.addDefiningDeclaration (f1Name, Cpp_LFlags.REG_FN_LF, ft);
        st.enterScope (df1);
        st.exitScope ();
        matchDf1 = st.newFunctionDeclaration (Cpp_LFlags.REG_FN_LF, f1Name, 
                                              null, null, ft);
        assertTrue (df1 == matchDf1);

        
        matchDf2 = st.newFunctionDeclaration (Cpp_LFlags.REG_FN_LF, f2Name, 
                                              null, null, ft);
        assertTrue (matchDf2 != matchDf1);
        assertTrue (st.getCurrentScope () == origScope);

        df2 = st.addDefiningDeclaration (f2Name, Cpp_LFlags.REG_FN_LF, ft);
        st.enterScope (df2);
        assertTrue (matchDf2 == df2);
        st.exitScope ();
    }

    public void testLookup () {
        d.msg (Debug.COMPILE, "Test: lookup");

        testAddDefiningDeclaration ();
        testNewFunctionDeclaration ();

        // now we have a namespace with two functions defined inside
        // lookup ::N1 
        d.msg (Debug.COMPILE, "looking for ::N1");
        ScopedName gN1 = new Cpp_ScopedName (NS1);
        gN1.set_absolute ();
        Declaration match = st.lookup(gN1, new LFlags ()).getSingleMember ();
        switch (lookupFlags.get (Cpp_LFlags.LCONTEXT)) {
        default:
            assertTrue (match == dns);
            break;
        }
        
        // lookup N1
        d.msg (Debug.COMPILE, "looking for N1");
        ScopedName n1 = new Cpp_ScopedName (NS1);
        match = st.lookup (n1, new LFlags ()).getSingleMember ();
        switch (lookupFlags.get (Cpp_LFlags.LCONTEXT)) {
        default:
            assertTrue (match == dns);
            break;
        }

        // lookup ::N1::f1
        d.msg (Debug.COMPILE, "looking for ::N1::f1");
        ScopedName gf1 = new Cpp_ScopedName (gN1);
        gf1.append (F1);
        match = st.lookup (gf1, new LFlags ()).getSingleMember ();
        switch (lookupFlags.get (Cpp_LFlags.LCONTEXT)) {
        default:
            assertTrue (match == df1);
            break;
        }

        // lookup N1::f1
        d.msg (Debug.COMPILE, "looking for N1::f1");
        ScopedName nf1 = new Cpp_ScopedName (n1);
        nf1.append (F1);
        match = st.lookup (nf1, new LFlags ()).getSingleMember ();
        switch (lookupFlags.get (Cpp_LFlags.LCONTEXT)) {
        default:
            assertTrue (match == df1);
            break;
        }

        // lookup f1
        d.msg (Debug.COMPILE, "looking for f1");
        ScopedName f1 = new Cpp_ScopedName (F1);
        match = st.lookup (f1, new LFlags ()).getSingleMember ();
        switch (lookupFlags.get (Cpp_LFlags.LCONTEXT)) {
        default:
            assertTrue (match == df1);
            break;
        }

    }
    

    public void testPriorDeclLookup () {
        d.msg (Debug.COMPILE, "Test: lookup for previous declaration");
        lookupFlags = Cpp_LFlags.PRIORDECL_LF.writable ();
        testLookup ();
    }
    
    public void testDeclStatLookup () {
        d.msg (Debug.COMPILE, "Test: lookup from within declaration statement");
        lookupFlags = Cpp_LFlags.DECLSTAT_LF.writable ();
        testLookup ();
    }
    

}

