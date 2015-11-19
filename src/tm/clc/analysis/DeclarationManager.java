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
 * Functions as the high-level interface to declaration building.<br>
 * Declaration building (at least in C++) involves building complete statements
 * (and sometimes sets of statements or entire functions). As such, it employs
 * the expression building logic in a less direct fashion than does its
 * counterpart, <code>ExpressionManager</code>. <br>
 * It also makes heavy use of both the compile time and runtime symbol tables. 
 */
public abstract class DeclarationManager {

    /** debugging comments this way */
    protected Debug dbg = Debug.getInstance ();

    /**
     * Standard sorry not implemented message for missing declaration categories
     */
    protected static final String SORRY_NOT_IMPLEMENTED = 
        "Sorry, {0} is not implemented";
    
    private static final String OUTERMOST_SCOPE = 
        "This operation cannot be performed because the parser is currently "
        + "in the outermost scope";

    /**
     * Contains the <code>CodeGenRule</code>s used by the expression building
     * logic.
     */
    protected RuleBase rb;

    /** 
     * Access the rulebase being used by the manager.
     * @return the <code>RuleBase</code>
     */
    public RuleBase rulebase () { return rb; } 


    /**
     * Keeps track of the number of non-static local variables available to a 
     * scope - this does not account for visibility issues like shadowing; 
     * it merely assists the runtime during program evaluation. <br>
     * It is not used during analysis at all.
     */
    public class VarCounter {
        private int staticTally = 0 ;
        private int localCount = 0 ;
        private int scopeCount = 0 ;
        private Vector stackedScopeCounts = new Vector ();
        // Invariant: localCount == scopeCount + sum( localTallies )

        /** Adds to the count of available variables */
        public void addLocal () {
            localCount += 1 ;
            scopeCount += 1 ;
        }

        /** Adds to the count of static variables */
        public void addStatic () {
            staticTally += 1 ; }

        /** Gives the count of available variables */	
        public int count () {
            return localCount ;
        }
        
        /** Indicates that a new local scope has been entered */	
        public void enterScope () { 
            stackedScopeCounts.addElement (new Integer (scopeCount));
            scopeCount = 0 ;
        }

        /** Indicates that a local scope has been exited */	
        public void exitScope () {
            int sz = stackedScopeCounts.size() ;
            Assert.apology (sz > 0, OUTERMOST_SCOPE);
            localCount -= scopeCount ;
            scopeCount = ((Integer) stackedScopeCounts.lastElement ()).intValue ();
            stackedScopeCounts.setSize( sz-1 );
        }
    }
}
