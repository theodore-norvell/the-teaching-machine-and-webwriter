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

import tm.utilities.Assert;

/**
 * Functions as the external interface to expression building. 
 * <p>An abstraction of the expression building logic, 
 * <code>ExpressionManager</code> provides access to this logic in a manner
 * appropriate to external components that require it. This is ultimately the
 * <code>ParserContext</code> and so indirectly the <code>Parser</code>, 
 * which drives the generation of expressions during the parsing phase.
 * <p>The <code>ExpressionManager</code> is more than an interface, and is so 
 * named because it manages the highest level of expression generation via
 * a very few rulesets combining <code>ExpressionBuilder</code>s for the various
 * (language specific) broad categories of expression.
 * <p>The <code>ExpressionManager</code> is also responsible for building 
 * and providing internal access to the <code>RuleBase</code>, a generic
 * repository of <code>CodeGenRule</code>s used in expression generation. 
 * 
 * @author Derek Reilly
 * @created November 26, 2001
 */
public abstract class ExpressionManager {

	/**
	 * Contains the <code>CodeGenRule</code>s used by the expression building
	 * logic.
	 */
	protected RuleBase rb;

	/** 
	 * Get the rulebase associated with this expression manager
	 * @return the rulebase
	 */
	public RuleBase rulebase () { return rb; } 
}
