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

import tm.utilities.Assert;

/**
 * swap two operands
 */
public class CGRSwapOperands extends CodeGenRule {

	/**
	 * Creates a new <code>CGRSwapOperands</code> instance.
	 */
	public CGRSwapOperands (int one, int two) {
		start = one;
		end = two;
	}

	/**
	 * swap the operands
	 */
	public void apply (ExpressionPtr exp) {
		Object t = exp.operandAtAs (start, Object.class);
		exp.set (exp.get (end), start);
		exp.set (t, end);
	}


}






