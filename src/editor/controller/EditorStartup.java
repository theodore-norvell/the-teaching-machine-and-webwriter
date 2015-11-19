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
package editor.controller;

import editor.model.IEditorModel;
import editor.utility.LoggerUtility;
import editor.view.EditorViewBase;
import editor.view.frame.EditorView;

/**
 * 
 *@author hao sun
 *@version $Revision: 1.1 $
 *@since version 1.1
 **/

public class EditorStartup {
    private EditorViewBase view=null;
    
    public EditorStartup(){
        view=new EditorView();
    }
    
    public EditorStartup(IEditorModel model){
        view=new EditorView(model);
    }
    
    public EditorStartup(EditorViewBase view){
        this.view=view;
    }
    
    /**
     * Initialize the editor and run 
     *
     */
    public void init(){
        if(view==null)
            throw new Error("There does not have an concret view!!");
        //TODO
        view.initialize();
        LoggerUtility.logOnConsole("Initializing the editor");
        //Check has plugin or not
        //TODO
//        PluginContainer.performPlugins(view);
        //Run view
        view.start();
        LoggerUtility.logOnEditorView(view, "Initializing finished!");
    }
    
    /**
     * Stop the editor
     *
     */
    public void stop(){
        //System.out.println("Stop the editor");
        if(view!=null)
            view.stop();
        else
            throw new Error("There does not have an concret view!!");
    }
    
    public void setModel(IEditorModel model){
        view.setModel(model);
    }
    
    public EditorViewBase getView(){
        return view;
    }
}

