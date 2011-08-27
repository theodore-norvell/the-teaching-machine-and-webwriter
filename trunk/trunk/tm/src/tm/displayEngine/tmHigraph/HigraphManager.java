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

package tm.displayEngine.tmHigraph;

import java.awt.Color;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RectangularShape;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.List;

import tm.backtrack.*;
import tm.clc.datum.AbstractPointerDatum;
import tm.configuration.Configuration;
import tm.displayEngine.DisplayInterface;
import tm.displayEngine.DisplayManager;
import higraph.model.interfaces.Higraph;
import higraph.model.interfaces.Subgraph;
import higraph.view.EdgeView;
import higraph.view.HigraphView;
import higraph.view.NodeView;
import higraph.view.PointDecorator;
import higraph.view.ZoneView;
import higraph.view.layout.*;
import tm.interfaces.Datum;
import tm.interfaces.DisplayContextInterface;
import tm.interfaces.Scriptable;
import tm.scripting.ScriptManager;
import tm.utilities.Assert;
import tm.utilities.Debug;
import tm.interfaces.Configurable;

/**
 * HigraphManager is a delegate of DisplayManager. Its responsibility is to manage Higraph
 * displays for the Teaching Machine. 
 * 
 * 
 * @author mpbl
 *
 */
public class HigraphManager implements Configurable, Scriptable, HigraphScriptingInterface {

    private final WholeGraphTM wholeGraph;
	
	// Valid Decorator types
	// TODO. Should these (and similar constants be declared in an interface?
	public final static int NONE = 0;
	public final static int ARROWHEAD = 1;
	public final static int CIRCLE = 2;
	
	/** Cross-reference between the ViewId and the actual view
	 */
	
	private class ViewRef{
		public String viewId;
		public HigraphViewTM view;
//		public boolean defaultShowNodeName = false;
		
		public ViewRef(String id, HigraphViewTM v){
			viewId = id;
			view = v;
		}			
	}
	
	private class HigraphRef{
		public String higraphId;
		public HigraphTM higraph;
		
		public HigraphRef(String id, HigraphTM hg){
			higraphId = id;
			higraph = hg;
		}			
	}

	private final List<ViewRef> viewMap;
	private final List<HigraphRef> higraphMap;
	private final List<HigraphViewTM> activeViewSet;
	
	/* This is a real problem. DisplayManager keeps a separate list of all displays and multiple calls to createAllDisplays
	 * can end up with the lists out of synch. The upshot is that refresh is being called on a different visualizer
	 * object than setView. A number of solutions present themselves
	 * 
	 * 1. Make sure that every call in DisplayManager is overridden in HigraphManager and keep the lists in synch
	 * 2. Change the availableDisplays list in HigraphManager from a list of Visualizers to a list of indices
	 * into DisplayManagers list of objects. Thus only one actual reference to a visualizer is maintained.
	 * 
	 * */
	//private List<HigraphVisualizer> availableDisplays;
//	final BTVar<Integer> currentViewVar; // The number of the view in the list to which commands are routed.
	
	private final BTTimeManager timeManager  ;
	private DisplayManager displayManager;
	
	
	public HigraphManager(DisplayContextInterface context) {
	    timeManager = context.getTimeManager() ;
		wholeGraph = new WholeGraphTM( timeManager );
		displayManager = (DisplayManager)context;
		
		activeViewSet = new ArrayList<HigraphViewTM>();
		viewMap = new ArrayList<ViewRef>();
		higraphMap = new ArrayList<HigraphRef>();
		higraphMap.add(new HigraphRef("wholeGraph", wholeGraph));
//		availableDisplays = new ArrayList<HigraphVisualizer>();
 //       currentViewVar = new BTVar<Integer>( timeManager, 0 ) ;
//		ConfigurationServer.getConfigurationServer().register(this, getId());
		ScriptManager.getManager().register(this);
	}
	
	/*************************** Scripting Functions ************************/
	
	

    /******************** Implementation of HigraphModelingInterface ****************/

	@Override
	public void makeNode(Datum d){
		makeNode(d, false);
	}
	
	@Override
	public void makeNode(Datum d, boolean deref){
		privateMakeNode(d, deref);
	}
	
	@Override
	public void deleteNode(Datum d){
		deleteNode(d, false);
	}
	
	
	@Override
	public void deleteNode(Datum d, boolean deref){
		NodeTM node = privateGetNode(d, deref);
//		System.out.println("Deleting node " + node);
		if (node != null) node.delete();
	}
	
	
	@Override
	public void addChild(Datum p, Datum c){
		addChild(p, false, c, false);
	}

	@Override
	public void addChild(Datum p, boolean derefP, Datum c){
		addChild(p, derefP, c, false);
	}
	
	@Override
	public void addChild(Datum p, Datum c, boolean derefC){
		addChild(p, false, c, derefC);
	}
	
	@Override
	public void addChild(Datum p, boolean derefP, Datum c, boolean derefC){
		NodeTM parent = privateGetNode(p, derefP);
		NodeTM child = privateGetNode(c, derefC);
		int end = parent.getNumberOfChildren();
		Assert.check(parent.canInsertChild(end, child),
				"parent node cannot accept a child");
		parent.insertChild(end, child);
	}
	
	@Override
	public void makeEdge(Datum source, Datum target){
		makeEdge(source, false, target, false);
	}
	
	@Override
	public void makeEdge(Datum source, boolean derefS, Datum target){
		makeEdge(source, derefS, target, false);
	}
	
	@Override
	public void makeEdge(Datum source, Datum target, boolean derefT){
		makeEdge(source, false, target, derefT);
	}
	
	@Override
	public void makeEdge(Datum source, boolean derefS, Datum target, boolean derefT){
		NodeTM sourceNode = privateGetNode(source, derefS);
		NodeTM targetNode = privateGetNode(target, derefT);
		
		/* We don't need to check since the TM higraph
		always allows edges to be added to nodes
		 */
		EdgeTM edge = wholeGraph.getEdge(sourceNode, targetNode);
		if (edge == null)
			edge = wholeGraph.makeEdge(sourceNode, targetNode, null);
	}
	

	@Override
	public void makeSubGraph(String id) {
		Assert.check(!id.equals("wholeGraph"), "wholeGraph cannot be used for a subGraph");
		Assert.check(getView(id) == null,"subGraph id " + id + " already assigned");
		higraphMap.add(new HigraphRef(id, wholeGraph.makeSubGraph())); 		
	}

	@Override
	public void addTopNode(String hgid, Datum t) {
		addTopNode(hgid, t, false);
	}
	
	@Override
	public void addTopNode(String hgid, Datum t, boolean deref) {
		Assert.check(!hgid.equals("wholeGraph"), "Can't addTopNode to wholeGraph");
		Higraph<NodePayloadTM, EdgePayloadTM, HigraphTM, WholeGraphTM, SubgraphTM, NodeTM, EdgeTM> hg = getHigraph(hgid);
		Assert.check(hg != null,"hiGraph id " + hgid + " not assigned");
		NodeTM n = privateMakeNode(t, deref);	
		((Subgraph<NodePayloadTM,EdgePayloadTM,HigraphTM,WholeGraphTM,SubgraphTM,NodeTM,EdgeTM>)(hg)).addTop(n);		
	}
	
	
	
    /******************** Implementation of Higraph Visualizer Interface ****************/

