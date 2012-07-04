#include <iostream>
#include<string>
/*#TS*/
#include"PDV_final.h"
/*#/TS*/


int main()
{
     /*#TS*/
     makeView("mainView", "wholeGraph","Higraph.PDV", "PlacedNode");
     setTitle("mainView", "Matrices PDV");
     setDefaultNodeValueShow(true, CENTER);
     setDefaultNodeValueColor(MARKER_RED);
     setDefaultNodeShape(RECTANGLE);
	 //ScriptManager::relay("HigraphManager", "setDefaultZoneColor", BLUE);
     ScriptManager::relay("HigraphManager", "setDefaultNodeFillColor", WHITE);
     ScriptManager::relay("HigraphManager", "setDefaultNodeValueShow", true, CENTER);
     ScriptManager::relay("HigraphManager", "setDefaultNodeValueColor", BLACK);
     ScriptManager::relay("HigraphManager", "setDefaultNodeNameShow", true, EAST);
     ScriptManager::relay("HigraphManager", "setDefaultNodeNameColor", MAGENTA);
	 ScriptManager::relay("HigraphManager","setDefaultNodeSize", 70, 70);
       
     /*#/TS*/
     const int rows = 3;
     const int cols = 4;
	 int val = 10;/*#TS*/ //setupval(val);
	 /*#/TS*/
     
     int matrix[rows*cols];
     /*#TS*/makeArray(matrix,(rows*cols),true,"Actual array");/*#/TS*/
	 int i,j;/*#TS*//*nodes for i & j*///setup_i_j(i,j);
	 ScriptManager::relay("HigraphManager","makeNode", i);
	 ScriptManager::relay("HigraphManager","dislocate", i, 360, 120);
     ScriptManager::relay("HigraphManager","setNodeValueShow", i, true);
	 ScriptManager::relay("HigraphManager","setNodeNameLabel", i, "i");
	 ScriptManager::relay("HigraphManager","setNodeNameNudge",i,-10,0);
     ScriptManager::relay("HigraphManager","setNodeShape", i, ELLIPSE);
	 ScriptManager::relay("HigraphManager","setNodeColor", i, BLACK);
     ScriptManager::relay("HigraphManager","setNodeFillColor", i, YELLOW);
     ScriptManager::relay("HigraphManager","setNodeNamePosition", i, WEST);
	 
	 ScriptManager::relay("HigraphManager","makeNode", j);
	 ScriptManager::relay("HigraphManager","dislocate", j, 500, 120);
     ScriptManager::relay("HigraphManager","setNodeValueShow", j, true);
	 ScriptManager::relay("HigraphManager","setNodeNameLabel", j, "j");
	 ScriptManager::relay("HigraphManager","setNodeNameNudge",j,-10,0);
	 ScriptManager::relay("HigraphManager","setNodeColor", j, BLACK);
     ScriptManager::relay("HigraphManager","setNodeFillColor", j, YELLOW);
     ScriptManager::relay("HigraphManager", "setNodeShape", j, ELLIPSE);
     ScriptManager::relay("HigraphManager","setNodeNamePosition", j, WEST);/*#/TS*/
	 
	 /*#TS*/int matrix1[rows][cols];int **mat = new int*[rows];
     for(int k=0;k<rows;k++)
        mat[k]=matrix1[k];
     makeMatrix(mat,rows,cols,true,"Theoretical matrix represenation");/*#/TS*/
	 
     for(i = 0; i < rows; i++)
     {
        for(j = 0; j < cols; j++)
            {	/*#TS*/ScriptManager::relay("HigraphManager","setNodeFillColor", matrix1[i][j], YELLOW);
					ScriptManager::relay("HigraphManager","setNodeFillColor", matrix[(i*cols)+j], YELLOW);/*#/TS*/
				/*#TS*/	matrix1[i][j]=val;/*#/TS*/
                matrix[(i*cols)+j]=val++;
				/*#TS*/ScriptManager::relay("HigraphManager","setNodeFillColor", matrix1[i][j], WHITE);
					ScriptManager::relay("HigraphManager","setNodeFillColor", matrix[(i*cols)+j], WHITE);/*#/TS*/
			}
     }
     
     /*#TS*/
     //ScriptManager::relay("HigraphManager","placeNode", testm, false, 0, 0);
     /*#/TS*/
     return 0;
}