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
import tm.clc.ast.OpParentheses;

/**
 * Rule to generate a parentheses expression; the wrapping of an expression
 * in parentheses.
 */
public class CGRParentheses extends CodeGenRule {
	private String left, right;

	/**
	 * Creates a new <code>CGRParentheses</code> instance.
	 *
	 * @param left the <code>String</code> image to use for the left parenthesis
	 * @param right the image to use for the left parenthesis
	 */
	public CGRParentheses (String left, String right) {
		this.left = left;
		this.right = right;
	}

	/**
	 * Generates the AST representation of a parentheses expression.
	 *
	 * @param exp holds the subexpression, will contain the same wrapped in 
	 * a parentheses expression
	 */
	public void apply (ExpressionPtr exp) { 
		ExpressionNode parenths = 
			new OpParentheses (exp.get_type (), left, right, exp.get ());
		exp.set (parenths);
	}

}
