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

import tm.clc.ast.ExpressionNode;
import tm.utilities.Assert;

/**
 * Performs type conversion of specified operand x to type of specified operand y
 */
public class CGRTypeConversion extends CodeGenRule {

    /**
     * Error message displayed when the operands provided are missing one or 
     * both of the positions required for the type conversion of operand[i]
     * to the type of operand[j], where (i,j) are indices provided in the
     * constructor.
     */
    public static final String INVALID_OPERAND_LIST = 
        "Cannot convert operand {0,number} to the type of operand {1,number} " +
        "from a set of {2,number} operands";
    private ConversionRules cr;

    /**
     * Creates a new <code>CGRTypeConversion</code> instance.
     *
     * @param rules the rules that will be applied to perform the conversion
     * @param fidx the index of the operand to convert (operands in l-r order)
     * @param tidx the index of the operand whose type is the target type
     * for the conversion (operands in l-r order)
     */
    public CGRTypeConversion (ConversionRules rules, int fidx, int tidx) {
        this (rules, fidx, tidx, true, true);
    }
    public CGRTypeConversion (ConversionRules rules, int fidx, int tidx, 
                                boolean showop, boolean interesting) {
        super (fidx, tidx);
        cr = rules;
        this.show_op = showop;
        this.interesting = interesting;
    }

    /**
     * Generates the AST representation of the conversion of one operand to  
     * the type of another operand. 
     * @param exp ignored
     * of the converted operand will replace (actually wrap around) the 
     * expression currently at that position in the array.
     */
    public void apply (ExpressionPtr exp) { 
        if (exp.operandCount () < start || exp.operandCount () < end) {
            Assert.apology (INVALID_OPERAND_LIST, new Object [] 
                            {new Integer (start), new Integer (end), 
                             new Integer (exp.operandCount ())});
        }
        ExpressionNode converted;
        ExpressionNode from = exp.get (start);
        converted = cr.makeConversionExpression 
            (from, exp.get_type (end), 
             ((show_op) ? exp.op().getName () : ""));

        // Next line commented out be TSN 05, Aug, 2002.
        //this.updateSequenceProperties (from, converted); 

        exp.set (converted, start); 
    }

}






