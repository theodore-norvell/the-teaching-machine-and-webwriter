/**
 * RegexJComponent.java
 * 
 * @date: 2011-6-15
 * @author: Xiaoyu Guo
 * This file is part of the Teaching Machine project.
 */
package regex.swing;

import higraph.swing.HigraphJComponent;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import javax.swing.Scrollable;
import javax.swing.SwingConstants;

import regex.model.IRegexHigraphObserver;
import regex.model.RegexHigraph;
import regex.view.RegexHigraphView;

/**
 * The RegexJComponent extends {@link HigraphJComponent} with additional
 * abilities with {@link Scrollable} and {@link IRegexHigraphObserver}.
 * @author Xiaoyu Guo
 */
public class RegexJComponent
extends HigraphJComponent 
implements Scrollable, IRegexHigraphObserver
{
    private static final long serialVersionUID = 9172817281342310471L;

    /** The flag is asserted when debugging layout managers. */
    protected static final boolean IN_DEBUG_MODE = false;
    
    public RegexJComponent() {
        super();
        this.drawingArea = new Dimension();
        this.setAutoscrolls(true);
    }
    
    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);

        if( higraphView != null ) {
            higraphView.drawArea( (Graphics2D) g ) ;
        }
        
        if(IN_DEBUG_MODE){
            // draw border
            Color previousColor = g.getColor();
            g.setColor(Color.red);
            g.drawRect(0, 0, this.getWidth() - 1, this.getHeight() - 1);
            g.setColor(previousColor);
        }
    }
    
    private RegexHigraphView higraphView;
    
    public void setSubgraphView( RegexHigraphView view ){
        if(this.higraphView != null){
            this.higraphView.getHigraph().deRegisterObserver(this);
        }
        this.higraphView = view ;
        this.higraphView.getHigraph().registerObserver(this);
    }
    
    public RegexHigraphView getSubgraphView(){
        return this.higraphView;
    }
    
    /* (non-Javadoc)
     * @see javax.swing.JComponent#getPreferredSize()
     */
    @Override
    public Dimension getPreferredSize() {
        if (this.drawingArea == null) {
            // no known size, choose parent's size
            return this.getParent().getSize();
        } else {
            // hack! 
            return new Dimension(
                (int)Math.max(this.drawingArea.getWidth(), this.getParent().getSize().getWidth()),
                (int)Math.max(this.drawingArea.getHeight(), this.getParent().getSize().getHeight())
            );
        }
    }
    
    /* required for Scrollable */
    private int maxUnitIncrement = 20;
 
    private Dimension drawingArea;
    
    /* (non-Javadoc)
     * @see javax.swing.Scrollable#getPreferredScrollableViewportSize()
     */
    @Override
    public Dimension getPreferredScrollableViewportSize() {
        // believe it or not, this is not working. 
        // after an hour I finally found that the size should be 
        // calculated in getPreferredSize()
        return this.drawingArea;
    }

    /* (non-Javadoc)
     * @see javax.swing.Scrollable#getScrollableUnitIncrement(java.awt.Rectangle, int, int)
     */
    @Override
    public int getScrollableUnitIncrement(
        Rectangle visibleRect, 
        int orientation, 
        int direction
    ) {
        //Get the current position.
        int currentPosition = 0;
        if (orientation == SwingConstants.HORIZONTAL) {
            currentPosition = visibleRect.x;
        } else {
            currentPosition = visibleRect.y;
        }

        //Return the number of pixels between currentPosition
        //and the nearest tick mark in the indicated direction.
        if (direction < 0) {
            int newPosition = currentPosition -
                             (currentPosition / maxUnitIncrement)
                              * maxUnitIncrement;
            return (newPosition == 0) ? maxUnitIncrement : newPosition;
        } else {
            return ((currentPosition / maxUnitIncrement) + 1)
                   * maxUnitIncrement
                   - currentPosition;
        }
    }
    

    /* (non-Javadoc)
     * @see javax.swing.Scrollable#getScrollableBlockIncrement(java.awt.Rectangle, int, int)
     */
    @Override
    public int getScrollableBlockIncrement(
        Rectangle visibleRect,
        int orientation, 
        int direction
    ) {
        if (orientation == SwingConstants.HORIZONTAL) {
            return visibleRect.width - maxUnitIncrement;
        } else {
            return visibleRect.height - maxUnitIncrement;
        }
    }
    

    /* (non-Javadoc)
     * @see javax.swing.Scrollable#getScrollableTracksViewportWidth()
     */
    @Override
    public boolean getScrollableTracksViewportWidth() {
        return false;
    }

    /* (non-Javadoc)
     * @see javax.swing.Scrollable#getScrollableTracksViewportHeight()
     */
    @Override
    public boolean getScrollableTracksViewportHeight() {
        return false;
    }
    
    
    /* (non-Javadoc)
     * @see regex.model.IRegexHigraphObserver#changed(regex.model.RegexSubgraph)
     */
    @Override
    public void changed(RegexHigraph regexHigraph) {
        // determine the new deminsion of drawingArea
        this.drawingArea = this.getSubgraphView().getDrawingArea();
        this.revalidate();
    }
}
