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
import tm.clc.ast.TyAbstractClass;
import tm.clc.ast.TyAbstractPointer;
import tm.clc.ast.TypeNode;
import tm.cpp.ast.TyArray;
import tm.cpp.ast.TyFundamental;
import tm.utilities.Assert;

/**
 * Determines whether and expression's type is complete; i.e. is/points/refers
 * ultimately to a fundamental type or user-defined type for which a definition
 * is known. An array of unknown size is also an incomplete object type.
 */
public class CGRCompleteTypeTest extends CGRTest {

    /**
     * Determines whether and expression's type is complete; i.e. is/points/refers
     * ultimately to a fundamental type or user-defined type for which a definition
     * is known.
     */
    public boolean applies (ExpressionPtr exp) {
        boolean result = false;
        TypeNode t = exp.get_base_type ();
        
        // want to look at base element type
        while (t instanceof TyAbstractPointer || 
               t instanceof TyArray) {
            if (t instanceof TyArray) {
                TyArray ta = (TyArray) t;
                if (ta.getNumberOfElements () == -1) {
                    // size unknown, incomplete type
                    break;
                }
                t = ta.getElementType ();
            } else { // pointer
                t = ((TyAbstractPointer) t).getPointeeType ();
            }
        }

        if (t instanceof TyFundamental) result = true;

        else if (t instanceof TyAbstractClass) { // user-defined type 
            result = ((TyAbstractClass) t).isDefined ();
        }

        return result;
    }
}
