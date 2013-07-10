package divideetimpera;
/*#I
import tm.scripting.ScriptManager;
import tm.scripting.PDV;
*/
class Input {
	int N;
	int[][] x;
	int[][] y;
	Input(int[][] x, int[][] y) {
		N = x.length;
		this.x = x;
		this.y = y;
	}
}
public class Strassen {
	int[][] result;
	/*#I
	int X;
	int Y;
	
	void updateXY(boolean down, int n) {
		int offset = 60;
		int dy = 45;
		
		if (down) {
			Y = Y + n * dy + offset;
		} else {
			Y = Y - n * dy - offset;
		}
 	}
    void makeMatrix(int[][] matrix, boolean showCells, String name){
        ScriptManager.relay("HigraphManager","makeNode", matrix, true);
        ScriptManager.relay("HigraphManager","setNodeValueShow", matrix, true, false);
        ScriptManager.relay("HigraphManager","setNodeNameLabel", matrix, true, name);
        ScriptManager.relay("HigraphManager","setNodeLayoutManager", matrix, true, "VertNestedTree");
        ScriptManager.relay("HigraphManager", "setNodeShape", matrix, true, PDV.RECTANGLE);
        for( int i = 0; i < matrix.length ; ++i ) {
            ScriptManager.relay("HigraphManager","makeNode", matrix[i], true);
            ScriptManager.relay("HigraphManager","addChild", matrix, true, matrix[i], true);
            ScriptManager.relay("HigraphManager", "setNodeShape", matrix[i], true, PDV.RECTANGLE);
            ScriptManager.relay("HigraphManager", "setNodeColor", matrix[i], true, PDV.WHITE);
            ScriptManager.relay("HigraphManager","setNodeNameShow", matrix[i], true, false);
            ScriptManager.relay("HigraphManager","setNodeNamePosition", matrix[i], true, PDV.CENTER);
            ScriptManager.relay("HigraphManager","setNodeValueShow", matrix[i], true, false);
            ScriptManager.relay("HigraphManager","setNodeLayoutManager", matrix[i], true, "HorizNestedTree");
            for (int j = 0; j <  matrix[i].length; ++j ){
                ScriptManager.relay("HigraphManager","makeNode", matrix[i][j]);
                if( !showCells){
                    ScriptManager.relay("HigraphManager","setNodeColor", matrix[i][j], PDV.TRANSPARENT);
                    ScriptManager.relay("HigraphManager","setNodeFillColor", matrix[i][j], PDV.TRANSPARENT);
                }  
                ScriptManager.relay("HigraphManager","setNodeNameShow", matrix[i][j], false);
                ScriptManager.relay("HigraphManager","setNodeNamePosition", matrix[i][j], PDV.CENTER);
                ScriptManager.relay("HigraphManager","setNodeValueShow", matrix[i][j], true);
                ScriptManager.relay("HigraphManager","setNodeValuePosition", matrix[i][j], PDV.CENTER );
                ScriptManager.relay("HigraphManager", "setNodeShape", matrix[i][j], PDV.RECTANGLE);
                ScriptManager.relay("HigraphManager","addChild", matrix[i], true, matrix[i][j]);
                if( i == 0 ) {
                    ScriptManager.relay("HigraphManager", "createNodeExtraLabel", matrix[i][j], "column", PDV.NORTH ) ;
                    String jString = Integer.toString(j) ;
                    ScriptManager.relay("HigraphManager", "setNodeExtraLabel", matrix[i][j], "column", jString ) ; }
                if( j == 0 ) {
                    ScriptManager.relay("HigraphManager", "createNodeExtraLabel", matrix[i][j], "row", PDV.WEST ) ;
                    String iString = Integer.toString(i) ;
                    ScriptManager.relay("HigraphManager", "setNodeExtraLabel", matrix[i][j], "row", iString ) ; } } }
   }
*/
	private int[][] getSubmatrix(int[][] x, int upperRow, int lowerRow, int leftColumn, int rightColumn) {
		int n = x.length;
		int[][] result = new int[n / 2][n / 2];
		int i = 0, j;
		for (int row = upperRow; row < lowerRow; row++) {
			j = 0;
			for (int column = leftColumn; column < rightColumn; column++) {
				result[i][j] = x[row][column];
				j = j + 1;
			}
			i = i + 1;
		}
		return result;
	}
	private void setSubmatrix(int[][] y, int[][] x, int upperRow, int lowerRow, int leftColumn, int rightColumn) {
		int i = 0, j;
		for (int row = upperRow; row < lowerRow; row++) {
			j = 0;
			for (int column = leftColumn; column < rightColumn; column++) {
				y[row][column] = x[i][j];
				j = j + 1;
			}
			i = i + 1;
		}
	}
	private int[][] subtract(int[][] x, int[][] y) {
		int n = x.length;
		int[][] result = new int[n][n];
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				result[i][j] = x[i][j] - y[i][j];
			}
		}
		return result;
	}
	private int[][] sum(int[][] x, int[][] y) {
		int n = x.length;
		int[][] result = new int[n][n];
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				result[i][j] = x[i][j] + y[i][j];
			}
		}
		return result;
	}
	/*#I
	void makeMatrix(Input instance, int[][] x, int[][] y, boolean showCells) {
		ScriptManager.relay("HigraphManager","makeNode", instance, true);
        ScriptManager.relay("HigraphManager","setNodeLayoutManager", instance, true, "HorizNestedTree");
		ScriptManager.relay("HigraphManager","placeNode", instance, true, X, Y);
        ScriptManager.relay("HigraphManager","setNodeValueShow", instance, true, false);
        ScriptManager.relay("HigraphManager", "setNodeShape", instance, true, PDV.RECTANGLE);
		makeMatrix(x, true, "x");
		makeMatrix(y, true, "y");
        ScriptManager.relay("HigraphManager","addChild", instance, true, x, true);
        ScriptManager.relay("HigraphManager","addChild", instance, true, y, true);
	 }
	void emphMatrix(int[][] x, int fillColor, int valueColor) {
		for (int r = 0; r < x.length; r++) {
			for (int c = 0; c < x[0].length; c++) {
            	ScriptManager.relay("HigraphManager", "setNodeFillColor", x[r][c], fillColor);
            	ScriptManager.relay("HigraphManager", "setNodeValueColor", x[r][c], valueColor);
			}
		}
	 }
	void deEmphMatrix(int[][] x) {
		for (int r = 0; r < x.length; r++) {
			for (int c = 0; c < x[0].length; c++) {
            	ScriptManager.relay("HigraphManager", "setNodeFillColor", x[r][c], PDV.WHITE);
            	ScriptManager.relay("HigraphManager", "setNodeValueColor", x[r][c], PDV.BLACK);
			}
		}
	 }
	void emphSubmatrix(int[][] x, int r1, int r2, int c1, int c2, int fillColor, int valueColor) {
		for (int r = r1; r < r2; r++) {
			for (int c = c1; c < c2; c++) {
            	ScriptManager.relay("HigraphManager", "setNodeFillColor", x[r][c], fillColor);
            	ScriptManager.relay("HigraphManager", "setNodeValueColor", x[r][c], valueColor);
			}
		}
	 }
	void deEmphSubmatrix(int[][] x, int r1, int r2, int c1, int c2) {
		for (int r = r1; r < r2; r++) {
			for (int c = c1; c < c2; c++) {
            	ScriptManager.relay("HigraphManager", "setNodeFillColor", x[r][c], PDV.WHITE);
            	ScriptManager.relay("HigraphManager", "setNodeValueColor", x[r][c], PDV.BLACK);
			}
		}
	 }
	*/
	private int[][] matrixProduct(Input in) {
		/*#I
		makeMatrix(in, in.x, in.y, true) ;
		*/
		int[][] r = new int[in.N][in.N];
		/*#I
		makeMatrix(r, true, "x*y");
        ScriptManager.relay("HigraphManager","addChild", in, true, r, true);
		*/
		if (in.N == 1) {
			r[0][0] = in.x[0][0] * in.y[0][0];
		} else {
			/*#I
			emphSubmatrix(in.x, 0, in.N / 2, 0, in.N / 2, PDV.GREEN, PDV.BLACK);
			*/
			int[][] a = getSubmatrix(in.x, 0, in.N / 2, 0, in.N / 2);
			/*#I
			deEmphSubmatrix(in.x, 0, in.N / 2, 0, in.N / 2);
			emphSubmatrix(in.x, 0, in.N / 2, in.N / 2, in.N, PDV.GREEN, PDV.BLACK);
			*/
			int[][] b = getSubmatrix(in.x, 0, in.N / 2, in.N / 2, in.N);
			/*#I
			deEmphSubmatrix(in.x, 0, in.N / 2, in.N / 2, in.N);
			emphSubmatrix(in.x, in.N / 2, in.N, 0, in.N / 2, PDV.GREEN, PDV.BLACK);
			*/
			int[][] c = getSubmatrix(in.x, in.N / 2, in.N, 0, in.N / 2);
			/*#I
			deEmphSubmatrix(in.x, in.N / 2, in.N, 0, in.N / 2);
			emphSubmatrix(in.x, in.N / 2, in.N, in.N / 2, in.N, PDV.GREEN, PDV.BLACK);
			*/
			int[][] d = getSubmatrix(in.x, in.N / 2, in.N, in.N / 2, in.N);
			/*#I
			deEmphSubmatrix(in.x, in.N / 2, in.N, in.N / 2, in.N);
			emphSubmatrix(in.y, 0, in.N / 2, 0, in.N / 2, PDV.GREEN, PDV.BLACK);
			*/
			int[][] e = getSubmatrix(in.y, 0, in.N / 2, 0, in.N / 2);
			/*#I
			deEmphSubmatrix(in.y, 0, in.N / 2, 0, in.N / 2);
			emphSubmatrix(in.y, 0, in.N / 2, in.N / 2, in.N, PDV.GREEN, PDV.BLACK);
			*/
			int[][] f = getSubmatrix(in.y, 0, in.N / 2, in.N / 2, in.N);
			/*#I
			deEmphSubmatrix(in.y, 0, in.N / 2, in.N / 2, in.N);
			emphSubmatrix(in.y, in.N / 2, in.N, 0, in.N / 2, PDV.GREEN, PDV.BLACK);
			*/
			int[][] g = getSubmatrix(in.y, in.N / 2, in.N, 0, in.N / 2);
			/*#I
			deEmphSubmatrix(in.y, in.N / 2, in.N, 0, in.N / 2);
			emphSubmatrix(in.y, in.N / 2, in.N, in.N / 2, in.N, PDV.GREEN, PDV.BLACK);
			*/
			int[][] h = getSubmatrix(in.y, in.N / 2, in.N, in.N / 2, in.N);
			/*#I
			deEmphSubmatrix(in.y, in.N / 2, in.N, in.N / 2, in.N);
			*/
			Input newIn = new Input(subtract(b, d), sum(g, h));
			/*#I
			emphSubmatrix(in.x, 0, in.N / 2, in.N / 2, in.N, PDV.BLUE, PDV.WHITE);
			emphSubmatrix(in.x, in.N / 2, in.N, in.N / 2, in.N, PDV.RED, PDV.WHITE);
			emphSubmatrix(in.y, in.N / 2, in.N, 0, in.N / 2, PDV.BLUE, PDV.WHITE);
			emphSubmatrix(in.y, in.N / 2, in.N, in.N / 2, in.N, PDV.BLUE, PDV.WHITE);
			updateXY(true, in.N);
			*/
			int[][] v0 = matrixProduct(newIn);
			/*#I
			ScriptManager.relay("HigraphManager","deleteNode", newIn, true);
			makeMatrix(v0, true, "v0");
	        ScriptManager.relay("HigraphManager","addChild", in, true, v0, true);
			*/
			newIn = new Input(sum(a, d), sum(e, h));
			/*#I
			deEmphSubmatrix(in.x, 0, in.N / 2, in.N / 2, in.N);
			deEmphSubmatrix(in.x, in.N / 2, in.N, in.N / 2, in.N);
			deEmphSubmatrix(in.y, in.N / 2, in.N, 0, in.N / 2);
			deEmphSubmatrix(in.y, in.N / 2, in.N, in.N / 2, in.N);
			emphSubmatrix(in.x, 0, in.N / 2, 0, in.N / 2, PDV.BLUE, PDV.WHITE);
			emphSubmatrix(in.x, in.N / 2, in.N, in.N / 2, in.N, PDV.BLUE, PDV.WHITE);
			emphSubmatrix(in.y, 0, in.N / 2, 0, in.N / 2, PDV.BLUE, PDV.WHITE);
			emphSubmatrix(in.y, in.N / 2, in.N, in.N / 2, in.N, PDV.BLUE, PDV.WHITE);
			*/
			int[][] v1 = matrixProduct(newIn);
			/*#I
			ScriptManager.relay("HigraphManager","deleteNode", newIn, true);
			makeMatrix(v1, true, "v1");
	        ScriptManager.relay("HigraphManager","addChild", in, true, v1, true);
			*/
			newIn = new Input(subtract(a, c), sum(e, f));
			/*#I
			deEmphSubmatrix(in.x, 0, in.N / 2, 0, in.N / 2);
			deEmphSubmatrix(in.x, in.N / 2, in.N, in.N / 2, in.N);
			deEmphSubmatrix(in.y, 0, in.N / 2, 0, in.N / 2);
			deEmphSubmatrix(in.y, in.N / 2, in.N, in.N / 2, in.N);
			emphSubmatrix(in.x, 0, in.N / 2, 0, in.N / 2, PDV.BLUE, PDV.WHITE);
			emphSubmatrix(in.x, in.N / 2, in.N, 0, in.N / 2, PDV.RED, PDV.WHITE);
			emphSubmatrix(in.y, 0, in.N / 2, 0, in.N / 2, PDV.BLUE, PDV.WHITE);
			emphSubmatrix(in.y, 0, in.N / 2, in.N / 2, in.N, PDV.BLUE, PDV.WHITE);
			*/
			int[][] v2 = matrixProduct(newIn);
			/*#I
			ScriptManager.relay("HigraphManager","deleteNode", newIn, true);
			makeMatrix(v2, true, "v2");
	        ScriptManager.relay("HigraphManager","addChild", in, true, v2, true);
			*/
			newIn = new Input(sum(a, b), h);
			/*#I
			deEmphSubmatrix(in.x, 0, in.N / 2, 0, in.N / 2);
			deEmphSubmatrix(in.x, in.N / 2, in.N, 0, in.N / 2);
			deEmphSubmatrix(in.y, 0, in.N / 2, 0, in.N / 2);
			deEmphSubmatrix(in.y, 0, in.N / 2, in.N / 2, in.N);
			emphSubmatrix(in.x, 0, in.N / 2, 0, in.N / 2, PDV.BLUE, PDV.WHITE);
			emphSubmatrix(in.x, 0, in.N / 2, in.N / 2, in.N, PDV.BLUE, PDV.WHITE);
			emphSubmatrix(in.y, in.N / 2, in.N, in.N / 2, in.N, PDV.BLUE, PDV.WHITE);
			*/
			int[][] v3 = matrixProduct(newIn);
			/*#I
			ScriptManager.relay("HigraphManager","deleteNode", newIn, true);
			makeMatrix(v3, true, "v3");
	        ScriptManager.relay("HigraphManager","addChild", in, true, v3, true);
			*/
			newIn = new Input(a, subtract(f, h));
			/*#I
			deEmphSubmatrix(in.x, 0, in.N / 2, 0, in.N / 2);
			deEmphSubmatrix(in.x, 0, in.N / 2, in.N / 2, in.N);
			deEmphSubmatrix(in.y, in.N / 2, in.N, in.N / 2, in.N);
			emphSubmatrix(in.x, 0, in.N / 2, 0, in.N / 2, PDV.BLUE, PDV.WHITE);
			emphSubmatrix(in.y, 0, in.N / 2, in.N / 2, in.N, PDV.BLUE, PDV.WHITE);
			emphSubmatrix(in.y, in.N / 2, in.N, in.N / 2, in.N, PDV.RED, PDV.WHITE);
			*/
			int[][] v4 = matrixProduct(newIn);
			/*#I
			ScriptManager.relay("HigraphManager","deleteNode", newIn, true);
			makeMatrix(v4, true, "v4");
	        ScriptManager.relay("HigraphManager","addChild", in, true, v4, true);
			*/
			newIn = new Input(d, subtract(g, e));
			/*#I
			deEmphSubmatrix(in.x, 0, in.N / 2, 0, in.N / 2);
			deEmphSubmatrix(in.y, 0, in.N / 2, in.N / 2, in.N);
			deEmphSubmatrix(in.y, in.N / 2, in.N, in.N / 2, in.N);
			emphSubmatrix(in.x, in.N / 2, in.N, in.N / 2, in.N, PDV.BLUE, PDV.WHITE);
			emphSubmatrix(in.y, in.N / 2, in.N, 0, in.N / 2, PDV.BLUE, PDV.WHITE);
			emphSubmatrix(in.y, 0, in.N / 2, 0, in.N / 2, PDV.RED, PDV.WHITE);
			*/
			int[][] v5 = matrixProduct(newIn);
			/*#I
			ScriptManager.relay("HigraphManager","deleteNode", newIn, true);
			makeMatrix(v5, true, "v5");
	        ScriptManager.relay("HigraphManager","addChild", in, true, v5, true);
			*/
			newIn = new Input(sum(c, d), e);
			/*#I
			deEmphSubmatrix(in.x, in.N / 2, in.N, in.N / 2, in.N);
			deEmphSubmatrix(in.y, in.N / 2, in.N, 0, in.N / 2);
			deEmphSubmatrix(in.y, 0, in.N / 2, 0, in.N / 2);
			emphSubmatrix(in.x, in.N / 2, in.N, 0, in.N / 2, PDV.BLUE, PDV.WHITE);
			emphSubmatrix(in.x, in.N / 2, in.N, in.N / 2, in.N, PDV.BLUE, PDV.WHITE);
			emphSubmatrix(in.y, 0, in.N / 2, 0, in.N / 2, PDV.BLUE, PDV.WHITE);
			*/
			int[][] v6 = matrixProduct(newIn);
			/*#I
			deEmphSubmatrix(in.x, in.N / 2, in.N, 0, in.N / 2);
			deEmphSubmatrix(in.x, in.N / 2, in.N, in.N / 2, in.N);
			deEmphSubmatrix(in.y, 0, in.N / 2, 0, in.N / 2);
			ScriptManager.relay("HigraphManager","deleteNode", newIn, true);
			makeMatrix(v6, true, "v6");
	        ScriptManager.relay("HigraphManager","addChild", in, true, v6, true);
			updateXY(false, in.N);
			*/
			setSubmatrix(r, sum(subtract(sum(v0, v1), v3), v5), 0, in.N / 2, 0, in.N / 2);
			/*#I
			emphMatrix(v0, PDV.BLUE, PDV.WHITE);
			emphMatrix(v1, PDV.BLUE, PDV.WHITE);
			emphMatrix(v3, PDV.RED, PDV.WHITE);
			emphMatrix(v5, PDV.BLUE, PDV.WHITE);
			emphSubmatrix(r, 0, in.N / 2, 0, in.N / 2, PDV.GREEN, PDV.BLACK);
			*/
			setSubmatrix(r, sum(v3, v4), 0, in.N / 2, in.N / 2, in.N);
			/*#I
			deEmphMatrix(v0);
			deEmphMatrix(v1);
			deEmphMatrix(v3);
			deEmphMatrix(v5);
			deEmphSubmatrix(r, 0, in.N / 2, 0, in.N / 2);
			emphMatrix(v3, PDV.BLUE, PDV.WHITE);
			emphMatrix(v4, PDV.BLUE, PDV.WHITE);
			emphSubmatrix(r, 0, in.N / 2, in.N / 2, in.N , PDV.GREEN, PDV.BLACK);
			*/
			setSubmatrix(r, sum(v5, v6), in.N / 2, in.N, 0, in.N / 2);
			/*#I
			deEmphMatrix(v3);
			deEmphMatrix(v4);
			deEmphSubmatrix(r, 0, in.N / 2, in.N / 2, in.N);
			emphMatrix(v5, PDV.BLUE, PDV.WHITE);
			emphMatrix(v6, PDV.BLUE, PDV.WHITE);
			emphSubmatrix(r, in.N / 2, in.N, 0, in.N / 2, PDV.GREEN, PDV.BLACK);
			*/
			setSubmatrix(r, subtract(sum(subtract(v1, v2), v4), v6), in.N / 2, in.N, in.N / 2, in.N);
			/*#I
			deEmphMatrix(v5);
			deEmphMatrix(v6);
			deEmphSubmatrix(r, in.N / 2, in.N, 0, in.N / 2);
			emphMatrix(v1, PDV.BLUE, PDV.WHITE);
			emphMatrix(v2, PDV.RED, PDV.WHITE);
			emphMatrix(v4, PDV.BLUE, PDV.WHITE);
			emphMatrix(v6, PDV.RED, PDV.WHITE);
			emphSubmatrix(r, in.N / 2, in.N, in.N / 2, in.N, PDV.GREEN, PDV.BLACK);
			*/
			int dummyLine;
			/*#I
			deEmphSubmatrix(r, in.N / 2, in.N, in.N / 2, in.N);
			ScriptManager.relay("HigraphManager","deleteNode", v0, true);
			ScriptManager.relay("HigraphManager","deleteNode", v1, true);
			ScriptManager.relay("HigraphManager","deleteNode", v2, true);
			ScriptManager.relay("HigraphManager","deleteNode", v3, true);
			ScriptManager.relay("HigraphManager","deleteNode", v4, true);
			ScriptManager.relay("HigraphManager","deleteNode", v5, true);
			ScriptManager.relay("HigraphManager","deleteNode", v6, true);
			*/
		}
		return r;
	}
	int[][] run(int[][] x, int[][] y) {	
		/*#I
		X = 10;
		Y = 10;
		*/
		Input in = new Input(x, y);
		int[][] result = matrixProduct(in);
		/*#I
		ScriptManager.relay("HigraphManager","deleteNode", result, true);
		makeMatrix(result, true, "x * y");
        ScriptManager.relay("HigraphManager","addChild", in, true, result, true);
		*/
		return result;
	}
	public static void main(String[] args) {
/*#I
ScriptManager.relay("HigraphManager", "makeView", "mainView", "wholeGraph","Higraph.CallTree", "PlacedNode");
ScriptManager.relay("HigraphManager", "setDefaultNodeFillColor", PDV.WHITE);
ScriptManager.relay("HigraphManager", "setDefaultNodeSize", 20, 20); 
ScriptManager.relay("HigraphManager", "setDefaultNodeValueColor", PDV.BLACK); 
*/
		int[][] x = { { 4, 2, 1, 0 }, { 4, 3, 1, 2 }, { 3, 4, 2, 0 }, { 2, 0, 3, 1 } };
		int[][] y = { { 2, 0, 4, 1 }, { 2, 3, 1, 0 }, { 2, 0, 1, 3 }, { 3, 0, 4, 2 } };
		Strassen s = new Strassen();
		s.run(x, y);
	}
}