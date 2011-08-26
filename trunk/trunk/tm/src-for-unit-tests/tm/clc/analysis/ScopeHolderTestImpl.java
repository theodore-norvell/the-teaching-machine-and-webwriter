/* 
 * ScopeHolderTestImpl.java
 */
package tm.clc.analysis;

import java.util.*;

/* Dummy concrete ScopeHolder subclass for use in testing other  
 * classes that want to possess a non-null ScopeHolder member
 */
public class ScopeHolderTestImpl extends ScopeHolder {

    public ScopeHolderTestImpl () { }

    public ScopeHolderTestImpl (ScopeHolder encl) { super (encl); }

    public DeclarationSet lookup (ScopedName name, LFlags flags) 
        throws UndefinedSymbolException {
        System.out.println ("lookup entered");
        return null;
    }

    protected DeclarationSet qualifiedLookup (ScopedName name, LFlags flags) 
        throws UndefinedSymbolException {
        return null;
    }

    protected DeclarationSet unqualifiedLookup 
        (ScopedName name, LFlags flags) throws UndefinedSymbolException {
        return null;
    }
    
    
    public void addOwnDeclaration (Declaration decl) { }
}



