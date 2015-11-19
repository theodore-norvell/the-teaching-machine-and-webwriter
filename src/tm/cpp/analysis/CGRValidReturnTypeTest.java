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
import tm.clc.ast.ExpressionNode;
import tm.clc.ast.TyAbstractClass;
import tm.clc.ast.TyAbstractFun;
import tm.clc.ast.TypeNode;
import tm.cpp.ast.ExpFunctionName;
import tm.utilities.Assert;


/** is the return type valid at the time of call ? */
public class CGRValidReturnTypeTest extends CGRTest {

    public CGRValidReturnTypeTest () {
        messageOnFalse = "return type is not valid at time of call";
    }

    /** 
     * is the return type valid at the time of call ? 
     * return type of fn must be complete object type, reference type
     *    or void
     *
     * @param exp contains the expression to test
     */
    public boolean applies (ExpressionPtr exp) {
        boolean valid = false;
        ExpressionNode fe = exp.get ();
        TypeNode fnType; 
        if (fe instanceof ExpFunctionName) {
            // there must be a matching function to use
            RankedFunction m = ((ExpFunctionName) fe).getMatch ();
            Assert.check (m != null && m.declaration != null);
            fnType = m.declaration.getType ();
            Assert.check (fnType != null && 
                           fnType instanceof TyAbstractFun);
            fnType = ((TyAbstractFun) fnType).returnType ();
        } else { // we should have a function call expression
            fnType = exp.get_type ();
        }

        if (fnType instanceof TyAbstractClass) {
            valid = ((TyAbstractClass) fnType).isDefined ();
        } else { // ok...
            valid = true;
        }
        return valid;
    }
    
}