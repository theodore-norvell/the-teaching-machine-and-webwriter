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

import tm.clc.analysis.CGRSequentialNode;
import tm.clc.analysis.CodeGenRule;
import tm.clc.analysis.Declaration;
import tm.clc.analysis.ExpressionBuilder;
import tm.clc.analysis.ExpressionManager;
import tm.clc.analysis.ExpressionPtr;
import tm.clc.analysis.FunctionDeclaration;
import tm.clc.analysis.RuleBase;
import tm.clc.analysis.ScopeHolder;
import tm.clc.analysis.ScopedName;
import tm.clc.ast.ExpResult;
import tm.clc.ast.ExpressionNode;
import tm.clc.ast.NoExpNode;
import tm.clc.ast.NodeList;
import tm.clc.ast.OpUpConversion;
import tm.clc.ast.TyAbstractArray;
import tm.clc.ast.TypeNode;
import tm.cpp.ast.ExpFunctionName;
import tm.cpp.ast.ExpMemberName;
import tm.cpp.ast.TyFun;
import tm.cpp.ast.TyRef;
import tm.cpp.parser.ParserConstants;
import tm.utilities.Assert;

/**
 * Functions as the external interface to expression building logic.
 * Used by <code>ParserContext</code> - many of its methods consist of 
 * single calls to <code>Cpp_ExpressionManager</code>. 
 * As well as providing the key points of access to expression building 
 * in C++, <code>Cpp_ExpressionManager</code> initializes the 
 * <code>RuleBase</code> of <code>CodeGenRules</code> that are used in 
 * analyzing, validating, and bulding the AST representation of expressions.
 * @author Derek Reilly
 * @version 1.0
 * @see tm.cpp.analysis.ParserContext
 * @created November 26, 2001
 */
