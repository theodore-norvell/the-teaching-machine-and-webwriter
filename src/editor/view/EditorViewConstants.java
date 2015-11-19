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
package editor.view;

import java.awt.Event;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.util.HashMap;

import javax.swing.KeyStroke;

/**
 * Common constants used by view package
 *@author hao sun
 *@version $Revision: 1.1 $
 *@since version 1.1
 **/

public interface EditorViewConstants {

    //Default setting
    public static int DEFAULT_FRAME_WIDTH=700;
    public static int DEFAULT_FRAME_HEIGHT=600;
    public static int DEFAULT_EDITOR_WIDTH=800;
    public static int DEFAULT_EDITOR_HEIGHT=600;
    
    public static boolean HAS_CONSOLE=true;
    public static int DEFAULT_EDITPANELLEFTLABEL_WIDTH=5;
    
    public static int DEFAULT_TABKEYINCREMENT=2;
    
    //Editor font property
    public static Font DEFAULT_EDIT_FONT=new Font(null,Font.PLAIN,12);
    
    //Key mapping
    public static KeyStroke EDITOR_UNDO_KEY=KeyStroke.getKeyStroke(KeyEvent.VK_Z, Event.CTRL_MASK);
    public static KeyStroke EDITOR_REDO_KEY=KeyStroke.getKeyStroke(KeyEvent.VK_Y, Event.CTRL_MASK);
    
    public static HashMap<KeyStroke,Object> KEY_MAP=new HashMap<KeyStroke, Object>();
    
    //Filter options
    public static boolean AUTO_INDENTATION_ENABLED=true;
    
    public static int TAB_INDENTATION=2;
   
}

