package tm.cpp.analysis;

import tm.clc.analysis.*;

import java.util.*;

public class CommonSHTestImpl extends CommonSH {

    public CommonSHTestImpl () { }

    public CommonSHTestImpl (ScopeHolder encl) { super (encl); }

    protected DeclarationSet unqualifiedLookup 
		(ScopedName name, LFlags flags) throws UndefinedSymbolException {
		DeclarationSet results = null;

		// get the part of the ScopedName that we're concerned with
		String part = name.selectedPart ();

		// search local scope to point of encounter
		results = scope.get (part);
		
		if (results == null && enclosingScope != null) {
		    // search enclosing scope 
		    results = ((CommonSH)enclosingScope).unqualifiedLookup (name, flags);
		}

		return (results == null) ? new DeclarationSetMulti (): results;
    }
}


