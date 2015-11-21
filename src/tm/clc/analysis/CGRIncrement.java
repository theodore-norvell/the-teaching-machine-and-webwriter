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
import tm.clc.ast.OpIncrement;
import tm.clc.ast.TypeNode;
import tm.utilities.Assert;


/**
 * Generates an AST expression node representing a prefix or postfix 
 * increment/decrement (++, --)
 */
public class CGRIncrement extends CodeGenRule {
	private boolean prefix, increment;
	
	/** creates a new rule instance 
	 * @param prefix prefix or postfix ?
	 * @param increment increment or decrement ?
	 */
	public CGRIncrement (boolean prefix, boolean increment) {
		this.prefix = prefix;
		this.increment = increment;
		start = 0;
	}

	/**
	 * Generates an AST representation of prefix or postfix increment/decrement
	 *
	 * @param exp contains the expression to increment/decrement
	 */
	public void apply (ExpressionPtr exp) { 
		ExpressionNode e = exp.get (start);
		String op_image = exp.op().getName ();
		TypeNode t = exp.get_base_type (start);
		ExpressionNode incr = 
			new OpIncrement (t, prefix, increment, op_image, e);
		exp.set (incr);
	}

}
