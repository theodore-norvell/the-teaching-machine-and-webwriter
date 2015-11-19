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

/**
 * A rule node that applies a single rule in succession to each of 
 * an expression's operands, followed by a rule associated with this node.
 *
 */
public class CGROperandNode extends CGRNode {
	private CodeGenRule operandrule;

	/**
	 * Creates a new <code>CGROperandNode</code> instance.
	 *
	 * @param rule the rule associated with this node
	 * @param operandrule the rule to apply to each of the operands
	 */
	public CGROperandNode (CodeGenRule rule, CodeGenRule operandrule) {
		super (rule);
		this.operandrule = operandrule;
	}

	/**
	 * Applies a rule to each of the operands provided, followed by a rule
	 * associated with this node.
	 * @param exp contains the final generated expression (or partially 
	 * generated). 
	 */
	public void apply (ExpressionPtr exp) {
		for (int i = 0; i < exp.operandCount (); i++) {
		    ExpressionPtr opep = new ExpressionPtr 
				(exp.get (i), exp.op (), new Object [] {exp.get (i)}); 
		    opep.opcat = exp.opcat ;
		    applyRule (operandrule, opep);
		    exp.set (opep.get (), i);
		}
		applyRule (rule, exp);
	}
}
