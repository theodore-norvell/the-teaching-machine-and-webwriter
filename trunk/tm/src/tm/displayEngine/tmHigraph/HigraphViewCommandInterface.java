package tm.displayEngine.tmHigraph;

import higraph.view.EdgeView;
import higraph.view.PointDecorator;

import java.awt.Color;
import java.awt.Stroke;
import java.awt.geom.RectangularShape;

import tm.interfaces.Datum;
import tm.utilities.Assert;

public interface HigraphViewCommandInterface {
	
/*#TSp*/
	/** Special functions for manipulating nodes in a view **/
	
	/**
	 * <p><b>Scripting Function</b></p>
	 * <p>
	 * dislocate the display of node n (plus all its children) by (dx,dy)
	 * </p>
	 * @param n the node to be dislocated in the view
	 * @param dx the amount of dislocation in the x direction
	 * @param dy the amount of dislocation in the y direction
	 */
	
	public void dislocate(Datum n, long dx, long dy);
	
	/**
	 * <p><b>Scripting Function</b></p>
	 * <p>
	 * place top left corner of node n at (x,y). All children are moved as well
	 * </p>
	 * @param n the node to be dislocated in the view
	 * @param x the position of the top left corner along the x axis
	 * @param y the position of the top left corner along the y axis
	 */
	public void placeNode(Datum n, long x, long y);
	/*#/TSp*/
	/*#TDefN*/	/**
	 * Set the defaults used for all nodes not individually specified. Affects only
	 * those views on the active view set
	 * @param long - longs are used across the scripting interface and have to be converted
	 */  
	// Note that colors conform to the standard Java hexadecimal color model.	
	public void setDefaultNodeColor(long c);      // Color of the shape outline
	public void setDefaultNodeFillColor(long c);  // Shape fill color
	public void setDefaultNodeNameColor(long c);  // Node name label color
	public void setDefaultNodeValueColor(long c); // node value label color
	
	// Node strokes are not yet defined and this routine is inoperative
	public void setDefaultNodeStroke(long s);
	
	// One of RECTANGLE, ROUNDED_RECTANGLE or ELLIPSE
	public void setDefaultNodeShape(long s);
	
	//Position is one of EAST,NORTHEAST,NORTH,NORTHWEST,WEST,SOUTHWEST,SOUTH or SOUTHEAST
	public void setDefaultNodeNameShow(boolean show, long position);  //name labels shown if true, at position
	public void setDefaultNodeNameNudge(long dx, long dy);            // nudge name labels from position by(dx,dy)
	public void setDefaultNodeValueShow(boolean show, long position); //value labels shown if true, at position
	public void setDefaultNodeValueNudge(long dx, long dy);           // nudge value labels from position by(dx,dy)
	/*#/TDefN*/
	
	/*#TDefE*/ /**
	 * Set the defaults used for the creation of all new edges. Affects only
	 * those views on the active view set
	 * @param long - longs are used across the scripting interface and have to be converted
	 */
	public void setDefaultEdgeColor(long c);
	public void setDefaultEdgeStroke(long s);
	
	/**
	 * Set the defaults used for the creation of all new edge decorators. Affects only
	 * those views on the active view set
	 * @param long - longs are used across the scripting interface and have to be converted
	 */
	// Decorators are one of ARROWHEAD, CIRCLE, NULL
	public void setDefaultTargetDecorator(long td);	
/*	public void setDefaultTargetDecoratorColor(long c);	
	public void setDefaultTargetDecoratorStroke(long s);*/
	public void setDefaultSourceDecorator(long sd);
/*	public void setDefaultSourceDecoratorColor(long c);	
	public void setDefaultSourceDecoratorStroke(long s);*/ /*#/TDefE*/
	
	/*#TDefB*//**
	 * Set the defaults used for the creation of all new branches. Affects only
	 * those views on the active view set
	 * @param long - longs are used across the scripting interface and have to be converted
	 */
	public void setDefaultBranchColor(long c);
    public void setDefaultBranchStroke(long s);
    
