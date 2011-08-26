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



import java.util.Enumeration;
import java.util.Vector;

import tm.clc.analysis.DeclarationSet;
import tm.clc.analysis.DeclarationSetMulti;
import tm.clc.analysis.Definition;
import tm.clc.analysis.LFlags;
import tm.clc.analysis.ScopeHolder;
import tm.clc.analysis.ScopedName;
import tm.clc.analysis.UndefinedSymbolException;
import tm.utilities.Assert;
import tm.utilities.Debug;



/** 

 * <code>ScopeHolder</code> implementation for functions (both methods and

 * regular functions) in C++. 

 * <ul>Handles :

 * <li>function scope (labels)

 * <li>function-prototype scope (parameter declaration clauses)

 * <li>local scope for functions (a function's outermost local scope)

 * </ul>

 * In addition, <em>friend</em> relationships and class/namespace membership

 * are managed here.

 */

public abstract class FunctionSH extends CommonSH implements Definition {



    /** 

     * the namespace/class this function is part of 

     * Because a function can be defined outside the namespace/class in which it is 

     * declared to be part of, we need to distinguish between enclosing scope

     * and 'owning' scope

     */

    protected ScopeHolder owningScope; 



    /**

     * the set of classes for which this function is declared to be a friend

     */

    protected Vector friends = new Vector ();



    /** 

     * Creates a new <code>FunctionSH</code> instance

     * @param encl the scope physically enclosing the function's definition.

     */

    public FunctionSH (ScopeHolder encl) { 

        super (encl);

        owningScope = enclosingScope; // this is the default

    }





    /** 

     * Fully qualify the id provided (as it is known in this scope)

     * <br>this will include the munged name of the function

     * @param id the id to qualify

     * @return the fully qualified id

     */

    protected ScopedName fqnToHere (String id) { 



        Assert.check (ownDeclaration != null);

        ScopedName fqn = new Cpp_ScopedName (ownDeclaration.getName ());

        fqn.setTerminalId 

            (OverloadResolver.munge (ownDeclaration.getName (), 

                                     ownDeclaration.getType (), 

                                     false));

        fqn.append (id);

        return fqn;

    }



    /** 

     * Set the <em>owning</em> scope, i.e. the scope representation of the 

     * entity that this function is most directly a member of, if any. This

     * will be a class or namespace.

     * @param scope the class or namespace scope

     */

    public void setOwningScope (ScopeHolder scope) { owningScope = scope; } 

    /** 

     * Provides the scope representation of the entity to which this function

     * most directly belongs, or <code>null</code> if none has been set

     */

    public ScopeHolder getOwningScope () { return owningScope; } 



    /**

     * Indicates that this function is a <code>friend</code> of the indicated

     * class. 

     * @param csh the scope representation of a class for which this function 

     * has been declared to be a friend of. 

     */

    public void addFriend (ClassSH csh) { friends.addElement (csh); }



    /** 

     * Unqualified lookup implementation for functions

     */

    protected DeclarationSet unqualifiedLookup (ScopedName name, LFlags flags) 

