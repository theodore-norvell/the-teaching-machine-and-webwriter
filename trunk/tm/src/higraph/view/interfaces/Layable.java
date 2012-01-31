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


/**
 * <p>Layable {@link higraph.view.ComponentView
 * view components} are designed to be laid out by a {@link
 * higraph.view.layout.SgLayoutManager
 * layout manager}. The layout process computes values for the next location
 * and size of layable view components. In some cases (
 * {@link higraph.view.EdgeView edges} in particular
 * but possibly {@link higraph.view.BranchView branches}
 * as well) the {@link higraph.view.layout.SgLayoutManager
 * layout manager} may also have to compute the entire new {@link java.awt.Shape shape}
 * of a viewable component.</p>
 * 
 * <p>In essence, then, layable objects permit the setting of their
 * {@link java.awt.Shape nextShape} and the querying of the current
 * {@link java.awt.Shape nextShape}. Information on their current
 * {@link java.awt.Shape Shape} is not directly available. This is because their Shape object
 * may not exist at all (for example, when a Node has been newly created).</p>
 * 
 *  <p>This works because a postCondition of a doTranslation routine is that
 *  the {@link java.awt.Shape nextShape} and {@link java.awt.Shape Shape}
 *  objects of any affected {@link
 *  higraph.view.ComponentView Component Views} are
 *  equal. Thus the normal sequence of events is<ol>
 *    <li>do a layout to compute {@link java.awt.Shape next shapes}</li>
 *    <li>do a transition to the new state</li>
 *    <li>repeat.</li></ol></p>
 * 
 * <p>In the event that a {@link
 * higraph.view.layout.SgLayoutManager
 * layout manager} were to use the existing values of
 * {@link java.awt.Shape nextShape} to calculate the new values, it
 * is possible a missed doTransition would simply result in accumulated
 * layouts. However, many managers will just layout components anew without
 * regard to how they were laid out the last time, in which case
 * doTranslation will just put into effect the last
 * {@link
 * higraph.view.layout.SgLayoutManager#layoutLocal()
 * layoutLocal()} call.</p>
 * 
 * @author mpbl
 *
 */


public interface Layable {
	public boolean isPinned();
	/**
	 * One of a set of convenience methods which always return the corresponding co-ordinate of nextBaseExtent
	 * including or ignoring labels and other zones as specified by HigraphView.
	 * @return nextBaseExtent().minX()
	 */
	public double getNextX();  // left side

	/**
	 * One of a set of convenience methods which always return the corresponding co-ordinate of nextBaseExtent
	 * including or ignoring labels and other zones as specified by HigraphView.
	 * @return nextBaseExtent().minY()
	 */
	public double getNextY();  // top side

	/**
	 * One of a set of convenience methods which always return the corresponding co-ordinate of nextBaseExtent
	 * including or ignoring labels and other zones as specified by HigraphView.
	 * @return nextBaseExtent().CenterX()
	 */
	public double getNextCenterX();

	/**
	 * One of a set of convenience methods which always return the corresponding co-ordinate of nextBaseExtent
	 * including or ignoring labels and other zones as specified by HigraphView.
	 * @return nextBaseExtent().CenterY()
	 */
	public double getNextCenterY();

	/**
	 * One of a set of convenience methods which always return the corresponding co-ordinate of nextBaseExtent
	 * including or ignoring labels and other zones as specified by HigraphView.
	 * @return nextBaseExtent().Width()
	 */
	public double getNextWidth();

	/**
		 * One of a set of convenience methods which always return the corresponding co-ordinate of nextBaseExtent
	 * including or ignoring labels and other zones as specified by HigraphView.
	 * @return nextBaseExtent().Height()
	 */
	public double getNextHeight();
	
	/**
	 * translate the layable item (but not its children if it has any). For translation of nodes with children
	 * see {@link higraph.view.NodeView.translateNextHierarchy(double, double)
	 * @param dx translation along the x axis
	 * @param dy translation along the y axis
	 */

	public void translateNext(double dx, double dy);
	
	/**
	 * a convenience method, equivalent to translateNext(x - getNextX(), y - getNextY())
	 * placeNext(0,0) will place the left edge of the nextShape at (0,0) which means it
	 * will be displayed at the next doTransition (if no other places or translations
	 * are done first). 
	 * 
	 * @param x the x co-ordinate of the next location
	 * @param y the y co-ordinate of the next location
	 */
	public void placeNext(double x, double y);
	
	/**
	 *  Note that the next state is used for laying out while the current state is used for drawing.
	 *  Thus layable is only concerned with the next state except for this method.
	 *  
	 *  At the end of the transition (which may take time so as to produce transition effects)
	 *  the next state is used to update the current state. Immediately after the transition, the next
	 *  state and the current state are the same, unless the layable object has been destructed.
	 *  
	 */
	
	public void doTransition();

}
