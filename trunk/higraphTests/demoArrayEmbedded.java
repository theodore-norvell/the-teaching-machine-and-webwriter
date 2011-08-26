/*
 * Created on Jun 16, 2011 by mpbl. 
 */

package demos;

import java.lang.*;
/*#I
import tm.scripting.ScriptManager ;
import tm.scripting.PDV ;
 */

public class EmbeddedArrayDemo {

	public static final int X0 = 20;     // Starting position for array displays
	public static final int Y0 = 20; 
	
    private int[] array;
 
    public EmbeddedArrayDemo(int s) {
    	if (s < 0) s = 0;
        array = new int[s];
        randomize();/*#TS*/PDV.makeArray(array, "myArray");/*#/TS*/
   } 
    
    public void randomize(){
    	for (int i = 0; i < array.length; i++)
    		array[i] = (17547 *i )%101;//(int)(100.5*MATH.random()); // This is not strictly correct 
     }/*#TS*/
    
     
    /********** special compound routines for this visualization ******/
      
    public static void setup(){
        ScriptManager.relay("HigraphManager", "makeView", "mainView", "wholeGraph","Higraph.PDV", "SimpleTree");
        ScriptManager.relay("HigraphManager", "setTitle", "mainView", "PDV of an Array");
        ScriptManager.relay("HigraphManager", "setDefaultNodeValueShow", true, PDV.CENTER);
        ScriptManager.relay("HigraphManager", "setDefaultNodeValueColor", PDV.WHITE);
        ScriptManager.relay("HigraphManager", "setDefaultNodeNameShow", true, PDV.SOUTH);
        ScriptManager.relay("HigraphManager", "setDefaultNodeNameColor", PDV.BLUE);
        ScriptManager.relay("HigraphManager", "setDefaultNodeFillColor",PDV.BLUE); 	
        ScriptManager.relay("HigraphManager", "setDefaultNodeShape",PDV.RECTANGLE); 	
    }/*#/TS*/
    
    
    public static void main(String[] str) {
    	/*#TS*/setup();/*#/TS*/
    	EmbeddedArrayDemo a = new EmbeddedArrayDemo(10);
        for (int i = 0; i < 4; i++)
        	;
    }
}
