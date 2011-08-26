/*
 * Created on Jun 16, 2011 by mpbl. 
 */

package demos;
/*#I
import tm.scripting.ScriptManager ;
import tm.scripting.PDV ;
 */
import java.lang.*;

public class EmbeddedMatrixDemo {

	public static final int X0 = 20;     // Starting position for array displays
	public static final int Y0 = 20; 

    private int[][] matrixC;
    private int[][] matrixD;
    private int[][] matrixE;
    private int[][] matrixF;
    
 
    public EmbeddedMatrixDemo(int s) {
    	if (s < 0) s = 0;
        matrixC = new int[s][s];
        matrixD = new int[s][s];
        matrixE = new int[s][s];
        matrixF = new int[s][s];
        randomize(matrixC, 0);
        randomize(matrixD, s);
        randomize(matrixE, 2*s);
        randomize(matrixF, 3*s);/*#TS*/makeMatrices();/*#/TS*/
   } 
    
    public void randomize(int[][] matrix, int s){
    	int tl = 0;
    	for (int i = 0; i < matrix.length; i++){
    		if (i>0) tl = tl + matrix[i-1].length;
    		for (int j = 0; j < matrix[i].length; j++)
    			matrix[i][j] = (17547 *(j + tl + s))%101;//(int)(100.5*MATH.random()); // This is not strictly correct
    	}
     }/*#TS*/
    
    /****** Visualization routines ***********/
    
    /*********** Primitive routines ******/  
     public void makeMatrices(){
     	PDV.makeMatrix(matrixC,"matrixC");
        ScriptManager.relay("HigraphManager","placeNode", matrixC, true, 0, 0);
        PDV.makeMatrix(matrixD,"matrixD");
        ScriptManager.relay("HigraphManager","placeNode", matrixD, true, 300, 0);
        PDV.makeMatrix(matrixE,"matrixE");
        ScriptManager.relay("HigraphManager","placeNode", matrixE, true, 0, 300);
        PDV.makeMatrix(matrixF,"matrixF");
        ScriptManager.relay("HigraphManager","placeNode", matrixF, true, 300, 300);
   	
    }
          
     /********** special compound routines for this visualization ******/
      
    public static void setup(){
        ScriptManager.relay("HigraphManager", "makeView", "mainView", "wholeGraph","Higraph.PDV", "PlacedNode");
        ScriptManager.relay("HigraphManager", "setTitle", "mainView", "PDV of Placed Matrices");
        ScriptManager.relay("HigraphManager", "setDefaultNodeShape", PDV.ELLIPSE);
        ScriptManager.relay("HigraphManager", "setDefaultNodeFillColor", PDV.BLUE);
        ScriptManager.relay("HigraphManager", "setDefaultNodeValueShow", true, PDV.CENTER);
        ScriptManager.relay("HigraphManager", "setDefaultNodeValueColor", PDV.WHITE);
        ScriptManager.relay("HigraphManager", "setDefaultNodeNameShow", true, PDV.EAST);
        ScriptManager.relay("HigraphManager", "setDefaultNodeNameColor", PDV.BLUE);
    }/*#/TS*/
    
    
    public static void main(String[] str) {
    	/*#TS*/setup();/*#/TS*/
    	EmbeddedMatrixDemo a = new EmbeddedMatrixDemo(4);
        ScriptManager.relay("HigraphManager", "dislocate", a.matrixE, true, 0, 20);
        ScriptManager.relay("HigraphManager", "dislocate", a.matrixE, true, -50, 40);
        ScriptManager.relay("HigraphManager", "dislocate", a.matrixD, true, 0, 0);
        ScriptManager.relay("HigraphManager", "dislocate", a.matrixE, true, 0, 0);
     }
}
