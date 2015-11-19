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

import java.util.Vector;

import tm.clc.analysis.Declaration;
import tm.clc.analysis.IdTable;
import tm.clc.analysis.ScopedName;
import tm.clc.analysis.SpecifierSet;
import tm.clc.ast.ExpressionNode;
import tm.clc.ast.FunctionDefnCompiled;
import tm.clc.ast.NoExpNode;
import tm.clc.ast.NodeList;
import tm.clc.ast.StatJoin;
import tm.clc.ast.StatementNode;
import tm.clc.ast.StatementNodeLink;
import tm.clc.ast.TyAbstractFun;
import tm.clc.ast.TypeNode;
import tm.clc.rtSymTab.RT_Symbol_Table;
import tm.cpp.parser.Parser;
import tm.cpp.parser.ParserConstants;
import tm.cpp.parser.Token;
import tm.interfaces.SourceCoords;
import tm.utilities.Assert;
import tm.utilities.Debug;
import tm.utilities.TMFile;
import tm.virtualMachine.VMState;

/**
 * Performs several functions intended for use by Parsers, broadly:
 * <ul><li>utility functions
 * <li>id lookup (and IdTable maintenance)
 * <li>scope tagging
 * <li>generation of Expression nodes
 * <li>semantic lookahead
 * <li>recording definitions and declarations</ul>
 *
 * Follows facade design pattern, as interface to analysis functionality for
 * the Parser et al.
 * @see tm.cpp.parser.Parser
 */
public class ParserContext implements ParserConstants, FundamentalTypeUser {


    /** set of ids encountered in source */
    public IdTable id_table = new IdTable ();
    /** mapping between actual source line and preprocessed line */
    public LineMap line_map ;

    private Cpp_CTSymbolTable ct_symbol_table = new Cpp_CTSymbolTable ();

    private Cpp_ExpressionManager expression_manager =
        new Cpp_ExpressionManager (ct_symbol_table);

    private Cpp_DeclarationManager declaration_manager;
    
    private Cpp_StatementManager statementManager = new Cpp_StatementManager() ;

    //private Parser parser; // for semantic lookahead

    private Debug dbg;

    private VMState vms ;

// Constructor
    public ParserContext( VMState vms, TMFile file ) {
        this.vms = vms ;
        line_map = new LineMap( file ) ;
        RT_Symbol_Table rtst = (RT_Symbol_Table) vms.getSymbolTable ();
        declaration_manager =
            new Cpp_DeclarationManager (expression_manager, statementManager, vms);
        dbg = Debug.getInstance();
        // !! temporary
        //		start_file_scope ("STOPGAP");
    }

// Names

    /**
     * Gives a <code>ScopedName</code> representing a class destructor given
     * the corresponding representation of the class name.
     * @param id the <code>ScopedName</code> representation of the class name,
     * will be converted into destructor name
     */
    public void name_to_destructor_name( ScopedName id ) {
        id.setTerminalId (OpTable.get(TILDE) + id.getTerminalId ());
    }

    //    public ScopedName id_to_name( int id ) { return null; }

    /**
     * Creates an <em>unqualified</em> <code>ScopedName</code> representation
     * of the operator indicated by <code>opcode</code>, with the "operator"
     * prefix, suitable for overloaded implementations
     * @param opcode indicates a particular C++ operator
     * @return a ScopedName representation of the operator
     */
    public ScopedName operator_to_name( int opcode ) {
        return new Cpp_ScopedName (OpTable.get (OPERATOR) +
                                   OpTable.get (opcode));
    }

    /** Assigned to an anonymous class */
    public ScopedName unique_name() {
        return new Cpp_ScopedName (ScopedName.UNIQUE);
    }

    /** Assigned to an abstract declarator in a parameter list */
    public ScopedName anon_name() {
        return new Cpp_ScopedName (ScopedName.ANON);
    }

// Definitions and declarations

