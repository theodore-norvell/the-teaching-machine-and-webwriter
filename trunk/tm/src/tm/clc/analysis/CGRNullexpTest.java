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
 * Tests for <code>null</code> expression. This can indicate that no applicable
 * ruleset has been determined to this point, or that rules have modified 
 * operands only to this point.
 *
 */
public class CGRNullexpTest extends CGRTest {

    /**
     * Is the expression <code>null</code> ?
     *
     * @param exp the <code>ExpressionPtr</code> containing an 
     * <code>ExpressionNode</code> or <code>null</code>
     * @return <code>true</code> if <code>exp</code> contains 
     * <code>null</code>, <code>false</code> otherwise.
     */
    public boolean applies (ExpressionPtr exp) {
        return (exp.get () == null);
    }
}
