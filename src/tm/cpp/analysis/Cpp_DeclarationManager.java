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
import java.util.Hashtable;
import java.util.Stack;
import java.util.Vector;

import tm.clc.analysis.Declaration;
import tm.clc.analysis.DeclarationManager;
import tm.clc.analysis.DeclarationSet;
import tm.clc.analysis.Definition;
import tm.clc.analysis.ExpressionPtr;
import tm.clc.analysis.FunctionDeclaration;
import tm.clc.analysis.LFlags;
import tm.clc.analysis.ScopeHolder;
import tm.clc.analysis.ScopedName;
import tm.clc.analysis.SpecifierSet;
import tm.clc.ast.ExpArgument;
import tm.clc.ast.ExpId;
import tm.clc.ast.ExpressionNode;
import tm.clc.ast.FunctionDefnCompiled;
import tm.clc.ast.NodeList;
import tm.clc.ast.OpAbsFuncCall;
import tm.clc.ast.OpArraySubscript;
import tm.clc.ast.OpDeref;
import tm.clc.ast.OpMemberCall;
import tm.clc.ast.StatDecl;
import tm.clc.ast.StatDo;
import tm.clc.ast.StatReturn;
import tm.clc.ast.StatementNodeLink;
import tm.clc.ast.TyAbstractClass;
import tm.clc.ast.TyAbstractFun;
import tm.clc.ast.TypeNode;
import tm.clc.ast.VarNode;
import tm.clc.datum.AbstractDatum;
import tm.clc.rtSymTab.RT_Symbol_Table;
import tm.cpp.ast.TyArray;
import tm.cpp.ast.TyClass;
import tm.cpp.ast.TyFun;
import tm.cpp.ast.TyNone;
import tm.cpp.ast.TyPointer;
import tm.cpp.ast.TyRef;
import tm.cpp.parser.ParserConstants;
import tm.interfaces.SourceCoords;
import tm.utilities.Assert;
import tm.utilities.Debug;
import tm.virtualMachine.VMState;

/**
 * Functions as the external interface to declaration building logic.
 * Used by <code>ParserContext</code> - many of its methods consist of
 * single calls to <code>Cpp_DeclarationManager</code>.
 * <p>This class is responsible for adding declarations to the compile-time
 * symbol table, the runtime symbol table, and keeping track of the current
 * scope and <em>var depth</em>.
 * <p>It is also responsible for building part or all of the AST representation
 * of particular declarations, including:
 * <ul><li>declaration and initialization of variables</li>
 * <li>automatic code generation for classes</li>
 * <li>declaration and initialization of function parameters</li>
 * </ul>
 * @author Derek Reilly
 * @version 1.0
 */