	/**
	 * Set the defaults used for the creation of all new branch decorators. Affects only
	 * those views on the active view set
	 * @param long - longs are used across the scripting interface and have to be converted
	 */
	public void setDefaultParentDecorator(long pd);
/*	public void setDefaultParentDecoratorColor(long c);	
	public void setDefaultParentDecoratorStroke(long s);*/
	public void setDefaultChildDecorator(long pd);
/*	public void setDefaultChildDecoratorColor(long c);
	public void setDefaultChildDecoratorStroke(long s);*/ /*#/TDefB*/
	
	/*#TDefZ*//**
	 * Set the defaults used for the creation of all new zones. Affects only
	 * those views on the active view set
	 * @param long - longs are used across the scripting interface and have to be converted
	 */
	public void setDefaultZoneColor(long c);
	public void setDefaultZoneFillColor(long c);	
	public void setDefaultZoneStroke(long s);
	public void setDefaultZoneShape(long s);
	public void setCountZones(boolean count);	
	
	/**
	 * Set the defaults used for the creation of all new labels. Affects only
	 * those views on the active view set
	 * @param long - longs are used across the scripting interface and have to be converted
	 */
	public void setDefaultLabelColor(long c);
	public void setDefaultLabelFillColor(long c);
    public void setCountLabels(boolean count);	/*#/TDefZ*/

    /*#TNode*/	
	/********* Start of individual component controls ***********/

	/** <p>Set a layoutManager for a node in the specified visualizer.
	 * Layout manager will apply to specified node ONLY in the specified visualizer
	 * and all its descendants in that visualizer that don't have their own
     * layout managers specified</p>
	 * 
	 * @param node the node associated with the datum   
	 *       <b>pre:</b> datum must exist and be associated with a node in the wholeGraph
	 *  @param layoutManager unique id for the TM layoutManagerPlugIn
	 *        <b>pre:</b> plugIn must exist and be available through TM plugIn manager
	 *  @param viewId the id of the subGraph view.
	 *        <b>pre:</b> view must exist.
	 *  @param chase (optional) if the node is a pointer datum dereference it
	 */
	public void setNodeLayoutManager(Datum node, String layoutManagerPlugIn);
	public void setNodeColor(Datum d, long c);
	public void setNodeFillColor(Datum d, long c);	
	public void setNodeShape(Datum d, long s);
	public void setNodeStroke(Datum d, long s);
	public void setNodeNameLabel(Datum d, String l);   // Over-ride the standard name label
	public void setNodeNameShow(Datum d, boolean show);
	public void setNodeNamePosition(Datum d, long position);
	public void setNodeNameNudge(Datum d, long dx, long dy);
	public void setNodeNameColor(Datum d, long c);	
    public void setNodeNameFillColor(Datum d, long c);  
	public void setNodeValueShow(Datum d, boolean show);
	public void setNodeValuePosition(Datum d, long position);
	public void setNodeValueNudge(Datum d, long dx, long dy);
	public void setNodeValueColor(Datum d, long c);	
    public void setNodeValueFillColor(Datum d, long c); 
    // Create an arbitrary label called labelName for the node defined by d, at position
	public void createNodeExtraLabel(Datum d, String labelName, long position);
	// set properties of arbitrary label labelName for node defined by d
	public void setNodeExtraPosition(Datum d, String labelName, long position);
	public void setNodeExtraNudge(Datum d, String labelName, long dx, long dy);
	public void setNodeExtraColor(Datum d, String labelName, long c);
	public void setNodeExtraLabel(Datum d, String labelName, String label);  // The actual label
    public void setNodeExtraFillColor(Datum d, String labelName, long c);/*#/TNode*/ 
		
    /*#TEdge*/// Edge controls
	public void setEdgeColor(Datum source, Datum target, long c);
	public void setEdgeFillColor(Datum source, Datum target, long c);
	public void setEdgeShape(Datum source, Datum target, long s);	
	public void setEdgeStroke(Datum source, Datum target, long s);
	
