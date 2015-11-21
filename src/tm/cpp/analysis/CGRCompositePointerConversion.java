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

import tm.clc.analysis.CodeGenRule;
import tm.clc.analysis.ExpressionPtr;
import tm.clc.ast.ExpressionNode;
import tm.clc.ast.TypeNode;
import tm.cpp.ast.TyPointer;
import tm.utilities.Assert;


/**
 * Implements composite pointer conversion rules, requires two operands
 * in first and second position, result is conversion of one or both
 * operands according to the rules, replacing (enclosing) the existing 
 * operands. 
 */
public class CGRCompositePointerConversion extends CodeGenRule {
    private StandardConversions sc = StandardConversions.getInstance ();

    
    public void apply (ExpressionPtr exp) { 
        
        ExpressionNode opa = exp.get (0);
        ExpressionNode opb = exp.get (1);
        TypeNode opat = opa.get_type ();
        TypeNode opbt = opb.get_type ();
        int cvqa = opat.getAttributes ();
        int cvqb = opbt.getAttributes ();

        // 2 pointer operands, (null pointer constant counts - but the case 
        // of two null pointer constant operands is handled by standard
        // arithmetic rules).
        // ?? should we generate an error if these conditions don't hold?
        if (opa.is_integral_constant ()) 
            opa = sc.makeNullPointerConstantConversion 
                (opa, sc.copyType (((TyPointer) opbt), cvqa));
        else if (opb.is_integral_constant ())
            opb = sc.makeNullPointerConstantConversion 
                (opb, sc.copyType (((TyPointer) opat), cvqb));

        // now perform :
        // 1. (possibly) ptr to anything --> ptr to void
        // 2. union of cv-qualification assigned to both operand types
        // .. both of these operations are handled by the 
        // StandardConversions.makeCompositePointerConversion method
        opa = sc.makeCompositePointerConversion (opa, opb);
        opb = sc.makeCompositePointerConversion (opb, opa);

        // results replace the respective operand values
        exp.set (opa, 0);
        exp.set (opb, 1);
    }
}
