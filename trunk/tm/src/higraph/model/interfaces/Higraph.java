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
 * <p> A higraph is simply a forest (set of trees) with
 * edges between the nodes.
 * <p> Higraphs are either WholeGraphs or Subgraphs.
 * 
 * @author Theodore S. Norvell & Michael Bruce-Lockhart
 *
 * @param <NP> The node payload type. Each node carries a payload.
 * @param <EP> The edge payload type. Each edge carries a payload.
 * @param <HG> The higraph type. This is an interface that both WG and SG should extend.
 * @param <WG> The wholeGraph type.
 * @param <SG> The subgraph type. 
 * @param <N> The node type.
 * @param <E> The edge type.
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
     * @return true if the node is in the higraph.
     */
    boolean contains(N node) ;
    
    /** Is an edge contained in a higraph.
     * @return true if the edge is in the higraph.
     */
    boolean contains(E edge) ;
    
   /** Return a set of all edges whose end-points are both in this subgraph,
     * but that are not INTERNAL, UP, or DOWN with respect to any node in this subgraph.
     * This is all edges that are either loops on a top node or that
     * or that are between trees defined by two different top nodes.  Equivalently:
     * this is all edges that are either loops on a top node, or are between
     * top nodes that are in this higraph but that either have no common ancestor,
     * or that have a common ancestor that is not in this higraph.
     * @see higraph.model.interfaces.Edge.EdgeCategory
     * */
    Collection<E> getGovernedEdges() ;


}
