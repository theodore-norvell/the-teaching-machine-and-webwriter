/*
 * Created on Jun 16, 2011 by mpbl. 
 */

package demos;

import java.lang.*;
/*#I
import tm.scripting.ScriptManager ;
import tm.scripting.PDV ;
 */

public class Array {
	public static final int X0 = 20;     // Starting position for array displays
	public static final int Y0 = 20; 
	
	/*#/TS*/   
    // Represents; a sequence of integers s
    // Invariant: a != null and a.length > 0 
    // Invariant: a.length/4 <= size and size <= a.length
    // Representation condition: s.size = size and for all i : {0,..size}. s[i]==a[i].
    private int[] array;
 
    public Array(int s) {
    	if (s < 0) s = 0;
        array = new int[s];
        randomize();/*#TS*/makeNodes(PDV.BLUE);placeArray(X0,Y0);/*#/TS*/
   } 
    
    public void randomize(){
    	for (int i = 0; i < array.length; i++)
    		array[i] = (17547 *i )%101;//(int)(100.5*MATH.random()); // This is not strictly correct 
     }/*#TS*/
    
    /****** Visualization routines ***********/
    
    /*********** Primitive routines ******/
    
    public void makeNodes(int color){
    	for (int i = 0; i <  array.length; i++){
            ScriptManager.relay("HigraphManager","makeNode", array[i]);
            ScriptManager.relay("HigraphManager","setNodeFillColor", array[i], color);
     	}
   }
       
    public void placeArray(int x, int y){
    	int xp = x;
    	for (int i = 0; i < array.length; i++){
            ScriptManager.relay("HigraphManager","placeNode", array[i], xp, y);
            xp = xp + 30;
    	}
   }
    
   public void colorArray(int color){
    	for (int i = 0; i < array.length; i++){
            ScriptManager.relay("HigraphManager","setNodeFillColor", array[i], color);
    	}
   }

    
    public void deleteNodes(){
    	for (int i = 0; i < array.length; i++)
          ScriptManager.relay("HigraphManager","deleteNode", array[i]);
    }
    
    /********** special compound routines for this visualization ******/
      
    public static void setup(){
        ScriptManager.relay("HigraphManager", "makeView", "mainView", "wholeGraph","Higraph.PDV", "PlacedNode");
        ScriptManager.relay("HigraphManager", "setTitle", "mainView", "PDV of an Array");
        ScriptManager.relay("HigraphManager", "setDefaultNodeValueShow", true, PDV.CENTER);
        ScriptManager.relay("HigraphManager", "setDefaultNodeValueColor", PDV.WHITE);
        ScriptManager.relay("HigraphManager", "setDefaultNodeShape",PDV.RECTANGLE); 	
    }/*#/TS*/
    
    
    public static void main(String[] str) {
    	/*#TS*/setup();/*#/TS*/
        Array a = new Array(10);
        ScriptManager.relay("HigraphManager", "dislocate", a.array[1], 0, 20);
        ScriptManager.relay("HigraphManager", "dislocate", a.array[4], 0, -20);
        ScriptManager.relay("HigraphManager", "dislocate", a.array[6], 20, 0);
        ScriptManager.relay("HigraphManager", "dislocate", a.array[1], 0, 0);
        ScriptManager.relay("HigraphManager", "dislocate", a.array[4], 0, 0);
        ScriptManager.relay("HigraphManager", "dislocate", a.array[6], 0, 0);
    }
}
