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
package editor.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.swing.text.BadLocationException;
import javax.swing.text.AbstractDocument.Content;
import javax.swing.text.html.parser.ContentModel;

import tm.utilities.Debug;

import editor.controller.actions.MenuBarActions;
import editor.controller.listeners.EditorDocumentListener;
import editor.model.DocumentBase.Type;
import editor.utility.EditState;
import editor.utility.EditorInitializeUtility;
import editor.utility.EditorProperty;
import editor.utility.LoggerUtility;
import editor.view.EditorViewBase;

/**
 * 
 *@author hao sun
 *@version $Revision: 1.1 $
 *@since version 1.1
 **/

public class EditorModel implements IEditorModel{
    
    private List<IMenuModel> editorMenu;
    
    private HashMap<String, List<IMenuModel>> editorPopUpMenu;
    
    private EditorViewBase editorView;

    //Doc index counter
    private int currentDocIndex=0;
    /**
     * Constructor of the editor model
     *
     */
    public EditorModel(){
        //TODO
        editorPopUpMenu=new HashMap<String, List<IMenuModel>>();
        setDefaultPopUpMenu();
    }
    
    public void addDocumentToEditor(String docName,EditState editState,Type type) {
        DocumentBase tempDoc=getDocumentBase(docName,"",type);
        tempDoc.setDocumentIndex(currentDocIndex);
                
        //Set edit state
        if(editState!=null)
            tempDoc.setEditState(editState);
        addDocument(tempDoc);
    }

    /**
     * Check the model has known docName document already or not
     * @param docName
     * @return true if there existed
     */
    private boolean hasDocument(String docName){
        boolean result=false;
        Iterator<DocumentBase> temp=docTable.values().iterator();
        while(temp!=null&&temp.hasNext()){
            if(docName.equalsIgnoreCase(temp.next().getDocumentName())){
                result=true;
                break;
            }
        }
        return result;
    }
    
    public DocumentBase getDocumentBase(String docName,String content,Type type){
        DocumentBase tempDoc=new DocumentModel();
        tempDoc.setType(type);
//      Set new document name
        if(docName!=null)
            tempDoc.setTitle(docName);
        //Add document listener
        tempDoc.getDocument().addDocumentListener(new EditorDocumentListener(editorView,tempDoc));
        
        if(!content.equals("")&&content!=null)
            try {
                tempDoc.setCurrentCaretEndPostion(content.length());
                tempDoc.setCurrentCaretStartPostion(content.length());
                
//                System.out.print("code :\n"+tempDoc.getTokenList().size());
                tempDoc.getDocument().insertString(0, content, null);
            } catch (BadLocationException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            
        return tempDoc;
    }
    
    public void changeEditState(String docName, EditState newState) {
        // TODO Auto-generated method stub
        
    }

    public void closeEditor() {
        // TODO Auto-generated method stub
        
    }

    public EditState getEditState(String docName) {
        try{
            return docTable.get(docName).getEditState();
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public void initializeEditor() {
        editorMenu=EditorInitializeUtility.getMenuModelList(this);
    }

    public boolean isDocumentModified(String docName) {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean isEditorOpened() {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean removeDocumentFromEditor(String docName) {
        // TODO Auto-generated method stub
        return false;
    }

    public void setModified(String docName, boolean modified) {
        // TODO Auto-generated method stub
        
    }

    public String getEditorProperty(EditorProperty property) {
        // TODO Auto-generated method stub
        return null;
    }

    public HashMap<EditorProperty, String> getEditorPropertyList() {
        // TODO Auto-generated method stub
        return null;
    }

    public void setProperty(HashMap<String, String> propertyList) {
        // TODO Auto-generated method stub
        
    }

    public void setView(EditorViewBase view) {
        editorView=view;
    }

    public EditorViewBase getView(){
        return editorView;
    }
    public DocumentBase getDocument(int index) {
        return docTable.get(index);
    }
    public boolean hasDocument() {
        return !docTable.isEmpty();
    }
    public int getDocmentNumber() {
        return docTable.size();
    }
    public List<IMenuModel> getEditorMenuList() {
        return editorMenu;
    }
    
    public List<IMenuModel> getPopUpMenu(String menuLocation) {
        return editorPopUpMenu.get(menuLocation);
    }
    public void setPopUpMenu(String menuLocation, List<IMenuModel> menu) {
        editorPopUpMenu.put(menuLocation,menu);
    }
    
    private void setDefaultPopUpMenu(){
        List<IMenuModel> tempList = new ArrayList<IMenuModel>();
        MenuModel tempMenu = new MenuModel("New", 0, editor.model.IMenuModel.Type.POPUP);
        tempMenu.setAction(new MenuBarActions.OpenNewFileDialogAction("test",
                getView()));
        tempList.add(tempMenu);
        
        editorPopUpMenu.put("mainFrame", tempList);
        
        tempList = new ArrayList<IMenuModel>();
        tempMenu = new MenuModel("Save", 0, editor.model.IMenuModel.Type.POPUP);
        tempMenu.setAction(new MenuBarActions.SaveTextPanelAction("save",
                getView()));
        tempList.add(tempMenu);
        tempMenu = new MenuModel("New", 0, editor.model.IMenuModel.Type.POPUP);
        tempMenu.setAction(new MenuBarActions.OpenNewFileDialogAction("new",
                getView()));
        tempMenu.setEnabled(false);
        tempList.add(tempMenu);
        editorPopUpMenu.put("editPanel", tempList);
    }
    
    public boolean addDocument(DocumentBase doc) {
        doc.setDocumentIndex(currentDocIndex);
        boolean result=true;
        //Check duplicated doc name
        if(hasDocument(doc.getDocumentName())){
            LoggerUtility.logOnConsole("Document has already existed!");
            result=false;
        }else{
            Debug.getInstance().msg(Debug.EDITOR, Integer.toString(currentDocIndex));
            docTable.put(currentDocIndex, doc);
            currentDocIndex++;
            result=true;
        }
        return result;
    }
    
    public void removeDocument(int docIndex) {
        Debug.getInstance().msg(Debug.EDITOR, Integer.toString(docIndex));
//        System.out.println("delete model:"+docTable.get(docIndex).getDocumentName());
        docTable.remove(docIndex);
    }
}