	@Override
	public void makeView(String viewId, String hgId, String visualizerPlugIn) {
		makeView(viewId, hgId, visualizerPlugIn, null);	
	}
	
/**
 * create a view of a hiGraph and tie it to a TM Visualizer
 * 
 * @param viewId - a unique id for the view
 * @param hgId - the id of a subGraph as set up by the makeSubGraph routine
 * @param visualizerPlugIn - the unique configId of the visualizer plugIn in which the view is to e displayed
 * @param layoutManager - the layout plugIn to be used (not implemented yet)
 */
	
	
	@Override
	public void makeView(String viewId, String hgId, String visualizerPlugIn, String layoutManager) {
		Debug.getInstance().msg(Debug.DISPLAY, "Trying to makeView " + viewId + " of higraph " + hgId
				+ " on visualizer " + visualizerPlugIn + " using layoutMangaer " + layoutManager);
		Assert.scriptingError(getView(viewId)==null, "view id " + viewId + " already exists");
		HigraphVisualizer display = getDisplay(visualizerPlugIn);
		Assert.scriptingError(display!=null, "no such plugIn as " + visualizerPlugIn);
		HigraphTM hg = getHigraph(hgId);
		
		Assert.scriptingError(hg != null, "higraph id " + hgId + " not assigned");
		// Create a new subgraph view for the plugin
	    ViewFactoryTM viewFactory = new ViewFactoryTM(timeManager);
		HigraphViewTM view = viewFactory.makeHigraphView(hg, display) ;
		
		viewMap.add(new ViewRef(viewId, view));
		setLayoutManager(layoutManager, view);

		// let the plugin know who its delegate is
		display.setView(view);
		activeViewSet.add(view);
	}
	
	@Override
	public void setTitle(String viewId, String title){
		HigraphView<NodePayloadTM,EdgePayloadTM,HigraphTM,WholeGraphTM,SubgraphTM,NodeTM,EdgeTM>
	    view = getView(viewId);
		Assert.scriptingError(view !=null, "view id " + viewId + " is not assigned.");
		view.setTitle(title);	
	}
	/**
	 * Set the default layoutManager for an entire view
	 * 
	 * @param layoutManager - the name of the layoutManager
	 * @param viewId - the name of the view
	 */


	@Override
	public void setLayoutManager(String layoutManager, String viewId) {
		Debug.getInstance().msg(Debug.DISPLAY, "setLayoutManager for "  + layoutManager);
		HigraphViewTM view = getView(viewId);
		Assert.check(view != null, "No such view as " + viewId);
		setLayoutManager(layoutManager, view);
	}
	
	   @Override
	    public void addToActiveViewSet(String viewId) {
	    	HigraphViewTM
	    		view = getView(viewId);
	    	Assert.check(view !=null, "Can't add unspecified view " + viewId + " to activeViewSet");
	    	for(HigraphViewTM v : activeViewSet)
	    		if (v == view) return; // already in activeViewSet
	    	activeViewSet.add(view);
	    }

	    @Override
	    public void removeFromActiveViewSet(String viewId) {
	    	HigraphViewTM
			view = getView(viewId);
	    	Assert.check(view !=null, "Can't remove unspecified view " + viewId + " from activeViewSet");
	    	activeViewSet.remove(view);
	    }
	    
	    public void clearActiveViewSet(){
	    	activeViewSet.clear();
	    }
		

	
	private void setLayoutManager(String layoutManager, HigraphView<NodePayloadTM,EdgePayloadTM,HigraphTM,WholeGraphTM,SubgraphTM,NodeTM,EdgeTM> view){
		AbstractLayoutManager<NodePayloadTM, EdgePayloadTM,
			HigraphTM, WholeGraphTM, SubgraphTM, NodeTM, EdgeTM>
				hglm = getLayoutManager(layoutManager);
		Assert.check(hglm != null, layoutManager + " does not exist.");
		view.setLayoutManager(hglm);
	}
	
	
	private AbstractLayoutManager<NodePayloadTM, EdgePayloadTM, HigraphTM, WholeGraphTM, SubgraphTM, NodeTM, EdgeTM>
	getLayoutManager(String layoutManager){	
		if (layoutManager.compareTo("SimpleTree")==0)
			return new SimpleTreeLayoutManager<NodePayloadTM, EdgePayloadTM,
					HigraphTM, WholeGraphTM, SubgraphTM, NodeTM, EdgeTM>();
		
		if (layoutManager.compareTo("NestedTree")==0)
			return new NestedTreeLayoutManager<NodePayloadTM, EdgePayloadTM,
					HigraphTM, WholeGraphTM, SubgraphTM, NodeTM, EdgeTM>();
		
        if (layoutManager.compareTo("HorizNestedTree")==0)
            return new NestedTreeLayoutManager<NodePayloadTM, EdgePayloadTM,
                    HigraphTM, WholeGraphTM, SubgraphTM, NodeTM, EdgeTM>(NestedTreeLayoutManager.Axis.X);
        
        if (layoutManager.compareTo("VertNestedTree")==0)
            return new NestedTreeLayoutManager<NodePayloadTM, EdgePayloadTM,
                    HigraphTM, WholeGraphTM, SubgraphTM, NodeTM, EdgeTM>(NestedTreeLayoutManager.Axis.Y);
        
		if (layoutManager.compareTo("PlacedNode")==0)
			return new PlacedNodeLayoutManager<NodePayloadTM, EdgePayloadTM,
					HigraphTM, WholeGraphTM, SubgraphTM, NodeTM, EdgeTM>();
		
		return null;
	}
	
    /**************** Implementation of HigraphViewCommandInterface **************/
	/* Generally, view commands are relayed to all views in the activeViewSet. */
	
	@Override
	public void placeNode(Datum n, long x, long y){
		placeNode(n, false, x, y);
	}
	
	@Override
	public void placeNode(Datum n, boolean deref, long x, long y){
		Assert.scriptingError(x >=0 && y >=0, "Cannot handle negative locations");
		NodeTM node = privateGetNode(n, deref);
		for(HigraphViewTM view : activeViewSet)
			view.getNodeView(node).setPlacement((int)x, (int) y);
	}
	
	
	/**  <p><b>A scripting function for manipulating higraph views</b></p>
	 * <p> dislocate, in all active views, the display of node n
	 * (plus all its children) by (dx,dy)</p>
	 * @param n the node to be dislocated in the view
	 * @param dx the amount of dislocation in the x direction
	 * @param dy the amount of dislocation in the y direction
	 */
	
	@Override
	public void dislocate(Datum n, long dx, long dy){
		dislocate(n, false, dx, dy);
	}
	
	@Override
	public void dislocate(Datum n, boolean deref, long dx, long dy){
		NodeTM node = privateGetNode(n, deref);
		for(HigraphViewTM view : activeViewSet){
			view.getNodeView(node).setDislocation((int)dx, (int)dy);				
		}	
	}
	

//Defaults are set in Higraph View to control properties of newly created nodes, edges, branches, etc.
	@Override
	public void setDefaultNodeColor(final long c){
		//for(HigraphViewTM view : activeViewSet)
		//	view.setDefaultNodeColor(privateGetColor(c));
	    forEachHGView( new HGViewAction() {
	        @Override void act(HigraphViewTM view) {
                view.setDefaultNodeColor(privateGetColor(c));
            }} ) ;
	}

    @Override
	public void setDefaultNodeFillColor(long c){
		for(HigraphViewTM view : activeViewSet)
			view.setDefaultNodeFillColor(privateGetColor(c));
	}
	
