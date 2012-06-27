// Created on Jun 16, 2011 by Theodore S. Norvell. 

package elastic_array;
/*#I
import tm.scripting.ScriptManager ;
import tm.scripting.PDV ;
 */
public class ElasticArray {

	/*#I public static final int X0 = 20; 
	public static final int Y0 = 20; */ 
    // Represents; a sequence of integers s
    // Invariant: a != null and a.length > 0 
    // Invariant: a.length/4 <= size and size <= a.length
    // Representation condition: s.size = size and for all i : {0,..size}. s[i]==a[i].
    private int size ;
    private int[] a ;

    public ElasticArray() {
        size = 0 ;  /*#I ScriptManager.relay("HigraphManager","makeNode", size);
        				  ScriptManager.relay("HigraphManager","placeNode", size, X0, Y0);
        				  ScriptManager.relay("HigraphManager","setNodeNameShow", size, true);*/
        a = new int[1] ;  /*#I makeNodes(a); colorArray(a, PDV.BLUE);  placeArray(a, X0+70, Y0); */
    }    /*#TS*/
    
    /*#TB*//****** Visualization routines ***********/
    
    /*********** Primitive routines ******//*#/TS*/
/*#I    
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
    }*/ /*#T S*/
    
    /********** special compound routines for this visualization ******//*#/T S*/
/*#I    
    public void makeNewArray(int[] array){
        makeNodes(array);
        placeArray(array, X0+70, Y0 + 80);
        colorArray(array, PDV.RED);
    }
    
    public void replaceArray(int[] oldArray, int[] newArray){
        deleteNodes(oldArray);
        colorArray(newArray, PDV.BLUE);
        placeArray(newArray, X0+70, Y0);
    }*//*#/TB*/
    
    /*#T setup*/ /*#I public static void setup(){
        ScriptManager.relay("HigraphManager", "makeView", "mainView", "wholeGraph","Higraph.PDV", "PlacedNode");
        ScriptManager.relay("HigraphManager", "setTitle", "mainView", "Higraph Visualization of ElasticArray");
        ScriptManager.relay("HigraphManager", "setDefaultNodeValueShow", true, PDV.CENTER);
        ScriptManager.relay("HigraphManager", "setDefaultNodeValueColor", PDV.WHITE);
        ScriptManager.relay("HigraphManager", "setDefaultNodeShape",PDV.RECTANGLE); 	
    }*/ /*#/T setup*/
    
    public int get(int i) {
        // assert 0 <= i && i < size ;
        return a[i] ; 
    }
    
    public void set(int i, int x) {
        // assert 0 <= i && i < size ;
        a[i] = x ; }
    
    public void grow() {
        if( size == a.length ) {
            int[] b = new int[ 2* a.length] ;/*#I makeNewArray(b);*/
            for(int i=0 ; i<a.length ; ++i )
                b[i] = a[i] ;
            /*#I replaceArray(a, b);*/ a = b ;   }
        a[size] = 0 ;
        ++size ;
    }
    
    public void shrink() {
        // assert size > 0 ;
        if( size-1 < a.length/4 ) {
            int[] b = new int[ a.length / 2 ] ;/*#TC*/ /*#I makeNewArray(b);*/ /*#/TC*/
            for( int i=0; i < size-1 ; ++i )
                b[i] = a[i] ;/*#TC*/ /*#I replaceArray(a, b);*/ /*#/TC*/        
         a = b ; }
        --size ;
    }
    
    public static void main(String[] str) { 	/*#TC*/ /*#I setup();*/ /*#/TC*/
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
