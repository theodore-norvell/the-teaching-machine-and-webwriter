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
 * Created on 2006-11-15
 * project: FinalProject
 */
package editor.controller.actions;

import java.awt.Event;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.KeyStroke;

import editor.view.EditorViewBase;

/**
 * Multiple editor textpanel redo action
 * @author hao sun
 * @version $Revision: 1.1 $
 * @since version 1.1
 */

public class MutipleEditorRedoAction extends AbstractAction {

    private EditorViewBase view;

    public MutipleEditorRedoAction(EditorViewBase view) {
        this.view = view;
    }

    public void actionPerformed(ActionEvent e) {
        int currentIndex = view.getCurrentActiveEditorIndex();
        KeyStroke key=KeyStroke.getKeyStroke(KeyEvent.VK_Y, Event.CTRL_MASK);  //redo
        if (view.getTextPanel(currentIndex) != null) {
            if (((AbstractAction)(view.getTextPanel(currentIndex).getInputMap().get(key))).isEnabled())
                ((AbstractAction)(view.getTextPanel(currentIndex).getInputMap().get(key))).actionPerformed(e);
        }else
            view.setStatusString("RedoAction is null");
    }
}
