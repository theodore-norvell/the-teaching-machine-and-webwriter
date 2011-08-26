#include <iostream>
/*#TS*/#include <ScriptManager>

/** Constant arguments for passing enumerated values into the TM
*/

// Colors
const int WHITE = 0xffffff;
const int BLACK = 0x000000;
const int GREY = 0x808080;
const int RED = 0xff0000;
const int GREEN = 0x00ff00;
const int BLUE = 0x0000ff;

// String markers
const int MARKER_RED = 0xffff;	
const int MARKER_BLUE = 0xfffe;	
const int MARKER_BLACK = 0xfffd;	
const int MARKER_GREEN = 0xfffc;
const int END_MARKER = 0xfff0;

// predefined node shapes
const int ELLIPSE = 0;
const int RECTANGLE = 1;
const int ROUND_RECTANGLE = 2;

// Predefined label positions, relative to component being labelled
const int CENTER = 0;        // (CenterX, CenterY)
const int EAST = 1;          // (MaxX, CenterY)
const int NORTHEAST = 2;     // (MaxX, MinY)
const int NORTH = 3;         // (CenterX, MinY)
const int NORTHWEST = 4;     // (MinX, MinY)
const int WEST = 5;          // (MinX, CenterY)
const int SOUTHWEST = 6;     // (MinX, MaxY)
const int SOUTH = 7;         // (CenterX, MaxY)
const int SOUTHEAST = 8;     // (MaxX, MaxY)




/* Standard service routines */

void moveNode(int& node, int x, int y){
    ScriptManager::relay("HigraphManager","placeNode", node, x, y);
}

void colorNode(int& node, int color, int fillColor, int nameColor){
    ScriptManager::relay("HigraphManager","setNodeColor", node, color);
    ScriptManager::relay("HigraphManager","setNodeFillColor", node, fillColor);
    ScriptManager::relay("HigraphManager","setNodeNameColor", node, nameColor);
}

void makeNode(int& node, int x, int y, int color, int fillColor, int labelColor){
    ScriptManager::relay("HigraphManager","makeNode", node);
    moveNode(node, x, y);
    colorNode(node, color, fillColor, labelColor);
}

void labelNode(int& node, char* s, int position){
	ScriptManager::relay("HigraphManager","createNodeExtraLabel", node, "altName", position);
//	ScriptManager::relay("HigraphManager","setNodeExtraNudge", node, "altName", -1,3);
	ScriptManager::relay("HigraphManager","setNodeExtraColor", node, "altName", RED);
	ScriptManager::relay("HigraphManager","setNodeExtraLabel", node, "altName", s);
}

void setDefaultNodeShape(int s){
    ScriptManager::relay("HigraphManager","setDefaultNodeShape", s);
}

void setNodeShape(int& node, int s){
    ScriptManager::relay("HigraphManager","setNodeShape", node, s);
}


void colorEdge(int& source, int& target, int color){
    ScriptManager::relay("HigraphManager", "setEdgeColor", source, target, color);
}

void makeEdge(int& source, int& target, int color){
    ScriptManager::relay("HigraphManager","makeEdge", source, target);
    ScriptManager::relay("HigraphManager", "setEdgeColor", source, target, color);
}

void createString(char* id){
    ScriptManager::relay("HigraphManager","createString", id);	
}

void clearString(char* id){
    ScriptManager::relay("HigraphManager","clearString", id);	
}

void addToString(char* id, char* addendum){
    ScriptManager::relay("HigraphManager","addToString", id, addendum);	
}

void placeString(char* id, int x, int y){
    ScriptManager::relay("HigraphManager","placeString", id, x, y);	
}

void setStringBaseColor(char* id, int color){
    ScriptManager::relay("HigraphManager","setStringBaseColor", id, color);	
}

void removeString(char* id){
    ScriptManager::relay("HigraphManager","removeString", id);	
}

void markSubString(char* id, char* subStr, int marker, int index){
    ScriptManager::relay("HigraphManager","markSubString", id, subStr, marker, index);		
}

void removeStringMarking(char* id){
    ScriptManager::relay("HigraphManager","removeStringMarking", id);	
}

void makeString(char* id, char* s, int x, int y, int c){
	createString(id);
	addToString(id, s);
	placeString(id, x, y);
	setStringBaseColor(id, c);
}



