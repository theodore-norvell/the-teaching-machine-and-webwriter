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

import java.util.Enumeration;import tm.clc.analysis.DeclarationSet;import tm.clc.analysis.DeclarationSetMulti;import tm.clc.analysis.Definition;import tm.clc.analysis.LFlags;import tm.clc.analysis.ScopeHolder;import tm.clc.analysis.ScopedName;import tm.clc.analysis.UndefinedSymbolException;import tm.utilities.Debug;


/** 
 * Scope representation and lookup implementation for C++ namespaces
 */
public class NamespaceSH extends CommonSH implements Definition {
	// has this namespace been visited during the current lookup?
	private boolean wasVisited = false;
	
	//	private Debug d = new Debug (true);

    public NamespaceSH (ScopeHolder encl) { super (encl); }
    public NamespaceSH () { super (); }

	/** is this the global namespace ? */
    public boolean isGlobalNS () { return (enclosingScope == null); }

	/**
	 * Indicates whether this namespace has been visited during the current 
	 * id lookup
	 */
	public boolean visited () { return wasVisited; }

	/**
	 * Sets the 'visited' flag. 
	 * @param v the value to set the flag to.
	 */
	protected void visited (boolean v) { wasVisited = v; }

	/** unqualified id lookup implementation for namespace scopes */
    protected DeclarationSet unqualifiedLookup (ScopedName name, LFlags flags)
		throws UndefinedSymbolException {

		d.msg (Debug.COMPILE, "welcome to namespace unqualified lookup");

		// namespace scopes are visited once - some cases will not check first
		// before calling this method
		if (this.visited ()) {
			d.msg (Debug.COMPILE, "this namespace already visited");
			return new DeclarationSetMulti (); 
		}

		DeclarationSet matches = null;


		// STEP 1 - local scope to point of encounter
		// STEP 2 - used namespaces
		// STEP 3 - enclosing scope

		String part = name.selectedPart ();
		d.msg (Debug.COMPILE, ">>> looking for " + part);
		// STEP 1 - local scope to point of encounter
		// 3.4.1-5
		matches = (name.index.terminal ()) 
			? searchLocalScope (part, flags.get (Cpp_LFlags.LELEMENT))
			: searchLocalScope (part, Cpp_LFlags.QUALIFIER);
		if (matches.isEmpty ()) { 
			d.msg (Debug.COMPILE, ">>> " + part + " not found in namespace scope");
		}
		
		// this is a tricky one - if we're looking for a class or namespace id, 
		// & if the match found is equal to our 'own declaration'
		// id (the name of this namespace), don't set this namespace as having
		// been visited. The reason: if a qualifier in a qualified id (i.e. a
		// redundant qualification), we also need to search this namespace
		// for the id immediately after the qualifier. If it's referenced
		// as a terminal id, well it will be found here for sure, no need to
		// set the visited flag as this is guaranteed to be the last stop.
		// I don't like this - but (I think) it beats managing this externally
		// or adding another flag. 
		// DR 10/22/01
		if (!(matches.getSingleMember () == ownDeclaration))
			this.visited (true);

		if (!hidden (part, flags)) { // target entity type not hidden by declared entity in this scope
			// STEP 2 - used namespaces
			// 3.4.1-2
			if (matches.isEmpty () && !usedNS.isEmpty ()) {
				d.msg (Debug.COMPILE, "searching used namespaces");
				matches = new DeclarationSetMulti ();
				for (Enumeration nse = usedNS.elements (); 
					 nse.hasMoreElements (); ) {
					NamespaceSH ns = (NamespaceSH) nse.nextElement ();
					// namespaces are searched at most once during lookup
					// (3.4.3.2-4)
					if (!ns.visited ()) 
						matches.merge (ns.unqualifiedLookup (name, flags));
				}
			}
			
			// STEP 3 - enclosing scope
			if (matches.isEmpty () && !isGlobalNS () &&
				!flags.intersects (Cpp_LFlags.INTERNAL)) {
				d.msg (Debug.COMPILE, "searching enclosing scope");
				matches = 
					((CommonSH) enclosingScope).unqualifiedLookup (name, flags);
			}
			
			//		if (!name.is_qualified ()) { // unqualified id
			// 3.4.1.5 
			// search local scope - all declarations up to this point
			// including those inserted by a using declaration 
			
			// 3.4.1.?? 
			// continue to the enclosing scope (another namespace, eventually 
			// global/file namespace)
			
			//		} else { // qualified id
			// 3.4.3.2
			// else if the current scope is a namespace
			// 3.4.3.2.1
			// lookup in the scope of the namespace
			// except
			// - template-arguments of a template-id - not implemented
			// 3.4.3.2.2
			// rules for namespace lookup are as follows: the set of all ns in
			// - "current scope" namespace (called "current namespace" here)
			// - transitive closure of all namespaces nominated by using-
			// directives in the current ns and its used namespaces, except that
			// the directives are ignored where a declaration for the name is
			// found in the namespace (including the current namespace)
			// ?? decipher when more than one match is acceptable. I think it's
			// ok if the references come from using-declarations.
			// 3.4.3.2.3
			// - multiple references to the same declaration (via using directives and 
			// using declarations) are permitted 
			// 3.4.3.2.4
			// - each namespace is searched at most once
			// 3.4.3.2.5
			// - if one declaration deals with a type and a set of other decls
			// use the same name for a non-type, the non-type name wins 
			// (effectively hides the type declaration) IFF all decls are 
			// from the same namespace (indirectly as well as directly? -- TEST)
			
			// 3.4.3.6 - implictly implemented
			// the namespace referred to in the nested-name-specifier (the part of the 
			// ScopedName before the unqualified id) is the namespace that should contain
			// the declaration of the unqualified id (duh). However, a using-directive can give
			// the first part of a nested-name-specifier implicitly (this is already addressed
			// by the lookup rules for using-directives).
			
			
			//		}
		}
		return matches;
    }

}