	public void createEdgeLabel(Datum source, Datum target, String labelName, long position) ;
	public void setEdgeLabel( Datum source, Datum target, String labelName, long c ) ;
	public void setEdgeLabel( Datum source, Datum target, String labelName, double c ) ;
    public void setEdgeLabel( Datum source, Datum target, String labelName, String c ) ;
    public void setEdgeLabelPosition(Datum source, Datum target, String labelName, long position) ;
    public void setEdgeLabelNudge( Datum source, Datum target, String labelName, long dx, long dy ) ;   
    public void setEdgeLabelColor(Datum source, Datum target, String labelName, long c) ;
    public void setEdgeLabelFill(Datum source, Datum target, String labelName, long c) ;
    
	public void setTargetDecorator(Datum source, Datum target, long d);
	public void setSourceDecorator(Datum source, Datum target, long d);/*#/TEdge*/

    /*#T Branch*/ // Branch controls
	public void setBranchColor(Datum d, long c);
	public void setBranchLabelColor(Datum d, long c);	
	public void setBranchShape(Datum d, long s);	
	public void setBranchStroke(Datum d, long s);
	public void setParentDecorator(Datum node, long d);
	public void setChildDecorator(Datum node, long d);/*#/T Branch*/

	
	/*#TDerefN*/ /*Deref versions of individual commands */
	public void placeNode(Datum n, boolean deref, long x, long y);
	public void dislocate(Datum n, boolean deref, long dx, long dy);

	public void setNodeColor(Datum d, boolean deref, long c);
	public void setNodeFillColor(Datum d, boolean deref, long c);	
	public void setNodeShape(Datum d, boolean deref, long s);
	public void setNodeStroke(Datum d, boolean deref, long s);
	public void setNodeNameLabel(Datum d, boolean deref, final String str);
	public void setNodeNameShow(Datum d,  boolean deref, boolean show);
	public void setNodeNamePosition(Datum d, boolean deref, long position);
	public void setNodeNameNudge(Datum d, boolean deref, long dx, long dy);
	public void setNodeNameColor(Datum d, boolean deref, long c);
    public void setNodeNameFillColor(Datum d, boolean deref, long c);
	public void setNodeValueShow(Datum d, boolean deref, boolean show);
	public void setNodeValuePosition(Datum d, boolean deref, long position);
	public void setNodeValueNudge(Datum d, boolean deref, long dx, long dy);
	public void setNodeValueColor(Datum d, boolean deref, long c);
	public void setNodeValueFillColor(Datum d, boolean deref, long c);
	public void createNodeExtraLabel(Datum d, boolean deref, String labelName, long position);
	public void setNodeExtraLabel(Datum d, boolean deref, String labelName, String theLabel);
	public void setNodeExtraPosition(Datum d, boolean deref, String labelName, long position);
	public void setNodeExtraNudge(Datum d, boolean deref, String labelName, long dx, long dy);
	public void setNodeExtraColor(Datum d, boolean deref, String labelName, long c);
    public void setNodeExtraFillColor(Datum d, boolean deref, String labelName, long c);
	public void setNodeLayoutManager(Datum d, boolean deref, String lm);/*#/TDerefN*/
	
	/*#TDerefE*/	
	public void setEdgeColor(Datum source, boolean derefS, Datum target, long c);
	public void setEdgeColor(Datum source, Datum target, boolean derefT, long c);
	public void setEdgeColor(Datum source, boolean derefS, Datum target, boolean derefT, long c);
	
	public void setEdgeFillColor(Datum source, boolean derefS, Datum target, long c);
	public void setEdgeFillColor(Datum source, Datum target, boolean derefT, long c);
	public void setEdgeFillColor(Datum source, boolean derefS, Datum target, boolean derefT, long c);
	
	public void setEdgeShape(Datum source, boolean derefS, Datum target, long s);
	public void setEdgeShape(Datum source, Datum target, boolean derefT, long s);
	public void setEdgeShape(Datum source, boolean derefS, Datum target, boolean derefT, long s);
	
	public void setEdgeStroke(Datum source, boolean derefS, Datum target, long s);
	public void setEdgeStroke(Datum source, Datum target, boolean derefT, long s);
	public void setEdgeStroke(Datum Source, boolean derefS, Datum target, boolean derefT, long s);
	
