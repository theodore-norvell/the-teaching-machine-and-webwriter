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
 * Created on 2006-11-20
 * project: FinalProject
 */
package editor;

import tm.interfaces.EditorPIFactoryInterface;
import tm.interfaces.EditorPIInterface;
import tm.plugins.Requirement;

/**
 * Editor Plugin Factory,create a factory to generate the editor plugin
 *@author hao sun
 *@version $Revision: 1.1 $
 *@since version 1.1
 **/

public class EditorPIFactory implements EditorPIFactoryInterface {

    private static EditorPIInterface editorPI=null;
    
    /**
     * Create editor
     */
    public EditorPIInterface createEditor() {
        if(editorPI==null)
            editorPI=new PIEditor();
        return editorPI;
    }

    /**
     * Get requirements
     */
    public Requirement[] getRequirements() {
        // TODO Auto-generated method stub
        return null;
    }
    
    /**
     * Create editor factory
     * @param string
     * @return
     */
    public static EditorPIFactoryInterface createInstance(String string){
        return new EditorPIFactory();
    }
}

