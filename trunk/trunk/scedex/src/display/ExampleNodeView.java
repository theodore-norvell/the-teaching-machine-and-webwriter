package display;


import java.awt.Color;
import java.awt.Stroke;
import java.awt.geom.RectangularShape;

import sc.model.EdgePayloadSC;
import sc.model.EdgeSC;
import sc.model.NodePayloadSC;
import sc.model.NodeSC;
import sc.model.SubGraphSC;
import sc.model.WholeGraphSC;
import higraph.view.NodeView;
import higraph.view.SubgraphView;

public class ExampleNodeView
extends NodeView<NodePayloadSC, EdgePayloadSC, WholeGraphSC, SubGraphSC, NodeSC, EdgeSC>
{

    public ExampleNodeView(SubgraphView view, NodeSC node, Color color, Stroke stroke, RectangularShape theShape) {
        super(view, node, color, stroke, theShape);
    }

    

    

}
