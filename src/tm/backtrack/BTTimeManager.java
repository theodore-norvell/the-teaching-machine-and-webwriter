//     Copyright 1998--2013 Michael Bruce-Lockhart and Theodore S. Norvell
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

import java.util.ArrayList;

import tm.utilities.Assert;
import tm.utilities.Debug;

public class BTTimeManager {
	boolean canRedo = false ;
	Transaction currentTransaction = new Transaction() ;
	private ArrayList<Transaction> undoStack = new ArrayList<Transaction>() ;
	private ArrayList<Transaction> redoStack = new ArrayList<Transaction>() ;
	
	/**/private boolean protect = false ;/**/
	
	public BTTimeManager( ) {
	}
	
	public void turnOnProtection() {
		if( !protect ) protect = true ;
		else {
			/**/Debug.getInstance().msg(Debug.BACKTRACK, "BT Attempt to protect when already protected" ) ; /**/
		}
	}
	
	public void turnOffProtection() {
		if( protect ) protect = false ;
		else {
			/**/Debug.getInstance().msg(Debug.BACKTRACK, "BT Attempt to turn off protectio when already off" ) ; /**/
		}
	}
	
	private void checkProtection() {
		if( protect ) {
			/**/Debug.getInstance().msg(Debug.BACKTRACK, "BT Change to transactional memory during protected time. " ) ; /**/
			/**/Debug.getInstance().msg(Debug.BACKTRACK, new Throwable("Protection Violation") ) ; /**/
		}
	}
	
	void noteBirth( BTVar<?> var) {
    	/**/Debug.getInstance().msg(Debug.BACKTRACK, ">>BTTimeManager.noteBirth()" ) ; /**/
    	/**/Debug.getInstance().msg(Debug.BACKTRACK, "  undo size = " +undoStack.size()+
    			"  redo size = " +redoStack.size()+
    			"  canRedo = " +canRedo) ; /**/
    	checkProtection() ;
		if( canRedo ) { redoStack.clear() ; canRedo = false ; }
		currentTransaction.noteBirth( var ) ;
    	/**/Debug.getInstance().msg(Debug.BACKTRACK, "  undo size = " +undoStack.size()+
    			"  redo size = " +redoStack.size()+
    			"  canRedo = " +canRedo) ; /**/
    	/**/Debug.getInstance().msg(Debug.BACKTRACK, "<<BTTimeManager.noteBirth()" ) ; /**/
	}
	
	void noteDeath( BTVar<?> var) {
    	/**/Debug.getInstance().msg(Debug.BACKTRACK, ">>BTTimeManager.noteDeath()" ) ; /**/
    	/**/Debug.getInstance().msg(Debug.BACKTRACK, "  undo size = " +undoStack.size()+
    			"  redo size = " +redoStack.size()+
    			"  canRedo = " +canRedo) ; /**/
    	checkProtection() ;
		if( canRedo ) { redoStack.clear() ; canRedo = false ; }
		currentTransaction.noteDeath( var ) ;
    	/**/Debug.getInstance().msg(Debug.BACKTRACK, "  undo size = " +undoStack.size()+
    			"  redo size = " +redoStack.size()+
    			"  canRedo = " +canRedo) ; /**/
    	/**/Debug.getInstance().msg(Debug.BACKTRACK, "<<BTTimeManager.noteDeath()" ) ; /**/
	}
	
	void noteUpdate( BTVar<?> var) {
    	/**/Debug.getInstance().msg(Debug.BACKTRACK, ">>BTTimeManager.noteUpdate()" ) ; /**/
    	/**/Debug.getInstance().msg(Debug.BACKTRACK, "  undo size = " +undoStack.size()+
    			"  redo size = " +redoStack.size()+
    			"  canRedo = " +canRedo) ; /**/
    	checkProtection() ;
		if( canRedo ) { redoStack.clear() ; canRedo = false ; }
		currentTransaction.noteUpdate(var) ;
    	/**/Debug.getInstance().msg(Debug.BACKTRACK, "  undo size = " +undoStack.size()+
    			"  redo size = " +redoStack.size()+
    			"  canRedo = " +canRedo) ; /**/
    	/**/Debug.getInstance().msg(Debug.BACKTRACK, "<<BTTimeManager.noteUpdate()" ) ; /**/
	}
	