    public void createEdgeLabel(Datum source, boolean derefS, Datum target, String labelName, long position);
    public void createEdgeLabel(Datum source, Datum target, boolean derefT, String labelName, long position);
    public void createEdgeLabel(Datum source, boolean derefS, Datum target, boolean derefT, String labelName, long position);
    
	public void setEdgeLabel( Datum source, boolean derefS, Datum target, String labelName, long c );
	public void setEdgeLabel( Datum source, boolean derefS, Datum target, String labelName, double c );
	public void setEdgeLabel( Datum source, boolean derefS, Datum target, String labelName, String value);
    public void setEdgeLabel( Datum source, Datum target, boolean derefT, String labelName, long c );
    public void setEdgeLabel( Datum source, Datum target, boolean derefT, String labelName, double c );
    public void setEdgeLabel( Datum source, Datum target, boolean derefT, String labelName, String value );
    public void setEdgeLabel( Datum source,  boolean derefS, Datum target,  boolean derefT, String labelName, long c );
    public void setEdgeLabel( Datum source,  boolean derefS, Datum target,  boolean derefT, String labelName, double c );
    public void setEdgeLabel( Datum source,  boolean derefS, Datum target,  boolean derefT, String labelName, String value );
    
    public void setEdgeLabelPosition(Datum source, boolean derefS, Datum target, String labelName, long position);
    public void setEdgeLabelPosition(Datum source, Datum target, boolean derefT, String labelName, long position) ;
    public void setEdgeLabelPosition(Datum source, boolean derefS, Datum target, boolean derefT, String labelName, long position);
    
    public void setEdgeLabelNudge( Datum source, boolean derefS, Datum target, String labelName, long dx, long dy );
    public void setEdgeLabelNudge( Datum source, Datum target, boolean derefT, String labelName, long dx, long dy );
    public void setEdgeLabelNudge( Datum source, boolean derefS, Datum target, boolean derefT, String labelName, long dx, long dy );
    
    public void setEdgeLabelColor(Datum source, boolean derefS, Datum target, String labelName, long c);
    public void setEdgeLabelColor(Datum source, Datum target, boolean derefT, String labelName, long c);
    public void setEdgeLabelColor(Datum source, boolean derefS, Datum target, boolean derefT, String labelName, long c);
    
    public void setEdgeLabelFill(Datum source, boolean derefS, Datum target, String labelName, long c);
    public void setEdgeLabelFill(Datum source, Datum target, boolean derefT, String labelName, long c);
    public void setEdgeLabelFill(Datum source, boolean derefS, Datum target, boolean derefT, String labelName, long c);
    
	public void setTargetDecorator(Datum source, boolean derefS, Datum target, long d);
	public void setTargetDecorator(Datum source, Datum target, boolean derefT, long d);
	public void setTargetDecorator(Datum source, boolean derefS, Datum target, boolean derefT, long d);
	
	public void setSourceDecorator(Datum source, boolean derefS, Datum target, long d);
	public void setSourceDecorator(Datum source, Datum target, boolean derefT, long d);
	public void setSourceDecorator(Datum source, boolean derefS, Datum target, boolean derefT, long d);/*#/TDerefE*/

	/*#TDerefB*/
	public void setBranchColor(Datum d, boolean deref, long c);
	public void setBranchLabelColor(Datum d, boolean deref, long c);
	public void setBranchShape(Datum d, boolean deref, long s);
	public void setBranchStroke(Datum d, boolean deref, long s);
	public void setParentDecorator(Datum node, boolean deref, long d);
	public void setChildDecorator(Datum node, boolean deref, long d);/*#/TDerefB*/

	/*#TStr*/	
	/************ Start of hiGraph string commands ***********************/	
	public void createString(String id);
	public void clearString(String id);
	public void addToString(String id, String addendum);
	public void placeString(String id, long x, long y);
	public void setStringBaseColor(String id, long color);
	public void removeString(String id);
	public void markSubString(String id, String subStr, long marker, long index);
	public void removeStringMarking(String id);/*#/TStr*/
}
