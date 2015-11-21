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

/* 
 * CommonSH.java
 */
package tm.cpp.analysis;



import java.util.Enumeration;
import java.util.Vector;

import tm.clc.analysis.Declaration;
import tm.clc.analysis.DeclarationSet;
import tm.clc.analysis.DeclarationSetMulti;
import tm.clc.analysis.LFlags;
import tm.clc.analysis.ScopeHolder;
import tm.clc.analysis.ScopedName;
import tm.clc.analysis.UndefinedSymbolException;
import tm.utilities.Assert;
import tm.utilities.Debug;



/**

 * Provides ability to search the scope for a <code>Declaration</code> 

 * matching a <code>ScopedName</code>. 

 */

public abstract class CommonSH extends ScopeHolder {



    /** error message given when an unqualified id is given to the 

     * qualified lookup routine

     */

    protected static final String NOT_QUALIFIED = 

        "cannot do qualified lookup on unqualified name {0}"; 



    /** 

     * <em>used namespaces</em> in this scope

     */

    protected Vector usedNS = new Vector (); // used by ns, fn, block



    /**  is this scope part of a class definition ? */

    protected boolean inClassDefinition = false;



    /** 

     * The nearest enclosing namespace. In lookup, a using-directive places

     * the ids found in the used namespace in the scope of the namespace

     * enclosing the block in which the using declaration is encountered.

     *

     * If the current block is a namespace, the declarations get added to 

     * the current scope. For purposes of lookup, classes are namespaces.

     *

     * Unqualified lookup will search current and enclosing scopes up to and

     * including the enclosing namespace. Then the 'used namespaces' are

     * searched, followed by a continuation of the hierarchical search from 

     * the enclosing namespace's enclosing scope to the global scope. This 

     * order follows hiding rules in 3.3.7-4

     *

     */

    //    protected CommonSH enclosingNS; 



    public CommonSH () { }



    public CommonSH (ScopeHolder encl) { super (encl); }





    /**

     * Indicates whether this scope is part of a class definition

     */

    public boolean inClass () { return inClassDefinition; }



    /**

     * Adds the <code>Declaration</code> to this scope

     * <p>Sets the <code>Declaration's</code> enclosing block to be this scope

     * <br>the fully qualified name will be constructed also.

     * @param decl the declaration to add

     */

    public void addDeclaration (Declaration decl) {

        super.addDeclaration (decl);

        // determine and assign the fqn of the declaration 

        decl.setName (fqnToHere (decl.getUnqualifiedName ()));

    }
    
	/**
	 * Adds the <code>Declaration</code> representing this scope to this scope
	 * <p>e.g. for a function <code>f () { ... }</code>, this method would be
	 * used to add 'f' to the local scope of the function's definition.
	 * @param decl the declaration to add
	 */
    public void addOwnDeclaration(Declaration decl){
        scope.put (decl);
        ownDeclaration = decl;
    }
    




    /** 

     * Fully qualify the id provided (as it is known in this scope)

     * @param id the id to qualify

     * @return the fully qualified id

     */

    protected ScopedName fqnToHere (String id) { 

        if (enclosingScope == null) {

            ScopedName fqn = new Cpp_ScopedName (id);

            fqn.set_absolute ();

            return fqn;

        } else if (ownDeclaration != null) { 

            // use our fqn plus the id

            ScopedName fqn = new Cpp_ScopedName (ownDeclaration.getName ());

            fqn.append (id);

            return fqn;

        } else {

            return ((CommonSH) enclosingScope).fqnToHere (id);

        }

    }



    /**

     * Performs lookup for all ids encountered in this scope, except ids 

     * that are qualified to be part of global scope (i.e. with a leading

     * scope resolution operator (::)).

     * @param name the scoped name (id) to resolve

     * @param flags any additional constraints on the lookup are indicated here

     * @throws UndefinedSymbolException if the id doesn't resolve

     */

    public DeclarationSet lookup (ScopedName name, LFlags flags) 