	public void setDefaultNodeNameColor(long c){
		for(HigraphViewTM view : activeViewSet)
			view.setDefaultNodeNameColor(privateGetColor(c));
	}
	
	public void setDefaultNodeValueColor(long c){
		for(HigraphViewTM view : activeViewSet)
			view.setDefaultNodeValueColor(privateGetColor(c));
	}
	
	public void setDefaultNodeSize(long w, long h){ // node size
		for(HigraphViewTM view : activeViewSet)
			view.setDefaultNodeSize((int)w, (int) h);
	}

	
	@Override
	public void setDefaultNodeStroke(long s) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setDefaultNodeShape(long s){
		for(HigraphViewTM view : activeViewSet)
			view.setDefaultNodeShape(getRectangularShape(s));
	}
	
	@Override
	public void setDefaultNodeNameShow(boolean show, long position){
		Assert.scriptingError(position >= ZoneView.POSITION_MIN && position <= ZoneView.POSITION_MAX, "Invalid enumerated postion");
		for(HigraphViewTM view : activeViewSet)
			view.setDefaultNodeNameShow(show, (int)position);
	}
	
	
	@Override
	public void setDefaultNodeNameNudge(long dx, long dy) {
		for(HigraphViewTM view : activeViewSet)
			view.setDefaultNodeNameNudge((int)dx, (int)dy);
	}

	@Override
	public void setDefaultNodeValueShow(boolean show, long position) {
		Assert.scriptingError(position >= ZoneView.POSITION_MIN && position <= ZoneView.POSITION_MAX, "Invalid enumerated postion");
		for(HigraphViewTM view : activeViewSet)
			view.setDefaultNodeValueShow(show, (int)position);
	}

	@Override
	public void setDefaultNodeValueNudge(long dx, long dy) {
		for(HigraphViewTM view : activeViewSet)
			view.setDefaultNodeValueNudge((int)dx, (int)dy);
	}
	
    @Override
	public void setDefaultEdgeColor(long c){
		for(HigraphViewTM view : activeViewSet)
			view.setDefaultEdgeColor(privateGetColor(c));		
	}
	
	
	@Override
	public void setDefaultEdgeStroke(long s) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void setDefaultSourceDecorator(long pd) {
		for(HigraphViewTM view : activeViewSet)
			view.setDefaultSourceDecorator(getDecorator(view, pd));		
	}

/*	@Override
	public void setDefaultSourceDecoratorColor(long c) {
		for(HigraphViewTM view : activeViewSet)
			view.setDefaultSourceDecoratorColor(privateGetColor(c));
		
	}

	@Override
	public void setDefaultSourceDecoratorStroke(long s) {
		// TODO Auto-generated method stub
		
	}*/

	@Override
	public void setDefaultTargetDecorator(long pd) {
		for(HigraphViewTM view : activeViewSet)
			view.setDefaultTargetDecorator(getDecorator(view, pd));				
	}

/*	@Override
	public void setDefaultTargetDecoratorColor(long c) {
		for(HigraphViewTM view : activeViewSet)
			view.setDefaultTargetDecoratorColor(privateGetColor(c));
	}

	@Override
	public void setDefaultTargetDecoratorStroke(long s) {
		// TODO Auto-generated method stub
		
	}*/

	public void setDefaultBranchColor(long c){
		for(HigraphViewTM view : activeViewSet)
			view.setDefaultBranchColor(privateGetColor(c));		
	}

	
	@Override
	public void setDefaultBranchStroke(long s) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setDefaultParentDecorator(long pd) {
		for(HigraphViewTM view : activeViewSet)
			view.setDefaultParentDecorator(getDecorator(view, pd));				
	}

/*	@Override
	public void setDefaultParentDecoratorColor(long c) {
		for(HigraphViewTM view : activeViewSet)
			view.setDefaultParentDecoratorColor(privateGetColor(c));
	}

	@Override
	public void setDefaultParentDecoratorStroke(long s) {
		// TODO Auto-generated method stub
		
	}*/

	@Override
	public void setDefaultChildDecorator(long pd) {
		for(HigraphViewTM view : activeViewSet)
			view.setDefaultChildDecorator(getDecorator(view, pd));				
	}

/*	@Override
	public void setDefaultChildDecoratorColor(long c) {
		for(HigraphViewTM view : activeViewSet)
			view.setDefaultChildDecoratorColor(privateGetColor(c));
	}

	@Override
	public void setDefaultChildDecoratorStroke(long s) {
		// TODO Auto-generated method stub
		
	}*/

	public void setDefaultZoneShape(long s){
		for(HigraphViewTM view : activeViewSet)
			view.setDefaultZoneShape(getRectangularShape(s));
	}
	

	
	@Override
	public void setDefaultZoneColor(long c) {
		for(HigraphViewTM view : activeViewSet)
			view.setDefaultZoneColor(privateGetColor(c));
	}

	@Override
	public void setDefaultZoneFillColor(long c) {
		for(HigraphViewTM view : activeViewSet)
			view.setDefaultZoneFillColor(privateGetColor(c));
	}

	@Override
	public void setDefaultZoneStroke(long s) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void setCountZones(boolean count){
		for(HigraphViewTM view : activeViewSet)
			view.setCountZones(count);		
	}
	
	@Override
	public void setDefaultLabelColor(long c) {
		for(HigraphViewTM view : activeViewSet)
			view.setDefaultLabelColor(privateGetColor(c));
	}
	
    @Override
    public void setDefaultLabelFillColor(long c) {
        for(HigraphViewTM view : activeViewSet)
            view.setDefaultLabelFillColor(privateGetColor(c));
    }
	
	@Override
	public void setCountLabels(boolean count){
		for(HigraphViewTM view : activeViewSet)
			view.setCountLabels(count);		
	}
	




/****************** Individual Component Settings *******************/
	
	/*** Node Settings ****/
	@Override
	public void setNodeColor(Datum d, final long c){
		setNodeColor(d, false, c);
	}
	
	@Override
	public void setNodeColor(Datum d, boolean deref, final long c){
		//for(HigraphViewTM view : activeViewSet){
		//	NodeTM node = privateGetNode(d);
		//	view.getNodeView(node).setColor(privateGetColor(c));				
		//}
	    forEachNodeView( d, deref,new NodeViewAction() {
	        @Override
            void act( NodeView<NodePayloadTM, EdgePayloadTM, HigraphTM, WholeGraphTM, SubgraphTM, NodeTM, EdgeTM> nodeView) {
                nodeView.setColor(privateGetColor(c)); } } ) ;
	}

	@Override
	public void setNodeFillColor(Datum d, long c){
		setNodeFillColor(d, false, c);
	}
	
	@Override
	public void setNodeFillColor(Datum d, boolean deref, long c){
		for(HigraphViewTM view : activeViewSet){
			NodeTM node = privateGetNode(d, deref);
			view.getNodeView(node).setFillColor(privateGetColor(c));				
		}
	}

	@Override
	public void setNodeShape(Datum d, long s) {
		setNodeShape(d, false, s);
	}
	
	@Override
	public void setNodeShape(Datum d, boolean deref, long s) {
		for(HigraphViewTM view : activeViewSet){
			NodeTM node = privateGetNode(d, deref);
			view.getNodeView(node).setNodeShapeType((int)s);
		}
	}
	

	
	@Override
	public void setNodeStroke(Datum d, long s){
		setNodeStroke(d, false, s);
	}
	
