package higraph.view;

import higraph.model.interfaces.Edge;
import higraph.model.interfaces.Higraph;
import higraph.model.interfaces.Node;
import higraph.model.interfaces.Payload;
import higraph.model.interfaces.Subgraph;
import higraph.model.interfaces.WholeGraph;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.Point2D;

import tm.backtrack.BTTimeManager;


public class CircleDecorator 
< NP extends Payload<NP>,
EP extends Payload<EP>,
HG extends Higraph<NP,EP,HG,WG,SG,N,E>,
WG extends WholeGraph<NP,EP,HG,WG,SG,N,E>,
SG extends Subgraph<NP,EP,HG,WG,SG,N,E>,
N extends Node<NP,EP,HG,WG,SG,N,E>, 
E extends Edge<NP,EP,HG,WG,SG,N,E>
>
extends PointDecorator<NP,EP,HG,WG,SG,N,E> {
	
	private static final int DIAMETER = 8;
	
	
	
	public CircleDecorator(HigraphView<NP,EP,HG,WG,SG,N,E> view, BTTimeManager tm){
		super(view, tm);
	}
	
	@Override
	/**
	 *  Make a decorator of the same subtype as pd
	 * @param pd a concrete decorator of some subtype of PointDecorator
	 * @param view the HigraphView in which the decorator is to be made
	 * @param tm the BTTimeManager
	 * @return
	 */
	public CircleDecorator<NP,EP,HG,WG,SG,N,E> makeDecorator(PointDecorator<NP,EP,HG,WG,SG,N,E> pd,
			HigraphView<NP,EP,HG,WG,SG,N,E> view, BTTimeManager tm){
		return view.getViewFactory().makeCircleDecorator(view);
	}

	
	@Override
	public double getNextCenterX() {
		return getNextX() + DIAMETER/2.0;
	}

	@Override
	public double getNextCenterY() {
		return getNextY();
	}

	@Override
	public double getNextHeight() {
		return DIAMETER;
	}

	@Override
	public double getNextWidth() {
		return DIAMETER;
	}


	@Override
	public void draw(Graphics2D screen) {
		ComponentView<NP,EP,HG,WG,SG,N,E> owner = getOwner();
		if(owner != null && owner.getVisibility()) {
			Point2D.Double pRef = new Point2D.Double(getNextX(), getNextY());
			Point2D.Double pCenter = new Point2D.Double(getNextX() + DIAMETER/2.0, getNextY());
			rotate(pCenter, pRef, (Double)thetaVar.get());
			Color currentColor = screen.getColor();
			Stroke currentStroke = screen.getStroke();
			screen.setColor(owner.getColor());
			screen.setStroke(owner.getStroke());
			screen.drawOval((int)(pCenter.x -DIAMETER/2.0 + 0.5),(int)( pCenter.y - DIAMETER/2.0 + 0.5), DIAMETER, DIAMETER);
			screen.setColor(screen.getBackground());
			screen.fillOval((int)(pCenter.x -DIAMETER/2.0 + 0.5),(int)( pCenter.y - DIAMETER/2.0 + 0.5), DIAMETER, DIAMETER);
			screen.setColor(currentColor);
			screen.setStroke(currentStroke);
		}
	}
}
