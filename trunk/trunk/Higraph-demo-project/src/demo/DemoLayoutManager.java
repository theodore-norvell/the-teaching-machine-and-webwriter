/*
 * Created on 2009-09-08 by Theodore S. Norvell. 
 */
package demo;

import java.util.List;

import demo.model.*;


import higraph.view.*;
import higraph.view.layout.SimpleForestLayoutManager;

public class DemoLayoutManager
extends SimpleForestLayoutManager<DemoPayload, DemoEdgeLabel, DemoHigraph, DemoWholeGraph, DemoSubgraph, DemoNode, DemoEdge>
{

    public DemoLayoutManager() {
        super();
    }
}
