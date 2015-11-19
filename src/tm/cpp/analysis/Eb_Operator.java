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

import java.util.Hashtable;

import tm.clc.analysis.CGRBinaryFloatingOp;
import tm.clc.analysis.CGRBinaryIntegralOp;
import tm.clc.analysis.CGRBinaryLogicalOp;
import tm.clc.analysis.CGRError;
import tm.clc.analysis.CGRIncrement;
import tm.clc.analysis.CGROperandBranchNode;
import tm.clc.analysis.CGROperandNode;
import tm.clc.analysis.CGRParentheses;
import tm.clc.analysis.CGRBinaryPointerOp;
import tm.clc.analysis.CGRSequentialNode;
import tm.clc.analysis.CGRSwapOperands;
import tm.clc.analysis.CGRUnaryFloatingOp;
import tm.clc.analysis.CGRUnaryIntegralOp;
import tm.clc.analysis.CGRUnimplemented;
import tm.clc.analysis.CTSymbolTable;
import tm.clc.analysis.CodeGenRule;
import tm.clc.analysis.ExpressionPtr;
import tm.clc.analysis.RuleBase;
import tm.clc.analysis.RuleProxy;
import tm.clc.ast.ExpId;
import tm.clc.ast.ExpressionNode;
import tm.clc.ast.TypeNode;
import tm.cpp.ast.TyArithmetic;
import tm.cpp.ast.TyArray;
import tm.cpp.ast.TyBool;
import tm.cpp.ast.TyClass;
import tm.cpp.ast.TyFloating;
import tm.cpp.ast.TyIntegral;
import tm.cpp.ast.TyPointer;
import tm.cpp.parser.ParserConstants;
import tm.utilities.Assert;
import tm.utilities.Debug;

/**
 * <code>ExpressionBuilder</code> responsible for operators in C++:
 * <ul>binary operations:
 * <li>pointer to member (->*, .*)</li>
 * <li>multiplicative (*, /, %)</li>
 * <li>additive (+, -)</li>
 * <li>shift (&lt;&lt;, &gt;&gt;>>)</li>
 * <li>relational (&lt;, &gt;, &lt;=, &gt;=)</li>
 * <li>equality (==, !=)</li>
 * <li>bitwise (&amp;, |, ^)</li>
 * <li>logical (&amp;&amp;, ||)</li>
 * <li>assignment (=, shorthand assignment operators)</li>
 * <li>comma (,)</li>
 * <li>subscript is treated as a binary operator ([])</li>
 * </ul>
 * <ul>unary operations:
 * <li>indirection (*)</li>
 * <li>address-of (&amp;)</li>
 * <li>positive/value-of (+)</li>
 * <li>negation (-)</li>
 * <li>logical not (!)</li>
 * <li>bitwise not (~)</li>
 * </ul>
 * .. plus the ternary conditional assignment operator (? :)
 */
