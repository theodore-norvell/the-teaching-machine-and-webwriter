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


import java.util.Vector;
import java.util.Enumeration;
import java.util.Hashtable;

import tm.clc.ast.TypeNode;
import tm.utilities.Assert;
import tm.utilities.Debug;

/**
 * An <code>ExpressionBuilder</code> is a <code>CodeGenRule</code> that serves
 * as a point of access to a specific range of <em>rulesets</em>. The method(s)
 * by which access is provided will vary according to the rulesets covered.
 * <p>The <code>ExpressionBuilder</code> is also responsible for
 * building the rulesets (collections of <code>CGRNode</code>s) that
 * represent and enforce the rules required to generate a range of expressions
 * Generally, this happens once in the lifetime of the
 * <code>ExpressionBuilder</code>, but no constraint is imposed in this regard.
 * <p>Each <code>ExpressionBuilder</code> has access to the generic repository
 * of <code>CodeGenRules</code> represented by the <code>RuleBase</code>, which
 * it uses to construct rulesets.
 */
public abstract class ExpressionBuilder extends CodeGenRule {

    /**
     * generic error message indicating that operands and operator don't
     * match for this expression builder
     */
    protected static final String INVALID_EXP =
        "{1} is not a recognized {0} expression identifier for the operands";
    /**
     * identifies the category of expression being handled by this
     * expression builder, for use in error messages
     */
    protected String EXP_TYPE;


    // RULE IDs -- these are rulebase keys and identifiers for error messages
    public static final String R_UNIMPLEMENTED_RULE = "unimplemented rule";
    public static final String R_NONE = "no rule";
    public static final String R_FETCH = "generic fetch rule";
    public static final String R_ARITHMETIC_CONVERSION =
        "standard arithmetic conversion";

    public static final String R_RESULT =
        "generate an assignment expression for function return value";

    public static final String R_BINARY_INTEGRAL_OP =
        "generate a binary integral operation expression";
    public static final String R_BINARY_FLOATING_OP =
        "generate a binary floating operation expression";
    public static final String R_UNARY_INTEGRAL_OP =
        "generate a unary integral operation expression";
    public static final String R_UNARY_FLOATING_OP =
        "generate a unary floating operation expression";
    public static final String R_BINARY_LOGICAL_OP =
        "generate a binary logical expression (and, or)";


    public static final String R_MEMBER_EXPRESSION =
        "generate a member access expression";

    public static final String R_PARENTHESES_EXPRESSION =
        "generate a parentheses expression or other do-nothing exp";
    public static final String R_SHIFT_EXPRESSION =
        "generate a shift expression";
    public static final String R_INTEGRAL_PROMOTION = "integral promotion";
    public static final String R_RELATIONAL_EXPRESSION =
        "generate a relational expression";
    public static final String R_BITWISE_EXPRESSION =
        "generate a bitwise expression";
    public static final String R_UNARY_BITWISE_EXPRESSION =
        "generate a unary bitwise expression";
    public static final String R_BOOLEAN_CONVERSION = "boolean conversion";
    public static final String R_UNARY_LOGICAL_EXPRESSION =
        "generate a unary logical expression";
    public static final String R_ASSIGNMENT_EXPRESSION =
        "generate an assignment expression";
    public static final String R_ASSIGNMENT_INITIALIZATION =
        "generate an initialization by assignment";
    public static final String R_SHORTHAND_ASSIGNMENT_EXPRESSION =
        "generate a shorthand assignment expression";

    public static final String R_TERNARY_CONDITIONAL_EXPRESSION =
        "generate a ternary conditional expression";

    public static final String R_SUBSCRIPT =
        "generate a subscripting expression";

    public static final String R_PREFIX_INCREMENT =
        "generate a prefix increment expression";
    public static final String R_POSTFIX_INCREMENT =
        "generate a postfix increment expression";
    public static final String R_PREFIX_DECREMENT =
        "generate a prefix decrement expression";
    public static final String R_POSTFIX_DECREMENT =
        "generate a postfix decrement expression";

    public static final String R_MODIFIABLE_LVALUE =
        "verify that an expression is a modifiable lvalue";
    public static final String R_LVALUE =
        "verify that an expression is an lvalue";
    public static final String R_CONVERT_RVALUE =
        "convert type of rvalue in assignment";

    public static final String R_ID_EXPRESSION =
        "generate an id expression";
    public static final String R_THIS_EXPRESSION =
        "generate a this expression";

    public static final String R_NEW_EXPRESSION =
        "generate a new expression";

    public static final String R_RESOLVE_FN_ID =
        "resolve a function id (using id and overload resolution)";
    public static final String R_FUNCTION_CALL =
        "generate a function/method call expression";
    public static final String R_CONVERT_VALUE_ARGUMENT =
        "convert a value function argument prior to call" ;
    public static final String R_CONVERT_REFERENCE_INITIALIZER =
        "convert a value to a reference prior to initialization" ;
    public static final String R_CONVERT_NO_PARAMETER_ARGUMENT =
        "convert a value function argument where there is no parameter" ;

    public static final String R_VALID_RETURN_TYPE =
        "ensure function return type is valid at time of call";

    public static final String T_NULLEXP = "test for null expression";
    public static final String T_LVALUE_FN = "test for function id expression";
    public static final String T_TYPEID_OPERAND = "test for typeid operand";
    public static final String T_REFERENCE = "test for reference expression";
    public static final String T_ABSTRACT =
        "test for reference or ptr to polymorphic type";
    public static final String T_EQUIVALENT_TYPES =
        "test for equivalent types among all operands";

