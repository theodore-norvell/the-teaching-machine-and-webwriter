/*
 * Created on Aug 5, 2011 by Theodore S. Norvell. 
 */

/*#I
import tm.scripting.ScriptManager ;
import tm.scripting.PDV ;
 */
public class UniformMatrixDemo {
    
    public static void main( String[] args ) {/*#I
        ScriptManager.relay("HigraphManager", "makeView", "mainView", "wholeGraph","Higraph.PDV", "SimpleTree");
        ScriptManager.relay("HigraphManager", "setTitle", "mainView", "An Example Matrix");
        ScriptManager.relay("HigraphManager", "setDefaultNodeFillColor", PDV.BLUE);
        ScriptManager.relay("HigraphManager", "setDefaultNodeValueColor", PDV.WHITE); */
        final int R = 4, C = 5 ;
        int [][] m = new int[R][C]; /*#I
        PDV.makeMatrix(m, true) ;
        ScriptManager.relay("HigraphManager","setNodeNameShow", m, true, true);
        ScriptManager.relay("HigraphManager","setNodeNamePosition", m, true, PDV.NORTH);
        ScriptManager.relay("HigraphManager","setNodeNameLabel", m, true, "m");
        ScriptManager.relay("HigraphManager","placeNode", m, true, 20, 20);*/
        int k = 0 ;
        for(int i=0 ; i < R ; ++i )
            for( int j = 0 ; j < C ; ++j )
                m[i][j] = k++ ;
    }

}
