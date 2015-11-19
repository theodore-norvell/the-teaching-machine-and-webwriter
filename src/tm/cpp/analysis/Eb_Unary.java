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

import tm.clc.analysis.CGRConditionalNode;
import tm.clc.analysis.CGRError;
import tm.clc.analysis.CGRFetch;
import tm.clc.analysis.CGRIncrement;
import tm.clc.analysis.CGROperandNode;
import tm.clc.analysis.CGRSequentialNode;
import tm.clc.analysis.CGRTest;
import tm.clc.analysis.CTSymbolTable;
import tm.clc.analysis.CodeGenRule;
import tm.clc.analysis.Declaration;
import tm.clc.analysis.ExpressionPtr;
import tm.clc.analysis.LFlags;
import tm.clc.analysis.RuleBase;
import tm.clc.analysis.ScopeHolder;
import tm.clc.analysis.ScopedName;
import tm.clc.ast.ConstInt;
import tm.clc.ast.ExpArgument;
import tm.clc.ast.ExpFetch;
import tm.clc.ast.ExpressionNode;
import tm.clc.ast.NoExpNode;
import tm.clc.ast.NodeList;
import tm.clc.ast.TyAbstractArray;
import tm.clc.ast.TyAbstractClass;
import tm.clc.ast.TyAbstractRef;
import tm.clc.ast.TypeNode;
import tm.cpp.ast.ExpFunctionName;
import tm.cpp.ast.ExpNew;
import tm.cpp.ast.ExpNewArray;
import tm.cpp.ast.TyArithmetic;
import tm.cpp.ast.TyArray;
import tm.cpp.ast.TyBool;
import tm.cpp.ast.TyClass;
import tm.cpp.ast.TyPointer;
import tm.cpp.ast.TyRef;
import tm.utilities.Assert;

/**
 * <code>ExpressionBuilder</code> responsible for unary expressions in C++,
 * <em>excluding</em> unary operators, which are covered by
 * <code>Eb_Operator</code>
 * <ul>
 * <li>prefix increment/decrement</li>
 * <li><em>sizeof</em></li>
 * <li><em>new</em></li>
 * <li><em>delete</em></li>
 * </ul>
 */