    /**
     * Adds a class declaration (defined) to the compile-time symbol
     * table, and creates an AST representation for the new type
     * @param head the class header, including all qualifiers/modifiers
     */
    public void add_class_declaration( ClassHead head ) {
        declaration_manager.add_class_declaration (head);
    }

    /**
     * Indicates that an elaborated type specifier has been encountered
     * for a class (or union or struct).
     * <p>This can occur in the context of an instance declaration, as in
     * <code>struct X x;</code> or a type declaration, such as
     * <code>class Y;</code>
     * @param name the name (including qualifiers) of the class
     */
    public void declare_class( ScopedName name, SourceCoords coords ) {
        declaration_manager.declare_class (name);
    }

    /**
     * Indicates that a new function definition has been encountered.
     * <br>This causes a defining declaration to be added to the
     * symbol table
     * @param coords The location of the definition
     * @param unqualifiedName The name of the function
     * @param tyFun The type of the function
     */
    public StatementNodeLink new_function_defn( SourceCoords coords, ScopedName unqualifiedName, TypeNode tyFun ) {
        Assert.error(tyFun instanceof TyAbstractFun, "Function definition has non functional type." ) ;
        return declaration_manager.new_function_defn (coords, unqualifiedName, (TyAbstractFun)tyFun);
    }

    /**
     * Called to indicate that a constructor's member initializer list
     * has been parsed.
     * @param initializer_list the set of initializations present in the
     * <em>mem-initializer</em> clause
     * @param body_link the current point in the AST representation of the
     * program
     */
    public StatementNodeLink constructor_initializer(
            Vector initializer_list,
            StatementNodeLink body_link ) {
        return declaration_manager.constructor_initializer
            (initializer_list, body_link) ;
    }

    /**
     * Indicates that the class declaration outer block has been completed,
     * and so the list of member declarations can be recorded and
     * analyzed, such that implicit method definitions of special member
     * functions may be generated
     */
    public void record_member_declarations( StatementNodeLink p, SourceCoords coords) {
        declaration_manager.record_member_declarations (p, coords);
    }

    /**
     * Indicates that a declaration has been encountered. This could be
     * a variable or function declaration, possibly others (not currently
     * implemented, like typedef, enum, namespace).
     */
    public StatementNodeLink simple_declaration( StatementNodeLink p,
                                            SourceCoords coords,
                                            ScopedName name,
                                            SpecifierSet spec_set,
                                            TypeNode type ) {
        return declaration_manager.simple_declaration
            (p, coords, name, spec_set, type) ;
    }

    /**
     * Indicates that a parameter declaration has been encountered.
     * The name is as it appears in the code and is not fully qualified.
     */
   public void parameter_declaration (SourceCoords coords, ScopedName name, SpecifierSet spec_set, TypeNode type) {
        declaration_manager.parameter_declaration
            (coords, name, spec_set, type) ;
   }


    /**
     * Called to indicate that an initializer expression has been entered.
     * @param name the id of the entity being initialized
     */
    public void start_initializer( ScopedName name ) {
        declaration_manager.start_initializer (name);
    }

    /**
     * Called to indicate that an initializer expression has been
     * parsed.
     * @param init the initialization expression
     * @param p statement block in which to add the initialization
     * (ignored for static initialization)
     */
    public StatementNodeLink end_initializer( Initializer init,
                                                StatementNodeLink p) {
        return declaration_manager.end_initializer (init, p);
    }

    /**
     * Called to indicate that an initializer expression has been
     * parsed.
     * @param init the initialization expression
     */
    public void end_initializer( Initializer init) {
        declaration_manager.end_initializer (init, null);
    }

    /** Generates or retrieves a <code>TypeNode</code> corresponding
     * to the specifiers/modifiers provided.
     * @param spec_set the type specifiers/modifiers.
     * @return the type
     */
    public TypeNode extract_type (SpecifierSet spec_set) {
        return declaration_manager.type_extractor.extract_type (spec_set);
    }

// Standard definitions

