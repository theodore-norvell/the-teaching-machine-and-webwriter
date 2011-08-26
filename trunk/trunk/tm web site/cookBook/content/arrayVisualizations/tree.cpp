#include <iostream>
/*#TS*/#include <ScriptManager>

const int WHITE = 0xffffff;
const int BLACK = 0x000000;
const int GREY = 0x808080;
const int RED = 0xff0000;
const int GREEN = 0x00ff00;
const int BLUE = 0x0000ff;

const int MARKER_RED = 0xffff;	
const int MARKER_BLUE = 0xfffe;	
const int MARKER_BLACK = 0xfffd;	
const int MARKER_GREEN = 0xfffc;
const int END_MARKER = 0xfff0;

// Boolean
const int TRUE = 1;
const int FALSE = 0;

// predefined node shapes
const int ELLIPSE = 0;
const int RECTANGLE = 1;
const int ROUND_RECTANGLE = 2;

//predefined point decorators
const int NONE = 0;
const int ARROWHEAD = 1;
const int CIRCLE = 2;

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

void setDefaultNodeShape(int s){
    ScriptManager::relay("HigraphManager","setDefaultNodeShape", s);
}

void setDefaultNodeColor(int c){
    ScriptManager::relay("HigraphManager","setDefaultNodeColor", c);
}

void setDefaultNodeFillColor(int c){
    ScriptManager::relay("HigraphManager","setDefaultNodeFillColor", c);	
}

void setDefaultShowNodeName(int show, int position){
    ScriptManager::relay("HigraphManager","setDefaultShowNodeName", show, position);	
}

void setDefaultNodeNameColor(int c){
    ScriptManager::relay("HigraphManager","setDefaultNodeNameColor", c);	
}

void setDefaultShowNodeValue(int show, int position){
    ScriptManager::relay("HigraphManager","setDefaultShowNodeValue", show, position);	
}

void setDefaultNodeValueColor(int c){
    ScriptManager::relay("HigraphManager","setDefaultNodeValueColor", c);	
}

void moveNode(int& node, int x, int y){
    ScriptManager::relay("HigraphManager","placeNode", node, x, y);
}

void colorNode(int& node, int color, int fillColor, int labelColor){
    ScriptManager::relay("HigraphManager","setNodeColor", node, color);
    ScriptManager::relay("HigraphManager","setNodeFillColor", node, fillColor);
    ScriptManager::relay("HigraphManager","setNodeLabelColor", node, labelColor);
}

void makeNode(int& node){
    ScriptManager::relay("HigraphManager","makeNode", node);
}

void makeNode(int& node, int x, int y, int color, int fillColor, int labelColor){
	ScriptManager::relay("HigraphManager","makeNode", node);
    moveNode(node, x, y);
    colorNode(node, color, fillColor, labelColor);
}

void addChild(int& node, int&child){
    ScriptManager::relay("HigraphManager","addChild", node, child);	
}

void makeSubGraph(char* s){
	ScriptManager::relay("HigraphManager","makeSubGraph", s);
}

void addTopNode(char* s, int& node){
	ScriptManager::relay("HigraphManager","addTopNode", s, node);
}

void setLayoutManager(int& node, char* lmName, char* graph){
    ScriptManager::relay("HigraphManager","setLayoutManager", node, lmName, graph);
}


void labelNode(int& node, char* s){
	ScriptManager::relay("HigraphManager","setNodeLabel", node, s);
}

void setNodeShape(int& node, int s){
    ScriptManager::relay("HigraphManager","setNodeShape", node, s);
}

void setDefaultEdgeColor(int color){
    ScriptManager::relay("HigraphManager","setDefaultEdgeColor", color);
}


void colorEdge(int& source, int& target, int color){
    ScriptManager::relay("HigraphManager", "setEdgeColor", source, target, color);
}

void makeEdge(int& source, int& target){
    ScriptManager::relay("HigraphManager","makeEdge", source, target);
}

void makeEdge(int& source, int& target, int color){
    ScriptManager::relay("HigraphManager","makeEdge", source, target);
    ScriptManager::relay("HigraphManager", "setEdgeColor", source, target, color);
}

void setDefaultTargetDecorator(int decorator){
    ScriptManager::relay("HigraphManager", "setDefaultTargetDecorator", decorator);	
}

void setDefaultSourceDecorator(int decorator){
    ScriptManager::relay("HigraphManager", "setDefaultSourceDecorator", decorator);	
}

void setDefaultParentDecorator(int decorator){
    ScriptManager::relay("HigraphManager", "setDefaultParentDecorator", decorator);	
}