        throws UndefinedSymbolException {



        d.msg (Debug.COMPILE, "welcome to function unqualifiedLookup"); 



        DeclarationSet matches = null;



        // is this function an inline friend function? This should be an attribute,

        // the friends Vector doesn't change once built.

        boolean inlineFriend = friends.contains (enclosingScope);



        // 3.4.1-1 

        // scopes are searched for a declaration in the order listed in each

        // respective category; lookup ends as soon as a declaration is found



        if (flags.contains (Cpp_LFlags.LABEL)) {

            // 3.3.4

            // if name is a label

            // search function scope or 

            // 1. search local scope, return decl if found

            // 2. if not found, create a declaration for the label (assume it's ok)

            Assert.apology ("Sorry, labels not yet implemented");



        } else {



            // this is the sequence we're following (needs to be verified for

            // all cases)

            // 1. local scope

            // 2. owning scope (owning namespace or class) or inline friend

            //    scope (if defined as inline friend)

            // 3. enclosing scope if not inline friend and different from owning

            // 4. check integrity of results

            // 5. check using directive matches

            // 6. determine final match set

            

            // get the part of the ScopedName that we're concerned with

            // a fn could only be concerned with the first part (unqualified)

            String part = name.selectedPart ();

            d.msg (Debug.COMPILE, "searching for " + part);



            // STEP 1

            // 3.4-2 

            // search local scope - all declarations up to this point

            // using-declarations will have been added to the local scope (7.3.3-2)

            matches = (name.index.terminal ()) 

                ? searchLocalScope (part, flags.get (Cpp_LFlags.LELEMENT))

                : searchLocalScope (part, Cpp_LFlags.QUALIFIER);





            if (!hidden (part, flags)) { // target entity type not hidden by declared entity in this scope

                // STEP 2

                if (matches.isEmpty ()) { 

                    if (inlineFriend) {

                        d.msg (Debug.COMPILE, "inline friend, searching enclosing"); 

                        matches = 

                            ((CommonSH)enclosingScope).unqualifiedLookup(name, flags);

                        

                    } else if (owningScope != enclosingScope) {

                        d.msg (Debug.COMPILE, "defined outside owning scope, searching owning");

                        // 3.4.1-6, 3.4.1-9

                        // if declared as part of a class/namespace, but defined outside the

                        // scope of that class/namespace (in a different namespace)

                        // search the scope of the owning class/namespace

                        

                        // we would like to search up to the scope

                        // enclosing both the ns and the fn defn

                        // this could be determined when the owningScope is set. This is 

                        // the same as friend classes - maybe we could create a new class for

                        // this type of relationship

                        

                        matches = ((CommonSH)owningScope).unqualifiedLookup (name, flags);

                    }

                }

                

                

                // STEP 3

                // 3.4.??? 

                // continue to the enclosing scope up to global scope

                // using-directives will alter behaviour (7.3.4-1)

                if (!inlineFriend &&

                    (matches.isEmpty () ||

                     this.enclosedBy (matches.getFirstMember().getEnclosingBlock()))) {

                    d.msg (Debug.COMPILE, "searching enclosing scope");

                    // need to watch here - we're only searching up to but

                    // not including the first enclosing scope with a 

                    // previously located match. If nothing is found here, we want

                    // to keep the previously found match(es); otherwise we 

                    // want to replace the previously found match(es).

                    DeclarationSet enclMatches = 

                        ((CommonSH)enclosingScope).unqualifiedLookup (name, flags);

                    if (!enclMatches.isEmpty ()) matches = enclMatches;

                    

                }

                

                // STEP 4

                // check matches for unresolvable ambuiguity

                // we should only have >1 result for overloaded functions, and 

                // for type & var (etc) in same scope

                // in either case, these results should all have been found in the same scope ????

                

                // STEP 5

                // search used namespaces

                if (!usedNS.isEmpty ()) {

                    d.msg (Debug.COMPILE, "searching used namespaces");

                    DeclarationSetMulti nsMatches = new DeclarationSetMulti ();

                    for (Enumeration nse = usedNS.elements (); 

                         nse.hasMoreElements (); ) {

                        d.msg (Debug.COMPILE, "searching used ns");

                        CommonSH ns = (CommonSH) nse.nextElement ();

                        nsMatches.merge (ns.unqualifiedLookup (name, flags));

                    }

                    

                    // STEP 5 (b) - check integrity of used ns results

                    

                    // If we have more than one result here, we have an ambiguity 

                    // error unless they refer to the same thing. 

                    // Assume this is checked for .. HERE?

                    

                    // STEP 6

                    // determine final match set (NOOP if no used namespaces)

                    // using directives insert names in the nearest enclosing namespace

                    // for the purposes of name lookup, classes are namespaces

                    // comparing location of results with location of

                    // existing matches to determine hiding

                    

                    

                }

            }



        }



        return matches;

    }



}





