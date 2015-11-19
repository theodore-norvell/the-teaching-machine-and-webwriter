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

import tm.utilities.Assert;
import tm.utilities.Debug;

/**
 * Represents a set of <code>Declarations</code>, 
 * returned by name lookup routines.
 *
 * @created Aug 17 2001
 * @author Derek Reilly
 */

public class DeclarationSetMulti extends Vector implements DeclarationSet {

    /** is this set empty? */
    //    public boolean isEmpty () { return (size() == 0); }

    /**
     * Intended for use when expecting a single Declaration in the set 
     * having an associated ScopeHolder, a class for example.
     * <ul>An apology message will be generated when 
     * <li>the set is empty
     * <li>there are >1 elements in the set
     * <li>the single element is not a scope holder</ul>
     *
     * @return the associated ScopeHolder
     */
    public ScopeHolder getScopeHolder () { 
        Declaration d = getSingleMember();
        Assert.apology (d != null, "Empty set or multiple matches");
        Assert.apology (d.isScopeHolder (), 
                        "Single matching declaration is not a scope holder");

        return ((ScopeHolder) d.getDefinition ());
    }

    /**
     * Return the single element in the set. 
     *
     * @return the single Declaration in the set.
     */
    public Declaration getSingleMember () { 
        if (size() != 1) {
            // just return null, allowing this to be a check
            return null;
        }
        
        return (Declaration) elementAt (0);

    }		

    /**
     * Return the first element in the set (null if empty). Useful when 
     * checking properties that should be shared across elements
     *
     * @return the first Declaration in the set.
     */
    public Declaration getFirstMember () { return  (Declaration) elementAt (0); }

    

    /**
     * Concatenate elements from set passed in with existing elements.
     * Multiple references to same object are allowed (no check for 
     * element equivalence will occur)
     * @param ds the DeclarationSet whose elements will be appended
     */
    public void append (DeclarationSet ds) { 
        if (ds == null) Assert.apology ("Cannot append null set");
        if (ds instanceof Declaration) addElement (ds);
        else {
            if (ds == this) Assert.apology ("Cannot append self");
            for (Enumeration e = ((DeclarationSetMulti) ds).elements (); 
                 e.hasMoreElements (); 
                 addElement (e.nextElement ())) 
                ;
        }
    }

    /**
     * Merges (union) the elements of the DeclarationSet passed in with the 
     * current elements.
     * @param ds the DeclarationSet whose elements will be merged
     */
    public void merge (DeclarationSet ds) { 
        if (ds == null) Assert.apology ("Cannot merge null set");
        if (ds instanceof Declaration) {
            if (!this.contains (ds)) addElement (ds);
        } else if (ds != this) {
            for (Enumeration e = ((DeclarationSetMulti) ds).elements (); 
                 e.hasMoreElements (); ) {
                Object d = e.nextElement ();
                if (!this.contains (d)) {
                    addElement (d);
                }
            }
        }
    }

    public Declaration declaration (int i) { 
        return (Declaration) elementAt (i); 
    }

   public boolean contains (Declaration d) {
        return super.contains (d);
    }
    
    
    
    /** Used for debugging. Dumps the contents of the declaration to
     * System.out
     * 
     */    
    
    public void dumpContents(String indent, Debug d){
       for( int i=0 ; i<size() ; ++i )
            declaration(i).dumpContents(indent, d) ;
    }
}
