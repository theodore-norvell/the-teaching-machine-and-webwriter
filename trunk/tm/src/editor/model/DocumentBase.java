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
 * Created on 2006-9-27
 * project: FinalProject
 */
package editor.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.StyledDocument;
import javax.swing.undo.UndoManager;

import tm.utilities.Debug;
import tm.utilities.TMFile;
import editor.controller.actions.EditorRedoAction;
import editor.controller.actions.EditorUndoAction;
import editor.controller.filters.IKeyFilter;
import editor.controller.listeners.EdtiorUndoableEditListener;
import editor.controller.parser.Token;
import editor.utility.EditState;
import editor.utility.GlobalValue;

/**
 * Document model base class Supper class for every child document
 * 
 * @author hao sun
 * @version $Revision: 1.1 $
 * @since version 1.1
 */

public abstract class DocumentBase {
    
    public enum Type{
        JAVA,
        CPP,
        ETC
    }
    private StyledDocument doc;

    // Caret position
    private int lastCaretStartPosition = 0;

    private int lastCaretEndPosition = 0;

    private int currentCaretStartPostion = 0;

    private int currentCaretEndPostion = 0;

    // token list for colorizing
    private List<Token> tokenList = new ArrayList<Token>();

    private String documentName;

    private int documentIndex = 0;

    private EditState editState = EditState.NULL;

    // Undo helpers
    private EditorUndoAction undoAction;

    private EditorRedoAction redoAction;

    private UndoManager undo = new UndoManager();

    private EdtiorUndoableEditListener edtiorUndoableEditListener;

    private String documentInitial = GlobalValue.DUCUMENT_INITIAL;

    private List<IKeyFilter> keyFilters = null;
    
    //Document type,JAVA as default type
    private Type type=null;

    //TMFile 
    private TMFile file=null;
    /**
     * Constructing without params
     * 
     */
    public DocumentBase() {
        createDocument();
    }

    /**
     * Constructing by doc index number
     * 
     * @param index
     *            doc index number
     */
    public DocumentBase(int index) {
        // TODO check it later
        this();
        documentIndex = index;
        setTitle(documentInitial + documentIndex);
    }

    /**
     * Create document
     * 
     */
    private void createDocument() {
        doc = new DefaultStyledDocument();

        // Add features
        undoAction = new EditorUndoAction(this);
        redoAction = new EditorRedoAction(this);

        edtiorUndoableEditListener = new EdtiorUndoableEditListener(this);
        doc.addUndoableEditListener(edtiorUndoableEditListener);
    }
    
    
    /**
     * @return the type
     */
    public Type getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(Type type) {
        this.type = type;
    }

    /**
     * Set title for this document
     * 
     * @param title
     */
    public void setTitle(String title) {
        // TODO to be tested
        documentName = title;
        doc.putProperty(DefaultStyledDocument.TitleProperty, title);
    }

    /**
     * @return the documentName
     */
    public String getDocumentName() {
        return documentName;
    }

    /**
     * @param documentName
     *            the documentName to set
     */
    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    /**
     * Set StyledDocument
     * 
     * @param doc
     */
    public void setDocument(StyledDocument doc) {
        this.doc = doc;
    }

    /**
     * Get StyledDocument
     * 
     * @return
     */
    public StyledDocument getDocument() {
        return doc;
    }

    /**
     * @return the documentIndex
     */
    public int getDocumentIndex() {
        return documentIndex;
    }

    /**
     * @param documentIndex
     *            the documentIndex to set
     */
    public void setDocumentIndex(int documentIndex) {
        this.documentIndex = documentIndex;
    }

    /**
     * @return the editState
     */
    public EditState getEditState() {
        return editState;
    }

    /**
     * @param editState
     *            the editState to set
     */
    public void setEditState(EditState editState) {
        this.editState = editState;
    }

    /**
     * Get current caret end position for document
     * @return the currentCaretEndPostion
     */
    public int getCurrentCaretEndPostion() {
        return currentCaretEndPostion;
    }

    /**
     *  Set current caret end position for document
     * @param currentCaretEndPostion
     *            the currentCaretEndPostion to set
     */
    public void setCurrentCaretEndPostion(int currentCaretEndPostion) {
        this.currentCaretEndPostion = currentCaretEndPostion;
    }

    /**
     *  Get current caret start position for document
     * @return the currentCaretStartPostion
     */
    public int getCurrentCaretStartPostion() {
        return currentCaretStartPostion;
    }

