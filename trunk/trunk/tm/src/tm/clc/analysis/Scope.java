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

package tm.clc.analysis;

import java.util.*;

/**
 * Represents the actual symbol table for a local scope. Essentially
 * a <code>Hashtable</code> of <code>DeclarationSet</code>s that also tags 
 * each element with an order of placement. 
 * <p>The placement value can be used to determine name hiding where 
 * appropriate to the language - note that this requires a sequential parsing
 *  order for declarations in a scope.
 */
public class Scope {
    private Hashtable theScope;
    
    // keeps track of total decls in scope
    // and records order of placement
    // ** line # + offset might be the best way to go, if we ever need
    // to compare this with the line in which an id is encountered.
    // Right now we're assuming that everything in the scope was 
    // declared prior to encountering a name that is looked up.
    protected int totalElements = 0; 
    
    /**
	 * Creates a new <code>Scope</code> instance.
	 */
	protected Scope () { theScope = new Hashtable (); }
    
    /**
	 * Add a <code>Declaration<code> to this scope. The declaration's
	 * <code>position</code> attribute will be set equal to its placement in this
	 * scope.
	 * @param decl the <code>Declaration</code> to add.
	 */
	public void put (Declaration decl) {
		// assuming order of addition equals placement in scope
		decl.setPosition (totalElements++);

		String name = decl.getUnqualifiedName ();

		DeclarationSet matches = (DeclarationSet) theScope.get (name);
		if (matches == null) {
			// add the Declaration to the table
			theScope.put (name, decl);
		} else if (matches instanceof Declaration) {
			// create a new list
			DeclarationSetMulti multiple = new DeclarationSetMulti ();
			multiple.addElement (matches);
			multiple.addElement (decl);
			theScope.put (name, multiple);
		} else {
			// add declaration to list of matches
			((DeclarationSetMulti) matches).addElement (decl);
		}
    }
    
    /**
	 * Gives the <code>DeclarationSet</code> matching the id represented by 
	 * <code>name</code>, or <code>null</code> if no match was found.
	 *
	 * @param name the id to lookup
	 * @return the matching <code>DeclarationSet</code> or <code>null</code> 
	 * if no match
	 */
	public DeclarationSet get (String name) { 
		return (DeclarationSet) theScope.get (name); 
    }

	/**
	 * Gives the <code>Declaration</code> with the specified position in 
	 * this scope. 
	 * <br><em>This is an expensive operation</em>
	 */
	public Declaration get (int pos) {
		Declaration match = null;
		for (Enumeration dsets = theScope.elements (); 
			 match == null && dsets.hasMoreElements (); ) {
			Enumeration ds = 
				((DeclarationSet) dsets.nextElement ()).elements ();
		    for ( ; match == null && ds.hasMoreElements (); ) {
				Declaration d = (Declaration) ds.nextElement ();
				if (d.getPosition () == pos) match = d;
			}
		}
		return match;
	}

	/**
	 * Copies the elements of <code>from</code> into this scope.
	 */
	public void copyElements (Scope from) {
		for (Enumeration e = from.theScope.elements (); 
			 e.hasMoreElements (); ) {
			Enumeration me = ((DeclarationSet) e.nextElement ()).elements ();
			for ( ; me.hasMoreElements (); ) 
				this.put (((Declaration) me.nextElement ()));
		}
	}

	/**
	 * Gives an enumeration over the <code>Declaration<code>s in the scope
	 * @return the enumeration
	 */
	public Enumeration elements () { return theScope.elements (); }
    
}