    public void enter_standard_declarations() {
        /* Some useful types and names
            TypeNode ty_int = TyInt.construct() ;
            TypeNode ty_double = TyDouble.construct() ;
            Vector double_vec = new Vector() ; double_vec.addElement( ty_double );
            TypeNode double_to_double = new TyFun( double_vec ) ;
            double_to_double.addToEnd( ty_double ) ;
            ScopedName istream_name = new ScopedName( "istream" ) ;
            ScopedName ostream_name = new ScopedName( "ostream" ) ;

        /* istream  {
            // For now istream has no base classes.
            Vector istream_base_classes = new Vector(0) ;
            ClassHead istream_head = new ClassHead( ClassHead.CLASS, istream_name, istream_base_classes ) ;
            add_class_declaration( istream_head ) ;
            start_class_scope() ;
            StatementNodeLink istream_p = new StatementNodeLink() ;
            // Add a data member -- just so we don't have a 0 size class
            SpecifierSet istream_x_spec_set = new Cpp_SpecifierSet() ;
            istream_x_spec_set.add( Cpp_Specifiers.SP_PRIVATE );
            istream_x_spec_set.add( Cpp_Specifiers.SP_INT );
            TypeNode istream_x_base_type =  extract_type( istream_x_spec_set ) ;
            ScopedName istream_x_name = new ScopedName("x") ;
            simple_declaration( istream_p, 0, istream_x_name, istream_x_spec_set, istream_x_base_type ) ;
            start_initializer( istream_x_name ) ;
            end_initializer( new Initializer( Parser.noExp_node ) ) ;
            // Finish off the class
            record_member_declarations(istream_p) ;
            end_class_scope() ; }


        /* cin  {
            ScopedName cin_name = new ScopedName( "cin" ) ;
            SpecifierSet cin_spec_set = new Cpp_SpecifierSet() ;
            cin_spec_set.add_type_name( istream_name );
            TypeNode cin_type = extract_type( cin_spec_set ) ;
            simple_declaration( null, 0, cin_name, cin_spec_set, cin_type ) ; }

        /* ostream  {
            // For now ostream has no base classes.
            Vector ostream_base_classes = new Vector(0) ;
            ClassHead ostream_head = new ClassHead( ClassHead.CLASS, ostream_name, ostream_base_classes ) ;
            add_class_declaration( ostream_head ) ;
            start_c
            lass_scope() ;
            StatementNodeLink ostream_p = new StatementNodeLink(), p = ostream_p ;
            // Add a data member -- just so we don't have a 0 size class
            SpecifierSet ostream_x_spec_set = new Cpp_SpecifierSet() ;
            ostream_x_spec_set.add( Cpp_Specifiers.SP_PRIVATE );
            ostream_x_spec_set.add( Cpp_Specifiers.SP_INT );
            TypeNode ostream_x_base_type =  extract_type( ostream_x_spec_set ) ;
            ScopedName ostream_x_name = new ScopedName("x") ;
            p = simple_declaration( p, 0, ostream_x_name, ostream_x_spec_set, ostream_x_base_type ) ;
            start_initializer( ostream_x_name ) ;
            end_initializer( new Initializer( Parser.noExp_node ) ) ;
            // Add function members for output.
            // ostream& operator<<( int ) ;
            SpecifierSet ostream_spec_set0 = new Cpp_SpecifierSet() ;
            ostream_spec_set0.add_type_name( ostream_name ) ;
            TypeNode ty_ostream = extract_type( ostream_spec_set0 ) ;
            TypeNode ty_ref_ostream = new TyRef( ty_ostream ) ;
            Vector p0 = new Vector(1); p0.addElement( ty_int ) ;
            TypeNode ty_fun0 = new TyFun(p0) ; ty_fun0.addToEnd( ty_ref_ostream ) ;
            ScopedName name0 = operator_to_name( SHIFTLEFT ) ;
            simple_declaration(p, 0, name0,  ostream_spec_set0, ty_fun0 ) ;
            // Finish off the class
            record_member_declarations(ostream_p) ;
            end_class_scope() ; }

        /* cout  {
            ScopedName cout_name = new ScopedName( "cout" ) ;
            SpecifierSet cout_spec_set = new Cpp_SpecifierSet() ;
            cout_spec_set.add_type_name( ostream_name );
            TypeNode cout_type = extract_type( cout_spec_set ) ;
            simple_declaration( null, 0, cout_name, cout_spec_set, cout_type ) ; }

        /* sin  {
            ScopedName sin_name = new ScopedName( "sin" ) ;
            SpecifierSet sin_spec_set = new Cpp_SpecifierSet() ;
            sin_spec_set.add( Cpp_Specifiers.SP_DOUBLE ) ;
            simple_declaration(null, 0, sin_name, sin_spec_set, double_to_double) ; }
    */
    }

// Scopes

