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
 * Created on 2006-11-10
 * project: FinalProject
 */
package editor.controller.listeners;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import editor.utility.LoggerUtility;
import editor.view.EditorViewBase;
import editor.view.component.EditorTabbedTextPanel;

/**
 * TabbedPane Change Listener
 *@author hao sun
 *@version $Revision: 1.1 $
 *@since version 1.1
 **/

public class TabbedPaneChangeListener implements ChangeListener {

    private EditorViewBase view;
    private int currentSelectedIndex=0;
    
    /**
     * Forbbit constructing without params
     *
     */
    private TabbedPaneChangeListener(){
        //No code here
    }
    
    /**
     * Constructing listener by known view
     * @param view
     */
    public TabbedPaneChangeListener(EditorViewBase view){
        this.view=view;
    }
    
    public void stateChanged(ChangeEvent e) {
        try{
            EditorTabbedTextPanel container=(EditorTabbedTextPanel)e.getSource();
            if(container.getSelectedIndex()!=currentSelectedIndex){
                //Call onTabChanged() event
                onTabChanged();
                
                //Update currentSelectedIndex
                currentSelectedIndex=container.getSelectedIndex();
            }
            //Update menu bar
            //TODO
            
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    /**
     * Event for tab changed
     *
     */
    private void onTabChanged(){
        //Update view menu bar
//        updateViewMenuBar();
    }
    
    private void updateViewMenuBar(){
//        view.updateMenu();
    }
}

