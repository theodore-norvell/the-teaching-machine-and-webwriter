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

package tm.javaLang.analysis;

import tm.clc.analysis.CGRTest;
import tm.clc.analysis.ExpressionPtr;
import tm.clc.ast.ExpressionNode;
import tm.clc.ast.TyAbstractRef;

/**
 * Tests whether an expression is an lvalue expression; i.e. refers to an 
 * object (something that has storage) or a function.
 */
public class CGRLvalueTest extends CGRTest {

	/** describes reason for test failing */
	protected String fail_detail = "not an lvalue";

	// test instance used for recursive / iterative tests 
	private static CGRLvalueTest subtest = new CGRLvalueTest ();

	/** 
	 * Creates a new rule instance, default to testing exp 
	 */ 
	public CGRLvalueTest () { }
	/** 
	 * Creates a new rule instance
	 * @param expressionIndex the index of the operand to test
	 */
	public CGRLvalueTest (int expressionIndex) { super (expressionIndex); }

	/** 
	 * Provides a specialized error message if test fails in assertion
	 * @param exp the expression being tested
	 * @return the error message
	 */
	public String getMessageOnFalse (ExpressionPtr exp) {
		ExpressionNode e = exp.get (start);
		return e.name () + " expression of type " + 
			e.get_type().getTypeString () + " : " + fail_detail;
	}

	/**
	 * This method tests if the prechosen expression is an lvalue
	 * @param exp expression being built
	 */
	public boolean applies (ExpressionPtr exp) {
                return exp.get (start).get_type () instanceof TyAbstractRef;
	}
}



