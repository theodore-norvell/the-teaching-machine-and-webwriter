#include <iostream>
#include <ScriptManager>

const int WHITE = 0xffffff;
const int BLACK = 0x000000;
const int GREY = 0x808080;
const int RED = 0xff0000;
const int GREEN = 0x00ff00;
const int BLUE = 0x0000ff;

const char MARKER_RED = 0xff;	
const char MARKER_BLUE = 0xfe;	
const char MARKER_BLACK = 0xfd;	
const char MARKER_GREEN = 0xfc;
const char ENDMARKER = 0xfb;

// predefined node shapes
const int ELLIPSE = 0;
const int RECTANGLE = 1;
const int ROUND_RECTANGLE = 2;



/* Standard service routines */

void moveNode(int& node, int x, int y){
    ScriptManager::relay("HigraphManager","placeNode", node, x, y);
}

void colorNode(int& node, int color, int fillColor, int labelColor){
    ScriptManager::relay("HigraphManager","setNodeColor", node, color);
    ScriptManager::relay("HigraphManager","setNodeFillColor", node, fillColor);
    ScriptManager::relay("HigraphManager","setNodeLabelColor", node, labelColor);
}

void makeNode(int& node, int x, int y, int color, int fillColor, int labelColor){
    ScriptManager::relay("HigraphManager","makeNode", node);
    moveNode(node, x, y);
    colorNode(node, color, fillColor, labelColor);
}

void labelNode(int& node, char* s){
	ScriptManager::relay("HigraphManager","setNodeLabel", node, s);
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

void drawLine(double x1, double y1, double x2, double y2){
    ScriptManager::relay("HigraphManager", "drawLine", x1, y1, x2, y2);
}

/* Constants and variables used by both the algorithm and the
instrumentation code */

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

int formula[ND][NV];

    /** The graph is represented by its adjacency matrix.*/
bool graph[NN][NN];

/* Constants and variables used by both the instrumentation code */

int p[NV];   // the array of positive variables - nodes 0, 2, .. 2*(NV-1)
int n[NV];   // the array of negative variables  n[i] = !p[i] - nodes 1, 3... 2*(NV-1) + 1

int f[ND];   // the array of first subTerms - nodes 2*NV, 2*NV+3... 2*NV + 3*(ND-1)
int s[ND];   // the array of second subTerms - nodes 2*NV+1, 2*NV+4... 2*NV + 3*(ND-1)+1
int t[ND];   // the array of third subTerms - nodes 2*NV+2, 2*NV+5... 2*NV + 3*(ND-1)+2


/*********** Instrumentation Methods *************************/


void setup(){
    ScriptManager::relay("HigraphManager", "makeView", "mainView", "wholeGraph","Higraph.WholeGraph", "EdgeOnly");
    ScriptManager::relay("HigraphManager", "setDefaultShowNodeName", 1);
    ScriptManager::relay("HigraphManager", "setTitle", "mainView", "Minimizing a Boolean formula in 3-CNF");
    ScriptManager::relay("HigraphManager", "setArbitraryString", "(X1 OR X2 OR X3) AND ((NOT X1) OR (NOT X2) OR X3) AND ((NOT X1) OR X2 OR (NOT X3))",  20, 20, BLACK);
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
}

void showAlternateNodeLabels(){
	int i;
	for (i = 0; i < NV; i++){
		labelNode(p[i], labels[2*i]);
		labelNode(n[i], labels[2*i + 1]);
	}
	
	for (i = 0; i < ND; i++){
		labelNode(f[i], labels[2*NV + 2*i]);
		labelNode(s[i], labels[2*NV + 2*i + 1]);
		labelNode(t[i], labels[2*NV + 2*i + 2]);
	}
}

void drawSomething(){
	drawLine(100, 25, 400, 25);
	drawLine(100, 30, 400, 30);
	drawLine(100, 35, 400, 35);
	drawLine(100, 25, 100, 35);
	drawLine(400, 25, 400, 35);
}


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

void createGraph() {
    for (int u = 0; u < NN; u++) {
        for (int v = 0; v < NN; v++) {
	    graph[u][v] = false;
        }
    }


// For each variable, add an edge between the two nodes corresponding to
// the variable: this implies to set the corresponding two elements in
// the adjacency matrix equal to true

    for (int v = 0; v < NV; v++) {
        int nodeId = 2 * v;
        graph[nodeId][nodeId + 1] = true;
        graph[nodeId + 1][nodeId] = true;
        makeEdge(mapNode(nodeId), mapNode(nodeId + 1), RED);
    }


// For each disjunction, add an edge between the three nodes
// corresponding to the disjunction: this implies to set the
// corresponding six elements in the adjacency matrix equal to true

    for (int c = 0; c < ND; c++) {
	    int nodeId = 2 * NV + 3 * c;
        graph[nodeId][nodeId + 1] = true;
        graph[nodeId + 1][nodeId] = true;
        makeEdge(mapNode(nodeId), mapNode(nodeId + 1), BLUE);
        graph[nodeId][nodeId + 2] = true;
        graph[nodeId + 2][nodeId] = true;
        makeEdge(mapNode(nodeId), mapNode(nodeId + 2), BLUE);
        graph[nodeId + 1][nodeId + 2] = true;
        graph[nodeId + 2][nodeId + 1] = true;
        makeEdge(mapNode(nodeId + 1), mapNode(nodeId + 2), BLUE);
    }

// For each disjunction and for each literal in the disjunction, add an
// edge between the two nodes corresponding to the disjunction element
// and the literal: this implies to set the corresponding two elements
// in the adjacency matrix equal to true

    for (int c = 0; c < ND; c++) {
        int nodeId = 2 * NV + 3 * c;
        for (int l = 0; l < 3; l++) {
            int v = formula[c][l];
            graph[nodeId + l][v] = true;
            graph[v][nodeId + l] = true;
            makeEdge(mapNode(nodeId + l), mapNode(v), GREEN);
        }
    }
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

void main( ) {
    setup();
    drawAllNodes(40, 40, 80, 200, 130);
    createFormula();
    createGraph();
    printGraph();
    makeLabels();
    showAlternateNodeLabels();
    drawSomething();
}
