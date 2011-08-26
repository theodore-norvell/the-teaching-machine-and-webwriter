package higraph.view.layout;

import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RectangularShape;
import java.util.Iterator;

import higraph.model.interfaces.Edge;
import higraph.model.interfaces.Higraph;
import higraph.model.interfaces.Node;
import higraph.model.interfaces.Payload;
import higraph.model.interfaces.Subgraph;
import higraph.model.interfaces.WholeGraph;
import higraph.view.BranchView;
import higraph.view.EdgeView;
import higraph.view.HigraphView;
import higraph.view.NodeView;

public class PlacedNodeLayoutManager
< NP extends Payload<NP>,
EP extends Payload<EP>,
HG extends Higraph<NP,EP,HG,WG,SG,N,E>,
WG extends WholeGraph<NP,EP,HG,WG,SG,N,E>,
SG extends Subgraph<NP,EP,HG,WG,SG,N,E>,
N extends Node<NP,EP,HG,WG,SG,N,E>, 
E extends Edge<NP,EP,HG,WG,SG,N,E>
>
extends AbstractLayoutManager<NP,EP,HG,WG,SG,N,E>{

	@Override
	public void layoutLocal(HigraphView<NP,EP,HG,WG,SG,N,E> hgView) {
//		System.out.println("EdgeOnlyLayoutManager");
		Iterator<NodeView<NP,EP,HG,WG,SG,N,E>> iterator = hgView.getTops();
		while(iterator.hasNext()){
			NodeView<NP,EP,HG,WG,SG,N,E> top = iterator.next();
			top.doLayout(); // recursive layout using top's layoutManager
			Point2D.Double p = top.getPlacement();
			if (p != null){
				top.translateNextHierarchy(p.x, p.y);
			}
			layoutBranches(top);
		}
		layoutEdges( hgView, hgView.getHigraph().getGovernedEdges() ) ;			
		
		iterator = hgView.getTops();
		while(iterator.hasNext())
			doDislocations(iterator.next());
	}
	
	@Override
	public void layoutLocal(NodeView<NP,EP,HG,WG,SG,N,E> nv) {
		final double OFFSET_X = 5;;
		final double OFFSET_Y = 5;
		
		int kids = nv.getNumChildren();	
		Rectangle2D r = new Rectangle2D.Double(0, 0, 30, 30);
		if (kids > 0) {
			for (int i = 0; i < nv.getNumChildren(); i++) {
				NodeView<NP,EP,HG,WG,SG,N,E> kid = nv.getChild(i);
				kid.doLayout();
				Point2D.Double p = kid.getPlacement();
				if (p != null){
					kid.translateNextHierarchy(p.x, p.y);
				}
				Rectangle2D kidExtent = kid.getNextExtent();
				Rectangle2D.union(r, kidExtent, r);
				kid.getBranch().setVisibility(false);

			}
			// expand myExtent past extent of last child
			r.add(new Point2D.Double(r.getMaxX() + OFFSET_X, r.getMaxY() + OFFSET_Y));
			nv.setNextShape(r);
			nv.setFillColor(null); // Huh?  TSN
			nv.translateNextHierarchy(0,0);
		}
		else
			nv.placeNext(0, 0);
		layoutEdges(nv);
	}
}
