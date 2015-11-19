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

package higraph.model.abstractClasses;

import java.awt.geom.GeneralPath;
import java.util.ArrayList;
import java.util.List;

import higraph.model.interfaces.*;
import tm.backtrack.BTVar;
import tm.backtrack.BTVector;
import tm.utilities.Assert;

public abstract class AbstractEdge
    <   NP extends Payload<NP>,
        EP extends Payload<EP>,
        HG extends AbstractHigraph<NP,EP,HG,WG,SG,N,E>,
        WG extends AbstractWholeGraph<NP,EP,HG,WG,SG,N,E>,
        SG extends AbstractSubgraph<NP,EP,HG,WG,SG,N,E>,
        N extends AbstractNode<NP,EP,HG,WG,SG,N,E>, 
        E extends AbstractEdge<NP,EP,HG,WG,SG,N,E>
    >
    implements Edge<NP,EP,HG,WG,SG,N,E>
{
	
	private BTVar<N> source;
	private BTVar<N> target;
	private final WG wholeGraph ;
	private BTVar<EP> label ;
	private BTVar<Boolean> deleted ;

	protected AbstractEdge(N source, N target, EP label, WG wholeGraph){
	    this.source = new BTVar<N>( wholeGraph.timeMan ) ;
		this.source.set( source );
        this.target = new BTVar<N>( wholeGraph.timeMan ) ;
		this.target.set( target );
        this.label = new BTVar<EP>( wholeGraph.timeMan ) ;
		this.label.set( label ) ;
		this.wholeGraph = wholeGraph ;
        this.deleted = new BTVar<Boolean>( wholeGraph.timeMan ) ;
		this.deleted.set(false) ;		
	}

	public N getSource() {
		return source.get();
	}

	public N getTarget() {
		
		return target.get();
	}

    public EP getPayload() {
        return label.get();
    }

    public WG getWholeGraph() {
        return wholeGraph;
    }

    public boolean canDelete() {
        return true;
    }

    public void delete() {
        if( ! deleted.get() ) {
            // TODO at some point we may need to deregister the views
            // here. [TSN 2009 Sept 7]
            wholeGraph.edges.remove( getThis() ) ;
            target.get().enteringEdges.remove( getThis() ) ;
            source.get().exitingEdges.remove( getThis() ) ;
            target.set( null )  ;
            source.set( null )  ;
            deleted.set( true );
        }
    }

    public boolean deleted() {
        return deleted.get();
    }

    public boolean canChangePayload(EP newPayload) {
        return true;
    }

    public void changePayload(EP newPayload) {
        Assert.check( canChangePayload( newPayload ) ) ;
        label.set( newPayload )  ;
    }

    public boolean canChangeSource(N newSource) {
        return true ;
    }
    
    public boolean canChangeTarget(N newSource) {
        return true;
    }

    public void changeSource(N newSource) {
        Assert.check( canChangeSource( newSource ) )  ;
        source.get().exitingEdges.remove( getThis() ) ;
        newSource.exitingEdges.add( getThis() ) ;
        source.set( newSource ) ;
    }

    public void changeTarget(N newTarget) {
        Assert.check( canChangeSource( newTarget ) )  ;
        target.get().exitingEdges.remove( getThis() ) ;
        newTarget.exitingEdges.add( getThis() ) ;
        target.set( newTarget );
    }
    
    /** Return the node that governs this edge, if there is one. Otherwise return null */
    public N getGoverningNode() {
        N src = source.get() ;
        N trg = target.get() ;
        if( src == null || trg == null ) return null ;
        if( src == trg ) { return src.getParent() ; }
        N lca = src.leastCommonAncestor( trg ) ;
        return lca ;
    }
    
    /** Return the node within a given Higraph that governs this edge, if there is one. Otherwise return null. */
    public N getGoverningNode(HG hg) {
        N gn = getGoverningNode() ;
        if( gn == null ) return null ;
        else if( ! hg.contains(gn) ) return null ;
        else return gn ;
    }

    /** Should be implemented by "return this" 
     * <p> See Java Generics and Collections (1st ed) section 9.4.
     * @return a reference to this object
     */
    protected abstract E getThis() ;
    
    

}
