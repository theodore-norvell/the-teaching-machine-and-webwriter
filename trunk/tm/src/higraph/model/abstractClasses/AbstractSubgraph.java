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
 * Created on 2009-09-12 by Theodore S. Norvell. 
 */
package higraph.model.abstractClasses;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import higraph.model.interfaces.*;
import higraph.model.interfaces.Node.NodeCategory;
import tm.backtrack.*;
import tm.utilities.Assert;

public abstract class AbstractSubgraph
    <   NP extends Payload<NP>,
        EP extends Payload<EP>,
        HG extends AbstractHigraph<NP,EP,HG,WG,SG,N,E>,
        WG extends AbstractWholeGraph<NP,EP,HG,WG,SG,N,E>,
        SG extends AbstractSubgraph<NP,EP,HG,WG,SG,N,E>,
        N extends AbstractNode<NP,EP,HG,WG,SG,N,E>, 
        E extends AbstractEdge<NP,EP,HG,WG,SG,N,E>
    >
    implements AbstractHigraph<NP,EP,HG,WG,SG,N,E>,
               Subgraph<NP,EP,HG,WG,SG,N,E>
{
    
    /*default*/ final WG wholeGraph ;
    /** A list of nodes representing the top nodes
     * of this subgraph.
     * 
     * <p>Invariants:
     * <ul>
     *     <li>for all v. v in topNodes == this in v.subgraphs
     *     <li>for all v,u in topNodes. u is not a descendant of v
     * </ul>
     * 
     */
    private final BTVector<N> topNodes  ;
    
    protected AbstractSubgraph( WG wholeGraph ) {
        this.wholeGraph = wholeGraph ;
        this.topNodes = new BTVector<N>( wholeGraph.timeMan ) ;
    }

    public void addTop(N v) {
        Assert.check( !v.deleted.get() && v.wholeGraph == this.wholeGraph); 
        // The following loop serves 3 purposes:
        // * If v is already a top node, there is nothing to do.
        // * If v is already in the subgraph, but not a top node,
        // v must not become a top node.
        // * If a descendant t of v is a top node, t should be removed
        // as a topNode before v is added.
        for( N top : topNodes ) {
            NodeCategory cat = top.categorize(v) ;
            switch( cat ) {
                case SELF : 
                    // v is already a top node.  Nothing to do
                    return ;
                case DESCENDANT :
                    // v is already has an ancestor as a top.
                    // It can not be added as a top.
                    return ;
                case ANCESTOR :
                    // top is a descendant of v.
                    // We need to remove the descendant first.
                    takeOut( top ) ;
                    break ; } }
        // If and when we get out of the loop, the subgraph does not
        // contain v, nor any descendant of v, nor any ancestor of v.
        putIn( v ) ;
    }

    /** Remove v as a topNode.
     * <p>Note that v need not be a top node,
     * in which case this method has no effect. 
     */
    public void removeTop(N v) {
        takeOut( v ) ;
    }

    public List<E> getEdges() {
        List<E> result = new ArrayList<E>() ;
        
        for( E edge : wholeGraph.edges ) {
            NodeCategory srcCat = categorize( edge.getSource() ) ;
            NodeCategory trgCat = categorize( edge.getTarget() ) ;
            if( (srcCat == NodeCategory.SELF || srcCat == NodeCategory.DESCENDANT) 
            &&  (trgCat == NodeCategory.SELF || trgCat == NodeCategory.DESCENDANT) )
                result.add(edge) ; }
            
        return result ;
    }

    public List<N> getNodes() {
        final List<N> result = new ArrayList<N>() ;
        for( N top : topNodes )
            top.traverse( new AbstractNode.Visitor<N>() {
                void previsit( N node ) {
                    result.add( node ) ; } } ) ;
        return result ;
    }

    public List<N> getTops() {
        // A fresh copy is return, in case there is a call
        // to removeTop.  This also protects against direct
        // deletion.
        return topNodes.toList() ;
    }

    public WG getWholeGraph() {
        return wholeGraph ;
    }

    public boolean contains(N node) {
        NodeCategory cat = categorize( node ) ;
        return cat == NodeCategory.SELF || cat == NodeCategory.DESCENDANT ;
    }

    public boolean contains(E edge) {
        return getEdges().contains(edge);
    }

   
    NodeCategory categorize(N node) {
        for( N top : topNodes ) {
            NodeCategory cat = top.categorize(node) ;
            switch( cat ) {
            case SELF : case ANCESTOR : case DESCENDANT :
                return cat ;
            } }
        return NodeCategory.OTHER ; }
    
    /*default*/ void takeOut(N node) {
        topNodes.remove(node) ;
        node.subgraphs.remove(getThis()) ;
    }
    
    /*default*/ void putIn(N top) {
        NodeCategory cat = categorize( top ) ;
        Assert.check( cat==NodeCategory.OTHER
                && !topNodes.contains(top)
                && !top.subgraphs.contains(getThis())) ;
        topNodes.add(top) ;
        top.subgraphs.add(getThis()) ;
    }
    
    /** Should be implemented by "return this" 
     * <p> See Java Generics and Collections (1st ed) section 9.4.
     * @return a reference to this object
     */
    protected abstract SG getThis() ;
    
    @Override public Collection<E> getGovernedEdges() {
        return wholeGraph.getGovernedEdges( this );
    }
}