public class Eb_Operator extends CppExpressionBuilder
    implements ParserConstants {

    /**
     * Error message indicating that an unrecognized or otherwise invalid
     * operator was provided as an argument.
     */
    public static final String INVALID_OPERATOR =
        "{0} is an invalid operator";
    /**
     * Error message indicating that an invalid # of operands was provided
     * for the requested operation.
     */
    public static final String INVALID_OPERAND_COUNT =
        "{0} does not take {1,number} operands";


    Hashtable unaryOperators, binaryOperators;

    CodeGenRule rs_tca; // ruleset for ternary conditional assignment operator

    // ruleset for operator overloading
    CodeGenRule rn_overloaded_operator;


    /**
     * Creates a new <code>Eb_BuiltInOperator</code> instance.
     *
     * @param ruleBase a reference to the set of all <code>CodeGenRules</code>
     * @param symbolTable the compile time symbol table to refer to for
     * declarations.
     */
    public Eb_Operator (RuleBase ruleBase, CTSymbolTable symbolTable) {
        super (ruleBase, symbolTable, "operator");
    }

    /**
     * Generates an operator expression, performing conversions
     * and applying other rules as appropriate given the operator and
     * operands provided.
     * <p>An apology will be generated if the arguments do not match a set
     * of rules to apply (indicating that no operator expression
     * exists for the arguments).
     * @param exp will contain the resulting operator expression,
     * with all required conversions and any other transformational rules
     * represented by AST nodes.
     */
    public void apply (ExpressionPtr exp) {
        d.msg (Debug.COMPILE, "*****apply for operator " + exp.op().getName () + " and " +
               exp.operandCount () + " operands: ");
        for (int i = 0; i < exp.operandCount (); i++) {
            ExpressionNode e = exp.get (i);
            d.msg (Debug.COMPILE, e.name () + " (" + e.get_type().getTypeString () + ")");
        }

        CodeGenRule ruleset = null;
        switch (exp.operandCount ()) {
        case 1:
            // unary expression
            ruleset = findRuleset (exp, exp.op (), unaryOperators);
            break;
        case 2:
            // binary expression
            ruleset = findRuleset (exp, exp.op (), binaryOperators);
            break;
        case 3:
            // ternary expression
            ruleset = rs_tca;
            break;
        default:
            // invalid operands
            Assert.apology (INVALID_OPERAND_COUNT, new Object []
                {exp.op (), new Integer (exp.operandCount ())});
        }

        // apply the rules
        ruleset.apply (exp);
    }

    private void addOverloadedBinaryOperatorRules (OperandTable lOperands) {
        OperandTable rOperands = new OperandTable ();
        rOperands.put (TypeNode.class, rn_overloaded_operator);
        lOperands.put (TyClass.class, rOperands);
        rOperands = new OperandTable ();
        rOperands.put (TyClass.class, rn_overloaded_operator);
        lOperands.put (TypeNode.class, rOperands);
    }

    protected void buildTables () {
        unaryOperators = new Hashtable ();
        binaryOperators = new Hashtable ();


        rn_overloaded_operator = rb.get (R_OVERLOADED_OPERATOR);

        String [] unaryArith, binaryArith, pointerArith, binaryLogical;
        unaryArith = new String []
        { OpTable.get (MINUS), OpTable.get (PLUS), OpTable.get (BANG), OpTable.get (TILDE) };
        binaryArith = new String []
        { null, null, null, null, OpTable.get (STAR), OpTable.get (SLASH),
          OpTable.get (PERCENT), OpTable.get (PLUS), OpTable.get (MINUS),
          OpTable.get (SHIFTLEFT), OpTable.get (SHIFTRIGHT),
          OpTable.get (SHIFTRIGHT), OpTable.get (LESSTHAN),
          OpTable.get (GREATERTHAN), OpTable.get (LESSTHANOREQUALTO),
          OpTable.get (GREATERTHANOREQUALTO), OpTable.get (EQUAL),
          OpTable.get (NOTEQUAL), OpTable.get (AMPERSAND),
          OpTable.get (BITWISEXOR), OpTable.get (BITWISEOR),
          null, null, null, null, null,
          /* shorthand assignment begins */
          null, null, null, null,
          OpTable.get (STARASSIGN), OpTable.get (SLASHASSIGN),
          OpTable.get (PERCENTASSIGN), OpTable.get (PLUSASSIGN),
          OpTable.get (MINUSASSIGN),
          OpTable.get (SHIFTLEFTASSIGN), OpTable.get (SHIFTRIGHTASSIGN),
          OpTable.get (SHIFTRIGHTASSIGN), null, null, null, null, null, null,
          OpTable.get (BITWISEANDASSIGN),
          OpTable.get (BITWISEXORASSIGN), OpTable.get (BITWISEORASSIGN),
        };
        pointerArith = new String []
        { null, null, null, null, null, null, null, null, null, null, null, null,
          OpTable.get (LESSTHAN), OpTable.get (GREATERTHAN),
          OpTable.get (LESSTHANOREQUALTO), OpTable.get (GREATERTHANOREQUALTO),
          OpTable.get (EQUAL), OpTable.get (NOTEQUAL), null, null, null,
          OpTable.get (PLUS), OpTable.get (MINUS), OpTable.get (MINUS),
          null, null,
          /* shorthand assignment begins */
          null, null, null, null, null, null, null, null, null, null, null, null,
          null, null, null, null, null, null, null, null, null,
          OpTable.get (PLUSASSIGN), OpTable.get (MINUSASSIGN),
          OpTable.get (MINUSASSIGN)
        };

        binaryLogical = new String []
            {null, null, null, null, null, null, null, null, null, null,
             null, null, null, null, null, null, null, null, null, null,
             null, null, null, null, OpTable.get (AND), OpTable.get (OR)
            };

        rb.put (R_BINARY_INTEGRAL_OP,
                new CGRBinaryIntegralOp (binaryArith, TyBool.get()));

        rb.put (R_BINARY_FLOATING_OP,
                new CGRBinaryFloatingOp (binaryArith, TyBool.get()));

        rb.put (R_UNARY_INTEGRAL_OP,
                new CGRUnaryIntegralOp (unaryArith));

        rb.put (R_UNARY_FLOATING_OP,
                new CGRUnaryFloatingOp (unaryArith));

        rb.put (R_UNARY_PTR_PLUS_OP,
                new CGRParentheses (OpTable.get (PLUS), ""));

        rb.put (R_POINTER_OP,
                new CGRBinaryPointerOp (pointerArith, TyBool.get()));

        rb.put (R_POINTER_POINTER_SUBTRACTION ,
                new CGRPointerPointerSubtract (pointerArith));

        rb.put (R_BINARY_LOGICAL_OP,
                new CGRBinaryLogicalOp (binaryLogical,
                                           TyBool.get ()));

        OperandTable lOperands, rOperands;

        // this node indicates that the rule sequence is not implemented,
        // not necessarily that the required expression nodes are not available
        CodeGenRule rn_unimplemented = rb.get (R_UNIMPLEMENTED_RULE);



        // pointer to member
        // ->*
        // rules:
        // 1. roperand is pointer to member of T
        // 2. loperand is of class T or derived from T
        // 3 ...
        lOperands = new OperandTable ();
        rOperands = new OperandTable ();
        rOperands.put (TyPointer.class, rn_unimplemented);
        lOperands.put (TyClass.class, rOperands);
        lOperands.put (TyPointer.class, rOperands);
        addOverloadedBinaryOperatorRules (lOperands);
        binaryOperators.put (OpTable.get (ARROWSTAR), lOperands);

        // .*
        lOperands = new OperandTable ();
        rOperands = new OperandTable ();
        rOperands.put (TyPointer.class, rn_unimplemented);
        lOperands.put (TyClass.class, rOperands);
        lOperands.put (TyPointer.class, rOperands);
        binaryOperators.put (OpTable.get (DOTSTAR), lOperands);

        // multiplicative

        // rules are:
        // 1. fetch
        // 2. arithmetic conversion
        // 3. build expression

        // terminal fetch is a common pattern
        CodeGenRule rn_fetch_terminal = rb.get (R_FETCH);

        // steps 1 and 2 are a standard arithmetic sequence
        CodeGenRule rn_std_arithmetic =
            new CGROperandNode
                (rb.get (R_ARITHMETIC_CONVERSION), rn_fetch_terminal);


        CodeGenRule r_binary_integral_op = rb.get (R_BINARY_INTEGRAL_OP);
        CodeGenRule r_binary_floating_op = rb.get (R_BINARY_FLOATING_OP);


        CodeGenRule rn_integral_arithmetic =
            new CGRSequentialNode
                (r_binary_integral_op, rn_std_arithmetic);

        CodeGenRule rn_floating_arithmetic =
            new CGRSequentialNode
                (r_binary_floating_op, rn_std_arithmetic);

        // *, /
        lOperands = new OperandTable ();
        rOperands = new OperandTable ();
        rOperands.put (TyClass.class, rn_overloaded_operator);
        rOperands.put (TyIntegral.class, rn_integral_arithmetic);
        rOperands.put (TyFloating.class, rn_floating_arithmetic);
        lOperands.put (TyIntegral.class, rOperands);
        rOperands = new OperandTable ();
        rOperands.put (TyArithmetic.class, rn_floating_arithmetic);
        rOperands.put (TyClass.class, rn_overloaded_operator);
        lOperands.put (TyFloating.class, rOperands);
        addOverloadedBinaryOperatorRules (lOperands);
        binaryOperators.put (OpTable.get (STAR), lOperands);
        binaryOperators.put (OpTable.get (SLASH), lOperands);

        // %
        lOperands = new OperandTable ();
        rOperands = new OperandTable ();
        rOperands.put (TyClass.class, rn_overloaded_operator);
        rOperands.put (TyIntegral.class, rn_integral_arithmetic);
        lOperands.put (TyIntegral.class, rOperands);
        addOverloadedBinaryOperatorRules (lOperands);
        binaryOperators.put (OpTable.get (PERCENT), lOperands);

        // additive
        // rules are:
        // 1. if operand is pointer, validate that type is complete
        // 2. fetch
        // 3. if both operands are arithmetic, arithmetic conversion
        // 4. build expression
        // ?? if one operand is integral, do we perform integral promotion anyway?

        // +
        // both operands arithmetic

        CodeGenRule r_pointer_operation = rb.get (R_POINTER_OP);
        // one operand a pointer
        CodeGenRule rn_ptr_operand_arithmetic =
            rb.get (RN_PTR_OPERAND_FETCH);

        // left operand a pointer
        CodeGenRule rn_lptr_additive = new RNLptrAdditive (rb);

        // right operand a pointer
        CodeGenRule rn_rptr_additive = new RNRptrAdditive (rb);

        lOperands = new OperandTable ();

        rOperands = new OperandTable ();
        rOperands.put (TyClass.class, rn_overloaded_operator);
        rOperands.put (TyArithmetic.class, rn_floating_arithmetic);
        lOperands.put (TyFloating.class, rOperands);

        rOperands = new OperandTable ();
        rOperands.put (TyClass.class, rn_overloaded_operator);
        rOperands.put (TyIntegral.class, rn_lptr_additive);
        lOperands.put (TyPointer.class, rOperands);
        lOperands.put (TyArray.class, rOperands);

        rOperands = new OperandTable ();
        rOperands.put (TyClass.class, rn_overloaded_operator);
        rOperands.put (TyIntegral.class, rn_integral_arithmetic);
        rOperands.put (TyFloating.class, rn_floating_arithmetic);
        rOperands.put (TyPointer.class, rn_rptr_additive);
        rOperands.put (TyArray.class, rn_rptr_additive);
        lOperands.put (TyIntegral.class, rOperands);

        addOverloadedBinaryOperatorRules (lOperands);
        binaryOperators.put (OpTable.get (PLUS), lOperands);

        // -
        // both operands pointers
        CodeGenRule rn_ptr_subtraction = new RNPtrSubtraction (rb);

        lOperands = new OperandTable ();
        rOperands = new OperandTable ();
        rOperands.put (TyClass.class, rn_overloaded_operator);
        rOperands.put (TyArithmetic.class, rn_floating_arithmetic);
        lOperands.put (TyFloating.class, rOperands);

        rOperands = new OperandTable ();
        rOperands.put (TyClass.class, rn_overloaded_operator);
        rOperands.put (TyIntegral.class, rn_integral_arithmetic);
        rOperands.put (TyFloating.class, rn_floating_arithmetic);
        lOperands.put (TyIntegral.class, rOperands);

        rOperands = new OperandTable ();
        rOperands.put (TyClass.class, rn_overloaded_operator);
        rOperands.put (TyIntegral.class, rn_lptr_additive);
        rOperands.put (TyPointer.class, rn_ptr_subtraction);
        rOperands.put (TyArray.class, rn_ptr_subtraction);
        lOperands.put (TyPointer.class, rOperands);
        lOperands.put (TyArray.class, rOperands);

        addOverloadedBinaryOperatorRules (lOperands);
        binaryOperators.put (OpTable.get (MINUS), lOperands);

        // shift operators
        // <<, >>
        // rules
        // 1. fetch
        // 2. perform integral promotions
        // 3. create expression (type is type of left operand)
        CodeGenRule rn_shift =
            new CGROperandNode
                (r_binary_integral_op,
                 new CGRSequentialNode
                     (rb.get (R_INTEGRAL_PROMOTION), rn_fetch_terminal));

        lOperands = new OperandTable ();
        rOperands = new OperandTable ();
        rOperands.put (TyClass.class, rn_overloaded_operator);
        rOperands.put (TyIntegral.class, rn_shift);
        lOperands.put (TyIntegral.class, rOperands);
        addOverloadedBinaryOperatorRules (lOperands);
        binaryOperators.put (OpTable.get (SHIFTLEFT), lOperands);
        binaryOperators.put (OpTable.get (SHIFTRIGHT), lOperands);

        // relational
        // <, >, <=, >=
        // rules
        // 1. fetch
        // 2. arithmetic conversions if operands are arithmetic or enum
        // 3. pointer conversions and qualification conversions if operands
        //    are pointers, to bring them to composite pointer type
        //CodeGenRule r_relational_expression = rb.get (R_RELATIONAL_EXPRESSION);
        CodeGenRule rn_floating_relational =
            new CGRSequentialNode
                (r_binary_floating_op, rn_std_arithmetic);
        CodeGenRule rn_integral_relational =
            new CGRSequentialNode
                (r_binary_integral_op, rn_std_arithmetic);

        CodeGenRule rn_composite_ptr_conversion =
            new CGROperandNode
                (rb.get (R_COMPOSITE_PTR_CONVERSION),
                 rn_ptr_operand_arithmetic); // Modified TSN 2002 Aug 08. Was rn_fetch_terminal
        CodeGenRule rn_pointer_relational =
            new CGRSequentialNode
                (r_pointer_operation, rn_composite_ptr_conversion);

        lOperands = new OperandTable ();
        rOperands = new OperandTable ();
        rOperands.put (TyClass.class, rn_overloaded_operator);
        rOperands.put (TyArithmetic.class, rn_floating_relational);
        lOperands.put (TyFloating.class, rOperands);

        rOperands = new OperandTable ();
        rOperands.put (TyClass.class, rn_overloaded_operator);
        rOperands.put (TyFloating.class, rn_floating_relational);
        rOperands.put (TyIntegral.class, rn_integral_relational);
        rOperands.put (TyPointer.class, rn_pointer_relational); // null pointer
        rOperands.put (TyArray.class, rn_pointer_relational);
        lOperands.put (TyIntegral.class, rOperands);

        rOperands = new OperandTable ();
        rOperands.put (TyClass.class, rn_overloaded_operator);
        rOperands.put (TyPointer.class, rn_pointer_relational);
        rOperands.put (TyArray.class, rn_pointer_relational);
        rOperands.put (TyIntegral.class, rn_pointer_relational); // null pointer
        lOperands.put (TyPointer.class, rOperands);
        lOperands.put (TyArray.class, rOperands);

        addOverloadedBinaryOperatorRules (lOperands);
        binaryOperators.put (OpTable.get (LESSTHAN), lOperands);
        binaryOperators.put (OpTable.get (GREATERTHAN), lOperands);
        binaryOperators.put (OpTable.get (LESSTHANOREQUALTO), lOperands);
        binaryOperators.put (OpTable.get (GREATERTHANOREQUALTO), lOperands);

        // equality
        // ==, !=
        // ** 5.10.2 -- equality comparisons apply to pointer-to-member
        //    operands also - but are we distinguishing ?
        // TBD. Does the next line make sense?
        addOverloadedBinaryOperatorRules (lOperands);
        binaryOperators.put (OpTable.get (EQUAL), lOperands);
        binaryOperators.put (OpTable.get (NOTEQUAL), lOperands);

        // bitwise operators
        // &, |, ^
        CodeGenRule rn_bitwise =
            new CGRSequentialNode
                (r_binary_integral_op, rn_std_arithmetic);
        lOperands = new OperandTable ();
        rOperands = new OperandTable ();
        rOperands.put (TyClass.class, rn_overloaded_operator);
        rOperands.put (TyIntegral.class, rn_bitwise);
        lOperands.put (TyIntegral.class, rOperands);
        addOverloadedBinaryOperatorRules (lOperands);
        binaryOperators.put (OpTable.get (AMPERSAND), lOperands);
        binaryOperators.put (OpTable.get (BITWISEOR), lOperands);
        binaryOperators.put (OpTable.get (BITWISEXOR), lOperands);

        // logical
        // &&, ||
        CodeGenRule rn_logical_operand =
            new CGRSequentialNode
                (rb.get (R_BOOLEAN_CONVERSION), rn_fetch_terminal);
        CodeGenRule rn_logical =
            new CGROperandNode
                (rb.get (R_BINARY_LOGICAL_OP), rn_logical_operand);


        lOperands = new OperandTable ();
        rOperands = new OperandTable ();
        rOperands.put (TyClass.class, rn_overloaded_operator);
        rOperands.put (TyPointer.class, rn_logical);
        rOperands.put (TyArray.class, rn_logical);
        rOperands.put (TyArithmetic.class, rn_logical);
        lOperands.put (TyArithmetic.class, rOperands);
        lOperands.put (TyPointer.class, rOperands);
        lOperands.put (TyArray.class, rOperands);
        addOverloadedBinaryOperatorRules (lOperands);
        binaryOperators.put (OpTable.get (AND), lOperands);
        binaryOperators.put (OpTable.get (OR), lOperands);

        // assignment
        // general rules
        // 1. left operand modifiable lvalue
        // 2. fetch roperand
        // 3. if loperand is a reference, assignment applied to object referred to
        //   -- the fetch will have already occurred, but this may impact how the
        //      assignment expression node is constructed.
        // 4. standard conversion sequence on roperand if loperand is not a class
        //  (copy assignment operator if loperand is a class)
        // 6. build assignment expression ** nonstandard, fetch loperand

        rb.put (R_SHORTHAND_ASSIGNMENT_EXPRESSION,
                new CGRUnimplemented (R_SHORTHAND_ASSIGNMENT_EXPRESSION));


        // =
        CodeGenRule rn_std_assignment = rb.get (R_ASSIGNMENT_EXPRESSION);

        lOperands = new OperandTable ();
        rOperands = new OperandTable ();
        rOperands.put (TypeNode.class, rn_overloaded_operator);
        lOperands.put (TyClass.class, rOperands);
        rOperands = new OperandTable ();
        rOperands.put (TypeNode.class, rn_std_assignment);
        lOperands.put (TypeNode.class, rOperands);

        binaryOperators.put (OpTable.get (ASSIGN), lOperands);

        CodeGenRule rn_op_assignment = new CGRSequentialNode
            (rn_std_assignment, new CGROpOpAssignGlue ());

        // *=, /=
        CodeGenRule rn_ia_assign = new CGRSequentialNode
            (rn_op_assignment, rn_integral_arithmetic);
        CodeGenRule rn_fa_assign = new CGRSequentialNode
            (rn_op_assignment, rn_floating_arithmetic);
        lOperands = new OperandTable ();
        rOperands = new OperandTable ();
        rOperands.put (TyClass.class, rn_overloaded_operator);
        rOperands.put (TyIntegral.class, rn_ia_assign);
        rOperands.put (TyFloating.class, rn_fa_assign);
        lOperands.put (TyIntegral.class, rOperands);
        rOperands = new OperandTable ();
        rOperands.put (TyArithmetic.class, rn_fa_assign);
        rOperands.put (TyClass.class, rn_overloaded_operator);
        lOperands.put (TyFloating.class, rOperands);
        addOverloadedBinaryOperatorRules (lOperands);

        binaryOperators.put (OpTable.get (STARASSIGN), lOperands);
        binaryOperators.put (OpTable.get (SLASHASSIGN), lOperands);

        // %=
        lOperands = new OperandTable ();
        rOperands = new OperandTable ();
        rOperands.put (TyClass.class, rn_overloaded_operator);
        rOperands.put (TyIntegral.class, rn_ia_assign);
        lOperands.put (TyIntegral.class, rOperands);
        addOverloadedBinaryOperatorRules (lOperands);

        binaryOperators.put (OpTable.get (PERCENTASSIGN), lOperands);


        // +=
        CodeGenRule rn_lpa_assign =
            new CGRSequentialNode (rn_op_assignment, rn_lptr_additive);
        CodeGenRule rn_rpa_assign =
            new CGRSequentialNode (rn_op_assignment, rn_rptr_additive);
        lOperands = new OperandTable ();
        rOperands = new OperandTable ();
        rOperands.put (TyClass.class, rn_overloaded_operator);
        rOperands.put (TyArithmetic.class, rn_fa_assign);
        lOperands.put (TyFloating.class, rOperands);
        rOperands = new OperandTable ();
        rOperands.put (TyClass.class, rn_overloaded_operator);
        rOperands.put (TyIntegral.class, rn_lpa_assign);
        lOperands.put (TyPointer.class, rOperands);
        //		lOperands.put (TyJavaArray.class, rOperands);
        rOperands = new OperandTable ();
        rOperands.put (TyClass.class, rn_overloaded_operator);
        rOperands.put (TyIntegral.class, rn_ia_assign);
        rOperands.put (TyFloating.class, rn_fa_assign);
        rOperands.put (TyPointer.class, rn_rpa_assign);
        //		rOperands.put (TyJavaArray.class, topRule);
        lOperands.put (TyIntegral.class, rOperands);
        addOverloadedBinaryOperatorRules (lOperands);

        binaryOperators.put (OpTable.get (PLUSASSIGN), lOperands);

        // -=
        CodeGenRule rn_ps_assign =
            new CGRSequentialNode (rn_op_assignment, rn_ptr_subtraction);

        lOperands = new OperandTable ();
        rOperands = new OperandTable ();
        rOperands.put (TyClass.class, rn_overloaded_operator);
        rOperands.put (TyArithmetic.class, rn_fa_assign);
        lOperands.put (TyFloating.class, rOperands);
        rOperands = new OperandTable ();
        rOperands.put (TyClass.class, rn_overloaded_operator);
        rOperands.put (TyIntegral.class, rn_ia_assign);
        rOperands.put (TyFloating.class, rn_fa_assign);
        lOperands.put (TyIntegral.class, rOperands);
        rOperands = new OperandTable ();
        rOperands.put (TyClass.class, rn_overloaded_operator);
        rOperands.put (TyIntegral.class, rn_lpa_assign);
        rOperands.put (TyPointer.class, rn_ps_assign);
        lOperands.put (TyPointer.class, rOperands);
        addOverloadedBinaryOperatorRules (lOperands);
        binaryOperators.put (OpTable.get (MINUSASSIGN), lOperands);

        // <<=, >>=
        CodeGenRule rn_shift_assign =
            new CGRSequentialNode (rn_op_assignment, rn_shift);
        rOperands.put (TyClass.class, rn_overloaded_operator);
        rOperands.put (TyIntegral.class, rn_shift_assign);
        lOperands.put (TyIntegral.class, rOperands);
        addOverloadedBinaryOperatorRules (lOperands);
        binaryOperators.put (OpTable.get (SHIFTLEFTASSIGN), lOperands);
        binaryOperators.put (OpTable.get (SHIFTRIGHTASSIGN), lOperands);

        // &=, |=, ^=
        CodeGenRule rn_bitwise_assign =
            new CGRSequentialNode (rn_op_assignment, rn_bitwise);
        lOperands = new OperandTable ();
        rOperands = new OperandTable ();
        rOperands.put (TyClass.class, rn_overloaded_operator);
        rOperands.put (TyIntegral.class, rn_bitwise_assign);
        lOperands.put (TyIntegral.class, rOperands);
        addOverloadedBinaryOperatorRules (lOperands);
        binaryOperators.put (OpTable.get (BITWISEANDASSIGN), lOperands);
        binaryOperators.put (OpTable.get (BITWISEORASSIGN), lOperands);
        binaryOperators.put (OpTable.get (BITWISEXORASSIGN), lOperands);

        // comma
        // ,
        lOperands = new OperandTable ();
        rOperands = new OperandTable ();
        rOperands.put (TypeNode.class, rn_unimplemented);
        lOperands.put (TypeNode.class, rOperands);
        addOverloadedBinaryOperatorRules (lOperands);
        binaryOperators.put (OpTable.get (COMMA), lOperands);


        // 5.2.1 subscripting : postfix-expression [expression]
        rb.put (R_SUBSCRIPT, new CGRSubscript ("[", "]"));
        // 1. fetch operands
        // ** note --- OpArraySubscript requires unfetched lvalue as
        //    array operand, so proceeding differently here.
        // 2. one operand should be a pointer, the other integral or enum
        // 3. the pointer should be to a completely defined type
        // 4. build the expression
        CodeGenRule rs_array_prep =
            new CGRSequentialNode
            (rb.get (R_CONVERT_TO_REFERENCE),
             rb.get (R_COMPLETE_TYPE));
        CodeGenRule rn_subscript_array =
            new CGROperandBranchNode
                (rb.get (R_SUBSCRIPT),
                 new CodeGenRule [] {rs_array_prep,
                                     rb.get (R_FETCH)});

        CodeGenRule rn_subscript_array_right =
            new CGRSequentialNode
                (rn_subscript_array,
                 new CGRSwapOperands (0,1));

        CodeGenRule rn_subscript_ptr =
            new CGROperandBranchNode
                (rb.get (R_SUBSCRIPT),
                 new CodeGenRule [] {rb.get (RN_PTR_OPERAND_FETCH),
                                     rb.get (R_FETCH)});

        CodeGenRule rn_subscript_ptr_right =
            new CGRSequentialNode
                (rn_subscript_ptr,
                 new CGRSwapOperands (0,1));

        lOperands = new OperandTable ();
        rOperands = new OperandTable ();
        rOperands.put (TyIntegral.class, rn_subscript_ptr);
        lOperands.put (TyPointer.class, rOperands);
        rOperands = new OperandTable ();
        rOperands.put (TyIntegral.class, rn_subscript_array);
        lOperands.put (TyArray.class, rOperands);
        rOperands = new OperandTable ();
        rOperands.put (TyPointer.class, rn_subscript_ptr_right);
        rOperands.put (TyArray.class, rn_subscript_array_right);
        lOperands.put (TyIntegral.class, rOperands);
        binaryOperators.put (OpTable.get (OPEN_BRACKET), lOperands);


        // 5.3.1 *** unary operators ***
        OperandTable operands;

        // * : indirection
        // applies to pointer
        // 1. fetch operand
        // 2. generate expression
        CodeGenRule rn_indirection =
            new CGROperandNode (rb.get (R_DEREFERENCE), rn_fetch_terminal);
        operands = new OperandTable ();
        operands.put (TyPointer.class, rn_indirection);
        operands.put (TyArray.class, rn_indirection);
        operands.put (TyClass.class, rn_overloaded_operator);
        unaryOperators.put (OpTable.get (STAR), operands);

        // & : address of
        // 1.  validate that operand is an lvalue
        // 2a. if id is qualified, and refers to static member,
        //     disregard cv-qualifiers
        // 2b. if the id is qualified, and refers to a non-static member,
        //     determine point of definition in class/ns hierarchy.
        //     (if we are supporting pointer-to-member distinction)
        // 2c. if the id is unqualified (including qualified id in parenths),
        //     pointer will take cv-qualification
        // 3.  generate the expression to create the pointer of the relevant
        //     type
        // ?? note : right now we are passing everything to OpAddressOf as if
        //    case 2c applied.


        CodeGenRule rn_address_of =
            new CGROperandNode
                (rb.get (R_ADDRESS_OF), rb.get (R_LVALUE));
        operands = new OperandTable ();
        operands.put (TypeNode.class, rn_address_of);
        unaryOperators.put (OpTable.get (AMPERSAND), operands);


        CodeGenRule rn_unary_integral =
            new CGROperandNode
                (rb.get (R_UNARY_INTEGRAL_OP),
                 new CGRSequentialNode (rb.get (R_INTEGRAL_PROMOTION),
                                          rn_fetch_terminal));
        CodeGenRule rn_unary_floating =
            new CGROperandNode
                (rb.get (R_UNARY_FLOATING_OP), rn_fetch_terminal);

        CodeGenRule rn_unary_ptr_plus =
            new CGROperandNode (rb.get (R_UNARY_PTR_PLUS_OP),
                                     rn_fetch_terminal);

        // + : positive / value-of
        // 1. fetch operand
        // 2. perform integral promotion if integral
        // 3. generate expression
        operands = new OperandTable ();
        operands.put (TyPointer.class, rn_unary_ptr_plus);
        operands.put (TyArray.class, rn_unary_ptr_plus);
        operands.put (TyFloating.class, rn_unary_floating);
        operands.put (TyIntegral.class, rn_unary_integral);
        operands.put (TyClass.class, rn_overloaded_operator);
        unaryOperators.put (OpTable.get (PLUS), operands);

        // - : negation
        // 1. fetch operand
        // 2. perform integral promotion if integral
        // 3. generate the expression
        operands = new OperandTable ();
        operands.put (TyFloating.class, rn_unary_floating);
        operands.put (TyIntegral.class, rn_unary_integral);
        operands.put (TyClass.class, rn_overloaded_operator);
        unaryOperators.put (OpTable.get (MINUS), operands);

        // ! : logical not
        // 1. fetch operand
        // 2. convert to boolean
        // 3. generate the expression
        CodeGenRule rn_unary_logical =
            new CGROperandNode
            (rb.get (R_UNARY_INTEGRAL_OP), rn_logical_operand);
        operands = new OperandTable ();
        operands.put (TypeNode.class, rn_unary_logical);
        unaryOperators.put (OpTable.get (BANG), operands);

        // ~ : bitwise not
        // 1. fetch operand
        // 2. perform integral promotions
        // 3. generate the expression
        operands = new OperandTable ();
        operands.put (TyIntegral.class, rn_unary_integral);
        operands.put (TyClass.class, rn_overloaded_operator);
        unaryOperators.put (OpTable.get (TILDE), operands);

        // 5.2.6 : prefix increment/decrement
        // 1. operand must be of arithmetic or pointer type (not boolean for
        //    decrement
        // 2. operand must be modifiable lvalue
        // ** removed 3. fetch operand ** do not fetch operand, OpIncrement et al need the TyRef to assign.
        // 4. build expression

        // ++
        rb.put (R_PREFIX_INCREMENT, new CGRIncrement (true, true));
        CodeGenRule rn_prefix_increment =
            new CGROperandNode (rb.get (R_PREFIX_INCREMENT),
                                  rb.get (R_MODIFIABLE_LVALUE));
        operands = new OperandTable ();
        operands.put (TyArithmetic.class, rn_prefix_increment);
        operands.put (TyPointer.class, rn_prefix_increment);
        unaryOperators.put (OpTable.get (PLUSPLUS), operands);

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
        unaryOperators.put (OpTable.get (MINUSMINUS), operands);


        // 5.16 *** ternary conditional operator (? :) ***
        rb.put (R_TERNARY_CONDITIONAL_EXPRESSION,
                new CGRUnimplemented (R_TERNARY_CONDITIONAL_EXPRESSION));
        // first operand is converted to bool (rn_logical_operand)

        // second and third operands undergo a barrage of comparisons and
        // possible conversions.
        // ** current implementation skips all evaluation involving these
        // operands
        rs_tca = new CGROperandBranchNode
            (rb.get (R_TERNARY_CONDITIONAL_EXPRESSION),
             new CodeGenRule []
                {rn_logical_operand, rn_fetch_terminal, rn_fetch_terminal});
    }


    public static final String RN_LPTR_ADDITIVE =
        "left operand a pointer in additive expression";
    public class RNLptrAdditive extends RuleProxy {
        public RNLptrAdditive (RuleBase rb) {
            super (RN_LPTR_ADDITIVE, rb);
        }

        protected CodeGenRule buildRule () {
            return new CGROperandBranchNode
                (ruleBase.get (R_POINTER_OP),
                 new CodeGenRule [] {
                     ruleBase.get (RN_PTR_OPERAND_FETCH),
                     ruleBase.get (R_FETCH)});
        }
    }

    public static final String RN_RPTR_ADDITIVE =
        "right operand a pointer in additive expression";
    public class RNRptrAdditive extends RuleProxy {
        public RNRptrAdditive (RuleBase rb) {
            super (RN_RPTR_ADDITIVE, rb);
        }

        protected CodeGenRule buildRule () {
            return new CGROperandBranchNode
                (ruleBase.get (R_POINTER_OP),
                 new CodeGenRule [] {
                     ruleBase.get (R_FETCH),
                     ruleBase.get (RN_PTR_OPERAND_FETCH)});

        }
    }

    public static final String RN_PTR_SUBTRACTION =
        "subtraction, with two pointer operands";
    private class RNPtrSubtraction extends RuleProxy {
        public RNPtrSubtraction (RuleBase rb) {
            super (RN_PTR_SUBTRACTION, rb);
        }

        protected CodeGenRule buildRule () {
            return new CGROperandNode
                (ruleBase.get (R_POINTER_POINTER_SUBTRACTION),
                 ruleBase.get (RN_PTR_OPERAND_FETCH));
        }
    }

    public class CGROpOpAssignGlue extends CodeGenRule {
        public void apply (ExpressionPtr exp) {
            ExpressionNode loperand = exp.get (0);
            while (!(loperand instanceof ExpId))
                loperand = loperand.child_exp (0);
            exp.set (loperand, 0);
        }
    }

}


