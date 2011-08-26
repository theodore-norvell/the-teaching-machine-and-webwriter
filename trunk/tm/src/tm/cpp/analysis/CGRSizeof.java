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
import tm.clc.analysis.ScopedName;
import tm.clc.ast.ExpUnimplemented;
import tm.clc.ast.ExpressionNode;
import tm.clc.ast.TypeNode;
import tm.utilities.Assert;

/**
 * Generates the AST representation of a <em>sizeof</em> expression.
 * <br><em>note:</em> sizeof is currently unimplemented.
 */
public class CGRSizeof extends CodeGenRule {

	public void apply (ExpressionPtr exp) {
		ExpressionNode sizeof = null;
		TypeNode sot = exp.get_type (0);
		if (exp.is (TypeNode.class, 0)) { // sizeof type
			sizeof = new ExpUnimplemented (sot, "sizeof type");
		} else { // sizeof expression
			sizeof = new ExpUnimplemented (sot, "sizeof expression");
		}
		exp.set (sizeof);
	}
}
