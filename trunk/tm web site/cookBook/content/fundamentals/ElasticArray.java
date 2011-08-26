// Created on Jun 16, 2011 by Theodore S. Norvell. 

package elastic_array;
/*#TS*/
import tm.scripting.ScriptManager;
/*#/TS*/
public class ElasticArray {
/*#TS*//*#TA*/
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
	/*#/TA*/
	/*#/TS*/   
    // Represents; a sequence of integers s
    // Invariant: a != null and a.length > 0 
    // Invariant: a.length/4 <= size and size <= a.length
    // Representation condition: s.size = size and for all i : {0,..size}. s[i]==a[i].
    private int size ;
    private int[] a ;

    public ElasticArray() {
        size = 0 ;
        a = new int[1] ;  /*#TS*/makeNodes(a); colorArray(a, BLUE);  placeArray(a, X0, Y0);/*#/TS*/
    }    /*#TS*/
    
    /*#TB*//****** Visualization routines ***********/
    
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
    
    public void makeNewArray(int[] array){
        makeNodes(array);
        placeArray(array, X0, Y0 + 80);
        colorArray(array, RED);
    }
    
    public void replaceArray(int[] oldArray, int[] newArray){
        deleteNodes(oldArray);
        colorArray(newArray, BLUE);
        placeArray(newArray, X0, Y0);
    }/*#/TB*//*#/TS*/
    
    /*#I public static void setup(){
        ScriptManager.relay("HigraphManager", "makeView", "mainView", "wholeGraph","Higraph.PDV", "PlacedNode");
        ScriptManager.relay("HigraphManager", "setTitle", "mainView", "Higraph Visualization of ElasticArray");
        ScriptManager.relay("HigraphManager", "setDefaultNodeValueShow", true, CENTER);
        ScriptManager.relay("HigraphManager", "setDefaultNodeValueColor", WHITE);
        ScriptManager.relay("HigraphManager", "setDefaultNodeShape",RECTANGLE); 	
    }*/
    
    public int get(int i) {
        // assert 0 <= i && i < size ;
        return a[i] ; 
    }
    
    public void set(int i, int x) {
        // assert 0 <= i && i < size ;
        a[i] = x ; }
    
    public void grow() {
        if( size == a.length ) {
            int[] b = new int[ 2* a.length] ;/*#TS*/ makeNewArray(b);/*#/TS*/
            for(int i=0 ; i<a.length ; ++i )
                b[i] = a[i] ;/*#TS*/ replaceArray(a, b);/*#/TS*/
            a = b ; }
        a[size] = 0 ;
        ++size ;
    }
    
    public void shrink() {
        // assert size > 0 ;
        if( size-1 < a.length/4 ) {
            int[] b = new int[ a.length / 2 ] ;/*#TS*/ makeNewArray(b);/*#/TS*/
            for( int i=0; i < size-1 ; ++i )
                b[i] = a[i] ;/*#TS*/ replaceArray(a, b);/*#/TS*/         
         a = b ; }
        --size ;
    }
    
    public static void main(String[] str) {
    	/*#TS*/setup();/*#/TS*/
        ElasticArray e = new ElasticArray() ;
        int x = 12 ;
        for( int i = 0 ; i < 10 ; ++i) {
            e.grow();
            e.set(i, x) ;
            ++x; }
        
        for( int i = 0 ; i < 10 ; ++i ) {
            e.shrink() ; }
    }
}