void drawLine(double x1, double y1, double x2, double y2){
    ScriptManager::relay("HigraphManager", "drawLine", x1, y1, x2, y2);
}

/* Constants and variables used by both the algorithm and the
instrumentation code */ /*#/TS*/

/** NV is the number of Boolean variables x_0, ..., x_{NV-1}.ì */

const int NV = 3;   /** A Boolean formula in 3-CNF is a conjunction of M disjunctions: each
                     * disjunction contains exactly three literals, where a literal is either a
                     * variable x_i (index 2*i) or its negation (index 2*i+1). Hence, a Boolean
                     * formula in 3-CNF can be represented by a 2-dimensional array with M rows
                     * and 3 columns: element at position (j,i) is equal to the index of the
                     * (i+1)-th literal in clause j.
                   */

const int ND = 3;
const int NN = 2*NV + 3*ND;
const int x0 = 40;
const int y0 = 40;
const int dx1 = 80;
const int dx2 = 200;
const int dy = 130;

int formula[ND][NV];

    /** The graph is represented by its adjacency matrix.*/
bool graph[NN][NN];
    
    /** Value vector */
bool val[NV];


/* Constants and variables used by both the instrumentation code */

int p[NV];   // the array of positive variables - nodes 0, 2, .. 2*(NV-1)
int n[NV];   // the array of negative variables  n[i] = !p[i] - nodes 1, 3... 2*(NV-1) + 1

int f[ND];   // the array of first subTerms - nodes 2*NV, 2*NV+3... 2*NV + 3*(ND-1)
int s[ND];   // the array of second subTerms - nodes 2*NV+1, 2*NV+4... 2*NV + 3*(ND-1)+1
int t[ND];   // the array of third subTerms - nodes 2*NV+2, 2*NV+5... 2*NV + 3*(ND-1)+2

/*#TS*/
/*********** Instrumentation Methods *************************/


void setup(){
    ScriptManager::relay("HigraphManager", "makeView", "mainView", "wholeGraph","Higraph.PDV", "PlacedNode");
    ScriptManager::relay("HigraphManager", "setDefaultNodeNameShow", true,CENTER);
    ScriptManager::relay("HigraphManager", "setTitle", "mainView", "Minimizing a Boolean formula in 3-CNF");
//    setDefaultNodeShape(ROUND_RECTANGLE);
    makeString("expression", "(X1 OR X2",  20, 10, BLACK);
    addToString("expression", " OR X3)");
    addToString("expression" ," AND ((NOT X1) OR (NOT X2) OR X3) AND ((NOT X1) OR X2 OR (NOT X3))");
    markSubString("expression" , "(NOT X1)", MARKER_RED, 0);
    markSubString("expression" , "(NOT X1)", MARKER_RED, 30);
}


void drawAllNodes(int x0, int y0, int dx1, int dx2, int dy){

    int x = x0;
    int y = y0;

    for (int i = 0; i < NV; i++) {
        makeNode(p[i], x, y, BLACK, BLUE, WHITE);
        x += dx1;
        makeNode(n[i], x, y, BLACK, RED, WHITE);
        x += dx2;

// For each variable, add an edge between the two nodes correspoNDing to
// the variable

//        makeEdge(p[i], n[i], BLUE);
    }
    x = x0 + dx2/2;
    y += dy;

    for (int j = 0; j < ND; j++ ){
        makeNode(f[j], x, y, BLACK, GREEN, BLACK);
        setNodeShape(f[j], ROUND_RECTANGLE);
        y += dy/2;
        x -= dx1/2;
        makeNode(s[j], x, y, BLACK, GREEN, BLACK);
        x += dx1;
        makeNode(t[j], x, y, BLACK, GREEN, BLACK);

//        makeEdge(f[j], s[j], GREEN);
//        makeEdge(f[j], t[j], GREEN);
//        makeEdge(t[j], s[j], GREEN);
        x += dx2;
        y -= dy/2;
    }
}

int& mapNode(int index){
	
	if (index < 2*NV)
		if (index%2 == 0)
			return p[index/2];
		else
			return n[index/2];
	index -= 2*NV;
	if (index%3 == 0) return f[index/3];
	if (index%3 == 1) return s[index/3];
	return t[index/3];
}

