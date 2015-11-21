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


public class ArrowHead 
< NP extends Payload<NP>,
EP extends Payload<EP>,
HG extends Higraph<NP,EP,HG,WG,SG,N,E>,
WG extends WholeGraph<NP,EP,HG,WG,SG,N,E>,
SG extends Subgraph<NP,EP,HG,WG,SG,N,E>,
N extends Node<NP,EP,HG,WG,SG,N,E>, 
E extends Edge<NP,EP,HG,WG,SG,N,E>
>
extends PointDecorator<NP,EP,HG,WG,SG,N,E> {
	
	private static final int XMAX = 32;
	private static final int YMAX = 12;
	
	
	
	public ArrowHead(HigraphView<NP,EP,HG,WG,SG,N,E> view, BTTimeManager tm){
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
	public ArrowHead<NP,EP,HG,WG,SG,N,E> makeDecorator(PointDecorator<NP,EP,HG,WG,SG,N,E> pd,
			HigraphView<NP,EP,HG,WG,SG,N,E> view, BTTimeManager tm){
		return view.getViewFactory().makeArrowHead(view);
	}

	
	@Override
	public double getNextCenterX() {
		return getNextX() + XMAX/2.0;
	}

	@Override
	public double getNextCenterY() {
		return getNextY();
	}

	@Override
	public double getNextHeight() {
		return 2 * YMAX;
	}

	@Override
	public double getNextWidth() {
		return XMAX;
	}


	@Override
	public void draw(Graphics2D screen) {
		ComponentView<NP,EP,HG,WG,SG,N,E> owner = getOwner();
		if (owner != null && owner.getVisibility()) {
		    Point2D.Double pRef = new Point2D.Double(getX(), getY());
			Point2D.Double p0 = new Point2D.Double(pRef.x + 6.0, pRef.y + 3.0);
			Point2D.Double p1 = new Point2D.Double(pRef.x + 6.0, pRef.y - 3.0);
			double cosTheta = Math.cos((Double)thetaVar.get());
			double sinTheta = Math.sin((Double)thetaVar.get());
			rotate(p0, pRef, cosTheta, sinTheta);
			rotate(p1, pRef, cosTheta, sinTheta);
			Color currentColor = screen.getColor();

			Stroke currentStroke = screen.getStroke();
			screen.setColor(owner.getColor());
			screen.setStroke(owner.getStroke());

			screen.drawLine((int)(pRef.x+.5), (int)(pRef.y+.5), (int)(p0.x+.5), (int)(p0.y+.5));
			screen.drawLine((int)(pRef.x+.5), (int)(pRef.y+.5), (int)(p1.x+.5), (int)(p1.y+.5));
			screen.setColor(currentColor);
			screen.setStroke(currentStroke);
		}
	}
}
