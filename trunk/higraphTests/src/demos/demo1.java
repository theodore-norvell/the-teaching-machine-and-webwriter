/*
 * Created on Jun 16, 2011 by Theodore S. Norvell. 

 */

package demos;

/*#TS*/import tm.scripting.ScriptManager;

public class Forest {

	// Colors
	public static final int WHITE = 0xffffff;
	public static final int BLACK = 0x000000;
	public static final int GREY = 0x808080;
	public static final int RED = 0xff0000;
	public static final int GREEN = 0x00ff00;
	public static final int BLUE = 0x0000ff;

	// String markers
	public static final int MARKER_RED = 0xffff;	
	public static final int MARKER_BLUE = 0xfffe;	
	public static final int MARKER_BLACK = 0xfffd;	
	public static final int MARKER_GREEN = 0xfffc;
	public static final int END_MARKER = 0xfff0;

	// predefined node shapes
	public static final int ELLIPSE = 0;
	public static final int RECTANGLE = 1;
	public static final int ROUND_RECTANGLE = 2;

	// Predefined zoneview positions, relative to component being labelled
	public static final int CENTER = 0;        // (CenterX, CenterY)
	public static final int EAST = 1;          // (MaxX, CenterY)
	public static final int NORTHEAST = 2;     // (MaxX, MinY)
	public static final int NORTH = 3;         // (CenterX, MinY)
	public static final int NORTHWEST = 4;     // (MinX, MinY)
	public static final int WEST = 5;          // (MinX, CenterY)
	public static final int SOUTHWEST = 6;     // (MinX, MaxY)
	public static final int SOUTH = 7;         // (CenterX, MaxY)
	public static final int SOUTHEAST = 8;     // (MaxX, MaxY)

	public static final int X0 = 20;     // Starting position for array displays
	public static final int Y0 = 20;  

	/*#/TS*/   
    // Represents; a sequence of integers s
    // Invariant: a != null and a.length > 0 
    // Invariant: a.length/4 <= size and size <= a.length
    // Representation condition: s.size = size and for all i : {0,..size}. s[i]==a[i].
    private int[] t;
    private int[] n;
    private int[] l;

    public Forest() {
        t = new int[3];  /*#TS*/makeNodes(t); colorArray(t, BLUE);/*#/TS*/
        n = new int[4];  /*#TS*/makeNodes(n); colorArray(n, GREEN);hookUpNodes();/*#/TS*/
        l = new int[10]; /*#TS*/makeNodes(l); colorArray(l, RED);hookUpLeaves();/*#/TS*/
       
    } 
    
    public static void randomize(int[] array){
    	for (int i = 0; i < array.length; i++)
    		array[i] = (int)(100*Math.random()); 
    }/*#TS*/
    
    /****** Visualization routines ***********/
    
    /*********** Primitive routines ******/
    
    public void makeNodes(int[] array){
    	for (int i = 0; i < array.length; i++){
            ScriptManager.relay("HigraphManager","makeNode", array[i]);
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
    
    public void hookUpNodes(){
    	ScriptManager.relay("HigraphManager","addChild", t[0], n[0]);
    	ScriptManager.relay("HigraphManager","addChild", t[1], n[1]);
    	ScriptManager.relay("HigraphManager","addChild", t[2], n[2]);
    	ScriptManager.relay("HigraphManager","addChild", n[2], n[3]);
    }
    
    public void hookUpLeaves(){
    	ScriptManager.relay("HigraphManager","addChild", n[0], l[0]);
    	ScriptManager.relay("HigraphManager","addChild", n[0], l[1]);
    	ScriptManager.relay("HigraphManager","addChild", n[0], l[2]);
    	ScriptManager.relay("HigraphManager","addChild", t[0], l[3]);
    	ScriptManager.relay("HigraphManager","addChild", n[1], l[4]);
    	ScriptManager.relay("HigraphManager","addChild", n[1], l[5]);
     	ScriptManager.relay("HigraphManager","addChild", t[2], l[6]);
    	ScriptManager.relay("HigraphManager","addChild", n[3], l[7]);
    	ScriptManager.relay("HigraphManager","addChild", n[3], l[8]);
    	ScriptManager.relay("HigraphManager","addChild", n[3], l[9]);
   }
    
    
    public static void setup(){
        ScriptManager.relay("HigraphManager", "makeView", "mainView", "wholeGraph","Higraph.PDV", "SimpleTree");
        ScriptManager.relay("HigraphManager", "setTitle", "mainView", "A Simple Forest");
        ScriptManager.relay("HigraphManager", "setDefaultNodeValueShow", true, CENTER);
        ScriptManager.relay("HigraphManager", "setDefaultNodeValueColor", WHITE);
        ScriptManager.relay("HigraphManager", "setDefaultNodeShape",ELLIPSE); 	
    }/*#/TS*/
    

    
    
    public static void main(String[] str) {
    	/*#TS*/setup();/*#/TS*/
        Forest f = new Forest();
        randomize(f.t);
        randomize(f.n);
        randomize(f.l);
       
    }
}
