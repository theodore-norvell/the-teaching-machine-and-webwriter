/**
 * IRotatable.java
 * 
 * @date: Aug 1, 2011
 * @author: Xiaoyu Guo
 * This file is part of the Teaching Machine project.
 */
package visreed.view.layout;

import visreed.model.Direction;

/**
 * @author Xiaoyu Guo
 *
 */
public interface IRotatable {
    
    /**
     * Gets the current direction
     * @return {@link visreed.model.Direction}
     */
    Direction getCurrentDirection();
    
    /**
     * Sets the new direction
     * @param newDirection {@link visreed.model.Direction}
     */
    void setDirection(Direction newDirection);
    
    // currently vertical-layouts are not supported
//    /**
//     * Rotates clockwise (by 90 degree)
//     */
//    void RotateClockwise();
//    
//    /**
//     * Rotates counter-clockwise (by 90 degree)
//     */
//    void RotateCounterClockwise();
    
    /**
     * Rotates by 180 degree.
     */
    void reverseDirection();
}
