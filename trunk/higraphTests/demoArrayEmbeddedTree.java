/*
 * Created on Jun 16, 2011 by mpbl. 
 */

package demos;
/*#I
import tm.scripting.ScriptManager ;
import tm.scripting.PDV ;
 */
import java.lang.*;

public class EmbeddedArrayDemo {

	public static final int X0 = 20;     // Starting position for array displays
	public static final int Y0 = 20; 

	private int a = 3;
	private int b = 7;
	private int c = -1;
	private int d = 11;
	private int e = 13;
	private int f = -5;
    private int[] arrayC;
    private int[] arrayD;
    private int[] arrayE;
    private int[] arrayF;
    
 
    public EmbeddedArrayDemo(int s) {
    	if (s < 0) s = 0;
        arrayC = new int[s];
        arrayD = new int[s];
        arrayE = new int[s];
        arrayF = new int[s];
        randomize(arrayC, 0);
        randomize(arrayD, s);
        randomize(arrayE, 2*s);
        randomize(arrayF, 3*s);/*#TS*/makeTree();/*#/TS*/
   } 
    
    public void randomize(int[] array, int s){
    	for (int i = 0; i < array.length; i++)
    		array[i] = (17547 *(i + s))%101;//(int)(100.5*MATH.random()); // This is not strictly correct 
     }/*#TS*/
    
    /****** Visualization routines ***********/
    
    /*********** Primitive routines ******/  
     public void makeTree(){
    	ScriptManager.relay("HigraphManager","makeNode", a);
    	ScriptManager.relay("HigraphManager","makeNode", b);
     	ScriptManager.relay("HigraphManager","makeNode", c);
        ScriptManager.relay("HigraphManager","addChild", a, c);
    	ScriptManager.relay("HigraphManager","makeNode", d);
        ScriptManager.relay("HigraphManager","addChild", b, d);
    	ScriptManager.relay("HigraphManager","makeNode", e);
        ScriptManager.relay("HigraphManager","addChild", b, e);
    	ScriptManager.relay("HigraphManager","makeNode", f);
        ScriptManager.relay("HigraphManager","addChild", d, f);
    	PDV.makeArray(arrayC,"arrayC");
        ScriptManager.relay("HigraphManager","addChild", c, arrayC, true);
        PDV.makeArray(arrayD,"arrayD");
        ScriptManager.relay("HigraphManager","addChild", d, arrayD, true);
        PDV.makeArray(arrayE,"arrayE");
        ScriptManager.relay("HigraphManager","addChild", e, arrayE, true);
        PDV.makeArray(arrayF,"arrayF");
        ScriptManager.relay("HigraphManager","addChild", f, arrayF, true);
   	
    }
          
     /********** special compound routines for this visualization ******/
      
    public static void setup(){
        ScriptManager.relay("HigraphManager", "makeView", "mainView", "wholeGraph","Higraph.PDV", "SimpleTree");
        ScriptManager.relay("HigraphManager", "setTitle", "mainView", "PDV of a Forest of Arrays");
        ScriptManager.relay("HigraphManager", "setDefaultNodeShape", PDV.ELLIPSE);
        ScriptManager.relay("HigraphManager", "setDefaultNodeFillColor", PDV.BLUE);
        ScriptManager.relay("HigraphManager", "setDefaultNodeValueShow", true, PDV.CENTER);
        ScriptManager.relay("HigraphManager", "setDefaultNodeValueColor", PDV.WHITE);
        ScriptManager.relay("HigraphManager", "setDefaultNodeNameShow", true, PDV.EAST);
        ScriptManager.relay("HigraphManager", "setDefaultNodeNameColor", PDV.BLUE);
    }/*#/TS*/
    
    
    public static void main(String[] str) {
    	/*#TS*/setup();/*#/TS*/
    	EmbeddedArrayDemo a = new EmbeddedArrayDemo(4);
        ScriptManager.relay("HigraphManager", "dislocate", a.e, 0, 20);
        ScriptManager.relay("HigraphManager", "dislocate", a.e, 0, 0);
   	
        for (int i = 0; i < 4; i++)
        	;
    }
}
