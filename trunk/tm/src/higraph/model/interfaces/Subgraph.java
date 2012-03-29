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

/**An interface for a hierarchical subgraph.
 * <p> A subgraph is a higraph that is not a wholeGraph. A subgraph
 * represents a subset of a particular wholeGraph. That is
 * every node in a subgraph is also a node of its wholeGraph
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
public interface Subgraph
    < NP extends Payload<NP>,
    EP extends Payload<EP>,
    HG extends Higraph<NP,EP,HG,WG,SG,N,E>,
    WG extends WholeGraph<NP,EP,HG,WG,SG,N,E>,
    SG extends Subgraph<NP,EP,HG,WG,SG,N,E>,
    N extends Node<NP,EP,HG,WG,SG,N,E>, 
    E extends Edge<NP,EP,HG,WG,SG,N,E> >
extends Higraph<NP,EP,HG,WG,SG,N,E>
{
	
	
	/** Add v to the list of this subgraph's top nodes.
	 * <p>If v is already a top node, nothing is done.
	 * <p>If v is a descendant of any top node, nothing is done.
	 * <p>If v is an ancestor of one or more top nodes u, all such
	 * u are implicitly removed as a top node before v is made a top node.
	 * 
	 * @param v a non-deleted node of the associated wholeGraph
	 */
	void addTop(N v);
	
	/** Remove a top node from this subgraph.
	 * <p>If v is not already a top node, nothing is done.
	 * @param v
	 */
	void removeTop(N v);
}