char* labels[NN];
int labelPositions[NN];

void makeLabels(){
// TM doesn't yet implement array initialization.  Grrrrr!
		labels[0] = "n[0]";
		labels[1] = "n[1]";
		labels[2] = "n[2]";
		labels[3] = "n[3]";
		labels[4] = "n[4]";
		labels[5] = "n[5]";
		labels[6] = "n[6]";
		labels[7] = "n[7]";
		labels[8] = "n[8]"; 
		labels[9] = "n[9]";
		labels[10] = "n[10]";
		labels[11] = "n[11]"; 
		labels[12] = "n[12]";
		labels[13] = "n[13]";
		labels[14] = "n[14]";
		labelPositions[0] = WEST;
		labelPositions[1] = NORTHEAST;
		labelPositions[2] = WEST;
		labelPositions[3] = EAST;
		labelPositions[4] = WEST;
		labelPositions[5] = EAST;
		labelPositions[6] = WEST;
		labelPositions[7] = SOUTH;
		labelPositions[8] = SOUTH; 
		labelPositions[9] = EAST;
		labelPositions[10] = SOUTH;
		labelPositions[11] = SOUTH; 
		labelPositions[12] = EAST;
		labelPositions[13] = SOUTH;
		labelPositions[14] = SOUTH;
}

void showAlternateNodeLabels(){
	int i;
	for (i = 0; i < NV; i++){
		labelNode(p[i], labels[2*i], labelPositions[2*i]);
		labelNode(n[i], labels[2*i + 1], labelPositions[2*i + 1]);
	}
	
	for (i = 0; i < ND; i++){
		labelNode(f[i], labels[2*NV + 3*i], labelPositions[2*NV + 3*i]);
		labelNode(s[i], labels[2*NV + 3*i + 1], labelPositions[2*NV + 3*i + 1]);
		labelNode(t[i], labels[2*NV + 3*i + 2], labelPositions[2*NV + 3*i + 2]);
	}
}

void drawSomething(){
	drawLine(100, 325, 400, 325);
	drawLine(100, 330, 400, 330);
	drawLine(100, 335, 400, 335);
	drawLine(100, 325, 100, 335);
	drawLine(400, 325, 400, 335);
}/*#/TS*/


/** This method creates the Boolean formula in 3-CNF. The current
  * implementation creates the following formula: (x_0 OR x_1 OR x_2) AND
  * ((NOT x_0) OR (NOT x_1) OR x_2) AND ((NOT x_0) OR x_1 OR (NOT x_2))
*/
void createFormula() {
    formula[0][0] = 0;
    formula[0][1] = 2;
    formula[0][2] = 4;
    formula[1][0] = 1;
    formula[1][1] = 3;
    formula[1][2] = 4;
    formula[2][0] = 1;
    formula[2][1] = 2;
    formula[2][2] = 5;
}




/** This method creates the graph corresponding to the Boolean formula in
  * 3-CNF. The number of nodes in the graph is equal to 2*n+3*m, since we
  * have two nodes for each variable and three nodes for each disjunction.
  */

void createEmptyGraph() {
    for (int u = 0; u < NN; u++) {
        for (int v = 0; v < NN; v++) {
	    graph[u][v] = false;
        }
    }
  }

void createVariableGadget(int v) {
// For variable v, add an edge between the two nodes corresponding to
// the variable: this implies to set the corresponding two elements in
// the adjacency matrix equal to true

    	int x = x0 + v*(dx1+dx2); /*#TS*/ makeNode(p[v], x, y0, BLACK, GREEN, WHITE); makeNode(n[v], x + dx1, y0,  BLACK, RED, WHITE);/*#/TS*/
        int nodeId = 2 * v;
        graph[nodeId][nodeId + 1] = true; /*#TS*/ makeEdge(p[v], n[v], BLACK);/*#/TS*/
//        graph[nodeId + 1][nodeId] = true; 
}

