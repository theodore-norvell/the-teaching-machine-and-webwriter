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
 * Created on 2006-9-30
 * project: FinalProject
 */
package editor.controller.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

import editor.controller.DocumentStyleHandler;
import editor.view.EditorViewBase;
import editor.view.dialog.EditorAboutDialog;
import editor.view.dialog.NewFileDialog;

/**
 * Menu Bar Actions
 * @author hao sun
 * @version $Revision: 1.1 $
 * @since version 1.1
 */

public class MenuBarActions {
    /**
     * Make reaction to the menu("File->New") open a dialog in the main frame
     * @author hsun
     *
     */
    public static class OpenNewFileDialogAction extends AbstractAction {

        private static final long serialVersionUID = 1L;

        private EditorViewBase editorView;

        private NewFileDialog newFileDialog = null;

        public OpenNewFileDialogAction(String text, EditorViewBase editorView) {
            super(text);
            this.editorView = editorView;
            // TODO
        }

        public void actionPerformed(ActionEvent e) {
            newFileDialog = new NewFileDialog(editorView);
            javax.swing.SwingUtilities.invokeLater(newFileDialog);
        }
    }

    /**
     * Make reaction to the menu("Help->About") open a dialog in the main frame
     * @author hsun
     *
     */
    public static class OpenEditorAboutAction extends AbstractAction {

        private static final long serialVersionUID = 1L;

        private EditorViewBase editorView;

        private EditorAboutDialog editorAboutDialog = null;

        public OpenEditorAboutAction(String text, EditorViewBase editorView) {
            super(text);
            this.editorView = editorView;
        }

        public void actionPerformed(ActionEvent e) {
            editorAboutDialog = new EditorAboutDialog(editorView);
            javax.swing.SwingUtilities.invokeLater(editorAboutDialog);
        }
    }
    
    /**
     * Save text oanel action
     * @author hsun
     *
     */
    public static class SaveTextPanelAction extends AbstractAction {

        private static final long serialVersionUID = 1L;
        
        private EditorViewBase editorView;
        
        /**
         * Construction
         * @param text
         * @param editorView
         */
        public SaveTextPanelAction(String text, EditorViewBase editorView){
            super(text);
            this.editorView = editorView;
        }
        
        /**
         * Perform action
         */
        public void actionPerformed(ActionEvent e) {
            if(editorView==null||editorView.getCurrentActiveEditorIndex()<0)
                return;
            editorView.saveCurrentDocument();
            DocumentStyleHandler docHandler=new DocumentStyleHandler(editorView.getModel().getDocument(editorView.getCurrentDocIndex()));
            int[] position={0,editorView.getModel().getDocument(editorView.getCurrentDocIndex()).getDocument().getLength()};
            docHandler.run(position);
            
            editorView.setStatusString("file saved");
        }
        
    }
    
    /**
     * Close TextPanel Action
     * @author hsun
     *
     */
    public static class CloseTextPanelAction extends AbstractAction {

        private static final long serialVersionUID = 1L;

        private EditorViewBase editorView;
        
        public CloseTextPanelAction(String text, EditorViewBase editorView){
            super(text);
            this.editorView = editorView;
        }
        
        public void actionPerformed(ActionEvent e) {
            if(editorView.getCurrentActiveEditorIndex()<0)
                return;
//          Custom button text
            Object[] options = {"Yes, please",
                                "No, thanks",
                                "canel"};
            int n = JOptionPane.showOptionDialog(editorView,
                "Would you like to  "
                + "save the file?",
                "Save confirmation",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[2]);
            switch(n){
            case 0:
                editorView.saveCurrentDocument();
                editorView.closeCurrentTextPanel();
                break;
            case 1:
                editorView.closeCurrentTextPanel();
                break;
            default:
                break;
            }

        }
        
    }
}
