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
import tm.clc.ast.OpArraySubscript;
import tm.clc.ast.OpPointerSubscript;
import tm.clc.ast.TyAbstractArray;
import tm.clc.ast.TyAbstractPointer;
import tm.clc.ast.TypeNode;
import tm.cpp.ast.TyRef;
import tm.utilities.Assert;

/**
 * Rule to generate a subscript expression (array element access / pointer
 * addition)
 */
public class CGRSubscript extends CodeGenRule {
	private String left, right;

	/**
	 * Creates a new <code>CGRSubscript</code> instance.
	 *
	 * @param left the <code>String</code> image to use for the left bracket
	 * @param right the image to use for the right bracket
	 */
	public CGRSubscript (String left, String right) {
		this.left = left;
		this.right = right;
	}

	/**
	 * Generates the AST representation of a subscript expression.
	 *
	 * @param exp will hold the array subscript expression
	 * subexpressions comprising the subscript, in the correct order 
	 * (exp1[exp2]) 
	 */
	public void apply (ExpressionPtr exp) {
		ExpressionNode loperand = exp.get (0);
		ExpressionNode roperand = exp.get (1);
		TypeNode lType = exp.get_base_type (0);
		ExpressionNode subscript = null;
		Assert.check (lType instanceof TyAbstractArray ||
					   lType instanceof TyAbstractPointer);
		if (lType instanceof TyAbstractArray) {
			TypeNode expType = ((TyAbstractArray) lType).getElementType ();



			subscript = new OpArraySubscript (new TyRef (expType), left, 
												right, loperand, roperand);
		} else { // pointer
			TypeNode expType = ((TyAbstractPointer) lType).getPointeeType ();
			subscript = new OpPointerSubscript (new TyRef (expType), left, 
												  right, loperand, roperand);
		}

		exp.set (subscript);
	}

}