public class Cpp_DeclarationManager extends DeclarationManager
    implements ParserConstants, Cpp_Specifiers, Cpp_LFlags {

    private static final String NO_MATCHING_TYPE =
        "No type found matching id {0}.";
    private static final String INIT_ID_BAD_DECL =
        "can't make initialization id expression without variable or class name";

    private static final String ZERO_INIT = "zero initialization";
    private static final String STATIC_INIT = "static initialization";
    private static final String DYNAMIC_INIT = "dynamic initialization";

    // the number of local variables visible at a given point during parsing
    private VarCounter varDepth = new VarCounter ();

    // the last class, namespace, or fn declaration - used when class, ns, or function's outer scope
    // is entered
    private Declaration lastDefiningDecl;
    private boolean ldd_scope_entered;
    private Declaration lastSimpleDecl;
    private Vector parameterDetails; // fn parameter names and line numbers
    private SourceCoords currentCoords;

    private Cpp_ExpressionManager expression_manager;
    private Cpp_StatementManager statementManager ;
    private Cpp_CTSymbolTable ct_symtab;
    private RT_Symbol_Table rt_symtab;
    private VMState vms;
    private UserDefinedConversions ud_conversions;
    /** Initialization rules */
    protected Eb_Initialization eb_initialization;
    /** Classifies a <code>Declaration</code> by type, identifies the type */
    public TypeExtractor type_extractor;
    
    // Stack of stacks for local declarations requiring destructors
    private Stack destructorRequired = new Stack() ;

    /**
     * Creates a new <code>Cpp_DeclarationManager</code> instance.
     * @param em the expression manager
     * @param sm the statement manager
     * @param vms the virtual machine state
     */
    public Cpp_DeclarationManager (Cpp_ExpressionManager em,
    		                       Cpp_StatementManager sm,
                                    VMState vms) {
        this.expression_manager = em;
        this.statementManager = sm ;
        this.rb = em.rulebase ();
        this.ct_symtab = em.symbolTable;
        this.vms = vms;
        this.rt_symtab = (RT_Symbol_Table) vms.getSymbolTable ();
        this.ud_conversions = new UserDefinedConversions (ct_symtab);
        this.type_extractor = new TypeExtractor (ct_symtab);
        this.eb_initialization = em.eb_Initialization;

    }


    /**
     * Adds a class declaration (defined) to the compile-time symbol
     * table, and creates an AST representation for the new type
     * @param head the class header, including all qualifiers/modifiers
     */
    public void add_class_declaration( ClassHead head ) {
        dbg.msg (Debug.COMPILE, "Cpp_DeclarationManager.add_class_declaration");
        ScopedName qualClassName = head.get_name ();

        // ** scope representation **
        Declaration classDecl = ct_symtab.addDefiningDeclaration ( head);
        ClassSH classScope = (ClassSH) classDecl.getDefinition ();

        // ** AST representation **
        // may need to create a new TyClass
        TyClass classType = (TyClass) classDecl.getType ();
        if (classType == null) {
            classType =
                new TyClass (qualClassName.getTerminalId (), qualClassName, classDecl);
        }

        // superclasses (building type definition)
        for (Enumeration scs = classScope.getSuperclasses ().elements ();
             scs.hasMoreElements (); ) {
            ClassSH sc = (ClassSH) scs.nextElement ();
            TyClass scType = (TyClass) sc.getOwnDeclaration().getType ();
            classType.addSuperClass (scType);
        }

        // let the symbol table know that we've got a class declaration for
        // the type
        ct_symtab.classTypeDefined (classType, classDecl);

        // keep track of the "last defining declaration"
        lastDefiningDecl = classDecl;
        ldd_scope_entered = false;

        dbg.msg (Debug.COMPILE, "class declaration fqn is " + classDecl.getName ());

    }

    /**
     * Indicates that an elaborated type specifier has been encountered
     * for a class (or union or struct).
     * <p>This can occur in the context of an instance declaration, as in
     * <code>struct X x;</code> or a type declaration, such as
     * <code>class Y;</code>
     * @param name the name (including qualifiers) of the class
     */
    public void declare_class( ScopedName name ) {
        // has the class been declared previously ?
        DeclarationSet candidates =
            ct_symtab.lookup (name, CLASS_LF);
        Declaration match = candidates.getSingleMember ();

        // access modifiers -- is the related entity accessible?

        // if declared previously , do nothing

        if (match == null) { // if not declared previously

            // create a new Declaration (no Definition at this point)
            Declaration newDecl =
                new Declaration (CLASS_LF, name );
            // add it to the symbol table
            ct_symtab.addDeclaration (newDecl);

            // generate an AST representation of the new (undefined) type
            TyClass type = new TyClass (name.getTerminalId (), name, newDecl);

            // add the type to the declaration
            newDecl.setType (type);
        }

    }

    /**
     * Indicates that a new function definition has been encountered.
     * <br>This causes a defining declaration to be added to the
     * symbol table
     * @param funDfn details about the function definition, including id,
     * return type and parameter types.
     * @param lno the line number on which the function definition begins,
     * used as the line number for parameter assignment.
     */
    public StatementNodeLink new_function_defn(
            SourceCoords coords,
            ScopedName unqualifiedName, 
            TyAbstractFun funType ) {
        return new_function_defn (coords, unqualifiedName, funType, true);
    }

    public StatementNodeLink new_function_defn( SourceCoords coords,
                                                ScopedName unqualifiedName,
                                                TyAbstractFun funType,
                                                boolean user_defined ) {
        currentCoords = coords;
        dbg.msg (Debug.COMPILE, "Cpp_DeclarationManager.new_function_defn");

        // 1 Declare the function
        LFlags fnCat = type_extractor.getFunctionCategories (unqualifiedName);
        if (!user_defined) fnCat.unset (Cpp_LFlags.USER_DEFINED);
        
        Declaration funDecl =
            ct_symtab.addDefiningDeclaration (unqualifiedName, fnCat,
                                              funType );
        
        // 2 Create a function defintion object and tell the StatementManager
        ScopedName fullName = funDecl.getName () ;
        Object key = OverloadResolver.munge (fullName, funType ) ;
        FunctionDefnCompiled funDfn
            = new FunctionDefnCompiled( key  ) ;
        
        StatementNodeLink fnLink = funDfn.getBodyLink() ;
        fnLink = statementManager.startFunctionBody( fnLink, coords ) ;
                
        funDecl.setRuntimeId ( key);

        // if this is the main function, store the key in the vms
        // main is any function named "main", defined in global scope
        // ** no checks for validity of main method signature
        // ** main must be defined (not just declared) in global scope
        boolean isMain = unqualifiedName.getName().equals ("main")
                      && ct_symtab.getCurrentScope () == ct_symtab.getGlobalScope () ;

        this.lastDefiningDecl = funDecl;

        // enter the function's outermost block scope so that parameter
        // declarations/initialization can occur
        this.ldd_scope_entered = false; // flag to handle extra start_scope
        // when brace is encountered
        ct_symtab.enterScope (lastDefiningDecl);
        start_scope (); // Scope for the function.

        // ** AST representation **
        // the function definition itself contains the AST representation
        // must add the declaration and initialization statements to the
        // function
        int pcount = funType.getParamCount ();
        Assert.check (pcount == parameterDetails.size ());
        fnLink = start_local_scope( fnLink, coords ) ; // Local scope for parameters
        for( int i = 0; i < pcount ; i++ ) {

            TypeNode pType = funType.getParamType(i);
            ParameterDetail pDetail =
                (ParameterDetail) parameterDetails.elementAt (i);
            ScopedName pName = (ScopedName) pDetail.decl.getName ();
            dbg.msg (Debug.COMPILE, "making VarNode with name " + pName.getName ());
            VarNode v = new VarNode(pName, pType) ;
            // parameter declaration
            StatDecl declStat = new StatDecl (pDetail.coords, varDepth.count(), v);
            fnLink.set (declStat);
            // move statement node link forward
            fnLink = declStat.next ();
            varDepth.addLocal();
            
            // Is a destructor required?
            if( requiresDestructor( pDetail.decl ) ) {
            	fnLink = statementManager.startDestructorScope(fnLink, pDetail.coords, varDepth.count() ) ;
            	// Push on stack
            	((Stack)destructorRequired.peek()).push( pDetail.decl ) ;
            }

            // initialization of parameters with argument values
            // ** default values not supported..
            String uq_name = pDetail.decl.getUnqualifiedName ();
            // peculiar situation for built-in copy constructor and
            // copy assignment operator. Don't try to initialize the
            // classobject parameter - this would call the very function you
            // are trying to define..
            if (! uq_name.equals (ScopedName.COPY_REF_ID)) {
                dbg.msg (Debug.COMPILE, "making initialization expression for " +
                         uq_name + " : " + pType.getTypeString ());
                // Next two lines replaced 05 AUg, 2002, TSN
                //ExpressionNode param =
                //    expression_manager.make_id_exp (new ScopedName (uq_name));
                ExpressionNode param = make_id_exp( pDetail.decl ) ;

                TypeNode argType = pType;
                // If the parameter is a class, then the argument is actually
                // a reference.
                if( pType instanceof TyClass ) {
                    argType = new TyRef( pType ) ; }

                Initializer arg = new Initializer ( Initializer.COPY,  new ExpArgument (argType, i));
                ExpressionNode assignExp = make_initialization_expression
                    (param, eb_initialization.COPY_INIT_SN, arg);

                StatDo assignStat = new StatDo (coords, varDepth.count(), assignExp);
                // Parameter initialization is uninteresting by default
                assignStat.setUninteresting (true);

                fnLink.set (assignStat);

                // move statement node link forward
                fnLink = assignStat.next ();
            }
        }

        // store the fn defn in the rt symbol table
        Debug.getInstance().msg(Debug.COMPILE,  "Adding: " + funDfn.getKey() ) ;
        rt_symtab.addFunctionDefinition (funDfn);
        if( isMain ) rt_symtab.addMainFunction( funDfn, null ) ;


        return fnLink;
    }

	/**
     * Called at the end of a class definition so that default implementations
     * of <em>special member functions</em> can be built and added to the
     * class.
     * <br>Note that this method must be called prior to leaving the class'
     * scope (via <code>end_class_scope</code>)
     * <ul>The methods in question are:
     * <li>default constructor (no arguments)
     * <li>copy constructor (taking a ref to class instance)
     * <li>copy assignment operator (taking a ref to class instance)
     * <li>destructor
     * </ul>
     * @param p a link to the AST representation of the class so far. Any
     *          autogenerated code will be appended via this link.
     */
    public void record_member_declarations( StatementNodeLink p, SourceCoords coords) {
    	currentCoords = coords ;
        // for each member function that could be autogenerated:
        // - try to locate an implementation (in class or perhaps in hierarchy
        //   according to spec rules
        // - if not found, autogenerate the code.
        // simple :D

        // we are in class scope
        ScopeHolder currentScope = ct_symtab.getCurrentScope ();
        Assert.check (currentScope instanceof ClassSH);
        ClassSH classScope = (ClassSH) currentScope;
        Declaration cd = classScope.getOwnDeclaration ();

        LFlags query;

        // default constructor ?
        query = new LFlags (M_MEMBER_FN + M_DEFAULT + M_CONSTRUCTOR);
        if (classScope.lookup (query).isEmpty ())
            build_default_constructor (cd, coords);

        // copy constructor ?
        query = new LFlags (M_MEMBER_FN + M_COPY + M_CONSTRUCTOR);
        if (classScope.lookup (query).isEmpty ())
            build_implicit_copy_method (cd, false);

        // copy assignment operator ?
        query = new LFlags (M_MEMBER_FN + M_COPY + M_OPERATOR);
        if (classScope.lookup (query).isEmpty ())
            build_implicit_copy_method (cd, true);

        // destructor ?
        query = new LFlags (M_MEMBER_FN + M_DESTRUCTOR);
        if (classScope.lookup (query).isEmpty ())
            build_destructor (cd);

        // the class is now defined
        Assert.check (cd.getType () instanceof TyClass);
        TyClass ct = (TyClass) cd.getType ();
        ct.setDefined ();

        // now that the class is defined, the class can
        // be categorized
        cd.getCategory().set (type_extractor.categorizeClass (cd));

    }

    /**
     * Catch-all declaration handler, building both the compile-time and
     * runtime representations of the declaration.
     * <ol>This method is called for:
     * <li>variable declarations (of all types)
     * <li>function declarations (vs. function definitions)
     * <li>others ? not yet implemented : <em>typedef, enum, namespace</em>
     * </ol>
     * @param p the current position in the AST representation of the program
     * @param line_number the line number in the preprocessed source in which
     * the declaration was encountered
     * @param name the declaration id
     * @param spec_set any specifiers/modifiers that accompanied the
     * declaration
     * @param type the type of the entity being declared. For function
     * declarations, this is a combination of return and parameter types.
     * @return the position in the program after statements are added for
     * the declaration (possibly no statements, in which case <code>p</code>
     * is returned).
     */
    public StatementNodeLink simple_declaration( StatementNodeLink p,
                                                   SourceCoords coords,
                                                   ScopedName name,
                                                   SpecifierSet spec_set,
                                                   TypeNode type ) {
        dbg.msg (Debug.COMPILE, "Cpp_DeclarationManager.simple_declaration");
        dbg.msg (Debug.COMPILE, "ScopedName full is " + name.getName ());
        dbg.msg (Debug.COMPILE, "ScopedName terminal is " + name.getUnqualifiedName ());

        // is this an initialization statement for a static data member ?
        Declaration d = null;
        if (!(type instanceof TyAbstractFun) &&
            ct_symtab.getCurrentScope () instanceof NamespaceSH &&
            name.is_qualified ()) {
            d = ct_symtab.lookup (name, Cpp_LFlags.VARIABLE_LF)
                .getSingleMember ();
        }
        if (d != null && d.getCategory().contains (Cpp_LFlags.CLASS_MEMBER) &&
            has_static_storage_duration (d)) { // it is
            // we just need to perform the initialization here, which will
            // occur in end_initializer. We put the declaration in
            // "lastSimpleDecl" for that operation, and that's it.
            lastSimpleDecl = d;

        } else { // otherwise we will add the declaration
            d = this.add_declaration (coords, name, spec_set, type);

            // >>>> AST representation <<<<

            if (d.getCategory().intersects( Cpp_LFlags.VARIABLE) ) {
                // create a var node
                dbg.msg (Debug.COMPILE, "creating a var node using " + d.getUnqualifiedName ());
                dbg.msg (Debug.COMPILE, "fully qualified as " + d.getName ().getName ());
                VarNode v = new VarNode (d.getName (), type);

                // add to type definition if this is a non-static class data
                // member
                if (d.getCategory().contains (CLASS_MEMBER) &&
                    !has_static_storage_duration (d)) {
                    ScopeHolder currentScope = ct_symtab.getCurrentScope ();
                    Declaration classDecl =	currentScope.getOwnDeclaration ();
                    TyClass tc = (TyClass) classDecl.getType ();
                    tc.addField (v);
                }

                // create the declaration statement
                StatDecl sd = new StatDecl (coords, varDepth.count (), v);

                // put statement in proper place, according to storage duration. and etc.
                if (has_static_storage_duration (d)) {
                    dbg.msg (Debug.COMPILE, "adding a global var");
                    // create a datum and put it in the runtime symbol table
                    ScopedName fqn = d.getName ();
                    AbstractDatum datum =
                        type.makeDatum (vms, vms.getStore().getStatic (),
                                        fqn.getTerminalId ());
                    rt_symtab.addStaticGlobal (fqn, fqn.getName (), datum);

                    // always zero-initialize
                    StatDo zero_init =  initialize_last_declaration
                        (eb_initialization.ZERO_INIT_SN, null);

                    // add an initialization statement to the zero initialization chain
                    rt_symtab.addInitializationStatement (ZERO_INIT, zero_init,
                                                            zero_init.next ());
                    // !! watch extern
                    varDepth.addStatic ();
                } else if (d.getCategory().contains (CLASS_MEMBER)) {
                    // class member
                    dbg.msg (Debug.COMPILE, "adding a class member");
                } else { // automatic storage duration
                    dbg.msg (Debug.COMPILE, "adding a local var");

                    // add statement to current block
                    Assert.check (p != null);
                    p.set (sd);
                    p = sd.next ();

                    // leave uninitialized (explicit initializer captured later)
                    varDepth.addLocal ();
                    
                    // Is a destructor required?
                    if( requiresDestructor( d ) ) {
                    	p = statementManager.startDestructorScope(p, coords, varDepth.count()) ;
                    	// Push on stack
                    	((Stack)destructorRequired.peek()).push( d ) ;
                    }
                }
            }
        }

        return p ;
    }


    /**
     * Records the occurrance of a parameter declaration. This can occur
     * in a function pointer definition, a function declaration, or
     * a function definition.
     * @param line_num the line number in the preprocessed source in which
     * the declaration was encountered
     * @param name the declaration id as encountered in source
     * @param spec_set any specifiers/modifiers that accompanied the
     * declaration
     * @param type the type of the entity being declared.
     */ 
    public void parameter_declaration (SourceCoords coords,
                                       ScopedName name,
                                       SpecifierSet spec_set,
                                       TypeNode type ) {
        dbg.msg (Debug.COMPILE, "Cpp_DeclarationManager.parameter_declaration");
        Declaration d =
            this.add_declaration (coords, name, spec_set, type);

        // store parameter details
        parameterDetails.addElement (new ParameterDetail (d, coords));

        //varDepth.addLocal (); // Deleted TSN 12 Aug 2002

    }


    /**
     * Adds a declaration in the symbol table for the encountered
     * decl (parameter or simple declaration). Performs other related
     * bookkeeping also.
     * @param coords the line number in the preprocessed source in which
     * the declaration was encountered
     * @param name the declaration id
     * @param spec_set any specifiers/modifiers that accompanied the
     * declaration
     * @param type the type of the entity being declared. For function
     * declarations, this is a combination of return and parameter types.
     * @return the newly created <code>Declaration</code>
     */
    protected Declaration add_declaration (SourceCoords coords,
                                           ScopedName name,
                                           SpecifierSet spec_set,
                                           TypeNode type ) {
        currentCoords = coords;

        Declaration d = null;
        // function declaration ?
        if (type instanceof TyFun) {
            dbg.msg (Debug.COMPILE, "adding function declaration for " + name.getName ());
            LFlags fnCat = type_extractor.getFunctionCategories (name);
            d = ct_symtab.newFunctionDeclaration ( fnCat, name, null, spec_set,
                                                  (TyFun) type);
            d.getCategory ().set (type_extractor.categorizeType (type));

            // since we're a function declaration (not a definition), remove
            // the var count for the parameters.
            // varDepth.exitScope (); // Deleted TSN 12 Aug 2002

            // set the function's runtime id
            d.setRuntimeId (OverloadResolver.munge (d.getName (), d.getType ()));

            dbg.msg (Debug.COMPILE, "function declaration runtime id is " + d.getRuntimeId ());
            dbg.msg (Debug.COMPILE, "function declaration fqn is " + d.getName().getName ());

        } else {
            // the type name in the SpecifierSet is used to determine
            // Declaration category and Definition
            ScopedName typeName = spec_set.get_type_name ();
            if (!(type instanceof TyAbstractClass)) {
            //			if (typeName == null) { // fundamental type

                if (spec_set.contains (SP_TYPEDEF)) {
                    d = new Declaration ( TYPEDEF_LF, name, null,
                                         spec_set, type);
                } else { // variable declaration
                    d = new Declaration ( VARIABLE_LF, name, null,
                                         spec_set, type);
                    d.getCategory ().set (type_extractor.categorizeType (type));
                }
                d.setDefinition (d);
            } else { // derived type

                // lookup type Declaration
                Assert.check (type instanceof TyAbstractClass);
                Declaration td = ct_symtab.getClassDeclaration
                    ((TyAbstractClass) type);

                // type should be found
                // ?? what about elaborated type specifier enum - the
                //    class case is taken care of in declare_class. Elab
                //    enum currently not implemented in parser.
                if (td == null)
                    Assert.apology (NO_MATCHING_TYPE, typeName.getName ());

                if (spec_set.contains (SP_TYPEDEF)) {
                    d = new Declaration (td.getCategory (), name,
                                         td.getDefinition (), spec_set, type);

                } else { // variable declaration
                    d = new Declaration (VARIABLE_LF, name, td,
                                         spec_set, type);
                }

                d.getCategory ().set (td.getCategory().get (VT_GROUP));
            }
            // ** do not support function pointers
            //    - will the spec_set's id name be empty?
            //    - their type is not any one particular function,
            //      so their Definition is not needed for lookup.

            // add the Declaration to the symbol table

            ct_symtab.addDeclaration (d);
        }

        // missing
        // enum Definitions are scalar (int) - not supported

        // stash representation
        lastSimpleDecl = d;

        return d;
    }



    /**
     * Called to indicate that a constructor's member initializer list
     * has been parsed.
     * @param initializer_list the set of initializations present in the
     * <em>mem-initializer</em> clause
     * @param body_link the current point in the AST representation of the
     * program - member initialization statements will be placed here
     */
    public StatementNodeLink constructor_initializer
        (Vector initializer_list, StatementNodeLink body_link) {

        Assert.check (lastDefiningDecl instanceof FunctionDeclaration);
        FunctionDeclaration fd = (FunctionDeclaration) lastDefiningDecl;
        if (!(fd.getCategory().contains (CONSTRUCTOR))) {
            // this method is called for every function
            return body_link;
        }

        // 0. Count the parameters.

        TyFun funType = (TyFun) fd.getType ();
        int parameter_count =  funType.getParamCount ();

        ClassSH classScope =
            (ClassSH) ((MemberFnSH) fd.getDefinition ()).getOwningScope ();

        // 1. identify the data members / subobjects being initialized
        //    in the mem-initializer
        Hashtable mis = new Hashtable() ;
        for (int i = 0; i < initializer_list.size (); i++) {
            ConstructorInitializer ci =
                (ConstructorInitializer) initializer_list.elementAt (i);
            Declaration mem = ct_symtab.lookup
                (ci.get_name (), CLASSHIER_LF).getSingleMember ();
            mis.put (mem, ci.get_initializer ());
        }


        // 2. initialize virtual base classes (not yet implemented)
        // 3. initialize non-virtual base classes in the order they appear
        //    in the base-specifier-list (not in the mem-initializer). If
        //    the base class was identified in the mem-init, initialize
        //    accordingly. Otherwise, if non-POD, default-initialize (leave
        //    unmentioned POD base classes uninitialized)

        body_link = initialize_members (new LFlags (M_BASE_CLASS),
                                        eb_initialization.DIRECT_INIT_SN,
                                        classScope, mis, body_link,
                                        parameter_count);

        // 4. initialize non-static data members in the order they appear
        //    in the class definition (not in the mem-initializer). If
        //    identified in the mem-initializer, initialize accordingly.
        //    Otherwise, leave uninitialized unless non-POD class type
        //    (default initialize in that case).
        body_link = initialize_members (new LFlags (M_DATA_MEMBER),
                                        eb_initialization.DIRECT_INIT_SN,
                                        classScope, mis, body_link,
                                        parameter_count);

        // - any const-qualified non-static data members left uninitialized
        //   (in base classes also), means the program is ill-formed
        // ** we are currently not checking this

        return body_link ;
    }


    // initialize the members matching the query in the provided
    // class scope
    private StatementNodeLink initialize_members (LFlags query,
                                                    ScopedName initCat,
                                                    ClassSH classScope,
                                                    Hashtable mis,
                                                    StatementNodeLink snl,
                                                    int var_depth) {
        return initialize_members (classScope.lookup (query).elements (),
                                   initCat, classScope, mis, snl, var_depth);
    }

    // initialize the enumerated members in the provided class scope
    private StatementNodeLink initialize_members (Enumeration members,
                                                    ScopedName initCat,
                                                    ClassSH classScope,
                                                    Hashtable mis,
                                                    StatementNodeLink snl,
                                                    int var_depth) {
        while (members.hasMoreElements ()) {
            Declaration member = (Declaration) members.nextElement ();
            Initializer init = (Initializer) mis.get (member);

            if (init != null || member.getCategory ().intersects (NON_POD)) {
                ExpressionNode init_exp = null;
                if (member.getCategory().intersects (VARIABLE)) { // data member
                    // regular initialization
                    init_exp = make_initialization_expression
                        (this.make_id_exp (member), initCat, init);
                } else if (member.getCategory().contains (Cpp_LFlags.CLASS)) { // subobject
                    // static initialization of subobject; use default initialization if
                    // initializer is empty. In either case this is a constructor call.
                    init_exp = makeSubObjectInitialization (member, init, classScope) ;
                }
                StatDo doInit = new StatDo (currentCoords, var_depth, init_exp);
                snl.set (doInit);
                snl = doInit.next ();
            }
        }
        return snl;
    }

    /**
     * Called to indicate that an initializer expression has been entered.
     * @param name the id of the entity being initialized
     */
    public void start_initializer (ScopedName name) {
        dbg.msg (Debug.COMPILE, "Cpp_DeclarationManager.start_initializer");
        // if the last declaration is for a static data member,
        // modify the scope rules to take this into account during lookup
        // of ids encountered in the initializer expression
        Assert.check (lastSimpleDecl != null);
        if (has_static_storage_duration (lastSimpleDecl)) {
        }

        // ** if this initializer is part of a constructor mem-initializer list,
        //    scope rules are as per inside function definition. This happens
        //    automatically, as mem-initializers are built by the parser after
        //    the constructor definition has been processed by the analyzer.

    }

    /**
     * Called to indicate that an initializer expression has been
     * parsed.
     * @param init the initialization expression - used to initialize
     * the most recently declared entity.
     * @param p statement block in which to add the initialization
     * (ignored for static initialization)
     */
    public StatementNodeLink end_initializer (Initializer init,
                                                StatementNodeLink p) {
        dbg.msg (Debug.COMPILE, "Cpp_DeclarationManager.end_initializer");
        // get out of here if it's not a variable declaration
        if (!lastSimpleDecl.getCategory().intersects (Cpp_LFlags.VARIABLE)) return p;

        // 1. get an id expression for the last declaration
        // 2. determine the initialization category
        // 3. apply the initialization rules
        // 4. wrap the result in a StatDo
        // 5. attach this to the statement chain
        // initialization

        Assert.check (lastSimpleDecl != null);

        boolean ssd = has_static_storage_duration (lastSimpleDecl);
        if (ssd) {
            // remove the scope modification flag
        }

        ScopedName initCat ;
        int initKind = init.initializationKind() ;
        if( initKind == Initializer.COPY ) {
            initCat = eb_initialization.COPY_INIT_SN ; }
        else if( initKind == Initializer.DIRECT ) {
            initCat = eb_initialization.DIRECT_INIT_SN ; }
        else { // NONE
            initCat = Eb_Initialization.NO_INIT_SN ; }

        StatDo doInit = null;
        // add to statement chain
        if (ssd) { // static storage duration
            // make an initialization statement
            doInit =
                initialize_last_declaration (initCat, init);

            // static or dynamic initialization ?
            String ssd_init_cat ;
            if( init.initializationKind() == Initializer.NONE ) {
                ssd_init_cat = STATIC_INIT ; }
            else if( init.length() == 1 ) {
                ssd_init_cat = (initializer_is_dynamic (init.getExp ()))
                    ? DYNAMIC_INIT
                    : STATIC_INIT; }
            else { // Multiple constructor arguments.
                ssd_init_cat = DYNAMIC_INIT ; }

            // add to the appropriate initialization chain
            rt_symtab.addInitializationStatement
                (ssd_init_cat, doInit, doInit.next ());

        } else if (p != null) { // automatic storage duration, not a data member
            if (lastSimpleDecl.getCategory().intersects (Cpp_LFlags.NON_POD) ||
                (init != null && init.initializationKind() != Initializer.NONE)) {
                // make an initialization statement
                doInit =
                    initialize_last_declaration (initCat, init);
                p.set (doInit); // p is null for non-static data members.
            }
        }

        // For const integral variables, record the initializer in
        // the declaration.
        // Is it an initialized integral const?
        if( lastSimpleDecl.getCategory().intersects( Cpp_LFlags.INTEGRAL)
         && (lastSimpleDecl.getType().getAttributes() & Cpp_Specifiers.CVQ_CONST) != 0
         && init.initializationKind() != Initializer.NONE
         && init.length() == 1 ) {
            ExpressionNode exp = init.getExp() ;
            // If the expression is a compile time constant record it
            if( exp.is_integral_constant() ) {
                lastSimpleDecl.setIntegralConstantValue
                     ( exp.get_integral_constant_value() ) ; } }

        lastSimpleDecl = null;

        if( ssd ) return p ;
        else if( doInit == null ) return p ;
        else return doInit.next ();
    }

    // determines whether an object with static storage duration has
    // an initializer which is static or dynamic
    private boolean initializer_is_dynamic (ExpressionNode exp) {
        // because this test occurs for initialization of entities with
        // static storage duration only, a non-static initializer is one
        // that contains function calls (visibly or by stealth).
        // - so, root through the expression tree for a function call
        boolean dynamic = exp instanceof OpAbsFuncCall;
        for (int i = 0; !dynamic && i < exp.childCount (); i++)
            dynamic = initializer_is_dynamic
                ((ExpressionNode) exp.child (i));

        return dynamic;
    }

    // determines whether an object has static storage duration
    private boolean has_static_storage_duration (Declaration d) {
        return ((ct_symtab.getCurrentScope () instanceof NamespaceSH) ||
                d.hasSpecifier (SP_STATIC) || d.hasSpecifier (SP_EXTERN));
    }

    /**
     * Generates the AST representation of an initialization expression
     * @param id_exp the left hand side of the initialization
     * @param init_type flags identifying the class of initialization to
     * occur
     * @param init contains the expression to use as an initializer (if any)
     * @return the resulting initialization expression
     */
    protected ExpressionNode make_initialization_expression
        (Object id_exp, ScopedName init_type, Initializer init) {

        // 1. determine what kind of initialization expression (if any)
        boolean hasNoInit = init == null || (init.initializationKind() == Initializer.NONE) ;
        ScopedName init_cat =
            (hasNoInit &&
             !(init_type.equals (eb_initialization.ZERO_INIT_SN)))
            ? eb_initialization.DEFAULT_INIT_SN
            : init_type;

        // determine visible representation of initialization operation
        ScopedName opid = new Cpp_ScopedName (
            (init_type.equals (eb_initialization.COPY_INIT_SN) ||
             init_type.equals (eb_initialization.ZERO_INIT_SN))
            ? OpTable.get (ASSIGN)
            : OpTable.get (OPEN_PAREN));

        ExpressionPtr init_exp;
        if (hasNoInit)
            init_exp = new ExpressionPtr (opid, new Object [] {id_exp});
        else {
            int len = init.length() ;
            Object [] exps = new Object[ 1 + len ] ;
            exps[0] = id_exp ;
            for( int i=0 ; i<len ; ++i ) exps[ i+1 ] = init.getExp(i) ;
            init_exp = new ExpressionPtr (opid, exps); }
        init_exp.opcat = init_cat;

        // 2. generate the AST representation of the initialization
        eb_initialization.apply (init_exp);

        return init_exp.get ();
    }


    /**
     * Applies no checks, performs no lookup, just builds a reference expression suitable for
     * initialization.
     */
    protected ExpressionNode make_id_exp (Declaration d) {
        ExpressionNode id_exp = null;
        if (d.getCategory().intersects (Cpp_LFlags.VARIABLE)) {
            id_exp = new ExpId (new TyRef (d.getType ()), d.getUnqualifiedName (),
                                 d.getName ());
            if( d.hasIntegralConstantValue() ) {
                id_exp.set_integral_constant_value( d.getIntegralConstantValue()); }
        } else {
            Assert.apology (INIT_ID_BAD_DECL);
        }
        return id_exp;
    }

    /* initialize the last recorded declaration with the provided
     *  initializer given the initialization category specified
     */
    private StatDo initialize_last_declaration
        (ScopedName init_cat, Initializer initializer) {
        ExpressionNode id_exp = make_id_exp (lastSimpleDecl);
        dbg.msg (Debug.COMPILE, "using last declaration, made " + ((id_exp == null) ? null :
                                                    ("\n" + id_exp.ppToString (3, 80))));
        ExpressionNode init_exp = make_initialization_expression
            (id_exp, init_cat, initializer);
        StatDo doInit = new StatDo (currentCoords, varDepth.count (),
                                    init_exp);
        return doInit;
    }

    /** Does an auto variable requrie a desctructor? This will be true for
     * non-POD class instances and for arrays of such.
	 * @param decl A declaration for an auto variable
	 * @return whether the variable will require a destructor when it goes out of scope.
	 */
	private boolean requiresDestructor(Declaration decl) {
		return decl.getCategory().intersects (Cpp_LFlags.NON_POD);
	}

    /**
	 * @param decl
	 * @return
	 */
	private ExpressionNode make_destructor_expression(Declaration decl) {
        ExpressionNode id_exp = make_id_exp (decl);
        ScopedName opid = Eb_Initialization.DESTRUCTION_SN ;
        ExpressionPtr destruct_exp_ptr
		    = new ExpressionPtr (opid, new Object [] {id_exp});
        eb_initialization.apply (destruct_exp_ptr);
		return destruct_exp_ptr.get() ;
	}
	
    /** Add the destuctor code for an auto variable
	 * @param p a head link
	 * @param coords
	 * @param decl The declaration of an auto variable
	 * @return a tail link
	 */
	private StatementNodeLink makeDestructorCall(StatementNodeLink p,
			                                     SourceCoords coords,
			                                     Declaration decl) {
///////////////////////
  
        ExpressionNode destructorExpression = make_destructor_expression( decl ) ;
        StatDo doInit = new StatDo (coords, varDepth.count (), destructorExpression );
        p = statementManager.startDestructorCode(p, coords, varDepth.count()) ;
        p.set( doInit ) ; p = doInit.next() ;
        p = statementManager.endDestructorBlock(p, coords, varDepth.count() ) ;
        return p;
	}

// Scopes


	/**
     * Enter a class declaration's scope (class body)
     */
    public void start_class_scope() {
        dbg.msg (Debug.COMPILE, "Cpp_DeclarationManager.start_class_scope");
        ct_symtab.enterScope (lastDefiningDecl);
    }

    /**
     * leave a class declaration's scope
     */
    public void end_class_scope() {
        dbg.msg (Debug.COMPILE, "Cpp_DeclarationManager.end_class_scope");
        //this.end_scope (); // Deleted. TSN 12 Aug 2002
        ct_symtab.exitScope(); // Added. TSN 12 Aug 2002
    }

    /**
     * Enters a local scope. This is usually an unnnamed scope, but may be
     * used to signal beginning of a function's outer scope.
     */
    public StatementNodeLink start_local_scope(StatementNodeLink p, SourceCoords coords) {
        dbg.msg (Debug.COMPILE, "Cpp_DeclarationManager.start_local_scope()");
        p = statementManager.startLocalScope( p, coords, get_current_var_depth() ) ;
        start_scope ();
        destructorRequired.push( new Stack() ) ;
        return p ;
    }

    /** Leaves a local scope */
    public StatementNodeLink end_local_scope(StatementNodeLink p, SourceCoords coords) {
        dbg.msg (Debug.COMPILE, "Cpp_DeclarationManager.end_local_scope()");
        Stack stk = (Stack) destructorRequired.pop() ;
        while( ! stk.empty() ) {
        	p = makeDestructorCall( p, coords, (Declaration) stk.pop() ) ;
        }
        this.end_scope ();
        p = statementManager.endLocalScope( p, coords, get_current_var_depth() ) ;
        return p ;
    }

	/**
     * Enters the scope for a function's parameter declaration clause
     */
    public void start_function_prototype_scope () {
        dbg.msg (Debug.COMPILE, "Cpp_DeclarationManager.start_function_prototype_scope");
        ct_symtab.enterFunctionPrototypeScope ();
        //varDepth.enterScope ();  // Deleted TSN 12 Aug 2002
        parameterDetails = new Vector ();
    }

    /* begin a new scope */
    private void start_scope () {
        if (!ldd_scope_entered) {
            ldd_scope_entered = true;
        } else {
            ct_symtab.enterScope ();
        }
        varDepth.enterScope ();
    }

    /* leave a scope */
    private void end_scope () {
        lastDefiningDecl = null;
        ct_symtab.exitScope ();
        varDepth.exitScope ();
    }


    /**
     * Leaves the scope for a function's parameter declaration clause
     */
    public void end_function_prototype_scope () {
        dbg.msg (Debug.COMPILE, "Cpp_DeclarationManager.end_function_prototype_scope");
        ct_symtab.exitFunctionPrototypeScope ();
    }

    /**
     * Signals the end of a function definition
     */
    public void end_function_defn ( StatementNodeLink p, SourceCoords coords ) {
        dbg.msg (Debug.COMPILE, "Cpp_DeclarationManager.end_function_defn");

        p = end_local_scope (p, coords); // End local scope for parameters
        p = statementManager.startFunctionEpilogue( p, coords ) ;
        
        ScopeHolder sh = ct_symtab.getCurrentScope() ;
        Assert.check( sh instanceof FunctionSH ) ;
        FunctionDeclaration fd = (FunctionDeclaration) ((FunctionSH)sh).getOwnDeclaration() ;
        
        if( fd.getCategory().contains (DESTRUCTOR)) {
            ClassSH classScope =
            	(ClassSH) ((MemberFnSH) fd.getDefinition ()).getOwningScope ();
            Declaration cd = classScope.getClassDeclaration() ;
            currentCoords = coords ;
            p = makeDestructorEpilogue( cd, p ) ;
        }
        end_scope() ; // End scope for function
        statementManager.endFunctionBody(p, coords) ;
    }

    /**
     * Enters a new translation unit's outer scope
     */
    public void start_file_scope( String file_name ) {
        dbg.msg (Debug.COMPILE, "Cpp_DeclarationManager.start_file_scope( String file_name )");
        // starting a file scope creates a new global scope
        // we treat global scope as the final enclosing namespace scope
        ct_symtab.enterFileScope ();

        varDepth.enterScope ();

        // start the initialization chains for entities with static storage
        // duration
        rt_symtab.newInitializationChain (ZERO_INIT);
        rt_symtab.newInitializationChain (STATIC_INIT);
        rt_symtab.newInitializationChain (DYNAMIC_INIT);
    }

    /**
     * Leaves a translation unit's outer scope
     */
    public void end_file_scope() {
        dbg.msg (Debug.COMPILE, "Cpp_DeclarationManager.end_file_scope()");
        // add extern declarations/definitions to extern linkage table
        ct_symtab.exitFileScope ();

        varDepth.exitScope ();

        // end the initialization chains for entities with static storage
        end_initialization_chain (ZERO_INIT);
        end_initialization_chain (STATIC_INIT);
        end_initialization_chain (DYNAMIC_INIT);
    }

    /*
     * Caps off a static initialization chain
     */
    private void end_initialization_chain (String ic) {
        rt_symtab.addInitializationStatement
            (ic, new StatReturn (SourceCoords.UNKNOWN, 0), null);
    }

    /**
     * Enters a new namespace scope
     */
    public void start_namespace_scope() {
        dbg.msg (Debug.COMPILE, "Cpp_DeclarationManager.start_namespace_scope()");
        start_scope ();
    }

    /**
     * Leaves the current namespace scope
     */
    public void end_namespace_scope() {
        dbg.msg (Debug.COMPILE, "Cpp_DeclarationManager.end_namespace_scope()");
        ct_symtab.exitScope ();

        varDepth.exitScope ();
    }

    /**
     * Gets the number of variables visible in the current scope.
     * @return the number of visible variables
     */
    public int get_current_var_depth() {
        dbg.msg (Debug.COMPILE, "Cpp_DeclarationManager.get_current_var_depth()");
        return varDepth.count () ;
    }

    /* the default default constructor default-constructs all NON-POD
     * base classes and non-static data members
     */
    private void build_default_constructor (Declaration cd, SourceCoords coords) {
        // 1. start function prototype scope
        start_function_prototype_scope ();
        // 2. end function prototype scope
        end_function_prototype_scope ();
        // 3. new function definition
        // relative id
        ScopedName fname = new Cpp_ScopedName (cd.getUnqualifiedName ());
        TyFun ftype = new TyFun (new Vector (), false);
        ftype.addToEnd (TyNone.get ());
        StatementNodeLink p = new_function_defn (currentCoords, fname, ftype, false);
        // 4. constructor initializer
        p = constructor_initializer (new Vector (), p);
        // 5. function body
        p = start_local_scope (p, currentCoords);
        p = end_local_scope (p, currentCoords);

        // 6. end function definition
        end_function_defn (p, currentCoords);
    }

    /** Build generated copy construct or assignment operator
     *  @param cd class declaration
     *  @param op is false for copy constructor, and true for assignment operator
     *  see ISO 12.8 for rules */
    private void build_implicit_copy_method	(Declaration cd, boolean op) {
        // 1. start function prototype scope
        start_function_prototype_scope ();
        // 2. should this be a const copy ?
        // - do all member objects and base classes have const (or const
        //   volatile) copy constructors/operators ?
        LFlags bquery = new LFlags (M_BASE_CLASS + M_RECURSE + M_MEMBER_FN
                                    + M_COPY +
                                    ((op) ? M_OPERATOR : M_CONSTRUCTOR));
        LFlags mquery = new LFlags (M_RECURSE + M_MEMBER_FN + M_COPY +
                                    ((op) ? M_OPERATOR : M_CONSTRUCTOR));
        ClassSH cscope = (ClassSH) cd.getDefinition ();
        dbg.msg (Debug.COMPILE, "looking for member objects and base classes");
        DeclarationSet results =
            cscope.lookup (new LFlags [] {bquery, mquery});
        boolean const_copy = true;
        /*************************************************
         * The following code decides whether or not the
         * parameter should be a const reference or not.
         * As a work-around until this code is fixed,
         * it is being commented out.
         */
        //  ClassSH currentClass = null;
        //  for (Enumeration el = results.elements (); el.hasMoreElements (); ) {
        //      Object e = el.nextElement ();
        //      Assert.check (e instanceof FunctionDeclaration);
        //      FunctionDeclaration fd = (FunctionDeclaration) e;
        //      // handle multiple copy constructors or operators per class
        //      MemberFnSH fsh = (MemberFnSH) fd.getDefinition ();
        //      ClassSH newClass = (ClassSH) fsh.getOwningScope ();
        //      if (newClass != currentClass && !const_copy)
        //          break;
        //      else if (newClass == currentClass && const_copy)
        //          continue;
        //      currentClass = newClass;
        //      // is the first parameter const ?
        //      const_copy = (fd.getParameter(0).getAttributes () + CVQ_CONST > 0);
        //  }

        // 3. add parameter
        dbg.msg (Debug.COMPILE, "adding parameter");
        TypeNode ref = new TyRef (cd.getType ());
        SpecifierSet spec_set = new Cpp_SpecifierSet ();
        if (const_copy) spec_set.add (SP_CONST);
        ScopedName param_name = new Cpp_ScopedName (ScopedName.COPY_REF);
        parameter_declaration (currentCoords, param_name, spec_set, ref);

        // 4. end function prototype scope
        end_function_prototype_scope ();

        // 5. new function definition
        // inline id
        ScopedName fname =
            (op) ? new Cpp_ScopedName ("operator=")
                 : new Cpp_ScopedName (cd.getUnqualifiedName ());
        Vector param_types = new Vector() ;
        param_types.addElement( ref ) ;
        TyFun ftype = new TyFun (param_types, false);
        ftype.addToEnd (((op) ? ref : TyNone.get ()));
        StatementNodeLink p = new_function_defn ( currentCoords, fname, ftype, false);
        
        // 6. enter function local scope
        p = start_local_scope (p, currentCoords);

        // 7. memberwise copy
        // call copy constructor/operator for each immediate base class, in the
        // order in which they appeared in the class declaration (virtual
        // not supported)
        DeclarationSet members = cscope.lookup (new LFlags (M_BASE_CLASS));
        // build rvalues / params
        ExpressionNode toCopy = new ExpArgument (ref, 0);
        ExpressionNode eThis = expression_manager.make_this_exp () ;
        ExpressionNode eThisDeref = new OpDeref( ref, OpTable.get (STAR), eThis );
        for (Enumeration ms = members.elements ();
             ms.hasMoreElements (); ) {
            Declaration d = (Declaration) ms.nextElement ();
            Initializer init = new Initializer(Initializer.COPY, eThisDeref ) ;
            ExpressionNode init_exp = makeSubObjectInitialization(d, init, cscope) ;
            int var_depth = 1 ;
            StatDo doInit = new StatDo (currentCoords, var_depth, init_exp);
            p.set( doInit ) ; p = doInit.next() ;
        }

        // work with non-static data members, in the order they
        // appeared in the class declaration.
        DeclarationSet data_members =
            cscope.lookup (new LFlags (M_DATA_MEMBER));

        // will need a reference to ourselves

        for (Enumeration dms = data_members.elements ();
             dms.hasMoreElements (); ) {
            Declaration d = (Declaration) dms.nextElement ();
            // build a member access expression
            ExpressionNode copy_mex = expression_manager.make_member_exp
                (toCopy, new Cpp_ScopedName (d.getUnqualifiedName ()));
            ExpressionNode this_mex = expression_manager.make_arrow_exp
                (eThis, new Cpp_ScopedName (d.getUnqualifiedName ()));
            TypeNode dType = d.getType ();
            if (dType instanceof TyRef)
                dType = ((TyRef) dType).getPointeeType ();

            if (dType instanceof TyArray) {
                p = array_smf ( (op) ? 1 : 2, this_mex, copy_mex, p);
            } else if (dType instanceof TyClass) {
                p = classobj_smf ( (op) ? 1 : 2, this_mex, copy_mex, p);
            } else { // scalar type
                p = scalar_copy (this_mex, copy_mex, p);
            }
        }

        // if a copy assignment operator, we need to return a reference to ourselves
        if (op) {
            StatDo setReturn =
                new StatDo (currentCoords, varDepth.count (), eThisDeref );
            p.set (setReturn);
            p = setReturn.next ();
        }

        // 8. end function local scope
        p = end_local_scope (p, currentCoords);

        // 9. end function definition
        end_function_defn (p, currentCoords);

    }


    /** Build an expression representing the parent of this object
     *  @param member The declaration of the parent class
     *  @param thisObj An lvalu expression representing a referece to this object
     */
    ExpressionNode makeSubObjectInitialization( Declaration member,
                                                Initializer init,
                                                ClassSH classScope ) {
        TypeNode class_t = classScope.getOwnDeclaration().getType ();
        return eb_initialization.make_init_subobject_exp (member.getType (), class_t, init);
    }

    /** Generates one statement in a generated routine.
     *  @param op is 0-->destuctor, 1-->assignment op, 2--> copy constructor
     *  @param lvalue is the thing to initialize, assign, or desctruct
     *  @param arg the expression to assign or copy.
     *  */
    private StatementNodeLink classobj_smf ( int op,
                                             ExpressionNode lval,
                                             ExpressionNode arg,
                                             StatementNodeLink p) {
        //Definition defn0 = d.getDefinition () ; // The field's definition
        //Assert.check( defn0 instanceof Declaration ) ;
        //Declaration d1 = (Declaration) defn0 ;
        //Definition defn1 = d1.getDefinition (); // The class's definition
        //Assert.check( defn1 instanceof ClassSH );
        //ClassSH bc = (ClassSH) defn1;

        // Obtain the ClassSH for the object
        ClassSH bc ; {
            Assert.check( lval.get_type () instanceof TyRef ) ;
            TyRef tyRef = (TyRef) lval.get_type () ;
            Assert.check( tyRef.getPointeeType () instanceof TyClass ) ;
            TyClass tyClass = (TyClass) tyRef.getPointeeType () ;
            Declaration decl = tyClass.getDeclaration() ;
            Definition defn = decl.getDefinition() ;
            Assert.check( defn instanceof ClassSH ) ;
            bc = (ClassSH) defn ; }

        // get the copy constructor / operator for this class
        LFlags query = new LFlags (M_MEMBER_FN +
                                   ((op == 0) ? M_DESTRUCTOR
                                    : M_COPY + ((op == 1) ? M_OPERATOR
                                                : M_CONSTRUCTOR)));
        Declaration cf = bc.lookup (query).getSingleMember ();
        Assert.check (cf != null);

        // build the function call expression

        // need to get a relative path
        ScopedName sn_path = new Cpp_ScopedName ();
        bc.buildRelativePath (cf, sn_path);

        // no conversions are required
        String fName = cf.getName().getTerminalId();
        TypeNode returnType = ((TyAbstractFun) cf.getType ()).returnType ();
        String accessOp =
            (lval.get_type () instanceof TyPointer)
            ? OpTable.get (ARROW)
            : OpTable.get (DOT);
        NodeList argList = op==0 ? new NodeList() : new NodeList( arg ) ;

        // I'm assuming nonvirtual calls here.
        ExpressionNode fnExp =
            new OpMemberCall (returnType, fName, accessOp, op == 1, op != 1,
                                cf.getRuntimeId (), false, lval,
                                cf.getRelativePath (sn_path),
								argList );

        StatDo doCopy = new StatDo (currentCoords, 0, fnExp);
        p.set (doCopy);
        p = doCopy.next ();

        return p;
    }

    /* copy initialization statements for scalar vars */
    private StatementNodeLink scalar_copy (ExpressionNode lval,
                                             ExpressionNode arg,
                                             StatementNodeLink p) {
        ExpressionNode assignExp =
            expression_manager.make_bin_op (ASSIGN, lval, arg);
        StatDo doCopy = new StatDo (currentCoords, 0, assignExp);
        p.set (doCopy);
        p = doCopy.next ();

        return p;
    }

    /* creates initialization or deletion statements for array data members in
     * special member functions
     */
    private StatementNodeLink array_smf (  int op,
                                           ExpressionNode lval,
                                           ExpressionNode arg,
                                           StatementNodeLink p) {
        Assert.check( lval.get_type () instanceof TyRef ) ;
        TyRef tyRef = (TyRef) lval.get_type () ;
        Assert.check( tyRef.getPointeeType () instanceof TyArray ) ;
        TyArray ta = (TyArray) tyRef.getPointeeType () ;
        TypeNode elType = ta.getElementType ();
        TyRef tyRefToElem = new TyRef( elType ) ;
        int elcount = ta.getNumberOfElements ();
        // TBD. We should generate a loop!!
        for (int i = (op > 0) ? 0 : elcount - 1;
             (op > 0 && i < elcount) || (op == 0 && i >= 0);
             i += (op == 0) ? -1 : 1) {
            // !! get rid of direct use of square brackets
            ExpressionNode lSub = new OpArraySubscript
                (tyRefToElem, "[", "]", lval,
                 Literals.make_int_const (Integer.toString (i), 10));
            ExpressionNode rSub = (op == 0) ? null
                : new OpArraySubscript
                    (tyRefToElem, "[", "]", arg,
                     Literals.make_int_const (Integer.toString (i), 10));
            if (elType instanceof TyArray) {
                p = array_smf ( op, lSub, rSub, p);
            } else if (elType instanceof TyClass) {
                p = classobj_smf ( op, lSub, rSub, p);
            } else {
                if (op > 0) p = scalar_copy (lSub, rSub, p);
            }

        }
        return p;
    }


    /* builds an implicitly defined destructor */
    private void build_destructor (Declaration cd) {
        // 1. start function prototype scope
        start_function_prototype_scope ();

        // 2. end function prototype scope
        end_function_prototype_scope ();

        // 5. new function definition
        ScopedName fname =
            new Cpp_ScopedName (OpTable.get (TILDE) + cd.getUnqualifiedName ());
        TyFun ftype = new TyFun (new Vector (), false);
        ftype.addToEnd (TyNone.get ());
        
        StatementNodeLink p = new_function_defn (currentCoords, fname, ftype, false);

        // 7. function body
        p = start_local_scope (p, currentCoords);

        // 9. end function body
        p = end_local_scope (p, currentCoords);

        // 10. end function definition
        end_function_defn (p, currentCoords);

    }

    /** Make the code that goes at the end of a destructor to ensure that members and base classes are destructed.
	 * @param cd The class Declaration
	 * @param p A head link
	 * @return A tail link
	 */
	private StatementNodeLink makeDestructorEpilogue(Declaration cd, StatementNodeLink p) {
		// will need a reference to ourselves
        ExpressionNode eThis = expression_manager.make_this_exp ();

        // work with non-static data members, in the reverse order to their
        // placement in the class declaration.
        ClassSH bc = (ClassSH) cd.getDefinition ();
        DeclarationSet data_members =
            bc.lookup (new LFlags (M_DATA_MEMBER));

        for (int i = data_members.size () - 1; i >= 0; i--) {
            Declaration d = data_members.declaration (i);
            // build a member access expression
            ExpressionNode this_mex = expression_manager.make_arrow_exp
                (eThis, new Cpp_ScopedName (d.getUnqualifiedName ()));
            TypeNode dType = d.getType ();
            if (dType instanceof TyRef)
                dType = ((TyRef) dType).getPointeeType ();

            if (dType instanceof TyArray) {
                p = array_smf ( 0, this_mex, null, p);
            } else if (dType instanceof TyClass) {
                p = classobj_smf ( 0, this_mex, null, p);
            } // else scalar type, do nothing
        }

        // call destructor for each immediate base class, in the reverse
        // order to their placement in the class declaration (virtual
        // not supported)
        TypeNode ref = new TyRef (cd.getType ());
        ExpressionNode eThisDeref = new OpDeref( ref
                                               , OpTable.get (STAR)
                                               , eThis ) ;
        DeclarationSet immed_base = bc.lookup (new LFlags (M_BASE_CLASS));
        for (int i = immed_base.size () - 1; i >= 0; i--) {
            Declaration d = immed_base.declaration (i);
            TypeNode dType = d.getType ();
            int[] path = new int[] { i } ; // Hope this can be trusted!!!
            TyRef tyRef = new TyRef() ;
            tyRef.addToEnd( dType ) ;
            ExpressionNode subObject = expression_manager.make_super_exp( tyRef, d.getName().getName()+"::", path) ;
            p = classobj_smf( 0, subObject, null, p ) ;
        }
		return p;
	}

	public class ParameterDetail {
        public Declaration decl;
        public SourceCoords coords;

        public ParameterDetail (Declaration d, SourceCoords cds) {
            decl = d;
            coords = cds;
        }
    }

}