    public void start_class_scope() {
        declaration_manager.start_class_scope ();
    }

    public void end_class_scope() {
        declaration_manager.end_class_scope ();
    }

    public StatementNodeLink start_local_scope(StatementNodeLink p, SourceCoords coords) {
        return declaration_manager.start_local_scope (p, coords );
    }

    public StatementNodeLink end_local_scope(StatementNodeLink p, SourceCoords coords) {
        return declaration_manager.end_local_scope( p, coords );
    }

    public void start_prototype_scope () {
        declaration_manager.start_function_prototype_scope ();
    }

    public void end_prototype_scope () {
        declaration_manager.end_function_prototype_scope ();
    }

    public void end_function_defn ( StatementNodeLink p, SourceCoords coords) {
        declaration_manager.end_function_defn (p, coords);
    }

    public void start_file_scope( String file_name ) {
        declaration_manager.start_file_scope (file_name);
    }

    public void end_file_scope() {
        declaration_manager.end_file_scope ();
    }

    public void start_namespace_scope() {
        declaration_manager.start_namespace_scope ();
    }

    public void end_namespace_scope() {
        declaration_manager.end_namespace_scope ();
    }

    /**
     * Gets the number of variables visible in the current scope.
     * @return the number of visible variables
     */
    public int get_current_var_depth() {
        return declaration_manager.get_current_var_depth ();
    }

// Semantic lookahead

    /**
     * Determines whether a user-defined
     *  type name follows the current (last consumed)
     * token. The type name may or may not be qualified.
     */
    public boolean lookahead_scoped_type_name_follows (Parser p) {
        dbg.msg (Debug.COMPILE, "ParserContext.lookahead_scoped_type_name_follows (Parser p)");
        final String scope = OpTable.get (SCOPE);

        boolean answer = false;

        // 1. build a scoped name using lookahead tokens -
        // the tokens must alternate between ids and the scope operator.
        // Stop building once we hit something else.
        int ti = 1;
        Token ct;
        ScopedName sn = new Cpp_ScopedName ();
        ct = p.getToken (ti++);
        dbg.msg (Debug.COMPILE, "ParserContext.lookahead_scoped_type_name_follows: peeking at "+ct.image);

        if (ct.kind == SCOPE) {
            sn.set_absolute() ;
            ct = p.getToken (ti++) ;
            dbg.msg (Debug.COMPILE, "ParserContext.lookahead_scoped_type_name_follows: peeking at "+ct.image);
        }

        while (true) {
            if (ct.kind != ID ) break ;
            sn.append (ct.image);
            ct = p.getToken (ti++) ;
            dbg.msg (Debug.COMPILE, "ParserContext.lookahead_scoped_type_name_follows: peeking at "+ct.image);
            if (ct.kind != SCOPE ) break ;
            ct = p.getToken (ti++);
            dbg.msg (Debug.COMPILE, "ParserContext.lookahead_scoped_type_name_follows: peeking at "+ct.image);
        }

        if (sn.length () >  0) {
            // 2. perform a lookup in the symbol table.
            // Ensure that:
            // a) the id is found (a new type declaration would be preceded by
            //    the explicit type specifier (class, struct, etc), and would
            //    not generate a call to this method)
            // b) the id refers to a type
            //dbg.msg ("looking up " + sn.getName (), dbg.VARVALUE, dbg.HIGH);
            dbg.msg (Debug.COMPILE, "ParserContext.lookahead_scoped_type_name_follows: looking up "+sn.toString());
            Declaration d =
                ct_symbol_table.lookup(sn, Cpp_LFlags.TYPE_LF).getSingleMember ();

            answer = (d != null);// && d.getCategory().intersects (Cpp_LFlags.TYPE));
        }
        dbg.msg (Debug.COMPILE, "ParserContext.lookahead_scoped_type_name_follows: answer is "+answer);
        return answer;
    }

// Expressions:

