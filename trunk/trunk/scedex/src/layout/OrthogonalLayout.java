package layout;

import java.awt.Color;
import java.awt.geom.Rectangle2D;

import higraph.model.interfaces.Edge;
import higraph.model.interfaces.Node;
import higraph.model.interfaces.Payload;
import higraph.model.interfaces.Subgraph;
import higraph.model.interfaces.WholeGraph;
import higraph.view.NodeView;
import higraph.view.SubgraphView;
import higraph.view.layout.SgLayoutManager;
import higraph.view.layout.SimpleTreeLayoutManager;

public class OrthogonalLayout < NP extends Payload<NP>,
                                EP extends Payload<EP>,
                                WG extends WholeGraph<NP,EP,WG,SG,N,E>,
                                SG extends Subgraph<NP,EP,WG,SG,N,E>,
                                N extends Node<NP,EP,WG,SG,N,E>, 
                                E extends Edge<NP,EP,WG,SG,N,E> >
                                extends SgLayoutManager<NP,EP,WG,SG,N,E> {
 public OrthogonalLayout(SubgraphView<NP, EP, WG, SG, N, E> view,
			NodeView<NP, EP, WG, SG, N, E> nodeView) {
		super(view, nodeView);
		
	}
public void layoutLocal() {
   layoutNodes(myNodeView,0,0);
   
}

private double layoutNodes(NodeView<NP, EP, WG, SG, N, E> nv,
		double bx, double by) {
	
		double baseHeight = nv.getNextHeight();  // single node
		double myHeight = baseHeight;       // node + descendants
		double myWidth = nv.getNextWidth();
		int numofchild = nv.getNumChildren();
		
		
		nv.setNextShape(new Rectangle2D.Double(bx, by, myWidth, myHeight));  
		
		if (numofchild > 0){
			double localY = by ;
			double heightoffset = baseHeight/2.0;
			double localX = bx;
			localY = localY + baseHeight;
			for (int i = 0; i < numofchild; i++){
				SgLayoutManager<NP,EP,WG,SG,N,E> sg;
				NodeView<NP,EP,WG,SG,N,E> child = nv.getChild(i);
				sg = new plainstateLayout<NP, EP, WG, SG, N, E>(myView, child);
				child.setLayoutManager(sg);
				sg.layoutLocal();
				sg.accumulateTranslation(localX,by);
				sg.applyTranslation();
				
				
				//System.out.println(sg.getExtent().getMaxX());
				//Rectangle2D.union(myExtent,sg.getExtent(), myExtent);
				
				child.setColor(Color.black);
				localY = localY + heightoffset;
				double chiildHeight = layoutNodes(child, localX, localY);
				localY +=chiildHeight;			
			}
			myHeight = localY - by - baseHeight;  
			nv.setNextShape(new Rectangle2D.Double(bx,by,myWidth, myHeight));
			
			
			
		}
		
		Rectangle2D.union(myExtent,
				new Rectangle2D.Double(bx, by, myWidth, myHeight), myExtent);
		return myHeight;
		
	
	
}
private void translateNodes(NodeView<NP,EP,WG,SG,N,E> n, double dx, double dy){
	for (int i = 0; i < n.getNumChildren(); i++ )
		translateNodes(n.getChild(i), dx, dy);
	n.translateNext(dx, dy);
			
}
public void applyTranslation() {
	translateNodes(myNodeView, dxLayout, dyLayout);
	super.applyTranslation();
}
}


