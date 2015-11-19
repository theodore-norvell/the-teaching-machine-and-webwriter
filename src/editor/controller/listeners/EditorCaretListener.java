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
 * Created on 2006-10-16
 * project: FinalProject
 */
package editor.controller.listeners;

import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.BadLocationException;

import editor.model.DocumentBase;
import editor.utility.DocumentUtility;
import editor.view.EditorViewBase;

/**
 * Editor Caret Listener
 *@author hao sun
 *@version $Revision: 1.1 $
 *@since version 1.1
 **/

public class EditorCaretListener implements CaretListener {

    private EditorViewBase editorView=null;
    
    private DocumentBase doc=null;
    
    private EditorCaretListener(){
//      No code here
    }
    /**
     * Costructor with known view and model
     * @param editorView
     * @param doc
     */
    public EditorCaretListener(EditorViewBase editorView,DocumentBase doc){
        this.editorView=editorView;
        this.doc=doc;
    }
    
    /**
     * Update caret
     */
    public void caretUpdate(CaretEvent e) {
        int[] rowAndColumn={1,1};
        try {
            rowAndColumn = DocumentUtility.getRowAndColumnByOffset(doc.getDocument().getText(0, doc.getDocument().getLength()), e.getDot());
       
        
        //back up the position
        doc.setLastCaretStartPosition(doc.getCurrentCaretStartPostion());
        doc.setLastCaretEndPosition(doc.getCurrentCaretEndPostion());
        
        //set new position
        if(e.getDot()<e.getMark()){
            doc.setCurrentCaretStartPostion(e.getDot());
            doc.setCurrentCaretEndPostion(e.getMark());
        }else{
            doc.setCurrentCaretStartPostion(e.getMark());
            doc.setCurrentCaretEndPostion(e.getDot());
        }
        
        //Check block selected or not
        if(e.getDot()!=e.getMark()){
            //block selected
            int[] selectedRowAndColumn=DocumentUtility.getRowAndColumnByOffset(doc.getDocument().getText(0, doc.getDocument().getLength()), e.getMark());

            editorView.setStatusString("caret selected from:"+selectedRowAndColumn[0]+" "+selectedRowAndColumn[1]+" to "+rowAndColumn[0]+" "+rowAndColumn[1]);
        }else{
            //non-block selected
            editorView.setStatusString("caret:"+rowAndColumn[0]+" "+rowAndColumn[1]);
        } 
        } catch (BadLocationException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }
}