void createClauseGadget(int c) {

// For disjunction c, add an edge between the three nodes
// corresponding to the disjunction: this implies to set the
// corresponding six elements in the adjacency matrix equal to true

	    int nodeId = 2 * NV + 3 * c;
	    int x = x0 + c*dx2 + dx2/2;
	    int y = y0 + dy; /*#TS*/ makeNode(f[c], x, y, BLACK, BLUE, WHITE); makeNode(s[c], x-dx1/2, y+dy/2, BLACK, BLUE, WHITE); makeNode(t[c], x+dx1/2, y+dy/2, BLACK, BLUE, WHITE);/*#/TS*/
        graph[nodeId][nodeId + 1] = true;  /*#TS*/ makeEdge(f[c], s[c], BLUE);/*#/TS*/
//        graph[nodeId + 1][nodeId] = true;
        graph[nodeId][nodeId + 2] = true; /*#TS*/ makeEdge(f[c], t[c], BLUE);/*#/TS*/
//        graph[nodeId + 2][nodeId] = true;
        graph[nodeId + 1][nodeId + 2] = true; /*#TS*/ makeEdge(s[c], t[c], BLUE);/*#/TS*/
//        graph[nodeId + 2][nodeId + 1] = true;

}

void connectClauseGadget(int c){

// For each disjunction and for each literal in the disjunction, add an
// edge between the two nodes corresponding to the disjunction element
// and the literal: this implies to set the corresponding two elements
// in the adjacency matrix equal to true

        int nodeId = 2 * NV + 3 * c;
        for (int l = 0; l < 3; l++) {
            int v = formula[c][l];
            if (nodeId + l > v){
            	graph[nodeId + l][v] = true; /*#TS*/ makeEdge(mapNode(nodeId + l), mapNode(v), GREEN);/*#/TS*/
            } else {
            	graph[v][nodeId + l] = true; /*#TS*/ makeEdge(mapNode(v), mapNode(nodeId + l), GREEN);/*#/TS*/
            }
        }
}

/*#TS*/void colorAllNodes(int color, int fillColor, int labelColor){
	for (int v = 0; v < NV; v++) {
		colorNode(p[v], color, fillColor, labelColor);
		colorNode(n[v], color, fillColor, labelColor);
	}
	for (int d = 0; d < ND; d++) {
		colorNode(f[d], color, fillColor, labelColor);
		colorNode(s[d], color, fillColor, labelColor);
		colorNode(t[d], color, fillColor, labelColor);		
	}	
}

void colorAllEdges(int color){
	for (int i = 0; i < NN; i++) 
		for (int j = 0; j < NN; j++)
			if (graph[i][j])
				colorEdge(mapNode(i), mapNode(j), color);
}/*#/TS*/

void inputValues(){
	val[0] = true;
	val[1] = false;
	val[2] = false;
}

void chooseVariableNode(int t){
	if (val[t]) {
		colorNode(p[t],BLACK, BLACK, WHITE);
		for (int e = 0; e < NN; e++){
			if (graph[2*t][e]){
				colorEdge(p[t], mapNode(e), GREEN);
				colorEdge(mapNode(e), p[t], GREEN);
			}
		}
	} else {
		colorNode(n[t], BLACK, BLACK, WHITE);
		for (int e = 0; e < NN; e++){
			if (graph[2*t+1][e]){
				colorEdge(n[t],mapNode(e), GREEN);
				colorEdge(mapNode(e), n[t], GREEN);
			}
		}
	}
}

