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

import tm.utilities.Assert;
import tm.utilities.Debug;

/**
 * Represents a rule used by the parser front end towards the generation 
 * of the AST representation of code. These rules are generally strung together
 * by <code>CGRNode</code>s, to build rule sets that can be applied to 
 * specific categories of expression.
 * <p>Because of the wide variety of rules, and more particularly the variety
 * of ways these rules modify an expression or parts thereof, AST 
 * <code>ExpressionNode</code>s are not used directly as parameters or 
 * return types. Instead the <code>ExpressionPtr</code> is used to permit a
 * greater range of rules to employ the <code>CodeGenRule</code> model.
 * @see ExpressionPtr  
 * @see tm.clc.ast.ExpressionNode  
 */
public abstract class CodeGenRule {

    /** debugging comments this way */
    public static final Debug d = Debug.getInstance ();

    /** the rule cannot be applied against the operand(s) provided */
    public static final String INVALID_OPERAND = 
        "{0} is not a valid operand for {1}";

    /** 
     * A start and end index indicating the operands involved when 
     * applying the rule. These values are interpreted differently 
     * depending on the rule:
     * <ul>
     * <li>only start is used when a rule involves a single operand
     * <li>start and end are used to indicate operands 1 and 2 in a 
     * binary operation
     * <li>start and end are used to specify a range of operands to 
     * use when applying the rule, or on which the rule shall be applied.
     * </ul>
     */
    protected int start = ExpressionPtr._EXP, end = start; 

    /**
     * is the generated expression (if any) interesting ?
     */
    protected boolean interesting = true;

    /**
     * should the generated expression (if any) include the provided operator/id ?
     */
    protected boolean show_op = true;
    
    /** A string to identify the rule */
    public String description ;

    /** creates a new rule instance */
    public CodeGenRule () { }

    /** creates a new instance of the rule, indicating by index either:
     * <ul><li>the entity from which to start when the rule executes, or
     * <li>the single entity to use during rule execution</ul>
     * @param start the index of the entity 
     */
    public CodeGenRule (int start) { this.start = start; }

    /** creates a new instance of the rule, indicating by index either:
     * <ul><li>the range of entities to use when the rule executes, or
     * <li>the two entities to use during rule execution</ul>
     * @param start the index of the first entity 
     * @param end the index of the second entity 
     */
    public CodeGenRule (int start, int end) { 
        this.start = start; 
        this.end = end;
    }
    
    /** 
     * Sets attributes of generated expression nodes after orig_exp and 
     * up to and including new_exp, according to defaults set for this 
     * rule instance.
     * <ul>notes:
     * <li>currently just sets interestingness
     * <li>presupposes that new_exp is at a higher point in an expression 
     *     chain than orig_exp
     * <li>presupposes that the expression chain is a complete ordering
     */
    /*  No longer needed.  05 AUg 2002, TSN
    protected void updateSequenceProperties (ExpressionNode orig_exp, 
                                             ExpressionNode new_exp) {
        ExpressionNode curr_exp = new_exp;
        while (curr_exp != orig_exp) {
            curr_exp.set_uninteresting (!interesting);
            Assert.assert (curr_exp.childCount () == 1); // strict
            curr_exp = curr_exp.child_exp (0);
        }
    }
    */

    /**
     * Usually generates part or all of the AST representation of an expression, 
     * given the raw materials provided in the method arguments.
     * <ul>There are several general categories of <code>apply</code>:
     * <li>using the operands and/or op/fn id, 
     * a new <code>ExpressionNode</code> is created and placed in 
     * <code>exp</code></li>
     * <li>using a partially generated expression, a
     * new <code>ExpressionNode</code> is created and placed in 
     * <code>exp</code></li>
     * <li>using the operands and possibly op/fn id,
     * one or more <code>ExpressionNode</code>s are created and replace one or
     * more of the elements in operands</li>
     * <li>using exp, it is determined whether or
     * not generation should proceed. If not, an <code>Assert.apology</code>
     * is created. Otherwise, the arguments pass through untouched. This is
     * called an <em>assertion</em> rule</li>
     * @param exp contains the (fully or partially) generated expression, or
     * is empty.
     */
    public abstract void apply (ExpressionPtr exp) ;


}

