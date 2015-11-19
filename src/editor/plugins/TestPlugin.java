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
 * Created on 2006-10-25
 * project: FinalProject
 */
package editor.plugins;

import tm.utilities.Debug;
import editor.view.EditorViewBase;

/**
 * 
 *@author hao sun
 *@version $Revision: 1.1 $
 *@since version 1.1
 **/

public class TestPlugin extends EditorPlugin {

    public TestPlugin(EditorViewBase view) {
        super(view);
        // TODO Auto-generated constructor stub
    }

    @Override
    protected void performAction() {
        // TODO Auto-generated method stub
        Debug.getInstance().msg(Debug.EDITOR, "Plugin(test) activated");
        Debug.getInstance().msg(Debug.EDITOR, "view:"+view);
    }

}

