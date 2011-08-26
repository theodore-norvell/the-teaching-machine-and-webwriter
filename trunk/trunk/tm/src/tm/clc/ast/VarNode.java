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

package tm.clc.ast;
 
import tm.clc.analysis.ScopedName;
import tm.utilities.Assert;

public class VarNode implements TypedNodeInterface {
	private ScopedName name ;
	private TypeNode type ;

    /**  nm should be absolute */  // <<<NOT IN JAVA mpbl
	public VarNode(ScopedName nm, TypeNode tp ) {
//		Assert.check( nm.is_absolute(), nm.toString() + " must be absolute" ) ;
		name = nm ;
		type = tp ;
    }

	public ScopedName getName() { return name ; }

	public TypeNode get_type() { return type ; } 
}
