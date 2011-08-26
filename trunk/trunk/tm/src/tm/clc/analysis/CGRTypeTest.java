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

import tm.clc.ast.TyAbstractPointer;
import tm.clc.ast.TypeNode;

/**
 * Tests for type - can test expression or one of operands, depending on 
 * constructor arguments. This works for basic types only - it will not 
 * distinguish between types of pointers, or specific classes, but it will
 * determine whether some type is a pointer or a class.
 */
public class CGRTypeTest extends CGRTest {
    /** the java class representing the type to test for */
    Class type;

    /** Use this constructor to test whether <code>exp</code> is of the 
     * indicated type
     */
    public CGRTypeTest (Class type) { 
        this.type = type;
    }

    /** User this constructor to test whether <code>operands[operand]</code> is
     * of the indicated type
     */
    public CGRTypeTest (Class type, int operand) {
        super (operand);
        this.type = type;
    }
        

    /**
     * Is the indicated operand or exp of the same type as <code>type</code> ?
     *
     * @param exp expression
     * @return <code>true</code> if indicated operand or exp is of the same
     * type as <code>type</code>, <code>false</code> otherwise.
     */
    public boolean applies (ExpressionPtr exp) {
        boolean result = false;
        TypeNode t = exp.get_base_type (start);

        // ?? do we need to dereference ?

        if (t instanceof TyAbstractPointer) { 
            // test for equality - distinguish between pointer and reference
            result = type.equals (t.getClass());
        } else {
            // test for inheritance or equality
            result = type.isAssignableFrom (t.getClass ());
        }
        
        return result;
    }
}
