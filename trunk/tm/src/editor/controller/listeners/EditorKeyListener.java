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
 * Created on 2006-11-9
 * project: FinalProject
 */
package editor.controller.listeners;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JTextPane;
import javax.swing.KeyStroke;

import editor.model.DocumentBase;
import editor.utility.LoggerUtility;
import editor.view.EditorViewBase;
import editor.view.EditorViewConstants;

/**
 * Editor key listener for inputting of textpanels
 *@author hao sun
 *@version $Revision: 1.1 $
 *@since version 1.1
 **/

public class EditorKeyListener implements KeyListener,EditorViewConstants{

    private DocumentBase doc;
    private JTextPane panel;
    
    private int tempKeyCode=0;
    
    /***
     * Fobbit to construct without params
     *
     */
    private EditorKeyListener(){
        
    }
    
    /**
     * Editor Key Listener constructor
     * @param view
     * @param docName
     */
    public EditorKeyListener(EditorViewBase view,int docIndex){
        //Find Textpane and document for this key listener
        panel=view.getTextPanel(docIndex);
        doc=view.getModel().getDocument(docIndex);
    }
    public void keyPressed(KeyEvent e) {
        //Save the keycode when here has the modifiers
        if(e.getModifiers()!=0)
            tempKeyCode=e.getKeyCode();
    }

    public void keyReleased(KeyEvent e) {
    }

    public void keyTyped(KeyEvent e) {
        KeyStroke currentKeyStroke=null;
        //Get current key
        if(e.getModifiers()==0)  //No Modifiers
            currentKeyStroke=KeyStroke.getKeyStroke(e.getKeyChar());
        else{
            currentKeyStroke=KeyStroke.getKeyStroke(tempKeyCode, e.getModifiers());
            tempKeyCode=0;
        }
        if(doc.getKeyFilters()!=null){
            //Perform Filters
            for(int i=0;i<doc.getKeyFilters().size();i++){
                boolean isMatched=doc.getKeyFilters().get(i).performFilter(currentKeyStroke);
//                LoggerUtility.logOnConsole(""+isMatched);
            }
        }
    }

}

