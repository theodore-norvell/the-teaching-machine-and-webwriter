/**
 * Direction.java
 * 
 * @date: Aug 1, 2011
 * @author: Xiaoyu Guo
 * This file is part of the Teaching Machine project.
 */
package regex.model;

/**
 * @author Xiaoyu Guo
 *
 */
public class Direction {
    public static final Direction EAST = new Direction(0);
//    NORTH(270),
    public static final Direction WEST = new Direction(180);
//    SOUTH(90),
    
    private int degree;
    
    private Direction(int degree){
        this.degree = degree;
    }
    
    // currently vertical directions are not supported
    /*
    public Direction RotateClockwise(){
        // TODO
        return null;
    }
    
    public Direction RotateCounterClockwize(){
        // TODO
        return null;
    }
    
    */
    
    /**
     * Gets the reverse direction.
     * e.g. EAST => WEST, NORTH => SOUTH, etc.
     * @return
     */
    public Direction getReverseDirection(){
        if(this.degree == EAST.degree){
            return WEST;
        } else if(this.degree == WEST.degree){
            return EAST;
        }
        return null;
    }
    
    public boolean equals(Direction b){
        return this.degree == b.degree;
    }
}
