package display;


import sc.model.EdgePayloadSC;
import sc.model.EdgeSC;
import sc.model.NodePayloadSC;
import sc.model.NodeSC;
import sc.model.SubGraphSC;
import sc.model.WholeGraphSC;
import higraph.view.SubgraphView;

public class ExampleSubgraphView
extends SubgraphView<NodePayloadSC, EdgePayloadSC, WholeGraphSC, SubGraphSC, NodeSC, EdgeSC>
{

    public ExampleSubgraphView(SubGraphSC subgraph) {
        super(subgraph);
    }

    

    

}