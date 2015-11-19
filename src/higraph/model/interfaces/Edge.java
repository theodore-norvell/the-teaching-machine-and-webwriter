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

import higraph.model.interfaces.Node.NodeCategory;

/**An interface for edges in hierarchical graphs.
 * <p>Edges carry labels (i.e. edge labels).
 * <p>Edges are directed and link a source node to a target node.
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
public interface Edge
    < NP extends Payload<NP>,
      EP extends Payload<EP>,
      HG extends Higraph<NP,EP,HG,WG,SG,N,E>,
      WG extends WholeGraph<NP,EP,HG,WG,SG,N,E>,
      SG extends Subgraph<NP,EP,HG,WG,SG,N,E>,
      N extends Node<NP,EP,HG,WG,SG,N,E>, 
      E extends Edge<NP,EP,HG,WG,SG,N,E> >
{
    /** The category of an edge e with respect to a node p depends on the 
     * categorization between e's source and target nodes with respect to p
     * as shown in the following table.
     * <table border="1">
     *    <tr> <th colspan="2" rowspan="2"></th><th colspan="4">Target:</th> </tr>
     *    <tr align="left">                     <th>SELF</th><th>DESC.</th>   <th>ANC.</th>    <th>OTHER</th> </tr>
     *    <tr> <th rowspan="4">Source:</th>
     *         <th align="right">SELF</th>      <td>LOOP</td><td>DOWN</td>    <td>OUT</td>     <td>OUT</td> </tr>
     *    <tr> <th align="right">DESCENDANT</th><td>UP</td>  <td>INTERNAL</td><td>DEEP_OUT</td><td>DEEP_OUT</td></tr>
     *    <tr> <th align="right">ANCESTOR</th>  <td>IN</td>  <td>DEEP_IN</td> <td>OTHER</td>   <td>OTHER</td></tr>
     *    <tr> <th align="right">OTHER</th>     <td>IN</td>  <td>DEEP_IN</td> <td>OTHER</td>   <td>OTHER</td></tr>
     *      
     * </table>
     * 
     * @see higraph.model.interfaces.Node.NodeCategory
     */
    public enum EdgeCategory{ LOOP, IN, OUT, UP, DOWN, INTERNAL, DEEP_IN, DEEP_OUT, OTHER }

    /** The source node for this edge. */
    N getSource() ;
    
    /** The target node for this edge. */
    N getTarget() ;
    
    /** The Payload of this edge. */
    EP getPayload() ;
    
    
    /** Get the WholeGraph which created this node.
     *  */
    WG getWholeGraph() ;
    
    /** Can this edge be deleted */
    boolean canDelete() ;
    
    /** Remove the edge from the graph.
     * <p>Once deleted, no further operations on the edge are allowed (other than
     * a query to <kbd>deleted()</kbd>).
     * <p><strong>Precondition:</strong></p>
     * <ul><li><code>canDelete() </code></li>
     * </ul>
     */
    void delete() ;
    
    /** Indicate whether the edge has been deleted. */
    boolean deleted() ;
    
    /** Can the payload be changed? */
    boolean canChangePayload( EP newPayload ) ;
    
    /** Change the payload.
     * <p><strong>Precondition:</strong></p>
     * <ul><li><code>canChangePayload( newPayload )</code></li>
     * </ul>
     * @param newPayload
     */
    void changePayload( EP newPayload ) ;
    
    boolean canChangeSource( N newSource ) ;
    
    boolean canChangeTarget( N newSource ) ;
    
    void changeSource( N newSource ) ;
    
    void changeTarget( N newTarget ) ;
    
    /** Return the node that governs this edge, if there is one. Otherwise return null */
    N getGoverningNode() ;
    
    /** Return the node within a given Higraph that governs this edge, if there is one. Otherwise return null. */
    N getGoverningNode(HG hg) ;

}
