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

import editor.view.EditorViewBase;

/**
 * 
 *@author hao sun
 *@version $Revision: 1.1 $
 *@since version 1.1
 **/

public abstract class EditorPlugin {
    protected EditorViewBase view;
    
    public EditorPlugin(EditorViewBase view){
        this.view=view;
    }
    private boolean isActivated=true;
    
    /**
     * Main part for excuting
     *
     */
    protected abstract void performAction();
    
    /**
     * Activate current plugin,it can be modified by developer
     *
     */
    public void initialPlugin(){
        //Perform action
        performAction();
    }
    public void setView(EditorViewBase view){
        this.view=view;
    }

    /**
     * @return the isActivated
     */
    public boolean isActivated() {
        return isActivated;
    }

    /**
     * @param isActivated the isActivated to set
     */
    public void setActivated(boolean isActivated) {
        this.isActivated = isActivated;
    }
    
}

