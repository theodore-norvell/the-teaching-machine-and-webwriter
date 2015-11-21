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
import tm.clc.ast.OpAddressOf;
import tm.clc.ast.TypeNode;
import tm.cpp.ast.TyPointer;
import tm.utilities.Assert;


/**
 * Generates an address-of expression (creating a pointer to an entity)
 */
public class CGRAddressOf extends CodeGenRule {

    /**
     * Generates an AST <code>OpAddressOf</code> expression, wrapping
     * the original expression in <code>exp</code>
     *
     * @param exp the original expression
     */
    public void apply (ExpressionPtr exp) { 
        int opidx = (exp.get () == null) ? 0 : -1;
        // get the address of exp
        ExpressionNode id = exp.get (opidx);
        String op_image = exp.op().getName ();
        TypeNode type_of_id = exp.get_base_type (opidx);
        TypeNode ptr_type = new TyPointer ();
        ptr_type.addToEnd (type_of_id);
        ExpressionNode address_of = new OpAddressOf (ptr_type, op_image, id);
        exp.set (address_of);
    }

}
