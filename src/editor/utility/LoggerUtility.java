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
 * Created on 2006-9-28
 * project: FinalProject
 */
package editor.utility;

import tm.utilities.Debug;
import editor.view.EditorViewBase;

/**
 * Logger Utility,make log info on view
 *@author hao sun
 *@version $Revision: 1.1 $
 *@since version 1.1
 **/

public class LoggerUtility {
    
    /**
     * Output log message on console
     * @param message
     */
    public static void logOnConsole(String message){
        //out put to console in system for temporary
        Debug.getInstance().msg(Debug.EDITOR, message);
    }
    
    /**
     * Output log message on view console textarea
     * @param message
     */
    public static void logOnEditorView(EditorViewBase view,String message){
        view.addConsoleString(message);
    }
}

