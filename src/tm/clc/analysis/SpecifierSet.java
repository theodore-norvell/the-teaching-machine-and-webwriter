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

import tm.clc.ast.TypeNode;

/**
 * A list of specifiers encountered in a declaration, including 
 * access modifiers, storage class specifiers, type names, cv-qualifiers, 
 * etc. 
 * <br>Used when building the compile-time and runtime representations 
 * of the declaration.
 */
public abstract class SpecifierSet {

    /** 
     * a map indicating which language-specific specifiers are in this 
     * set, and which are not.
     */
    protected boolean [] spec_list;
    /** 
     * the id naming the type - this attribute is set only for user-defined
     * types 
     */
    protected ScopedName type_name;

    /**
     * Adds a specifier to this set
     * @param specifier an integer representing a specific, language-specific
     * specifier
     */
    public void add (int specifier) { 
        spec_list [specifier] = true; 
    }

    /**
     * Adds the id representing the user-defined type of the entity being
     * declared.
     * @param name the type id
     */
    public void add_type_name (ScopedName name) { type_name = name; }

    /**
     * Gives the id of the user-defined type, or <code>null</code> if 
     * the associated entity declaration is for a builtin type
     */
    public ScopedName get_type_name () { return type_name; }

    /** 
     * Indicates whether the provided specifier is in this set.
     * @param specifier the language-specific identifier to test for
     * @return <code>true</code> if the specifier is in the set, 
     * <code>false</code> otherwise.
     */
    public boolean contains (int specifier) { return spec_list [specifier]; }

    /** 
     * Indicates whether the provided set is a subset of this set. 
     * <br>The <code>type_name</code> attribute is ignored.
     * @param specSet the set to compare against
     * @return <code>true</code> if the set is a subset, 
     * <code>false</code> otherwise.
     */
    public boolean contains (SpecifierSet specSet) {
        boolean hasAll = false;
        int specLength = specSet.spec_list.length;
        if (specLength <= spec_list.length) {
            int i = 0;
            for ( ; i < specLength; i++) 
                if (specSet.contains (i) && (! this.contains (i))) 
                    break;
            hasAll = (i == specLength);
        }
        return hasAll;
    }

}
