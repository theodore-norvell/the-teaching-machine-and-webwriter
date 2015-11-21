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

import tm.clc.analysis.CGRSequentialNode;
import tm.clc.analysis.CTSymbolTable;
import tm.clc.analysis.CodeGenRule;
import tm.clc.analysis.ExpressionPtr;
import tm.clc.analysis.RuleBase;
import tm.clc.analysis.ScopedName;
import tm.cpp.parser.ParserConstants;

/**
 * <code>ExpressionBuilder</code> responsible for primary expressions in C++:
 * <ul>
 * <li><em>this</em></li>
 * <li>parenthesized expressions</li>
 * <li>id expressions</li>
 * </ul>
 * literals are handled in <code>Literals</code>
 */
public class Eb_Primary extends CppExpressionBuilder 
	implements ParserConstants {

	/**
	 * Creates a new <code>Eb_Primary</code> instance.
	 *
	 * @param ruleBase a reference to the set of all <code>CodeGenRules</code>
	 */
	public Eb_Primary (RuleBase ruleBase, CTSymbolTable symbolTable) { 
		super (ruleBase, symbolTable, "primary");
	}

	/**
	 * Does nothing. Primary expression rules are applied directly via 
	 * retrieval from the rule base.
	 */
	public void apply (ExpressionPtr exp, ScopedName op, 
					   ExpressionPtr [] operands) { }


	protected void buildTables () {
		CodeGenRule rs_id_exp = 
			new CGRSequentialNode
			(rb.get (R_REFERENCE_LVAL), new CGRIdExp (symbolTable));

		rb.put (R_ID_EXPRESSION, rs_id_exp);
		rb.put (R_THIS_EXPRESSION, new CGRThis (symbolTable));
	   
	}
}


