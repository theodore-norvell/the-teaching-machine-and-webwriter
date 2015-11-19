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

import tm.clc.analysis.CGRTest;
import tm.clc.analysis.ExpressionPtr;

/**
 * Tests for globally qualified id/operator. 
 */
public class CGRGlobalQualTest extends CGRTest {

    /**
     * Is the id/operator globally qualified ?
     *
     * @param exp contains the id/operator to test
     * @return <code>true</code> if globally qualified,
     * <code>false</code> otherwise.
     */
    public boolean applies (ExpressionPtr exp) {
        return (exp.op().is_absolute ());
    }
}
