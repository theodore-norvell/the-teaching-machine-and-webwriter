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
import tm.clc.ast.TyAbstractPointer;
import tm.clc.ast.TypeNode;
import tm.javaLang.ast.TyJava;
import tm.utilities.Debug;


/**
 * Tests whether a conversion is widening or an identity
 */
public class CGRWideningOrEqualTest extends CGRTest {

	/**
	 * This method tests whether a conversion isn't narrowing.
	 * @param exp expression being tested
     * <P><strong>Precondition:</strong>
     * <ul><li>exp.get_base_type (0) is the type to convert to
     *     </li>
     *     <li>exp.get_base_type (1) is the type to convert from
     * </ul>
     * <p><strong>Postcondition:</strong>
     * <ul>
     *     <li> result iff exp.get_base_type (1) is the same as or 
     *          can be widened to exp.get_base_type (0)
     *     </li>
     * </ul>
	 */
	public boolean applies (ExpressionPtr exp) {
            TypeNode toType = exp.get_base_type (0);
            TypeNode fromType = exp.get_base_type (1);
            Debug.getInstance().msg(Debug.COMPILE, "WideningOrEqualTest: to-type is " + (toType).toString() +
                " and from-type is " + (fromType).toString());
            
            // Side cast. Java TypeNodes are also TyJava. This is why tag interfaces are a pain!
            return toType.equal_types(fromType)
                || ((TyJava)toType).isReachableByWideningFrom((TyJava)fromType);
	}
}