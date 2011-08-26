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

package tm.languageInterface;

import tm.interfaces.SourceCoords;
import tm.virtualMachine.VMState;

public interface NodeInterface {
    void step(VMState vms) ;

    void select(VMState vms) ;

    /** Is the node's evaluation uninteresting?
      * When an uninteresting node as the selected
      * node on any level of the evaluation stack,
      * the TM does not typically stop.
      */
    boolean isUninteresting() ;

    /** Is the node worth stopping at?
     * The TM typically stops only when the
     * selected node is considered stop worthy.
     */
    boolean isStopWorthy() ;

    /** Return source coordinates associated with the node if any.
     * @return the coordinates associated with the node
     * @return null if no coordinates are associated with this node
     */
    SourceCoords getCoords() ;
}