public class Eb_Unary extends CppExpressionBuilder
    implements FundamentalTypeUser {
    Eb_Initialization eb_init;

    /**
     * Creates a new <code>Eb_Unary</code> instance.
     *
     * @param ruleBase a reference to the set of all <code>CodeGenRules</code>
     */
    public Eb_Unary (RuleBase ruleBase, CTSymbolTable symbolTable,
                     Eb_Initialization initializationRules) {
        super (ruleBase, symbolTable, "unary");
        this.eb_init = initializationRules;
    }

    protected void buildTables () {

        OperandTable operands;

        CodeGenRule rn_unimplemented = rb.get (R_UNIMPLEMENTED_RULE);

        rb.put (R_SIZEOF, new CGRSizeof ());
        // sizeof
        // if operand is a TypeNode, create a sizeof (typeid), otherwise
        // create a sizeof (expression)
        CodeGenRule rn_sizeof =
            new CGROperandNode
                (rb.get (R_SIZEOF),
                 new CGRConditionalNode
                     ((CGRTest) rb.get (T_TYPEID_OPERAND),
                      rb.get (R_NONE),
                      rb.get (R_FETCH)));

        operands = new OperandTable ();
        operands.put (TypeNode.class, rn_sizeof);
        rulesets.put (OpTable.get (SIZEOF), operands);

        // new

        // a) if the object(s) to create is/are of class type
        //    1. if the operator is globally qualified (::new), use the
        //       standard new, else perform lookup for overloaded operator new
        //    2. if not found, use standard new (placement set should be empty)
        //    3. else ensure that an overloaded operator new with parameters
        //       matching args provided in the placement set exists. A call to
        //       this operator will constitute the allocation portion of the
        //       new expression
        //    4. perform lookup for a constructor with parameters matching
        //       the arguments provided in the new-initializer
        //    5. if not found, complain. Otherwise, a call to this constructor
        //       will constitute the initialization portion of the new
        //       expression
        //    6. wrap these in a ?
        //       - if the new expression is for an array,
        //       indicate the cardinality and location
        //       of generated datums using the flexible_dimension set or the
        //       initializer if flexible_dimension is empty
        // b) otherwise, use the default (global) operator new -
        //    1. use default new, using flexible_dimension to indicate
        //       cardinality, placement for allocation arguments, and the
        //       initializer (which must be empty or contain a single element)
        //       to initialize the objects.
        // ?? .. initializer might also be used in place of
        // ??    flexible_dimension
        // ?? - I HAVE NO IDEA WHERE THIS RULE CAME FROM OR WHAT IT MEANS
        // ?? DR April 2, 02

        CodeGenRule r_new_init = new CGRNewInitializer ();
        CodeGenRule rs_new =
            new CGRConditionalNode
                (new CGROverloadedNewTest (symbolTable),
                 new CGRSequentialNode
                     (r_new_init, new CGROverloadedNew ( rb.get( R_CONVERT_VALUE_ARGUMENT ),
                                                         rb.get( R_CONVERT_REFERENCE_INITIALIZER ),
                                                         rb.get( R_CONVERT_NO_PARAMETER_ARGUMENT ))),
                 new CGRSequentialNode
                     (new CGRNew (), r_new_init));

        operands = new OperandTable ();
        operands.put (TypeNode.class, rs_new);
        rulesets.put (OpTable.get (NEW), operands);


        // delete

        // 1.  ensure that the pointer is not to a constant
        // 2a. if the object(s) to create is/are of class type
        //    1. if the operator is globally qualified (::delete), use the
        //       standard delete, else perform lookup for overloaded operator
        //       delete
        //    2. if not found, use standard delete
        //    3. else use overloaded operator delete
        //       A call to this operator will constitute the deallocation
        //       portion of the delete expression, and will call the destructor
        //       if one exists (destruction portion of the delete expression)
        //    6. ?? if the delete expression is for a single object
        //       (not an array),
        //       construct an ExpDelete using all this as an argument.
        // 2b. otherwise, use the default (global) operator delete
        //
        // note: deletion of arrays follows a similar pattern, but
        // identification of array cardinality is required, and construction
        // of a node_list representing the destructor calls corresponding to
        // each element in the array. It is unclear whether either or both of
        // these steps will be handled up front or by the AST representation of
        // delete.

        CodeGenRule r_delete =
            new CGRSequentialNode( new CGRDelete (), new CGRFetch (0) ) ;

        rb.put (R_DELETE_EXPRESSION, r_delete);
        CodeGenRule rs_overloaded_delete =
            new CGRConditionalNode
            ((CGRTest) rb.get (T_GLOBALLY_QUALIFIED),
             r_delete,
             new CGRConditionalNode
             ((CGRTest) rb.get (T_OVERLOADED_OPERATOR),
              new CGROverloadedDelete (rb.get( R_CONVERT_VALUE_ARGUMENT ),
                                        rb.get( R_CONVERT_REFERENCE_INITIALIZER ),
                                        rb.get( R_CONVERT_NO_PARAMETER_ARGUMENT )),
              r_delete));

        operands = new OperandTable ();
        operands.put (TyClass.class, rs_overloaded_delete);
        operands.put (TypeNode.class, r_delete);
        rulesets.put (OpTable.get (DELETE), operands);


        // 5.2.6 : prefix increment/decrement
        // 1. operand must be of arithmetic or pointer type (not boolean for
        //    decrement
        // 2. operand must be modifiable lvalue
        // 3. build expression

        // ++
        rb.put (R_PREFIX_INCREMENT, new CGRIncrement (true, true));
        CodeGenRule rn_prefix_increment =
            new CGROperandNode (rb.get (R_PREFIX_INCREMENT),
                                  rb.get (R_MODIFIABLE_LVALUE));
        operands = new OperandTable ();
        operands.put (TyArithmetic.class, rn_prefix_increment);
        operands.put (TyPointer.class, rn_prefix_increment);
        rulesets.put (OpTable.get (PLUSPLUS), operands);

        // --
        rb.put (R_PREFIX_DECREMENT, new CGRIncrement (true, false));
        CodeGenRule rn_prefix_decrement =
            new CGROperandNode (rb.get (R_PREFIX_DECREMENT),
                                  rb.get (R_MODIFIABLE_LVALUE));
        CodeGenRule rn_disallow_boolean_decrement =
            new CGRError ("cannot decrement boolean");
        operands = new OperandTable ();
        operands.put (TyBool.class, rn_disallow_boolean_decrement);
        operands.put (TyArithmetic.class, rn_prefix_decrement);
        operands.put (TyPointer.class, rn_prefix_decrement);
        rulesets.put (OpTable.get (MINUSMINUS), operands);
    }

    protected ExpressionNode make_sizeof (TypeNode t) {
        ExpressionPtr size_t =
            new ExpressionPtr (new Cpp_ScopedName (OpTable.get (SIZEOF)),
                                new Object [] {t});
        rb.get (R_SIZEOF).apply (size_t);
        return size_t.get ();
    }

    private static final String INVALID_OPERATOR_NEW =
        "{0} not found for type {1}";
    /**
     * Determines if an overloaded implementation exists and is being
     * used instead of the builtin <em>operator new</em> or
     * <em>operator new []</em>
     */
    public class CGROverloadedNewTest extends CGROverloadedTest {
        private int placement_pos = 2; // position in operands of placement list
        private int type_pos = 0; // position in operands of type
        public CGROverloadedNewTest (CTSymbolTable st) {
            super (st);
        }

        public boolean applies (ExpressionPtr exp) {
            TypeNode t = exp.get_type (type_pos);

            // new array ?
            if (t instanceof TyAbstractArray) {
                // - use element type
                t = ((TyAbstractArray) t).getElementType ();
            }

            ScopedName operator = exp.op ();

            Vector args_v = make_args_v (t, exp.get_list (placement_pos));

            ExpFunctionName efn = null;

            if (!operator.is_absolute () && t instanceof TyAbstractClass) {
                // lookup in class scope
                Declaration cd =
                    symbolTable.getClassDeclaration ((TyAbstractClass) t);
                Assert.check (cd != null);
                ScopeHolder c_scope = (ScopeHolder) cd.getDefinition ();
                efn = findOverloadedImplementation
                    (operator, args_v, new LFlags (), c_scope);
            }

            if (efn == null) { // lookup in global scope
                Assert.check (symbolTable instanceof Cpp_CTSymbolTable);
                ScopeHolder g_scope =
                    ((Cpp_CTSymbolTable) symbolTable).getGlobalScope ();
                efn = findOverloadedImplementation
                    (operator, args_v, new LFlags (), g_scope);
                if (efn == null && operator.length () > 1) { // error
                    Assert.apology (INVALID_OPERATOR_NEW, new String []
                        {operator.getName (), t.getTypeString ()});
                }
            }

            // add the function name to the operands
            if (efn != null) exp.addOperand (efn);

            return efn != null;
        }

        private Vector make_args_v (TypeNode t, NodeList placement) {
            Vector args_v = new Vector ();
            // first arg is sizeof (t)
            args_v.addElement (make_sizeof (t));

            for (int i = 0; i < placement.length (); i++)
                args_v.addElement (placement.get (i));

            return args_v;
        }
    }

    /**
     * Uses initializer args to generate an initialization expression
     * appropriate to the object being created.
     * <br><em>direct-initialization</em> is the form used for
     * new expressions, where an initializer exists. If not,
     * <em>default-initialization</em> is used for non-POD class objects.
     */
    public class CGRNewInitializer extends CodeGenRule {

        public void apply (ExpressionPtr exp) {
            // initializer args are at position 3
            int init_pos = 3;

            // type of object being initialized
            TypeNode obj_type = exp.get_type (0);

            // ExpArgument is used to refer to object being initialized
            // (type is at pos 0), for a built-in new, otherwise we will
            // use the (already generated) result of the overloaded new
            // !! overloaded operator new [] NOT YET SUPPORTED

            ExpressionNode new_obj = exp.get ();
            if (new_obj == null) new_obj = new ExpArgument (new TyRef(obj_type), 0);

            // need a new ExpressionPtr to apply initialization rules
            ExpressionPtr init_exp = null;

            // NOTE: The code here used to allow for a single
            // expression to be at the init_pos.  I can't see
            // how it can be anything other than a node list
            // or a NoExpNode (which I'm using to signal that there
            // is no initialization in the "new expression".
            // Therefore I'm asserting that at init_pos there
            // must either be a NodeList or a singe NoExpNode.
            NodeList init_args ;
            if (exp.is (NodeList.class, init_pos))
                init_args = exp.get_list (init_pos);
            else { // no initializer
                ExpressionNode single_init = exp.get (init_pos);
                Assert.check( single_init instanceof NoExpNode ) ;
                init_args = null ; }

            // is initializer there ?
            if ( init_args != null ) {
                //   Zero or more initializers
                //   See ISO 5.3.4 para 15

                int len = init_args.length() ;
                ScopedName opid ;
                if( len == 1 ) {
                    opid = new Cpp_ScopedName (OpTable.get (ASSIGN)) ; }
                else {
                    opid = new Cpp_ScopedName (OpTable.get (OPEN_PAREN)) ; }

                Object [] exps = new Object[ 1 + len ] ;
                exps[0] = new_obj ;
                for( int i=0 ; i<len ; ++i ) exps[ i+1 ] = init_args.get(i) ;

                init_exp = new ExpressionPtr (opid, exps);

                if( len == 0 ) {
                    // Default initialize
                    init_exp.opcat = eb_init.DEFAULT_INIT_SN;
                } else if( obj_type instanceof TyAbstractClass ) {
                    // Call the constructor
                    init_exp.opcat = eb_init.DIRECT_INIT_SN ;
                } else if( len == 1 ) {
                    init_exp.opcat = eb_init.COPY_INIT_SN;
                } else {
                    Assert.apology( "Too many initializers" ); }

            } else { // No initializer
                boolean pod = true;
                if ( obj_type instanceof TyAbstractClass) {
                    Declaration classDecl =	symbolTable.getClassDeclaration
                        ((TyAbstractClass) obj_type);
                    pod = classDecl.getCategory().intersects
                        (Cpp_LFlags.POD);
                }

                if ( !pod ) {
                    //   - default initialize
                    init_exp = new ExpressionPtr
                        (new Cpp_ScopedName (OpTable.get (ASSIGN)),
                         new Object [] {new_obj});
                    init_exp.opcat = eb_init.DEFAULT_INIT_SN;
                }
            }

            if (init_exp != null) {
                // apply the initialization rules
                eb_init.apply (init_exp);

                // add the generated initialization expression
                if (exp.get () == null) { // builtin new
                    // add to the set of operands (will be position 4)
                    exp.addOperand (init_exp.get ());
                } else {
                    // replace exp result
                    exp.set (init_exp.get ());
                }
            } // else do nothing - no initialization
        }
    }

    /**
     * Generates the AST representation of an overloaded <em>new</em> operator,
     * as a specialized case of function call.
     */
    public class CGROverloadedNew extends CGRFunctionCall {

        public CGROverloadedNew (CodeGenRule value_argument_conversion_rule,
                                 CodeGenRule reference_argument_conversion_rule,
                                 CodeGenRule rs_convert_no_parameter_argument) {
            super (value_argument_conversion_rule,
                   reference_argument_conversion_rule,
                   rs_convert_no_parameter_argument,
                   true); }

        /**
         * The <em>new</em> method uses the allocation operands as its arguments.
         * @param exp contains the expression being built - allocation argument(s)
         * if any are expected to be at position 2.
         * @return the allocation arguments in a <code>NodeList</code>
         */
        protected NodeList build_args_list (ExpressionPtr exp) {
            NodeList args;
            if (exp.is (NodeList.class, 2))
                args = exp.get_list (2);
            else
                args = new NodeList (exp.get (2));

            // first arg is sizeof (t)
            args.addFirstChild (make_sizeof (exp.get_type (0)));

            return args;
        }
    }

    /**
     * Generates the standard new allocation expression for arrays and
     * other objects
     */
    public class CGRNew extends CodeGenRule {

        public void apply (ExpressionPtr exp) {

            TypeNode t = exp.get_type (0);

            ExpressionNode flex_dimen = exp.get (1);

            // array or not ?
            boolean new_array =
                ((t instanceof TyArray) ||
                 !(flex_dimen instanceof NoExpNode));


            if (new_array) { // array missing flexible_dimension ?
                if (flex_dimen instanceof NoExpNode) {
                    // we need to construct a constant expression
                    // representing the array's cardinality
                    int card = ((TyArray) t).getNumberOfElements ();
                    flex_dimen = new ConstInt (ctyInt, Integer.toString (card),
                                                card);
                } else { // perform fetch if necessary
                    if (flex_dimen.get_type () instanceof TyAbstractRef) {
                        TypeNode base_type =
                            ((TyAbstractRef) flex_dimen.get_type())
                            .getPointeeType ();
                        flex_dimen = new ExpFetch (base_type, flex_dimen);
                    }
                }
            }


            boolean show_init ;
            final int init_pos = 3;
            NodeList init_args ;
            if (exp.is (NodeList.class, init_pos)) {
                init_args = exp.get_list (init_pos);
                show_init = true ; }
            else { // no initializer
                ExpressionNode single_init = exp.get (init_pos);
                Assert.check( single_init instanceof NoExpNode ) ;
                init_args = new NodeList() ;
                show_init = false ; }

            // initialization expression
            ExpressionNode init_exp = null;
            if (exp.operandCount () > 4) init_exp = exp.get (4);

            ExpressionNode new_exp;
            TyPointer result_type = new TyPointer ();
            result_type.addToEnd (t);
            if (new_array) {
                new_exp = new ExpNewArray (result_type, show_init,
                                             flex_dimen, init_exp);
            } else {
                new_exp = new ExpNew (result_type,
                                       show_init, init_args, init_exp);
            }

            // phew!
            exp.set (new_exp);
        }
    }
}



