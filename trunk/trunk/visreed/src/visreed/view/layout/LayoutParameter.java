/**
 * LayoutParameter.java
 * 
 * @date: Nov 2, 2011
 * @author: Xiaoyu Guo
 * This file is part of the Teaching Machine project.
 */
package visreed.view.layout;

import visreed.model.Direction;

/**
 * @author Xiaoyu Guo
 */
public class LayoutParameter implements IRotatable {
	
	public LayoutParameter(){
	}
	
	/* Handle padding */
	private Offset padding = new Offset();
	
	public void setPadding(Offset value){
		padding = value;
	}
	
	public Offset getPadding(){
		return padding;
	}
    
    /* Handling rotation */
    private Direction headingDirection = Direction.EAST;
    
    /* (non-Javadoc)
     * @see visreed.view.IRotatable#getCurrentDirection()
     */
    @Override
    public final Direction getCurrentDirection(){
        return this.headingDirection;
    }
    
    /* (non-Javadoc)
     * @see visreed.view.IRotatable#setDirection(visreed.model.Direction)
     */
    @Override
    public void setDirection(Direction newDirection){
        if(! newDirection.equals(this.headingDirection)){
            this.headingDirection = newDirection;
        }
    }
    
    /* (non-Javadoc)
     * @see visreed.view.IRotatable#reverseDirection()
     */
    @Override
    public final void reverseDirection(){
        this.headingDirection = this.headingDirection.getReverseDirection();
    }
}
