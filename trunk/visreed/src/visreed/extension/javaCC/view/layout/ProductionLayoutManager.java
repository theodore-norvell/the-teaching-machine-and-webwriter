/**
 * ProductionLayoutManager.java
 * 
 * @date: Oct 3, 2011
 * @author: Xiaoyu Guo
 * This file is part of the Teaching Machine project.
 */
package visreed.extension.javaCC.view.layout;

import visreed.view.VisreedNodeView;
import visreed.view.layout.VisreedNodeLayoutManager;

/**
 * @author Xiaoyu Guo
 */
public class ProductionLayoutManager extends VisreedNodeLayoutManager {

	protected ProductionLayoutManager() {
		super();
	}
	
    private static final ProductionLayoutManager instance = new ProductionLayoutManager();
    public static ProductionLayoutManager getInstance(){
        return instance;
    }

	/* (non-Javadoc)
	 * @see visreed.view.layout.VisreedNodeLayoutManager#layoutNode(visreed.view.VisreedNodeView, double, double)
	 */
	@Override
	public void layoutNode(VisreedNodeView nv, double px, double py) {
		// TODO Auto-generated method stub
	}

}
