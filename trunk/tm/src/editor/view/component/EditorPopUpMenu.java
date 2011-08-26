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
 * Created on 2006-11-1
 * project: FinalProject
 */
package editor.view.component;

import javax.swing.JPopupMenu;

import editor.utility.EditorInitializeUtility;
import editor.view.EditorViewBase;

/**
 * All popup menus used in editor
 *@author hao sun
 *@version $Revision: 1.1 $
 *@since version 1.1
 **/

public class EditorPopUpMenu {
    /**
     * Main Frame PopUp Menu
     * @author hsun
     *
     */
    public class MainFramePopUpMenu extends JPopupMenu{
        private EditorViewBase mainFrameView;
        public MainFramePopUpMenu(EditorViewBase mainFrameView){
            super();
            this.mainFrameView=mainFrameView;
            createPopUpMenu();
        }
        
        private void createPopUpMenu(){
            ;
        }
    }
}