	@Override
	public void setNodeStroke(Datum d, boolean deref, long s){
		
	}
	
	@Override
	public void setNodeNameLabel(Datum d, final String str){
		setNodeNameLabel(d, false, str);
	}
	
	@Override
	public void setNodeNameLabel(Datum d, boolean deref, final String str){
		//for(HigraphViewTM view : activeViewSet){
		//	NodeTM node = privateGetNode(d);
		//	NodeView<NodePayloadTM, EdgePayloadTM, HigraphTM, WholeGraphTM, SubgraphTM, NodeTM, EdgeTM>
		//		nodeView = view.getNodeView(node);
		//	  LabelTM label = (LabelTM) nodeView.findLabel("name");
        //    Assert.scriptingError(label != null, "label " + "name" + " doesn't exist for this node");
        //    label.setTheLabel(str);
		//}
	    forEachNodeLabel( "name", d, deref, new LabelAction() {
            @Override void act(LabelTM label) {
                // TODO Auto-generated method stub
                label.setTheLabel(str);
            }} ) ;
	}

    @Override
	public void setNodeNameShow(Datum d, boolean show) {
    	setNodeNameShow(d, false, show);
    }
    
    @Override
	public void setNodeNameShow(Datum d,  boolean deref, boolean show) {
		for(HigraphViewTM view : activeViewSet){
			NodeView<NodePayloadTM, EdgePayloadTM, HigraphTM, WholeGraphTM, SubgraphTM, NodeTM, EdgeTM>
				nodeView = view.getNodeView(privateGetNode(d, deref));
			LabelTM label = (LabelTM) nodeView.findLabel("name");
            label.setShow(show);
		}
	}
    
    @Override
	public void setNodeNamePosition(Datum d, long position) {
    	setNodeNamePosition(d, false, position);
    }
    
   @Override
	public void setNodeNamePosition(Datum d, boolean deref, long position) {
		for(HigraphViewTM view : activeViewSet){
			NodeTM node = privateGetNode(d, deref);
			NodeView<NodePayloadTM, EdgePayloadTM, HigraphTM, WholeGraphTM, SubgraphTM, NodeTM, EdgeTM>
				nodeView = view.getNodeView(node);
			LabelTM label = (LabelTM) nodeView.findLabel("name");
            label.setPlacement((int)position);
		}
	}

	@Override
	public void setNodeNameNudge(Datum d, long dx, long dy) {
		setNodeNameNudge(d, false, dx, dy);
	}
	
	@Override
	public void setNodeNameNudge(Datum d, boolean deref, long dx, long dy) {
		for(HigraphViewTM view : activeViewSet){
			NodeTM node = privateGetNode(d, deref);
			NodeView<NodePayloadTM, EdgePayloadTM, HigraphTM, WholeGraphTM, SubgraphTM, NodeTM, EdgeTM>
				nodeView = view.getNodeView(node);
			LabelTM label = (LabelTM) nodeView.findLabel("name");
            Assert.scriptingError(label != null, "label " + "name" + " doesn't exist for this node");
            label.setNudge(dx, dy);
		}
	}

	@Override
	public void setNodeNameColor(Datum d, long c) {
		setNodeNameColor(d, false, c);
	}
	
	@Override
	public void setNodeNameColor(Datum d, boolean deref, long c) {
		for(HigraphViewTM view : activeViewSet){
			NodeTM node = privateGetNode(d, deref);
			NodeView<NodePayloadTM, EdgePayloadTM, HigraphTM, WholeGraphTM, SubgraphTM, NodeTM, EdgeTM>
				nodeView = view.getNodeView(node);
			LabelTM label = (LabelTM) nodeView.findLabel("name");
			label.setColor(new Color((int)c));
		}
	}
	
	@Override
    public void setNodeNameFillColor(Datum d, long c) {
		setNodeNameFillColor(d, false, c);
	}
	
	@Override
    public void setNodeNameFillColor(Datum d, boolean deref, long c) {
        for(HigraphViewTM view : activeViewSet){
            NodeTM node = privateGetNode(d, deref);
            NodeView<NodePayloadTM, EdgePayloadTM, HigraphTM, WholeGraphTM, SubgraphTM, NodeTM, EdgeTM>
                nodeView = view.getNodeView(node);
            LabelTM label = (LabelTM) nodeView.findLabel("name");
            label.setFillColor( privateGetColor(c) );
        }
    }  

    @Override
	public void setNodeValueShow(Datum d, boolean show) {
    	setNodeValueShow(d, false, show);
    }
    
    @Override
	public void setNodeValueShow(Datum d, boolean deref, boolean show) {
		for(HigraphViewTM view : activeViewSet){
			NodeView<NodePayloadTM, EdgePayloadTM, HigraphTM, WholeGraphTM, SubgraphTM, NodeTM, EdgeTM>
				nodeView = view.getNodeView(privateGetNode(d, deref));
			LabelTM label = (LabelTM) nodeView.findLabel("value");
            label.setShow(show);
		}
	}
    
	@Override
	public void setNodeValuePosition(Datum d, long position) {
		setNodeValuePosition(d, false, position);
	}
	
	@Override
	public void setNodeValuePosition(Datum d, boolean deref, long position) {
		for(HigraphViewTM view : activeViewSet){
			NodeTM node = privateGetNode(d, deref);
			NodeView<NodePayloadTM, EdgePayloadTM, HigraphTM, WholeGraphTM, SubgraphTM, NodeTM, EdgeTM>
				nodeView = view.getNodeView(node);
			LabelTM label = (LabelTM) nodeView.findLabel("value");
            label.setPlacement((int)position);
		}
	}

	@Override
	public void setNodeValueNudge(Datum d, long dx, long dy) {
		setNodeValueNudge(d, false, dx, dy);
	}
	
	@Override
	public void setNodeValueNudge(Datum d, boolean deref, long dx, long dy) {
		for(HigraphViewTM view : activeViewSet){
			NodeTM node = privateGetNode(d, deref);
			NodeView<NodePayloadTM, EdgePayloadTM, HigraphTM, WholeGraphTM, SubgraphTM, NodeTM, EdgeTM>
				nodeView = view.getNodeView(node);
			LabelTM label = (LabelTM) nodeView.findLabel("value");
            label.setNudge(dx, dy);
		}
	}
	
	@Override
	public void setNodeValueColor(Datum d, long c) {
		setNodeValueColor(d, false, c);
	}
	
	@Override
	public void setNodeValueColor(Datum d, boolean deref, long c) {
		for(HigraphViewTM view : activeViewSet){
			NodeTM node = privateGetNode(d, deref);
			NodeView<NodePayloadTM, EdgePayloadTM, HigraphTM, WholeGraphTM, SubgraphTM, NodeTM, EdgeTM>
				nodeView = view.getNodeView(node);
			LabelTM label = (LabelTM) nodeView.findLabel("value");
             label.setColor(new Color((int)c));
		}
	}
	   
     @Override
    public void setNodeValueFillColor(Datum d, long c) {
    	setNodeValueFillColor(d, false, c);
    }
    
    @Override
    public void setNodeValueFillColor(Datum d, boolean deref, long c) {
        for(HigraphViewTM view : activeViewSet){
            NodeTM node = privateGetNode(d, deref);
            NodeView<NodePayloadTM, EdgePayloadTM, HigraphTM, WholeGraphTM, SubgraphTM, NodeTM, EdgeTM>
                nodeView = view.getNodeView(node);
            LabelTM label = (LabelTM) nodeView.findLabel("value");
            label.setFillColor( privateGetColor(c) );
        }
    }  
	
