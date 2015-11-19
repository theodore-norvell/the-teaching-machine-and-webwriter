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
 * Rule which generates the AST representation of a unary operation with an 
 * integral operand
 */
public class CGRUnaryIntegralOp extends CGRArithmeticOp {

	/**
	 * Creates a new <code>CGRUnaryIntegralOp</code> instance.
	 *
	 * @param applicable the set of applicable operators
	 */
	public CGRUnaryIntegralOp (String [] applicable) { super (applicable); }

	/**
	 * Generates the AST representaion of a unary operation with an integral
	 * operand
	 * @param exp will contain the resulting AST expression, prior contents
	 * discarded
	 */
	public void apply (ExpressionPtr exp) {
		TypeNode expType = exp.get_type (0);
		int operator_id = getOpId (exp.op().getTerminalId ());
		String operator_image = getOpImage (operator_id);
		ExpressionNode integralOp = 
			new OpInt (expType, operator_id, operator_image, exp.get (0));
		exp.set (integralOp);
	}
}
