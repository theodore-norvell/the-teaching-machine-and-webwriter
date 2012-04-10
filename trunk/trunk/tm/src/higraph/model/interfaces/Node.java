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

package higraph.model.interfaces;

import java.util.*;

/**An interface for nodes in hierarchical graphs.
 * <p>Nodes carry payloads (i.e. labels)
 * <p>Nodes are arranged in a parent/child hierarchy
 * <p>Nodes serve as the end points for edges.
 * 
 * @author Theodore S. Norvell & Michael Bruce-Lockhart
 *
 * @param <NP> The node payload type. Each node carries a payload.
 * @param <EP> The edge payload type. Each edge carries a payload.
 * @param <WG> The wholeGraph type. 
 * @param <SG> The subgraph type. 
 * @param <N> The node type.
 * @param <E> The edge type.
 */
public interface Node
    < NP extends Payload<NP>,
    EP extends Payload<EP>,
    HG extends Higraph<NP,EP,HG,WG,SG,N,E>,
    WG extends WholeGraph<NP,EP,HG,WG,SG,N,E>,
    SG extends Subgraph<NP,EP,HG,WG,SG,N,E>,
    N extends Node<NP,EP,HG,WG,SG,N,E>, 
    E extends Edge<NP,EP,HG,WG,SG,N,E> >
