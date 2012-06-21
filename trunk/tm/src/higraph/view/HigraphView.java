//     Copyright 1998--2010 Michael Bruce-Lockhart and Theodore S. Norvell
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

package higraph.view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RectangularShape;
import java.awt.geom.RoundRectangle2D;
import java.util.Iterator;
import java.util.Vector;

import tm.backtrack.*;
import tm.utilities.Assert;

import higraph.model.interfaces.*;
import higraph.view.layout.AbstractLayoutManager;

/**
 * <p>The primary viewing mechanism for higraph.</p>
 * 
 * <p>Represents one view of a
 * {@link higraph.model.interfaces.Higraph higraph}.
 * Higraph views always take their structure from the higraph in the model with
 * which they are associated. To ensure this discipline, the individual
 * {@link ComponentView ComponentViews} which make up the higraph view are
 * actually listed with the model component of the model higraph.</p>
 * 
 * <p>Thus, changing the structure of the model will automatically change the
 * structure of all views associated with it.</p>
 * 
 * <p>Each higraph view must have an associated {@link java.awt.Component}
 * which can be repainted by the host drawing system. That {@link java.awt.Component Component} should,
 * in turn, delegate its local painting to {@link #drawArea(Graphics2D)}.
 *
 * @param NP The Node Payload type
 * @param EP The Edge Payload type
 * @param HG The Higraph type 
 * @param WG The Whole Graph type
 * @param SG The Subgraph type
 * @param N  The Node type
 * @param E  The Edge type
 * 
 * @author mpbl
*/

