/*
 * Created on Jun 16, 2011 by Theodore S. Norvell. 

 */

package demos;

import java.lang.*;
/*#I
import tm.scripting.ScriptManager ;
import tm.scripting.PDV ;
 */

public class Forest {

	public static final int X0 = 20;     // Starting position for array displays
	public static final int Y0 = 20; 
	
	public static final int NODES = 17;
	public static final String[] names = {"top1", "top2", "top3", "n1", "n2", "n3", "n4",
                                     "leaf1", "leaf2", "leaf3", "leaf4", "leaf5", "leaf6", "leaf7", "leaf8", "leaf9", "leaf10"};
	public static final int[] positions = {PDV.WEST, PDV.WEST, PDV.EAST, PDV.WEST, PDV.WEST, PDV.EAST, PDV.WEST,
		PDV.SOUTH, PDV.SOUTH, PDV.SOUTH, PDV.WEST, PDV.SOUTH, PDV.SOUTH, PDV.SOUTH, PDV.SOUTH, PDV.SOUTH, PDV.SOUTH};

	/*#/TS*/   
    // Represents; a sequence of integers s
    // Invariant: a != null and a.length > 0 
    // Invariant: a.length/4 <= size and size <= a.length
    // Representation condition: s.size = size and for all i : {0,..size}. s[i]==a[i].
    private int[] n;
 
    public Forest() {
        n = new int[NODES];
        randomize(n);/*#TS*/makeTops(n, 3); makeMids(n, 3, 4); makeLeaves(n, 7); hookUpNodes(); hookUpLeaves();/*#/TS*/
//        /*#TS*/makeSomeEdges(); /*#/TS*/
   } 
    
    public void randomize(int[] array){
    	for (int i = 0; i < array.length; i++)
    		array[i] = (17547 *i )%101;//(int)(100.5*MATH.random()); // This is not strictly correct 
     }/*#TS*/
    
    /****** Visualization routines ***********/
    
    /*********** Primitive routines ******/
    
    public void makeNodes(int[] array, int s, int n, int color){
    	for (int i = s; i <  s + n; i++){
            ScriptManager.relay("HigraphManager","makeNode", array[i]);
            ScriptManager.relay("HigraphManager","setNodeFillColor", array[i], color);
            ScriptManager.relay("HigraphManager","createNodeExtraLabel", array[i], "myName", positions[i]);
            ScriptManager.relay("HigraphManager","setNodeExtraLabel", array[i], "myName", names[i]);
     	}
   }
       
    public void placeArray(int[] array, int x, int y){
    	int xp = x;
    	for (int i = 0; i < array.length; i++){
            ScriptManager.relay("HigraphManager","placeNode", array[i], xp, y);
            xp = xp + 30;
    	}
   }
    
   public void colorArray(int[] array, int color){
    	for (int i = 0; i < array.length; i++){
            ScriptManager.relay("HigraphManager","setNodeFillColor", array[i], color);
    	}
   }

    
    public void deleteNodes(int[] array){
    	for (int i = 0; i < array.length; i++)
          ScriptManager.relay("HigraphManager","deleteNode", array[i]);
    }
    
    /********** special compound routines for this visualization ******/
    
    public void makeTops(int[] array, int n){
    	makeNodes(array, 0, n, PDV.BLUE);
    }
    
    public void makeMids(int[] array, int s, int n){
    	makeNodes(array, s, n, PDV.GREEN);
    }

    public void makeLeaves(int[] array, int s){
    	makeNodes(array, s, NODES-s, PDV.RED);
    }
    
    public void hookUpNodes(){
    	ScriptManager.relay("HigraphManager","addChild", n[0], n[3]);
    	ScriptManager.relay("HigraphManager","addChild", n[1], n[4]);
    	ScriptManager.relay("HigraphManager","addChild", n[2], n[5]);
    	ScriptManager.relay("HigraphManager","addChild", n[5], n[6]);
    }
    
    public void hookUpLeaves(){
    	ScriptManager.relay("HigraphManager","addChild", n[3], n[7]);
    	ScriptManager.relay("HigraphManager","addChild", n[3], n[8]);
    	ScriptManager.relay("HigraphManager","addChild", n[3], n[9]);
    	ScriptManager.relay("HigraphManager","addChild", n[0], n[10]);
    	ScriptManager.relay("HigraphManager","addChild", n[4], n[11]);
    	ScriptManager.relay("HigraphManager","addChild", n[4], n[12]);
     	ScriptManager.relay("HigraphManager","addChild", n[2], n[13]);
    	ScriptManager.relay("HigraphManager","addChild", n[6], n[14]);
    	ScriptManager.relay("HigraphManager","addChild", n[6], n[15]);
    	ScriptManager.relay("HigraphManager","addChild", n[6], n[16]);
   }
    
    public void makeSomeEdges(){
        ScriptManager.relay("HigraphManager", "setDefaultEdgeColor", PDV.BLUE);
   	    ScriptManager.relay("HigraphManager","makeEdge", n[5], n[4]);
    	ScriptManager.relay("HigraphManager","makeEdge", n[9], n[10]);
    	ScriptManager.relay("HigraphManager","setDefaultTargetDecorator", PDV.ARROWHEAD);
    	ScriptManager.relay("HigraphManager","setDefaultSourceDecorator", PDV.CIRCLE);
    	ScriptManager.relay("HigraphManager","makeEdge", n[10], n[2]);
    	ScriptManager.relay("HigraphManager","makeEdge", n[15], n[15]);
    	ScriptManager.relay("HigraphManager","makeEdge", n[6], n[6]);
    	ScriptManager.relay("HigraphManager","makeEdge", n[12], n[2]);
    }
    
    
    public static void setup(){
        ScriptManager.relay("HigraphManager", "makeView", "mainView", "wholeGraph","Higraph.PDV", "SimpleTree");
        ScriptManager.relay("HigraphManager", "setTitle", "mainView", "A Simple Forest");
        ScriptManager.relay("HigraphManager", "setDefaultNodeValueShow", true, PDV.CENTER);
        ScriptManager.relay("HigraphManager", "setDefaultNodeValueColor", PDV.WHITE);
        ScriptManager.relay("HigraphManager", "setDefaultNodeShape",PDV.ELLIPSE); 	
    }/*#/TS*/
    
    
    public static void main(String[] str) {
    	/*#TS*/setup();/*#/TS*/
        Forest f = new Forest();
        ScriptManager.relay("HigraphManager", "dislocate", f.n[1], 0, 20);
        ScriptManager.relay("HigraphManager", "dislocate", f.n[2], -20, 40);
        ScriptManager.relay("HigraphManager", "dislocate", f.n[1], 0, 0);
        ScriptManager.relay("HigraphManager", "dislocate", f.n[2], 0, 0);
        ScriptManager.relay("HigraphManager", "dislocate", f.n[5], 0, -20);
        ScriptManager.relay("HigraphManager", "dislocate", f.n[4], 20, 0);
        ScriptManager.relay("HigraphManager", "dislocate", f.n[5], 0, 0);
        ScriptManager.relay("HigraphManager", "dislocate", f.n[4], 0, 0);
        ScriptManager.relay("HigraphManager", "dislocate", f.n[12], 60, -140);
        ScriptManager.relay("HigraphManager", "dislocate", f.n[12], 0, 0);
    }
}
