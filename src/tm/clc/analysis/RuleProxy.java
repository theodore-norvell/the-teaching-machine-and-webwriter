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
 * Proxy permitting lazy construction of rule sequences.
 * A rule sequence for a situation deemed obscure enough can be 
 * wrapped in a custom <code>RuleProxy</code>. The rule sequence will not be 
 * built until the first time it is used.
 */
public abstract class RuleProxy extends CodeGenRule {
	/** the identifier for the rule sequence used as the rulebase key */
	protected String ruleId;
	/** the rulebase the sequence should be added to */
	protected RuleBase ruleBase;
	/** will contain the rule sequence once built */
	protected CodeGenRule rule;

	/** 
	 * Creates a new <code>RuleProxy</code> instance
	 * @param ruleId the rule identifier
	 * @param rb the rulebase
	 */
	public RuleProxy (String ruleId, RuleBase rb) { 
		this.ruleId = ruleId;
		this.ruleBase = rb;
	}

	/**
	 * Applies the associated rule sequence. If the sequence has not yet 
	 * been built, this method will first generate the rule sequence and
	 * add an entry for it in the rulebase. This will generally replace 
	 * the entry for the proxy (if any), and so any subsequent accesses of 
	 * the rule will directly access the rule sequence. 
	 * <br><em>note:</em>Unfortunately, 
	 * rulebase access occurs during rule sequence construction, and not 
	 * (in general) rule application.
	 * @param exp the expression (and raw materials) against which to apply
	 * the rule
	 */
	public void apply (ExpressionPtr exp) {
		if (rule == null) {
			rule = buildRule ();
			ruleBase.put (ruleId, rule);
		}
		rule.apply (exp);
	}

	/**
	 * Deriving classes build the rule sequence in this method's
	 * implementation
	 * @return the rule sequence
	 */
	protected abstract CodeGenRule buildRule () ;
}