    /**
     *  set current caret start position for document
     * @param currentCaretStartPostion
     *            the currentCaretStartPostion to set
     */
    public void setCurrentCaretStartPostion(int currentCaretStartPostion) {
        this.currentCaretStartPostion = currentCaretStartPostion;
    }

    /**
     *  Get last caret end position for document
     * @return the lastCaretEndPosition
     */
    public int getLastCaretEndPosition() {
        return lastCaretEndPosition;
    }

    /**
     *  Set last caret end  position for document
     * @param lastCaretEndPosition
     *            the lastCaretEndPosition to set
     */
    public void setLastCaretEndPosition(int lastCaretEndPosition) {
        this.lastCaretEndPosition = lastCaretEndPosition;
    }

    /**
     * @return the lastCaretStartPosition
     */
    public int getLastCaretStartPosition() {
        return lastCaretStartPosition;
    }

    /**
     * @param lastCaretStartPosition
     *            the lastCaretStartPosition to set
     */
    public void setLastCaretStartPosition(int lastCaretStartPosition) {
        this.lastCaretStartPosition = lastCaretStartPosition;
    }

    /**
     * @return the tokenList
     */
    public List<Token> getTokenList() {
        return tokenList;
    }

    /**
     * @param tokenList
     *            the tokenList to set
     */
    public void setTokenList(List<Token> tokenList) {
        this.tokenList = tokenList;
    }

    public EditorRedoAction getRedoAction() {
        return redoAction;
    }

    public EditorUndoAction getUndoAction() {
        return undoAction;
    }

    public UndoManager getUndoManager() {
        return undo;
    }

    public void setCanUndo(boolean canUndo) {
        edtiorUndoableEditListener.setCanUndo(canUndo);
    }

    /**
     * @return the keyFilters
     */
    public List<IKeyFilter> getKeyFilters() {
        return keyFilters;
    }

    /**
     * @param keyFilters
     *            the keyFilters to set
     */
    public void setKeyFilters(List<IKeyFilter> keyFilters) {
        this.keyFilters = keyFilters;
    }

    
    /**
     * @return the file
     */
    public TMFile getFile() {
        return file;
    }

    /**
     * @param file the file to set
     */
    public void setFile(TMFile file) {
        this.file = file;
    }

    /**
     * Get content from document and write into TMFile
     *
     */
    public void saveFile(){
        try {
            String content=doc.getText(0, doc.getLength());
            file.writeFile(content);
            
        } catch (BadLocationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    /**
     * Insert String in offset
     * 
     * @param offset
     * @param string
     */
    public void insertString(int offset, String string) {
        try {
            // Update document
            doc.insertString(offset, string, null);
            // Update current and last caret position
//            if (lastCaretStartPosition >= offset)
//                lastCaretStartPosition += string.length();
//            if (lastCaretEndPosition >= offset)
//                lastCaretEndPosition += string.length();
//            if (currentCaretStartPostion >= offset)
//                currentCaretStartPostion += string.length();
//            if (currentCaretEndPostion >= offset)
//                currentCaretEndPostion += string.length();
//            System.out.println("currentPos after filting"+getCurrentCaretEndPostion());
            // Update token list
//            for (int i = 0; i < string.length(); i++){
//                tokenList.add(offset, null);
//                System.out.println("Add");
//            }
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    /**
     * Remove String in offset
     * 
     * @param offset
     * @param stringLength
     */
    public void removeString(int offset, int stringLength) {
        try {
//            System.out.println("currentposition:"+currentCaretEndPostion);
            // Update document
            doc.remove(offset, stringLength);
            // Update current and last caret position
//            if (lastCaretStartPosition >= offset)
//                lastCaretStartPosition -= stringLength;
//            if (lastCaretEndPosition >= offset)
//                lastCaretEndPosition -= stringLength;
//            if (currentCaretStartPostion >= offset)
//                currentCaretStartPostion -= stringLength;
//            if (currentCaretEndPostion >= offset)
//                currentCaretEndPostion -= stringLength;

            //Show test
            for (int i = 0; i < tokenList.size(); i++)
                Debug.getInstance().msg(Debug.EDITOR, tokenList.get(i).toString());
            // Update token list
//            for (int i = 0; i < stringLength; i++)
//                if (offset < tokenList.size())
//                    tokenList.remove(offset);
            
            Debug.getInstance().msg(Debug.EDITOR, "currentposition after:"+currentCaretEndPostion);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }
}
