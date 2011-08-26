package jsnoopy.swingui;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

/**
 * <p>Title: JSnoopy</p>
 * <p>Description: Regression testing based on event sequences.</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Memorial University of Newfoundland</p>
 * @author Theodore S. Norvell
 * @version 1.0
 */

class TextEditor extends JDialog {
    private BorderLayout borderLayout1 = new BorderLayout();
    private JScrollPane jScrollPane1 = new JScrollPane();
    private JTextPane jTextPane = new JTextPane();
    private JButton dismissButton = new JButton();

    public TextEditor(JFrame owner ) {
        super(owner, "Replay complete", true ) ;
        try {
            jbInit();
            pack() ;
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
    private void jbInit() throws Exception {
        this.getContentPane().setLayout(borderLayout1);
        dismissButton.setText("Dismiss");
        dismissButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dismissButton_actionPerformed(e);
            }
        });
        jTextPane.setMinimumSize(new Dimension(200, 100));
        jTextPane.setPreferredSize(new Dimension(200, 200));
        jScrollPane1.setMinimumSize(new Dimension(200, 200));
        jScrollPane1.setPreferredSize(new Dimension(200, 200));
        this.getContentPane().add(jScrollPane1, BorderLayout.CENTER);
        this.getContentPane().add(dismissButton, BorderLayout.SOUTH);
        jScrollPane1.getViewport().add(jTextPane, null);
    }

    void setText( String text ) {
        jTextPane.setText( text ); }

    String getText() {
        return jTextPane.getText() ; }

    void dismissButton_actionPerformed(ActionEvent e) {
        setVisible(false) ;
    }
}