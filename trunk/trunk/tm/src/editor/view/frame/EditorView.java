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
package editor.view.frame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.undo.UndoManager;

import editor.controller.actions.EditorRedoAction;
import editor.controller.actions.EditorUndoAction;
import editor.controller.listeners.TabbedPaneChangeListener;
import editor.model.DocumentBase;
import editor.model.EditorModel;
import editor.model.IEditorModel;
import editor.model.DocumentBase.Type;
import editor.utility.EditState;
import editor.utility.EditorInitializeUtility;
import editor.utility.LoggerUtility;
import editor.view.EditorViewBase;
import editor.view.component.EditorTabbedTextPanel;
import editor.view.component.TextPane;

/**
 * View component for a TM editor
 * @author hao sun
 * @version $Revision: 1.1 $
 * @since version 1.1
 */

public class EditorView extends EditorViewBase {

    //editor model
    private IEditorModel editorModel=null;  //  @jve:decl-index=0:
    // add components to the frame
    // Menu bar
    private JMenuBar mb=null;
    //Tool bar
    private JToolBar tb=null;
    private JPanel panelInFrame;
    private JComponent internelFrame;
    private JPopupMenu mainFramePopUpMenu=null;
    
    //Editor textpanel
    private EditorTabbedTextPanel tabbedPane=null;
    private JTextArea consolePanel=null;
    private JLabel statuLabel=null;


    /**
     * Construcor for main frame
     * 
     */
    
    public EditorView(IEditorModel editor){
        setModel(editor);
//        initialize();
    }
    public EditorView() {
        editorModel=new EditorModel();
//        initialize();
    }
	
    /**
     * Update
     */
    public void update(){
//        panelInFrame
    }
    
    /**
     * Create UI
     */
    public void initialize(){
        if(editorModel!=null){
            tabbedPane = new EditorTabbedTextPanel(this);
            editorModel.setView(this);
            editorModel.initializeEditor();
        }else
            LoggerUtility.logOnConsole("Error!no model for this editor");
        
        //Initialize editor constants
        setConstants();
        //set basic properties
        setTitle("Editor for TM");
        setSize(new Dimension(DEFAULT_FRAME_WIDTH, DEFAULT_FRAME_HEIGHT));
        //TODO to be added
        
        //set menu bar
        mb = getMenuBarForMainFrame();
        mb.setSize(DEFAULT_FRAME_WIDTH, mb.HEIGHT);
        setJMenuBar(mb);
        //set tool bar
        tb=getToolBarForMainFrame();
        add(tb, BorderLayout.PAGE_START);
        
        //set panels
        panelInFrame=new JPanel(new GridLayout(1, 1));
        internelFrame=getInternelTabFrame();
        panelInFrame.setSize(this.getSize());
        panelInFrame.add(internelFrame, BorderLayout.CENTER);
        panelInFrame.setBackground(Color.BLACK);
        getContentPane().add(panelInFrame,BorderLayout.CENTER);
        
        statuLabel=new JLabel("status");
        getContentPane().add(statuLabel, BorderLayout.PAGE_END);
        
        //Add popup menu
        mainFramePopUpMenu=EditorInitializeUtility.convertToPopUpMenu(this.getModel().getPopUpMenu("mainFrame"));
//        add(mainFramePopUpMenu);
        
        //Add Tabbedpane change listener
//        tabbedPane.addChangeListener(l);
        //Add resize lisstener to garuntee the size fit in the screen
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent e) {
                panelInFrame.setSize(panelInFrame.getParent().getSize());
                internelFrame.setSize(panelInFrame.getSize());
            }
        });
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent e) {
                stop();
            }
        });
        
    }
    @Override
    public void createAndShowUI() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//        setUndecorated(true);
