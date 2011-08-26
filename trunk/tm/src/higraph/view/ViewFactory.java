//     Copyright 1998--2010 Michael Bruce-Lockhart and Theodore S. Norvell
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License. 
// You may obtain a copy of the License at 
//
//     http://www.apache.org/licenses/LICENSE-2.0 
//
// Unless required by applicable law or agreed to in writing, 
// software distributed under the License is distributed on an 
// "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
// either express or implied. See the License for the specific language 
// governing permissions and limitations under the License.

/*
 * Created on 2009-11-11 by Theodore S. Norvell. 
 */
package higraph.view;

import java.awt.Component;
import tm.backtrack.*;
import tm.displayEngine.tmHigraph.HigraphViewTM;
import tm.displayEngine.tmHigraph.NodeTM;
import tm.displayEngine.tmHigraph.NodeViewTM;

import higraph.model.interfaces.*;

/**
 * 
 * @author theo
 * 
 * @param NP The Node Payload type
 * @param EP The Edge Payload type
 * @param HG The Higraph type 
 * @param WG The Whole Graph type
 * @param SG The Subgraph type
 * @param N  The Node type
 * @param E  The Edge type

 */
public class ViewFactory
    < NP extends Payload<NP>,
      EP extends Payload<EP>,
      HG extends Higraph<NP,EP,HG,WG,SG,N,E>,
      WG extends WholeGraph<NP,EP,HG,WG,SG,N,E>,
      SG extends Subgraph<NP,EP,HG,WG,SG,N,E>,
      N extends Node<NP,EP,HG,WG,SG,N,E>, 
      E extends Edge<NP,EP,HG,WG,SG,N,E>
    >
{	
	protected final BTTimeManager timeMan ;
	
	public ViewFactory( BTTimeManager tm ) {
	    timeMan = tm ;
	}

    
    public HigraphView<NP,EP,HG,WG,SG,N,E> makeHigraphView(HG hg, Component display) {
        return new HigraphView<NP,EP,HG,WG,SG,N,E>(this, hg, display, timeMan) ;
    }
    
    public NodeView<NP,EP,HG,WG,SG,N,E> makeNodeView(HigraphView<NP,EP,HG,WG,SG,N,E> hgv, N node) {
    	return new NodeView<NP,EP,HG,WG,SG,N,E>(hgv, node, timeMan);
     }
   
    public EdgeView<NP,EP,HG,WG,SG,N,E> makeEdgeView(HigraphView<NP,EP,HG,WG,SG,N,E> hgv, E edge) {
    	return new EdgeView<NP,EP,HG,WG,SG,N,E>(hgv, edge, timeMan);
    }
    
    public BranchView<NP,EP,HG,WG,SG,N,E> makeBranchView(NodeView<NP,EP,HG,WG,SG,N,E> nv) {
        return new BranchView<NP,EP,HG,WG,SG,N,E>(nv, timeMan) ;
    }
       
    public DropZone<NP,EP,HG,WG,SG,N,E> makeDropZone(NodeView <NP,EP,HG,WG,SG,N,E> nv) {
        return new DropZone<NP,EP,HG,WG,SG,N,E>(nv, timeMan) ;
    }
    
    public Label<NP,EP,HG,WG,SG,N,E> makeLabel(ComponentView<NP,EP,HG,WG,SG,N,E> cv, String id, int position) {
        return new Label<NP,EP,HG,WG,SG,N,E>(id, position, cv, timeMan) ;
    }
    
    public ArrowHead<NP,EP,HG,WG,SG,N,E> makeArrowHead(HigraphView<NP,EP,HG,WG,SG,N,E> hgv){
    	return new ArrowHead<NP,EP,HG,WG,SG,N,E>(hgv, timeMan);
    }
    
    public CircleDecorator<NP,EP,HG,WG,SG,N,E> makeCircleDecorator(HigraphView<NP,EP,HG,WG,SG,N,E> hgv){
    	return new CircleDecorator<NP,EP,HG,WG,SG,N,E>(hgv, timeMan);
    }
    
    public PointDecorator<NP,EP,HG,WG,SG,N,E> makeDecorator(PointDecorator<NP,EP,HG,WG,SG,N,E> pd, HigraphView<NP,EP,HG,WG,SG,N,E> hgv){
    	return pd == null ? null : pd.makeDecorator(pd, hgv, timeMan);
    }
 
 }
