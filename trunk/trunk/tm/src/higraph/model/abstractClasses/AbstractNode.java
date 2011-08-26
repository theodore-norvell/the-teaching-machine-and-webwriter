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

import java.util.*;

import tm.backtrack.BTTimeManager;
import tm.backtrack.BTVar;
import tm.backtrack.BTVector;
import tm.utilities.Assert;

import higraph.model.interfaces.*;
import higraph.model.interfaces.Edge.EdgeCategory;

public abstract class AbstractNode
     <   NP extends Payload<NP>,
        EP extends Payload<EP>,
        HG extends AbstractHigraph<NP,EP,HG,WG,SG,N,E>,
        WG extends AbstractWholeGraph<NP,EP,HG,WG,SG,N,E>,
        SG extends AbstractSubgraph<NP,EP,HG,WG,SG,N,E>,
        N extends AbstractNode<NP,EP,HG,WG,SG,N,E>, 
        E extends AbstractEdge<NP,EP,HG,WG,SG,N,E>
    >
    implements	Node<NP,EP,HG,WG,SG,N,E>
{
    protected final BTVar<NP> payload ;
    protected final WG wholeGraph ;
    protected final BTVar<N> parent;
    protected final BTVar<Integer> depth ;
    protected final BTVar<Boolean> deleted ;
	/*default*/ final BTVector<N> childNodes;
	/*default*/ final BTVector<E> exitingEdges;
	/*default*/ final BTVector<E> enteringEdges;
	/** Those subgraphs of which this is a top node.
	 * <P> Invariant: for all subgraphs sg. this in sg.topNodes iff sg in this.subgraphs
	 *  */
	/*default*/ final BTVector<SG> subgraphs ;
    
    private AbstractNode(WG wholeGraph) {
        this.wholeGraph = wholeGraph;
        BTTimeManager timeMan = wholeGraph.timeMan ;
        this.parent = new BTVar<N>( timeMan );
        this.payload = new BTVar<NP>(timeMan) ;
        this.subgraphs = new BTVector<SG>( timeMan ) ;
        this.exitingEdges = new BTVector<E>( timeMan );
        this.enteringEdges  = new BTVector<E>( timeMan );
        this.depth = new BTVar<Integer>( timeMan ) ;
        this.deleted = new BTVar<Boolean>( timeMan ) ;
        this.deleted.set( false ) ;
        this.childNodes = new BTVector<N>( timeMan );
        
    }

	protected AbstractNode(WG wholeGraph, NP payload ) {
	    this( wholeGraph ) ;
		this.payload.set( payload );
		this.depth.set( 0 ) ;
	}
	
	/** This protected constructor is used for duplicating.
	 * See {@link #dupNode(AbstractNode, AbstractNode, HashMap)} */
	protected AbstractNode(N original, N parent ) {
        this( original.wholeGraph ) ;
	    this.parent.set( parent ) ;
	    this.payload.set( original.payload.get().copy() ) ;
        this.depth.set( parent==null ? 0 : parent.depth.get() + 1 ) ;
        this.deleted.set( false ) ;
	}

	public WG getWholeGraph() {		
		return wholeGraph;
	}
	
	/** Get all children of the node */
	public List<N> getChildren() { 
	    int sz = getNumberOfChildren() ;
	    List<N> result = new ArrayList<N>() ;
	    for( int i=0; i < sz ; ++i ) {
	        result.add( getChild(i) ) ;  }
	    return result ;
	}
	
	/** Get a list of all descendants of the node in pre-order. */
	public List<N> getDescendants() {
        int sz = getNumberOfChildren() ;
        final List<N> result = new ArrayList<N>() ;
        for( int i=0; i < sz ; ++i ) {
            getChild(i).traverse(new Visitor<N>() {
                void previsit(N node) { result.add( node ) ; }
            } ) ;  }
        return result ;
	}
	
	public N getParent() {
		return parent.get();
	}
	
	/** -1 if there is no parent;
	 * otherwise getParent().getChild( getPosition() ) == this.
	 * @return -1 if there is no parent; otherwise getParent().getChild( getPosition() ) == this.
	 */
	public int getPosition() {
	    if( parent == null )
	        return -1 ;
	    else
	        for( int i=0 ; true ; ++i ) 
	            if( parent.get().getChild(i)==this )
	                return i ;
	}
	

	public int getNumberOfChildren() {
		return childNodes.size();
	}

	public N getChild(int i) {
		Assert.check( !deleted.get() );
		return childNodes.get(i);
	}

    public NP getPayload() {
        return payload.get();
    }
	
	public boolean canReplacePayload(NP payload) {
	    Assert.check( !deleted.get() ) ;
		return true;
	}

	public void replacePayload(NP payload) {
		Assert.check(canReplacePayload(payload));
		this.payload.set( payload );
	}

    public boolean canDelete() {
        return true;
    }
    
    public void delete() {
        Assert.check( canDelete() ) ;
        privateDelete() ; }

    private void privateDelete() {
        if( deleted.get() ) return ;
        
        // Note the following loops iterate over copies of the vectors.
        for( N child : childNodes ) child.privateDelete() ;
        for( E edge : enteringEdges ) edge.delete() ;
        for( E edge : exitingEdges ) edge.delete() ;
        
        if( parent.get() != null ) {
            parent.get().childNodes.remove( getThis()  ) ;
            parent.set( null ) ; }
        else {
            wholeGraph.roots.remove( getThis() ) ; }
        wholeGraph.nodes.remove( getThis() ) ;
        if( subgraphs.size() != 0 ) {
            // Remove from all subgraphs in two steps.
            // Step 0. Make a list of all subgraphs to remove it from.
            // Step 1. Remove it from all subgraphs.
            // As a side effect, sg will be removed from this.subgraphs.
            for( SG sg : subgraphs ) {
                sg.removeTop( getThis() ) ;  } }
        deleted.set( true ) ;
    }
    
    public boolean deleted() { return deleted.get()  ; }
    
    public boolean canDetach() {
        Assert.check( !deleted.get() ) ;
        return true;
    }
    
    public void detach() {
        Assert.check( canDetach() ) ;
        
        // If it's not a root already, make it a root.
        if( parent.get() != null ) {
            parent.get().childNodes.remove( getThis() ) ;
            parent.set( null ) ;
            adjustDepth() ;
            wholeGraph.roots.add( getThis() ) ;
        }
    }

    public boolean canDuplicate() {
        Assert.check( !deleted.get() ) ;
        return true;
    }

    public N duplicate() {
        Assert.check( canDuplicate() ) ;
        // "map" maps original nodes to their duplicates.
        HashMap<N, N> map = new HashMap<N,N>() ;
        N newRoot = dupNode( null, map ) ;
        wholeGraph.roots.add( newRoot ) ;
        Set<E> copiedEdges = new HashSet<E>() ;
        dupEdges( getThis(), newRoot, map, copiedEdges ) ;
        return newRoot ;
    }
    
    private N dupNode(N parent, HashMap<N,N> map) {
        N copy = wholeGraph.constructNode( getThis(), parent ) ;
        map.put( getThis(), copy ) ;
        for( N child : childNodes ) {
            N childCopy = child.dupNode(copy, map ) ;
            copy.childNodes.add( childCopy ) ;
        }
        return copy ;
    }
    
    private void dupEdges(N original, N copy, HashMap<N,N> map, Set<E> copiedEdges ) {
        ArrayList<E> allEdges = new ArrayList<E>();
        allEdges.addAll( original.enteringEdges.toList() ) ;
        allEdges.addAll( original.exitingEdges.toList() ) ;
        for( E origEdge : allEdges ) {
            if( ! copiedEdges.contains( origEdge ) ) {
                N origSource = origEdge.getSource() ;
                N source ;
                if( map.containsKey( origSource ) )
                    source = map.get( origSource ) ;
                else source = origSource ;
                
                N origTarget = origEdge.getTarget() ;
                N target ;
                if( map.containsKey( origTarget ) )
                    target = map.get( origTarget ) ;
                else target = origTarget ;
                
                wholeGraph.makeEdge(source, target, origEdge.getPayload().copy() ) ;
                copiedEdges.add( origEdge ) ;
            }
        }
        // Now do the same for the children
        for( N origChild : original.childNodes ) {
            N myChild = map.get( origChild ) ;
            dupEdges(origChild, myChild, map, copiedEdges ) ; }
    }

    public boolean canInsertChild(int position, N node) {
        Assert.check( !deleted.get() && !node.deleted.get() ) ;
        NodeCategory nodeCat = categorize( node ) ;
        return 0 <= position && position <= childNodes.size() 
            && nodeCat != NodeCategory.SELF 
            && nodeCat != NodeCategory.ANCESTOR ;
    }

    public void insertChild(int position, N root) {
        Assert.check( canInsertChild( position, root) 
                   && root.getParent() == null) ;
        this.uncheckedInsertChild( position, root ) ;
    }
    
    public boolean canReplace(N node) {
        Assert.check( !deleted.get() && !node.deleted.get() ) ;
        NodeCategory nodeCat = categorize( node ) ;
        return nodeCat != NodeCategory.SELF 
            && nodeCat != NodeCategory.ANCESTOR ;
    }

    public void replace(N root) {
        Assert.check( root.getParent() == null
                   && canReplace( root ) ) ;
        
        // Record the subgraphs of this node.
        final List<SG> recordedSubgraphs = subgraphs.toList() ;
        
        // Ensure that no node in the replacing tree is already
        // in any of these subgraphs.
        // This is done in two steps.
        // Step 0. Collect the nodes to remove
        final List<N> nodesToRemove = new ArrayList<N>() ;
        final List<SG> subgraphList = new ArrayList<SG>() ;
        root.traverse( new Visitor<N>() {
            void previsit( N node ) {
                for( SG sg : node.subgraphs ) 
                    if( recordedSubgraphs.contains( sg ) ) {
                        // node is a top node of a subgraph sg that the
                        // replaced node is a top node of.
                        nodesToRemove.add( node ) ;
                        subgraphList.add(sg) ; }
            } } ) ;
        //Step 1 Do the removals
        Iterator<SG> sgIt = subgraphList.iterator() ;
        for( N node : nodesToRemove ) 
            sgIt.next().removeTop( node ) ;
        // End of sanitization.
        
        // Delete this node and replace it with the root.
        if( parent.get() == null ) {
            this.privateDelete() ;
        }
        else {
            N tempParent = parent.get() ; // As this node is about to be deleted. 
            int position = parent.get().childNodes.lastIndexOf( getThis() ) ;
            this.privateDelete() ;
            tempParent.uncheckedInsertChild( position, root ) ;
        }
        
        // Put the (former) root into all subgraphs that this node was a top node of.
        for( SG sg : recordedSubgraphs ) sg.addTop( root ) ;
    }
    
    /** Just like insertChild, but does not check the well-formedness
     * of inserting the child.  This should only be used to add children
     * to a node that has just been created.
     * 
     * @param position
     * @param newChild
     */
    public void uncheckedInsertChild( int position, N newChild ) {
        Assert.check( 0 <= position && position <= childNodes.size()
                   && newChild.parent.get() == null ) ;
        
        // Remove the new child and all its descendants as
        // top nodes of any subgraph of which the parent is a member.
        // This is because a descendant of a top node should not
        // be a top node.
        // We do this in two steps.
        // Step 0. Collect all nodes that need to be
        // removed from a subgraph.
        final List<N> nodeToRemove = new ArrayList<N>() ;
        final List<SG> subgraphList = new ArrayList<SG>() ;
        final N parent = getThis() ;
        newChild.traverse( new Visitor<N>() {
                void previsit( N node ) {
                    for( SG sg : node.subgraphs ) 
                        if( sg.contains( parent ) ) {
                            nodeToRemove.add(node) ;
                            subgraphList.add( sg ) ;
                        }
        } } ) ;
        // Step 1. Remove the nodes from the subgraphs.
        Iterator<SG> sgIt = subgraphList.iterator() ;
        for( N node : nodeToRemove ) {
            sgIt.next().removeTop( node ) ; }
    
        // Now move the newChild to this node.
        wholeGraph.roots.remove( newChild ) ;
        childNodes.insertElementAt( newChild, position ) ;
        newChild.parent.set( getThis()  );
        newChild.adjustDepth( ) ;
    }
    
    public boolean canPermuteChildren(int[] p) {
        Assert.check( !deleted.get() ) ;
        Assert.check( p.length == childNodes.size() ) ;
        for( int i=0, sz = p.length ; i < sz ; ++i ) {
            boolean found = false ;
            for( int j = 0 ; j<sz ; ++j ) {
                if( p[j]==i ) {found = true ; break ; } }
            Assert.check( found ) ; }
        return true;
    }

    public void permuteChildren(int[] p) {
        Assert.check( canPermuteChildren(p)) ;
        ArrayList<N> newChildList = new ArrayList<N>() ;
        for( int i = 0 ; i < childNodes.size() ; ++i )
            newChildList.add( childNodes.get(p[i]) ) ;
        for( int i = 0 ; i < childNodes.size() ; ++ i ) 
            childNodes.removeElementAt( 0 ) ;
        for( N node : newChildList ) 
            childNodes.add( node ) ;
    }

    public Collection<E> exitingEdges() {
        Assert.check( !deleted.get() ) ;
        return exitingEdges.toList() ;
    }

    public Collection<E> enteringEdges() {
        Assert.check( !deleted.get() ) ;
        return enteringEdges.toList() ;
    }
    
    public Collection<E> getIncidentEdges() {
        Set<E>  result = new HashSet<E>() ;
        result.addAll( enteringEdges() ) ;
        result.addAll( exitingEdges() ) ;
        return result ;
    }

    public Collection<E> getEdges(Set<EdgeCategory> categories) {
        Assert.check( !deleted.get() ) ;
        Collection<E>  superSet ;
        if( categories.contains(EdgeCategory.OTHER) )
            superSet = wholeGraph.getEdges() ;
        else {
            superSet = new HashSet<E>() ;
            this.collectLocalEdges( superSet ) ; }
        
        Set<E>  result = new HashSet<E>() ;
        for( E edge : superSet ) {
            EdgeCategory cat = categorize( edge ) ;
            if( categories.contains( cat ) ) {
                result.add( edge ) ;  } }
        
        return result ;
    }
    
    void collectLocalEdges(Collection<E>  contained ) {
        contained.addAll( enteringEdges.toList() ) ;
        contained.addAll( exitingEdges.toList() ) ;
        
        for( N child : childNodes ) 
            child.collectLocalEdges( contained ) ;
    }

    public NodeCategory categorize(N node) {
        if( node == this ) return NodeCategory.SELF ;
        int dThis = this.depth.get() ;
        int dNode = node.depth.get() ;
        if( dNode > dThis ) {
            N p = node ;
            for( int i = 0 ; i < dNode-dThis ; ++i ) p = p.parent.get() ;
            if( p == this ) return NodeCategory.DESCENDANT ;
            else return NodeCategory.OTHER ; }
        if( dNode < dThis ) {
            N p = getThis() ;
            for( int i = 0 ; i < dThis-dNode ; ++i )  p = p.parent.get() ;
            if( p == node ) return NodeCategory.ANCESTOR ; }
        return NodeCategory.OTHER ;
    }

    public EdgeCategory categorize(E edge) {
        NodeCategory srcCat = categorize( edge.getSource() ) ;
        NodeCategory trgCat = categorize( edge.getTarget() ) ;
        switch( srcCat ) {
        case SELF:
            switch( trgCat ) {
            case SELF : return EdgeCategory.LOOP ;
            case DESCENDANT: return EdgeCategory.DOWN ;
            default: return EdgeCategory.OUT ;
            }
        case DESCENDANT:
            switch( trgCat ) {
            case SELF : return EdgeCategory.UP ;
            case DESCENDANT: return EdgeCategory.INTERNAL ;
            default: return EdgeCategory.DEEP_OUT ;
            }
        default :
            switch( trgCat ) {
            case SELF : return EdgeCategory.IN ;
            case DESCENDANT: return EdgeCategory.DEEP_IN ;
            default: return EdgeCategory.OTHER ;
            }
        }
    }
    
    /** The least common ancestor.
     * 
     * @param q a node. Not null.
     * @return null if there is not least common ancestor in the WholeGraph,
     * otherwise the least common ancestor of this and q.
     */
    @Override public N leastCommonAncestor( N q ) {
        Assert.check(q != null) ;
        N p = getThis() ;
        int pdepth = p.depth.get();
        int qdepth = q.depth.get();
        while( pdepth > qdepth ) {
            p = p.parent.get(); --pdepth ; }
        while( qdepth > pdepth ) {
            q = q.parent.get(); --qdepth ; }
        // assert pdepth == qdepth ;
        while( p != q ) {
            p = p.parent.get(); --pdepth ;
            q = q.parent.get(); --qdepth ; }
        return p ;
    }
    
    @Override public Collection<E> getGovernedEdges() {
        // (0) Find all local edges
        Collection<E> localEdges = new HashSet<E>() ;
        this.collectLocalEdges(localEdges) ;
        // (1) Keep only those that have this node as
        // the LCA of the source and target, but aren't loops
        // and those that are loops and have an LCA that is a child
        // of this node.
        Collection<E> result = new HashSet<E>() ;
        for( E e : localEdges ) { 
            if( e.getSource()==e.getTarget() ) {
                // Loop. Is the parent this node?
                if( e.getSource().getParent() == this ) 
                    // Keep it.
                    result.add(e) ; }
            else {
                if( e.getSource().leastCommonAncestor( e.getTarget() ) == this )
                    // Keep it
                    result.add(e) ; } }
        return result ;
    }
	
    private void adjustDepth() {
        traverse( new Visitor<N>() {
            void previsit(N node) {
                if( node.parent.get() == null) node.depth.set( 0 );
                else node.depth.set( node.parent.get().depth.get() + 1 ) ;
            } } ) ;
    }
        
    /** Should be implemented by "return this" 
     * <p> See Java Generics and Collections (1st ed) section 9.4.
     * @return a reference to this object
     */
    protected abstract N getThis() ;
    
    protected static abstract class Visitor<N> {
        void previsit(N node) {} 
        void postvisit(N node) {}
    }
    
    protected void traverse( Visitor<N> visitor ) {
        visitor.previsit( getThis() ) ;
        for( N child : childNodes ) child.traverse(visitor) ;
        visitor.postvisit( getThis() ) ;
    }
}
