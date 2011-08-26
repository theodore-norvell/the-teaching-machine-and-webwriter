package layout;

import java.awt.geom.Rectangle2D;
import java.util.Iterator;

import higraph.model.interfaces.Edge;
import higraph.model.interfaces.Node;
import higraph.model.interfaces.Payload;
import higraph.model.interfaces.Subgraph;
import higraph.model.interfaces.WholeGraph;
import higraph.view.NodeView;
import higraph.view.SubgraphView;
import higraph.view.layout.SgLayoutManager;
import higraph.view.layout.SimpleTreeLayoutManager;

public class scLayout
           < NP extends Payload<NP>,
             EP extends Payload<EP>,
             WG extends WholeGraph<NP,EP,WG,SG,N,E>,
             SG extends Subgraph<NP,EP,WG,SG,N,E>,
             N extends Node<NP,EP,WG,SG,N,E>, 
             E extends Edge<NP,EP,WG,SG,N,E> >
             extends SgLayoutManager<NP,EP,WG,SG,N,E> {
			 
	private static final double OFFSET = 30;

	public scLayout(SubgraphView<NP,EP,WG,SG,N,E> view) {
		   super(view);
	}

	@Override
	public void layoutLocal() {
		SgLayoutManager<NP,EP,WG,SG,N,E> sg;
		NodeView<NP,EP,WG,SG,N,E> nodeView;
		
		Iterator<NodeView<NP,EP,WG,SG,N,E>> iterator = myView.getTops();
		while(iterator.hasNext()){
			nodeView = iterator.next();
			sg = new OrthogonalLayout<NP,EP,WG,SG,N,E>(
						myView, nodeView);
			nodeView.setLayoutManager(sg);
			sg.layoutLocal();
		}
		iterator = myView.getTops();  
		double localX = 0;
		myExtent.setRect(0., 0., 0., 0.);
		while(iterator.hasNext()){

			nodeView = iterator.next();
			sg = nodeView.getLayoutManager();
			sg.accumulateTranslation(localX, 0);
			sg.applyTranslation();
			Rectangle2D.union(myExtent,sg.getExtent(), myExtent);
			localX = myExtent.getMaxX() + OFFSET;
		}
		
		
	}
	
	
	
	
	
	
	
	
	
}