	void notePutByte( BTByteArray ba, int i ) {
    	/**/Debug.getInstance().msg(Debug.BACKTRACK, ">>BTTimeManager.noteBytePut()" ) ; /**/
    	/**/Debug.getInstance().msg(Debug.BACKTRACK, "  undo size = " +undoStack.size()+
    			"  redo size = " +redoStack.size()+
    			"  canRedo = " +canRedo) ; /**/
    	checkProtection() ;
		if( canRedo ) { redoStack.clear() ; canRedo = false ; }
		currentTransaction.notePutByte( ba, i ) ;
    	/**/Debug.getInstance().msg(Debug.BACKTRACK, "  undo size = " +undoStack.size()+
    			"  redo size = " +redoStack.size()+
    			"  canRedo = " +canRedo) ; /**/
    	/**/Debug.getInstance().msg(Debug.BACKTRACK, "<<BTTimeManager.noteBytePut()" ) ; /**/
	}
	
    /** Checkpoint is used to establish a new checkpoint */
    public void checkpoint() {
    	checkpoint("") ;
    }
    
    /** Checkpoint is used to establish a new checkpoint */
    public void checkpoint(String description) { 
    	/**/Debug.getInstance().msg(Debug.BACKTRACK, ">>BTTimeManager.checkPoint()" ) ; /**/
    	/**/Debug.getInstance().msg(Debug.BACKTRACK, "  undo size = " +undoStack.size()+
    			"  redo size = " +redoStack.size()+
    			"  canRedo = " +canRedo) ; /**/
    	undoStack.add( currentTransaction ) ;
    	currentTransaction = new Transaction() ;
    	/**/Debug.getInstance().msg(Debug.BACKTRACK, "  undo size = " +undoStack.size()+
    			"  redo size = " +redoStack.size()+
    			"  canRedo = " +canRedo) ; /**/
    	/**/Debug.getInstance().msg(Debug.BACKTRACK, "<<BTTimeManager.checkPoint()" ) ; /**/
    }

    /** Undo is used to back up to the last checkpoint */
    public void undo() {
    	/**/Debug.getInstance().msg(Debug.BACKTRACK, ">>BTTimeManager.undo()" ) ; /**/
    	/**/Debug.getInstance().msg(Debug.BACKTRACK, "  undo size = " +undoStack.size()+
    			"  redo size = " +redoStack.size()+
    			"  canRedo = " +canRedo) ; /**/
    	if( ! undoStack.isEmpty() ) {
    		currentTransaction.apply() ; 
    		redoStack.add( currentTransaction ) ; canRedo = true ; 
    		currentTransaction = undoStack.remove( undoStack.size() - 1 ) ; }
    	/**/Debug.getInstance().msg(Debug.BACKTRACK, "  undo size = " +undoStack.size()+
    			"  redo size = " +redoStack.size()+
    			"  canRedo = " +canRedo) ; /**/
    	/**/Debug.getInstance().msg(Debug.BACKTRACK, "<<BTTimeManager.undo()" ) ; /**/
    }
    
    public String getCurrentDescription(){
    	Assert.toBeDone() ;
    	return null ;
    }
    
    public boolean canUndo() { 
    	return  ! undoStack.isEmpty() ;
    }
    
    public boolean canRedo() { return canRedo ; }
    
    public void redo() {
    	/**/Debug.getInstance().msg(Debug.BACKTRACK, ">>BTTimeManager.redo()" ) ; /**/
    	/**/Debug.getInstance().msg(Debug.BACKTRACK, "  undo size = " +undoStack.size()+
    			"  redo size = " +redoStack.size()+
    			"  canRedo = " +canRedo) ; /**/
    	if( canRedo ) {
    		Assert.check( redoStack.size() > 0 ) ;
    		undoStack.add( currentTransaction ) ;
    		currentTransaction = redoStack.remove( redoStack.size() - 1 ) ;
    		currentTransaction.apply() ;
    		canRedo = redoStack.size() > 0 ; }
    	/**/Debug.getInstance().msg(Debug.BACKTRACK, "  undo size = " +undoStack.size()+
    			"  redo size = " +redoStack.size()+
    			"  canRedo = " +canRedo) ; /**/
    }
}
