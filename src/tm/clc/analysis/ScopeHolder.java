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

import tm.utilities.Debug;

/**
 * Forms part or all of the compile-time representation of a language element
 * with a scope. Examples in C++ include functions, classes, blocks, 
 * and namespaces. The counterpart representation for entities is the 
 * <code>Declaration<code>, which (for functions, classes, and namespaces in 
 * C++) are coupled with <code>ScopeHolder</code>s to form the complete
 * compile-time representation of the entity.
 * <p>While the <code>Declaration</code> is primarily representational, 
 * the <code>ScopeHolder</code> is primarily functional, providing the ability
 * to search a scope and related scopes for the <code>DeclarationSet</code> 
 * matching a <code>ScopedName</code>.
 * @author Derek Reilly
 * @version 1.0
 * @see Declaration
 * @see DeclarationSet
 * @see Definition
 * @see Scope
 * @see ScopedName
 * @created Aug 10 2001
 */
public abstract class ScopeHolder implements Definition {

    /**
     * The actual representation of the language element's scope.
     */
    protected Scope scope;

    /**
     * The <code>ScopeHolder</code> that physically encloses this scope
     * (wrt code structure), or <code>null</code> for an outermost or global
     * scope.
     */
    protected ScopeHolder enclosingScope = null;

    /**
     * The counterpart <code>Declaration<code> representation of this 
     * entity. This is <code>null</code> if the language element does not 
     * have a declaration (e.g. a common block).
     */
    protected Declaration ownDeclaration = null;

    /** 
     * The <code>Declaration</code> of the class that this scope is 
     * a part of, either directly (within the class definition), or 
     * indirectly (non-inline method definitions and their inner blocks).
     * This is <code>null</code> when this scope is not part of a class.
     */
    protected Declaration classDeclaration = null;

    /** 
     * Gives <code>Declaration</code> of the class that this scope is 
     * a part of, either directly (within the class definition), or 
     * indirectly (non-inline method definitions and their inner blocks).
     * @return the class' <code>Declaration</code>, or <code>null</code>
     * if this scope is not part of a class.
     */
    public Declaration getClassDeclaration () { return classDeclaration; }

    /**
     * Debugging info controlled by an occurring via <code>d</code>.
     */
    protected static Debug d = Debug.getInstance();
    
    /** functionality not implemented error message */
    public static final String NOT_IMPLEMENTED = 
        "Sorry, {0} is not implemented";

    /**
     * Creates a new <code>ScopeHolder</code> instance.
     */
    public ScopeHolder () { scope = new Scope (); }

    /**
     * Creates a new <code>ScopeHolder</code> instance enclosed by 
     * <code>encl</code>.
     *
     * @param encl the enclosing <code>ScopeHolder</code> 
     */
    public ScopeHolder (ScopeHolder encl) { 
        this ();
        enclosingScope = encl;
    }

    /**
     * Adds <code>decl</code> to this scope
     * <p>Sets the <code>Declaration's</code> enclosing block to be this scope
     * @param decl the declaration to add
     */
        public void addDeclaration (Declaration decl) {
            decl.setEnclosingBlock (this);
            scope.put (decl);
        }

    /**
     * Adds the <code>Declaration</code> representing this scope to this scope
     * <p>e.g. for a function <code>f () { ... }</code>, this method would be
     * used to add 'f' to the local scope of the function's definition.
     * @param decl the declaration to add
     */
    abstract public void addOwnDeclaration (Declaration decl);

    /**
     * High-level interface to lookup ids in this scope.
     *
     * @param name a <code>ScopedName</code> value representing the id.
     * @param flags a <code>LFlags</code> value constraining the search.
     * @return a <code>DeclarationSet</code> value containing matching 
     * <code>Declarations</code>.
     * 
     * @exception UndefinedSymbolException if the id cannot be located
     */
    public abstract DeclarationSet lookup (ScopedName name, LFlags flags) 
        throws UndefinedSymbolException ;

    /**
     * High-level non exception throwing version of lookup 
     *
     * @param name a <code>ScopedName</code> value representing the id.
     * @param flags a <code>LFlags</code> value constraining the search.
     * @return a <code>DeclarationSet</code> value containing matching 
     * <code>Declarations</code>.
     */
    public DeclarationSet lookUp (ScopedName name, LFlags flags){
        try {
           if (false) throw new UndefinedSymbolException(name);
           return lookup(name,flags);
       } catch (UndefinedSymbolException e) { // This satisfies the compiler as we over-ride lookups
           return null;
       }
    }


