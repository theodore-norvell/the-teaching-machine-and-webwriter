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
import tm.clc.ast.OpLogical;
import tm.clc.ast.TypeNode;
import tm.utilities.Assert;

/**
 * Generates expressions for binary logical operations (and, or)
 */
public class CGRBinaryLogicalOp extends CGRArithmeticOp {
	private TypeNode resultType;

	/**
	 * Creates a new <code>CGRBinaryLogicalOp</code> instance.
	 *
	 * @param applicable the set of applicable operations
	 * @param resultType the type of evaluated logical expressions
	 */
	public CGRBinaryLogicalOp (String [] applicable, 
								  TypeNode resultType) { 
		super (applicable); 
		this.resultType = resultType;
	}


	/**
	 * Generates a new <code>OpLogical</code> AST node representing the 
	 * binary logical operation.
	 * @param exp will hold the <code>OpLogical</code>
	 */
	public void apply (ExpressionPtr exp) {

		int operator_id = getOpId (exp.op().getTerminalId ());
		String operator_image = getOpImage (operator_id);
		ExpressionNode logicalOp = 
			new OpLogical (resultType, operator_id, operator_image, 
							exp.get (0), exp.get (1));
		exp.set (logicalOp);
	}
}