	@Override
	public void createNodeExtraLabel(Datum d, String labelName, long position){
		createNodeExtraLabel(d, false, labelName, position);
	}
	
	@Override
	public void createNodeExtraLabel(Datum d, boolean deref, String labelName, long position){
		for(HigraphViewTM view : activeViewSet){
			NodeTM node = privateGetNode(d, deref);
			NodeView<NodePayloadTM, EdgePayloadTM, HigraphTM, WholeGraphTM, SubgraphTM, NodeTM, EdgeTM>
				nodeView = view.getNodeView(node);
			nodeView.addLabel(view.getViewFactory().makeLabel(nodeView, labelName, (int)position));
		}		
	}
	
	@Override
	public void setNodeExtraLabel(Datum d, String labelName, String theLabel){
		setNodeExtraLabel(d, false, labelName, theLabel);
	}
	
	@Override
	public void setNodeExtraLabel(Datum d, boolean deref, String labelName, String theLabel){
		for(HigraphViewTM view : activeViewSet){
			NodeTM node = privateGetNode(d, deref);
			NodeView<NodePayloadTM, EdgePayloadTM, HigraphTM, WholeGraphTM, SubgraphTM, NodeTM, EdgeTM>
				nodeView = view.getNodeView(node);
			LabelTM label = (LabelTM)nodeView.findLabel(labelName);
			Assert.scriptingError(label != null, "label " + labelName + " doesn't exist for this node");
			label.setTheLabel(theLabel);
		}
	}

	@Override
	public void setNodeExtraPosition(Datum d, String labelName, long position){
		setNodeExtraPosition(d, false, labelName, position);
	}
	
	@Override
	public void setNodeExtraPosition(Datum d, boolean deref, String labelName, long position){
		for(HigraphViewTM view : activeViewSet){
			NodeTM node = privateGetNode(d, deref);
			NodeView<NodePayloadTM, EdgePayloadTM, HigraphTM, WholeGraphTM, SubgraphTM, NodeTM, EdgeTM>
				nodeView = view.getNodeView(node);
			LabelTM label = (LabelTM)nodeView.findLabel(labelName);
			Assert.scriptingError(label != null, "label " + labelName + " doesn't exist for this node");
			label.setPlacement((int)position);
		}
	}

	@Override
	public void setNodeExtraNudge(Datum d, String labelName, long dx, long dy){
		setNodeExtraNudge(d, false, labelName, dx, dy);
	}
	
	@Override
	public void setNodeExtraNudge(Datum d, boolean deref, String labelName, long dx, long dy){
		for(HigraphViewTM view : activeViewSet){
			NodeTM node = privateGetNode(d, deref);
			NodeView<NodePayloadTM, EdgePayloadTM, HigraphTM, WholeGraphTM, SubgraphTM, NodeTM, EdgeTM>
				nodeView = view.getNodeView(node);
			LabelTM label = (LabelTM)nodeView.findLabel(labelName);
			Assert.scriptingError(label != null, "label " + labelName + " doesn't exist for this node");
			label.setNudge(dx, dy);
		}
	}

	
	@Override
	public void setNodeExtraColor(Datum d, String labelName, long c){
		setNodeExtraColor(d, false, labelName, c);
	}
	
	@Override
	public void setNodeExtraColor(Datum d, boolean deref, String labelName, long c){
		for(HigraphViewTM view : activeViewSet){
			NodeTM node = privateGetNode(d, deref);
			NodeView<NodePayloadTM, EdgePayloadTM, HigraphTM, WholeGraphTM, SubgraphTM, NodeTM, EdgeTM>
				nodeView = view.getNodeView(node);
			LabelTM label = (LabelTM)nodeView.findLabel(labelName);
			Assert.scriptingError(label != null, "label " + labelName + " doesn't exist for this node");
			label.setColor(privateGetColor(c));
		}
	}
	
	@Override
	public void setNodeLayoutManager(Datum d,String lm){
		setNodeLayoutManager(d, false, lm);
	}
	
	@Override
	public void setNodeLayoutManager(Datum d, boolean deref, String lm){
		for(HigraphViewTM view : activeViewSet)
			view.getNodeView(privateGetNode(d, deref)).setLayoutManager(getLayoutManager(lm));
	}

	@Override
    public void setNodeExtraFillColor(Datum d, String labelName, long c) {
		setNodeExtraFillColor(d, false, labelName, c);
	}
	
	@Override
    public void setNodeExtraFillColor(Datum d, boolean deref, String labelName, long c) {
        for(HigraphViewTM view : activeViewSet){
            NodeTM node = privateGetNode(d, deref);
            NodeView<NodePayloadTM, EdgePayloadTM, HigraphTM, WholeGraphTM, SubgraphTM, NodeTM, EdgeTM>
                nodeView = view.getNodeView(node);
            LabelTM label = (LabelTM) nodeView.findLabel(labelName);
            Assert.scriptingError(label != null, "label '" + labelName + "' doesn't exist for this node");
            label.setFillColor(privateGetColor(c));
        }
    }  

	
	/***** Edge Settings *****/
	@Override
	public void setEdgeColor(Datum source, Datum target, long c){
		setEdgeColor(source, false, target, false, c);
	}
	
	@Override
	public void setEdgeColor(Datum source, boolean derefS, Datum target, long c){
		setEdgeColor(source, derefS, target, false, c);
	}
	
	@Override
	public void setEdgeColor(Datum source, Datum target, boolean derefT, long c){
		setEdgeColor(source, false, target, derefT, c);
	}
	
	@Override
	public void setEdgeColor(Datum source, boolean derefS, Datum target, boolean derefT, long c){
		for(HigraphViewTM view : activeViewSet){
			EdgeTM edge = privateGetEdge(source, derefS, target, derefT);
			view.getEdgeView(edge).setColor(privateGetColor(c));
		}
	}
	
	@Override
	public void setEdgeFillColor(Datum source, Datum target, long c){
		setEdgeFillColor(source, false, target, false, c);
	}
	
	@Override
	public void setEdgeFillColor(Datum source, boolean derefS, Datum target, long c){
		setEdgeFillColor(source, derefS, target, false, c);
	}
	
	@Override
	public void setEdgeFillColor(Datum source, Datum target, boolean derefT, long c){
		setEdgeFillColor(source, false, target, derefT, c);
	}
	
	@Override
	public void setEdgeFillColor(Datum source, boolean derefS, Datum target, boolean derefT, long c){
		for(HigraphViewTM view : activeViewSet){
			EdgeTM edge = privateGetEdge(source, derefS, target, derefT);
			view.getEdgeView(edge).setFillColor(privateGetColor(c));
		}
	}
	
	@Override
	public void setEdgeShape(Datum source, Datum target, long s){
		setEdgeShape(source, false, target, false, s);
	}
	
	@Override
	public void setEdgeShape(Datum source, boolean derefS, Datum target, long s){
		setEdgeShape(source, derefS, target, false, s);
	}
	
	@Override
	public void setEdgeShape(Datum source, Datum target, boolean derefT, long s){
		setEdgeShape(source, false, target, derefT, s);
	}
	
	@Override
	public void setEdgeShape(Datum source, boolean derefS, Datum target, boolean derefT, long s){
		
	}
	
