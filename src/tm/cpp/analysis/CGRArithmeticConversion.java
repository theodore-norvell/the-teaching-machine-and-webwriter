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

import tm.clc.analysis.CodeGenRule;
import tm.clc.analysis.ExpressionPtr;
import tm.clc.ast.ExpressionNode;
import tm.cpp.ast.TyArithmetic;
import tm.utilities.Assert;

/**
 * Implements arithmetic conversion rules, requires two operands
 * in first and second position, result is conversion of one or both
 * operands according to the rules, replacing (enclosing) the existing 
 * operands. 
 */
public class CGRArithmeticConversion extends CodeGenRule {
    private StandardConversions sc = StandardConversions.getInstance ();

    public void apply (ExpressionPtr exp) { 
         
        // 2 arithmetic operands 
        // ?? should we generate an error if these conditions don't hold?
        if (! (( exp.operandCount () == 2 && 
                exp.get_type (0) instanceof TyArithmetic &&
                exp.get_type (1) instanceof TyArithmetic))) return;

        Vector converted = 
            sc.makeArithmeticConversionExpressions
            (exp.get (0), exp.get (1));
        exp.set ((ExpressionNode) converted.elementAt (0), 0);
        exp.set ((ExpressionNode) converted.elementAt (1), 1);
    }
}
