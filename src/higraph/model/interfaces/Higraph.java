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

import java.util.Collection;
import java.util.List;

/**An interface for hierarchical graphs.
 * <p> A higraph consists of a set of trees and edges that run between nodes of those trees.
 * <p> There are three interfaces in JHigraph that extend Higraph.
 * <ul><li>WholeGraph: The trees are defined by the root (i.e. parentless) nodes of the WholeGraph.
 *     <li>Subgraph: A collection of nodes, none of which is an ancestor of another.
 *     The trees are defined by these nodes.
 *     <li>Node: Each Node can be seen as a Higraph in which the trees are defined by
 *     the node's children.
 * <ul>
 * 
 * @author Theodore S. Norvell & Michael Bruce-Lockhart
 *
 * @param <NP> The node payload type. Each node carries a payload.
 * @param <EP> The edge payload type. Each edge carries a payload.
 * @param <HG> The Higraph type. This is an interface that WG, SG, and N should extend.
 * @param <WG> The WholeGraph type.
 * @param <SG> The Subgraph type. 
 * @param <N> The Node type.
 * @param <E> The Edge type.
 */
public interface Higraph
    < NP extends Payload<NP>,
      EP extends Payload<EP>,
      HG extends Higraph<NP,EP,HG,WG,SG,N,E>,
      WG extends WholeGraph<NP,EP,HG,WG,SG,N,E>,
      SG extends Subgraph<NP,EP,HG,WG,SG,N,E>,
      N extends Node<NP,EP,HG,WG,SG,N,E>, 
      E extends Edge<NP,EP,HG,WG,SG,N,E> >
{
	/**
	 * A node is a top node iff it is in the higraph and is either a root
	 * (i.e. has no parent) or it has a parent, but the parent is not
	 * in the higraph.
	 * @return an ordered set of the top level nodes
	 */
	List<N> getTops();
	
	/** Is the node a top node */
	boolean isTop(N node) ;
	
	/** Get a top node.
	 * <p><strong>precondition</strong> 0 <= position && position < getNumberOfTops() 
	 * @param position
	 * @return
	 */
	N getTop(int position) ;
	
	/** How many top nodes are there. */
	int getNumberOfTops() ;
	
	/** Can the node be moved to the given position.
	 * <p> result implies 0 <= position && position < getNumberOfTops() && node.isTop()
	 * @param node
	 * @param position
	 * @return
	 */
	boolean canMoveTop(N node, int position) ;
	
	/** Can the node be moved to the given position.
	 * <p><strong>precondition</strong> canMoveTop(node, position)
	 * @param node
	 * @param position
	 * @return
	 */
	void moveTop(N node, int position) ;
	
	/** Get all nodes in the higraph.
	 * <p>
	 * A node is in a higraph iff it is a top node or a descendant of
	 * a top node.
	 * @return an ordered set of all nodes in the higraph.
	 */
	List<N> getNodes();
	
	/** Get all edges in the higraph.
	 * <p> An edge is in a higraph iff both its target and source and in the higraph.
	 * @return
	 */
	List<E> getEdges();
	
	/** Get the WholeGraph that this higraph is a part of.
	 * <p>If this object is a WholeGraph, then this object is
	 * the result. 
	 * 
	 * @return
	 */
	WG getWholeGraph() ;
    
    /** Is a node contained in a higraph.
     * <p>A node is considered to be contained by the higraph if it is a top node or a
     * descendant of a top node in the higraph.
     * @return true if the node is in the higraph.
     */
    boolean contains(N node) ;
    
    /** Is an edge contained in a higraph.
     * <p>A an edge is contained within a higraph exactly if both its end points are contained in
     * the higraph.
     * @return true if the edge is in the higraph.
     */
    boolean contains(E edge) ;
    
   /** Return a set of all edges whose end-points are both in this higraph,
     * but that are not INTERNAL, UP, or DOWN with respect to any node in this subgraph.
     * When this higraph is itself a node, we also include edges that 
     * are UP or DOWN with respect to this node.
     * 
     * This is all edges that 
     * <ul><li>are loops on a top node ,
     *     <li>connect trees defined by two different top nodes, or
     *     <li>connect this higraph with a node in this higraph (when the higraph is itself a node).
     * </ul> 
     * Note that the third category is not "contained in" the higraph as defined by
     *   higraph.model.interfaces.Higraph.contains.
     * @see higraph.model.interfaces.Edge.EdgeCategory
     * @see higraph.model.interfaces.Higraph#contains
     * @see higraph.model.interfaces.Node#getGovernedEdges 
     * */
    Collection<E> getGovernedEdges() ;


}