	@Override
	public void setEdgeStroke(Datum source, Datum target, long s){
		setEdgeStroke(source, false, target, false, s);
	}
	
	@Override
	public void setEdgeStroke(Datum source, boolean derefS, Datum target, long s){
		setEdgeStroke(source, derefS, target, false, s);
	}
	
	@Override
	public void setEdgeStroke(Datum source, Datum target, boolean derefT, long s){
		setEdgeStroke(source, false, target, derefT, s);
	}
	
	@Override
	public void setEdgeStroke(Datum Source, boolean derefS, Datum target, boolean derefT, long s){
		
	}
	
	@Override
    public void createEdgeLabel(Datum source, Datum target, String labelName, long position){
		createEdgeLabel(source, false, target, false, labelName, position);
	}
	
	@Override
    public void createEdgeLabel(Datum source, boolean derefS, Datum target, String labelName, long position){
		createEdgeLabel(source, derefS, target, false, labelName, position);		
	}
	
	@Override
    public void createEdgeLabel(Datum source, Datum target, boolean derefT, String labelName, long position){
		createEdgeLabel(source, false, target, derefT, labelName, position);
	}
	
	@Override
    public void createEdgeLabel(Datum source, boolean derefS, Datum target, boolean derefT, String labelName, long position){
        for(HigraphViewTM view : activeViewSet){
            EdgeTM edge = privateGetEdge(source, derefS, target, derefT);
            EdgeView<NodePayloadTM, EdgePayloadTM, HigraphTM, WholeGraphTM, SubgraphTM, NodeTM, EdgeTM>
                edgeView = view.getEdgeView(edge);
            edgeView.addLabel(view.getViewFactory().makeLabel(edgeView, labelName, (int)position));
        }       
    }

	@Override
	public void setEdgeLabel( Datum source, Datum target, String labelName, long c ) {
	    setEdgeLabel(source, false, target, false, labelName, Long.toString(c));
	}
	
	@Override
	public void setEdgeLabel( Datum source, Datum target, String labelName, double c ) {
	    setEdgeLabel(source, false, target, false, labelName, Double.toString(c));
	}
	
	@Override
	public void setEdgeLabel( Datum source, Datum target, String labelName, String value ) {
	    setEdgeLabel(source, false, target, false, labelName, value);
	}
	
	@Override
	public void setEdgeLabel( Datum source, boolean derefS, Datum target, String labelName, long c ) {
	    setEdgeLabel(source, false, target, false, labelName, Long.toString(c)) ;
	}
	
	@Override
	public void setEdgeLabel( Datum source, boolean derefS, Datum target, String labelName, double c ) {
	    setEdgeLabel(source, false, target, false, labelName, Double.toString(c)) ;
	}
	
	@Override
	public void setEdgeLabel( Datum source, boolean derefS, Datum target, String labelName, String value) {
	    setEdgeLabel(source, false, target, false, labelName, value) ;
	}
	

    @Override
    public void setEdgeLabel( Datum source, Datum target, boolean derefT, String labelName, long c ) {
        setEdgeLabel(source, false, target, derefT, labelName, Long.toString(c)) ;
    }
    
    @Override
    public void setEdgeLabel( Datum source, Datum target, boolean derefT, String labelName, double c ) {
        setEdgeLabel(source, false, target, derefT, labelName, Double.toString(c)) ;
    }
    
    @Override
    public void setEdgeLabel( Datum source, Datum target, boolean derefT, String labelName, String value) {
        setEdgeLabel(source, false, target, derefT, labelName, value) ;
    }
    
    @Override
    public void setEdgeLabel( Datum source, boolean derefS, Datum target, boolean derefT, String labelName, long c ) {
        setEdgeLabel(source, derefS, target, derefT, labelName, Long.toString(c)) ;
    }
    
    @Override
    public void setEdgeLabel( Datum source, boolean derefS, Datum target, boolean derefT, String labelName, double c ) {
        setEdgeLabel(source, derefS, target, derefT, labelName, Double.toString(c)) ;
    }
    
    @Override
    public void setEdgeLabel( Datum source,  boolean derefS, Datum target,  boolean derefT, String labelName, String value ) {
        for(HigraphViewTM view : activeViewSet){
            EdgeTM node = privateGetEdge(source, derefS, target, derefT);
            EdgeView<NodePayloadTM, EdgePayloadTM, HigraphTM, WholeGraphTM, SubgraphTM, NodeTM, EdgeTM>
                edgeView = view.getEdgeView(node);
            LabelTM label = (LabelTM)edgeView.findLabel(labelName);
            Assert.scriptingError(label != null, "label " + labelName + " doesn't exist for this edge");
            label.setTheLabel(value);
        }
    }
    
    @Override
    public void setEdgeLabelPosition(Datum source, Datum target, String labelName, long position) {
    	setEdgeLabelPosition(source, false, target, false, labelName, position);
    }
        
    @Override
    public void setEdgeLabelPosition(Datum source, boolean derefS, Datum target, String labelName, long position) {
    	setEdgeLabelPosition(source, derefS, target, false, labelName, position);

    }
        
    @Override
    public void setEdgeLabelPosition(Datum source, Datum target, boolean derefT, String labelName, long position) {
    	setEdgeLabelPosition(source, false, target, derefT, labelName, position);

    }
                
    @Override
    public void setEdgeLabelPosition(Datum source, boolean derefS, Datum target, boolean derefT, String labelName, long position) {
        
        for(HigraphViewTM view : activeViewSet){
            EdgeTM node = privateGetEdge(source, derefS, target, derefT);
            EdgeView<NodePayloadTM, EdgePayloadTM, HigraphTM, WholeGraphTM, SubgraphTM, NodeTM, EdgeTM>
                edgeView = view.getEdgeView(node);
            LabelTM label = (LabelTM)edgeView.findLabel(labelName);
            Assert.scriptingError(label != null, "label " + labelName + " doesn't exist for this edge");
            label.setPlacement( (int)position) ;
        }
    }
    
    
    @Override
    public void setEdgeLabelNudge( Datum source, Datum target, String labelName, long dx, long dy ) {
    	setEdgeLabelNudge(source, false, target, false, labelName, dx, dy);
    }
    
    @Override
    public void setEdgeLabelNudge( Datum source, boolean derefS, Datum target, String labelName, long dx, long dy ) {
    	setEdgeLabelNudge(source, derefS, target, false, labelName, dx, dy);
   }
    
    @Override
    public void setEdgeLabelNudge( Datum source, Datum target, boolean derefT, String labelName, long dx, long dy ) {
    	setEdgeLabelNudge(source, false, target, derefT, labelName, dx, dy);
    }
    
   @Override
    public void setEdgeLabelNudge( Datum source, boolean derefS, Datum target, boolean derefT, String labelName, long dx, long dy ) {
        for(HigraphViewTM view : activeViewSet){
            EdgeTM node = privateGetEdge(source, derefS, target, derefT);
            EdgeView<NodePayloadTM, EdgePayloadTM, HigraphTM, WholeGraphTM, SubgraphTM, NodeTM, EdgeTM>
                edgeView = view.getEdgeView(node);
            LabelTM label = (LabelTM)edgeView.findLabel(labelName);
            Assert.scriptingError(label != null, "label " + labelName + " doesn't exist for this edge");
            label.setNudge(dx, dy) ;
        }
    }
    
   @Override
   public void setEdgeLabelColor(Datum source, Datum target, String labelName, long c) {
	   setEdgeLabelColor(source, false, target, false, labelName, c);
   }
   
