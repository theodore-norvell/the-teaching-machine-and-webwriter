package statechart;

import java.awt.Dimension;

import java.awt.Rectangle;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JToolBar;

public class mainFrame extends JFrame {
    
    private JMenu jMainMenu = new JMenu();
    private JMenuBar SCMenuBar = new JMenuBar();
    
    private JMenuItem jMenuItemNew = new JMenuItem();
    private JMenuItem jMenuItemSave = new JMenuItem();
    private JMenuItem jMenuItemLoad = new JMenuItem();
    private JMenuItem jMenuItemExit1 = new JMenuItem();
    
    private JMenu jMenuHelp = new JMenu();
    private JMenuItem jMenuItemInstruction = new JMenuItem();
    private JMenuItem jMenuItemExit = new JMenuItem();
    private JToolBar stateToolBar1 = new JToolBar();

    public mainFrame() {
        super ("state chart editor");
        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void jbInit() throws Exception {
        this.getContentPane().setLayout( null );
        this.setSize(new Dimension(885, 547));
        
        this.setJMenuBar(SCMenuBar);
        
        SCMenuBar.add(jMainMenu);
        SCMenuBar.add(jMenuHelp);
        
        jMainMenu.setText("Menu"); 
        jMenuItemNew.setText("create new state chart");
        jMenuItemNew.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                jMenuItemNew_actionPerformed(e);
            }
        });
        jMenuItemSave.setText("Save");
        jMenuItemLoad.setText("load");
        jMenuItemExit.setText("Exit");
        jMenuItemExit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                jMenuItemExit_actionPerformed(e);
            }
        });
        
        
        jMenuHelp.setText("Help");
        
        jMenuItemInstruction.setText("Instruction");
        jMenuItemInstruction.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                jMenuInstruction_actionPerformed(e);
            }
        });
        
        jMenuItemExit1.setText("Exit");
        jMenuItemExit1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                jMenuItemExit1_actionPerformed(e);
            }
        });
        
        
        stateToolBar1.setBounds(new Rectangle(735, 5, 140, 430));
        jMainMenu.add(jMenuItemNew);
        jMainMenu.add(jMenuItemSave);
        jMainMenu.add(jMenuItemLoad);
        jMainMenu.add(jMenuItemExit);
        
        jMenuHelp.add(jMenuItemInstruction);
        jMenuHelp.add(jMenuItemExit1);
       
        this.getContentPane().add(stateToolBar1, null);
    }

    private void jMenuItemExit_actionPerformed(ActionEvent e) {
    	System.exit(0);
    }

    private void jMenuInstruction_actionPerformed(ActionEvent e) {
        instructionFrame frame = new instructionFrame();
        frame.setVisible(true);
        
    }
    private void jMenuItemExit1_actionPerformed(ActionEvent e) {
    	System.exit(0);
    }
    private void jMenuItemNew_actionPerformed(ActionEvent e) {
    	stateFrame frame = new stateFrame();
        frame.setVisible(true);
    }
}
