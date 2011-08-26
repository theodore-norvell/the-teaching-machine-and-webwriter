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
 * Created on 2006-10-1
 * project: FinalProject
 */
package editor.view.dialog;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JRootPane;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;

import editor.view.EditorViewBase;

/**
 * New File Dialog
 *@author hao sun
 *@version $Revision: 1.1 $
 *@since version 1.1
 **/

public class NewFileDialog extends JDialog implements Runnable{

    private static final long serialVersionUID = 1L;
    private JPanel jContentPane = null;
    private JButton buttonOk = null;
    private JButton buttonCancel = null;
    private JLabel jLabel = null;
    private JLabel jLabel1 = null;
    private JRadioButtonMenuItem radioJavaType = null;
    private JRadioButtonMenuItem radioCppType = null;
    private JPanel jPanel = null;
    private JRadioButtonMenuItem radioOtherType = null;
    
    private JDialog currentDialog=null;
    private EditorViewBase editorView=null;
    private JTextArea outputMessage = null;;
    /**
     * @param owner
     */
    public NewFileDialog(EditorViewBase owner) {
        super(owner);
        currentDialog=this;
        editorView=owner;
    }

    /**
     * This method initializes this
     * 
     * @return void
     */
    private void initialize() {
        this.setSize(new Dimension(300, 200));
        this.setTitle("Choose New File Type");
        this.setContentPane(getJContentPane());
        
        setUndecorated(true);
        rootPane.setWindowDecorationStyle(JRootPane.FRAME);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setVisible(true);
//        addWindowListener(new WindowClosingAction());
    }
//
//    private class WindowClosingAction extends java.awt.event.WindowAdapter{
//        public void windowClosing(java.awt.event.WindowEvent e) {
//            System.out.println("windowClosing()"); // TODO Auto-generated Event stub windowClosing()
//        }
//    }
    /**
     * This method initializes jContentPane	
     * 	
     * @return javax.swing.JPanel	
     */
    private JPanel getJContentPane() {
        if (jContentPane == null) {
            jLabel1 = new JLabel();
            jLabel1.setText(" File type:");
            jLabel1.setBounds(new Rectangle(35, 40, 52, 18));
            jLabel = new JLabel();
            jLabel.setBounds(new Rectangle(5, 20, 145, 31));
            jLabel.setText("      New file type");
            jContentPane = new JPanel();
            jContentPane.setLayout(null);
            jContentPane.setBorder(BorderFactory.createLineBorder(Color.gray, 5));
            jContentPane.add(jLabel, null);
            jContentPane.add(getJPanel(), null);
        }
        return jContentPane;
    }

