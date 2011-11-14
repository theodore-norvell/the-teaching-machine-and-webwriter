/**
 * VisreedNodeView.java
 * 
 * @date: 2011-5-25
 * @author: Xiaoyu Guo (MUN# 200982155)
 * This file was prepared by Xiaoyu Guo. It was completed by me alone.
 */
package visreed.view;

import higraph.view.HigraphView;
import higraph.view.NodeView;
import higraph.view.ZoneView;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.SystemColor;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.JPopupMenu;
import javax.swing.TransferHandler.TransferSupport;

import tm.backtrack.BTTimeManager;
import tm.backtrack.BTVar;
import tm.utilities.Assert;
import visreed.awt.GraphicsHelper;
import visreed.model.Direction;
import visreed.model.ISelectable;
import visreed.model.VisreedEdge;
import visreed.model.VisreedEdgeLabel;
import visreed.model.VisreedHigraph;
import visreed.model.VisreedNode;
import visreed.model.VisreedSubgraph;
import visreed.model.VisreedWholeGraph;
import visreed.model.payload.VisreedPayload;
import visreed.pattern.IObserver;
import visreed.swing.IInteractable;
import visreed.swing.menu.PopupMenuHelper;
import visreed.view.layout.IRotatable;
import visreed.view.layout.LayoutParameter;
import visreed.view.layout.SingleChildLayoutManager;
import visreed.view.layout.VisreedNodeLayoutManager;

/**
 * @author Xiaoyu Guo
 */
