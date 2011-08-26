package tm.displayEngine.tmHigraph;

import tm.interfaces.Datum;

public interface HigraphVisualizerInterface {
	
	/** <p>Make a view of the specified higraph tied to the specified TM visualizerPlugIn using
	 * the default layout manager associated with that visualizerPlugIn.
	 * As a convenience the tm automatically builds the wholeGraph
	 * but it must still be tied to a visualizer, using the reserved
	 * id "wholeGraph".</p>
	 * <p> Note that multiple visualizers may be tied to a single higraph</p>
	 * 
	 * @param id unique higraph id either for a subgraph as specified in
	 *         makeSubgraph(id) or the wholeGraph using reserved id "wholeGraph"
	 *       <b>pre:</b> id must exist 
	 *  @param visualizerPlugIn unique id for the TM visualizerPlugIn
	 *        <b>pre:</b> plugIn must exist and be available through TM plugIn manager
	 */
	
	public void makeView(String viewId, String hgId, String visualizerPlugIn);

	
	/** <p>Make a view of the specified higraph tied to the specified TM visualizerPlugIn using
	 * the layout manager specified.
	 * As a convenience the tm automatically builds the wholeGraph
	 * but it must still be tied to a visualizer, using the reserved
	 * id "wholeGraph".</p>
	 * <p> Note that multiple views may be tied to a single higraph</p>
	 * <p> Newly created views are added to the ActiveViewSet</p>
	 * 
	 * @param viewId - a unique id given to the new view, used to reference it after its creation
	 * 
	 * @param hgId unique higraph id either for a subgraph as specified in
	 *         makeSubgraph(id) or the wholeGraph using reserved id "wholeGraph"
	 *       <b>pre:</b> hgId must exist 
	 *  @param visualizerPlugIn unique id for the TM visualizerPlugIn
	 *        <b>pre:</b> plugIn must exist and be available through TM plugIn manager
	 *  @param layoutManager unique id for the Higraph layoutManager
	 *        <b>pre:</b> plugIn must exist and be available through TM plugIn manager
	 */
	public void makeView(String viewId, String hgId, String visualizerPlugIn, String layoutPlugIn);

	/** <p>Set the title in the titleBar of the Visualizer's TM SubWindow </p>
	 * 
	 * @param viewId the name of the view, which must already have been assigned
	 * @param title the title to appear in the window bar.
	 */
	public void setTitle(String viewId, String title);
	
	/** <p>Set a default layoutManager for a visualizer. Layout
	 * manager will apply to top nodes and all their descendents that
	 * don't have their own layout managers specified</p>
	 * 
	 *  @param layoutManagerPlugIn unique id for the TM layoutManagerPlugIn
	 *        <b>pre:</b> plugIn must exist and be available through TM plugIn manager
	 *  @param visualizerPlugIn unique id for the TM visualizerPlugIn
	 *        <b>pre:</b> plugIn must exist and be available through TM plugIn manager
	 */
	public void setLayoutManager(String layoutManagerPlugIn, String visualizerPlugIn);		
	
	
	/**
	 * Add the specified view to the active view set. View commands
	 * are relayed to all views in the active view set
	 * 
	 * @param viewId - the name of the view to be added to the activeViewSet
	 */
	
	public void addToActiveViewSet(String viewId);
	
	
	
	/**
	 * Remove the specified view from the active view set. View commands
	 * will no longer be relayed to this view.
	 * 
	 * @param visualizerPlugIn - the name of the plugIn to be removed from the set
	 */

	public void removeFromActiveViewSet(String viewId);
	
	/**
	 * Remove all views from the active view set. View commands
	 * will not be relayed to any views 
	 *  
	 * @param visualizerPlugIn - the name of the plugIn to be removed from the set
	 */

	public void clearActiveViewSet( );
}