   @Override
   public void setEdgeLabelColor(Datum source, boolean derefS, Datum target, String labelName, long c) {
	   setEdgeLabelColor(source, derefS, target, false, labelName, c);
   }
   
   @Override
   public void setEdgeLabelColor(Datum source, Datum target, boolean derefT, String labelName, long c) {
	   setEdgeLabelColor(source, false, target, derefT, labelName, c);
   }
   
    @Override
    public void setEdgeLabelColor(Datum source, boolean derefS, Datum target, boolean derefT, String labelName, long c) {
        for(HigraphViewTM view : activeViewSet){
            EdgeTM node = privateGetEdge(source, derefS, target, derefT);
            EdgeView<NodePayloadTM, EdgePayloadTM, HigraphTM, WholeGraphTM, SubgraphTM, NodeTM, EdgeTM>
                edgeView = view.getEdgeView(node);
            LabelTM label = (LabelTM)edgeView.findLabel(labelName);
            Assert.scriptingError(label != null, "label " + labelName + " doesn't exist for this edge");
            label.setColor(privateGetColor(c)) ;
        }
    }
    
    @Override
    public void setEdgeLabelFill(Datum source, Datum target, String labelName, long c) {
    	setEdgeLabelFill(source, false, target, false, labelName, c);
    }
    
    @Override
    public void setEdgeLabelFill(Datum source, boolean derefS, Datum target, String labelName, long c) {
    	setEdgeLabelFill(source, derefS, target, false, labelName, c);
    }
    
    @Override
    public void setEdgeLabelFill(Datum source, Datum target, boolean derefT, String labelName, long c) {
    	setEdgeLabelFill(source, false, target, derefT, labelName, c);
    }
    
    @Override
    public void setEdgeLabelFill(Datum source, boolean derefS, Datum target, boolean derefT, String labelName, long c) {
        for(HigraphViewTM view : activeViewSet){
            EdgeTM node = privateGetEdge(source, derefS, target, derefT);
            EdgeView<NodePayloadTM, EdgePayloadTM, HigraphTM, WholeGraphTM, SubgraphTM, NodeTM, EdgeTM>
                edgeView = view.getEdgeView(node);
            LabelTM label = (LabelTM)edgeView.findLabel(labelName);
            Assert.scriptingError(label != null, "label " + labelName + " doesn't exist for this edge");
            label.setFillColor(privateGetColor(c)) ;
        }
    }
	
    @Override
	public void setTargetDecorator(Datum source, Datum target, long d){
    	setTargetDecorator(source, false, target, false, d);
    }
    
    @Override
	public void setTargetDecorator(Datum source, boolean derefS, Datum target, long d){
    	setTargetDecorator(source, derefS, target, false, d);
    }
    
    @Override
	public void setTargetDecorator(Datum source, Datum target, boolean derefT, long d){
    	setTargetDecorator(source, false, target, derefT, d);
    }
    
    @Override
	public void setTargetDecorator(Datum source, boolean derefS, Datum target, boolean derefT, long d){
		for(HigraphViewTM view : activeViewSet){
			EdgeTM edge = privateGetEdge(source, derefS, target, derefT);
			view.getEdgeView(edge).setTargetDecorator(getDecorator(view, d));
		}		
	}
	
    @Override
	public void setSourceDecorator(Datum source, Datum target, long d){
    	setSourceDecorator(source, false, target, false, d);
   }
    
    @Override
	public void setSourceDecorator(Datum source, boolean derefS, Datum target, long d){
    	setSourceDecorator(source, derefS, target, false, d);
    }
    
    @Override
	public void setSourceDecorator(Datum source, Datum target, boolean derefT, long d){
    	setSourceDecorator(source, false, target, derefT, d);
    }
    
    @Override
	public void setSourceDecorator(Datum source, boolean derefS, Datum target, boolean derefT, long d){
		for(HigraphViewTM view : activeViewSet){
			EdgeTM edge = privateGetEdge(source, derefS, target, derefT);
			view.getEdgeView(edge).setSourceDecorator(getDecorator(view, d));
		}		
	}
	


	/***** Branch Settings *****/	
    @Override
	public void setBranchColor(Datum d, long c){
    	setBranchColor(d, false, c);
	}

    @Override
	public void setBranchColor(Datum d, boolean deref, long c){
		
	}

    @Override
	public void setBranchLabelColor(Datum d, long c){
    	setBranchLabelColor(d, false, c);
	}

    @Override
	public void setBranchLabelColor(Datum d, boolean deref, long c){
		
	}

    @Override
	public void setBranchShape(Datum d, long s){
    	setBranchShape(d, false, s);
	}
	
    @Override
	public void setBranchShape(Datum d, boolean deref, long s){
		
	}
	
    @Override
	public void setBranchStroke(Datum d, long s){
    	setBranchStroke(d, false, s);
	}
	
    @Override
	public void setBranchStroke(Datum d, boolean deref, long s){
		
	}
	
    @Override
	public void setParentDecorator(Datum node, long d){
    	setParentDecorator(node, false, d);
    }
    
    @Override
	public void setParentDecorator(Datum node, boolean deref, long d){
		for(HigraphViewTM view : activeViewSet){
			NodeTM nodeTM = privateGetNode(node, deref);
			view.getNodeView(nodeTM).getBranch().setParentDecorator(getDecorator(view, d));
		}		
	}
	
    @Override
	public void setChildDecorator(Datum node, long d){
    	setChildDecorator(node, false, d);
    }
    
   @Override
	public void setChildDecorator(Datum node, boolean deref, long d){
		for(HigraphViewTM view : activeViewSet){
			NodeTM nodeTM = privateGetNode(node, deref);
			view.getNodeView(nodeTM).getBranch().setChildDecorator(getDecorator(view, d));
		}		
	}
	
	
	public void createString(String id) {
		for(HigraphViewTM view : activeViewSet)
			view.createString(id);		
	}
		
	public void clearString(String id) {
		for(HigraphViewTM view : activeViewSet)
			view.clearString(id);		
	}
		
	public void addToString(String id, String addendum) {
		for(HigraphViewTM view : activeViewSet)
			view.addToString(id, addendum);		
	}
		

	public void placeString(String id, long x, long y) {
		for(HigraphViewTM view : activeViewSet)
			view.placeString(id, (int)x, (int)y);		
	}
		
	public void setStringBaseColor(String id, long color) {
		for(HigraphViewTM view : activeViewSet)
			view.setStringBaseColor(id, (int)color);		
	}
		
	public void removeString(String id) {
		for(HigraphViewTM view : activeViewSet)
			view.removeString(id);		
	}

	
	public void markSubString(String id, String subStr, long marker, long index) {
		for(HigraphViewTM view : activeViewSet){
			view.markSubString(id, subStr, (char)marker, (int)index);
		}
	}
	
	public void removeStringMarking(String id) {
		for(HigraphViewTM view : activeViewSet)
			view.removeStringMarking(id);		
	}

	public void drawLine(double x1, double y1, double x2, double y2){
		for(HigraphViewTM view : activeViewSet){
			view.setLine(x1, y1, x2, y2);
		}		
	}


 	/***************** End of Scripting Functions **********************/
	
	private NodeTM privateMakeNode(Datum d, boolean deref){
		NodeTM node = wholeGraph.getNode(d, deref);
		if ( node == null){
			if (deref)
				if (d instanceof AbstractPointerDatum)
					d = ((AbstractPointerDatum)d).deref();
				else
					Assert.scriptingError("argument must be a reference or pointer");
			node = wholeGraph.makeRootNode(new NodePayloadTM(d));
		}
		return node;		
	}
	
