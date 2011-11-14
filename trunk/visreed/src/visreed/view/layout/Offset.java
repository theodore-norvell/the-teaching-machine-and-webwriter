/**
 * Offset.java
 * 
 * @date: Nov 2, 2011
 * @author: Xiaoyu Guo
 * This file is part of the Teaching Machine project.
 */
package visreed.view.layout;

/**
 * @author Xiaoyu Guo
 */
public class Offset {

	/* Handle padding */
	private double top = 0;
	private double left = 0;
	private double right = 0;
	private double bottom = 0;
	
	public Offset(double top, double right, double bottom, double left){
		set(top, right, bottom, left);
	}
	
	public Offset(double top, double right, double bottom){
		set(top, right, bottom, right);
	}
	
	public Offset(double top, double right){
		set(top, right, top, right);
	}
	
	public Offset(double value){
		set(value, value, value, value);
	}
	
	public Offset(){
		set(0, 0, 0, 0);
	}
	
	public void set(double top, double right, double bottom, double left){
		this.top = top;
		this.left = left;
		this.bottom = bottom;
		this.left = left;
	}
	
	public void set(double top, double leftAndRight, double bottom){
		set(top, leftAndRight, bottom, leftAndRight);
	}
	
	public void set(double top, double right){
		set(top, right, top, right);
	}
	
	public void set(double value){
		set(value, value, value, value);
	}
	
	public double getTop(){
		return top;
	}
	
	public double getRight(){
		return right;
	}
	
	public double getBottom(){
		return bottom;
	}
	
	public double getLeft(){
		return left;
	}
}
