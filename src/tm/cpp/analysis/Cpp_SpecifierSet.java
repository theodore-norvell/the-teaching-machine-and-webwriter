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

import tm.clc.analysis.SpecifierSet;

/** 
 * <code>SpecifierSet</code> used for declarations in C++
 */
public class Cpp_SpecifierSet extends SpecifierSet 
	implements Cpp_Specifiers {

	/** Creates a new instance - specifier list is initialized for 
	 * C++ specifiers
	 */
	public Cpp_SpecifierSet () { 
		spec_list = new boolean [SP_NUM_SPECIFIERS];
	}

	/**
	 * Debugging use
	 */
	public String toString () {
		StringBuffer specStr = new StringBuffer ("specifier set : ");
		for (int i = 0; i < spec_list.length; i++) 
			if (spec_list [i]) specStr.append (spec_strings [i] + " ");
		if (type_name != null) 
			specStr.append ("\ntype name : " + type_name.getName ());
		return specStr.toString ();
	}
}


