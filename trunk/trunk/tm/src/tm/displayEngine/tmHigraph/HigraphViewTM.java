package tm.displayEngine.tmHigraph;

import java.awt.Color;
import java.awt.Component;
import java.awt.geom.Point2D;

import javax.swing.text.ComponentView;

import tm.backtrack.BTTimeManager;
import tm.backtrack.BTVar;
import higraph.view.HigraphView;

public class HigraphViewTM extends HigraphView<NodePayloadTM,
EdgePayloadTM,
HigraphTM,
WholeGraphTM,
SubgraphTM,
NodeTM,
EdgeTM >{

	private static final boolean DEFAULT_SHOW_NODE_NAME = false;
	private static final int DEFAULT_NODE_NAME_POSITION = ComponentView.EAST;
	private static final Color DEFAULT_NODE_NAME_COLOR = Color.BLACK;
	private static final boolean DEFAULT_SHOW_NODE_VALUE = false;
	private static final int DEFAULT_NODE_VALUE_POSITION = ComponentView.CENTER;
	private static final Point2D DEFAULT_NODE_LABEL_NUDGE = new Point2D.Double(0, 0);;
	private static final Color DEFAULT_NODE_LABEL_COLOR = Color.BLACK;
	
	private final BTVar<Boolean> defaultNodeNameShowVar;
	private final BTVar<Integer> defaultNodeNamePositionVar;
	private final BTVar<Point2D> defaultNodeNameNudgeVar;
	private final BTVar<Color> defaultNodeNameColorVar;
	private final BTVar<Boolean> defaultNodeValueShowVar;
	private final BTVar<Integer> defaultNodeValuePositionVar;
	private final BTVar<Point2D> defaultNodeValueNudgeVar;
	private final BTVar<Color> defaultNodeValueColorVar;

	public HigraphViewTM(ViewFactoryTM viewFactory, HigraphTM theGraph, Component display, BTTimeManager timeMan){
		super(viewFactory, theGraph, display, timeMan);
		defaultNodeNameShowVar = new BTVar<Boolean>(timeMan, DEFAULT_SHOW_NODE_NAME);
		defaultNodeNamePositionVar = new BTVar<Integer>(timeMan, DEFAULT_NODE_NAME_POSITION);
		defaultNodeNameNudgeVar = new BTVar<Point2D>(timeMan, DEFAULT_NODE_LABEL_NUDGE);
		defaultNodeNameColorVar = new BTVar<Color>(timeMan, DEFAULT_NODE_LABEL_COLOR);
		defaultNodeValueShowVar = new BTVar<Boolean>(timeMan, DEFAULT_SHOW_NODE_VALUE);
		defaultNodeValuePositionVar = new BTVar<Integer>(timeMan, DEFAULT_NODE_VALUE_POSITION);
		defaultNodeValueNudgeVar = new BTVar<Point2D>(timeMan, DEFAULT_NODE_LABEL_NUDGE);
		defaultNodeValueColorVar = new BTVar<Color>(timeMan, DEFAULT_NODE_LABEL_COLOR);
	}
	@Override
	public void startTransition(){
		for(NodeTM node : getHigraph().getNodes()){
			LabelTM label = (LabelTM)getNodeView(node).findLabel("value");
			if (label != null)
				label.setTheLabel(node.getPayload().getValue());
		}
		super.startTransition();
	}
	
	public void setDefaultNodeNameShow(boolean show, int position){
		defaultNodeNameShowVar.set(show);
		defaultNodeNamePositionVar.set(position);
	}
			
	public boolean getDefaultNodeNameShow(){
		return defaultNodeNameShowVar.get();
	}
	
	public int getDefaultNodeNamePosition(){
		return defaultNodeNamePositionVar.get();
	}
	
	public void setDefaultNodeNameNudge(int dx, int dy){
		defaultNodeNameNudgeVar.set(new Point2D.Double(dx, dy));
	}
	
	public Point2D getDefaultNodeNameNudge(){
		return defaultNodeNameNudgeVar.get();
	}
	
	public void setDefaultNodeNameColor(Color c){
		defaultNodeNameColorVar.set(c);
	}
	
	public Color getDefaultNodeNameColor(){
		return defaultNodeNameColorVar.get();
	}
	
	public void setDefaultNodeValueShow(boolean show, int position){
		defaultNodeValueShowVar.set(show);
		defaultNodeValuePositionVar.set(position);
	}
	
	public boolean getDefaultNodeValueShow(){
		return defaultNodeValueShowVar.get();
	}
	
	public int getDefaultNodeValuePosition(){
		return defaultNodeValuePositionVar.get();
	}

	public void setDefaultNodeValueNudge(int dx, int dy){
		defaultNodeValueNudgeVar.set(new Point2D.Double(dx, dy));
	}
	
	public Point2D getDefaultNodeValueNudge(){
		return defaultNodeValueNudgeVar.get();
	}
	
	public void setDefaultNodeValueColor(Color c){
		defaultNodeValueColorVar.set(c);
	}
	
	public Color getDefaultNodeValueColor(){
		return defaultNodeValueColorVar.get();
	}
}
