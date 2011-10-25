/**
 * JavaCCViewFactory.java
 * 
 * @date: Aug 29, 2011
 * @author: Xiaoyu Guo
 * This file is part of the Teaching Machine project.
 */
package visreed.extension.javaCC.view;

import tm.backtrack.BTTimeManager;
import visreed.view.IGraphContainer;
import visreed.view.VisreedDropZone;
import visreed.view.VisreedNodeView;
import visreed.view.VisreedViewFactory;

/**
 * @author Xiaoyu Guo
 *
 */
public class JavaCCViewFactory extends VisreedViewFactory {
	
    /**
     * @param tm
     */
    public JavaCCViewFactory(BTTimeManager tm, IGraphContainer gt) {
        super(tm, gt);
        
    }

    public VisreedDropZone makeGoToDefinitionDropZone(VisreedNodeView nv){
    	return new GoToDefinitionDropZone(nv, timeMan, graphContainer);
    }
}