    /** debugging comments this way */
    public Debug dbg = Debug.getInstance ();

    /** rules broken down by operator / operand */
    protected Hashtable rulesets = new Hashtable ();

    /**
     * The set of <code>CodeGenRule</code>s from which rulesets are built.
     */
    public RuleBase rb;

    /**
     * Creates a new <code>ExpressionBuilder</code> instance.
     * @param expType names the category of expression covered
     */
    public ExpressionBuilder (String expType) { EXP_TYPE = expType; }

    /**
     * Generates an expression, performing conversions
     * and applying other rules as appropriate given the operator and
     * operands provided.
     * <p>An apology will be generated if the arguments do not match a set
     * of rules to apply (indicating that no expression of this type
     * exists for the arguments).
     * @param exp will contain the AST representation of the resulting
     * expression, including any conversions.
     * @param op the operator (if applicable)
     * @param operands contains the operands - the elements in this array
     * may change as a side effect of this method.
     */
    public void apply (ExpressionPtr exp) {
        // find the rules
        CodeGenRule ruleset = findRuleset (exp);
        // apply the rules
        ruleset.apply (exp);
    }

    /**
     * Returns the ruleset to apply given the operator and operands
     * in <code>exp</code>, or <code>null</code> if no matching ruleset
     * can be found. Uses default rule table.
     */
    protected CodeGenRule findRuleset (ExpressionPtr exp) {
        return findRuleset (exp, exp.opcat, rulesets);
    }
    /**
     * Returns the ruleset to apply given the operands in <code>exp</code>
     * and operator <code>op</code>, or <code>null</code> if no matching
     * ruleset can be found. Uses default rule table.
     */
    protected CodeGenRule findRuleset (ExpressionPtr exp, ScopedName op) {
        return findRuleset (exp, op, rulesets);
    }

    /**
     * Returns the ruleset to apply given the operands in <code>exp</code>
     * and operator <code>op</code>, or <code>null</code> if no matching
     * ruleset can be found. Uses provided <code>ruleTable</code>.
     */
    protected CodeGenRule findRuleset (ExpressionPtr exp, ScopedName op,
                                       Hashtable ruleTable) {
        CodeGenRule ruleset = null;

        OperandTable opRules =
            (OperandTable) ruleTable.get (op.getTerminalId ());
        if (opRules != null) {
            for (int i = 0; i < exp.operandCount (); i++) {
                TypeNode type = exp.get_base_type (i);
                Object match = opRules.get (type);
                if (match == null) {
                    // no possible match - break out
                    break;
                } else if (match instanceof CodeGenRule) {
                    // found the ruleset, assign and break out
                    ruleset = (CodeGenRule) match;
                    break;
                } else { // another table, continue along operands
                    opRules = (OperandTable) match;
                }
            }
        }

        if (ruleset == null)
            Assert.error (INVALID_EXP, new String []
                {EXP_TYPE, op.getName ()});

        return ruleset;
    }

    /**
     * Build internal <code>rulesets</code> and add to <code>ruleBase</code>.
     * <br>Also build any rule tables, matching operand types to operators.
     */
    protected abstract void buildTables () ;


    /**
     * Build rules that are common among all expression builders
     */
    protected void buildCommonRules () {
        // used for an unimplemented/undetermined rule sequence
        CodeGenRule rn_unimplemented =
            new CGRUnimplemented ("unimplemented rule sequence");
        rb.put (R_UNIMPLEMENTED_RULE, rn_unimplemented);

        // used for a stubbed rule, may be used to just perform linked rules
        rb.put (R_NONE, new CGRNone ());

        rb.put (R_FETCH, new CGRFetch ());

        rb.put (R_RESULT, new CGRResult ());

        rb.put (T_ABSTRACT, new CGRUnimplemented (T_ABSTRACT));

        rb.put (T_EQUIVALENT_TYPES, new CGREquivalentTypeTest ());

    }

    /**
     * A table used to match operators to operands to rulesets.
     * Each operator will have an <code>OperandTable</code> associated with it.
     * For binary operators, the left operand's type is used as a key,
     * associated with another OperandTable matching right operand types with
     * rulesets to apply (for the particular operator and operand combination).
     * When an operator has no built-in meaning for the type of either or both
     * operands, the first appropriate table lookup will return null.
     */
    public class OperandTable {
        private Vector table = new Vector ();

        /** create a new <code>OperandTable</code> instance */
        public OperandTable () { }

        /**
         * Provide the <code>OperandTable</code> or ruleset matching the
         * provided <code>TypeNode</code>
         */
        public Object get (TypeNode key) {
            Object match = null;
            // traverse the table for the class or most derived base class
            // in the table
            for (Enumeration els = table.elements ();
                 els.hasMoreElements () && match == null; ) {
                OperandTable.Element el =
                    (OperandTable.Element) els.nextElement ();
                if (el.key().isInstance (key)) match = el.value ();
            }
            return match;
        }

        /**
         * Add an element to this table.
         * @param key the class deriving <code>TypeNode</code> to match.
         * this can be a base class, for which derived classes will match
         * unless matched to an element added prior to this.
         * @param value the <code>OperandTable</code> or ruleset to associate
         * with the key.
         */
        public void put (Class key, Object value) {
            table.addElement (new OperandTable.Element (key, value));
        }

        private class Element {
            private Class key;
            private Object value;
            public Element (Class key, Object value) {
                this.key = key;
                this.value = value;
            }
            public Class key () { return key; }
            public Object value () { return value; }
        }
    }

}


