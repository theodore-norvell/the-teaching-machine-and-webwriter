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
 * Created on 2006-11-27
 * project: FinalProject
 */
package editor.view.dialog;

import javax.swing.JPanel;
import java.awt.Frame;
import java.awt.BorderLayout;
import javax.swing.JDialog;
import javax.swing.JLabel;
import java.awt.Rectangle;
import javax.swing.JTextArea;
import java.awt.Dimension;
import javax.swing.JButton;
import javax.swing.WindowConstants;

/**
 * Editor About Dialog
 *@author hao sun
 *@version $Revision: 1.1 $
 *@since version 1.1
 **/

public class EditorAboutDialog extends JDialog  implements Runnable{

    private static final long serialVersionUID = 1L;

    private JPanel jContentPane = null;

    private JTextArea jTextArea = null;

    private JButton jButton = null;

    /**
     * @param owner
     */
    public EditorAboutDialog(Frame owner) {
        super(owner);
    }

    /**
     * This method initializes this
     * 
     * @return void
     */
    private void initialize() {
        this.setSize(300, 200);
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        this.setResizable(false);
        this.setTitle("About TM Editor");
        this.setContentPane(getJContentPane());
    }

    /**
     * This method initializes jContentPane
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getJContentPane() {
        if (jContentPane == null) {
            jContentPane = new JPanel();
            jContentPane.setLayout(null);
            jContentPane.setName("");
            jContentPane.add(getJTextArea(), null);
            jContentPane.add(getJButton(), null);
        }
        return jContentPane;
    }

    /**
     * This method initializes jTextArea	
     * 	
     * @return javax.swing.JTextArea	
     */
    private JTextArea getJTextArea() {
        if (jTextArea == null) {
            jTextArea = new JTextArea();
            jTextArea.setBounds(new Rectangle(10, 21, 271, 83));
            jTextArea.setPreferredSize(new Dimension(229, 106));
            jTextArea.setWrapStyleWord(true);
            jTextArea.setEditable(false);
            jTextArea.setLineWrap(true);
            jTextArea.setText("    This is a editor for Teaching Machine. Designed and coded by Hao Sun.");
        }
        return jTextArea;
    }

    /**
     * This method initializes jButton	
     * 	
     * @return javax.swing.JButton	
     */
    private JButton getJButton() {
        if (jButton == null) {
            jButton = new JButton();
            jButton.setBounds(new Rectangle(103, 125, 73, 24));
            jButton.setText("Close");
            jButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    dispose();
                }
            });
        }
        return jButton;
    }

    public void run() {
        initialize();
        setVisible(true);
    }

}
