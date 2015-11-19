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

import tm.clc.analysis.Declaration;
import tm.clc.analysis.DeclarationSet;
import tm.clc.analysis.DeclarationSetMulti;
import tm.clc.analysis.Definition;
import tm.clc.analysis.FunctionDeclaration;
import tm.clc.analysis.LFlags;
import tm.clc.analysis.Scope;
import tm.clc.analysis.ScopeHolder;
import tm.clc.analysis.ScopedName;
import tm.clc.analysis.SpecifierSet;
import tm.clc.analysis.UndefinedSymbolException;
import tm.clc.ast.TyAbstractArray;
import tm.clc.ast.TyAbstractClass;
import tm.clc.ast.TyAbstractPointer;
import tm.clc.ast.TypeNode;
import tm.cpp.ast.TyRef;
import tm.cpp.parser.ParserConstants;
import tm.utilities.Assert;
import tm.utilities.Debug;


/** 
 * Represents the scope of a class definition for id resolution.
 * <p>This class provides searching functionality above the usual
 * search by id. Additionaly, searches can be performed for specific types 
 * of entities within the class scope, including base classes, constructors, 
 * data members, and finer gradations via combination of search criteria.
 */
public class ClassSH extends CommonSH 
    implements Definition, ParserConstants, Cpp_Specifiers, Cpp_LFlags {

    private static final String RELATIVE_PATH_FN = 
        "relative paths are not built for function declarations";

    /** set of superclass scopes, as <code>ClassSH</code> instances */
    protected Vector superclasses = new Vector ();
    /** set of modifiers for each superclass, as <code>SpecifierSet</code>s */
    protected Vector scmodifiers = new Vector ();
    /** set of friends */
    protected Vector friends = new Vector ();


    /** 
     * the namespace this class is a member of 
     * Because a class can be defined outside the namespace it is 
     * declared to be part of, we need to distinguish between enclosing scope
     * and 'owning' scope
     */
    protected ScopeHolder owningScope; 


    /** 
     * Creates a new instance
     * @param encl the physically enclosing scope (distinct from class 
     * hierarchy
     */
    public ClassSH (ScopeHolder encl) { 
        super (encl); 
        inClassDefinition = true;
        owningScope = enclosingScope;
    }

    /** 
     * Specifies a distinct <em>owning scope</em>, which is the scope to which
     * the class scope belongs, and is not necessarily physically enclosed 
     * by. 
     * <br>For example, the class may be a namespace member, but be 
     * defined outside the namespace scope.
     * @param owning the owning scope.
     */
    public void setOwningScope (ScopeHolder owning) { owningScope = owning; }

    /** 
     * Adds a superclass 
     * @param sc the superclass' scope
     * @param modifiers any inheritance modifiers
     */ 
    public void addSuperclass (ScopeHolder sc, SpecifierSet modifiers) { 
        superclasses.addElement (sc);
        scmodifiers.addElement (modifiers);
    }

    /**
     * Provides the set of all superclass scopes
     * @return the superclass scopes (<code>ScopeHolder</code>s)
     */
    public Vector getSuperclasses () { return superclasses; }

    /** 
     * Add the <code>Declaration</code> that corresponds to this class
     * scope, to the class scope (its own name is considered an id both 
     * visible to and part of its scope)
     * @param decl the class' <code>Declaration</code>
     */
    public void addOwnDeclaration (Declaration decl) {
        super.addOwnDeclaration (decl);
        classDeclaration = this.getOwnDeclaration ();
    }

    /**
     * Adds the <code>Declaration</code> to this scope
     * <p>Sets the <code>Declaration's</code> enclosing block to be this scope
     * <br>the fully qualified name will be constructed also.
     * @param decl the declaration to add
     */
    public void addDeclaration (Declaration decl) {
        super.addDeclaration (decl);
        decl.getCategory().set (CLASS_MEMBER);
    }

    /** This is here so that inner classes can access the
    * scope variable */
    private Scope getScope() { return scope ; }

    /** 
     * Indicates whether the provided declaration can be located relative 
     * to this class, building the relative runtimeId of the Declaration 
     * along the way. This method is not available to function declarations,
     * which have a different runtimeId format - use buildRelativePath instead. 
     * @param decl the <code>Declaration</code> to locate
     * @return <code>true</code> if found, <code>false</code> otherwise 
     */ 
    protected boolean locateRelativeToThis (Declaration decl) {
        if (decl instanceof FunctionDeclaration)
            Assert.apology (RELATIVE_PATH_FN);
        // I can cast to ScopedName here, we don't have a FunctionDeclaration
        ScopedName rtid = (ScopedName) decl.getRuntimeId ();
        if (rtid == null) { 
            rtid = new Cpp_ScopedName ();
            decl.setRuntimeId (rtid);
        }
                
        return buildRelativePath (decl, rtid);
    }

    /**
     * Builds a relative path from this class to the declaration
     * -- for details, see CLC documentation
     * @param decl the <code>Declaration</code> to locate
     * @param p the path information will be stored here
     * @return <code>true</code> if found, <code>false</code> otherwise 
     */
    protected boolean buildRelativePath (Declaration decl, ScopedName p) {
        boolean found = false;		
        DeclarationSet matches = searchLocalScope 
            (decl.getName().getTerminalId (), decl.getCategory().getRawVal ());
        if (!matches.contains (decl)) {
            // search superclasses
            for (int i = 0; i < superclasses.size () && !found; i++) {
                ClassSH sc = (ClassSH) superclasses.elementAt (i);
                p.append (Integer.toString (i));
                if (sc.buildRelativePath (decl, p)) found = true;
                else p.removeTerminalId ();
            }
        } else { 
            found = true;
        }
        return found;
    }

    /** 
     * Gives the "reverse-qualified scoped name", which is the qualified
     * id representation of the runtime id for non-static data members.
     * <br>Behaviour is undefined if used for any other type of class 
     * member, or with a declaration that is not in this class' scope.
     */
    public ScopedName reverseQualify (Declaration d) {
        ScopedName r_qual = new Cpp_ScopedName ();
        int [] path = d.getRelativePath ();
        ClassSH cscope = this;
        for (int i = 0; i < path.length; i++) {
            int bc_idx = path [i];
            Assert.check (bc_idx < cscope.superclasses.size ());
            cscope = (ClassSH) cscope.superclasses.elementAt (bc_idx);
            r_qual.append (cscope.getOwnDeclaration().getUnqualifiedName ());
        }
        return r_qual;
    } 

    /**
     * Compare the 'precedence' of two ScopeHolders.
     * This is in relation to lookup rules for names used in a class
     * definition or member function. Hiding is enforced where 
     * name equivalence exists between two Declarations in scopes of
     * unequal precedence. The order of precedence is as follows:
     * <ol><li>class scope 
     * <li>function or block scope
     * <li>namespace scope</ol>
     * @param a first scope
     * @param b second scope
     * @return -1 if precedence(first) < precedence(second), 1 if >, 0 if ==
     */
    protected int comparePrecedence (ScopeHolder a, ScopeHolder b) {
        if (a instanceof ClassSH) {
            if (b instanceof ClassSH) return 0;
            else return 1;
        } else if (a instanceof NamespaceSH) {
            if (b instanceof NamespaceSH) return 0;
            else return -1;
        } else {
            if (b instanceof ClassSH) return -1;
            else if (b instanceof NamespaceSH) return 1;
            else return 0;
        }
    }

    
    /* javadoc from base class */
    protected DeclarationSet unqualifiedLookup (ScopedName name, LFlags flags)
        throws UndefinedSymbolException {
        return unqualifiedLookup (name, flags, new Cpp_ScopedName ());
    }

    /** 
     * Performs lookup for an unqualified id, while building the runtime id
     * @param name the unqualified id
     * @param flags lookup flags
     * @param runtimeId the associated runtime id, which is built as the 
     * lookup traverses the class hierarchy.
     */
    protected DeclarationSet unqualifiedLookup (ScopedName name, LFlags flags, 
                                                 ScopedName runtimeId) 
        throws UndefinedSymbolException {

        d.msg (Debug.COMPILE, "welcome to class unqualified lookup");

        DeclarationSet matches = null;

        // initial version - usual behaviour
        // STEP 1 - local scope to point of encounter (doesn't work for fn,
        // nested class defn - fn requires the entire scope).
        // STEP 2 - class hierarchy
        // STEP 3 - ns hierarchy if a direct ns member defined outside ns
        // STEP 4 - enclosing: can be class, ns, fn, block 

        // STEP 1 - local scope to point of encounter
        // 3.4.1-7
        String part = name.selectedPart ();
        d.msg (Debug.COMPILE, "looking for " + part);
        matches = (name.index.terminal ()) 
            ? searchLocalScope (part, flags.get (LELEMENT))
            : searchLocalScope (part, QUALIFIER);

        if (!matches.isEmpty ()) {
            d.msg (Debug.COMPILE, "found match(es) in class scope");
            Declaration dm = matches.getSingleMember ();
            if (dm != null && dm.getCategory().intersects (VARIABLE)) {
                d.msg (Debug.COMPILE, "single variable match in class scope");
                // if the match is a non-static variable (there can be only one)
                // set the runtimeId of the Declaration (a volatile attribute that
                // only has legitimacy in the context of this particular lookup)
                if (!dm.hasSpecifier (SP_STATIC)) 
                    dm.setRuntimeId (runtimeId);
            }
        }

        if (!hidden (part, flags)) { // target entity type not hidden by declared entity in this scope
            // STEP 2 - class hierarchy
            if (matches.isEmpty () && !superclasses.isEmpty () 
                && !flags.intersects (LOCAL)) {
                d.msg (Debug.COMPILE, "searching class hierarchy");
                // indicate that we are performing a subclass search
                LFlags scFlags;
                if (!flags.contains (CLASSHIER)) {
                    d.msg (Debug.COMPILE, "setting classhier flag");
                    scFlags = new LFlags (flags);
                    scFlags.set (CLASSHIER);
                } else {
                    scFlags = flags;
                }
                
                matches = new DeclarationSetMulti ();
                for (int i = 0; i < superclasses.size (); i++) {
                    ClassSH sc = (ClassSH) superclasses.elementAt (i);
                    
                    // Add the relative position of this superclass to the 
                    // runtimeId for non-static data member matches.
                    // ** note that this does not use the absolute / collapsed
                    // positioning of subobjects, but a path via immediate
                    // parents to the desired data member
                    runtimeId.append (Integer.toString (i));
                    DeclarationSet scmatches = 
                        sc.unqualifiedLookup (name, scFlags, runtimeId);
                    if (!scmatches.isEmpty ()) {
                        
                        // we need to enforce hiding rules here : 3.4.1-7, 3.4.1-8
                        // matches found in a class defn have highest precedence
                        // followed by matches found in a function (local classes)
                        // (matches in namespaces enclosing superclasses are ignored)
                        //
                        // if lookup via one branch of the class hierarchy returns
                        // results from a scope of lower precedence than those 
                        // returned via lookup from another branch, those results 
                        // with lower precedence are discarded.
                        //
                        // This is accomplished by comparing the enclosing scope of
                        // the first element from the current superclass' lookup 
                        // results with the first element of those found via other 
                        // superclasses so far, and updating matches
                        
                        if (!matches.isEmpty ()) {
                            ScopeHolder scMatchSc = 
                                scmatches.getFirstMember().getEnclosingBlock ();
                            ScopeHolder exMatchSc = 
                                matches.getFirstMember().getEnclosingBlock ();
                            switch (comparePrecedence (scMatchSc, exMatchSc)) {
                            case -1: // current class matches lower precedence
                                // ignore these results
                                break;
                            case 0: // current class matches same precedence
                            // merge matches
                                matches.merge (scmatches);
                                break;
                            case 1: // current class matches higher precedence
                                // replace matches 
                                matches = scmatches;
                                break;
                            }
                        } else { // append matches
                        matches.append (scmatches); 
                        }
                    }
                }
                
            }
            
            // STEP 3 - ns hierarchy if a direct ns member defined outside ns
            // not searched if lookup originated from a subclass
            if (matches.isEmpty ()  && 
                owningScope != enclosingScope &&
                !flags.intersects (INTERNAL)) {
                d.msg (Debug.COMPILE, "searching owning ns hierarchy");
                matches = 
                    ((CommonSH) owningScope).unqualifiedLookup (name, flags);
            }
            
            // STEP 4 - enclosing scope: can be class, ns, fn, block
            // if lookup originated from a subclass, we stop here
            // if enclosed by a base class, we do not stop here: previous (fruitless)
            // search of base class was class hierarchy only, not chain of enclosing
            // scopes.
            if (matches.isEmpty () &&
                (!flags.intersects (INTERNAL))) {
                d.msg (Debug.COMPILE, "searching enclosing scope");
                matches = 
                    ((CommonSH) enclosingScope).unqualifiedLookup (name, flags);
            }

            // if this search is not occurring via a friend relationship or
            // superclass relationship
            // include friend designations when determining names in scope
            
            // ?? using-declarations ?? - are treated as in class scope?
            
            //		if (!name.is_qualified ()) {
            // 3.4.1.12
            // if name encountered in the definition of a static data member
            // search entire class scope
            
            // else
            // 3.4.1.7 
            // search local scope - all declarations up to this point

            
            // search class hierarchy
            
            // if nested class
            // search enclosing class to point of encounter
            // search hierarchy of enclosing class
            // continue up chain of enclosing classes 
            // if the outermost is an inner class, search enclosing blocks to 
            // point of encounter
            
            // otherwise
            // search enclosing blocks up to point of encounter
            
            // ?? 3.4.1.7 - note ?? - does this include class hierarchies??
            // if name is a class or function introduced in a friend declaration,
            // only search the innermost enclosing namespace scope up to 
            // point of encounter
            // else
            // search from the innermost enclosing namespace up to point of 
            // encounter
            // continue to global scope (the global/file namespace)
            //		} else { // qualified name
            // qualified lookup - rules 
            // 3.4.3.1
            // if the current scope is a class
            // look up in scope of class 
            // ?? not sure what note means : a class member can be referred to
            // using a qualified-id at any point in its potential scope (3.3.6)
            // unless
            // - a destructor name - follow 3.4.3.5 (below)
            // - a conversion-type-id of an operator-function-id is looked up
            // in the class scope but also in the context in which the entire
            // expression occurs and shall refer to the same type in both
            // contexts
            // - the template-arguments of a template-id - not implemented
            
            // 3.4.3.5 - destructors
            // class names are looked up in the scope designated by the nested-
            // name-specifier. Examples:
            // A::B::~B() (where B is a class)
            // - look in scope of namespace A
            // - don't get this - why not look in class B anyway? - see third example
            // AB::~AB() (AB is a typedef class alias for class A)
            // - should find ~A()
            // - ?? does A need to be defined in the same enclosing scope?
            // I1::~I2() (I1 and I2 are typedefs of class C)
            // - look in enclosing scope... I suppose this will find C's destructor?
            //		}
        }
        return matches;
            
    }

    /** 
     * Performs lookup for a set of entities matching the provided query.
     * <br>This can be, for example, the set of all constructors, all 
     * <em>POD</em> base classes, all non-static data members, etc.
     * @param q the query, containing specialized flags for class element
     * lookup. These flags are defined in <code>Cpp_LFlags</code>, and 
     * are prefixed with <em>M_</em>.
     * @return the set of <code>Declaration</code>s matching the query.
     */
    public DeclarationSet lookup (LFlags q) { 
        d.msg (Debug.COMPILE, "performing class element lookup");
        return new ElementQuery (q, this).lookup (); 
    }

    /** 
     * Performs lookup for a set of entities matching the provided queries.
     * Each query is executed independently, and matching elements 
     * are simply <em>appended</em> to prior matches, not merged.
     * @param qs the set of queries to perform
     * @return the set of <code>Declaration</code>s matching at least one of
     * the queries.
     */
    public DeclarationSet lookup (LFlags [] qs) {
        DeclarationSet matches = new DeclarationSetMulti ();
        for (int i = 0; i < qs.length; i++) matches.append (lookup (qs [i]));
        return matches; 
    }



    /** 
     * Used to ask for a specific range of class elements, including
     * various data members, member functions, inner and base classes.
     * <p>Normal lookup is based on id. This class provides a uniform way 
     * of getting <code>Declaration</code>s based on other criteria.
     * <p><code>Declaration</code>s must match <strong>all</strong> criteria
     * to count as a match; the criteria themselves can be generic.
     * <br>The exception to this rule are special member functions; all 
     * special member functions can be retrieved by specifying
     */
    public class ElementQuery {
        
        SpecifierSet specifiers = new Cpp_SpecifierSet ();
        LFlags category = new LFlags (); // matches must have all flags set
        LFlags group = new LFlags (); // matches must have some matching flags
        int entityType = 0;
        LFlags query;
        ClassSH this_cs;

        /** 
         * Create a new query 
         * @params q the flags comprising the query parameters
         */
        public ElementQuery (LFlags q, ClassSH back_ref) {
            query = q;
            setCategory (); 
            addSpecifiers ();
            this_cs = back_ref;
        }

        // determines what type of entity is being searched for.
        // all queries MUST have an entity type
        private void setCategory () {
            // determine class entity
            if (query.contains (M_BASE_CLASS)) entityType = M_BASE_CLASS;
            else entityType = query.get (M_ENTITY_TYPE);

            switch (entityType) {
            case M_DATA_MEMBER :
                category.set (VARIABLE);
                break;
            case M_MEMBER_FN :
                category.set (MEM_FN);
                break;
            case M_BASE_CLASS :
                category.set (Cpp_LFlags.CLASS);
                break;
            case M_INNER_CLASS :
                category.set (Cpp_LFlags.CLASS);
                break;
            case M_FRIEND :
                // friends not currently supported
                Assert.apology (NOT_IMPLEMENTED, "friend");
                break;
            case M_TYPEDEF :
                // typedef not currently supported
                Assert.apology (NOT_IMPLEMENTED, "typedef");
                break;
            default :
                break;
            }
            
            // additional category flags
            if (query.contains (M_USER_DEFINED)) 
                category.set (USER_DEFINED);
            if (query.contains (M_POD)) 
                group.set (POD);
            if (query.contains (M_NON_POD)) 
                group.set (NON_POD);
        }

        // add specifiers - matches must contain all of the specifiers
        // present
        private void addSpecifiers () {
            if (query.contains (M_STATIC)) 
                specifiers.add (SP_STATIC);
            // auto, a default, is handled differently
            if (query.contains (M_VIRTUAL)) 
                specifiers.add (SP_VIRTUAL);
            if (query.contains (M_EXPLICIT)) 
                specifiers.add (SP_EXPLICIT);
            if (query.contains (M_FRIEND)) 
                specifiers.add (SP_EXPLICIT);
            if (query.contains (M_PRIVATE)) 
                specifiers.add (SP_PRIVATE);
            if (query.contains (M_PROTECTED)) 
                specifiers.add (SP_PROTECTED);
            if (query.contains (M_PUBLIC)) 
                specifiers.add (SP_PUBLIC);
            if (query.contains (M_CONST)) 
                specifiers.add (SP_CONST);
        }


        /**
         * Find all members matching the query. To match, a member must
         * have all qualities identified in the query.
         * @return the matching <code>Declaration</code>s
         */
        public DeclarationSet lookup () {
            DeclarationSet matches = new DeclarationSetMulti ();
            switch (entityType) {
            case M_BASE_CLASS :
                // use superclasses
                if (query.get (M_ENTITY_TYPE) == M_BASE_CLASS) {
                    // just a base class query

                    // order of traversal is always according to order of 
                    // appearance in class declaration
                    for (int i = 0; i < superclasses.size (); i++) {
                        ClassSH superclass = 
                            (ClassSH) superclasses.elementAt (i);
                        
                        
                        Declaration decl = superclass.getOwnDeclaration ();
                        SpecifierSet sc_spec_set = 
                            (SpecifierSet) scmodifiers.elementAt (i);
                        
                        // query modifiers: virtual, private/public/protected
                        // these are all in specifiers
                        if (sc_spec_set.contains (specifiers)) 
                            addMatches (matches, decl);
                        
                        if (query.contains (M_RECURSE)) 
                            addRecursiveMatches (matches, decl);
                    } 
                } else {
                    // looking at base class elements
                    LFlags goodsQuery = new LFlags (query);
                    goodsQuery.unset (M_BASE_CLASS);
                    for (int i = 0; i < superclasses.size (); i++) {
                        ClassSH superclass = 
                            (ClassSH) superclasses.elementAt (i);
                        
                        matches.append (superclass.lookup (goodsQuery));
                        
                        if (query.contains (M_RECURSE)) 
                            matches.append (superclass.lookup (query));
                    } 
                }
                break;
            case M_FRIEND :
                // use friends
                break;
            default :
                // use scope
                for (Enumeration ms = getScope().elements (); 
                     ms.hasMoreElements (); ) {
                    Enumeration ms2 = 
                        ((DeclarationSet) ms.nextElement ()).elements ();
                    while (ms2.hasMoreElements ()) {
                        // for each declaration in the scope
                        Declaration mem = (Declaration) ms2.nextElement ();
                        if (basicChecksPass (mem)) {
                            // do any other tests appropriate for the entity 
                            // type
                            switch (entityType) {
                            case M_MEMBER_FN:
                                if (!(mem instanceof FunctionDeclaration)
                                    || !(memberFnChecksPass 
                                         ((FunctionDeclaration) mem))) break;
                                // else fall through
                            default:
                                // this is a match, add it
                                addMatches (matches, mem);
                                break;
                            }
                        }
                        // if we need to recurse, process this declaration's
                        // inner elements (if any)
                        if (query.contains (M_RECURSE)) 
                            addRecursiveMatches (matches, mem); 
                    }
                }
                break;
            }
            return matches;
        }

        // basic checks that apply to most searches
        private boolean basicChecksPass (Declaration mem) {
            TypeNode type = null; // ** never set.. future expansion ??
            /*
            d.msg ("** basic checks for declaration " + mem.getName ());
            d.msg ("** declaration type " + mem.getType().getTypeString ());
            if ((group.getRawVal () == 0 ||  
                 mem.getCategory ().intersects (group))) {
                d.msg ("** declaration category intersects set specified in group");
                if ((category.getRawVal () == 0 ||
                     mem.getCategory ().contains (category))) {
                    d.msg ("** declaration category contains set specified in category");
                    if (mem.getSpecifiers ().contains (specifiers)) {
                        d.msg ("** declaration's specifiers contain set in specifiers");
                        if (type == null || 
                            type.getClass().isAssignableFrom 
                            (mem.getType().getClass ())) {
                            d.msg ("** no type restriction or declaration type instanceof type");

                        }
                    }
                }
            }
            */
            return (
                    // declaration category intersects set specified
                    // in group
                    (group.getRawVal () == 0 ||
                     mem.getCategory ().intersects (group)) && 
                    // declaration category contains set specified
                    // in category
                    (category.getRawVal () == 0 ||
                     mem.getCategory ().contains (category)) &&
                    // declaration's specifiers contain set in 
                    // specifiers
                    mem.getSpecifiers ().contains (specifiers) &&
                    // no type restriction or declaration type 
                    // instanceof type
                    (type == null || 
                     type.getClass().isAssignableFrom 
                     (mem.getType().getClass ())) &&
                    // auto by default, can't get both in one query
                    ((!(query.contains (M_AUTO))) ||
                     (query.contains (M_AUTO) && 
                      (!(mem.getSpecifiers ().contains (SP_CONST)))))
                    &&
                    // - handling of static specifier is upstream
                    ((query.contains (M_STATIC)) ||
                     !(mem.getSpecifiers ().contains (SP_STATIC)))
                    ); 
        }

        // add matches from one set to another
        private void addMatches (DeclarationSet to, DeclarationSet from) {
            if (query.contains (M_DISTINCT)) to.merge (from);
            else to.append (from);
        }
        
        // add matches from a declaration to the list of matches so far.
        // this will recurse into sub elements
        private void addRecursiveMatches (DeclarationSet to, 
                                          Declaration from) {
            // if a class or class object, this means look at members
            // if an array, look at element type
            // if a pointer, look at pointee type
            // if a reference, look at type of referenced object
            // anything else (function, typedef, enum, fundamental type) NO_OP

            LFlags cat = from.getCategory ();
            if (! cat.contains (Cpp_LFlags.TYPEDEF)) {
                TypeNode t = base_element_type (from.getType ());
                if (t instanceof TyAbstractClass) {
                    Declaration icdecl = 
                        Cpp_CTSymbolTable.getClassDeclaration 
                        ((TyAbstractClass) t);
                    ClassSH icdefn = (ClassSH) icdecl.getDefinition ();
                    if (icdefn != this_cs)
                        addMatches (to, icdefn.lookup (query));
                } // otherwise do nothing
            }
        }


        private TypeNode base_element_type (TypeNode t) {
            if (t instanceof TyAbstractPointer)
                return base_element_type 
                    (((TyAbstractPointer) t).getPointeeType ());
            else if (t instanceof TyAbstractArray) 
                return base_element_type
                    (((TyAbstractArray) t).getElementType ());
            else 
                return t;
        }
            
        // performs checks that apply to member functions
        private boolean memberFnChecksPass (FunctionDeclaration mem) {
            // most attributes are already determined by regular means:
            // - private/protected/public, cvq, extern, static/auto, 
            //   inline, friend, virtual, explicit, user_defined

            Declaration cd = getOwnDeclaration ();
            TypeNode ct = cd.getType ();
            String cname = cd.getUnqualifiedName ();
            String mname = mem.getUnqualifiedName ();
            int param_nd = mem.firstParamWithoutDefaultValue ();
            boolean pass = true;
            /*
            d.msg ("** member function check");
            d.msg ("** class name : " + cname);
            d.msg ("** method name : " + mname);
            d.msg ("** class type : " + ct.getTypeString ());
            if (param_nd >= 0) {
                d.msg ("** first non-default param type : " 
                       + mem.getParameter (param_nd).getTypeString ());
            } else {
                d.msg ("** no parameters");
            }
            */
            // constructors
            if (query.contains (M_CONSTRUCTOR)) {
                pass = !
                    ((!cname.equals (mname)) ||
                     // default constructor
                     (query.contains (M_DEFAULT) &&
                      (!(mem.defaultValuesAfter (-1)))) ||
                     // copy constructor
                     (query.contains (M_COPY) &&
                      ((!(param_nd >= 0 && mem.defaultValuesAfter (param_nd))) ||
                       (!(mem.getParameter (param_nd) instanceof TyRef)) ||
                       (!(((TyRef) mem.getParameter (param_nd)).getPointeeType ()
                          .equal_types (ct))))) || /*CHECK*/
                     // conversion constructor
                     (query.contains (M_CONVERSION) &&
                      (!(param_nd >= 0 && mem.defaultValuesAfter (param_nd))))
                     );
            } else if (query.contains (M_DESTRUCTOR)) {
                // destructor
                pass = mname.equals (OpTable.get (TILDE) + cname);
            } else if (query.contains (M_OPERATOR)) {
                pass = mname.startsWith (OpTable.OPERATOR_S);
                String opname = 
                    (pass) 
                    ? mname.substring (OpTable.OPERATOR_S.length ()).trim()
                    : null;
                if (pass && query.contains (M_COPY)) {
                    // copy assignment operator
//					d.msg ("testing for copy assignment operator match against " + mname);
                    pass = !
                      ((!(opname.equals (OpTable.get (ASSIGN)))) ||
                        (mem.getParameterCount () != 1) ||
                        (!((mem.getParameter (0).equal_types (ct)) || /*CHECK*/
                           ((mem.getParameter (0) instanceof TyRef) &&
                            (((TyRef) mem.getParameter (0)).getPointeeType ()
                             .equal_types (ct)))))); /*CHECK*/
                } else if (pass && query.contains (M_CONVERSION)) {
                    // type conversion function
                    pass = Character.isLetter (opname.charAt (0)) && 
                        OpTable.get (opname) < 0;
                }
            }
            return pass;
            
        }
        
    }
}




