/**
 * ArrowDirection.java
 * 
 * @date: Jun 30, 2011
 * @author: Xiaoyu Guo
 * This file is part of the Teaching Machine project.
 */
package visreed.awt;

/**
 * ArrowDirection provides an easy representation of directions.
 * @author Xiaoyu Guo
 */
public enum ArrowDirection {
    RIGHT(0),
    UP(270),
    LEFT(180),
    DOWN(90),
    
    EAST(0),
    NORTH(270),
    WEST(180),
    SOUTH(90);
    
    private double degree;
    private int numOfQuadrants;
    
    private ArrowDirection(double degree){
        this.degree = degree;
        this.numOfQuadrants = (int)(degree / 90);
    }
    
    /**
     * Gets the number of quadrants
     * @return
     */
    int getNumOfQuadrants(){
        return this.numOfQuadrants;
    }
    
    /**
     * Gets the degree form of the angle
     * @return
     */
    double getDegree(){
        return this.degree;
    }
    
    /**
     * Gets the radiant form of the angle
     * @return
     */
    double getRadian(){
        return Math.toRadians(this.degree);
    }
    
    /**
     * Gets the reversed direction
     * @return
     */
    public ArrowDirection getReverseDirection(){
        ArrowDirection result = RIGHT;
        if(this.degree == 0){
            result = LEFT;
        } else if (this.degree == 90){
            result = UP;
        } else if (this.degree == 270){
            result = DOWN;
        }
        return result;
    }
}
