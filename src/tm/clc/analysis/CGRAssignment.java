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
import tm.clc.ast.OpAssign;
import tm.clc.ast.OpOpAssign;
import tm.utilities.Debug;

/**
 * Generates an assignment expression. Shorthand notation (+=, etc) is 
 * also supported by this rule.
 */
public class CGRAssignment extends CodeGenRule {

    /**
     * Generates an <code>OpAssign</code> expression, given the 
     * <code>operands</code>
     * @param exp containing the operands; will also contain 
     * the resulting expression
     */
    
    public void apply (ExpressionPtr exp) { 
        ExpressionNode assignment;
        ExpressionNode assignment_operator_exp = null;
        ExpressionNode loperand, roperand;
        if (exp.operandCount () >= 2) {
            loperand = exp.get (0);
            roperand = exp.get (1);
            assignment_operator_exp = exp.get ();
        } else {
            loperand = exp.get ();
            roperand = exp.get (0);
        }

        if (assignment_operator_exp == null) {
            assignment = new OpAssign (loperand.get_type (), 
                                        exp.op().getName (),
                                    loperand, roperand);
        } else { // shorthand assignment
          String full_op = exp.op().getName ();
          String assign_op = full_op.substring (full_op.length () - 1);
          assignment = new OpOpAssign (loperand.get_type (), 
                                         full_op, assign_op, 
                                         loperand, roperand,
                                         assignment_operator_exp);
        }

        d.msg (Debug.COMPILE, "assignment expression built as:\n" + assignment.ppToString (3, 80));
        exp.set (assignment);
    }

}






