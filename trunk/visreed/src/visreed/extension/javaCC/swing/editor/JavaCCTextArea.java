/**
 * JavaCCTextArea.java
 * 
 * @date: Oct 3, 2011
 * @author: Xiaoyu Guo
 * This file is part of the Teaching Machine project.
 */
package visreed.extension.javaCC.swing.editor;

import java.util.List;

import javax.swing.JOptionPane;

import visreed.extension.javaCC.model.JavaCCWholeGraph;
import visreed.extension.javaCC.parser.ParseException;
import visreed.model.VisreedNode;
import visreed.swing.editor.VisreedTextArea;

/**
 * @author Xiaoyu Guo
 *
 */
public class JavaCCTextArea extends VisreedTextArea {

	private static final long serialVersionUID = 5410193452747522580L;

    /**
     * Try to parse the regular expression
     * @return {@value true} if the parse succeeded, {@value false} otherwise
     */
	@Override
    public boolean tryParseText(){
        try {
            VisreedNode root = JavaCCWholeGraph.construct((JavaCCWholeGraph) wholeGraph, this.getText());
            if(root == null){
                return false;
            }
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(
                null,
                e,
                "Parse error",
                JOptionPane.ERROR_MESSAGE
            );
            return false;
        }
        return true;
    }

    /**
     * Refresh model from the (updated) text
     */
	@Override
    public void refreshFromText(){
        // update the model from this.getText()
        VisreedNode newTop;
        try {
            newTop = JavaCCWholeGraph.construct((JavaCCWholeGraph) this.wholeGraph, this.getText());
            if(newTop != null){
                List<VisreedNode> tops = this.graph.getTops();
                for(VisreedNode n : tops){
                    this.graph.removeTop(n);
                }
                this.graph.addTop(newTop);
            }
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            JOptionPane.showMessageDialog(
                null,
                e,
                "Parse error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }
}
