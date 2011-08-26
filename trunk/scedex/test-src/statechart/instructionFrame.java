package statechart;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.TextArea;

import javax.swing.JFrame;

public class instructionFrame extends JFrame {
    private TextArea textArea1 = new TextArea();

    public instructionFrame() {
        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void jbInit() throws Exception {
        this.getContentPane().setLayout( null );
        this.setSize(new Dimension(214, 300));
        textArea1.setBounds(new Rectangle(0, 0, 210, 275));
        textArea1.setText("this fuction is not ready yet");
        this.getContentPane().add(textArea1, null);
    }
}
