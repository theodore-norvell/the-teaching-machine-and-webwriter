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

import tm.clc.ast.TyAbstractRef;
import tm.clc.ast.TypeNode;

/**
 * Tests for type equivalence among all operands. 
 */
public class CGREquivalentTypeTest extends CGRTest {
    /** 
     * Creates a new rule instance 
     */
    public CGREquivalentTypeTest () {
        messageOnFalse = "Non-equivalent operand types found";
    }

    /**
     * Does each operand have the same type ?
     *
     * @param exp contains the operands to check
     * @return <code>true</code> if <code>operands</code> have equivalent 
     * types, <code>false</code> otherwise.
     */
    public boolean applies (ExpressionPtr exp) {
        TypeNode [] types = new TypeNode [exp.operandCount ()];

        for (int i = 0; i < exp.operandCount (); i++) {
            types [i] = exp.get_base_type (i);
        }

        // eliminate references (reference to A and A are 
        // considered equivalent)
        for (int i = 0; i < types.length; i++) 
            if (types [i] instanceof TyAbstractRef)
                types [i] = ((TyAbstractRef) types [i]).getPointeeType ();

        // ensure that all types are the same via TypeNode.equals
        int i;
        for (i = 0; i + 1 < types.length && 
                 types [i].equal_types (types [i + 1]); i++) ; /*CHECK*/
        return (i == types.length - 1);

    }
    
}
