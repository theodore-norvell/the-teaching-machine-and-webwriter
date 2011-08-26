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
import tm.utilities.Assert;

/**
 * Rule for generating the AST representation of unary floating point
 * operations.
 */
public class CGRUnaryFloatingOp extends CGRArithmeticOp {

	/**
	 * Creates a new <code>CGRUnaryFloatingOp</code> instance.
	 *
	 * @param applicable the set of operator ids for which this rule applies
	 */
	public CGRUnaryFloatingOp (String [] applicable) { super (applicable); }

	/**
	 * Generates the AST representation of unary floating point operations.
	 *
	 * @param exp will contain the AST expression, any previous contents 
	 * are discarded
	 */
	public void apply (ExpressionPtr exp) {
		TypeNode expType = exp.get_type (0);
		int operator_id = getOpId (exp.op().getTerminalId ());
		String operator_image = getOpImage (operator_id);
		ExpressionNode floatingOp = 
			new OpFloat (expType, operator_id, operator_image, exp.get (0));
		exp.set (floatingOp);
	}

}
