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

import java.awt.Dimension;
import java.awt.Event;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.MenuBar;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;
import javax.swing.ScrollPaneConstants;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.PlainDocument;
import javax.swing.text.StyleConstants;

import editor.controller.filters.IKeyFilter;
import editor.controller.filters.IndentationKeyFilter;
import editor.controller.filters.TabKeyFilter;
import editor.controller.listeners.EditorCaretListener;
import editor.controller.listeners.EditorKeyListener;
import editor.model.DocumentBase;
import editor.utility.EditState;
import editor.utility.EditorInitializeUtility;
import editor.utility.LoggerUtility;
import editor.view.EditorViewBase;
import editor.view.EditorViewConstants;

/**
 * Tabbed Text panel for editor main frame
 *@author hao sun
 *@version $Revision: 1.1 $
 *@since version 1.1
 **/

public class EditorTabbedTextPanel extends JTabbedPane implements EditorViewConstants{
    
    private EditorViewBase view;
    
    private JPopupMenu tabbedPanePopUpMenu;
    
    private HashMap<Integer, TextPane> textPaneTable=new HashMap<Integer, TextPane>();
    
    private List<Integer> textPaneDocIndexMap=new ArrayList<Integer>();
    
    private HashMap<KeyStroke,AbstractAction> textPaneKeyAction=new HashMap<KeyStroke, AbstractAction>();   //key action table
    
//    private MenuBar textPanelMenuBar;
    /**
     * Forbbit constructing without view object
     *
     */
    private EditorTabbedTextPanel(){
        //No code here
    }
    /**
     * Constructor for the tabbed text panel
     * @param view
     */
    public EditorTabbedTextPanel(EditorViewBase view){
        super();
        this.view=view;
        creatUI();
    }
    
    /**
     * Create UI
     *
     */
    private void creatUI(){
        
        //Set tabbed panel properties here
        //TODO
        setPreferredSize(new Dimension(this.getSize().width-DEFAULT_EDITPANELLEFTLABEL_WIDTH,this.getSize().height)); 
    }
    
    /**
     * Add panel onto tabbed text panel
     * @param doc
     */
    public void addTabPanel(DocumentBase doc){
        JComponent tempPanel = makeEditPanel(doc);
//        tempPanel.requestFocusInWindow();
        addTab(doc.getDocumentName(), null, tempPanel,
                doc.getDocumentName());
        setSelectedIndex(getTabCount()-1);
    }
    