    /**
     * Conversion to boolean type, used in contexts where boolean value
     * is required (if conditions, loop conditions)
     */
    public ExpressionNode coerce_to_bool( ExpressionNode e) {
        // ** right now we are using the rules defined for cast expressions
        //    in the cast notation, which may not be exactly correct
        return expression_manager.make_cast_exp (tyBool, e);
    }

    /**
     * Conversion to int type, used in contexts where int value is required
     * (switch statements)
     */
    public ExpressionNode coerce_to_integral( ExpressionNode e ) {
        // ** right now we are using the rules defined for cast expressions
        //    in the cast notation, which may not be exactly correct
        return expression_manager.make_cast_exp (tyInt, e);
    }

    public ExpressionNode make_bool_const( boolean b ) {
        return Literals.make_bool_const (b);
    }

    public ExpressionNode make_char_const( String token_image ) {
        return Literals.make_char_const (token_image);
    }

    public ExpressionNode make_decimal_const( String token_image ) {
        return Literals.make_int_const (token_image, 10);
    }

    public ExpressionNode make_float_const( String token_image ) {
        return Literals.make_float_const (token_image);
    }

    public ExpressionNode make_hex_const( String token_image ) {
        return Literals.make_int_const (token_image, 16);
    }

    public ExpressionNode make_octal_const( String token_image ) {
        return Literals.make_int_const (token_image, 8);
    }

    public ExpressionNode make_string_const( String token_image ) {
        return Literals.make_string_const (token_image, vms);
    }

    /**
     * Generate an AST ExpressionNode that represents an identifier.
     * Evaluating the expression returns the value represented by the
     * identifier.
     */
    public ExpressionNode make_id_exp( ScopedName name ) {
        dbg.msg (Debug.COMPILE, "ParserContext.make_id_exp( ScopedName name )");
        return expression_manager.make_id_exp (name);
    }

    /**
     * Makes the AST representation of the binary operation indicated by
     * operator <code>op</code>, and the two operands <code>x</code> and
     * <code>y</code>.
     * @param op maps to a C++ operator
     * @param x the first operand
     * @param y the second operand
     * @return the AST representation of <code>x op y</code>
     */
    public ExpressionNode make_bin_op(int op, ExpressionNode x, ExpressionNode y) {
        return expression_manager.make_bin_op (op, x, y);
    }

    public ExpressionNode make_unary_op( int op, ExpressionNode x ) {
        return expression_manager.make_unary_op (op, x);
    }

    public ExpressionNode make_postfix_exp( int op, ExpressionNode x ) {
        return expression_manager.make_postfix_exp (op, x);
    }

    public ExpressionNode make_paren_exp( ExpressionNode x ) {
        return expression_manager.make_paren_exp (x);
    }

