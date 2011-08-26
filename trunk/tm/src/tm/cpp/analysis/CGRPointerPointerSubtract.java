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

import tm.clc.analysis.CGRArithmeticOp;
import tm.clc.analysis.ExpressionPtr;
import tm.clc.ast.Arithmetic;
import tm.clc.ast.ExpressionNode;
import tm.clc.ast.OpPointer;
import tm.clc.ast.TypeNode;
import tm.cpp.ast.TyInt;

/**
 * Generates the AST representation of <em>pointer-pointer subtraction</em>
 */
public class CGRPointerPointerSubtract extends CGRArithmeticOp {
	/** 
	 * Creates a new rule instance
	 * @param applicable the set of operators for which this rule is 
	 * applicable
	 */
	public CGRPointerPointerSubtract (String [] applicable) { super (applicable); }

	public void apply (ExpressionPtr exp) {
		TypeNode expType = TyInt.get ();
		int operator_id = Arithmetic.SUBTRACT_POINTER_POINTER;
		String operator_image = getOpImage (operator_id);
		ExpressionNode pointerOp = 
			new OpPointer (expType, operator_id, operator_image, exp.get (0), 
							exp.get (1));
		exp.set (pointerOp);
	}
}