public abstract class VisreedNodeView 
extends NodeView<VisreedPayload, VisreedEdgeLabel, VisreedHigraph, VisreedWholeGraph, VisreedSubgraph, VisreedNode, VisreedEdge>
implements ISelectable, IRotatable, IInteractable, IObserver<VisreedNode> {
    /** Handles the LayoutManager */
    private final BTVar<VisreedNodeLayoutManager> layoutManagerVar;
    
    /** The flag is asserted when debugging layout managers. */
    protected static boolean IN_DEBUG_MODE = false;
    public static void setDebugMode(boolean value){
        IN_DEBUG_MODE = value;
    }
    public static boolean getDebugMode(){
        return IN_DEBUG_MODE;
    }
    
    /* Default attributes for drawing selection */
    private static final double POINT_RADIUS_PIXEL = 3.0;
    protected static final int SELECTED_BORDER_RADIUS_PIXEL = 10;
    protected static final float SELECTED_STROKE_WIDTH_PIXEL = 4.0F;
    protected static final Stroke SELECTED_BORDER_STROKE = new BasicStroke(
        SELECTED_STROKE_WIDTH_PIXEL
    );
    protected static final Stroke SELECTED_BORDER_DROPZONE_STROKE = new BasicStroke();
    protected static final Color SELECTED_BORDER_COLOR = SystemColor.textHighlight;

    protected VisreedNodeView(
        HigraphView<VisreedPayload, VisreedEdgeLabel, VisreedHigraph, VisreedWholeGraph, VisreedSubgraph, VisreedNode, VisreedEdge> v, 
        VisreedNode node,
        BTTimeManager timeMan
    ) {
        super(v, node, timeMan);
        this.layoutManagerVar = new BTVar<VisreedNodeLayoutManager>(timeMan);
        this.stretchVar = new BTVar<Rectangle2D>(timeMan);
        this.zoneMap = new HashMap<String, VisreedDropZone>();
        this.layoutParameter = new LayoutParameter();
    }
    
    /**
     * Only for debugging the layout manager.
     * <p>This function will draw a red border around the extent.
     * @param screen
     */
    protected void drawDebugBorder(Graphics2D screen){
        // use getExtent() to get the extent for dealing with drawing
        if(IN_DEBUG_MODE){
            Rectangle2D extent = this.getNextShapeExtent();
            Color previousColor = screen.getColor();
            screen.setColor(Color.red);
            // border
            screen.drawRect(
                (int)extent.getX(),
                (int)extent.getY(),
                (int)extent.getWidth(),
                (int)extent.getHeight()
            );
            
            // node name
            GraphicsHelper.paintStringOnTopLeft(
                screen,
                this.getNode().getPayload().getDescription(), 
                extent.getX(),
                extent.getY()
            );
            
            // entry point
            GraphicsHelper.fillOval(screen, getEntryPoint(), POINT_RADIUS_PIXEL);
            
            // exit point
            GraphicsHelper.fillOval(screen, getExitPoint(), POINT_RADIUS_PIXEL);
            screen.setColor(previousColor);
        }
    }
    
    /* (non-Javadoc)
     * @see higraph.view.NodeView#toString()
     */
    @Override
    public String toString(){
        String nodeType = getNode() == null ? "null" : getNode().getPayload().getDescription();
        String nodeValue = getNode() == null ? "" : getNode().getPayload().format(getNode());
        return nodeType + "_\"" + nodeValue + "\"";
    }
    
    /* (non-Javadoc)
     * @see higraph.view.NodeView#drawSelf(java.awt.Graphics2D)
     */
    @Override
    protected final void drawSelf(Graphics2D screen){
        // draw self
        this.drawNode(screen);
        this.drawString(screen);
        
        // draw children
        for(int i = 0; i < this.getNumChildren(); i++){
            VisreedNodeView child = this.getVisreedChild(i);
            if(child != null){
                child.drawNode(screen);
                child.draw(screen);
                child.drawString(screen);
            }
        }
        
        drawSelection(screen);
        drawHover(screen);
        drawDebugBorder(screen);
    }

    /**
     * @param screen
     */
    protected void drawSelection(Graphics2D screen) {
        if (this.isSelected()){
            Rectangle2D extent = this.getExtent();
            Color previousColor = screen.getColor();
            Stroke previousStroke = screen.getStroke();
            
            screen.setColor(SELECTED_BORDER_COLOR);
            screen.setStroke(SELECTED_BORDER_STROKE);
            
            // selection border
            screen.drawRoundRect(
                (int)extent.getX(),
                (int)extent.getY(),
                (int)extent.getWidth(),
                (int)extent.getHeight(), 
                SELECTED_BORDER_RADIUS_PIXEL, 
                SELECTED_BORDER_RADIUS_PIXEL
            );
            
            // drop zones
            screen.setStroke(SELECTED_BORDER_DROPZONE_STROKE);
            Iterator<ZoneView<VisreedPayload, VisreedEdgeLabel, VisreedHigraph, VisreedWholeGraph, VisreedSubgraph, VisreedNode, VisreedEdge>> iter = this.getZoneIterator();
            while(iter.hasNext()){
                ZoneView<VisreedPayload, VisreedEdgeLabel, VisreedHigraph, VisreedWholeGraph, VisreedSubgraph, VisreedNode, VisreedEdge> zone = iter.next();
                screen.drawRect(
                    (int)zone.getNextX(),
                    (int)zone.getNextY(),
                    (int)zone.getNextWidth(),
                    (int)zone.getNextHeight()
                );
            }
            
            screen.setStroke(previousStroke);
            screen.setColor(previousColor);
        }
    }
    
    /**
     * @param screen
     */
    protected void drawHover(Graphics2D screen) {
        if (this.isHoverOn() && !this.isSelected()){
            // hover has lower priority than selection
            Rectangle2D extent = this.getExtent();
            Color previousColor = screen.getColor();
            Stroke previousStroke = screen.getStroke();
            
            screen.setColor(SELECTED_BORDER_COLOR);
            screen.setStroke(SELECTED_BORDER_STROKE);
            
            // selection border
            screen.drawRoundRect(
                (int)extent.getX(),
                (int)extent.getY(),
                (int)extent.getWidth(),
                (int)extent.getHeight(), 
                SELECTED_BORDER_RADIUS_PIXEL, 
                SELECTED_BORDER_RADIUS_PIXEL
            );
            
            // drop zones
            screen.setStroke(SELECTED_BORDER_DROPZONE_STROKE);
            Iterator<ZoneView<VisreedPayload, VisreedEdgeLabel, VisreedHigraph, VisreedWholeGraph, VisreedSubgraph, VisreedNode, VisreedEdge>> iter = this.getZoneIterator();
            while(iter.hasNext()){
                ZoneView<VisreedPayload, VisreedEdgeLabel, VisreedHigraph, VisreedWholeGraph, VisreedSubgraph, VisreedNode, VisreedEdge> zone = iter.next();
                screen.drawRect(
                    (int)zone.getNextX(),
                    (int)zone.getNextY(),
                    (int)zone.getNextWidth(),
                    (int)zone.getNextHeight()
                );
            }
            
            screen.setStroke(previousStroke);
            screen.setColor(previousColor);
        }
    }
    
    /**
     * Handles painting of the node
     * @param screen
     */
    protected abstract void drawNode(Graphics2D screen);
    
    /**
     * Handles painting of any strings.
     * @param screen
     */
    protected void drawString(Graphics2D screen) {}
    
    /* (non-Javadoc)
     * @see higraph.view.NodeView#doLayout()
     */
    public void doLayout(){
        // refresh drop zones
        refreshDropZone();
        getLayoutManager().layoutNode(this);
    }
    
    public void doTransition(){
        for(VisreedDropZone zone : this.zoneMap.values()){
            zone.doTransition();
        }
        super.doTransition();
    }
    
    /**
     * Gets the corresponding {@link VisreedNodeLayoutManager} of this view
     * @return the specified LayoutManager, default if not specified.
     */
    public VisreedNodeLayoutManager getLayoutManager(){        
        if(this.layoutManagerVar.get() == null){
            return this.getLayoutHelper();
        } else {
            return layoutManagerVar.get();
        }
    }

    public void setLayoutManager(VisreedNodeLayoutManager manager){
        this.layoutManagerVar.set( manager ) ;
    }
    
    private VisreedViewFactory factory;
    
    /**
     * @param regexViewFactory
     */
    public void setNodeViewFactory(VisreedViewFactory value) {
        this.factory = value;
    }
    
    public VisreedViewFactory getNodeViewFactory(){
        return this.factory;
    }
    /**
     * Gets the corresponding concrete NodeViewLayoutManager class
     * by default this will return the {@link SingleChildLayoutManager}
     * @return
     */
    protected VisreedNodeLayoutManager getLayoutHelper(){
    	return SingleChildLayoutManager.getInstance();
    };
    
    /**
     * Gets the ith child and cast it to {@link visreed.view.VisreedNodeView}
     * @param i
     * @return
     */
    public VisreedNodeView getVisreedChild(int i){
        NodeView<VisreedPayload, VisreedEdgeLabel, VisreedHigraph, VisreedWholeGraph, VisreedSubgraph, VisreedNode, VisreedEdge> kid = this.getChild(i);
        if(kid instanceof VisreedNodeView){
            return (VisreedNodeView)kid;
        } else {
            return null;
        }
    }
    
    /** handing entry and exit points */
//    private Point2D entryPoint;
//    private Point2D exitPoint;
    
//    private double entryOffsetX = 0.0;
    private double entryOffsetY = 0.0;
    
    /**
     * Gets the entry point 
     * @return
     */
    public Point2D getEntryPoint(){
//        if(this.entryPoint != null){
//            return this.entryPoint;
//        }
        Rectangle2D extent = this.getNextShapeExtent();
        Point2D result = new Point2D.Double(
            extent.getX(), extent.getCenterY() + entryOffsetY
        );
        
        if(this.getCurrentDirection().equals(Direction.WEST)){
            result = new Point2D.Double(
                extent.getMaxX(), extent.getCenterY() + entryOffsetY
            );
        }
        
        return result;
    }
    
//    public void setEntryPoint(Point2D value){
//        this.entryPoint = value;
//    }
    
    /**
     * Gets the exit point
     * @return
     */
    public Point2D getExitPoint(){
//        if(this.exitPoint != null){
//            return this.exitPoint;
//        }
        Rectangle2D extent = this.getNextShapeExtent();
        Point2D result = new Point2D.Double(
            extent.getMaxX(), extent.getCenterY() + entryOffsetY
        );
        
        if(this.getCurrentDirection().equals(Direction.WEST)){
            result = new Point2D.Double(
                extent.getX(), extent.getCenterY() + entryOffsetY
            );
        }
        
        return result;
    }
    
//    public void setExitPoint(Point2D value){
//        this.exitPoint = value;
//    }
    
    public void setEntryOffsetY(double value){
    	this.entryOffsetY = value;
    }
    
    /* The stretch handler */
    private BTVar<Rectangle2D> stretchVar;
    void setStretch(Rectangle2D value){
        stretchVar.set(value);
    }
    public void setStretchWidth(double value){
        Rectangle2D currentValue = (Rectangle2D) this.getStretch().clone();
        currentValue.setRect(0, 0, value, currentValue.getHeight());
        this.setStretch(currentValue);
    }
    
    public void setStretchHeight(double value){
        Rectangle2D currentValue = (Rectangle2D) this.getStretch().clone();
        currentValue.setRect(0, 0, currentValue.getWidth(), value);
        this.setStretch(currentValue);
    }
    
    public Rectangle2D getStretch(){
        return (stretchVar.get()==null) ? new Rectangle2D.Double() : stretchVar.get();
    }
    
    /**
     * Resets all the previously saved parameters for layout.<br />
     * This function should be called when changing the structure of the node
     * and re-layout is needed.
     */
    public void resetLayout(){
    	this.setStretch(null);
//    	this.setEntryPoint(null);
//    	this.setExitPoint(null);
    }
    
//    /* (non-Javadoc)
//     * @see higraph.view.NodeView#translateNext(double, double)
//     */
//    @Override   
//    public void translateNext(double dx, double dy) {
//        super.translateNext(dx, dy);
//        if(this.entryPoint != null){
//            entryPoint.setLocation(entryPoint.getX() + dx, entryPoint.getY() + dy);
//        }
//        if(this.exitPoint != null){
//            exitPoint.setLocation(exitPoint.getX() + dx, exitPoint.getY() + dy);
//        }
//    }
    
    /* Handles selection */

    /* (non-Javadoc)
     * @see visreed.model.ISelectable#isSelected()
     */
    @Override
    public boolean isSelected() {
        if(this.getNode() == null){
            return false;
        }
        return this.getNode().isSelected();
    }

    /* (non-Javadoc)
     * @see visreed.model.ISelectable#select()
     */
    @Override
    public final void select() {
        setSelected(true);
    }

    /* (non-Javadoc)
     * @see visreed.model.ISelectable#deSelect()
     */
    @Override
    public final void deSelect() {
        setSelected(false);
    }

    /* (non-Javadoc)
     * @see visreed.model.ISelectable#setSelected(boolean)
     */
    @Override
    public void setSelected(boolean selected) {
        if(this.getNode() != null){
            this.getNode().setSelected(selected);
        }
    }
    
    /**
     * Returns whether the node view intersects with the specified marquee
     * selection box
     * @param marquee the desired marquee selection {@link java.awt.geom.Rectangle2D}
     * @return {@value true} if the marquee intersects with this node, {@value false} otherwise
     */
    public boolean intersects(Rectangle2D marquee){
        return this.getNextShapeExtent().intersects(marquee);
    }

    /* (non-Javadoc)
     * @see visreed.swing.IInteractable#setHoverOn()
     */
    @Override
    public final void setHoverOn(){
        this.getNode().setHoverOn();
    }
    
    /* (non-Javadoc)
     * @see visreed.swing.IInteractable#setHoverOff()
     */
    @Override
    public final void setHoverOff(){
        this.getNode().setHoverOff();
        // sets all its zones hovered off
        for(VisreedDropZone dz : this.zoneMap.values()){
            if(dz.isHoverOn()){
                dz.setHoverOff();
            }
        }
    }

    /* (non-Javadoc)
     * @see visreed.swing.IInteractable#isHoverOn()
     */
    @Override
    public boolean isHoverOn(){
        return this.getNode().isHoverOn();
    }
    
    /** Handling Drops */
    
    /* (non-Javadoc)
     * @see visreed.swing.IInteractable#isDropAccepted()
     */
    @Override
    public boolean isDropAccepted(){
        return false;
    }
    
    /* (non-Javadoc)
     * @see visreed.swing.IInteractable#handleDrop(java.util.List)
     */
    @Override
    public void handleDrop(TransferSupport support, List<VisreedNode> nodes){}

    /* (non-Javadoc)
     * @see visreed.swing.IInteractable#handleClick(java.awt.event.MouseEvent)
     */
    @Override
    public void handleClick(MouseEvent e) {}

    /* (non-Javadoc)
     * @see visreed.swing.IInteractable#handleDoubleClick(java.awt.event.MouseEvent)
     */
    @Override
    public void handleDoubleClick(MouseEvent e) {}
    
    /* (non-Javadoc)
     * @see visreed.swing.IInteractable#getPopupMenu(java.awt.event.MouseEvent)
     */
    @Override
    public JPopupMenu getPopupMenu(MouseEvent e){
        return PopupMenuHelper.getPopupMenu(this);
    }
    
    /** Zone Handling */
    protected Map<String, VisreedDropZone> zoneMap;
    
    public void addZone(VisreedDropZone zone){
        super.addZone(zone);
        this.zoneMap.put(zone.getId(), zone);
    }
    
    public void removeZone(VisreedDropZone zone){
        super.removeZone(zone);
        this.zoneMap.remove(zone.getId());
    }
    
    public final void removeZones(){
        Iterator<ZoneView<VisreedPayload, VisreedEdgeLabel, VisreedHigraph, VisreedWholeGraph, VisreedSubgraph, VisreedNode, VisreedEdge>> itor = this.getZoneIterator();
        while(itor.hasNext()){
            this.removeZone(itor.next());
        }
        this.zoneMap.clear();
    }
    
    @Override
    public VisreedDropZone findZone(String id){
        Assert.check(id != null, "Can only search for a non-null id");
        return this.zoneMap.get(id);
    }
    
    /**
     * Check all the drop zones of this node view. <br />
     * Default implementation will do nothing.
     */
    public final void refreshDropZone(){
        if(this.shouldRefreshDropZone()){
            this.reCreateDropZone();
        }
    }
    
    /**
     * Nodes with drop zones should return true.
     * @return
     */
    protected boolean shouldRefreshDropZone(){
        return false;
    }
    
    /**
     * Re-create all the drop zones to match the current children set. <br />
     * Precondition: shouldRefreshDropZone() == true;
     */
    protected void reCreateDropZone(){
    }
    

    /**
     * Move the zones
     * <p> Stack: doLayout() -> placeNext() -> translateNext() </p>
     * <p> Precondition: <pre>shouldRefreshDropZone() == true; </pre> </p>
     * <p> Note: the concrete work is handled in LayoutManagers. </p>
     */
    @Override
    public void moveZone(
        ZoneView<VisreedPayload, VisreedEdgeLabel, VisreedHigraph, VisreedWholeGraph, VisreedSubgraph, VisreedNode, VisreedEdge> zone)
    {
        Assert.check(zone instanceof VisreedDropZone);
        this.getLayoutManager().layoutZone(this, (VisreedDropZone)zone);
    }
    
    /**
     * Create an Drop Zone and add it to the zone list.
     * @param id the id of the drop zone.
     * @param number the index of the children (0, 1, ...), default is 0.
     * @return the created drop zone, null if fails.
     */
    protected final VisreedDropZone makeAndAddDropZone(String id, int number){
        if(this.mayAddZones() && this.findZone(id) == null){
            VisreedDropZone zone = this.getNodeViewFactory().makeDropZone(this);
            zone.setId(id);
            zone.setNodeNumber(number);
            this.addZone(zone);
            return zone;
        } else {
            return null;
        }
    }
    
    protected final VisreedDropZone makeAndAddInsertChildDropZone(String id, int number){
        if(this.mayAddZones() && this.findZone(id) == null){
            VisreedDropZone zone = this.getNodeViewFactory().makeInsertChildDropZone(this);
            zone.setId(id);
            zone.setNodeNumber(number);
            this.addZone(zone);
            return zone;
        } else {
            return null;
        }
    }
    
    /**
     * @param point
     */
    public final void handleZoneHovering(Point point) {
        for(VisreedDropZone dz : this.zoneMap.values()){
            if(dz.getNextShapeExtent().contains(point)){
                dz.setHoverOn();
            } else {
                dz.setHoverOff();
            }
        }
    }
    
    
    /* Handling rotation */
    protected LayoutParameter layoutParameter;
    
    public void setLayoutParameter(LayoutParameter value){
    	if(!value.equals(this.layoutParameter)){
    		this.layoutParameter = value;
    		this.refresh();
    	}
    }
    
    public LayoutParameter getLayoutParameter(){
    	return this.layoutParameter;
    }
    
    /* (non-Javadoc)
     * @see visreed.view.IRotatable#getCurrentDirection()
     */
    @Override
    public final Direction getCurrentDirection(){
        return this.layoutParameter.getCurrentDirection();
    }
    
    /* (non-Javadoc)
     * @see visreed.view.IRotatable#setDirection(visreed.model.Direction)
     */
    @Override
    public void setDirection(Direction newDirection){
        if(! newDirection.equals(this.getCurrentDirection())){
            // TODO calculate the difference with old direction
            this.layoutParameter.setDirection(newDirection);
            
            // TODO rotate all children
            for(int i = 0; i < this.getNumChildren(); i++){
                this.getVisreedChild(i).setDirection(newDirection);
            }
        }
    }
    
    /* (non-Javadoc)
     * @see visreed.view.IRotatable#reverseDirection()
     */
    @Override
    public final void reverseDirection(){
        this.layoutParameter.reverseDirection();
        for(int i = 0; i < this.getNumChildren(); i++){
            this.getVisreedChild(i).reverseDirection();
        }
    }
    
    /* (non-Javadoc)
     * @see visreed.pattern.IObserver#changed(visreed.pattern.IObservable)
     */
    @Override
    public void changed(VisreedNode o){
    	// force the view to re-paint
    	this.resetLayout();
    	this.refresh();
    }
}