extends Higraph<NP, EP, HG, WG, SG, N, E>
{
	
    /** The node category of n with respect to p is
     * <table>
     *   <tr> <th align="left">SELF</th> <td>if n is p,</td> </tr>
     *   <tr> <th align="left">ANCESTOR</th> <td>if n is an ancestor of p,</td> </tr>
     *   <tr> <th align="left">DESCENDANT</th> <td>if n is a descendant of p, and</td> </tr>
     *   <tr> <th align="left">OTHER</th> <td>otherwise.</td> </tr>
     * </table>
     */
	public enum NodeCategory{ SELF, ANCESTOR, DESCENDANT, OTHER }

    /** Get the WholeGraph which created this node.
	 *  */
	WG getWholeGraph() ;

	/** The parent of this node.
     * <p> If the node is a root,
     * then <code>null</code> is returned.
     * @return
     */
    N getParent( ) ;
    
    
    /** Get a list of all children in left to right order */
    List<N> getChildren() ;
    
    /** Get a list of all descendants in prefix order */
    List<N> getDescendants() ;
	
	/**
	 *  How many children does this node have.
	 */
	int getNumberOfChildren() ;
	
	/** Return child i
	 * <p>Precondition: 0 <= i && i < getNumberOfChildren() 
	 * @param i
	 * @return
	 */
	N getChild(int i) ;
	
	/** Get the payload.  Warning mutating (as opposed to replacing)
     * the payload may cause the graph to become ill-formed. */
    NP getPayload() ;
    
    /** Can the current payload be replaced with another.
     * <p> Replacing the payload may simply mean replacing
     * the payload, or it may require extensive modifications
     * to the graph to ensure that well-formedness is respected.
     * 
     * @param payload
     * @return
     */
    boolean canReplacePayload( NP payload ) ;
	
    /** Replace the payload with a new one.
     * <p> Precondition: canReplacePayload( payload ) 
     * @param payload
     */
    void replacePayload( NP payload ) ;
	

    /** Can this node be deleted.
     * Deleting a node may cause the graph to become mal-formed.
     * In this case either <kbd>canDelete</kbd> will return false,
     * or else <kbd>delete</kbd> will restructure the tree to
     * restore it to being well formed. For example we might have
     * tables that have rows as children and rows that have items
     * as children.  Suppose there is an invariant that all
     * table rows must have the same number of chidren. Then an
     * attempt to delete an item would either be rejected or the
     * delete method would delete the corresponding item in all rows.
     * @return true if delete can be called.
     */
    boolean canDelete() ;
    

    /** Delete the node from the graph.
     * 
     * <p>
     * Deleting a node also entails deleting all its descendants
     * and all edges that are in edges, out edges, loop edges,
     * up-edges, or down-edges of this node or any of its descendants.
     * 
     * <p>
     * Note that deleting a node may entail other structural
     * changes to the graph. (See <kbd>canDelete</kbd> for an
     * example.)
     * 
     * <p>
     * The node and all its descendant will be removed from all subgraphs. 
     * 
     * <p>
     * Once a node or edge has been deleted, it can no longer be used for
     * anything ever again.  It is a (generally unwritten) precondition of
     * each routine in this interface (except <kbd>deleted</kbd> 
     * that this node (and any node parameters) have not been deleted.
     * (An implementation might mark each deleted node and edge in some
     * way so that this precondition can be checked.)  If you want to
     * take a node out of a tree, but keep it around for some future
     * purpose, consider using <kbd>detach</kbd> instead of delete.
     * 
     * <p><strong>Precondition:</strong></p>
     * <ul>
     *      <li><code> canDelete() </code></li>
     * </ul>
     * 
     */
    void delete() ;
    
    /** Indicate whether the node has been deleted. */
    boolean deleted() ;
    
    /** Can the node be made a root.
     * <p> In many cases <kbd>canDetach</kbd> will be the same as <kbd>canDelete</kbd>.
     * 
     * @return
     */
    
    boolean canDetach() ;
    
    /** Make the node a root.
     * 
     * <p> Detaching a node makes it a root. If it is already a root,
     * it remains a root. If it is not a root, then its parent
     * will have one fewer children after the detach.
     * 
     * <p> If the node is the top node of a subgraph, it remains a top
     * node of that subgraph. It will be removed from all subgraphs
     * that it is in, but is not a top node of. 
     * 
     * <p> Edges that have this node or its descendants as source
     * or target are unaffected by detach, but they may have
     * their classification with respect to various nodes changed.
     * 
     * <p><strong>Precondition:</strong></p>
     * <ul><li><code> canDetach() </code></li>
     * </ul>
     * 
     */
    void detach() ;
	
    /** Can the node be duplicated.
     * <p> Frankly, it's hard to think of a reason a node couldn't be duplicated.
     * 
     * @return
     */
    boolean canDuplicate() ;
    
    /** Duplicate the node.
     * 
     * <p>
     * Duplicating a node makes a copy of this node and copies of all its
     * descendants. The copy of this node will be a new root.
     * 
     * <p>
     * The copy and all its descendants will be in no subgraph.
     * 
     * <p>
     * All edges that enter or exit this node or its descendants
     * will also be duplicated as follows.
     * <ul>
     *     <li> If both source and target of the edge are among
     *          this node and its descendants, the copy of the edge
     *          will run between the two copies.
     *     </li>
     *     <li> All other copied edges run between a node that is this node
     *          or a descendant (v) and a node that is not (u). The new edge
     *          runs between a new node (v's copy) and the noncopied node (u),
     *          in the same direction, of course, as the original.
     *     </li>
     * </ul>
     * <p>
     * Payloads of nodes and edges are copied by the <kbd>copy()</kbd>
     * method of Payload.
     * 
     * <p><strong>Precondition:</strong></p>
     * <ul><li><code> canDuplicate() </code></li>
     * </ul>
     * @return the new node.
     * 
     */
    N duplicate() ;
    
    /** Can a node be inserted into this node at the given position.
     * 
     *  
     * <p>
     * As a general rule this routine will return false unless
     * position is in range (<kbd>0 <= position && position <= getNumberOfChildren</kbd>)
     * Furthermore the child must not be the same as this node, nor
     * can it be an ancestor of this node.
     * 
     * <p>
     * There may be further restrictions, based on well-formedness
     * constraints.
     * 
     * <p> Even though <code>insertChild</code> requires that the
     * <code>child</child> be a root, this routine ignores the
     * "rootiness" (or lack thereof) of the child. The reason for
     * this anomally is discussed below.
     * 
     * <p>In order to move a node, it should be detached first and then
     * inserted.  The <code>canInsert</code> method can be called before
     * the detach, like this:
     * <pre>    if( child.canDetach() && parent.canInsert(i, child) ) {
     *        child.detach() ;
     *        if( canInsert(i, child) // Double check.
     *            parent.insert(i, child) ; }
     * </pre>
     * This works, even if <code>child</code> is not a root, because
     * <code>canInsert</code> ignores the fact that <code>child</code> is not a root.
     * 
     * <p>A similar comment applies to copying a node. I.e. in order to
     * copy a node it should be duplicated and then inserted in the new
     * position. Thus you can write
     * 
     * <pre>    if( child.canDuplicate() && parent.canInsert(i, child) ) {
     *         N duplicate = child.duplicate();
     *         if( canInsert(i, duplicate) // Double check.
     *             parent.insert(i, duplicate) ; }
     * </pre>
     * 
     * @param position In most cases this would be the index where the
     *         child will end up.  Typically the child already at that position
     *         (if any) will be shifted to the right.
     *         There is no limitation on the value of position. If it is too large,
     *         or too small, the method returns false.
     * @param node The node to insert.
     * @return true if the replacement will succeed and false otherwise.
     */
    boolean canInsertChild( int position, N node ) ;
    
    /** Insert a child into this node.
     * 
     * <p> Specific implementations may interpret this in different ways.
     * For example if the invariant of a node requires it to have exactly 1 child,
     * inserting a new child might cause the existing child to be deleted.
     * 
     * <p> The inserted root remains a top node of any Subgraph it is a 
     * top node of unless the new parent (i.e. this node) is already in that Subgraph.
     * (This exception is to maintain the invariant that if a node
     * is a top node of a subgraph then its parent must not be in that
     * subgraph.)  The inserted node will be added to all the subgraphs
     * that this node is in. (This maintains the invariant that if a node
     * is in a subgraph, so are all its children.)
     * 
     * <p> The node being inserted and its descendants may have edges related
     * to them. The categorisation of these nodes may change.
     * 
     * <p><strong>Precondition:</strong></p>
     * <ul>
     *     <li><code> canInsertChild(position, root) </code></li>
     *     <li><code> root.getParent() == null  </code></li>
     * </ul>
     * 
     * @param position In most cases this would be the index where the
     *         child will end up.  Typically the child already at that position
     *         (if any) will be shifted to the right.
     * @param root The root to insert.
     */
    void insertChild( int position, N root ) ;
    
    
    /** Can this node be replaced by another
     * 
     * <p>As a general rule this routine will return false if
     * the root is an ancestor of this node or is this node.
     * <p>
     * There may be further restrictions, based on well-formedness
     * constraints.
     * 
     * <p>
     * Note that, although replace requires that the node be a root,
     * this routine does not. To move a node it should be detached
     * before replacing.  However v.canReplace(u) (together with
     * u.canDetach()) may consulted before the detach is done.
     * (It is recommended to consult v.canReplace(u) again after
     * the u.detach()).  Thus the sequence of calls could be
     * <pre> 
     *       if( v.canReplace(n) && n.canDetach()) {  // Check
     *           n.detach() ;
     *           if( v.canReplace(n) ) // Double check
     *               v.replace(n) ; }
     * </pre>
     * 
     * @param node The node to replace this node with.
     * @return true if the replacement will succeed and false otherwise.
     */
    boolean canReplace( N node ) ;
    
    /** Replace this node with another.
     * <p>Specific implementations may interpret this in different ways.
     * 
     * <p> If the root is the top node of a subgraph, it remains
     * as the top node of that subgraph.  If this node is the top
     * node of a subgraph, the new node will become a top node of
     * that subgraph.
     * 
     * <p> The node being inserted and its descendants may have edges related
     * to them. The categorisation of these nodes may change.
     * 
     * <p><strong>Precondition:</strong></p>
     * <ul><li>
     *          <code> canReplace(root) </code></li>
     *          <code> root.getParent() == null  </code></li>
     * </ul>
     * 
     * @param root The root to replace this node with.
     */
    void replace( N root ) ;
    
    /** Can the children of a node be rearranged.
     * <p> The new order is such that after the permutation
     * the first child is the one initially indexed by <code>p[0]</code>
     * the second child is the one initially indexed by  <code>p[1]</code>
     * and so on.
     * <p><strong>Precondition:</strong></p>
     * <ul><li><code>p.length==getNumberOfChildren()</code></li>
     *     <li>Each number from 0 up to <code>getNumberOfChildren()</code>
     *         appears exactly once in p.</li>
     * </ul>
     * 
     * @param p
     * @return
     */
    boolean canPermuteChildren( int[] p ) ;
    
    /** Perform the permutation.
     * <p><strong>Precondition:</strong></p>
     * <ul><li><code>canPermuteChildren( p )</code></li>
     * </ul>
     * @param p
     */
    void permuteChildren( int[] p ) ;

		
	
	/** Exiting edges.
     * <p>
     * Exiting edges are all edges that have this node as a source.
     * <p>
     * This is for access only. Changing the collection does not change the model. */
    Collection<E> exitingEdges() ;
    
    /** Entering edges.
     * <p>
     * Entering edges are all edges that have this node as a target.
     * <p>
     * This is for access only. Changing the collection does not change the model. */
    Collection<E> enteringEdges() ;
    
    /** Return the union of the entering and exiting edges
     *  <p>
     * This is for access only. Changing the collection does not change the model.*/
    Collection<E> getIncidentEdges()  ;
    
    /** Get edges according to category.
     * <p>
     * This is for access only. Changing the collection does not change the model. */
    Collection<E> getEdges(Set<Edge.EdgeCategory> categories) ;
    
    /** Return a set of all edges that are INTERNAL, UP, or DOWN with respect to this node,
     * but that are not INTERNAL, UP, or DOWN with respect to any descendant of this node.
     * This will be all edges that are loops on a child of this node
     * or that are not loops and have an LCA that is this node. (The LCA of an
     * edge is the least common ancestor of its source and target, if there is one, and is null
     * otherwise.)
     * <p>
     * This is for access only. Changing the collection does not change the model.
     * @see higraph.model.interfaces.Edge.EdgeCategory
     * @see higraph.model.interfaces.Higraph#getGovernedEdges() 
     * 
     * */
    Collection<E> getGovernedEdges() ;
    
    /** Get the category for node n with respect to this node,
     * as described at {@link higraph.model.interfaces.Node.NodeCategory}.
     *   
     * @see higraph.model.interfaces.Node.NodeCategory
     * @param n a node, not null.
     * @return the category of n with respect to this node.
     */
    Node.NodeCategory categorize( N n ) ;
    
    /** Get the category for an edge with respect to this node.
     * The categories are based on the source and target nodes of the edge as 
     * described at {@link higraph.model.interfaces.Edge.EdgeCategory
     * 
     * @see higraph.model.interfaces.Edge.EdgeCategory
     * @param e an edge, not null
     * @return the appropriate category.
     */
    Edge.EdgeCategory categorize( E e ) ;
    
    /** The least common ancestor.
     * 
     * @param node
     * @return null if there is not least common ancestor in the WholeGraph, otherwise the least common ancestor.
     */
    N leastCommonAncestor( N node ) ;

}