void chooseClauseNodes(int c){
	if(formula[c][0]%2==0){
		if(val[formula[c][0]/2]){
			colorNode(s[c], BLACK, BLACK, WHITE);
			colorNode(t[c], BLACK, BLACK, WHITE);
			for (int e = 0; e < 2*NV; e++){
				if (graph[2*NV+3*c+1][e]){
					colorEdge(s[c],mapNode(e), RED);
					colorEdge(mapNode(e), s[c], RED);
				}
				if (graph[2*NV+3*c+2][e]){
					colorEdge(t[c],mapNode(e), RED);
					colorEdge(mapNode(e), t[c], RED);
				}
			}
		}
	} else {
		if(!val[formula[c][0]/2]){
			colorNode(s[c], BLACK, BLACK, WHITE);
			colorNode(t[c], BLACK, BLACK, WHITE);
			for (int e = 0; e < 2*NV; e++){
				if (graph[2*NV+3*c+1][e]){
					colorEdge(s[c],mapNode(e), RED);
					colorEdge(mapNode(e), s[c], RED);
				}
				if (graph[2*NV+3*c+2][e]){
					colorEdge(t[c],mapNode(e), RED);
					colorEdge(mapNode(e), t[c], RED);
				}
			}
		}		
	}
	if(formula[c][1]%2==0){
		if(val[formula[c][1]/2]){
			colorNode(f[c], BLACK, BLACK, WHITE);
			colorNode(t[c], BLACK, BLACK, WHITE);
			for (int e = 0; e < 2*NV; e++){
				if (graph[2*NV+3*c][e]){
					colorEdge(f[c],mapNode(e), RED);
					colorEdge(mapNode(e), f[c], RED);
				}
				if (graph[2*NV+3*c+2][e]){
					colorEdge(t[c],mapNode(e), RED);
					colorEdge(mapNode(e), t[c], RED);
				}
			}
		}
	} else {
		if(!val[formula[c][1]/2]){
			colorNode(f[c], BLACK, BLACK, WHITE);
			colorNode(t[c], BLACK, BLACK, WHITE);
			for (int e = 0; e < 2*NV; e++){
				if (graph[2*NV+3*c][e]){
					colorEdge(f[c],mapNode(e), RED);
					colorEdge(mapNode(e), f[c], RED);
				}
				if (graph[2*NV+3*c+2][e]){
					colorEdge(t[c],mapNode(e), RED);
					colorEdge(mapNode(e), t[c], RED);
				}
			}
		}
	}
	if(formula[c][2]%2==0){
		if(val[formula[c][2]/2]){
			colorNode(f[c], BLACK, BLACK, WHITE);
			colorNode(s[c], BLACK, BLACK, WHITE);
			for (int e = 0; e < 2*NV; e++){
				if (graph[2*NV+3*c][e]){
					colorEdge(f[c],mapNode(e), RED);
					colorEdge(mapNode(e), f[c], RED);
				}
				if (graph[2*NV+3*c+1][e]){
					colorEdge(s[c],mapNode(e), RED);
					colorEdge(mapNode(e), s[c], RED);
				}
			}
		}
	} else {
		if(!val[formula[c][2]/2]){
			colorNode(f[c], BLACK, BLACK, WHITE);
			colorNode(s[c], BLACK, BLACK, WHITE);
			for (int e = 0; e < 2*NV; e++){
				if (graph[2*NV+3*c][e]){
					colorEdge(f[c],mapNode(e), RED);
					colorEdge(mapNode(e), f[c], RED);
				}
				if (graph[2*NV+3*c+1][e]){
					colorEdge(s[c],mapNode(e), RED);
					colorEdge(mapNode(e), s[c], RED);
				}
			}
		}		
	}
	colorEdge(f[c],s[c], RED);
	colorEdge(f[c],t[c], RED);
	colorEdge(s[c],t[c], RED);
}



/** This methods prints the adjacency matrix of the graph,
  * by using 0s and 1s.
*/ 

void printGraph() {
    for (int i = 0; i < NN; i++) {
        for (int j = 0; j < NN; j++) {
	       if (graph[i][j])
                cout << 1 << " ";
           else
                 cout << 0 << " ";
        }
        cout << '\n';
    }
        cout << '\n';
}

/** Main method. */

void main( ) { /*#TS*/  setup(); /*#/TS*/
    createFormula();
    createEmptyGraph();
    for (int v = 0; v < NV; v++)
        createVariableGadget(v);
    for (int c = 0; c < ND; c++)
    	createClauseGadget(c);
    for (int c = 0; c < ND; c++) {
    	connectClauseGadget(c);
    }  /*#TS*/ colorAllNodes(BLACK, WHITE, BLACK); colorAllEdges(BLACK);makeString("blackNodes", "black nodes signify ...", 800, 50, BLACK);/*#/TS*/
    
    inputValues();  /*#TS*/ removeStringMarking("expression");/*#/TS*/
    for (int v = 0; v < NV; v++)
    	chooseVariableNode(v);
    for (int c = 0; c < ND; c++)
    	chooseClauseNodes(c);
    printGraph();
    makeLabels();
    showAlternateNodeLabels();
    drawSomething();
    makeEdge(f[2], f[2], BLUE);
}