public class HigraphView 
    < NP extends Payload<NP>,
      EP extends Payload<EP>,
      HG extends Higraph<NP,EP,HG,WG,SG,N,E>,
      WG extends WholeGraph<NP,EP,HG,WG,SG,N,E>,
      SG extends Subgraph<NP,EP,HG,WG,SG,N,E>,
      N extends Node<NP,EP,HG,WG,SG,N,E>, 
      E extends Edge<NP,EP,HG,WG,SG,N,E>
    >
{
	public static final char MARKER_RED = '\uffff';	
	public static final char MARKER_BLUE = '\ufffe';	
	public static final char MARKER_BLACK = '\ufffd';	
	public static final char MARKER_GREEN = '\ufffc';
	public static final char ENDMARKER = '\ufff0';
	
	public static final double DEFAULT_SHAPE_WIDTH = 30.0;
	public static final double DEFAULT_SHAPE_HEIGHT = 30.0;
	public static final double DEFAULT_ROUND_X = 5.0;
	public static final double DEFAULT_ROUND_Y = 5.0;


	private static final long serialVersionUID = 8550462309891566240L;
	
    private static final Color DEFAULT_NODE_COLOR = Color.black;
    private static final Color DEFAULT_NODE_FILL_COLOR = Color.blue;
	private static final Stroke DEFAULT_NODE_STROKE = new BasicStroke();
	private static final RectangularShape DEFAULT_NODE_SHAPE =
		new Ellipse2D.Double(0, 0, DEFAULT_SHAPE_WIDTH, DEFAULT_SHAPE_HEIGHT);
	
    private static final Color DEFAULT_EDGE_COLOR = Color.red;
	private static final Stroke DEFAULT_EDGE_STROKE = new BasicStroke(2.0F);
	private static final Color DEFAULT_TARGET_DECORATOR_COLOR = DEFAULT_EDGE_COLOR;
	private static final Stroke DEFAULT_TARGET_DECORATOR_STROKE = DEFAULT_EDGE_STROKE;
	private static final Color DEFAULT_SOURCE_DECORATOR_COLOR = DEFAULT_EDGE_COLOR;
	private static final Stroke DEFAULT_SOURCE_DECORATOR_STROKE = DEFAULT_EDGE_STROKE;
   
    private static final Color DEFAULT_BRANCH_COLOR = Color.black;
	private static final Stroke DEFAULT_BRANCH_STROKE = new BasicStroke(2.0F);
	private static final Color DEFAULT_PARENT_DECORATOR_COLOR = DEFAULT_BRANCH_COLOR;
	private static final Stroke DEFAULT_PARENT_DECORATOR_STROKE = DEFAULT_BRANCH_STROKE;
	private static final Color DEFAULT_CHILD_DECORATOR_COLOR = DEFAULT_BRANCH_COLOR;
	private static final Stroke DEFAULT_CHILD_DECORATOR_STROKE = DEFAULT_BRANCH_STROKE;

	// Used for all labels
	private static final Color DEFAULT_LABEL_COLOR = Color.black;
	private static final Color DEFAULT_LABEL_FILL_COLOR = null;

    private static final Color DEFAULT_ZONE_COLOR = Color.black;
    private static final Color DEFAULT_ZONE_FILL_COLOR = Color.yellow;
	private static final Stroke DEFAULT_ZONE_STROKE = new BasicStroke();
	private static final RectangularShape DEFAULT_ZONE_SHAPE =
		new Rectangle2D.Double(0, 0, DEFAULT_SHAPE_WIDTH, DEFAULT_SHAPE_HEIGHT);

	

	private final BTVar<Color> defaultNodeColorVar;
	private final BTVar<Color> defaultNodeFillColorVar;
	private final BTVar<Stroke> defaultNodeStrokeVar;
	private final BTVar<Double> defaultNodeWidthVar;
	private final BTVar<Double> defaultNodeHeightVar;
	private final BTVar<RectangularShape> defaultNodeShapeVar;

	private final BTVar<Color> defaultEdgeColorVar;
	private final BTVar<Stroke> defaultEdgeStrokeVar;
	private final BTVar<PointDecorator<NP,EP,HG,WG,SG,N,E>> defaultTargetDecoratorVar;
	private final BTVar<Color> defaultTargetDecoratorColorVar;
	private final BTVar<Stroke> defaultTargetDecoratorStrokeVar;
	private final BTVar<PointDecorator<NP,EP,HG,WG,SG,N,E>> defaultSourceDecoratorVar;
	private final BTVar<Color> defaultSourceDecoratorColorVar;
	private final BTVar<Stroke> defaultSourceDecoratorStrokeVar;
	
	private final BTVar<Color> defaultBranchColorVar;
    private final BTVar<Stroke> defaultBranchStrokeVar;
	private final BTVar<PointDecorator<NP,EP,HG,WG,SG,N,E>> defaultParentDecoratorVar;
	private final BTVar<Color> defaultParentDecoratorColorVar;
	private final BTVar<Stroke> defaultParentDecoratorStrokeVar;
	private final BTVar<PointDecorator<NP,EP,HG,WG,SG,N,E>> defaultChildDecoratorVar;
	private final BTVar<Color> defaultChildDecoratorColorVar;
	private final BTVar<Stroke> defaultChildDecoratorStrokeVar;
    
	private final BTVar<Color> defaultLabelColorVar;
	private final BTVar<Color> defaultLabelFillColorVar;
	private final BTVar<Boolean> countLabelsVar;

	private final BTVar<Color> defaultZoneColorVar;
	private final BTVar<Color> defaultZoneFillColorVar;
	private final BTVar<Stroke> defaultZoneStrokeVar;
	private final BTVar<RectangularShape> defaultZoneShapeVar;
	private final BTVar<Boolean> countZonesVar;
	
	private class DisplayString{
		public String id;
		public StringBuilder theString;
		public Color baseColor;
		public int x;
		public int y;
		public DisplayString(String id){
			this.id = new String(id);
		}
	}
	
	private final BTVar<ViewFactory<NP,EP,HG,WG,SG,N,E>> viewFactoryVar ;
	private final HG myGraph ;
	// myDisplay being a component creates a dependency on the AWT that we will probably want to be rid of some day.
	private final BTVar<Component> myDisplayVar;
	private final BTVar<AbstractLayoutManager<NP,EP,HG,WG,SG,N,E>> layoutManagerVar;
	private final BTVar<String> titleVar;
	private final BTFunction<N, NodeView<NP,EP,HG,WG,SG,N,E>> nodeViewFunc;	
	private final BTFunction<E, EdgeView<NP,EP,HG,WG,SG,N,E>> edgeViewFunc;    
//	private final BTVar<Rectangle2D> extentVar;
	private final BTVar<Rectangle2D> nextExtentVar;
	private final BTVector<DisplayString> myStringVector;
	private final BTVector<Line2D.Double> lineVector;
	
	static final int STATIC=42, TRANSITIONING=13 ;
	final BTVar<Integer> animationState ;
	private final BTVar<Double> animationDegree ;


	protected HigraphView(ViewFactory<NP,EP,HG,WG,SG,N,E> viewFactory, HG theGraph, Component display, BTTimeManager timeMan) {
	    defaultNodeColorVar = new BTVar<Color>( timeMan, DEFAULT_NODE_COLOR);
	    defaultNodeFillColorVar = new BTVar<Color>( timeMan, DEFAULT_NODE_FILL_COLOR);
	    defaultNodeStrokeVar = new BTVar<Stroke>( timeMan, DEFAULT_NODE_STROKE);
	    defaultNodeWidthVar = new BTVar<Double>(timeMan, DEFAULT_SHAPE_WIDTH);
	    defaultNodeHeightVar = new BTVar<Double>(timeMan, DEFAULT_SHAPE_HEIGHT);
        defaultNodeShapeVar = new BTVar<RectangularShape>( timeMan, DEFAULT_NODE_SHAPE);

	    defaultEdgeColorVar = new BTVar<Color>( timeMan, DEFAULT_EDGE_COLOR);
	    defaultEdgeStrokeVar = new BTVar<Stroke>( timeMan, DEFAULT_EDGE_STROKE);
	    defaultTargetDecoratorVar = new BTVar<PointDecorator<NP,EP,HG,WG,SG,N,E>>(timeMan);
	    defaultTargetDecoratorColorVar = new BTVar<Color>( timeMan, DEFAULT_TARGET_DECORATOR_COLOR);
		defaultTargetDecoratorStrokeVar = new BTVar<Stroke>( timeMan, DEFAULT_TARGET_DECORATOR_STROKE);
	    defaultSourceDecoratorVar = new BTVar<PointDecorator<NP,EP,HG,WG,SG,N,E>>(timeMan);
		defaultSourceDecoratorColorVar = new BTVar<Color>( timeMan, DEFAULT_SOURCE_DECORATOR_COLOR);
		defaultSourceDecoratorStrokeVar = new BTVar<Stroke>( timeMan, DEFAULT_SOURCE_DECORATOR_STROKE);

	    defaultBranchColorVar = new BTVar<Color>(timeMan, DEFAULT_BRANCH_COLOR);
	    defaultBranchStrokeVar = new BTVar<Stroke>(timeMan, DEFAULT_BRANCH_STROKE);
	    defaultParentDecoratorVar = new BTVar<PointDecorator<NP,EP,HG,WG,SG,N,E>>(timeMan);
	    defaultParentDecoratorColorVar = new BTVar<Color>(timeMan, DEFAULT_PARENT_DECORATOR_COLOR);
		defaultParentDecoratorStrokeVar = new BTVar<Stroke>(timeMan, DEFAULT_PARENT_DECORATOR_STROKE);
	    defaultChildDecoratorVar = new BTVar<PointDecorator<NP,EP,HG,WG,SG,N,E>>(timeMan);
		defaultChildDecoratorColorVar = new BTVar<Color>(timeMan, DEFAULT_CHILD_DECORATOR_COLOR);
		defaultChildDecoratorStrokeVar = new BTVar<Stroke>(timeMan, DEFAULT_CHILD_DECORATOR_STROKE);
	 
	    defaultLabelColorVar = new BTVar<Color>(timeMan, DEFAULT_LABEL_COLOR);
	    defaultLabelFillColorVar = new BTVar<Color>(timeMan, DEFAULT_LABEL_FILL_COLOR);
	    countLabelsVar = new BTVar<Boolean>(timeMan, true); 

	    defaultZoneColorVar = new BTVar<Color>( timeMan, DEFAULT_ZONE_COLOR);
		defaultZoneFillColorVar = new BTVar<Color>( timeMan, DEFAULT_ZONE_FILL_COLOR);
		defaultZoneStrokeVar = new BTVar<Stroke>( timeMan, DEFAULT_ZONE_STROKE);;
		defaultZoneShapeVar = new BTVar<RectangularShape>( timeMan, DEFAULT_ZONE_SHAPE);
	    countZonesVar = new BTVar<Boolean>(timeMan, true); 
       
	    myGraph = theGraph ;
		myDisplayVar = new BTVar<Component>(timeMan, display);
		//id = theGraph.registerView(this);
		viewFactoryVar = new BTVar<ViewFactory<NP,EP,HG,WG,SG,N,E>>(timeMan, viewFactory);
		layoutManagerVar = new BTVar<AbstractLayoutManager<NP,EP,HG,WG,SG,N,E>>( timeMan ) ; // initially null.
		titleVar = new BTVar<String>( timeMan ) ;
		nodeViewFunc = new BTFunction<N, NodeView<NP,EP,HG,WG,SG,N,E>>(timeMan) ;
		edgeViewFunc = new BTFunction<E, EdgeView<NP,EP,HG,WG,SG,N,E>>(timeMan) ;
//		extentVar = new BTVar<Rectangle2D>( timeMan ) ; // initially next
		nextExtentVar = new BTVar<Rectangle2D>( timeMan ) ; 
		myStringVector = new BTVector<DisplayString>( timeMan ) ;
		lineVector = new BTVector<Line2D.Double>(timeMan);
		
		animationState = new BTVar<Integer>(timeMan, STATIC) ;
		animationDegree = new BTVar<Double>(timeMan, 1.0) ;
		
	}

	
	public void setLayoutManager(AbstractLayoutManager<NP,EP,HG,WG,SG,N,E> lm){
		layoutManagerVar.set( lm ) ;		
	}
	
	public AbstractLayoutManager<NP,EP,HG,WG,SG,N,E> getLayoutManager(){
		return layoutManagerVar.get() ;		
	}
	
	public ViewFactory<NP,EP,HG,WG,SG,N,E> getViewFactory() {
	    return viewFactoryVar.get();
	}
	
	/**
	 * Note that it will be assumed that extents includes the branches
	 * between generations but this is not formally computed (for reasons
	 * of efficiency). It is possible to construct pathological cases
	 * where a branch extends outside the extent of its parent as
	 * defined here.
	 * 
	 * 
	 * @return the smallest rectangle that encloses the bounding rectangles
	 *         of this node plus all its descendants plus all the
	 *         edges it governs
	
	
	public Rectangle2D getNextExtent(){
		// lazy evaluation. Requires doTransition() to clear nextExtent
		if (nextExtentVar.get() == null) {
		    Rectangle2D newExtent = new Rectangle();
			for(N node:myGraphVar.get().getTops())
				newExtent=newExtent.createUnion(getNodeView(node).getNextExtent());
			nextExtentVar.set( newExtent ) ;
		}
		return nextExtentVar.get();
	}*/
	
	public void setNextExtent(Rectangle2D extent){
		nextExtentVar.set(extent);
	}
	
	/*public Rectangle2D getExtent(){
		return extentVar.get();
	} */
	
	/** Get ready to animate. This means doing layout.
	 * 
	 */
	public void startTransition(){
		if( animationState.get() == TRANSITIONING ) {
			finishTransition() ; }
		// Animation state is STATIC
		if (layoutManagerVar.get() != null){
			layoutManagerVar.get().layoutLocal(this);
			
			Rectangle2D viewExtent = new Rectangle2D.Double();
			Iterator<NodeView<NP,EP,HG,WG,SG,N,E>> iterator = getTops();
			while(iterator.hasNext()){
				NodeView<NP,EP,HG,WG,SG,N,E> top = iterator.next();
				Rectangle2D.union(viewExtent, top.getNextExtent(), viewExtent);
				top.startTransition();
			}
			for(E edge:myGraph.getEdges())
				getEdgeView(edge).startTransition();
			myDisplayVar.get().setPreferredSize(new Dimension((int)(viewExtent.getWidth()+0.5), (int)(viewExtent.getHeight()+0.5)));
			animationState.set( TRANSITIONING ) ;
		}
//		myDisplayVar.get().repaint() ;
	}
	
	public void advanceTransition( double degree ) {
		int state = animationState.get() ;
		if( state == STATIC ) {
			startTransition() ; }
		Iterator<NodeView<NP,EP,HG,WG,SG,N,E>> iterator = getTops();
		while(iterator.hasNext()){
			NodeView<NP,EP,HG,WG,SG,N,E> top = iterator.next();
			top.advanceTransition( degree ); }
		for(E edge:myGraph.getEdges())
			getEdgeView(edge).advanceTransition(degree);
		myDisplayVar.get().repaint() ;
	}
	
	public void finishTransition() {
		if( animationState.get() == TRANSITIONING) {
			Iterator<NodeView<NP,EP,HG,WG,SG,N,E>> iterator = getTops();
			while(iterator.hasNext()){
				NodeView<NP,EP,HG,WG,SG,N,E> top = iterator.next();
				top.finishTransition(); }
			
			for(E edge:myGraph.getEdges())
				getEdgeView(edge).finishTransition(); }
		animationState.set(STATIC) ;
		myDisplayVar.get().repaint() ;
	}
	
	/** Return null if the argument is null or there is no existing view and none can be made,
	 * otherwise return a view, making one if needed.
	 * In particular, this routine might return a view even if the
	 * node has been deleted or moved out of this higraph.
	 * @param node
	 * @return
	 */
	public NodeView<NP,EP,HG,WG,SG,N,E> getNodeView(N node){
	    if (node == null) return null;
        NodeView<NP,EP,HG,WG,SG,N,E> view = nodeViewFunc.at(node) ;
	    if( view != null ) {
	        return view ; }
	    else if( !node.deleted() && getHigraph().contains(node)){
	        view = viewFactoryVar.get().makeNodeView(this, node) ;
	        nodeViewFunc.map(node, view) ;
	        return view ; }
	    else return null ;
	}
    
    /** Return null if the argument is null or there is no existing view and none can be made,
     * otherwise return a view, making one if needed.
     * In particular, this routine might return a view even if the
     * edge has been deleted or moved out of this higraph.
     * @param node
     * @return
     */
    public EdgeView<NP,EP,HG,WG,SG,N,E> getEdgeView(E edge){
    	if (edge == null) return null;
        EdgeView<NP,EP,HG,WG,SG,N,E> view = edgeViewFunc.at(edge) ;
        if( view != null ) {
            return view ;
        } else if( !edge.deleted() && getHigraph().contains(edge)){
        	ViewFactory<NP, EP, HG, WG, SG, N, E> vf = viewFactoryVar.get();
            view = vf.makeEdgeView(this, edge) ;
            edgeViewFunc.map(edge, view) ;
           return view ; }
        else return null ;
    }
	
	public TopIterator getTops(){
		return new TopIterator();
	}
	
	
/*	public BranchView<NP,EP,HG,WG,SG,N,E> getBranchView(N node){
		if(node.getParent() == null) return null;
		NodeView<NP,EP,HG,WG,SG,N,E> nodeView = (NodeView<NP,EP,HG,WG,SG,N,E>) node.getView(id);
		return nodeView.getBranch();
	}*/
	
	public void setTitle(String title){
	    titleVar.set( title );
	}
	
	public String getTitle(){
	    return titleVar.get();
	}
	
	/* Default controls */
	public void setDefaultNodeColor(Color c){
		defaultNodeColorVar.set( c ) ;
	}
			
	public Color getDefaultNodeColor(){
		return defaultNodeColorVar.get();
	}
			
	public void setDefaultNodeFillColor(Color c){
		defaultNodeFillColorVar.set(c);
	}
			
	public Color getDefaultNodeFillColor(){
		return defaultNodeFillColorVar.get();
	}
	
	public void setDefaultNodeSize(int w, int h){
		defaultNodeWidthVar.set((double)w);
		defaultNodeHeightVar.set((double)h);
	}
	
	public double getDefaultNodeWidth(){
		return defaultNodeWidthVar.get();
	}

	public double getDefaultNodeHeight(){
		return defaultNodeHeightVar.get();
	}
				
	public void setDefaultNodeStroke(Stroke s){
		defaultNodeStrokeVar.set( s );
	}
			
	public Stroke getDefaultNodeStroke(){
		return defaultNodeStrokeVar.get() ;
	}
			
	public void setDefaultNodeShape(RectangularShape s){
		defaultNodeShapeVar.set(s);
	}
	
	public RectangularShape getDefaultNodeShape(){
		RectangularShape newNodeShape = (RectangularShape)defaultNodeShapeVar.get().clone();
		newNodeShape.setFrame(0, 0, defaultNodeWidthVar.get(), defaultNodeHeightVar.get());
		return newNodeShape;
	}
	
	public void setDefaultEdgeColor(Color c){
		defaultEdgeColorVar.set( c );
	}
			
	public Color getDefaultEdgeColor(){
		return defaultEdgeColorVar.get() ;
	}
			
	public void setDefaultEdgeStroke(Stroke s){
		defaultEdgeStrokeVar.set( s ) ;
	}
			
	public Stroke getDefaultEdgeStroke(){
		return defaultEdgeStrokeVar.get();
	}
	
	public void setDefaultTargetDecorator(PointDecorator<NP,EP,HG,WG,SG,N,E> pd){
/*		if (pd != null){
			pd.setColor(defaultTargetDecoratorColorVar.get());
			pd.setStroke(defaultTargetDecoratorStrokeVar.get());
		}*/
		defaultTargetDecoratorVar.set(pd);
	}
	
	public PointDecorator<NP,EP,HG,WG,SG,N,E> getDefaultTargetDecorator(){
		return defaultTargetDecoratorVar.get();
	}
	
	public void setDefaultTargetDecoratorColor(Color c){
		defaultTargetDecoratorColorVar.set(c);
	}
	
	public Color getDefaultTargetDecoratorColor(){
		return defaultTargetDecoratorColorVar.get();
	}
	
	public void setDefaultTargetDecoratorStroke(Stroke s){
		defaultTargetDecoratorStrokeVar.set(s);
	}
	
	public Stroke getDefaultTargetDecoratorStroke(){
		return defaultTargetDecoratorStrokeVar.get();
	}
	
	public void setDefaultSourceDecorator(PointDecorator<NP,EP,HG,WG,SG,N,E> pd){
	/*	if (pd != null){
			pd.setColor(defaultSourceDecoratorColorVar.get());
			pd.setStroke(defaultSourceDecoratorStrokeVar.get());
		}*/
		defaultSourceDecoratorVar.set(pd);
	}
	
	public PointDecorator<NP,EP,HG,WG,SG,N,E> getDefaultSourceDecorator(){
		return defaultSourceDecoratorVar.get();
	}
	
	public void setDefaultSourceDecoratorColor(Color c){
		defaultSourceDecoratorColorVar.set(c);
	}
	
	public Color getDefaultSourceDecoratorColor(){
		return defaultSourceDecoratorColorVar.get();
	}
	
	public void setDefaultSourceDecoratorStroke(Stroke s){
		defaultSourceDecoratorStrokeVar.set(s);
	}
	
	public Stroke getDefaultSourceDecoratorStroke(){
		return defaultSourceDecoratorStrokeVar.get();
	}
	
	public void setDefaultBranchColor(Color c){
		defaultBranchColorVar.set( c ) ;
	}
			
	public Color getDefaultBranchColor(){
		return defaultBranchColorVar.get() ;
	}
	
	public Stroke getDefaultBranchStroke(){
		return defaultBranchStrokeVar.get() ;
	}
            
    public void setDefaultBranchStroke(Stroke s){
    	defaultBranchStrokeVar.set( s ) ;
    }
	
	public void setDefaultParentDecorator(PointDecorator<NP,EP,HG,WG,SG,N,E> pd){
	/*	if (pd != null){
			pd.setColor(defaultParentDecoratorColorVar.get());
			pd.setStroke(defaultParentDecoratorStrokeVar.get());
		}*/
		defaultParentDecoratorVar.set(pd);
	}
	
	public PointDecorator<NP,EP,HG,WG,SG,N,E> getDefaultParentDecorator(){
		return defaultParentDecoratorVar.get();
	}
	
	public void setDefaultParentDecoratorColor(Color c){
		defaultParentDecoratorColorVar.set(c);
	}
	
	public Color getDefaultParentDecoratorColor(){
		return defaultParentDecoratorColorVar.get();
	}
	
	public void setDefaultParentDecoratorStroke(Stroke s){
		defaultParentDecoratorStrokeVar.set(s);
	}
	
	public Stroke getDefaultParentDecoratorStroke(){
		return defaultParentDecoratorStrokeVar.get();
	}
	
	public void setDefaultChildDecorator(PointDecorator<NP,EP,HG,WG,SG,N,E> pd){
	/*	if (pd != null){
			pd.setColor(defaultChildDecoratorColorVar.get());
			pd.setStroke(defaultChildDecoratorStrokeVar.get());
		}*/
		defaultChildDecoratorVar.set(pd);
	}
	
	public PointDecorator<NP,EP,HG,WG,SG,N,E> getDefaultChildDecorator(){
		return defaultChildDecoratorVar.get();
	}
	
	public void setDefaultChildDecoratorColor(Color c){
		defaultChildDecoratorColorVar.set(c);
	}
	
	public Color getDefaultChildDecoratorColor(){
		return defaultChildDecoratorColorVar.get();
	}
	
	public void setDefaultChildDecoratorStroke(Stroke s){
		defaultChildDecoratorStrokeVar.set(s);
	}
	
	public Stroke getDefaultChildDecoratorStroke(){
		return defaultChildDecoratorStrokeVar.get();
	}
	
	public void setDefaultLabelColor(Color c){
		defaultLabelColorVar.set( c ) ;
	}
			
	public Color getDefaultLabelColor(){
		return defaultLabelColorVar.get() ;
	}
	
	public void setDefaultLabelFillColor(Color c){
		defaultLabelFillColorVar.set( c ) ;
	}
			
	public Color getDefaultLabelFillColor(){
		return defaultLabelFillColorVar.get();
	}
	
	public boolean getCountLabels(){
		return countLabelsVar.get();
	}
	
	public void setCountLabels(boolean count){
		countLabelsVar.set(count);
	}
	
	public void setDefaultZoneColor(Color c){
		defaultZoneColorVar.set( c ) ;
	}
			
	public Color getDefaultZoneColor(){
		return defaultZoneColorVar.get();
	}
			
	public void setDefaultZoneFillColor(Color c){
		defaultZoneFillColorVar.set(c);
	}
			
	public Color getDefaultZoneFillColor(){
		return defaultZoneFillColorVar.get();
	}
			
	public void setDefaultZoneStroke(Stroke s){
		defaultZoneStrokeVar.set( s );
	}
			
	public Stroke getDefaultZoneStroke(){
		return defaultZoneStrokeVar.get() ;
	}	
	
	public void setDefaultZoneShape(RectangularShape s){
		defaultZoneShapeVar.set(s);
	}
	
	public RectangularShape getDefaultZoneShape(){
		return defaultZoneShapeVar.get() ;
	}
	
	public boolean getCountZones(){
		return countZonesVar.get();
	}
	
	public void setCountZones(boolean count){
		countZonesVar.set(count);
	}
	

	
	public void createString(String id){
		Assert.scriptingError(getDisplayString(id)== null, "String " + id + " already exists");
		myStringVector.add(new DisplayString(id));
	}
	
	public void clearString(String id){
		DisplayString ds = getDisplayString(id);
		Assert.scriptingError(ds != null, "String " + id + " doesn't exist");
		ds.theString = new StringBuilder();
	}
	
	public void addToString(String id, String addendum){
		DisplayString ds = getDisplayString(id);
		Assert.scriptingError(ds != null, "String " + id + " doesn't exist");
		if (ds.theString == null) ds.theString = new StringBuilder();
		ds.theString.append(addendum);
	}
	
	public void placeString(String id, int x, int y){
		DisplayString ds = getDisplayString(id);
		Assert.scriptingError(ds != null, "String " + id + " doesn't exist");
		ds.x = x;
		ds.y = y;
	}
	
	public void setStringBaseColor(String id, int c){
		DisplayString ds = getDisplayString(id);
		Assert.scriptingError(ds != null, "String " + id + " doesn't exist");
		ds.baseColor = new Color(c);
	}
	
	public void removeString(String id){
		DisplayString ds = getDisplayString(id);
		Assert.scriptingError(ds != null, "String " + id + " doesn't exist");
		myStringVector.remove(ds);
	}
	
	public void markSubString(String id, String subStr, char marker, int index) {
		DisplayString ds = getDisplayString(id);
		Assert.scriptingError(ds != null, "String " + id + " doesn't exist");
		if (ds.theString != null) {
			int p = ds.theString.indexOf(subStr, index);
			ds.theString.insert(p+subStr.length(), ENDMARKER);
			ds.theString.insert(p, marker);
		}
	}
	
	public void removeStringMarking(String id) {
		DisplayString ds = getDisplayString(id);
		Assert.scriptingError(ds != null, "String " + id + " doesn't exist");
		if (ds.theString != null) {
			int l = ds.theString.length()-1;
			for (int i = l; i >= 0; i--) 
				if(ds.theString.charAt(i) >= ENDMARKER)
					ds.theString.deleteCharAt(i);
		}
		
	}

	private DisplayString getDisplayString(String id){
		Iterator<DisplayString> iter = myStringVector.iterator();
		while (iter.hasNext()){
			DisplayString s = iter.next();
			if (s.id.compareTo(id) == 0)
				return s;
		}
		return null;			
	}
	
	public void setLine(double x1, double y1, double x2, double y2){
		lineVector.add(new Line2D.Double(x1, y1, x2, y2));
	}
	
//	public int getId(){
//		return id;
//	}
	
	public Component getDisplay(){
		return myDisplayVar.get();
	}
	
	public HG getHigraph(){
		return myGraph;
	}
	
	public void repaintLocalComponent(ComponentView<NP,EP,HG,WG,SG,N,E> component){
		Rectangle r = component.getShape().getBounds();
		myDisplayVar.get().repaint(20, r.x, r.y, r.width, r.height);
		
	}
	
	public void drawArea(Graphics2D screen){
		//System.out.println("TheGraph is " + myGraph);
		// Draw the edges		
		for(E edge:myGraph.getEdges()){
			EdgeView<NP,EP,HG,WG,SG,N,E> edgeView = getEdgeView(edge);
//			System.out.println("Drawing edge " + edge + " in color " + edgeView.getColor());
			edgeView.draw(screen);
        }
//		draw the node - branch trees
		for(N node:myGraph.getTops()){
			//System.out.println("Drawing node " + node);
			getNodeView(node).draw(screen);			
		}
// draw the edge decorators
		for(E edge:myGraph.getEdges()){
			EdgeView<NP,EP,HG,WG,SG,N,E> edgeView = getEdgeView(edge);
			PointDecorator<NP,EP,HG,WG,SG,N,E> decorator = edgeView.getTargetDecorator();
			if (decorator != null)
				decorator.draw(screen);
			decorator = edgeView.getSourceDecorator();
			if (decorator != null)
				decorator.draw(screen);
        }
// draw branch decorators
		for(N node:myGraph.getNodes()){
			BranchView<NP,EP,HG,WG,SG,N,E> branchView = getNodeView(node).getBranch();
			if (branchView !=null) {
				PointDecorator<NP,EP,HG,WG,SG,N,E> decorator = branchView.getParentDecorator();
				if (decorator != null)
					decorator.draw(screen);
				decorator = branchView.getChildDecorator();
				if (decorator != null)
					decorator.draw(screen);
			}
        }
		
		drawLines(screen);
		drawStrings(screen);
	}
	
	public void drawLines(Graphics2D screen){	
		Color currentColor = screen.getColor();
		screen.setColor(Color.black);
		for (Line2D line: lineVector){
			screen.drawLine((int)line.getX1(), (int)line.getY1(), (int)line.getX2(), (int)line.getY2());
		}
		screen.setColor(currentColor);
	}
		
	public void drawStrings(Graphics2D screen){	
		Iterator<DisplayString> iter = myStringVector.iterator();
		while (iter.hasNext()) {
			DisplayString ds = iter.next();

			if (ds.theString != null && ds.theString.length() > 0){
				int advance = ds.x;
				int advances[];		// Character sizes
	
	
			// Must convert expression to array of characters as drawChars can only work with
			// char arrays
				char tempArray[] = ds.theString.toString().toCharArray();
	  
//				g.setFont(myFont);
				FontMetrics fm = screen.getFontMetrics();
				advances = fm.getWidths();	// Widths of every character
				int baseline = fm.getAscent() + ds.y;

				Color currentColor = screen.getColor();
				Color currColor = ds.baseColor;
				screen.setColor(currColor);
				Vector attrStack = new Vector() ;
				for( int i = 0, sz = ds.theString.length() ; i < sz ; ++ i ) {
					char c = tempArray[i];		// Next character
					switch (c) {
					    case MARKER_RED:
					        attrStack.addElement(currColor) ;
					        currColor = Color.red ;
					        screen.setColor(currColor) ;
						    break;
						case MARKER_BLUE:
					        attrStack.addElement(currColor) ;
					        currColor = Color.blue;
					        screen.setColor( currColor ) ;
						    break;
						case MARKER_BLACK:
					        attrStack.addElement(currColor) ;
					        currColor = Color.black;
					        screen.setColor( currColor ) ;
						    break;
						case MARKER_GREEN:
					        attrStack.addElement(currColor) ;
					        currColor = Color.green;
					        screen.setColor( currColor ) ;
						    break;
						case ENDMARKER:
							currColor = (Color) attrStack.elementAt( attrStack.size() - 1 ) ;
						    screen.setColor( currColor ) ;
						    attrStack.removeElementAt( attrStack.size() - 1 ) ;
						    break ;
						default:
//							System.out.println("drawing " + tempArray[i] + " at (" + advance + ", " + baseline + ")");						
						    screen.drawChars(tempArray,i,1,advance,baseline);
						    advance += advances[c];
					}  // Switch
				}  // For loop
				screen.setColor(currentColor);
			} // if there is a theString
		} // End displayString vector while loop
	}   // End drawStrings

			
	
	public void dispose(){
		//theGraph.deRegisterView(this);
	}

	
	public class TopIterator implements Iterator<NodeView<NP,EP,HG,WG,SG,N,E>> {
		Iterator<N> topIterator;
		
		public TopIterator(){
			topIterator =  myGraph.getTops().iterator();
		}
		
		public boolean hasNext(){
			return topIterator.hasNext();
		}
		
		public NodeView<NP,EP,HG,WG,SG,N,E> next(){
			return  getNodeView(topIterator.next());
		}
		
		public void remove(){
			
		}
		
	}
}
