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

/**An interface for a top level hierarchical graph.
 * <p> A top level higraph has the property that each
 * of its topNodes is a root node.
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
public interface WholeGraph
    < NP extends Payload<NP>,
    EP extends Payload<EP>,
    HG extends Higraph<NP,EP,HG,WG,SG,N,E>,
    WG extends WholeGraph<NP,EP,HG,WG,SG,N,E>,
    SG extends Subgraph<NP,EP,HG,WG,SG,N,E>,
    N extends Node<NP,EP,HG,WG,SG,N,E>, 
    E extends Edge<NP,EP,HG,WG,SG,N,E> >
extends Higraph<NP,EP,HG,WG,SG,N,E>
{
    /** Make a root node.
     * <p>
     * The new node has no parent and a default sequence of children.
     * Typically the default set of initial children is determined by the
     * payload's tag. Thus creating a node may entail creating a
     * bunch of other nodes as its children.  For example if the 
     * tree represents an html document, creating a node tagged
     * "html" may entail first creating "head" and "body" nodes
     * to be its children. 
     * 
     * <p>
     * In tagless implementations, the default set of children
     * will usually be empty.
     * 
     * @param payload
     * @return
     */
    N makeRootNode( NP payload ) ;
	
	/** Can we make an edge from the source to the target?
     * In some implementations edges may be restricted.
     * For example in a StateChart application, it may be that
     * edges can not go to or from a part of an "and" node.
     * 
     * @param source
     * @param target
     * @param payload
     * @return
     */
    boolean canMakeEdge( N source, N target, EP payload ) ; 
	
    /** Make an edge from the given source to the given target
     * <p><strong>Precondition:</strong></p>
    * <ul><li><code> canMakeEdge( source, target ) </code></li>
    * </ul>
     *  
     * @param source
     * @param target
     * @param payload
     * @return
     */
    E makeEdge(N source, N target, EP payload ) ;
    
    /** Make a new SubGraph.
     * <p> The subgraph will initially contain no nodes and no edges.
     * 
     * @return the new SubGraph
     */
	SG makeSubGraph() ;
}
