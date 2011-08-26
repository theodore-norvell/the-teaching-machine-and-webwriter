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

import tm.clc.ast.ExpResult;
import tm.clc.ast.ExpressionNode;
import tm.clc.ast.OpAssign;
import tm.utilities.Assert;

/**
 * Generates the AST representation of the initialization
 * of a return value with a <em>return expression</em>
 */
public class CGRResult extends CodeGenRule {

    /**
     * Generates the AST representation of the initialization
     * of a return value with a <em>return expression</em>
     */
    public void apply (ExpressionPtr exp) {
        // generate an ExpResult
        ExpressionNode e_result = new ExpResult (exp.get_type ());

        // exp becomes right hand side of assignment
        ExpressionNode e_assign = 
            new OpAssign (e_result.get_type (), exp.op().getTerminalId (),
                           e_result, exp.get ());
        
        exp.set (e_assign);
    }
}
