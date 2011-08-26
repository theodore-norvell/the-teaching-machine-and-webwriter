package display;

import sc.model.*;
import higraph.view.layout.SimpleForestLayoutManager;
import higraph.view.layout.SimpleTreeLayoutManager;

public class ExampleLayoutManager
extends SimpleForestLayoutManager<NodePayloadSC, EdgePayloadSC, WholeGraphSC, SubGraphSC, NodeSC, EdgeSC>
{

    public ExampleLayoutManager(
            ExampleSubgraphView subgraphview) {
        super(subgraphview);
    }
}