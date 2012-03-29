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


import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import tm.utilities.Assert;
import tm.backtrack.*;

import higraph.model.interfaces.*;

public abstract class AbstractWholeGraph
    <   NP extends Payload<NP>,
        EP extends Payload<EP>,
        HG extends AbstractHigraph<NP,EP,HG,WG,SG,N,E>,
        WG extends AbstractWholeGraph<NP,EP,HG,WG,SG,N,E>,
        SG extends AbstractSubgraph<NP,EP,HG,WG,SG,N,E>,
        N extends AbstractNode<NP,EP,HG,WG,SG,N,E>, 
        E extends AbstractEdge<NP,EP,HG,WG,SG,N,E>
    >
    implements AbstractHigraph<NP,EP,HG,WG,SG,N,E>, WholeGraph<NP,EP,HG,WG,SG,N,E>
{

    /*default*/ final BTTimeManager timeMan ;
	/*default*/ final BTVector<N> roots;
	/*default*/ final BTVector<N> nodes;
	/*default*/ final BTVector<E> edges;
    /*default*/ final BTVector<SG> subgraphs ;
	
	public AbstractWholeGraph( BTTimeManager timeMan ) {
	    this.timeMan = timeMan ;
	    roots = new BTVector<N>( timeMan ) ;
	    nodes = new BTVector<N>( timeMan ) ;
	    edges = new BTVector<E>( timeMan ) ;
	    subgraphs = new BTVector<SG>( timeMan ) ;
	}

	public N makeRootNode(NP payload) {
		N node = constructNode(getWholeGraph(), payload);
		addChildrenHook( node ) ;
		roots.add( node ) ;
		nodes.add( node ) ;
		return node;
	}
    
    protected void addChildrenHook(N node) {
        // No children by default.
    }

    public List<N> getTops() {
        // Make a copy to avoid modification.
        return roots.toList() ;
    }    /** Is the node a top node */
	
    public boolean isTop(N node) {
		return roots.contains( node ) ;
	}
	
	/** Get a top node.
	 * <p><strong>precondition</strong> 0 <= position && position < getNumberOfTops() 
	 * @param position
	 * @return
	 */
	public N getTop(int position) {
		return roots.get(position) ;
	}
	
	/** How many top nodes are there. */
	public int getNumberOfTops() {
		return roots.size() ;
	}
	
	/** Can the node be moved to the given position.
	 * <p> result implies 0 <= position && position < getNumberOfTops() && node.isTop()
	 * @param node
	 * @param position
	 * @return
	 */
	public boolean canMoveTop(N node, int position) {
		return isTop(node) && 0 <= position && position < roots.size() ;
	}
	
	/** Can the node be moved to the given position.
	 * <p><strong>precondition</strong> canMoveTop(node, position)
	 * @param node
	 * @param position
	 * @return
	 */
	public void moveTop(N node, int position) {
		Assert.check( canMoveTop(node, position) ) ;
		roots.remove(node) ;
		roots.insertElementAt(node, position) ;
	}
    
    /** Return a reference to this objects. 
     * <p>Should be implemented in any concrete nongeneric subclass by "return this" 
     * <p> See Java Generics and Collections (1st ed) section 9.4.
     * @return a reference to this object
     */
    public abstract WG getWholeGraph() ;

	public boolean canMakeEdge(N source, N target, EP label) {
		return source.wholeGraph == this && target.wholeGraph == this;
	}

	public E makeEdge(N source, N target, EP label) {
		Assert.check(canMakeEdge(source, target, label));
		//System.out.println("Making edge from " + source + " to " + target);
		E edge = constructEdge(source, target, label);

		// Here I add this edge into the lists of outEdge and inEdge
		source.exitingEdges.add(edge);
		target.enteringEdges.add(edge);
		edges.add(edge);
		return edge;
	}
	
	public SG makeSubGraph() {
	    SG sg = constructSubGraph() ;
	    subgraphs.add( sg ) ;
	    return sg ;
	}

    public List<E> getEdges() {
        return edges.toList();
    }

    public List<N> getNodes() {
        return nodes.toList() ;
    }
    
    public boolean contains(N node) {
        return nodes.contains( node ) ;
    }
    
    public boolean contains(E edge) {
        return edges.contains( edge ) ;
    }
    
    /** Construct a node
     * <p>This method should be implemented in any nongeneric subclass by simply calling the constructor of the node class */
    protected abstract N constructNode(WG higraph, NP payload) ;
    
    /** Construct a node
     * <p>This method should be implemented in any nongeneric subclass by simply calling the constructor of the node class */
    protected abstract N constructNode(N original, N parent ) ;
    
    /** Construct a node
     * <p>This method should be implemented in any nongeneric subclass by simply calling the constructor of the edge class */
    protected abstract E constructEdge(N source, N target, EP label) ;
    

    
    /** Construct a node
     * <p>This method should be implemented in any nongeneric subclass by simply calling the constructor of the subgraph class */
    protected abstract SG constructSubGraph() ;
    
    
    @Override public Collection<E> getGovernedEdges() {
        return getGovernedEdges( this );
    }
    
    /** Get all edges governed by a given higraph.
     * @see higraph.model.interfaces.Higraph#getGovernedEdges()
     * @param higraph
     * @return
     */
    Collection<E> getGovernedEdges( AbstractHigraph<NP,EP,HG,WG,SG,N,E> higraph ) {
        /* Implementation note: This is a common utility method used by both
         *      AbstractWholeGraph.getGovernedEdges() 
         *  and
         *      AbstractSubgraph.getGovernedEdges() 
         *  Ideally we would put the common implementation of
         *  getGovernedEdges() in the common super class of
         *  AbstractWholeGraph and AbstractSubgraph. However there is no such
         *  class (at present) and so I put the common implementation here.
         *  It would be a static method, but for the fact that the Java compiler
         *  won't allow it to be.  It is crucial that there are no messages sent to
         *  the "this" object. Messages should be sent to the higraph parameter.
         */
        // (0) Find all edges that have at least one endpoint in this higraph.
        Collection<E> edges = new HashSet<E>() ;
        for( N n : higraph.getNodes() ) {
            edges.addAll( n.enteringEdges() ) ;
            edges.addAll( n.exitingEdges() ) ; }
            
        // (1) Keep only those that are loops on top nodes or
        // that are between distinct trees of this higraph.
        Collection<E> result = new HashSet<E>() ;
        List<N> tops = higraph.getTops() ;
        for( E e : edges ) { 
            N src = e.getSource() ;
            N trg = e.getTarget();
            // Only keep edges if both endpoints are in this higraph.
            if( higraph.contains(src) && higraph.contains(trg) ) {
                if( src==trg ) {
                    // Keep loops that are on top nodes
                    if( tops.contains( src ) ) result.add(e) ; }
                else {
                    // Keep edges whose LCA is not contained in this higraph.
                    N lca = src.leastCommonAncestor( trg ) ;
                    if( lca == null || ! higraph.contains(lca) ) result.add(e) ; } } }
        return result ;
    }

}
