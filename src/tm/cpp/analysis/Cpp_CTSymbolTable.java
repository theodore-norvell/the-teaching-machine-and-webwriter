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

import tm.clc.analysis.CTSymbolTable;
import tm.clc.analysis.Declaration;
import tm.clc.analysis.DeclarationSet;
import tm.clc.analysis.DeclarationSetMulti;
import tm.clc.analysis.Definition;
import tm.clc.analysis.FunctionDeclaration;
import tm.clc.analysis.LFlags;
import tm.clc.analysis.ScopeHolder;
import tm.clc.analysis.ScopedName;
import tm.clc.analysis.SpecifierSet;
import tm.clc.analysis.UndefinedSymbolException;
import tm.clc.ast.TyAbstractFun;
import tm.clc.ast.TypeNode;
import tm.cpp.parser.ParserConstants;
import tm.utilities.Assert;
import tm.utilities.Debug;

/**
 * Functions as the external interface to symbol table lookups
 * 
 * The ParserContext uses CTSymbolTable when it needs to perform a lookup, 
 * when it creates a new Declaration/Definition, and when it enters/leaves
 * a scope.
 * @author Derek Reilly
 * @version 1.0
 * @see tm.cpp.analysis.ParserContext
 * @created August 31, 2001
 */
public class Cpp_CTSymbolTable extends CTSymbolTable 
    implements ParserConstants, Cpp_Specifiers, Cpp_LFlags {

    // error messages
    /**
     * Error message provided when asked to set up a scope representation 
     * for an entity that doesn't contain a scope.
     */
    protected static final String NON_SCOPE_ENTITY = 
        "{0} is not recognized as an entity having scope.";

    private static final String NOT_A_CLASS = 
        "In {0}, id {1} refers to a {2}, not a class.";

    protected static final String UNKNOWN_SCOPE = 
        "Path {0} was not found";
    protected static final String NO_DECL_IN_SCOPE = 
        "No declaration of {0} found in {1}";

    /**
     * Error message indicating that a previous <code>Declaration</code> 
     * is required to perform the lookup
     */
    protected static final String PREV_DECLARATION_REQD = 
        "Previous declaration required to perform lookup in context {0}";

    /** indicates a missing feature */
    protected static final String NOT_IMPLEMENTED = 
        "Sorry, lookup for {0} is not implemented.";

    /** The last qualified declaration encountered in the current statement.
     * Used to accommodate lookup rules for declaration statements
     */
    protected Declaration lastQualifiedDeclaration;

    // last function prototype scope - used to add parameter ids to the 
    // function's outermost scope
    private ScopeHolder lastFunctionPrototypeScope;

    // lookup context - modifies compile time symbol table lookup behaviour
    private LFlags lookupContext = new LFlags ();

    /**
     * A reference to the global scope.
     */
    protected NamespaceSH globalScope;

    /** the set of all defined namespaces */
    protected Vector allNamespaces = new Vector ();


    /** 
     * Build the <em>runtime id</em> for this <code>Declaration</code>.
     * <br>The runtime representation of the id depends on the type of entity
     * that it is, but will uniquely identify the entity at runtime.
     * <br><strong>note:</strong> the runtime id for a particular declaration
     * can change depending on the context in which it is accessed. It is
     * important that the id be generated and used in the same context as 
     * that in which the declaration was located.
     * @param match the declaration that was matched in a prior id lookup - 
     * the runtime ID will be associated (temporarily) directly with this
     * declaration. 
     */
    protected void buildRuntimeId (Declaration match) {
        // If the match is a non-static data member, build the runtimeId.
        // Otherwise, the fqn or function key will be used.
        //debug.msg ("welcome to buildRuntimeId");
        if (match != null && ((CommonSH) currentScope).inClass () &&
            match.getCategory().intersects (Cpp_LFlags.VARIABLE) &&
            match.getEnclosingBlock () instanceof ClassSH && 
            ! (match.hasSpecifier (SP_STATIC))) {
            // get a reference to the class scope from which 
            // the id was used
            ScopeHolder classScope = currentScope;
            while (!(classScope instanceof ClassSH)) 
                classScope = (classScope instanceof BlockSH)
                    ? classScope.getEnclosingScope ()
                    : ((MemberFnSH) classScope).getOwningScope ();
            
            // locate the Declaration relative to this class
            // (builds and assigns the runtimeId)
            if (match.getEnclosingBlock () != classScope) {
                match.setRuntimeId (new Cpp_ScopedName ());
                ((ClassSH) classScope).locateRelativeToThis (match);
            }
        }
    }


    /**
     * Performs an id lookup under contextual constraints identified in 
     * the LFlags parameter
     * @param name the id as encountered (i.e. qualified only if so found in
     * the code
     * @param flags an LFlags object containing contextual info regarding the
     * encounter of the id, such as within a declaration statement.
     * @return a DeclarationSet containing all Declarations matching the id
     * under the current scope and following relevant lookup rules.
     */
    public DeclarationSet lookup (ScopedName name, LFlags flags) {
        DeclarationSet matches = null;

        flags = new LFlags (flags);
        flags.set (lookupContext);
        try {
            if (name.is_absolute () && !flags.intersects (Cpp_LFlags.CLASSREF)) { 
                matches = globalScope.lookup (name, flags);

                buildRuntimeId (matches.getSingleMember ());

            } else {
                switch (flags.get (Cpp_LFlags.LCONTEXT)) {
                case Cpp_LFlags.QUALDEST:
                    Assert.apology (NOT_IMPLEMENTED, "qualified destructors");
                    
                case Cpp_LFlags.CLASSREF:
                    // 3.4.5

                    // error checking
                    if (lastDeclaration == null) 
                        Assert.apology (PREV_DECLARATION_REQD, 
                                        flags.toString (Cpp_LFlags.LCONTEXT));

                    // in certain cases, begin lookup at the local scope:
                    // - id is qualified 
                    // - id is destructor call
                    // - previous declaration was of SCALAR type
                    DeclarationSet localMatches = new DeclarationSetMulti ();

                    if (name.is_qualified () ||
                        name.getTerminalId().startsWith (OpTable.get (TILDE)) ||
                        lastDeclaration.getCategory().contains (Cpp_LFlags.SCALAR)) {
                        localMatches = (name.is_absolute ()) 
                            ? currentScope.lookup (name, flags)
                            : globalScope.lookup (name, flags);
                    }

                    // if object expression is of class type, look in class scope
                    if (lastDeclaration.getDefinition () instanceof ClassSH) {
                        ClassSH classScope = 
                            (ClassSH) lastDeclaration.getDefinition ();
                        
                        matches = (name.is_absolute ())
                            ? localMatches
                            : classScope.lookup (name, flags);

                        // need to pick through the results in some cases:
                        // the possible scenarios are:
                        // 1. unqualified id
                        // 2. qualified id of the form class-or-namespace::...
                        // 3. qualified id of the form ::class-or-namespace::...

                        // 1. (unqualified-id) : results are ok as they are
                        if (name.is_qualified ()) {

                            // 2. (qualified-id of the form class-or-namespace::...) : prefer
                            //   class results over local scope results. 
                            if (!name.is_absolute () && matches.isEmpty ()) 
                                matches = localMatches;
                            

                            // 2 (con't) and 3 (globally qualified id)
                            // Need to validate as actual class attributes. 
                            // Need to build runtimeID.
                            // (actually, for (2) where class lookup produces results, we know
                            // that the results are class attributes. What we don't know is the 
                            // runtime ID to use for the member access. The single operation
                            // executed here performs both functions, so we do it regardless)
                            // ** currently we're not performing validation on methods. Instead,
                            //    we're looking at single member matches, and then validating
                            //    only if it's a data member. (TM is not a compiler..)
                            Declaration match = matches.getSingleMember ();
                            if (match != null &&
                                match.getCategory().intersects (Cpp_LFlags.VARIABLE)) {
                                match.setRuntimeId (new Cpp_ScopedName ());
                                if (!classScope.locateRelativeToThis (match)) {
                                    matches =  new DeclarationSetMulti ();
                                }
                            }
                        }

                    } else { // local matches are what we are interested in
                        matches = localMatches;
                    }
                    
                    break;

                case Cpp_LFlags.DECLSTAT:
                    // check local scope
                    matches = currentScope.lookup (name, flags);

                    // also search in context of last qualified decl
                    if (lastQualifiedDeclaration != null) {
                        Declaration lqd = lastQualifiedDeclaration;
                        // ?? not sure what 'context' means in the case of 
                        //    a declaration for something with scope (e.g. a 
                        //    function or class). Right now we begin lookup
                        //    from within that scope.
                        ScopeHolder lqdScope = 
                            (lqd.isScopeHolder ())
                            ? (ScopeHolder) lqd.getDefinition ()
                            : lqd.getEnclosingBlock ();
                        DeclarationSet lqdMatches = 
                            lqdScope.lookup (name, flags);
                        if (!lqdMatches.isEmpty ()) {
                            DeclarationSet combined = 
                                new DeclarationSetMulti ();
                            combined.append (matches);
                            combined.merge (lqdMatches);
                            matches = combined;
                        }
                    }
                    if (name.is_qualified ()) 
                        buildRuntimeId (matches.getSingleMember ());

                    break;

                case Cpp_LFlags.PRIORDECL: 
                    // local only if unqualified, normal qualified otherwise
                    matches = (name.is_qualified ()) 
                        ? currentScope.lookup (name, new LFlags 
                                               (flags.get (Cpp_LFlags.LELEMENT)))
                        : currentScope.lookup (name, flags);
                    break;

                default: // all other flags are handled downstream
                    //debug.msg ("default handling of lookup", debug.LOCATION, debug.HIGH);
                    matches = currentScope.lookup (name, flags);
                    if (name.is_qualified ()) 
                        buildRuntimeId (matches.getSingleMember ());
                    break;
                } 
            }
        } catch (UndefinedSymbolException use) {
            // just using apology for now, may throw instead for calling entity to handle
            Assert.apology (use.getMessage ());
        
        } finally {
            // reset all namespace 'visited' flags
            for (Enumeration nse = allNamespaces.elements (); 
                 nse.hasMoreElements (); )
                ((NamespaceSH) nse.nextElement ()).visited (false);
        }

        // update references to last qualified and last declaration.
        // if > 1 match, these will be set to null temporarily, and updated
        // after ambiguity resolution
        lastDeclaration = matches.getSingleMember ();
        if (name.is_qualified ()) 
            lastQualifiedDeclaration = matches.getSingleMember ();

        return matches;
    }

    /**
     * Gets the global scope.
     * @return the <code>NamespaceSH</code> instance representing the global scope
     */
    public NamespaceSH getGlobalScope () { return globalScope; }

    /** 
     * Add a declaration to the current scope, which is the 
     * <code>ScopeHolder</code>'s own <code>Declaration</code>. This 
     * method is appropriate for classes.
     * @param coords The source coordinates
     * @param head the specifiers and id for the class
     */
    public Declaration addDefiningDeclaration (ClassHead head) {
        // 
        // ** we are ignoring the ClassHead key (class/struct/union)..
        // we don't support union

        Declaration decl = 
            addDefiningDeclaration (head.get_name (), CLASS_LF, null);
        ClassSH classScope = (ClassSH) decl.getDefinition ();

        // add superclasses
        for (Enumeration e = head.get_specifiers().elements ();
             e.hasMoreElements (); ) {
            Cpp_SpecifierSet spec_set = (Cpp_SpecifierSet) e.nextElement ();
            ScopedName type_name = spec_set.get_type_name ();
            Assert.check (type_name != null);
            // ?? lookup context modifier required ?
            Declaration match = lookup (type_name).getSingleMember ();
            if (match == null) 
                Assert.apology (NO_MATCHING_TYPE, type_name.getName ());
            if (!(match.getCategory ().contains (Cpp_LFlags.CLASS))) 
                Assert.apology (NOT_A_CLASS, new String []
                                { "base class specifier", 
                                  match.getName().getName (),
                                  (match.getType () == null) 
                                  ? "no type set"
                                  : match.getType().getTypeString () });  

            classScope.addSuperclass ((ClassSH) match.getDefinition (), 
                                      spec_set);
            
        }

        return decl;
        
    }

    /**
     * Creates a <code>Declaration & ScopeHolder Definition</code> given the 
     * provided values. Performs appropriate lookups for prior 
     * <code>Declarations</code>, and is aware of the scope differences for 
     * qualified and unqualified 'defining declarations'. 
     * @param name the id (qualified or unqualified) to relate to the declaration
     * @param flags indicate the kind of entity (can be class (incl struct), 
     * namespace or function (regular or member))
     * @return a copy of the <code>Declaration</code> created
     */
    public Declaration addDefiningDeclaration (ScopedName name, LFlags flags,
                                               TypeNode type) {
        // create the definition (a scope holder)
        ScopeHolder defn = null; 
        int declType = flags.get (Cpp_LFlags.LELEMENT);
        switch (declType) {

        case Cpp_LFlags.MEM_FN:
            defn = new MemberFnSH (currentScope);
            ClassSH owning = memberOf (name);
            Assert.check (owning != null);
            ((MemberFnSH) defn).setOwningScope (owning);
            break;
        case Cpp_LFlags.REG_FN:
            defn = new RegularFnSH (currentScope);
            break;
        case Cpp_LFlags.CLASS:
            defn = new ClassSH (currentScope);
            break;
        case Cpp_LFlags.NAMESPACE:
            defn = new NamespaceSH (currentScope);
            allNamespaces.addElement (defn);
            break;
        default:
            Assert.apology (NON_SCOPE_ENTITY, flags.toString  ());
        }


        Declaration d; // the defining Declaration
        LFlags lookupFlags = new LFlags (declType + Cpp_LFlags.PRIORDECL);
        DeclarationSet results = lookup (name, lookupFlags);
        if (flags.intersects (Cpp_LFlags.FUNCTION)) { // function definition
            debug.msg (Debug.COMPILE, ">>> adding a function definition");
            // find previous declarations
            d = OverloadResolver.findMatch (results, (TyAbstractFun) type);
            if (d == null) { 
                // first time function was declared
                d = new FunctionDeclaration ( flags, name, defn, 
                                             new Cpp_SpecifierSet (), 
                                             (TyAbstractFun) type); 
                if (name.is_qualified ()) {
                    // if the declaration was qualified, only add it to the current
                    // scope if the qualification refers to this scope
                    qualifiedDefn (d, false);

                } else {
                    debug.msg (Debug.COMPILE, ">>> actually adding the fn defn to the scope");
                    this.addDeclaration (d);
                }

            } else { 
                // the function has been declared prior to this,
                // just set its definition and type attributes

                d.setDefinition (defn);
                d.setType (type);
            }
        } else { // class or namespace
            if (name.is_qualified ()) {
                // create a new declaration
                d = new Declaration ( flags, name, defn, null, type);

                // add the definition and type to all prior matching 
                // Declarations
                for (Enumeration e = results.elements (); 
                     e.hasMoreElements (); ) {
                    Declaration cdecl = (Declaration) e.nextElement ();
                    cdecl.setDefinition (defn);
                    cdecl.setType (type);
                }
                // if the declaration was qualified, only add it to the current
                // scope if the qualification refers to this scope
                qualifiedDefn (d, !(results.isEmpty ()));
            
            } else { 
                // unqualified name
                // unqualified multiple declarations within the same scope are 
                // treated by the lookup logic as a single Declaration (duplicates
                // are not added to the scope)
                d = results.getSingleMember (); 
                // create a new Declaration if no match found
                if (d == null) {
                    d = new Declaration ( flags, name, defn, null, type);
                    // add the declaration to the current scope 
                    this.addDeclaration (d);
                } else { 
                    // set the definition
                    d.setDefinition (defn);
                }
            }
        }

        // add the Declaration to the defn's scope (id is local to its own scope)
        defn.addOwnDeclaration (d);

        // copy over any parameter declarations to the function body outer
        // scope (if this is a function definition). We wait until this 
        // point to do this so that the fqn can be set correctly for each
        // parameter.
        if (flags.intersects (Cpp_LFlags.FUNCTION)) 
            defn.moveContents (lastFunctionPrototypeScope);
        
        return d;
    }

    /* qualified definition  -- perform some 
     * checks, possibly adding declaration to current scope
     */
    private void qualifiedDefn (Declaration d, boolean previousDecl) {
        // only add the defining decl to the current
        // scope if the qualification refers to this scope
        ScopedName path = new Cpp_ScopedName (d.getName ());
        path.removeTerminalId ();
        Declaration match = lookup (path).getSingleMember ();
        Assert.apology (match != null, UNKNOWN_SCOPE, 
                        path.getName ());
        if (match.getDefinition () == currentScope) 
            this.addDeclaration (d);
        // otherwise the function definition is not part of 
        // the current scope - trouble if it hasn't been declared in
        // the other scope
        else if (!previousDecl)
            Assert.apology (NO_DECL_IN_SCOPE, 
                            new String [] { d.getName().getTerminalId (), 
                                            path.getName ()});
    }

    /** longhand determination of class membership */
    protected ClassSH memberOf (ScopedName id) {
        ClassSH class_sh = null;
        if (id.is_qualified ()) {
            ScopedName qualId = new Cpp_ScopedName (id);
            qualId.removeTerminalId ();
            Declaration qualifier = lookup(qualId).getSingleMember ();
            if (qualifier.getCategory ().contains (Cpp_LFlags.CLASS)) { 
                class_sh = (ClassSH) qualifier.getDefinition ();
            } 
        } else if (currentScope instanceof ClassSH) {
            class_sh = (ClassSH) currentScope;
        }
        return class_sh;
    }

    /**
     * Determines what to do when a function declaration is encountered
     * during parsing. Will add a new <code>Declaration</code> if no 
     * previous one exists, otherwise will ignore for purposes of id lookup.
     * @param flags indicates function type
     * @param name the function id
     * @param params the parameter types
     * @return the <code>FunctionDeclaration</code> matching the name
     */
    public FunctionDeclaration newFunctionDeclaration (LFlags flags, 
                                                       ScopedName name,
                                                       Definition defn, 
                                                       SpecifierSet spec_set,
                                                       TyAbstractFun fType) {
        debug.msg (Debug.COMPILE, "Cpp_CTSymbolTable.newFunctionDeclaration");
        FunctionDeclaration d = null;

        // has the function been previously declared in this scope ?
        LFlags prev_declared = 
            new LFlags (Cpp_LFlags.PRIORDECL + Cpp_LFlags.FUNCTION);
        DeclarationSet prevDecls = lookup (name, prev_declared); 
        debug.msg (Debug.COMPILE, "looking for exact match among " + prevDecls.size () + 
                   " prior declarations");
        d = OverloadResolver.findMatch (prevDecls, fType);

        // if not, add the Declaration
        if (d == null) {
            debug.msg (Debug.COMPILE, "no previous declaration found for " + name.getName ());
            // error if name is qualified and we're not in the indicated scope
            // (declaration must exist in indicated scope also)

            d = new FunctionDeclaration ( flags, name, defn, spec_set, fType);

            addDeclaration (d);
        }

        return d;
    }


    /* javadoc inserted from base class */
    public void enterScope () {
        currentScope = new BlockSH ((CommonSH) currentScope);
    }

    /* javadoc inserted from base class */
    public void enterFileScope () {
        globalScope = new NamespaceSH ();
        currentScope = globalScope;
        allNamespaces.addElement (globalScope);
    }

    /* javadoc inserted from base class */
    public void exitFileScope () {
        globalScope = null;
        currentScope = null;
        allNamespaces.removeAllElements ();
    }

    /** 
     * Enter a "function prototype scope" - this will alter lookup context, 
     * and create a temporary scope to contain appropriate ids encountered
     * in the parameter list. 
     */
    public void enterFunctionPrototypeScope () {
        debug.msg (Debug.COMPILE, ">> Cpp_CTSymbolTable.enterFunctionPrototypeScope");
        this.enterScope ();
        lookupContext.set (Cpp_LFlags.DECLSTAT);
    }

    /** 
     * Leaves a "function prototype scope" - this will revert lookup context 
     * to its setting prior to the function prototype, return to the enclosing
     * scope, and store a copy of the function prototype scope in case this 
     * was encountered in the context of a function definition, in which case
     * its contents can be copied to the function's outermost scope.
     */
    public void exitFunctionPrototypeScope () {
        debug.msg (Debug.COMPILE, "leaving a function prototype scope");
        lastFunctionPrototypeScope = currentScope;
        this.exitScope ();
        lookupContext.unset (Cpp_LFlags.DECLSTAT);
    }

}





