//     Copyright 1998--2010 Michael Bruce-Lockhart and Theodore S. Norvell
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License. 
// You may obtain a copy of the License at 
//
//     http://www.apache.org/licenses/LICENSE-2.0 
//
// Unless required by applicable law or agreed to in writing, 
// software distributed under the License is distributed on an 
// "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
// either express or implied. See the License for the specific language 
// governing permissions and limitations under the License.

package tm.cpp.analysis;

import tm.clc.analysis.DeclarationSet;import tm.clc.analysis.LFlags;import tm.clc.analysis.ScopedName;import tm.clc.analysis.UndefinedSymbolException;import tm.utilities.Assert;
/** 
 * <code>ScopeHolder</code> for a common inner block (unnamed, usually within 
 * a function body)
 */
public class BlockSH extends CommonSH {

	/**
	 * Creates a new <code>BlockSH</code> instance, with the provided 
	 * enclosing scope
	 * @param the block's enclosing scope
	 */
    public BlockSH (CommonSH encl) { 
		super (encl); 
		inClassDefinition = encl.inClass ();
		classDeclaration = encl.getClassDeclaration ();
	}
	
	/* javadoc from base class */
    protected DeclarationSet unqualifiedLookup (ScopedName name, LFlags flags) 
		throws UndefinedSymbolException {

		DeclarationSet results = null;


		// get the part of the ScopedName that we're concerned with
		// a block could only be concerned with the first part
		String part = name.selectedPart ();

		if (flags.contains (Cpp_LFlags.LABEL)) {
		    // 3.3.4
		    // if name is a label
		    // search function scope or 
		    // 1. search local scope, return decl if found
		    // 2. if not found, create a declaration for the label (assume it's ok)
		    Assert.apology ("Sorry, labels not yet implemented");

		} else {

		    // search local scope to point of encounter
		    // using-declarations are added to local scope (7.3.3-2)
		    results = (name.index.terminal ()) 
				? searchLocalScope (part, flags.get (Cpp_LFlags.LELEMENT))
				: searchLocalScope (part, Cpp_LFlags.QUALIFIER);
		    
		    if (!hidden (part, flags) && results.isEmpty () && 
				!flags.intersects (Cpp_LFlags.LOCAL)) {
				// search enclosing scope 
				// using-directives will alter behaviour (7.3.4-1)
				results = ((CommonSH)enclosingScope).unqualifiedLookup (name, flags);
		    }
		}
		return results;
    }

}


