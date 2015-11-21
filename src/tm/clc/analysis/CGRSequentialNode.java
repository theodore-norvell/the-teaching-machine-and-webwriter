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
 * A rule node implementing sequential execution: first a single precursor
 * node is applied, followed by execution of a rule associated with this node.
 */
public class CGRSequentialNode extends CGRNode {
	private CodeGenRule precursor;

	/**
	 * Creates a new <code>CGRSequentialNode</code> instance.
	 *
	 * @param rule the rule associated with this node
	 * @param precursor the rule to apply prior to executing our rule
	 */
	public CGRSequentialNode (CodeGenRule rule, CodeGenRule precursor) {
		super (rule);
		this.precursor = precursor;
	}

	/**
	 * Applies the precursor rule, followed by execution of our 
	 * associated rule.
	 *
	 * @param exp contains final expression or incomplete version thereof
	 * @param opfn operation or function id
	 */
	public void apply(ExpressionPtr exp) {
		applyRule (precursor, exp);
		applyRule (rule, exp);
	}
}
