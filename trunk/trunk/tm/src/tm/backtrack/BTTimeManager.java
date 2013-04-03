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

public class BTTimeManager {
	boolean canRedo = false ;
	Transaction currentTransaction = new Transaction() ;
	ArrayList<Transaction> undoStack = new ArrayList<Transaction>() ;
	ArrayList<Transaction> redoStack = new ArrayList<Transaction>() ;
	
	public BTTimeManager( ) {
	}
	
	void noteBirth( BTVar<?> var) {
		if( canRedo ) { redoStack.clear() ; canRedo = false ; }
		currentTransaction.noteBirth( var ) ;
	}
	
	void noteDeath( BTVar<?> var) {
		if( canRedo ) { redoStack.clear() ; canRedo = false ; }
		currentTransaction.noteDeath( var ) ;
	}
	
	void noteUpdate( BTVar<?> var) {
		if( canRedo ) { redoStack.clear() ; canRedo = false ; }
		currentTransaction.noteUpdate(var) ;
	}
	
	void notePutByte( BTByteArray ba, int i ) {
		if( canRedo ) { redoStack.clear() ; canRedo = false ; }
		currentTransaction.notePutByte( ba, i ) ;
	}
	
    /** Checkpoint is used to establish a new checkpoint */
    public void checkpoint() {
    	checkpoint("") ;
    }
    
    /** Checkpoint is used to establish a new checkpoint */
    public void checkpoint(String description) { 
    	undoStack.add( currentTransaction ) ;
    	currentTransaction = new Transaction() ;
    }

    /** Undo is used to back up to the last checkpoint */
    public void undo() {
    	if( ! undoStack.isEmpty() ) {
    		currentTransaction.apply() ; 
    		redoStack.add( currentTransaction ) ; canRedo = true ; 
    		currentTransaction = undoStack.remove( undoStack.size() - 1 ) ; }
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
    	if( canRedo ) {
    		Assert.check( redoStack.size() > 0 ) ;
    		undoStack.add( currentTransaction ) ;
    		currentTransaction = redoStack.remove( redoStack.size() - 1 ) ;
    		currentTransaction.apply() ;
    		canRedo = redoStack.size() > 0 ; }
    }
}
