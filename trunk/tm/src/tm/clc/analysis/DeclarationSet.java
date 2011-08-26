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

import java.util.*;

import tm.utilities.Debug;


/**
 * Represents a set of Declarations, returned by name lookup routines.
 * It facilitates several cases of result (0, 1 or many).
 *
 * @created Aug 17 2001
 * @author Derek Reilly
 */

public interface DeclarationSet {

    /** is this set empty? */
    public boolean isEmpty () ; 

    /**
     * Intended for use when expecting a single Declaration in the set 
     * having an associated ScopeHolder, a class for example.
     *
     * @return the associated ScopeHolder
     */
    public ScopeHolder getScopeHolder () ;

    /**
     * Return the single element in the set. Will return null if there are 0 or
     * many elements.
     *
     * @return the single Declaration in the set.
     */
    public Declaration getSingleMember () ;

    /**
     * Return the first element in the set (null if empty). Useful when 
     * checking properties that should be shared across elements
     *
     * @return the first Declaration in the set.
     */
    public Declaration getFirstMember () ;

    /**
     * Concatenate elements from set passed in with existing elements.
     * Multiple references to same object are allowed (no check for 
     * element equivalence will occur)
     * @param ds the DeclarationSet whose elements will be appended
     */
    public void append (DeclarationSet ds) ;

    /**
     * Merges (union) the elements of the DeclarationSet passed in with the 
     * current elements.
     * @param ds the DeclarationSet whose elements will be merged
     */
    public void merge (DeclarationSet ds) ; 

    /**
     * Provides an enumeration over the elements of the set
     * @return an <code>Enumeration</code> over the set's elements
     */
    public Enumeration elements () ;

    /** 
     * Gives the number of elements in the set
     * @return the number of elements
     */
    public int size () ;

    /**
     * Indicates whether the provided declaration is an element in the set
     */
    public boolean contains (Declaration d) ;
    
    /**
     * Returns the <code>Declaration</code> at the indicated position
     * @param i the position of the desired element
     * @return the <code>Declaration</code> at <code>i</code>
     */
    public Declaration declaration (int i) ;
    
   /** Used for debugging. Dumps the contents of the declaration to
     * System.out
     * 
     */
    public void dumpContents(String indent, Debug d);

}
