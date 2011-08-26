/**
 * TerminalNodeView.java
 * 
 * @date: 2011-5-30
 * @author: Xiaoyu Guo
 * This file is part of the Teaching Machine project.
 */
package regex.view;

import higraph.view.HigraphView;

import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import regex.RegexDialog;
import regex.awt.GraphicsHelper;
import regex.model.RegexEdge;
import regex.model.RegexEdgeLabel;
import regex.model.RegexHigraph;
import regex.model.RegexNode;
import regex.model.RegexPayload;
import regex.model.RegexSubgraph;
import regex.model.RegexWholeGraph;
import regex.model.payload.TerminalPayload;
import regex.view.layout.RegexNodeLayoutManager;
import regex.view.layout.TerminalLayoutManager;
import tm.backtrack.BTTimeManager;

/**
 * @author Xiaoyu Guo
 *
 */
public class TerminalNodeView extends RegexNodeView {

    /** Maximum radius for round rectangle  */
    private static final int MAX_ROUND_RADIUS_PX = 10;
    private static final Color FILL_COLOR = Color.pink;
    
    public TerminalNodeView(
            HigraphView<RegexPayload, RegexEdgeLabel, RegexHigraph, RegexWholeGraph, RegexSubgraph, RegexNode, RegexEdge> v,
            RegexNode node,
            BTTimeManager timeMan) {
        super(v, node, timeMan);
        this.font = DEFAULT_FONT;
    }
    
    protected Font font;
    protected static final Font DEFAULT_FONT = new Font("Serif", Font.PLAIN, 14);

    public double getDesiredWidth(){
        return this.font.getSize()  / 2.1 * this.getNode().getPayload().getDescription().length();
    }
    
    public String getTerminal(){
        String result = "";
        try{
            result = ((TerminalPayload)(this.getNode().getPayload())).getTerminal();
        } catch (Exception e){
        }
        
        return result;
    }
    
    public void setTerminal(String value){
        try{
            ((TerminalPayload)(this.getNode().getPayload())).setTerminal(value);
        } catch (Exception e){
            
        }
    }
    
    /* (non-Javadoc)
     * @see regex.view.RegexNodeView#drawNode(java.awt.Graphics2D)
     */
    @Override
    protected void drawNode(Graphics2D screen) {
        Rectangle2D extent = this.getExtent();
        
        // border
        screen.setColor(this.colorVar.get());
        screen.drawRoundRect(
            (int)extent.getX(), 
            (int)extent.getY(),
            (int)extent.getWidth(),
            (int)extent.getHeight(), 
            MAX_ROUND_RADIUS_PX,
            MAX_ROUND_RADIUS_PX
        );

        // content
        if(this.getOutlineOnly() == false){
            // dealing with filling color
            Paint previousPaint = screen.getPaint();
            Paint gradientPaint = new GradientPaint(
                (float) this.getNextShapeExtent().getX(), 
                (float) this.getNextShapeExtent().getY(),
                GraphicsHelper.getGradientColor(FILL_COLOR),
                (float) this.getNextShapeExtent().getX(), 
                (float) this.getNextShapeExtent().getMaxY(),
                FILL_COLOR
            );
            screen.setPaint(gradientPaint);
            screen.fillRoundRect(
                (int)extent.getX() + 1, 
                (int)extent.getY() + 1,
                (int)extent.getWidth() - 1,
                (int)extent.getHeight() - 1, 
                MAX_ROUND_RADIUS_PX,
                MAX_ROUND_RADIUS_PX
            );
            screen.setPaint(previousPaint);
        }
        
        screen.setColor(this.getColor());
        if(this.getColor() == null){
            screen.setColor(Color.black);
        }
        if(this.getNode() != null && this.getNode().getPayload() != null){
            Font previousFont = screen.getFont();
            
            screen.setFont(this.font);
            GraphicsHelper.paintCenteredString(
                screen, 
                this.getNode().getPayload().getDescription(),
                extent.getCenterX(),
                extent.getCenterY()
            );
            
            screen.setFont(previousFont);
        }
    }

    /* (non-Javadoc)
     * @see regex.view.RegexNodeView#getLayoutHelper()
     */
    @Override
    protected RegexNodeLayoutManager getLayoutHelper() {
        return TerminalLayoutManager.getInstance();
    }
    
    public void handleDoubleClick(MouseEvent e){
        if(e.getButton() == MouseEvent.BUTTON1){
            String newValue = (String)JOptionPane.showInputDialog(
                null,
                "Editing value for Terminal Node",
                this.getTerminal()
            );

            if(newValue != null && newValue.length() > 0 && !(newValue.equals(this.getTerminal()))){
                this.setTerminal(newValue);
                this.getHigraphView().getHigraph().getWholeGraph().notifyObservers();
            }
        }
    }
}
