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

import tm.clc.ast.TyAbstractClass;
import tm.utilities.Assert;
import tm.utilities.Debug;

/**
 * Functions as the external interface to symbol table lookups
 * 
 * The Parser uses CTSymbolTable when it needs to perform a lookup, 
 * when it creates a new Declaration/Definition, and when it enters/leaves
 * a scope.
 * @author Derek Reilly
 * @created August 16, 2001
 */
public abstract class CTSymbolTable {
	/* no type found matching id */
    protected static final String NO_MATCHING_TYPE = 
        "No type found matching id {0}.";

        /** The last declaration encountered, used for class member references */
    protected Declaration lastDeclaration;

        /** A table matching class declarations with their type definitions
         * !! need to know when a program has been completely parsed, so 
         *    that this table can be cleared.
         */
    protected static Hashtable classDeclarations = new Hashtable ();

	/** debugging comments this way */
    protected static Debug debug = Debug.getInstance ();

        /**
	 * A reference to the <code>ScopeHolder</code> representing the <em>current
	 * scope</em>, that is the scope in which an id is encountered by the 
	 * parser. This reference is updated as scopes are entered and exited.
	 *
	 */
    protected ScopeHolder currentScope;

        /**
         * Perform an id lookup under the default rules. Use when the id is not
         * part of an object/class member reference (dot notation or dereferenced),
         * in a declaration statement, or part of the parameter list of a function
         * call.
         * @param name the id as encountered (i.e. qualified only if so found in 
         * the code
         * @return a DeclarationSet containing all Declarations matching the id
         * under the current scope and following relevant lookup rules.
         */
    public DeclarationSet lookup (ScopedName name) {
		return lookup (name, new LFlags (LFConst.EMPTY));
    }

    /**
     * Perform an id lookup under contextual constraints identified in 
     * the LFlags parameter
     * @param name the id as encountered (i.e. qualified only if so found in
     * the code
     * @param flags an <code>LFlags</code> object containing contextual info 
	 * regarding the encounter of the id
     * @return a <code>DeclarationSet</code> containing all 
	 * <code>Declarations</code> matching the id
     * under the current scope and following relevant lookup rules.
     */
    public abstract DeclarationSet lookup (ScopedName name, LFlags flags) ;

	/**
	 * Adds a <code>Declaration</code> to the current scope.
	 * @param d the <code>Declaration</code> to add
	 */
    public void addDeclaration (Declaration d) { 
		debug.msg (Debug.COMPILE, ">>> adding declaration " + d.getUnqualifiedName () + 
				   " to " + currentScope.getClass().getName ());
		currentScope.addDeclaration (d);
    }

	/**
	 * Indicates that a new scope has been entered, which has a related 
	 * <code>Declaration</code>. This could be a function/method, class, etc.
	 * @param newScope a <code>Declaration</code> value
	 */
	public void enterScope (Declaration newScope) {
		debug.msg (Debug.COMPILE, "entering scope of " + newScope.toString());
		Assert.apology (newScope.isScopeHolder (), 
						"Declaration must have scope to enter scope!");
		currentScope = (ScopeHolder) newScope.getDefinition ();
		// note : relationship to previous current scope determined via
		// a call to addDeclaration
    }

	/**
	 * Indicates that a scope without a related <code>Declaration</code> 
	 * (e.g. block scope) has been entered.
	 */
    public abstract void enterScope () ;

	/**
	 * Indicates that the current scope is exited
	 */
    public void exitScope () { 
		debug.msg (Debug.COMPILE, "leaving a scope");
		currentScope = currentScope.getEnclosingScope ();
    }

	/**
	 * Indicates that a new file is being parsed.
	 */
	public void enterFileScope () { }

	/**
	 * Indicates that a file has been completely parsed.
	 */
	public void exitFileScope () { }

	/**
	 * Gets the current scope
	 * @return a <code>ScopeHolder</code> value representing the current scope
	 */
	public ScopeHolder getCurrentScope () { return currentScope; }


	public static Declaration getClassDeclaration (TyAbstractClass ct) {
		return (Declaration) classDeclarations.get (ct);
	}

	public static void classTypeDefined (TyAbstractClass ct, Declaration d) {
		d.setType (ct);
		classDeclarations.put (ct, d);
	}

	
}
