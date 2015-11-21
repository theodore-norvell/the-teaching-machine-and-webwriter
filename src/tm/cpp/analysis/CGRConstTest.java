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

import tm.clc.analysis.CGRTest;
import tm.clc.analysis.ExpressionPtr;
import tm.clc.ast.ExpressionNode;
import tm.utilities.Assert;

/**
 * Tests whether an expression is of const type.
 */
public class CGRConstTest extends CGRTest {

	/** 
	 * Create a new rule instance, default to testing expression being
	 * built
	 */ 
	public CGRConstTest () { }
	/**
	 * Create a new rule instance, specifying which operand to test
	 * @param expressionIndex index of the operand to test
	 */
	public CGRConstTest (int expressionIndex) { super (expressionIndex); }

	/** 
	 * Provide a specialized error message if the test fails within an 
	 * assertion
	 */
	public String getMessageOnFalse (ExpressionPtr exp) {
		Assert.check (start < exp.operandCount());
		ExpressionNode e = exp.get (start);
		return e.name () + " expression of type " + 
			e.get_type().getTypeString () + " is not const";
	}

	/**
	 * This method tests if the prechosen expression is of const type
	 * @param exp expression being built, operands being used
	 */
	public boolean applies (ExpressionPtr exp) {
		Assert.check (start < exp.operandCount());
		ExpressionNode e = exp.get (start);
		Assert.check (e != null);
		int attributes = e.get_type().getAttributes ();
		return ((attributes & Cpp_Specifiers.CVQ_CONST)	!= 0);
	}
}