//        rootPane.setWindowDecorationStyle(JRootPane.FRAME);
        
        setVisible(true);
    }

    public void setModel(IEditorModel editor) {
        editorModel=editor;
        if(editor!=null)
            editor.setView(this);
//        editorModel.setView(this);
    }

    public IEditorModel getModel() {
        return editorModel;
    }
    
    /**
     * Get menu bar for main frame
     * @return
     */
    private JMenuBar getMenuBarForMainFrame() {
        if(editorModel!=null){
            return EditorInitializeUtility.convertToMenuBar(editorModel.getEditorMenuList());
        }else{
            LoggerUtility.logOnConsole("No model for editor!");
            return null;
        }
    }
    
    /**
     * Get tool bar main frame
     * @return
     */
    private JToolBar getToolBarForMainFrame() {
        if(editorModel!=null){
            return EditorInitializeUtility.convertToToolBar(tb,editorModel.getEditorMenuList());
        }else{
            LoggerUtility.logOnConsole("No model for editor!");
            return null;
        }
    }
    
    /**
     * Get edit tabbed panel frame
     * TODO
     * @return
     */
    private JComponent getInternelTabFrame(){
        //Add tabbedPane listener
        tabbedPane.addChangeListener(new TabbedPaneChangeListener(this));
        
        if(editorModel.hasDocument()){
            for(int i=0;i<editorModel.getDocmentNumber();i++){
                tabbedPane.addTabPanel(editorModel.getDocument(i));
            }
        }else{
            //Open an empty file
            LoggerUtility.logOnConsole("No known document!");
            editorModel.addDocumentToEditor(null, EditState.NEW,Type.JAVA);
        }
        
        JLabel editPanelRightLabel=new JLabel("  ");
        editPanelRightLabel.setVisible(true);
        editPanelRightLabel.setSize(new Dimension(DEFAULT_EDITPANELLEFTLABEL_WIDTH,this.getSize().height));

        JPanel textPanel=new JPanel();
        
        textPanel.setLayout(new BorderLayout());
        
        textPanel.add(tabbedPane,BorderLayout.CENTER);
        textPanel.add(editPanelRightLabel,BorderLayout.EAST);
        
        JSplitPane splitPanel;

        //Add split panel
        if(HAS_CONSOLE){
            consolePanel=new JTextArea();
            consolePanel.setEditable(false);
            consolePanel.setSize(new Dimension(this.getSize().width,(int)(this.getSize().height*0.2)));

            JScrollPane scrollPaneForLog = new JScrollPane(consolePanel);
            
            splitPanel=new JSplitPane(JSplitPane.VERTICAL_SPLIT,textPanel,scrollPaneForLog);
            splitPanel.setOneTouchExpandable(true);
            splitPanel.setDividerLocation((int)(this.getSize().getHeight()*0.8));
//            splitPanel.setDividerSize(1);
            return splitPanel;
        }else{
            return textPanel;
        }
    }
    
    /**
     * Set Constants for editor at very beginning
     *
     */
    private void setConstants(){
        KEY_MAP.put(EDITOR_REDO_KEY,"Redo Key");
        KEY_MAP.put(EDITOR_UNDO_KEY,"Undo Key");
    }
    
    public void addDocumentView(DocumentBase doc){
        tabbedPane.addTabPanel(doc);
        updateMenu();
    }
//    
//    public int getCurrentDocIndex(){
//        return currentDocIndex;
//    }
    
    public void setStatusString(String status) {
        if(statuLabel!=null)
            statuLabel.setText(status);
    }
    
    public TextPane getTextPanel(int docIndex){
        return tabbedPane.getTextPanel(docIndex);
    }
    
    public void addConsoleString(String message) {
        if(consolePanel!=null)
            consolePanel.append(message+"\r\n");
        else
            LoggerUtility.logOnConsole("No consolePanel for add");
    } 
    
    public void setConsoleString(String message) {
        if(consolePanel!=null)
            consolePanel.setText(message);
        else
            LoggerUtility.logOnConsole("No consolePanel for set");
    }
     
    public EditorRedoAction getRedoAction(int docIndex) {
        return editorModel.getDocument(docIndex).getRedoAction();
    }
     
    public EditorUndoAction getUndoAction(int docIndex) {
        return editorModel.getDocument(docIndex).getUndoAction();
    }
     
    public UndoManager getUndoManager(int docIndex) {
        return editorModel.getDocument(docIndex).getUndoManager();
    }
     
    public int getCurrentActiveEditorIndex() {
        return tabbedPane.getSelectedIndex();
    }
     
    public void updateMenu() {
        remove(mb);
        mb = getMenuBarForMainFrame();
        mb.setSize(DEFAULT_FRAME_WIDTH, mb.HEIGHT);
//        setJMenuBar(mb);
    }
     
    public void closeCurrentTextPanel() {
        tabbedPane.removeCurrentTextPane();
    }
    
    public int getCurrentDocIndex(){
        return  tabbedPane.getCurrentDocIndex();
    }
     
    public void saveCurrentDocument() {
        getModel().getDocument(tabbedPane.getCurrentDocIndex()).saveFile();
    }
    
}
