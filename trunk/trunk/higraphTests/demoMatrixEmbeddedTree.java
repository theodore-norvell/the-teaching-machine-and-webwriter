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

	private int a = 3;
	private int b = 7;
	private int c = -1;
	private int d = 11;
	private int e = 13;
	private int f = -5;
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
        randomize(matrixF, 3*s);/*#TS*/makeTree();/*#/TS*/
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
    	PDV.makeMatrix(matrixC,"matrixC");
        ScriptManager.relay("HigraphManager","addChild", c, matrixC, true);
        PDV.makeMatrix(matrixD,"matrixD");
        ScriptManager.relay("HigraphManager","addChild", d, matrixD, true);
        PDV.makeMatrix(matrixE,"matrixE");
        ScriptManager.relay("HigraphManager","addChild", e, matrixE, true);
        PDV.makeMatrix(matrixF,"matrixF");
        ScriptManager.relay("HigraphManager","addChild", f, matrixF, true);
   	
    }
          
     /********** special compound routines for this visualization ******/
      
    public static void setup(){
        ScriptManager.relay("HigraphManager", "makeView", "mainView", "wholeGraph","Higraph.PDV", "SimpleTree");
        ScriptManager.relay("HigraphManager", "setTitle", "mainView", "PDV of a Forest of Matrices");
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
        ScriptManager.relay("HigraphManager", "dislocate", a.c, 0, -20);
        ScriptManager.relay("HigraphManager", "dislocate", a.e, 20, 0);
        ScriptManager.relay("HigraphManager", "dislocate", a.e, 0, 0);
        ScriptManager.relay("HigraphManager", "dislocate", a.c, 0, 0);
        ScriptManager.relay("HigraphManager", "dislocate", a.matrixE[2][3], 10, 0);
        ScriptManager.relay("HigraphManager", "dislocate", a.matrixE[2][3], 0, 0);
    }
}
