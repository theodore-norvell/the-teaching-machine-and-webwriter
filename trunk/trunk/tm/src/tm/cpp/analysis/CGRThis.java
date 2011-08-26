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

import tm.clc.analysis.CTSymbolTable;
import tm.clc.analysis.CodeGenRule;
import tm.clc.analysis.Declaration;
import tm.clc.analysis.ExpressionPtr;
import tm.clc.analysis.LFConst;
import tm.clc.analysis.ScopeHolder;
import tm.clc.analysis.ScopedName;
import tm.clc.ast.ExpThis;
import tm.clc.ast.ExpressionNode;
import tm.cpp.ast.TyPointer;
import tm.utilities.Assert;

/**
 * Generates the AST representation of a <em>this</em> expression.
 */
public class CGRThis extends CodeGenRule {

	CTSymbolTable symbolTable;

	/** 
	 * Creates a new rule instance
	 * @param symbolTable the compile-time symbol table used to locate
	 * the class information relevant to the expression.
	 */
	public CGRThis (CTSymbolTable symbolTable) {
		this.symbolTable = symbolTable;
	}

	public void apply (ExpressionPtr exp) {

		// this expressions can occur in member functions or in class scope, 
		// access the class declaration to get the type
		ScopeHolder sch = symbolTable.getCurrentScope ();
		
		Declaration c_decl = 
			symbolTable.getCurrentScope().getClassDeclaration ();

		TyPointer this_ptr = new TyPointer ();
		this_ptr.addToEnd (c_decl.getType ());
		
		ExpressionNode this_exp = 
			new ExpThis (this_ptr, exp.op().getName ());

		exp.set (this_exp);
	}
}
