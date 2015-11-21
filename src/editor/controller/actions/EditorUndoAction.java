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
package editor.controller.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;

import tm.utilities.Debug;

import editor.model.DocumentBase;
import editor.utility.LoggerUtility;
import editor.view.EditorViewBase;

/**
 * Editor Undo Action
 *@author hao sun
 *@version $Revision: 1.1 $
 *@since version 1.1
 **/

public class EditorUndoAction extends AbstractAction {

//    private EditorViewBase view;
//    private String docName;
    private DocumentBase doc;
//    private  undoManager;
    
    private EditorUndoAction(){
        //No code here
    }
    /**
     * Constructor for undo action
     *
     */
    public EditorUndoAction(DocumentBase doc){
        super("Undo");
//        this.view=view;
//        this.docName=docName;
        this.doc=doc;
        //Set defualt is false cause no action performed when editor is initilized
        setEnabled(false);
    }
    
    public void actionPerformed(ActionEvent e) {
        try {
            doc.getUndoManager().undo();  //undo
        } catch (CannotUndoException ex) {
            Debug.getInstance().msg(Debug.EDITOR, "Unable to undo: " + ex);
            ex.printStackTrace();
        }
        updateUndoState();
        doc.getRedoAction().updateRedoState();
    }
    
    /**
     * update Undo State
     *
     */
    public void updateUndoState() {
        if (doc.getUndoManager().canUndo()) {
            setEnabled(true);
            putValue(Action.NAME, doc.getUndoManager().getUndoPresentationName());
//            LoggerUtility.logOnConsole(Action.NAME+"   "+doc.getUndoManager().getUndoPresentationName());
        } else {
            setEnabled(false);
            putValue(Action.NAME, "Undo");
        }
    }
}

