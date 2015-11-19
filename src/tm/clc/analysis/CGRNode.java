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

import tm.utilities.Debug;

/**
 * A step in a sequence of rules applied against the elements of an
 * expression towards the generation of an AST representation 
 * of the expression.
 * <p>Nodes are generally linked with other nodes to form a rule tree; the 
 * relationships (links and traversal patterns) between a node and other nodes 
 * are specific to the implementing class and are defined there.
 */
public abstract class CGRNode extends CodeGenRule {
    protected static boolean printRules = true;

    /**
     * The rule to apply at this point in the sequence. Not all structural 
     * rules (nodes) will use this.
     */
    protected CodeGenRule rule;

    /**
     * Creates a new <code>CGRNode</code> instance.
     *
     * @param rule the rule to apply when called to.
     */
    public CGRNode (CodeGenRule rule) {
        this.rule = rule;
    }

    protected void applyRule (CodeGenRule the_rule, ExpressionPtr exp) {
        if (printRules) {
            if(the_rule.description != null) d.msg (Debug.COMPILE, "Applying rule: "+the_rule.description);
            else d.msg (Debug.COMPILE, "Applying rule of class: "+the_rule.getClass().getName()) ;
        }
        the_rule.apply (exp);
    }

}
