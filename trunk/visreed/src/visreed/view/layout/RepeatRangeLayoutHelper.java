/**
 * RepeatRangeLayoutHelper.java
 * 
 * @date: Oct 11, 2011
 * @author: Xiaoyu Guo
 * This file is part of the Teaching Machine project.
 */
package visreed.view.layout;

import java.awt.geom.Rectangle2D;

import visreed.view.VisreedNodeView;

/**
 * RepeatRangeLayoutHelper just put the child in its center, from left to right.
 * @author Xiaoyu Guo
 */
public class RepeatRangeLayoutHelper extends VisreedNodeLayoutManager {
    protected RepeatRangeLayoutHelper() {
        super();
    }
    private static final double HSPACE_PIXEL = 20;
    private static final double VSPACE_PIXEL = 20;
    public static double getHSpacePixel(){
    	return HSPACE_PIXEL;
    }
    public static double getVSpacePixel(){
    	return VSPACE_PIXEL;
    }

	/* (non-Javadoc)
	 * @see visreed.view.layout.VisreedNodeLayoutManager#layoutNode(visreed.view.VisreedNodeView, double, double)
	 */
	@Override
	public void layoutNode(VisreedNodeView nv) {
        if(nv == null){
            return;
        }

        // TODO assert(nv.getNumChildren() == 1)
        VisreedNodeView kid = nv.getVisreedChild(0);
        
        double kidWidth = 0.0;
        double kidHeight = 0.0;
        if(kid != null){
            kid.doLayout();
            kidWidth = kid.getNextShapeExtent().getWidth();
            kidHeight = kid.getNextShapeExtent().getHeight();
        }
        
        nv.placeNext(0, 0);
        
        kid.placeNextHierarchy(HSPACE_PIXEL, VSPACE_PIXEL);
        
        // set the padding
        Rectangle2D myNextExtent = new Rectangle2D.Double(
            0, 
            0, 
            kidWidth + 2 * HSPACE_PIXEL, 
            kidHeight + VSPACE_PIXEL * 2
        );
        nv.setNextShape(myNextExtent);
	}

	private static final RepeatRangeLayoutHelper instance = new RepeatRangeLayoutHelper();
	public static RepeatRangeLayoutHelper getInstance(){
		return instance;
	}
}
