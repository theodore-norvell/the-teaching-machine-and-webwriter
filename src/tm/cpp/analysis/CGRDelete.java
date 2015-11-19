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
import tm.cpp.ast.ExpDelete;
import tm.cpp.ast.ExpDeleteArray;
import tm.cpp.parser.ParserConstants;


/**
 * Generates the standard delete expression. Initialization portion
 * occurs in another rule, depending on operand type.
 */
public class CGRDelete extends CodeGenRule implements ParserConstants {

	public void apply (ExpressionPtr exp) {
		if (exp.op().getTerminalId().equals (OpTable.get (DELETE))) {
			exp.set (new ExpDelete (exp.get_type (0), exp.get (0)));
		} else { // delete array
			exp.set (new ExpDeleteArray (exp.get_type (0), exp.get (0)));
		}
	}
}
