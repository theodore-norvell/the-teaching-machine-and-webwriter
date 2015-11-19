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

package tm.javaLang.analysis;

import tm.clc.analysis.CGRTest;
import tm.clc.analysis.ExpressionPtr;
import tm.javaLang.ast.TyJava;
import tm.javaLang.ast.TyPrimitive;
import tm.utilities.Assert;
/**
 * In Java, conversion between primitive and reference types is never allowed.
 * Tests whether both operands are primitive (returns true) or reference (returns false)
 * Throws an error if operands are mixed
 */
public class CGRTypeConformanceTest extends CGRTest {
    static final String eMessage = "Cannot convert between primitive and reference types.";

	public boolean applies (ExpressionPtr exp) {
            TyJava leftType =  (TyJava)exp.get_type (0);
            TyJava rightType =  (TyJava)exp.get_type (1);
            if (leftType instanceof TyPrimitive) {
                Assert.check(rightType instanceof TyPrimitive, eMessage);
                return true;
            }
            Assert.check(!(rightType instanceof TyPrimitive), eMessage);
            return false;
        }

}

