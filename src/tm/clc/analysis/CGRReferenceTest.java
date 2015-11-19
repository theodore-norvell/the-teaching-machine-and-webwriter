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
 * Tests for reference expression. 
 */
public class CGRReferenceTest extends CGRTest {

	public CGRReferenceTest () { super (); }
	public CGRReferenceTest (int pos) { super (pos); }

	/**
	 * Is the expression a reference ?
	 *
	 * @param exp contains the expression to test
	 * @return <code>true</code> if <code>exp</code> contains 
	 * a reference, <code>false</code> otherwise.
	 */
	public boolean applies (ExpressionPtr exp) {
		TypeNode t = exp.get_base_type (start);
		return (t instanceof TyAbstractRef); 
	}
}
