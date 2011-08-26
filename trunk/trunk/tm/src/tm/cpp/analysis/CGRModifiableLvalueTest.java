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

import java.util.Hashtable;

import tm.clc.analysis.ExpressionPtr;
import tm.clc.analysis.ScopedName;
import tm.clc.ast.ExpId;
import tm.clc.ast.ExpressionNode;
import tm.clc.ast.TyAbstractArray;
import tm.clc.ast.TyAbstractFun;
import tm.clc.ast.TypeNode;
import tm.cpp.ast.ExpFunctionName;
import tm.utilities.Assert;

/**
 * Tests whether an expression is a <em>modifiable lvalue</em>. In C++, 
 * this is an lvalue that is "not a function name, an array name, or 
 * <code>const</code>" (ARM 3.7)
 */
public class CGRModifiableLvalueTest extends CGRLvalueTest {

	public CGRModifiableLvalueTest () { super (); }
	public CGRModifiableLvalueTest (int expressionIndex) {
		super (expressionIndex);
	}

	/**
	 * Determines whether the expression contained in <code>exp</code>
	 * is a modifiable lvalue.
	 * @param exp expression being built
	 * @see tm.cpp.analysis.CGRLvalueTest#applies(ExpressionPtr, ScopedName, ExpressionPtr [])
	 */
	public boolean applies (ExpressionPtr exp) {
		boolean testResult = false;

		// 1. do we even have an lvalue ?
		if (super.applies (exp)) {
			// 2. is this an unmodifiable lvalue ? 
			// One of: 
			// - function name
			// - array name
			// - const

			ExpressionNode e = exp.get (start);
			Assert.check (e != null);			
			// ?? as with CGRLvalue, we are allowing 'fetched' lvalues in this
			//    check, assuming some external reason for it. We are only 
			//    enforcing the spec's rules about modifiability.
			TypeNode t = exp.get_base_type ();
			
			int tAttr = t.getAttributes ();
			if (!((e instanceof ExpFunctionName) || 
				  (t instanceof TyAbstractFun) ||
				  (t instanceof TyAbstractArray && e instanceof ExpId) ||
				  ((tAttr & Cpp_Specifiers.CVQ_CONST) != 0))) {
				testResult = true;
			}

			if (testResult == false) fail_detail = "not a modifiable lvalue";
		}
		return testResult;
	}
}