void setDefaultChildDecorator(int decorator){
    ScriptManager::relay("HigraphManager", "setDefaultChildDecorator", decorator);	
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

void setup(){
	// These are empty subgraphs at this point but they allows visualizers to be defined
    makeSubGraph("oneTree");
    makeSubGraph("kidTree");
    
	// Make 3 visualizers for 3 different views
    ScriptManager::relay("HigraphManager", "makeView", "mainView", "wholeGraph","Higraph.WholeGraph", "SimpleForest");
    ScriptManager::relay("HigraphManager", "makeView", "kidTree",  "kidTree", "Higraph.KidTree", "SimpleTree");
    ScriptManager::relay("HigraphManager", "makeView", "nestedTree", "oneTree", "Higraph.NestedTree", "NestedTree");
    ScriptManager::relay("HigraphManager", "setTitle", "mainView", "A Forest of Trees");
    ScriptManager::relay("HigraphManager", "setTitle", "kidTree", "A Subtree of One Child");
    ScriptManager::relay("HigraphManager", "setTitle", "nestedTree", "A Nested View of the Forest");
    
    // These settings will apply to all visualizers
    setDefaultShowNodeValue(TRUE, CENTER);
    setDefaultNodeValueColor(WHITE);
    setDefaultShowNodeName(TRUE, SOUTHWEST);
    setDefaultNodeNameColor(BLACK);
    setDefaultTargetDecorator(ARROWHEAD);
    setDefaultChildDecorator(ARROWHEAD);
    setDefaultParentDecorator(CIRCLE);
}



void drawLine(double x1, double y1, double x2, double y2){
    ScriptManager::relay("HigraphManager", "drawLine", x1, y1, x2, y2);
}/*#/TS*/

const int ROOTS = 2;
const int KIDS = 2;
const int GKIDS = 3;
const int GGKIDS = 2;

int roots[ROOTS];
int kids[KIDS*ROOTS];
int grandKids[GKIDS*GGKIDS*ROOTS];
int greatGrandKids[GGKIDS*GKIDS*KIDS*ROOTS];

void buildTree(){
     for (int i = 0; i < ROOTS; i++){
         roots[i] = -1;
         makeNode(roots[i]);
         for (int j = 0; j < KIDS; j++){
             kids[i*KIDS+j] = i;
             addChild(roots[i], kids[KIDS*i+j]);
             for (int k = 0; k < GKIDS; k++){
                 grandKids[(KIDS*i+j)*GKIDS+k] = i*KIDS + j;
                 addChild(kids[KIDS*i+j], grandKids[(KIDS*i+j)*GKIDS +k]);
		 for (int l = 0; l < GGKIDS; l++){
                     greatGrandKids[((KIDS*i +j)*GKIDS + k)*GGKIDS +l] = (KIDS*i +j)*GKIDS + k;
                     addChild(grandKids[(KIDS*i+j)*GKIDS + k], greatGrandKids[((KIDS*i+j)*GKIDS +k)*GGKIDS+l]);
                 }
             }
         }
     }
     makeEdge(kids[1], kids[1]);
     makeEdge(kids[1], kids[3]);
     makeEdge(kids[3], grandKids[1]);
     makeEdge(grandKids[4], grandKids[5]);
     makeEdge(roots[1], grandKids[9]);
     addTopNode("oneTree", roots[1]);
     addTopNode("kidTree", kids[GGKIDS]);
 //    setLayoutManager(kids[GGKIDS], "NestedTree", "wholeGraph");

}

int main(){
	setup();
    buildTree();
    /* Make three views as follows:
              a view called "simpleTree" of the Higraph "oneTree" to be displayed in the visualizer "Higraph.OneTree"
              using the layoutManager "SimpleTree"
              
              a view called "boxedTree" of the same Higraph "oneTree" to be displayed in the visualizer "Higraph.OneTreeBoxed"
              using the layoutManager "BoxedTree"
             
             a view called "kidTree" of the Higraph "kidTree" to be displayed in the visualizer "Higraph.KidTree"
              using the layoutManager "SimpleTree"
              
              Notes:
                    1. visualizers are TM windows in which views are shown and are prespecified in the configuration file
                    GGKIDS. layoutManagers determine how a view is presented
                    3. Once a view is created, the view name is used to manipulate it. (Aside: since views are 1:1 with
                       visualizers, we could use the same name - but it could cause confusion down the line; moreover
                       visualizer names are bound by configId naming conventions).
                    GKIDS. Newly made views are automatically added to the activeViewSet
*/
    
    /* Do something to active views */
    
    
    ScriptManager::relay("HigraphManager", "setCountLabels", FALSE);
    ScriptManager::relay("HigraphManager", "setCountZones", FALSE);

    ScriptManager::relay("HigraphManager", "removeFromActiveViewSet", "kidTree");
    ScriptManager::relay("HigraphManager", "removeFromActiveViewSet", "mainView");
    for (int i = 0; i < GGKIDS*GKIDS*KIDS*ROOTS; i++)
    	ScriptManager::relay("HigraphManager", "setNodeValueColor", greatGrandKids[i], RED);
   

 /*     Note: Might also want convenience method to set parameters on a particular view.
*/
    
    
    return 0;
}
             
     
