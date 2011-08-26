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
import tm.clc.ast.OpInt;
import tm.clc.ast.TypeNode;
import tm.utilities.Assert;

/**
 * Generates expressions for binary integral operations
 */
public class CGRBinaryIntegralOp extends CGRArithmeticOp {

    TypeNode boolType ;

    /**
     * Creates a new <code>CGRBinaryIntegralOp</code> instance.
     *
     * @param applicable the set of applicable operations
     * @param boolType The boolean type for the language.
     */
    public CGRBinaryIntegralOp (String [] applicable, TypeNode boolType) {
        super (applicable);
        this.boolType = boolType ; }

    /**
     * Generates a new <code>OpInt</code> AST node representing the 
     * binary integral operation.
     * @param exp will hold the <code>OpInt</code>
     */
    public void apply (ExpressionPtr exp) {
        int operator_id = getOpId (exp.op().getTerminalId ());
        TypeNode expType ;
        if( hasBoolResult (operator_id) ) expType = boolType ;
        else expType = exp.get_type(0) ;
        String operator_image = getOpImage (operator_id);
        ExpressionNode integralOp = new OpInt (expType, operator_id, operator_image, 
                               exp.get (0), exp.get (1));
        exp.set (integralOp);
    }
}
