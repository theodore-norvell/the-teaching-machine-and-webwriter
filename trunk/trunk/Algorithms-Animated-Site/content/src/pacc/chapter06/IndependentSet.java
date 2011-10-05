package pacc.chapter06;

//@formatter:off
/*#I
import tm.scripting.ScriptManager;
import tm.scripting.PDV;*/
//@formatter:on

class Input {
    int I = 9;
    int[][] in = { { 15, 75 }, { 90, 95 }, { 20, 30 }, { 35, 50 }, { 60, 70 },
            { 85, 100 }, { 25, 40 }, { 45, 55 }, { 65, 80 } };/*#I
    int[] xPos = new int[]{170, 320,  20,  95, 270, 320,  20, 170, 270} ;
    int[] yPos = new int[]{ 80,  80,  80,   0,   0,   0,   0,   0,  80} ;*/
}

public class IndependentSet {
    Input in;
    int[][] right;
    boolean[] assign;

    IndependentSet(Input in) {
        this.in = in;
        //@formatter:off
        /*#I
        PDV.makeMatrix(in.in, true) ;
        ScriptManager.relay("HigraphManager","addTopNode",  "matrixSG", in.in, true) ;
        ScriptManager.relay("HigraphManager","setNodeNameShow", in.in, true, true);
        ScriptManager.relay("HigraphManager","setNodeNamePosition", in.in, true, PDV.NORTH);
        ScriptManager.relay("HigraphManager","setNodeNameLabel", in.in, true, "in.in");
        ScriptManager.relay("HigraphManager","addTopNode",  "matrixSG", in.in, true) ;*/
        //@formatter:on
        right = new int[in.I][2];
        assign = new boolean[in.I];/*#I
        for( int i = 0 ; i < in.I ; ++i ) {
            ScriptManager.relay("HigraphManager","makeNode", in.xPos[i] ) ;
            ScriptManager.relay("HigraphManager","addTopNode",  "graphSG", in.xPos[i] ) ;
            ScriptManager.relay("HigraphManager","placeNode", in.xPos[i], in.xPos[i], in.yPos[i]  ) ;
            ScriptManager.relay("HigraphManager","setNodeShape", in.xPos[i], PDV.RECTANGLE  ) ;
            ScriptManager.relay("HigraphManager","setNodeValueShow", in.xPos[i], false  ) ;
            String start = Integer.toString( in.in[i][0] ) ;
            String end = Integer.toString( in.in[i][1] ) ;
            String label = start.concat("-").concat(end) ;
            ScriptManager.relay("HigraphManager","setNodeNameLabel", in.xPos[i], label  ) ;
            ScriptManager.relay("HigraphManager","setNodeNamePosition", in.xPos[i], PDV.CENTER  ) ;
            ScriptManager.relay("HigraphManager","setNodeNameShow", in.xPos[i], true  ) ; }
        for( int i = 0 ; i < in.I ; ++i )
            for( int j = 0; j < in.I ; ++j )
                if( i != j && 
                (  in.in[i][0] <= in.in[j][0] && in.in[j][0] <= in.in[i][1]
                || in.in[i][0] <= in.in[j][1] && in.in[j][1] <= in.in[i][1]
                || in.in[j][0] <= in.in[i][0] && in.in[i][0] <= in.in[j][1]
                || in.in[j][0] <= in.in[i][1] && in.in[i][1] <= in.in[j][1] ) ) {
                    ScriptManager.relay("HigraphManager", "makeEdge", in.xPos[i], in.xPos[j] ) ; }
        
        */
        //@formatter:off
        //@formatter:on
        for (int i = 0; i < in.I; i++) {
            right[i][0] = in.in[i][1];
            right[i][1] = i;
            assign[i] = false;
        }
    }

    private void sort() {
        for (int i = 0; i < in.I; i++) {
            int nextExtreme = right[i][0];
            int nextInterval = right[i][1];
            int j = i;
            while ((j > 0) && (right[j - 1][0] > nextExtreme)) {
                right[j][0] = right[j - 1][0];
                right[j][1] = right[j - 1][1];
                j = j - 1;
            }
            right[j][0] = nextExtreme;
            right[j][1] = nextInterval;
        }
    }

