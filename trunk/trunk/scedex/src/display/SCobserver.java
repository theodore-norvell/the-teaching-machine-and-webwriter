package display;

import java.awt.Color;
import java.awt.event.MouseEvent;

import sc.model.EdgePayloadSC;
import sc.model.EdgeSC;
import sc.model.NodePayloadSC;
import sc.model.NodeSC;
import sc.model.SubGraphSC;
import sc.model.WholeGraphSC;
import higraph.view.ComponentView;
import higraph.view.ZoneView;
import higraph.view.interfaces.SubgraphEventObserver;

public class SCobserver  <NodePayloadSC, EdgePayloadSC, 
                          WholeGraphSC, SubGraphSC,
                          NodeSC, EdgeSC> 
                          implements SubgraphEventObserver {

	@Override
	public void clickedOn(ComponentView component, MouseEvent e) {
		
	}

	@Override
	public void dragged(ComponentView component, MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void movedOver(ComponentView component, MouseEvent e) {
		
		component.setColor(Color.red);
		
	}

}
