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
import tm.clc.ast.OpFloat;
import tm.clc.ast.TypeNode;

/**
 * Rule to generate AST representations for built-in binary operations with
 * floating operands. Any conversions are expected to have occured before
 * this rule is applied.
 *
 */
public class CGRBinaryFloatingOp extends CGRArithmeticOp {

    TypeNode boolType ;

    /**
     * Creates a new <code>CGRBinaryFloatingOp</code> instance.
     * @param applicable a set of applicable operators
     * @param boolType The boolean type for the language.
     */
    public CGRBinaryFloatingOp (String [] applicable, TypeNode boolType) {
        super (applicable);
        this.boolType = boolType ; }

    /**
     * Generates the AST representation of the binary operation
     *
     * @param exp will contain the AST expression node, contains operands
     * at positions 0 and 1
     */
    public void apply (ExpressionPtr exp) {
        int operator_id = getOpId (exp.op().getTerminalId ());
        TypeNode expType ;
        if( hasBoolResult (operator_id) ) expType = boolType ;
        else expType = exp.get_type(0) ;
        String operator_image = getOpImage (operator_id);
        ExpressionNode floatingOp = 
            new OpFloat (expType, operator_id, operator_image, exp.get (0), 
                          exp.get (1));
        exp.set (floatingOp);
    }
}

