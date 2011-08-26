//     Copyright 2007 Hao Sun
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

/*
 * Created on 2006-11-7
 * project: FinalProject
 */
package editor.controller.listeners;

import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;

import editor.model.DocumentBase;

/**
 * Editor undoable edit lister,snoop the state of unable event
 *@author hao sun
 *@version $Revision: 1.1 $
 *@since version 1.1
 **/

public class EdtiorUndoableEditListener  implements UndoableEditListener {

    private DocumentBase documentModel;
    /**
     * Forbit to construct without view
     *
     */
    private EdtiorUndoableEditListener(){
        //No code here
    }
    
    public EdtiorUndoableEditListener(DocumentBase documentModel){
        this.documentModel=documentModel;
    }
    
    private boolean undoableEditCanHappened=true;
    
    /**
     * Perfrom action when undoable event happened
     */
    public void undoableEditHappened(UndoableEditEvent e) {
        //Remember the edit and update the menus.
        if(!undoableEditCanHappened){
            return;
        }
        documentModel.getUndoManager().addEdit(e.getEdit());
        documentModel.getUndoAction().updateUndoState();
        documentModel.getRedoAction().updateRedoState();
    }
    /**
     * Set can undo or not 
     * @param canUndo
     */
    public void setCanUndo(boolean canUndo){
        undoableEditCanHappened=canUndo;
    }
}

