/**
 * SourceCodeParagraphView.java
 * 
 * @date: Jun 27, 2011
 * @author: Xiaoyu Guo
 * This file is part of the Teaching Machine project.
 */
package visreed.swing;

import javax.swing.text.Element;
import javax.swing.text.ParagraphView;

/**
 * @author Xiaoyu Guo
 *
 */
public class SourceCodeParagraphView extends ParagraphView {

    /**
     * @param elem
     */
    public SourceCodeParagraphView(Element elem) {
        super(elem);
    }

    /* (non-Javadoc)
     * @see javax.swing.text.FlowView#layout(int, int)
     */
    @Override
    protected void layout(int width, int height) {
        // turn off text wrapping
        super.layout(Short.MAX_VALUE, height);
    }
    
    /* (non-Javadoc)
     * @see javax.swing.text.BoxView#getMinimumSpan(int)
     */
    @Override
    public float getMinimumSpan(int axis) {
        return super.getPreferredSpan(axis);
    }
}