	private NodeTM privateGetNode(Datum d, boolean deref){
		NodeTM node = wholeGraph.getNode(d, deref);
		Assert.scriptingError(node!=null, d.getName() + " has not been defined as a node.");
		return node;		
	}
	
	private EdgeTM privateGetEdge(Datum source, boolean derefS, Datum target, boolean derefT){
		NodeTM sourceNode = privateGetNode(source, derefS);
		NodeTM targetNode = privateGetNode(target, derefT);
		EdgeTM edge = wholeGraph.getEdge(sourceNode, targetNode);
		Assert.scriptingError(edge != null, "No edge defined between " + source.getName() + " and " + target.getName());
		return edge;		
	}
	
/** Mapping routines to map integer selectors passed across the scripting divide to Higraph objects */
	
	private PointDecorator<NodePayloadTM, EdgePayloadTM, HigraphTM, WholeGraphTM, SubgraphTM, NodeTM, EdgeTM> getDecorator(HigraphViewTM view, long d) {
		switch ((int) d){
		case ARROWHEAD:
			return view.getViewFactory().makeArrowHead(view);
		case CIRCLE:
			return view.getViewFactory().makeCircleDecorator(view);
		case NONE:
			return null;
		default:
			Assert.scriptingError("Decorator type " + d + " is unrecognized.");
			return null;
		}
	}
	
	private RectangularShape getRectangularShape(long d){
		switch ((int)d){
		case NodeView.ELLIPSE:
			return new Ellipse2D.Double(0, 0, HigraphView.DEFAULT_SHAPE_WIDTH, HigraphView.DEFAULT_SHAPE_HEIGHT);
		case NodeView.RECTANGLE:
			return new Rectangle2D.Double(0, 0, HigraphView.DEFAULT_SHAPE_WIDTH, HigraphView.DEFAULT_SHAPE_HEIGHT);
		case NodeView.ROUND_RECTANGLE:
			return new RoundRectangle2D.Double(0, 0, HigraphView.DEFAULT_SHAPE_WIDTH, HigraphView.DEFAULT_SHAPE_HEIGHT,
					HigraphView.DEFAULT_ROUND_X, HigraphView.DEFAULT_ROUND_Y);
			default: Assert.scriptingError("Only shapes defined are ELLIPSE, RECTANGLE and ROUND_RECTANGLE.");
		}
		return null;
	}





	
	/** A notification method to allow the DisplayManager
	 * to notify the HigraphManager that a new HigraphVisualizer
	 * display plugin has been added
	 * TODO Right now all displays are automatically tied to 
	 * the displayGraph subgraph 
	 * 
     * TODO.  Right now the "views" list is not backtrackable.  It is assumed that all subgraphs are added
     * during display creation. If this assumption goes out of date, we have to worry about uncreating displays and
     * undoing the effect of this routine.  TSN 2010 June 9
	 * 
	 * @param display
	 */
/*	public void higraphDisplayAdded(HigraphVisualizer display){
		String id = display.getId();
		boolean shouldAdd = true;
		for(HigraphVisualizer d : availableDisplays)
			if (d.getId().compareTo(id) == 0) {
				d = display;  // replace one display with another
				shouldAdd = false;
			}
		if (shouldAdd)
			availableDisplays.add(display);
//		System.out.println("subGraph Visualizer Display "+ display + " added");
	}*/
	
/*	public void subgraphDisplayRemoved(SubgraphVisualizer display){
		for (int i = 0; i < views.size(); i++){
			HigraphView<NodePayloadTM, EdgePayloadTM, WholeGraphTM, SubgraphTM, NodeTM, EdgeTM>
				view = views.get(i);
			if((SubgraphVisualizer)(view.getDisplay())==display){
				System.out.println("removing view " + view + " for display " + display);
				views.remove(i);
				theGraph.deRegisterView(view);
			}
		}
		
	}*/
	
	
	

	public void dispose() {
//    	ConfigurationServer.getConfigurationServer().deregister(this);
		ScriptManager.getManager().deRegister(this);
	}

	public void notifyOfLoad(Configuration config) {
		// TODO Auto-generated method stub
		
	}

	public void notifyOfSave(Configuration config) {
		// TODO Auto-generated method stub
		
	}

	public String getId() {
		return "HigraphManager";
	}
	
	private HigraphViewTM getView(String id){
		for(ViewRef vr : viewMap)
			if(id.compareTo(vr.viewId)==0) return vr.view;	
		return null;
	}
	
	private HigraphViewTM getAndCheckView(String id){
		HigraphViewTM view = getView(id);
		Assert.check(id !=null, "view " + id + " does not exist");
		return view;
	}

	
	private HigraphTM getHigraph(String id){
		for(HigraphRef hgr : higraphMap)
			if(id.equals(hgr.higraphId)) return hgr.higraph;	
		return null;
	}
	
	private Color privateGetColor(long c){
		return c < 0 ? null : new Color((int)c);
	}


	private HigraphVisualizer getDisplay(String id){
		DisplayInterface[] availableDisplays = displayManager.getDisplays()  ;
		for (DisplayInterface display : availableDisplays)
			if (display.getId().equals(id))
				return (HigraphVisualizer)display;
		return null;
	}

/*	HigraphView<NodePayloadTM,EdgePayloadTM,HigraphTM,WholeGraphTM,SubgraphTM,NodeTM,EdgeTM> currentView() {
	    return views.get( currentViewVar.get() ) ; }

	NodeView<NodePayloadTM,EdgePayloadTM,HigraphTM,WholeGraphTM,SubgraphTM,NodeTM,EdgeTM> currentView(NodeTM node) {
        return currentView().getNodeView(node) ; }
	
	EdgeView<NodePayloadTM,EdgePayloadTM,HigraphTM,WholeGraphTM,SubgraphTM,NodeTM,EdgeTM> currentView(EdgeTM edge) {
        return currentView().getEdgeView(edge) ; }*/
	
	
    
    private void forEachHGView(HGViewAction hgViewAction) {
        for(HigraphViewTM view : activeViewSet) {
            hgViewAction.act( view ) ; }
    }
    
    private abstract class HGViewAction {
        abstract void act(HigraphViewTM view) ;
    }
    
    private void forEachNodeView(Datum d, boolean deref, NodeViewAction hgViewAction) {
        for(HigraphViewTM view : activeViewSet) {
            NodeTM node = privateGetNode(d, deref);
            hgViewAction.act(view.getNodeView(node)); }
    }
    
    private abstract class NodeViewAction {
        abstract void act(NodeView<NodePayloadTM, EdgePayloadTM, HigraphTM, WholeGraphTM, SubgraphTM, NodeTM, EdgeTM> nodeView) ;
    }
    
    private void forEachNodeLabel(String labelName, Datum d, boolean deref, LabelAction labelAction) {
        for(HigraphViewTM view : activeViewSet) {
            NodeTM node = privateGetNode(d, deref);
            LabelTM label = (LabelTM) view.getNodeView(node).findLabel(labelName) ;
            Assert.scriptingError(label != null, "label '" + labelName + "' doesn't exist for this node");
            labelAction.act(label) ; }
    }
    
    private abstract class LabelAction {
        abstract void act(LabelTM label) ;
    }


}