    /**
     * This method initializes buttonOk	
     * 	
     * @return javax.swing.JButton	
     */
    private JButton getButtonOk() {
        if (buttonOk == null) {
            buttonOk = new JButton();
            buttonOk.setText("OK");
            buttonOk.setBounds(new Rectangle(129, 85, 51, 28));
            buttonOk.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    //Check
                    if(!radioJavaType.isSelected()&&!radioCppType.isSelected()&&!radioOtherType.isSelected()){
                        //Output error message
                        String errorMessage="error: \r file type \nmust be decided!";
//                        LoggerUtility.logOnConsole(errorMessage);
                        outputMessage.setText(errorMessage);
                        return;
                    }
                    //Open the new file
                    try{
                        editorView.makeNewDocumentView();
                    }catch(Exception e1){
                        e1.printStackTrace();
                    }
                    dispose();
                    currentDialog=null;
                }
            });
        }
        return buttonOk;
    }

    /**
     * This method initializes buttonCancel	
     * 	
     * @return javax.swing.JButton	
     */
    private JButton getButtonCancel() {
        if (buttonCancel == null) {
            buttonCancel = new JButton();
            buttonCancel.setText("Cancel");
            buttonCancel.setBounds(new Rectangle(187, 85, 80, 28));
            buttonCancel.addActionListener(new java.awt.event.ActionListener() {
                /* (non-Javadoc)
                 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
                 */
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    //Close the dialog
                    dispose();
                    currentDialog=null;
                }
            });
            
        }
        return buttonCancel;
    }

    /**
     * This method initializes radioJavaType	
     * 	
     * @return javax.swing.JRadioButtonMenuItem	
     */
    private JRadioButtonMenuItem getRadioJavaType() {
        if (radioJavaType == null) {
            radioJavaType = new JRadioButtonMenuItem();
            radioJavaType.setText("java");
            radioJavaType.setBounds(new Rectangle(102, 36, 61, 23));
            radioJavaType.addItemListener(new java.awt.event.ItemListener() {
                public void itemStateChanged(java.awt.event.ItemEvent e) {
                    //Check
                    if(radioJavaType.isSelected()){
                        radioCppType.setSelected(false);
                        radioOtherType.setSelected(false);    
                    }
                }
            });
        }
        return radioJavaType;
    }

    /**
     * This method initializes radioCppType	
     * 	
     * @return javax.swing.JRadioButtonMenuItem	
     */
    private JRadioButtonMenuItem getRadioCppType() {
        if (radioCppType == null) {
            radioCppType = new JRadioButtonMenuItem();
            radioCppType.setText("c++");
            radioCppType.setBounds(new Rectangle(166, 35, 61, 23));
            radioCppType.addItemListener(new java.awt.event.ItemListener() {
                public void itemStateChanged(java.awt.event.ItemEvent e) {
                    // Check
                    if(radioCppType.isSelected()){
                        radioJavaType.setSelected(false);
                        radioOtherType.setSelected(false);    
                    }
                }
            });
        }
        return radioCppType;
    }

    /**
     * This method initializes jPanel	
     * 	
     * @return javax.swing.JPanel	
     */
    private JPanel getJPanel() {
        if (jPanel == null) {
            jPanel = new JPanel();
            jPanel.setLayout(null);
            jPanel.setBounds(new Rectangle(8, 47, 276, 120));
            jPanel.setBorder(BorderFactory.createTitledBorder(null, "Type select", TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.BOLD, 12), new Color(51, 51, 51)));
            jPanel.add(getRadioJavaType(), null);
            jPanel.add(getRadioCppType(), null);
            jPanel.add(jLabel1, null);
            jPanel.add(getRadioOtherType(), null);
            jPanel.add(getButtonOk(), null);
            jPanel.add(getButtonCancel(), null);
            jPanel.add(getOutputMessage(), null);
        }
        return jPanel;
    }

    /**
     * This method initializes radioOtherType	
     * 	
     * @return javax.swing.JRadioButtonMenuItem	
     */
    private JRadioButtonMenuItem getRadioOtherType() {
        if (radioOtherType == null) {
            radioOtherType = new JRadioButtonMenuItem();
            radioOtherType.setBounds(new Rectangle(102, 60, 61, 23));
            radioOtherType.setText("others");
            radioOtherType.addItemListener(new java.awt.event.ItemListener() {
                public void itemStateChanged(java.awt.event.ItemEvent e) {
                    //Check
                    if(radioOtherType.isSelected()){
                        radioCppType.setSelected(false);
                        radioJavaType.setSelected(false);    
                    }
                }
            });
        }
        return radioOtherType;
    }

    public void run() {
        initialize();
    }

    /**
     * This method initializes outputMessage	
     * 	
     * @return javax.swing.JTextArea	
     */
    private JTextArea getOutputMessage() {
        if (outputMessage == null) {
            outputMessage = new JTextArea();
            outputMessage.setBounds(new Rectangle(9, 58, 91, 51));
            outputMessage.setForeground(Color.red);
            outputMessage.setEditable(false);
            outputMessage.setBackground(new Color(238, 238, 238));
        }
        return outputMessage;
    }

}
