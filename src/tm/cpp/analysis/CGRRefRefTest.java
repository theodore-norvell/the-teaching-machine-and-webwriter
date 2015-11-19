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
import tm.clc.ast.TyAbstractRef;
import tm.clc.ast.TypeNode;

/**
 * Tests whether type is reference to reference
 */
public class CGRRefRefTest extends CGRTest {

    /** Use this constructor to test whether <code>exp</code> is a
     *  reference to a reference
     */
    public CGRRefRefTest () { 
    }

    /** User this constructor to test whether <code>operands[operand]</code>
     *  reference to a reference
     */
    public CGRRefRefTest (int operand) {
        super (operand);
    }
        

    /**
     * Is the indicated operand or exp of type reference to reference?
     * @param exp expression pointer
     * @return <code>true</code> if indicated operand or exp of type reference to
     *  reference, <code>false</code> otherwise.
     */
    public boolean applies (ExpressionPtr exp) {
        TypeNode t = exp.get_type (start);

        // ?? do we need to dereference ?

        if (t instanceof TyAbstractRef) { 
            // test for equality - distinguish between pointer and reference
            return ((TyAbstractRef) t).getPointeeType() instanceof TyAbstractRef ;
        } else {
            return false ;
        }
    }
}
