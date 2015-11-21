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

import tm.clc.analysis.CodeGenRule;
import tm.clc.analysis.CGRFetch;
import tm.clc.analysis.ExpressionPtr;
import tm.clc.ast.ExpressionNode;
import tm.clc.ast.OpArraySubscript;
import tm.clc.ast.TypeNode;
import tm.javaLang.ast.TyJavaArray;
import tm.javaLang.ast.TyInt;
import tm.javaLang.ast.TyPointer;
import tm.javaLang.ast.TyRef;

import tm.utilities.Assert;

/**
 * Rule to generate a subscript expression (array element access / pointer
 * addition)
 */
public class CGRSubscript extends CodeGenRule {

	/**
	 * Generates the AST representation of a subscript expression.
	 *
	 * @param exp will hold the array subscript expression
	 * subexpressions comprising the subscript, in the correct order 
	 * (exp0[exp1]) 
	 */
	public void apply (ExpressionPtr exp) {
		CGRFetch lfetch = new CGRFetch(0);
		CGRFetch rfetch = new CGRFetch(1);
		lfetch.apply(exp);
		rfetch.apply(exp);
		
		
		Assert.error(exp.get_type(0) instanceof TyPointer, "Subscripting being applied to a non-array.");		
		TyPointer tyPointer = (TyPointer)exp.get_type(0);

		CodeGenRule rule = new CGRIntegralPromotion(1);
		rule.apply(exp);
		TypeNode rType = exp.get_type(1);

		Assert.error(rType instanceof TyInt, "subscripts must be integer type");
		
		Assert.error(tyPointer.getPointeeType() instanceof TyJavaArray, "Subscripting being applied to a non-array.");
		TyJavaArray tyArray = (TyJavaArray)tyPointer.getPointeeType();

		ExpressionNode subscript = new OpArraySubscript (new TyRef (tyArray.getElementType()), "[", 
												"]", exp.get(0), exp.get(1));

		exp.set (subscript);
	}

}