    private void computeIndependentSet() {
        int l = -1;
        for (int j = 0; j < in.I; j++) {
            //@formatter:off
            /*#I
            if (j>0) {
                ScriptManager.relay("HigraphManager", "setNodeFillColor", right[j-1][0], PDV.WHITE);
                ScriptManager.relay("HigraphManager", "setNodeValueColor", right[j-1][0], PDV.BLACK);
                ScriptManager.relay("HigraphManager", "setNodeFillColor", right[j-1][1], PDV.WHITE);
                ScriptManager.relay("HigraphManager", "setNodeValueColor", right[j-1][1], PDV.BLACK); 
            }
            ScriptManager.relay("HigraphManager", "setNodeFillColor", right[j][0], PDV.RED);
            ScriptManager.relay("HigraphManager", "setNodeValueColor", right[j][0], PDV.WHITE);
            ScriptManager.relay("HigraphManager", "setNodeFillColor", right[j][1], PDV.RED);
            ScriptManager.relay("HigraphManager", "setNodeValueColor", right[j][1], PDV.WHITE); */
            //@formatter:on
            int i = right[j][1];
            //@formatter:off
            /*#I
            ScriptManager.relay("HigraphManager", "setNodeFillColor", in.in[i][0], PDV.RED);
            ScriptManager.relay("HigraphManager", "setNodeValueColor", in.in[i][0], PDV.WHITE);
            ScriptManager.relay("HigraphManager", "setNodeFillColor", in.in[i][1], PDV.RED); 
            ScriptManager.relay("HigraphManager", "setNodeValueColor", in.in[i][1], PDV.WHITE);*/
            //@formatter:on
            if ((j == 0) || (in.in[i][0] > in.in[l][1])) {
                //@formatter:off
                /*#I
                int pl = l; */
                //@formatter:on
                assign[i] = true;
                l = i;
                //@formatter:off
                /*#I
                if (pl>0) {
                    ScriptManager.relay("HigraphManager", "setNodeFillColor", in.in[pl][0], PDV.GREEN);
                    ScriptManager.relay("HigraphManager", "setNodeValueColor", in.in[pl][0], PDV.BLACK);
                    ScriptManager.relay("HigraphManager", "setNodeFillColor", in.in[pl][1], PDV.GREEN); 
                    ScriptManager.relay("HigraphManager", "setNodeValueColor", in.in[pl][1], PDV.BLACK);
                }
                ScriptManager.relay("HigraphManager", "setNodeFillColor", in.in[i][0], PDV.BLUE);
                ScriptManager.relay("HigraphManager", "setNodeValueColor", in.in[i][0], PDV.WHITE);
                ScriptManager.relay("HigraphManager", "setNodeFillColor", in.in[i][1], PDV.BLUE);
                ScriptManager.relay("HigraphManager", "setNodeValueColor", in.in[i][1], PDV.WHITE); */
                //@formatter:on
            }
            //@formatter:off
            /*#I
            if (l!=i) {
                ScriptManager.relay("HigraphManager", "setNodeFillColor", in.in[i][0], PDV.WHITE);
                ScriptManager.relay("HigraphManager", "setNodeValueColor", in.in[i][0], PDV.BLACK);
                ScriptManager.relay("HigraphManager", "setNodeFillColor", in.in[i][1], PDV.WHITE); 
                ScriptManager.relay("HigraphManager", "setNodeValueColor", in.in[i][1], PDV.BLACK);
            } */
            //@formatter:on
        }
        //@formatter:off
        /*#I
        ScriptManager.relay("HigraphManager", "setNodeFillColor", right[in.I-1][0], PDV.WHITE);
        ScriptManager.relay("HigraphManager", "setNodeValueColor", right[in.I-1][0], PDV.BLACK);
        ScriptManager.relay("HigraphManager", "setNodeFillColor", right[in.I-1][1], PDV.WHITE);
        ScriptManager.relay("HigraphManager", "setNodeValueColor", right[in.I-1][1], PDV.BLACK); 
        ScriptManager.relay("HigraphManager", "setNodeFillColor", in.in[l][0], PDV.GREEN);
        ScriptManager.relay("HigraphManager", "setNodeValueColor", in.in[l][0], PDV.BLACK);
        ScriptManager.relay("HigraphManager", "setNodeFillColor", in.in[l][1], PDV.GREEN); 
        ScriptManager.relay("HigraphManager", "setNodeValueColor", in.in[l][1], PDV.BLACK); */
        //@formatter:on
    }

    public static void main(String[] args) {
        //@formatter:off
        /*#I
        ScriptManager.relay("HigraphManager", "makeSubGraph", "matrixSG" ) ;
        ScriptManager.relay("HigraphManager", "makeSubGraph", "graphSG" ) ;
        ScriptManager.relay("HigraphManager", "makeView", "mainView", "matrixSG","Higraph.Matrices", "SimpleTree");
        ScriptManager.relay("HigraphManager", "makeView", "graphView", "graphSG","Higraph.Graph", "PlacedNode");
        ScriptManager.relay("HigraphManager", "setDefaultNodeFillColor", PDV.WHITE);
        ScriptManager.relay("HigraphManager", "setDefaultNodeValueColor", PDV.BLACK); */
        //@formatter:on
        Input in = new Input();
        IndependentSet is = new IndependentSet(in);
        is.sort();
        is.computeIndependentSet();
    }
}
