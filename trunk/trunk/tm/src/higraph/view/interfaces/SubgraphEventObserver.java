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

package higraph.view.interfaces;

import java.awt.datatransfer.Transferable;
import java.awt.event.MouseEvent;
import java.util.Stack;

import javax.swing.Icon;
import javax.swing.TransferHandler;

import higraph.model.interfaces.*;
import higraph.view.ComponentView;

public abstract class SubgraphEventObserver
    < NP extends Payload<NP>,
      EP extends Payload<EP>,
      HG extends Higraph<NP,EP,HG,WG,SG,N,E>,
      WG extends WholeGraph<NP,EP,HG,WG,SG,N,E>,
      SG extends Subgraph<NP,EP,HG,WG,SG,N,E>,
      N extends Node<NP,EP,HG,WG,SG,N,E>, 
      E extends Edge<NP,EP,HG,WG,SG,N,E>
    >
{
    
    /** Called when the mouse is being moved with no buttons down. */
	public void movedOver(Stack<ComponentView<NP,EP,HG,WG,SG,N,E>> stack, MouseEvent e) {}
	
	/** Called when the mouse is being moved with one or more buttons down. */ 
	public void dragged(Stack<ComponentView<NP,EP,HG,WG,SG,N,E>> stack, MouseEvent e) {}

	/** Called when the mouse button has been pressed and released. */
	public void clickedOn(Stack<ComponentView<NP,EP,HG,WG,SG,N,E>> stack, MouseEvent e) {}
	
	
	/** Called when a mouse button goes down. */
	public void pressedOn(Stack<ComponentView<NP,EP,HG,WG,SG,N,E>> stack, MouseEvent e) {}
	
	/** Called when a mouse button goes up. */ 
	public void releasedOn(Stack<ComponentView<NP,EP,HG,WG,SG,N,E>> stack, MouseEvent e) {}
	
	/** What drag actions are allowed to be initiated. 
	 * 
	 * @see java.awt.dnd.DnDConstants
	 * 
	 * @return
	 */
	public int getSourceActions() {
	    return java.awt.dnd.DnDConstants.ACTION_NONE ;
	}
	/** Called when a drag-and-drop action starts over this HigraphView.
	 * <p> The return result should either be a pointer to a transferable
	 *  or null.
	 *  <p>When a drag starts, three events happen in sequence.
	 *  <ol><li>pressedOn -- the observer should take note of
	 *          the top most draggable component view on the stack -- if any.
	 *      <li>getSourceActions -- typically the observer will return
	 *                    ACTION_NONE if no draggable componentView was
	 *                    noted during the pressedOn, ACTION_MOVE_OR_COPY if
	 *                    the noted componentView can be moved or copied, and
	 *                    ACTION_COPY if the noted componentView can only be MOVED.
	 *      <li>createTransferable -- typically the observer will make a
	 *                    ViewTransferObject.
	 *  </ol> 
	 *  @return null if there is nothing under the mouse to drag.
	 */
	public Transferable createTransferable() {
	    return null ;
	}
	
	/** Called when the mouse is dragged over this subgraphView during a drag-and-drop action.
     * <p> The result is used (among other things) to affect the look of the cursor.
     *  @return true if the proposed action is possible.
     */
    public boolean canDropHere( Stack<ComponentView<NP,EP,HG,WG,SG,N,E>> stack,
	                    TransferHandler.TransferSupport supportObj ) {  
        return false ;
    }
    
    /** Called when a drag-and-drop action ends over this subgraphView.
     * Also called in response to a paste operation.
     * Do not assume that canDropHere has already been called, so
     * it is a good idea to check whether canDropHere is true before
     * going any further.
     * 
     * @param stack
     * @param support
     * @return true if the action is successful.
     */
    public boolean importData( Stack<ComponentView<NP,EP,HG,WG,SG,N,E>> stack,
            TransferHandler.TransferSupport support) {
        return false ;
    } 
    
    /** Return an icon to put next to the cursor.
     *  <p> There is no guarantee that this will be called or that if called it will have any effect.
     *  @return null to show nothing.
     */
    public Icon getVisualRepresentation(Transferable t) {
        return null ;
    }

}
