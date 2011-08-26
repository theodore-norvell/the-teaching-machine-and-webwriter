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
 * A rule node providing conditional application of rule sets. See 
 * the <code>apply</code> method for details.
 */
public class CGRConditionalNode extends CGRNode {
	private CGRTest condition;
	private CodeGenRule precursor;
	private CodeGenRule onTrue;
	private CodeGenRule onFalse;

	/**
	 * Creates a new <code>CGRConditionalNode</code> instance.
	 *
	 * @param test the conditional expression to evaluate 
	 * @param precursor the ruleset to apply prior to this condition
	 * @param onTrue the ruleset to apply when <code>test</code> is 
	 * <code>true</code>
	 * @param onFalse the ruleset to apply when <code>test</code> is 
	 * <code>false</code>
	 */
	public CGRConditionalNode (CGRTest test, 
								 CodeGenRule precursor, 
								 CodeGenRule onTrue, 
								 CodeGenRule onFalse) {
		super (null);
		this.condition = test;
		this.precursor = precursor;
		this.onTrue = onTrue;
		this.onFalse = onFalse;
	}

	/**
	 * Creates a new <code>CGRConditionalNode</code> instance.
	 *
	 * @param test the conditional expression to evaluate 
	 * @param onTrue the ruleset to apply when <code>test</code> is 
	 * <code>true</code>
	 * @param onFalse the ruleset to apply when <code>test</code> is 
	 * <code>false</code>
	 */
	public CGRConditionalNode (CGRTest test, 
								 CodeGenRule onTrue, 
								 CodeGenRule onFalse) {
		this (test, null, onTrue, onFalse);
	}

	/**
	 * Creates a new <code>CGRConditionalNode</code> instance.
	 *
	 * @param test the conditional expression to evaluate 
	 * @param onTrue the ruleset to apply when <code>test</code> is 
	 * <code>true</code>
	 */
	public CGRConditionalNode (CGRTest test, 
								 CodeGenRule onTrue) {
		this (test, null, onTrue, null);
	}

	/**
	 * Execute this rule node
	 * <ol>the following steps occur:
	 * <li><code>precursor</code> rules are applied</li>
	 * <li><code>condition.applies</code> is executed to determine next rules</li>
	 * <li><code>onFalse</code> or <code>onTrue</code> rules are applied</li>
	 * </ol>
	 * @param exp not used, passed to other rule nodes
	 * @param opfn  not used, passed to other rule nodes
	 */
	public void apply (ExpressionPtr exp) {
		if (precursor != null) applyRule (precursor, exp);

		if (condition.applies (exp)) {
			if (onTrue != null) applyRule (onTrue, exp);
		} else { 
			if (onFalse != null) applyRule (onFalse, exp);
		}
	}
}

