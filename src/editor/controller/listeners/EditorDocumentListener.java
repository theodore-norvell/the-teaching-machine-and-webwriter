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
 * Created on 2006-10-9
 * project: FinalProject
 */
package editor.controller.listeners;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import editor.controller.DocumentStyleHandler;
import editor.model.DocumentBase;
import editor.view.EditorViewBase;

/**
 * Editor's document listener,snooping the event and take reaction for the updating
 *@author hao sun
 *@version $Revision: 1.1 $
 *@since version 1.1
 **/

public class EditorDocumentListener implements DocumentListener{

    private EditorViewBase editorView=null;
    
    private DocumentBase doc=null;
    
    private DocumentStyleHandler docHandler;
    
    private EditorDocumentListener(){
        //No code here
    }
    
    public EditorDocumentListener(EditorViewBase editorView,DocumentBase doc){
        this.editorView=editorView;
        this.doc=doc;
        docHandler=new DocumentStyleHandler(doc);
        
    }
    public void changedUpdate(DocumentEvent e) {
//        LoggerUtility.logOnConsole("change");
    }

    public void insertUpdate(DocumentEvent e) {
        commonUpdate();
    }

    public void removeUpdate(DocumentEvent e) {
        commonUpdate();
    }
    
    /**
     * Common update issues without respecting document updating type
     *
     */
    private void commonUpdate(){
        
        //Colorize the document
        docHandler.run();
    }
}

