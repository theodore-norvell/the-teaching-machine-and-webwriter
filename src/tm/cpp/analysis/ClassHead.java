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

import java.util.Vector;

import tm.clc.analysis.ScopedName;

/** 
 * A preliminary representational class containing details about a class 
 * declaration. Similar in function to <code>SpecifierSet</code> and 
 * <code>FunctionDefnCompiled</code>. 
 */
public class ClassHead {

	/** identifies a <em>class</code> specifier */
    public static final int CLASS = 0;
	/** identifies a <em>struct</code> specifier */
    public static final int STRUCT = 1;
	/** identifies a <em>union</code> specifier */
    public static final int UNION = 2;

	private ScopedName name;
	private int key;
	private Vector specifiers;

	/** 
	 * Creates a new <code>ClassHead</code> instance
	 * @param key type of class (class, struct, union)
	 * @param name id of class
	 * @param specifiers a set of additional specifiers. Each element is 
	 * a <code>SpecifierSet</code> with details about a single base class.
	 */
    public ClassHead (int key, ScopedName name, Vector specifiers) {
		this.key = key;
		this.name = name;
		this.specifiers = specifiers;
	}

    public int get_key () { return key; }
    public ScopedName get_name () { return name; }
	public Vector get_specifiers () { return specifiers; }
}
