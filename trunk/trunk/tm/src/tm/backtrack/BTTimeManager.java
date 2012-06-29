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

package tm.backtrack;

import java.util.Vector ;

/** A manager for checkpointing. All objects that are to be
    checkpointed together must share the same manager.
    The manager is consulted when an object needs to know
    the current time.
 */
public class BTTimeManager {
    private BTTime currentTime ; // The current time line.
    
    private Vector<Backtrackable> registrants ; // Registered objects

    public BTTimeManager( ) {
        currentTime = new BTTime( null, "none" ) ;
        registrants = new Vector<Backtrackable>(1) ;
    }

    public void register( Backtrackable btObj ) {
        registrants.addElement( btObj ) ; }
        
    /** Checkpoint is used to establish a new checkpoint */
    public void checkpoint() {
	checkpoint( "" ) ;
    }
    
    /** Checkpoint is used to establish a new checkpoint */
    public void checkpoint(String description) {
	currentTime = new BTTime( currentTime, description ) ;

	for( int i=0, sz = registrants.size() ; i<sz ; ++i ) {
	    Backtrackable registrant = registrants.elementAt(i) ;
	    registrant.checkpoint() ; }
    }

    /** Undo is used to back up to the last checkpoint */
    public void undo() {
        if( currentTime.prev != null ) {
            currentTime = currentTime.prev ;
        
            for( int i=0, sz = registrants.size() ; i<sz ; ++i ) {
                Backtrackable registrant = registrants.elementAt(i) ;
                registrant.undo() ; } }
    }
    
    public String getCurrentDescription(){return currentTime.description; }
    
    public boolean canUndo() { return currentTime.epoch != 0 ; }

    BTTime getCurrentTime() {
        return currentTime ;
    }
    
}