package layout;

import java.awt.Color;
import java.awt.geom.Rectangle2D;
import java.util.Iterator;

import higraph.model.interfaces.Edge;
import higraph.model.interfaces.Node;
import higraph.model.interfaces.Payload;
import higraph.model.interfaces.Subgraph;
import higraph.model.interfaces.WholeGraph;
import higraph.view.DropZone;
import higraph.view.NodeView;
import higraph.view.SubgraphView;
import higraph.view.ZoneView;
import higraph.view.layout.SgLayoutManager;

public class plainstateLayout < NP extends Payload<NP>,
                             EP extends Payload<EP>,
                             WG extends WholeGraph<NP,EP,WG,SG,N,E>,
                             SG extends Subgraph<NP,EP,WG,SG,N,E>,
                             N extends Node<NP,EP,WG,SG,N,E>, 
                             E extends Edge<NP,EP,WG,SG,N,E> >
                             extends SgLayoutManager<NP,EP,WG,SG,N,E> {

	public plainstateLayout(SubgraphView<NP, EP, WG, SG, N, E> view,
			NodeView<NP, EP, WG, SG, N, E> nodeView) {
		super(view, nodeView);
		
	}

	@Override
	public void layoutLocal() {
		layoutNodes(myNodeView,0.,0.); 
			
		}

	private double layoutNodes(NodeView<NP, EP, WG, SG, N, E> nv,
			double d, double e) {
		double baseWidth = nv.getNextWidth();  
		double myWidth = baseWidth;      
		double myHeight = nv.getNextHeight();
		int kids = nv.getNumChildren();
		
		nv.setNextShape(new Rectangle2D.Double(d, e, myWidth, myHeight));  // put node at top left
		
		if (kids > 0){
			double localX = d;
			double offset = baseWidth/2.0;
			localX = localX + baseWidth;
			for (int i = 0; i < kids; i++){
				NodeView<NP,EP,WG,SG,N,E> kid = nv.getChild(i);
				System.out.println("aaa");
				localX += offset;
				double kidWidth = layoutNodes(kid, localX, e);
				localX +=kidWidth;			
			}
			myWidth = localX - d - offset;  // width of the whole
			
			
			//nv.setNextShape(new Rectangle2D.Double(localX - myWidth,e,myWidth, myHeight));
			
		}
		Rectangle2D.union(myExtent,
				new Rectangle2D.Double(d, e, myWidth, myHeight), myExtent);
		return myWidth;
		
	}
	public void applyTranslation() {
		translateNodes(myNodeView, dxLayout, dyLayout);
		super.applyTranslation();
	}
	private void translateNodes(NodeView<NP,EP,WG,SG,N,E> n, double dx, double dy){
		for (int i = 0; i < n.getNumChildren(); i++ )
			translateNodes(n.getChild(i), dx, dy);
		n.translateNext(dx, dy);
				
	}
		
	}



