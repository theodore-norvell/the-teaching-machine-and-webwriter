/**
 * VisreedFileFrame.java
 * 
 * @date: Nov 21, 2011
 * @author: Xiaoyu Guo
 * This file is part of the Teaching Machine project.
 */
package visreed.app;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.TransferHandler;

import visreed.swing.VisreedFrameTransferActionListener;
import visreed.swing.editor.VisreedTextArea;

/**
 * This is a more complicated frame for visreed projects with menu and file operations.
 * @author Xiaoyu Guo
 */
public class VisreedFileFrame extends VisreedSimpleFrame {
	private static final long serialVersionUID = 1477897814069798934L;
	
	protected JMenuBar mainMenuToolBar;
    
    protected File currentFile;
    protected JFileChooser fileChooser;
	
	public VisreedFileFrame(){
		super();
		currentFile = null;
	}

	/* (non-Javadoc)
	 * @see visreed.app.VisreedSimpleFrame#initializeControl()
	 */
	@Override
	protected void initializeControl(){
		this.mainMenuToolBar = new JMenuBar();
		
		fileChooser = new JFileChooser();
		fileChooser.setMultiSelectionEnabled(false);
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		
		super.initializeControl();
		
		setMappings(mainTextArea);
	}
	
	/**
     * Initialize the tool bars
     */
    protected void initializeToolBars(){
    	FlowLayout layout = new FlowLayout();
    	layout.setAlignment(FlowLayout.LEFT);
    	this.toolBarContainer.setLayout(layout);
        
    	this.fillMainMenuBar(mainMenuToolBar);
    	
        JToolBar testToolBar = new JToolBar();
        this.fillTestToolBar(testToolBar);
        toolBarContainer.add(testToolBar);
        
        JToolBar optionToolBar = new JToolBar();
        this.fillOptionToolBar(optionToolBar);
        toolBarContainer.add(optionToolBar);
    }