    /**
     * Makes a conditional expression of the form <code>(x) ? y : z ;</code>
     * @param x the conditional expression to be evaluated
     * @param y the assignment expression evaluated if <code>x</code> is true
     * @param z the assignment expression evaluated if <code>x</code> is false
     * @return the <code>ExpressionNode</code> representing the conditional
     * expression
     */
    public ExpressionNode make_conditional_exp(ExpressionNode x,
                                                ExpressionNode y,
                                                ExpressionNode z) {
        return expression_manager.make_conditional_exp (x, y, z);
    }

    public ExpressionNode make_arrow_exp(ExpressionNode x, ScopedName name) {
        return expression_manager.make_arrow_exp (x, name);
    }

    public ExpressionNode make_member_exp(ExpressionNode x, ScopedName name) {
        return expression_manager.make_member_exp (x, name);
    }

     /** Make an expression for the "new" operator.
      @param type is the type of data to be allocated.
      @param flexible_dimension is a NoExpNode if it is a singleton allocation
             and is otherwise an expression indicating how many objects
             should be allocated.
      @param placement is a list of expressions indicating the placement options.
      @param has_initializer: whether or not there is an initializer.
      @param initializer the initializer parameters, if any.
    */
    public ExpressionNode make_new_exp( TypeNode type,
                                         ExpressionNode flexible_dimension,
                                         NodeList placement,
                                         boolean has_initializer,
                                         NodeList initializer ) {
        if (!has_initializer) initializer = null;
        return expression_manager.make_new_exp (type, flexible_dimension,
                                                placement, initializer);
    }

    public ExpressionNode make_delete_array_exp( ExpressionNode x ) {
        return expression_manager.make_delete_array_exp (x);
    }

    public ExpressionNode make_delete_exp( ExpressionNode x ) {
        return expression_manager.make_delete_exp (x);
    }

    /**
     * Makes a function call expression node, given an id expression and a list
     * of arguments.
     */
    public ExpressionNode make_function_call_exp( ExpressionNode x,
                                                   NodeList args ) {
        return expression_manager.make_function_call_exp (x, args);
    }

    public ExpressionNode make_sizeof_type_exp( TypeNode type ) {
        return expression_manager.make_sizeof_type_exp (type);
    }

    public ExpressionNode make_sizeof_value( ExpressionNode x ) {
        return expression_manager.make_sizeof_value (x);
    }

    public ExpressionNode make_cast_exp( TypeNode type, ExpressionNode x ) {
        return expression_manager.make_cast_exp (type, x);
    }

    /**
     * Builds the AST representation of a reinterpret cast expression
     * (<em>reinterpret_cast&lt;type-id&gt;(expression)</em>)
     *
     * @param type represents the <em>type-id</em>
     * @param x represents the <em>expression</em>
     * @return the AST representation
     */
    public ExpressionNode make_reinterpret_cast_exp( TypeNode type, ExpressionNode x ) {
        return expression_manager.make_cast_exp (type, Eb_Cast.SN_REINTERPRET_CAST,
                                                 x);
    }

    /**
     * Builds the AST representation of a static cast expression
     * (<em>static_cast&lt;type-id&gt;(expression)</em>)
     *
     * @param type represents the <em>type-id</em>
     * @param x represents the <em>expression</em>
     * @return the AST representation
     */
    public ExpressionNode make_static_cast_exp( TypeNode type, ExpressionNode x ) {
        return expression_manager.make_cast_exp (type, Eb_Cast.SN_STATIC_CAST, x);
    }

    /**
     * Builds the AST representation of a const cast expression
     * (<em>const_cast&lt;type-id&gt;(expression)</em>)
     *
     * @param type represents the <em>type-id</em>
     * @param x represents the <em>expression</em>
     * @return the AST representation
     */
    public ExpressionNode make_const_cast_exp( TypeNode type, ExpressionNode x ) {
        return expression_manager.make_cast_exp (type, Eb_Cast.SN_CONST_CAST, x);
    }