        throws UndefinedSymbolException {



        DeclarationSet results = null;



        // first make sure the name is marked read only

        name.completed ();



        if (name.is_qualified ()) { // this is a qualified name

            results = qualifiedLookup (name, flags);



        } else { // this is an unqualified name 

            results = unqualifiedLookup (name, flags);



            if (false) {

                // 3.4.2

                // ARGUMENT-DEPENDENT NAME LOOKUP

                // this is an unqualified function call or cast-exp

                results = argumentDependentLookup (name, flags);

            }

        }



        return results;

    }





    /**

     * Performs id resolution for unqualified ids in the postfix-expression

     * of a function call (ISO 3.4.2 basic.lookup.koenig)

     * <br><strong>not yet implemented</strong>

     */

    protected DeclarationSet argumentDependentLookup 

        (ScopedName name, LFlags flags) {



        return null;



        // assumes ordinary unqualified lookup has been performed



        // look at the associated classes and namespaces

        // but: 

        // do not follow using-directives in an associated namespace

        // ? any namespace-scope friend functions decalred in associated

        // classes are visible within their respective namespaces even if not

        // visible during an ordinary lookup (11.4)





        // 3.4.2

        // for each type T in the function call

        // if T is a class

        // add the class and its hierarchy

        // add the namespaces in which the classes are defined



        // if T is an enumerated type

        // add the namespace in which it is defined

        // if the enum is a class member, add the class



        // if T is a pointer or an array of X

        // add the namespaces and classes associated with X



        // if T is a function type

        // add the namespaces and classes associated with the function's

        // parameter types and the return type



        // if T is a pointer to a member function of a class X

        // as with function type, plus the namespaces and classes  associated 

        // with X



        // if T is a pointer to a data member of a class X

        // namespaces and classes associated with the type of the data member

        // plus those associated with X



        // if T is a template-id

        // not implemented

    }



    /**

     * C++ - specific routine for qualified id lookup.

     * <ul>ISO notes:

     * <li>3.4.3-4 (global scope) case taken care of by <code>CTSymbolTable</code> 

     * before this routine is called

     * </ul>

     * @param name the scoped name (qualified id) to resolve

     * @param flags any additional constraints on the lookup are indicated here

     * @throws UndefinedSymbolException if any part of the qualifier doesn't resolve

     */

    protected DeclarationSet qualifiedLookup (ScopedName name, LFlags flags) 

        throws UndefinedSymbolException {



        d.msg (Debug.COMPILE, ">>>welcome to common_sh qualified lookup, in " + 

               this.getClass().getName ());

        // 3.4.3.4 (global scope) case taken care of before this method is called



        // break references up

        // current scope is local scope

        // for each reference

        // search for reference in current scope

        // if found, new current scope is the namespace or class referred to



        // this test may be overkill - is this method ever called outside of 

        // the CTSymbolTable and other ScopeHolders ?

        if (!name.is_qualified ())

            Assert.apology (NOT_QUALIFIED, name.getName ());



        DeclarationSet matches = unqualifiedLookup (name, flags);

      

        

        if (name.index.terminal ()) { // reached terminal id in scoped name

            name.index.reset ();

            return matches;



        } else { // continue along qualifiers

            if (matches.isEmpty ()) {

                throw new UndefinedSymbolException (name);

            }



            // getScopeHolder will check for multiple matches or 

            // non-scope-holder match

            CommonSH msh = (CommonSH) matches.getScopeHolder (); 

            name.index.advance ();

            // lookup for any id after the first qualifier is constrained by 

            // its qualifier (3.4.3-3)

            // ** TODO ** special cases for destructor names and 

            // conversion-type-ids (3.4.3.1-1)

            flags.set (Cpp_LFlags.QUALIFIED); 

            return msh.qualifiedLookup (name, flags);

        }



        

        // 3.4.3-3

        // any name coming after a qualified name in a declaration is looked 

        // up in the scope of the qualified name's class or namespace

        // anything before the qualified name is as normal

        // - This is handled by CTSymbolTable and the algorithm in this method



        // 3.4.3.1

        // 1. a conversion-type-id of an operator-function-id is looked up

        // both in the scope of the class and in the context in which 

        // the entire postfix-expression occurs and shall refer to the same

        // type in both contexts

        // - the approach currently taken is that since they 'shall refer

        //   to the same type', we can just look for it from the class scope.

        //   We don't need to check that the type is visible and equivalent

        //   in the pf-exp context.

        // 2. destructor name is looked up as specified in 3.4.3-5

        // - still need to detangle this one. Approach is to handle up 

        //   front via lookup context flag QUALDEST.

    }