	/**
	 * Fills the main menu 
	 * @param menuBar
	 */
	@SuppressWarnings("serial")
	protected void fillMainMenuBar(JMenuBar menuBar) {
		setJMenuBar(menuBar);
		
		// File
		JMenu mnfile = new JMenu("File");
		mnfile.setMnemonic(KeyEvent.VK_F);
		menuBar.add(mnfile);
		
		JMenuItem mntmNew = new JMenuItem("New");
		mntmNew.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_MASK));
		mntmNew.setMnemonic(KeyEvent.VK_N);
		mntmNew.addActionListener(new AbstractAction(){
			@Override
			public void actionPerformed(ActionEvent e) {
				currentFile = null;
				setText("");
			}}
		);
		mnfile.add(mntmNew);
		
		JMenuItem mntmOpen = new JMenuItem("Open...");
		mntmOpen.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK));
		mntmOpen.setMnemonic(KeyEvent.VK_O);
		mntmOpen.addActionListener(new AbstractAction(){
			@Override
			public void actionPerformed(ActionEvent e) {
				int returnVal = fileChooser.showOpenDialog((Component) e.getSource());
				
				if(returnVal == JFileChooser.APPROVE_OPTION){
					File file = fileChooser.getSelectedFile();
					openFile(file);
				}
			}}
		);
		mnfile.add(mntmOpen);
		
		mnfile.addSeparator();
		
		JMenuItem mntmClose = new JMenuItem("Close");
		mntmClose.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, InputEvent.CTRL_MASK));
		mntmClose.setMnemonic(KeyEvent.VK_C);
		mnfile.add(mntmClose);
		
		mnfile.addSeparator();
		
		JMenuItem mntmSave = new JMenuItem("Save");
		mntmSave.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
		mntmSave.setMnemonic(KeyEvent.VK_S);
		mntmSave.addActionListener(new AbstractAction(){
			@Override
			public void actionPerformed(ActionEvent e) {
				if(currentFile == null){
					int returnVal = fileChooser.showSaveDialog((Component) e.getSource());
					
					if(returnVal == JFileChooser.APPROVE_OPTION){
						currentFile = fileChooser.getSelectedFile();
						
					}
				}
				saveToFile(currentFile);
			}}
		);
		mnfile.add(mntmSave);
		
		JMenuItem mntmSaveAs = new JMenuItem("Save As...");
		mntmSaveAs.setMnemonic(KeyEvent.VK_A);
		mntmSaveAs.addActionListener(new AbstractAction(){
			@Override
			public void actionPerformed(ActionEvent e) {
				int returnVal = fileChooser.showSaveDialog((Component) e.getSource());
				
				if(returnVal == JFileChooser.APPROVE_OPTION){
					File file = fileChooser.getSelectedFile();
					
					saveToFile(file);
				}
			}}
		);
		mnfile.add(mntmSaveAs);
		
		mnfile.addSeparator();
		
		JMenuItem mntmExit = new JMenuItem("Exit");
		mntmExit.setMnemonic(KeyEvent.VK_X);
		mnfile.add(mntmExit);
		
		// Edit
		JMenu mnEdit = new JMenu("Edit");
		menuBar.add(mnEdit);
		
		VisreedFrameTransferActionListener ccpListener = new VisreedFrameTransferActionListener();
		
		JMenuItem mntmUndo = new JMenuItem("Undo");
		mntmUndo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_MASK));
		mntmUndo.setMnemonic(KeyEvent.VK_U);
		mnEdit.add(mntmUndo);
		
		mnEdit.addSeparator();
		
		JMenuItem mntmCut = new JMenuItem("Cut");
		mntmCut.setActionCommand((String) TransferHandler.getCutAction().getValue(Action.NAME));
		mntmCut.addActionListener(ccpListener);
		mntmCut.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_MASK));
		mntmCut.setMnemonic(KeyEvent.VK_T);
		mnEdit.add(mntmCut);
		
		JMenuItem mntmCopy = new JMenuItem("Copy");
		mntmCopy.setActionCommand((String) TransferHandler.getCopyAction().getValue(Action.NAME));
		mntmCopy.addActionListener(ccpListener);
		mntmCopy.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_MASK));
		mntmCopy.setMnemonic(KeyEvent.VK_C);
		mnEdit.add(mntmCopy);
		
		JMenuItem mntmPaste = new JMenuItem("Paste");
		mntmPaste.setActionCommand((String) TransferHandler.getPasteAction().getValue(Action.NAME));
		mntmPaste.addActionListener(ccpListener);
		mntmPaste.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_MASK));
		mntmPaste.setMnemonic(KeyEvent.VK_P);
		mnEdit.add(mntmPaste);
		
		mnEdit.addSeparator();
		
		JMenuItem mntmDelete = new JMenuItem("Delete");
		mntmDelete.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0));
		mntmDelete.setMnemonic(KeyEvent.VK_D);
		mnEdit.add(mntmDelete);
		
		mnEdit.addSeparator();
		
		JMenu mnSurroundWith = new JMenu("Surround With");
		mnSurroundWith.setMnemonic(KeyEvent.VK_S);
		mnEdit.add(mnSurroundWith);
		
		// Help
		JMenu mnHelp = new JMenu("Help");
		mnHelp.setMnemonic(KeyEvent.VK_H);
		menuBar.add(mnHelp);
		
		JMenuItem mntmAbout = new JMenuItem("About...");
		mntmAbout.setMnemonic(KeyEvent.VK_A);
		mnHelp.add(mntmAbout);
	}
	
	private void setMappings(VisreedTextArea obj){
		ActionMap map = obj.getActionMap();
        map.put(TransferHandler.getCutAction().getValue(Action.NAME),
            TransferHandler.getCutAction()
        );
        map.put(TransferHandler.getCopyAction().getValue(Action.NAME),
            TransferHandler.getCopyAction()
        );
        map.put(TransferHandler.getPasteAction().getValue(Action.NAME),
            TransferHandler.getPasteAction()
        );
	}
	
	/**
	 * called after the wholegraph has changed.
	 */
	protected void refreshHook() {}
    
    public void openFile(File file){
		StringBuffer sb = new StringBuffer();
		
		FileReader reader = null;
		try{
			reader = new FileReader(file);
			BufferedReader br = new BufferedReader(reader);
			int ch ;
			while((ch = br.read()) != -1){
				sb.append((char) ch);
			}
			
			this.setText(sb.toString());
		} catch (Exception ex) {
		} finally {
			if(reader != null){
				try {
					reader.close();
				} catch (IOException ex) {
				}
			}
		}
		
		currentFile = file;
    }
    
    public void saveToFile(File file){
		FileWriter writer = null;
		try{
			writer = new FileWriter(file);
			BufferedWriter wr = new BufferedWriter(writer);
			wr.write(this.getText());
		} catch (Exception ex) {
		} finally {
			if(writer != null){
				try {
					writer.close();
				} catch (IOException ex) {
				}
			}
		}
		
		currentFile = file;
    }
}
