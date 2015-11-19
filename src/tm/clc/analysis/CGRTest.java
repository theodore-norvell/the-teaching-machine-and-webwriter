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
 * <code>CGRTest</code> subclasses define conditions used by
 * <code>CGRConditionalNode</code>s, and <code>CGRAssertion</code>s.
 */
public abstract class CGRTest extends CodeGenRule {
    /** A message that can be displayed if the test does not pass - can
     *  be set to something more informative than the default.
     */
    protected String messageOnFalse = "Test did not pass";

    /** creates a new instance of the test */
    public CGRTest () { }
    /** creates a new instance of the test, indicating by index either:
     * <ul><li>the entity from which to start evaluation when the test executes, or
     * <li>the single entity to consider during test execution</ul>
     * @param start the index of the entity
     */
    public CGRTest (int start) { super (start); }
    /** creates a new instance of the test, indicating by index either:
     * <ul><li>the range of entities to consider in evaluation when the test executes, or
     * <li>the two entities to consider during test execution</ul>
     * @param start the index of the first entity
     * @param end the index of the second entity
     */
    public CGRTest (int start, int end) { super (start, end); }

    /**
     * Does the condition apply ?
     *
     * @param exp contains an expression
     */
    public abstract boolean applies (ExpressionPtr exp) ;


    /**
     * Do-nothing implementation. <code>CodeGenRule</code> interface is
     * implemented so conditions can be stored in <code>RuleBase</code>.
     */
    public void apply (ExpressionPtr exp) { }

    /**
     * Gives the message to supply when false (if such an action is
     * appropriate). Subclasses can override this method to provide
     * additional, possibly dynamic information.
     * @param exp the expression on which the test was performed
     */
    public String getMessageOnFalse (ExpressionPtr exp) {
        return messageOnFalse;
    }

}