    /**
     * Returns the <code>ScopeHolder</code> enclosing this one.
     *
     * @return the enclosing <code>ScopeHolder</code>, or <code>null</code>
     * if none exists (e.g. global scope).
     */
    public ScopeHolder getEnclosingScope () { return enclosingScope; }

    /** 
     * Get the <code>Declaration</code> associated with this scope.
     * @return the associated <code>Declaration</code>, or <code>null</code>
     * if this scope is unnamed
     */
    public Declaration getOwnDeclaration () { return ownDeclaration; }

    /**
     * Returns the <code>Declaration</code> at position <code>pos</code> in 
     * this scope, or <code>null</code> if none exists at the position.
     * <br><em>This is an expensive operation</em>
     */
    public Declaration getDeclaration (int pos) { return scope.get (pos); }

    /**
     * Returns the innermost <code>ScopeHolder</code> from this point with
     * a declaration (e.g. a class, namespace, or function in C++). If this 
     * <code>ScopeHolder</code> is itself associated with a declaration, 
     * it is returned, otherwise it works up the enclosing scope chain.
     * @return the innermost <code>ScopeHolder</code> with a declaration
     */
    public ScopeHolder getInnermostDeclaredScope () { 
        ScopeHolder ids = null;

        if (ownDeclaration != null) 
            ids = this;

        else if (enclosingScope != null) 
            ids = enclosingScope.getInnermostDeclaredScope ();

        return ids;

    }
    

    /**
     * Indicates whether the provided <code>ScopeHolder</code> encloses 
     * (directly or indirectly) this <code>ScopeHolder</code>
     * @param sh the <code>ScopeHolder</code> to test
     * @return <code>true</code> if this scope is physically enclosed by 
     * <code>sh</code>, <code>false</code> otherwise
     */
    public boolean enclosedBy (ScopeHolder sh) {
        return (enclosingScope == null)? false 
            :  (enclosingScope == sh)  ? true 
            :  enclosingScope.enclosedBy (sh);
    }

    /**
     * Performs id resolution given an unqualified id.
     *
     * @param name the id to resolve
     * @param flags an <code>LFlags</code> value constraining the search
     * @return a <code>DeclarationSet</code> value with matching 
     * <code>Declarations</code>.
     * @exception UndefinedSymbolException if the id cannot be located
     */
    protected abstract DeclarationSet unqualifiedLookup (ScopedName name, LFlags flags) 
        throws UndefinedSymbolException ;

    /**
     * Performs id resolution given a qualified id.
     *
     * @param name the id to resolve
     * @param flags an <code>LFlags</code> value constraining the search
     * @return a <code>DeclarationSet</code> value with matching 
     * <code>Declarations</code>.
     * @exception UndefinedSymbolException if the id cannot be located
     */
    protected abstract DeclarationSet qualifiedLookup (ScopedName name, LFlags flags) 
        throws UndefinedSymbolException ;

    /**
     * Moves the contents of <code>fromSH</code> into this 
     * <code>ScopeHolder</code>'s scope.
     */
    public void moveContents (ScopeHolder fromSH) {

        if (fromSH != null) {
            d.msg (Debug.COMPILE, "copying over " + fromSH.scope.totalElements + " declarations");
            scope.copyElements (fromSH.scope);	

            // set the enclosing scope attribute
            for (Enumeration decls = fromSH.elements (); 
                 decls.hasMoreElements (); ) 
                ((Declaration) decls.nextElement ()).setEnclosingBlock (this);

            // this is a move, remove elements from original ScopeHolder 
            fromSH.scope = new Scope ();
        }
    }

    /** 
     * Gives an <code>Enumeration</code> over the <code>Declarations</code>
     * contained directly within this <code>ScopeHolder's</code> scope.
     */
    public Enumeration elements () { return scope.elements (); }
        
        
    public void dumpContents(String indent, Debug d){   // used for debugging
        d.msg(Debug.COMPILE, " { // " + toString());
        
        //if (ownDeclaration != null)
        //    ownDeclaration.dumpContents();
        Enumeration declarations = elements();
        while (declarations.hasMoreElements()){
            DeclarationSet matches = (DeclarationSet)declarations.nextElement();
            if( matches == null ) {
                d.msg(Debug.COMPILE, indent+"null" ); }
            else {
                matches.dumpContents(indent, d); }
        }
   }
    
}