/*#TS*/#include <ScriptManager>

#include<stdlib.h>

int temp;
const int WHITE = 0xffffff;
const int BLACK = 0x000000;
const int GREY = 0x808080;
const int RED = 0xff0000;
const int GREEN = 0x00ff00;
const int BLUE = 0x0000ff;
const int TRANSPARENT = -1;
const int AQUA = 0x00ffff;
const int MAGENTA = 0x660000;
const int YELLOW = 0xffff00;
const int LIGHT_GREEN = 0x33FF66;
const int BEIGE = 0xFFCC99;

const int MARKER_RED = 0xffff;	
const int MARKER_BLUE = 0xfffe;	
const int MARKER_BLACK = 0xfffd;	
const int MARKER_GREEN = 0xfffc;
const int END_MARKER = 0xfff0;

int color_lookup(int a)
{
	switch (a)
	{
		case 0: return LIGHT_GREEN;
		case 1: return BEIGE;
	}
}

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

void setDefaultNodeNameShow(bool show, int position){
    ScriptManager::relay("HigraphManager","setDefaultNodeNameShow", show, position);	
}

void setDefaultNodeNameColor(int c){
    ScriptManager::relay("HigraphManager","setDefaultNodeNameColor", c);	
}

void setDefaultNodeValueShow(bool show, int position){
    ScriptManager::relay("HigraphManager","setDefaultNodeValueShow", show, position);	
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


void setNodeLabel(int& node, char* s){
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

void setCountLabels(bool val){
    ScriptManager::relay("HigraphManager", "setCountLabels", val);
}

void setCountZones(bool val){
    ScriptManager::relay("HigraphManager", "setCountZones", val);
}

void removeFromActiveViewSet(char* view){
    ScriptManager::relay("HigraphManager", "removeFromActiveViewSet", view);
}

void setNodeValueColor(int& node,long color){
    ScriptManager::relay("HigraphManager", "setNodeValueColor", node, color);
}

//void makeView(char* viewId, char* hgId, char* plugIn){
//    makeView(viewId, hgId, plugIn, "");
//}

void makeView(char* viewId, char* hgId, char* plugIn, char* plugInLayout){
    ScriptManager::relay("HigraphManager", "makeView", viewId, hgId, plugIn, plugInLayout);
}

/*void makeArray(int array[], int length){
    makeArray(array, length, true, "");
}
	
void makeArray(int array[], int length, bool showCells){
    makeArray(array, length, showCells, "");
}
	
void makeArray(int array[], int length, char* name){
    makeArray(array, length, true, name);
}
*/	
/*void makeArray(int array[], int rows, int cols, bool showCells, char* name){
	int length = rows*cols;
    ScriptManager::relay("HigraphManager","makeNode", array);
    ScriptManager::relay("HigraphManager", "placeNode", array, 350, 0);
    ScriptManager::relay("HigraphManager","setNodeNamePosition", array, SOUTH);
    if (strcmp(name,"")!=0)
      	ScriptManager::relay("HigraphManager","setNodeNameLabel", array, name);
    ScriptManager::relay("HigraphManager","setNodeValueShow", array, false);
    ScriptManager::relay("HigraphManager","setNodeLayoutManager", array, "VertNestedTree");
	char s[6];
	int k,j,l;
    for (int i = 0; i < length; i++){
            ScriptManager::relay("HigraphManager","makeNode", array[i]);
			char temp[10];
			j=0;l=i;
			s[0]='(';
			while(l/10!=0)
			{
				temp[j++]=(char)(48+(l%10));
				l=l/10;
			}
			s[1]=(char)(48+l);
			
			for(k=1;k<=j;k++)
			{
				s[k+1]=temp[j-k];
			}
			s[k+1]=')';
			s[k+2]='\0';
				ScriptManager::relay("HigraphManager","setNodeNameLabel", array[i], s);
            ScriptManager::relay("HigraphManager", "dislocate", array[i], 2, 0);
            if(!showCells){
                ScriptManager::relay("HigraphManager","setNodeColor", array[i], TRANSPARENT);
                ScriptManager::relay("HigraphManager","setNodeFillColor", array[i], TRANSPARENT);
            }
			else
			{
				ScriptManager::relay("HigraphManager","setNodeFillColor", array[i], color_lookup(i/cols));
			}
           	ScriptManager::relay("HigraphManager","setNodeNamePosition", array[i], SOUTH);
            ScriptManager::relay("HigraphManager", "setNodeShape", array[i], RECTANGLE);
            ScriptManager::relay("HigraphManager","addChild", array, array[i]);
			array[i]=(rand()%1479)+1;
     	}
   }*/
	
void makeArray(double array[], int rows, int cols, bool showCells, char* name, int x, int y){
	int length = rows*cols;
    ScriptManager::relay("HigraphManager","makeNode", array);
    ScriptManager::relay("HigraphManager", "placeNode", array, x, y);
    ScriptManager::relay("HigraphManager","setNodeNamePosition", array, SOUTH);
    if (strcmp(name,"")!=0)
      	ScriptManager::relay("HigraphManager","setNodeNameLabel", array, name);
    ScriptManager::relay("HigraphManager","setNodeValueShow", array, false);
    ScriptManager::relay("HigraphManager","setNodeLayoutManager", array, "VertNestedTree");
	char s[6];
	int k,j,l;
    for (int i = 0; i < length; i++){
            ScriptManager::relay("HigraphManager","makeNode", array[i]);
			char temp[10];
			j=0;l=i;
			s[0]='(';
			while(l/10!=0)
			{
				temp[j++]=(char)(48+(l%10));
				l=l/10;
			}
			s[1]=(char)(48+l);
			
			for(k=1;k<=j;k++)
			{
				s[k+1]=temp[j-k];
			}
			s[k+1]=')';
			s[k+2]='\0';
				ScriptManager::relay("HigraphManager","setNodeNameLabel", array[i], s);
            ScriptManager::relay("HigraphManager", "dislocate", array[i], 2, 0);
            if(!showCells){
                ScriptManager::relay("HigraphManager","setNodeColor", array[i], TRANSPARENT);
                ScriptManager::relay("HigraphManager","setNodeFillColor", array[i], TRANSPARENT);
            }
			else
			{
				ScriptManager::relay("HigraphManager","setNodeFillColor", array[i], color_lookup(i/cols));
			}
           	ScriptManager::relay("HigraphManager","setNodeNamePosition", array[i], SOUTH);
            ScriptManager::relay("HigraphManager", "setNodeShape", array[i], RECTANGLE);
            ScriptManager::relay("HigraphManager","addChild", array, array[i]);
			array[i]=((rand()%9999));
     	}
   }
	
/*void makeMatrix(int* matrix[], int length, int width){
    makeMatrix(matrix, length, width, true, "");
}
    
void makeMatrix(int* matrix[], int length, int width, bool showCells){
    makeMatrix(matrix, length, width, showCells, "");
}

void makeMatrix(int* matrix[],int length,  int width, char* name){
    makeMatrix(matrix, length, width, true, name);
}    
*/

char* convert(int num)
{
    char temp[10];char s[10];
    int i=0;
    while(num/10!=0)
    {
        temp[i++]=(char)(48+(num%10));
        num=num/10;
    }
    s[0]=(char)(48+num);
    int j;
    for(j=1;j<i;j++)
    {
        s[j]=temp[i-j];
    }
    s[j]='\0';
	return s;
}

/*void makeMatrix(int** matrix, int length, int width, bool showCells, char* name){
     ScriptManager::relay("HigraphManager","makeNode", matrix);
	 ScriptManager::relay("HigraphManager", "placeNode", matrix, 25, 100);
     ScriptManager::relay("HigraphManager","setNodeValueShow", matrix, false);
     ScriptManager::relay("HigraphManager","setNodeNameLabel", matrix, name);
     ScriptManager::relay("HigraphManager","setNodeLayoutManager", matrix, "VertNestedTree");
     ScriptManager::relay("HigraphManager", "setNodeShape", matrix, RECTANGLE);
     ScriptManager::relay("HigraphManager","setNodeNamePosition", matrix, SOUTH);
	 
	 int i,j;
	 char *name2 = new char[10];
	 for(i = 0; i < length; i++)
     {
        
        
        ScriptManager::relay("HigraphManager","makeNode", matrix[i]);
		ScriptManager::relay("HigraphManager","addChild", matrix, matrix[i]);
        ScriptManager::relay("HigraphManager","dislocate", matrix[i], 2, 0);
		//ScriptManager::relay("HigraphManager", "setNodeShape", matrix[i], RECTANGLE);
        ScriptManager::relay("HigraphManager", "setNodeColor", matrix[i], WHITE);
        ScriptManager::relay("HigraphManager","setNodeNameShow", matrix[i], false);
        ScriptManager::relay("HigraphManager","setNodeNamePosition", matrix[i], CENTER);
        ScriptManager::relay("HigraphManager","setNodeValueShow", matrix[i], false);
        ScriptManager::relay("HigraphManager","setNodeLayoutManager", matrix[i], "HorizNestedTree");
        
        
         
        
            for(j = 0; j < width; j++)
            {
                ScriptManager::relay("HigraphManager","makeNode", matrix[i][j]);
                /*if( !showCells){
                    ScriptManager::relay("HigraphManager","setNodeColor", matrix[i][j], TRANSPARENT);
                    ScriptManager::relay("HigraphManager","setNodeFillColor", matrix[i][j], TRANSPARENT);
                    }*//*
				name2[0]="(";
				name2[1]=(char)(48+i);
				name2[2]=",";
				name2[3]=(char)(48+j);
				name2[4]=")";
				name2[5]="\0";
				ScriptManager::relay("HigraphManager","setNodeNameLabel", matrix[i][j], name2);
				ScriptManager::relay("HigraphManager","setNodeNameShow", matrix[i][j], true);
                ScriptManager::relay("HigraphManager","setNodeNamePosition", matrix[i][j], SOUTH);
                ScriptManager::relay("HigraphManager","setNodeValueShow", matrix[i][j], true);
                ScriptManager::relay("HigraphManager","setNodeValuePosition", matrix[i][j], CENTER );
                ScriptManager::relay("HigraphManager", "setNodeFillColor", matrix[i][j], color_lookup(i));
                ScriptManager::relay("HigraphManager","addChild", matrix[i], matrix[i][j]);
				//ScriptManager::relay("HigraphManager","dislocate", matrix[i][j], 2, 0);
                /*if( i == 0 ) {
                    ScriptManager::relay("HigraphManager", "createNodeExtraLabel", matrix[i][j], "column", NORTH ) ;
                    char* jString =  "";
                    convert(j,jString);
                    cout<<jString;
                    string work (jString);
                    ScriptManager::relay("HigraphManager", "setNodeExtraLabel", matrix[i][j], "column", work ) ;}
                if( j == 0 ) {
                ScriptManager::relay("HigraphManager", "createNodeExtraLabel", matrix[i][j], "row", WEST ) ;
                char* iString;
                convert(i,iString);
                ScriptManager::relay("HigraphManager", "setNodeExtraLabel", matrix[i][j], "row", iString ) ;}*/
            /*}
   }
}*/

void makeMatrix(double** matrix, int length, int width, bool showCells, char* name){
     ScriptManager::relay("HigraphManager","makeNode", matrix);
	 ScriptManager::relay("HigraphManager", "placeNode", matrix, 25, 100);
     ScriptManager::relay("HigraphManager","setNodeValueShow", matrix, false);
     ScriptManager::relay("HigraphManager","setNodeNameLabel", matrix, name);
     ScriptManager::relay("HigraphManager","setNodeLayoutManager", matrix, "VertNestedTree");
     ScriptManager::relay("HigraphManager", "setNodeShape", matrix, RECTANGLE);
     ScriptManager::relay("HigraphManager","setNodeNamePosition", matrix, SOUTH);
	 
	 int i,j;
	 char *name2 = new char[10];
	 for(i = 0; i < length; i++)
     {
        
        
        ScriptManager::relay("HigraphManager","makeNode", matrix[i]);
		ScriptManager::relay("HigraphManager","addChild", matrix, matrix[i]);
        ScriptManager::relay("HigraphManager","dislocate", matrix[i], 2, 0);
		//ScriptManager::relay("HigraphManager", "setNodeShape", matrix[i], RECTANGLE);
        ScriptManager::relay("HigraphManager", "setNodeColor", matrix[i], WHITE);
        ScriptManager::relay("HigraphManager","setNodeNameShow", matrix[i], false);
        ScriptManager::relay("HigraphManager","setNodeNamePosition", matrix[i], CENTER);
        ScriptManager::relay("HigraphManager","setNodeValueShow", matrix[i], false);
        ScriptManager::relay("HigraphManager","setNodeLayoutManager", matrix[i], "HorizNestedTree");
        
        
         
        
            for(j = 0; j < width; j++)
            {
                ScriptManager::relay("HigraphManager","makeNode", matrix[i][j]);
                /*if( !showCells){
                    ScriptManager::relay("HigraphManager","setNodeColor", matrix[i][j], TRANSPARENT);
                    ScriptManager::relay("HigraphManager","setNodeFillColor", matrix[i][j], TRANSPARENT);
                    }*/
				name2[0]="(";
				name2[1]=(char)(48+i);
				name2[2]=",";
				name2[3]=(char)(48+j);
				name2[4]=")";
				name2[5]="\0";
				ScriptManager::relay("HigraphManager","setNodeNameLabel", matrix[i][j], name2);
				ScriptManager::relay("HigraphManager","setNodeNameShow", matrix[i][j], true);
                ScriptManager::relay("HigraphManager","setNodeNamePosition", matrix[i][j], SOUTH);
                ScriptManager::relay("HigraphManager","setNodeValueShow", matrix[i][j], true);
                ScriptManager::relay("HigraphManager","setNodeValuePosition", matrix[i][j], CENTER );
                ScriptManager::relay("HigraphManager", "setNodeFillColor", matrix[i][j], color_lookup(i));
                ScriptManager::relay("HigraphManager","addChild", matrix[i], matrix[i][j]);
				//ScriptManager::relay("HigraphManager","dislocate", matrix[i][j], 2, 0);
                /*if( i == 0 ) {
                    ScriptManager::relay("HigraphManager", "createNodeExtraLabel", matrix[i][j], "column", NORTH ) ;
                    char* jString =  "";
                    convert(j,jString);
                    cout<<jString;
                    string work (jString);
                    ScriptManager::relay("HigraphManager", "setNodeExtraLabel", matrix[i][j], "column", work ) ;}
                if( j == 0 ) {
                ScriptManager::relay("HigraphManager", "createNodeExtraLabel", matrix[i][j], "row", WEST ) ;
                char* iString;
                convert(i,iString);
                ScriptManager::relay("HigraphManager", "setNodeExtraLabel", matrix[i][j], "row", iString ) ;}*/
            }
    }
	ScriptManager::relay("HigraphManager","makeNode", temp);
	ScriptManager::relay("HigraphManager","addChild", temp, matrix);
    ScriptManager::relay("HigraphManager","setNodeColor", temp, TRANSPARENT);
    ScriptManager::relay("HigraphManager","setNodeNameShow", temp, false);
    ScriptManager::relay("HigraphManager","setNodeValueShow", temp, false);
	
}

void setTitle(char* viewId, char* title){
    ScriptManager::relay("HigraphManager", "setTitle", viewId, title);
}

void drawLine(double x1, double y1, double x2, double y2){
    ScriptManager::relay("HigraphManager", "drawLine", x1, y1, x2, y2);
}
    
void setupval(double& val){
	 ScriptManager::relay("HigraphManager","makeNode", val);
	 ScriptManager::relay("HigraphManager","placeNode", val, 5, 0);
	 //ScriptManager::relay("HigraphManager","setNodeSize", 40, 40);
     ScriptManager::relay("HigraphManager","setNodeValueShow", val, true);
	 ScriptManager::relay("HigraphManager","setNodeNameLabel", val, "val");
	 ScriptManager::relay("HigraphManager","setNodeNameNudge",val,-10,0);
	 //ScriptManager::relay("HigraphManager","setNodeColor", val, BLACK);
     //ScriptManager::relay("HigraphManager","setNodeFillColor", val, YELLOW);
     //ScriptManager::relay("HigraphManager", "setNodeShape", val, ELLIPSE);
     //ScriptManager::relay("HigraphManager","setNodeNamePosition", val, WEST);
}	 

void setup_i_j(int& i,int& j)
{

	 ScriptManager::relay("HigraphManager","makeNode", i);
	 ScriptManager::relay("HigraphManager","placeNode", i, 105,0);
	 //ScriptManager::relay("HigraphManager","setNodeSize", 40, 40);
     ScriptManager::relay("HigraphManager","setNodeValueShow", i, true);
	 ScriptManager::relay("HigraphManager","setNodeNameLabel", i, "i");
	 ScriptManager::relay("HigraphManager","setNodeNameNudge",i,-10,0);
	 i=rand()%1249;
     //ScriptManager::relay("HigraphManager","setNodeShape", i, ELLIPSE);
	 //ScriptManager::relay("HigraphManager","setNodeColor", i, BLACK);
     //ScriptManager::relay("HigraphManager","setNodeFillColor", i, YELLOW);
     //ScriptManager::relay("HigraphManager","setNodeNamePosition", i, WEST);
	 
	 
	 ScriptManager::relay("HigraphManager","makeNode", j);
	 ScriptManager::relay("HigraphManager","placeNode", j, 205, 0);
	 //ScriptManager::relay("HigraphManager","setNodeSize", 40, 40);
     ScriptManager::relay("HigraphManager","setNodeValueShow", j, true);
	 ScriptManager::relay("HigraphManager","setNodeNameLabel", j, "j");
	 ScriptManager::relay("HigraphManager","setNodeNameNudge",j,-10,0);
	 //ScriptManager::relay("HigraphManager","setNodeColor", j, BLACK);
     //ScriptManager::relay("HigraphManager","setNodeFillColor", j, YELLOW);
     //ScriptManager::relay("HigraphManager", "setNodeShape", j, ELLIPSE);
	 //ScriptManager::relay("HigraphManager","setNodeNamePosition", j, WEST);
	 j=rand()%3230;
	 
}
/*#/TS*/