    /**
     * Create editor panel
     * @param doc
     * @return
     */
    protected JComponent makeEditPanel(DocumentBase doc) {
        TextPane panel=new TextPane();
//        panel.setSize(this.getSize());
        panel.setMaximumSize(new Dimension(DEFAULT_EDITOR_WIDTH, DEFAULT_EDITOR_HEIGHT));
        panel.setPreferredSize(new Dimension(DEFAULT_EDITOR_WIDTH, DEFAULT_EDITOR_HEIGHT));
        panel.setFont(DEFAULT_EDIT_FONT);
        panel.setMargin(new Insets(5,5,5,5));
        panel.setLayout(new GridLayout(1, 1));

//        try {
//            System.out.println("test code:"+view.getModel().getDocument(doc.getDocumentIndex()).getDocument().getText(0, 30));
//        } catch (BadLocationException e1) {
//            // TODO Auto-generated catch block
//            e1.printStackTrace();
//        }
        if(view.getModel().getDocument(doc.getDocumentIndex())!=null){
            panel.setStyledDocument(view.getModel().getDocument(doc.getDocumentIndex()).getDocument());
        }
            panel.setText("");
        
        //Add panel to scrollPane
        JScrollPane scrollPane = new JScrollPane(panel,ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        textPaneTable.put(doc.getDocumentIndex(), panel);
        textPaneDocIndexMap.add(doc.getDocumentIndex());

//        System.out.println("textPaneDocIndexMap:"+(textPaneDocIndexMap.size()-1)+" add"+doc.getDocumentIndex());
        scrollPane.add(tabbedPanePopUpMenu=EditorInitializeUtility.convertToPopUpMenu(view.getModel().getPopUpMenu("editPanel")));
        
        //Add key binding
        
        KeyStroke key=KeyStroke.getKeyStroke(KeyEvent.VK_Z, Event.CTRL_MASK);  //undo
        panel.getInputMap().put(key, doc.getUndoAction());
        key=KeyStroke.getKeyStroke(KeyEvent.VK_Y, Event.CTRL_MASK);  //redo
        panel.getInputMap().put(key, doc.getRedoAction());
        
        //add CTRL+X :cut
        key=KeyStroke.getKeyStroke(KeyEvent.VK_X, Event.CTRL_MASK);
        Action tempAction=getAction(panel.getActions(),DefaultEditorKit.cutAction);
        if(tempAction!=null)
            panel.getInputMap().put(key, tempAction);
        //add CTRL+C:copy
        key=KeyStroke.getKeyStroke(KeyEvent.VK_C, Event.CTRL_MASK);
        tempAction=getAction(panel.getActions(),DefaultEditorKit.copyAction);
        if(tempAction!=null)
            panel.getInputMap().put(key, tempAction);
//      add CTRL+V:copy
        key=KeyStroke.getKeyStroke(KeyEvent.VK_V, Event.CTRL_MASK);
        tempAction=getAction(panel.getActions(),DefaultEditorKit.pasteAction);
        if(tempAction!=null)
            panel.getInputMap().put(key, tempAction);
//      add CTRL+A:select all
        key=KeyStroke.getKeyStroke(KeyEvent.VK_A, Event.CTRL_MASK);
        tempAction=getAction(panel.getActions(),DefaultEditorKit.selectAllAction);
        if(tempAction!=null)
            panel.getInputMap().put(key, tempAction);
        
        addBinding(panel, textPaneKeyAction);
        
        //Add key filter
        List<IKeyFilter> keyFilters=new ArrayList<IKeyFilter>();
        //Make key auto indentation
        if(AUTO_INDENTATION_ENABLED)
            keyFilters.add(new IndentationKeyFilter(doc));
        //Make Tab key filting
        keyFilters.add(new TabKeyFilter(doc));
        
        doc.setKeyFilters(keyFilters);
        
        //Add listener
        panel.addKeyListener(new EditorKeyListener(view,doc.getDocumentIndex()));
        
        panel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent e) {
                if (e.isPopupTrigger()) {
                    tabbedPanePopUpMenu.show(e.getComponent(),
                               e.getX(), e.getY());
                }
            }
            public void mouseReleased(java.awt.event.MouseEvent e) {
                if (e.isPopupTrigger()) {
                    tabbedPanePopUpMenu.show(e.getComponent(),
                               e.getX(), e.getY());
                }
            }
        });

        panel.addCaretListener(new EditorCaretListener(view,view.getModel().getDocument(doc.getDocumentIndex())));
        return scrollPane;
    }
    
    /**
     * Get text panel by name known
     * @param name
     * @return
     */
    public TextPane getTextPanel(int docIndex){
        return textPaneTable.get(docIndex);
    }
    
    /**
     * Add key binging
     *
     */
    private void addBinding(TextPane panel,HashMap<KeyStroke,AbstractAction> actionList){
        //Check the precondition
        if(panel==null||actionList==null||actionList.size()==0)
            return;
        InputMap inputMap = panel.getInputMap();
        KeyStroke[] keyStrokeSet=(KeyStroke[])actionList.keySet().toArray();
        for(int i=0;i<keyStrokeSet.length;i++){
            inputMap.put(keyStrokeSet[i], actionList.get(keyStrokeSet[i]));
        }
    }
    /**
     * @param textPaneKeyAction the textPaneKeyAction to set
     */
    public void setTextPaneKeyAction(
            HashMap<KeyStroke, AbstractAction> textPaneKeyAction) {
        this.textPaneKeyAction = textPaneKeyAction;
    }
    
    /**
     * Remove Current TextPanel
     *
     */
    public void removeCurrentTextPane(){
        int currentDocIndex=getCurrentDocIndex();
        if(currentDocIndex<0)
            return;
        textPaneDocIndexMap.remove(view.getCurrentActiveEditorIndex());

        this.remove(view.getCurrentActiveEditorIndex());
        //Delete view
        textPaneTable.remove(currentDocIndex);
        //Delete model
        view.getModel().removeDocument(currentDocIndex);
    }
    
    private void removeTextPaneMap(int currentTextPaneIndex){
        
    }
    
    /**
     * Get current document index
     * @return
     */
    public int getCurrentDocIndex(){
//        System.out.println("getCurrentActiveEditorIndex:"+view.getCurrentActiveEditorIndex());
        return view.getCurrentActiveEditorIndex()<0?-1:textPaneDocIndexMap.get(view.getCurrentActiveEditorIndex());
    }
    
    /**
     * Get action by known action name
     * @param actionArray
     * @param actionName
     * @return action
     */
    private Action getAction(Action[] actionArray,String actionName){
        for(int i=0;i<actionArray.length;i++){
            if(actionArray[i].getValue(Action.NAME).equals(actionName))
                return actionArray[i];
        }
        return null;
    }
}

