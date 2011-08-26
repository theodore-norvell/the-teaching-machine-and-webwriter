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
 * Created on 2006-9-27
 * project: FinalProject
 */
package editor.view;

import javax.swing.JFrame;
import javax.swing.undo.UndoManager;

import editor.controller.actions.EditorRedoAction;
import editor.controller.actions.EditorUndoAction;
import editor.model.DocumentBase;
import editor.model.IEditorModel;
import editor.model.DocumentBase.Type;
import editor.utility.EditState;
import editor.view.component.TextPane;

/**
 * A view abstract class worked with a editor's model interface.
 * It offers all basic methods invoked by controller or model package
 * 
 * @author hao sun
 * @version $Revision: 1.1 $
 * @since version 1.1
 */

abstract public class EditorViewBase extends JFrame implements EditorViewConstants,Runnable{
    
    
    /**
     * Create and build the GUI for the editor
     * invoked by the model or controller
     */
    public abstract void createAndShowUI();
    
    /**
     * Create UI
     *
     */
    public abstract void initialize();
    
    /**
     * Set model
     * @param editor
     */
    public abstract void setModel(IEditorModel editor);
    
    /**
     * Get model for this view
     * @return editor model
     */
    public abstract IEditorModel getModel( );
    /**
     * Start the Editor
     *
     */
    public void start(){
        javax.swing.SwingUtilities.invokeLater(this);
    }
    
    /**
     * Stop the Editor
     * 
     */
    public void stop(){
        this.dispose();
    }
    
    /**
     * create and show the UI
     * 
     */
    public void run(){
        createAndShowUI();
    }
    
    /**
     * Add doc to the view
     * @param doc document model
     */
    public abstract void addDocumentView(DocumentBase doc);

    /**
     * Make new document
     *
     */
    public void makeNewDocumentView(){
        if(getModel()!=null){
            getModel().addDocumentToEditor(null, EditState.NEW,Type.JAVA);
        }
    }
    
    /**
     * Set message onto the status bar 
     * @param status
     */
    public abstract void setStatusString(String status);
    
    /**
     * Set message onto the console textarea 
     * @param status
     */
    public abstract void setConsoleString(String message);
    
    /**
     * Add message onto the console textarea 
     * @param status
     */
    public abstract void addConsoleString(String message);
    
    /**
     * 
     * @param name
     * @return
     */
    public abstract TextPane getTextPanel(int docIndex);
    
    /**
     * Get undo action 
     * @return undo action
     */
    public abstract EditorUndoAction getUndoAction(int docIndex);
    
    /**
     * Get redo action 
     * @return redo action
     */
    public abstract EditorRedoAction getRedoAction(int docIndex);
    
    /**
     * Get undo manager
     * @return
     */
    public abstract UndoManager getUndoManager(int docIndex);
    
    /**
     * Get current active textpanel index
     * @return index
     */
    public abstract int getCurrentActiveEditorIndex();
    
    /**
     * Get current active Document index
     * @return
     */
    public abstract int getCurrentDocIndex();
    
    /**
     * Update main menu when tabbed panel state changed
     *
     */
    public abstract void updateMenu();
    
    /**
     * save Current Document
     *
     */
    public abstract void saveCurrentDocument();
    
    /**
     * close Current TextPanel
     *
     */
    public abstract void closeCurrentTextPanel();
}
