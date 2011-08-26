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

package tm.javaLang.analysis;

import tm.clc.analysis.CGRNode;
import tm.clc.analysis.CodeGenRule;
import tm.clc.analysis.ExpressionPtr;
import tm.clc.ast.ExpressionNode;
import tm.clc.ast.OpArithmeticConversion;
import tm.clc.ast.TypeNode;


/** A rule node implementing shorthand assignment expansion: first a new
 * expressionPtr is created, using the basic operands,then the base rule is applied
 * to it. Finally, the specified conversion rule is applied to the result, using
 * the type of the original operand[0] as the target.
 *
 * */

public class CGROpOpNode extends CGRNode{
    private boolean doConversion;

    /** Create an OpOpNode
     * @param baseRule The rule associated with the base shorthand operand
     * @param finalConversion the conversion rule to be applied after the base rule has been applied
     */    
    public CGROpOpNode(CodeGenRule baseRule, boolean convert){
        super(baseRule);    //The Basic arithmetic operation
        doConversion = convert;
        description = "shorthand assignment";
    }

    /** Modifies the expression passed in by adding the AST node tee for the expanded
     * expression as an extra operand
     * @param exp The original expression containing the compound operation
     */    
     public void apply(ExpressionPtr exp) {
        String fullOp = exp.op().getName ();
        String baseOp = fullOp.substring (0,fullOp.length () - 1);
        
        // Create expressionPtr for basic operation
            ExpressionPtr expanded = new ExpressionPtr(new Java_ScopedName(baseOp), new Object [] {});
            expanded.addOperand(exp.get(0));
            expanded.addOperand(exp.get(1));
        // and apply base rule to it
            applyRule(rule, expanded);
            if (doConversion) { // shorthand = notation does implicit casts
                TypeNode toType =  exp.get_base_type (0);
                TypeNode fromType =  expanded.get_type ();
                if (!toType.equal_types(fromType)) {
                    ExpressionNode convert = new OpArithmeticConversion(toType, "",expanded.get());
                    exp.set(convert);
                    return;
                }
            }
       // plug result in as extra operand on original expression
           exp.set(expanded.get(1));

    }

}