    /**

     * Search local scope 

     * @param name the id to resolve

     * @param types constraints on the types of Declarations that will match

     * @see Clc.Analysis.Cpp_LFlags 

     */

    protected DeclarationSet searchLocalScope (String name, int types) {

        DeclarationSet matches = scope.get (name);

        if (matches == null) {

            d.msg (Debug.COMPILE, name + " has no matches in local scope");

            matches = new DeclarationSetMulti ();

        }

        if (types != Cpp_LFlags.ANY && !matches.isEmpty ()) {

            // whittle down results according to flags

            d.msg (Debug.COMPILE, name + " has match(es), whittling down result for types " + types);

            DeclarationSetMulti realMatches = new DeclarationSetMulti ();

            for (Enumeration e = matches.elements (); e.hasMoreElements (); ) {

                Declaration decl = (Declaration) e.nextElement ();

                if (decl.getCategory().intersects (types)) {

                    realMatches.addElement (decl);

                }

            }

            matches = realMatches;

        }

        return matches;

    }



    /**

     * Indicates whether any declarations matching <code>name</code> in enclosing (etc.) scopes  

     * are hidden by a declaration in this scope. 

     * <br><strong>note:</strong> you will generally use <code>searchLocalScope</code> to do a 

     * proper search for matches in this scope prior to using this method. 

     * @param name the id to check

     * @param flags any lookup flags - if the lookup is taking place for an id with an 

     * <em>elaborated type specifier</em>, the id will not be hidden by matches in this scope.

     */

    protected boolean hidden (String name, LFlags flags) {

        return (!(flags.contains (Cpp_LFlags.ELABTYPE) || scope.get (name) == null));

    }



    /**

     * Performs id resolution for an unqualified id.

     * <br>See specific implementations for details.

     * @param name the scoped name (id) to resolve

     * @param flags any additional constraints on the lookup are indicated here

     * @throws UndefinedSymbolException if the id doesn't resolve

     */

    protected abstract DeclarationSet unqualifiedLookup 

        (ScopedName name, LFlags flags) throws UndefinedSymbolException ;





    /** 

     * determine innermost shared scope between current scope and each using 

     * directive; this is where the names in the "used" namespace will be 

     * located for purposes of lookup (but not those defined at a later point 

     * in an extension namespace defn.)

     * <br>Note: Searching a "used namespace" also searches namespaces used by

     * that namespace (transitivity)

     * @param name the namespace id referred to in the using-directive

     */

    protected void processUsingDirective (ScopedName name) { 

        // 3.4.1-2

        // the declarations from the namespace nominated by a using-directive

        // become visible in a namespace enclosing it. The declarations are

        // considered part of the enclosing namespace for the purposes of 

        // lookup.



        // find matching namespace definition

   

        // add to usedNS Vector

    }



    /**

     * Add to local scope the id and matching <code>Declaration</code> 

     * referred to in the using-declaration.

     * @param name the id referred to in the using-declaration

     */

    protected void processUsingDeclaration (ScopedName name) { 

        // find matching namespace member



        // add to local scope

    }



    public void moveContents (ScopeHolder fromSH) {

        if (fromSH == null) return;

        // update the fully qualified id of each declaration

        for (Enumeration decls = fromSH.elements ();

             decls.hasMoreElements (); ) {

            Declaration decl = (Declaration) decls.nextElement ();

            ScopedName new_fqn = this.fqnToHere (decl.getUnqualifiedName ());

            d.msg (Debug.COMPILE, "parameter fqn was " + decl.getName ().getName () 

                   + ", is now " + new_fqn.getName ());

            decl.setName (new_fqn);

        }



        // rest of move procedure

        super.moveContents (fromSH);

    }





}