    /**
     * Builds the AST representation of a dynamic cast expression
     * (<em>dynamic_cast&lt;type-id&gt;(expression)</em>)
     *
     * @param type represents the <em>type-id</em>
     * @param x represents the <em>expression</em>
     * @return the AST representation
     */
    public ExpressionNode make_dynamic_cast_exp( TypeNode type, ExpressionNode x ) {
        return expression_manager.make_cast_exp (type, Eb_Cast.SN_DYNAMIC_CAST, x);
    }

    /**
     * Builds the AST representation of explicit type conversion in the
     * functional notation
     * (<em>simple-type-specifier (expression-list)</em>).
     *
     * @param type represents the <em>simple-type-specifier</em>
     * @param arg_list represents the <em>expression-list</em>
     * @return the AST representation of the explicit type conversion in
     * functional notation
     */
    public ExpressionNode make_explicit_conversion( TypeNode type, NodeList arg_list ) {
        return expression_manager.make_cast_expression (type, arg_list);
    }

    public ExpressionNode make_this_exp() {
        return expression_manager.make_this_exp ();
    }
      
    public StatementNodeLink startBreakRegion(
    		    StatementNodeLink p,
                SourceCoords coords,
			    int depth,
				boolean hasContinue ) {

        return statementManager.startBreakRegion(p, coords, depth, hasContinue ) ;
    }
    
    public StatementNodeLink endBreakRegion(
    		StatementNodeLink p,
			SourceCoords coords,
			int depth,
			boolean hasContinue ) {
    	
    	return statementManager.endBreakRegion( p, coords, depth, hasContinue ) ;
    }
    
    public void setBreakTarget( StatementNode target ) {
    	statementManager.setBreakTarget( target ) ; ;
    }
    
    public void setContinueTarget( StatementNode target ) {
    	statementManager.setContinueTarget( target ) ;
    }
    
    public void startSwitchStatement(
    		StatementNodeLink p,
			SourceCoords coords,
			int depth,
			ExpressionNode e,
			StatJoin exit) {
    	statementManager.startSwitchStatement(p, coords, depth, e, exit) ;
    }
    
    public void endSwitchStatement( ) {
    	
    	statementManager.endSwitchStatement( ) ;
    }
    
    public void addCaseToSwitch(
    		StatementNodeLink p,
			int depth,
			ExpressionNode e ) {
    	statementManager.addCaseToSwitch(p, depth, e) ;
    }
    
    public void addDefaultToSwitch(
    		StatementNodeLink p,
			int depth ) {
    	statementManager.addDefaultToSwitch(p, depth) ;
    }
    
    public void makeBreak(
    		StatementNodeLink p,
			SourceCoords coords,
			int depth ) {
        statementManager.makeBreak( p, coords, depth );
    }
    
    public void makeContinue(
    		StatementNodeLink p,
			SourceCoords coords,
			int depth ) {
    	statementManager.makeContinue( p, coords, depth) ;
    }
    
    public void makeReturn(
    		StatementNodeLink p,
			SourceCoords coords,
			int depth,
			ExpressionNode e ) {

        if( !( e instanceof NoExpNode ) ) {
        	e = expression_manager.make_return_exp (e);
            p = statementManager.makeDo( p, coords, depth, e ) ; }
        statementManager.makeReturn( p, coords, depth ) ;
    }    

// Errors and Warnings
    protected static final String SORRY_NOT_IMPLEMENTED =
        "Sorry, {0} is not implemented";
    protected static final String SORRY_NOT_IMPLEMENTED_LNO =
        "Sorry, {0} is not implemented -- encountered in line {1}";

    public void sorry_not_implemented( String feature_name ) {
        Assert.apology (SORRY_NOT_IMPLEMENTED, new Object [] { feature_name });
    }

    public void sorry_not_implemented(SourceCoords coords, String feature_name ) {
        Assert.apology (SORRY_NOT_IMPLEMENTED_LNO,
                        new Object [] { feature_name,
                                        coords.toString() });
    }

    public void warning_message( String message ) {}


}