public class Cpp_ExpressionManager extends ExpressionManager 
    implements ParserConstants, FundamentalTypeUser {

    /**
     * Standard sorry not implemented message for missing expression categories
     */
    protected static final String SORRY_NOT_IMPLEMENTED = 
        "Sorry, {0} is not implemented";

    private static final String NEED_FN_DECL = 
        "Function declaration required to perform this operation";

    protected Cpp_CTSymbolTable symbolTable;

    // expression builders
    /**
     * A reference to the <code>ExpressionBuilder</code> for 
     * operators (binary, unary, ternary conditional).
     */
    protected Eb_Operator eb_Operator;
    /**
     * A reference to the <code>ExpressionBuilder</code> for 
     * primary expressions
     */
    protected Eb_Primary eb_Primary;

    /**
     * A reference to the <code>ExpressionBuilder</code> for 
     * unary expressions
     */
    protected Eb_Unary eb_Unary;
    /**
     * A reference to the <code>ExpressionBuilder</code> for 
     * postfix expressions
     */
    protected Eb_Postfix eb_Postfix;
    /**
     * A reference to the <code>ExpressionBuilder</code> for 
     * cast expressions
     */
    protected Eb_Cast eb_Cast;
    /**
     * A reference to the <code>ExpressionBuilder</code> for 
     * intialization expressions
     */
    protected Eb_Initialization eb_Initialization;

    /** rule for id expressions */
    protected CodeGenRule r_id_exp;
    /** rule for parentheses */
    protected CodeGenRule r_parenth_exp;
    /** rule for function calls */
    protected CodeGenRule r_func_call;
    /** rule for class member access in the dot notation */
    //protected CodeGenRule r_member_exp; // NEVER USED!
    /** rule for class member access in the arrow notation */
    //protected CodeGenRule r_arrow_exp; // NEVER USED!
    /** rule for <em>this</em> expressions */
    protected CodeGenRule r_this_exp;
    /** rule for constructor result expressions - non-standard, currently 
     * unused
     */
    protected CodeGenRule r_constructor_result_exp;

    /** id for <em>this</em> keyword */
    protected static ScopedName sn_this = new Cpp_ScopedName ("this");
    /** id for <em>new</em> keyword */
    protected static ScopedName sn_new = 
        new Cpp_ScopedName (OpTable.get (NEW));
    /** id for <em>new []</em> */
    protected static ScopedName sn_new_array = 
        new Cpp_ScopedName (OpTable.get (NEW) + OpTable.get (OPEN_BRACKET));
    /** id for <em>delete</em> keyword */
    protected static ScopedName sn_delete = 
        new Cpp_ScopedName (OpTable.get (DELETE));
    /** id for <em>delete []</em> */
    protected static ScopedName sn_delete_array = 
        new Cpp_ScopedName (OpTable.get (DELETE) + OpTable.get (OPEN_BRACKET));
    /** id for <em>sizeof</em> keyword */
    protected static ScopedName sn_sizeof = 
        new Cpp_ScopedName (OpTable.get (SIZEOF));
    /** id for ternary conditional operator */
    protected static ScopedName sn_tern = new Cpp_ScopedName ("?:");
    /** id for empty/no operator */
    protected static ScopedName sn_empty = new Cpp_ScopedName ("");

    /* NOTE >>>
     * At the moment the rules are being generated from scratch every time
     * a new Cpp_ExpressionManager is created (i.e. each time a new file is
     * opened from the GUI, at least). 
     * What we'd like to do is update only the dynamic portions of the rules, 
     * such as a symbol table reference. To permit multiple sessions, we will
     * need context switching, or stick with the current situation. RuleProxy
     * can be used to give lazy generation of rule sequences, but I don't 
     * know how much time this will save in practice -- need to test in live 
     * setting. DR 21/02/02
     */

    /**
     * Creates a new <code>Cpp_ExpressionManager</code> instance, 
     * building the <code>RuleBase</code>.
     * @param ctst the compile-time symbol table
     */
    public Cpp_ExpressionManager (Cpp_CTSymbolTable ctst) {
        this.symbolTable = ctst;
        rb = new RuleBase ();
        buildRuleBase ();
    }

    /** Code details of returning a value from a function 
     * This involves possible conversion of <code>e</code> to the
     * return type of the function.
     */
    public ExpressionNode make_return_exp(ExpressionNode e ) {
        ScopeHolder sh = 
            symbolTable.getCurrentScope().getInnermostDeclaredScope ();

        if (sh == null) Assert.apology (NEED_FN_DECL);

        Declaration d = sh.getOwnDeclaration ();
        
        if (!(d instanceof FunctionDeclaration)) Assert.apology (NEED_FN_DECL);
 
        // ** remainder to be converted into application of ruleset. 

        TypeNode funType = d.getType() ;
        Assert.check( funType instanceof TyFun ) ;
        TypeNode rtnType = ((TyFun)funType).returnType() ;

        ExpressionPtr result = new ExpressionPtr 
            (null, new Cpp_ScopedName (OpTable.get (ASSIGN)), 
             new Object []	{new ExpResult (new TyRef (rtnType)), e}); 
        result.opcat = Eb_Initialization.COPY_INIT_SN;

        eb_Initialization.apply (result);

        return result.get ();
            
    }



    /**
     * Generates the AST representation of an id expression given the 
     * <code>ScopedName</code> provided.
     *
     * @param id a <code>ScopedName</code> value
     * @return the id expression for the <code>id</code>, or an 
     * <code>ExpFunctionName</code> placeholder if the id refers to a 
     * function.
     */
    public ExpressionNode make_id_exp (ScopedName id) {
        ExpressionPtr idExpPtr = new ExpressionPtr (id, new Object [] {});

        r_id_exp.apply (idExpPtr);
        return idExpPtr.get ();
    }

    /**
     * Make a binary operation expression - this includes both built-in and
     * overloaded uses of an operator.
     *
     * @param op indicates the binary operator encountered, using constants
     * defined in <code>Cpp.Parser.ParserConstants</code>
     * @param x left operand
     * @param y right operand
     * @return the AST representation of the expression
     */
    public ExpressionNode make_bin_op (int op, ExpressionNode x, 
                                        ExpressionNode y) {
        ScopedName snop = new Cpp_ScopedName (OpTable.get (op));
        ExpressionPtr binExpPtr = 
            new ExpressionPtr (snop, new Object [] {x, y});
        
        eb_Operator.apply (binExpPtr);

        return binExpPtr.get ();
    }
    
    /**
     * Make a unary operation expression - this includes both built-in and
     * overloaded uses of an operator.
     *
     * @param op indicates the unary operator encountered, using constants
     * defined in <code>Cpp.Parser.ParserConstants</code>
     * @param x the operand
     * @return the AST representation of the expression
     */
    public ExpressionNode make_unary_op( int op, ExpressionNode x ) {
        ScopedName snop = new Cpp_ScopedName (OpTable.get (op));
        ExpressionPtr unExpPtr = 
            new ExpressionPtr (snop, new Object [] {x});

        eb_Operator.apply (unExpPtr);

        return unExpPtr.get ();
    }

    /**
     * Describe <code>make_postfix_exp</code> method here.
     *
     * @param op an <code>int</code> value
     * @param x an <code>ExpressionNode</code> value
     * @return an <code>ExpressionNode</code> value
     */
    public ExpressionNode make_postfix_exp( int op, ExpressionNode x ) {

        ScopedName snop = new Cpp_ScopedName (OpTable.get (op));
        ExpressionPtr pfExpPtr = 
            new ExpressionPtr (snop, new Object [] {x});

        eb_Postfix.apply (pfExpPtr);

        return pfExpPtr.get ();
    }

    /**
     * Generate a parentheses expression - wrapping the provided expression
     * in parentheses.
     *
     * @param x the expression to wrap in parentheses
     * @return an <code>ExpressionNode</code> representing <code>(x)</code>
     */
    public ExpressionNode make_paren_exp( ExpressionNode x ) {
        ExpressionPtr exp = new ExpressionPtr (x);
        r_parenth_exp.apply (exp);
        return exp.get ();
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
        ExpressionPtr ternExpPtr = 
            new ExpressionPtr (sn_tern, new Object [] {x, y, z});

        eb_Operator.apply (ternExpPtr);

        return ternExpPtr.get ();
    }

    /**
     * Class member access via arrow notation.
     *
     * @param x an expression with an object pointer value
     * @param name identifies the member to access
     * @return AST representation of the member access expression
     */
    public ExpressionNode make_arrow_exp(ExpressionNode x, ScopedName name) {
        ScopedName snarrow = new Cpp_ScopedName (OpTable.get (ARROW));
        ExpressionPtr arrExpPtr = new ExpressionPtr 
            (snarrow, new Object [] {x, new ExpMemberName (name)});
        eb_Postfix.apply (arrExpPtr);
        return arrExpPtr.get ();
    }

    /**
     * Class member access via arrow notation.
     *
     * @param x an expression with a class/object value
     * @param name identifies the member to access
     * @return AST representation of the member access expression, or 
     * an <code>ExpFunctionName</code> if this is a member function call.
     */
    public ExpressionNode make_member_exp(ExpressionNode x, ScopedName name) {
        ScopedName sndot = new Cpp_ScopedName (OpTable.get (DOT));
        ExpressionPtr memExpPtr = new ExpressionPtr 
            (sndot, new Object [] {x, new ExpMemberName (name)});
        eb_Postfix.apply (memExpPtr);
        return memExpPtr.get ();
    }
    
    /**
     * Describe <code>make_new_exp</code> method here.
     *
     * @param type a <code>TypeNode</code> value
     * @param flexible_dimension an <code>ExpressionNode</code> value
     * @param placement a <code>NodeList</code> value
     * @param initializer a <code>NodeList</code> value
     * @return an <code>ExpressionNode</code> value
     */
    public ExpressionNode make_new_exp( TypeNode type,
                                         ExpressionNode flexible_dimension,
                                         NodeList placement,
                                         NodeList initializer ) {
        if (flexible_dimension == null) flexible_dimension = new NoExpNode ();
        if (placement == null) placement = new NodeList ();

        // !! right now there is no indication of the operator as 
        //    encountered in source. This is needed to perform 
        //    proper overload resolution.
        ScopedName op = (type instanceof TyAbstractArray || 
                          !(flexible_dimension instanceof NoExpNode))
            ? sn_new_array
            : sn_new;

        Object initializer_for_exp_ptr = initializer==null
                                       ? (Object)new NoExpNode()
                                       : (Object)initializer ;
        ExpressionPtr newExpPtr = new ExpressionPtr (op, new Object []
            {type, flexible_dimension, placement, initializer_for_exp_ptr });
        newExpPtr.opcat = sn_new;

        eb_Unary.apply (newExpPtr);

        return newExpPtr.get ();
    }

    /**
     * Describe <code>make_delete_array_exp</code> method here.
     *
     * @param x an <code>ExpressionNode</code> value
     * @return an <code>ExpressionNode</code> value
     */
    public ExpressionNode make_delete_array_exp( ExpressionNode x ) {
        ExpressionPtr delExpPtr = 
            new ExpressionPtr (sn_delete_array, new Object [] {x});
        delExpPtr.opcat = sn_delete;

        eb_Unary.apply (delExpPtr);

        return delExpPtr.get ();
    }

    /**
     * Describe <code>make_delete_exp</code> method here.
     *
     * @param x an <code>ExpressionNode</code> value
     * @return an <code>ExpressionNode</code> value
     */
    public ExpressionNode make_delete_exp( ExpressionNode x ) {
        ExpressionPtr delExpPtr = 
            new ExpressionPtr (sn_delete, new Object [] {x});

        eb_Unary.apply (delExpPtr);

        return delExpPtr.get ();
    }

    /**
     * Makes a function call expression node, given a function name and a list
     * of arguments.
     * @param x the function name
     * @param args function arguments
     * @return the function call expression
     */
    public ExpressionNode make_function_call_exp( ExpressionNode x, 
                                                   NodeList argsnl ) {

        ScopedName snfn = 
            (x instanceof ExpFunctionName) 
            ? ((ExpFunctionName) x).getName ()
            : new Cpp_ScopedName ("***function ptr***"); // function pointer - not implemented, caught downstream

        Object [] args = new Object [argsnl.length ()];
        for (int i = 0; i < args.length; i++) 
            args [i] = argsnl.get (i);

        ExpressionPtr fnExpPtr = new ExpressionPtr (x, snfn, args);		

        r_func_call.apply (fnExpPtr);

        return fnExpPtr.get ();
    }

    /**
     * Describe <code>make_sizeof_type_exp</code> method here.
     *
     * @param type a <code>TypeNode</code> value
     * @return an <code>ExpressionNode</code> value
     */
    public ExpressionNode make_sizeof_type_exp( TypeNode type ) {
        ExpressionPtr soExpPtr = 
            new ExpressionPtr (sn_sizeof, new Object [] {type});

        eb_Unary.apply (soExpPtr);

        return soExpPtr.get ();
    }

    /**
     * Make a <em>sizeof</em> expression given an expression as an operand 
     * (as opposed to a <em>typeid</em>).
     * @param x the expression from which to derive the <em>size_t</em> value
     * @return the AST representation of the <em>sizeof</em> expression
     */
    public ExpressionNode make_sizeof_value( ExpressionNode x ) {
        ExpressionPtr soExpPtr = 
            new ExpressionPtr (sn_sizeof, new Object [] {x});

        eb_Unary.apply (soExpPtr);

        return soExpPtr.get ();
    }

    /**
     * Builds the AST representation of explicit type conversion in the cast 
     * notation 
     * (<em>(type-id) cast-expression</em>). 
     *
     * @param type represents the <em>type-id</em>
     * @param x represents the <em>cast-expression</em>
     * @return the AST representation of the explicit type conversion in 
     * cast notation
     */
    public ExpressionNode make_cast_exp( TypeNode type, ExpressionNode x ) {

        ExpressionPtr cExpPtr = 
            new ExpressionPtr (sn_empty, new Object [] {type, x});

        eb_Cast.castNotationExpression (cExpPtr);

        return cExpPtr.get ();
    }

    /**
     * Builds the AST representation of a cast expression in the new notation
     * (<em>{const|reinterpret|dynamic|static}_cast&lt;type-id&gt;(expression)</em>)
     *
     * @param type represents the <em>type-id</em>
     * @param op one of <em>static_cast, dynamic_cast, reintpret_cast, const_cast</em>
     * @param x represents the <em>expression</em>
     * @return the AST representation
     */
    public ExpressionNode make_cast_exp( TypeNode type, ScopedName op,
                                          ExpressionNode x ) {
        ExpressionPtr cExpPtr = 
            new ExpressionPtr (op, new Object [] {type, x});

        eb_Cast.apply (cExpPtr);

        return cExpPtr.get ();
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
    public ExpressionNode make_cast_expression ( TypeNode type, NodeList arg_list ) {

        Object [] operands = new Object [arg_list.length () + 1];
        operands [0] = type;
        for (int i = 0; i < arg_list.length (); i++) {
            operands [i+1] = arg_list.get (i);
        }

        ExpressionPtr fncExpPtr = new ExpressionPtr (sn_empty, operands);

        eb_Cast.functionNotationExpression (fncExpPtr);

        return fncExpPtr.get ();
    }

    /**
     * Describe <code>make_this_exp</code> method here.
     *
     * @return an <code>ExpressionNode</code> value
     */
    public ExpressionNode make_this_exp() {
        ExpressionPtr thisExpPtr = new ExpressionPtr (sn_this, new Object [] {});
        r_this_exp.apply (thisExpPtr);
        return thisExpPtr.get ();
    }

    /**
     * Make a node representing a subobject of the current object
     *
     * @return an <code>ExpressionNode</code> value
     */
    public ExpressionNode make_super_exp( TypeNode ty, String image, int[] path ) {
        ExpressionPtr thisExpPtr = new ExpressionPtr (sn_this, new Object [] {});
        r_this_exp.apply (thisExpPtr);
        ExpressionNode thisExp = thisExpPtr.get ();
        return new OpUpConversion( ty, image, path, thisExp) ;
    }

    /**
     * Generate the AST representation of setting a constructor's
     * return value to be a reference to the class object itself. 
     * @return the AST representation
     */
    public ExpressionNode make_constructor_result_exp() {
        ExpressionPtr crExpPtr = new ExpressionPtr (sn_empty, null);
        r_constructor_result_exp.apply (crExpPtr);
        return crExpPtr.get ();
    }

    /**
     * Build a table holding rules shared among more than one expression
     * builder.
     */
    private void buildRuleBase () {
        eb_Operator = new Eb_Operator (rb, symbolTable);
        eb_Primary = new Eb_Primary (rb, symbolTable);
        eb_Initialization = new Eb_Initialization (rb, symbolTable);
        eb_Unary = new Eb_Unary (rb, symbolTable, eb_Initialization);
        eb_Postfix = new Eb_Postfix (rb, symbolTable);
        eb_Cast = new Eb_Cast (rb, symbolTable, eb_Initialization);


        r_id_exp = rb.get (Eb_Primary.R_ID_EXPRESSION);
        r_parenth_exp = rb.get (Eb_Primary.R_PARENTHESES_EXPRESSION);
        r_func_call = rb.get (Eb_Postfix.RS_FUNCTION_CALL);
        //r_member_exp = rb.get (Eb_Postfix.RS_MEMBER_EXPRESSION);
        //r_arrow_exp = rb.get (Eb_Postfix.RS_ARROW_EXPRESSION);
        r_this_exp = rb.get (Eb_Primary.R_THIS_EXPRESSION);

        r_constructor_result_exp = 
             new CGRSequentialNode
             (rb.get (ExpressionBuilder.R_RESULT), 
              rb.get (Eb_Unary.R_THIS_EXPRESSION));
    }

}

