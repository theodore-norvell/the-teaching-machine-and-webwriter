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

import java.io.IOException;

import tm.interfaces.EditorPIInterface;
import tm.utilities.Debug;
import tm.utilities.TMFile;
import editor.controller.EditorStartup;
import editor.model.DocumentBase;
import editor.model.EditorModel;
import editor.model.IEditorModel;
import editor.model.DocumentBase.Type;

/**
 * Editor plugin
 *@author hao sun
 *@version $Revision: 1.1 $
 *@since version 1.1
 **/

public class PIEditor implements EditorPIInterface {

    private static EditorStartup mainUI=null;
    private IEditorModel model=null;
    
    /**
     * Start editing file by given file
     */
    public void startEditingFile(TMFile arg0) {
        if(arg0==null)
            throw new Error("no file sended in!!");
        if(mainUI==null){
            mainUI=new EditorStartup();
        }else{
            mainUI.getView().start();
        }
        //Get code file type
        String fileExtension=arg0.getFileName().substring(arg0.getFileName().lastIndexOf(".")+1);
        Type docType=Type.ETC;
        if(fileExtension.equalsIgnoreCase("java")){
            //Set Java Type
            docType=Type.JAVA;
        }else if(fileExtension.equalsIgnoreCase("cpp")){
            //Set Cpp type
            docType=Type.CPP;
        }
        
        String fileContent="";
        try {
            fileContent = arg0.readFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(model==null){
            model=new EditorModel();
            DocumentBase tempDoc=model.getDocumentBase(arg0.getFileName(), "",docType);
            tempDoc.setFile(arg0);
            Debug.getInstance().msg(Debug.EDITOR, docType.toString());
//            tempDoc.setType(docType);
            model.addDocument(tempDoc);
            mainUI.setModel(model);
            mainUI.init();
            //Do not save into Undo manage action
            mainUI.getView().getModel().getDocument(mainUI.getView().getCurrentDocIndex()).setCanUndo(false);
            mainUI.getView().getModel().getDocument(mainUI.getView().getCurrentDocIndex()).insertString(0, fileContent);
//          start to save into Undo manage action
            mainUI.getView().getModel().getDocument(mainUI.getView().getCurrentDocIndex()).setCanUndo(true);
        }else{
            DocumentBase tempDoc=model.getDocumentBase(arg0.getFileName(), "",docType);
            tempDoc.setFile(arg0);
//            tempDoc.setType(docType);
            Debug.getInstance().msg(Debug.EDITOR, mainUI.getView().getModel().toString());
            if(mainUI.getView().getModel().addDocument(tempDoc)){
                mainUI.getView().addDocumentView(tempDoc);
//              Do not save into Undo manage action
                mainUI.getView().getModel().getDocument(mainUI.getView().getCurrentDocIndex()).setCanUndo(false);
                mainUI.getView().getModel().getDocument(mainUI.getView().getCurrentDocIndex()).insertString(0,fileContent);
//              start to save into Undo manage action
                mainUI.getView().getModel().getDocument(mainUI.getView().getCurrentDocIndex()).setCanUndo(true);
            }
        }
    }
    
}

