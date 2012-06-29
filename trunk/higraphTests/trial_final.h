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
     ScriptManager::relay("HigraphManager", "setDefaultNodeFillColor", BLUE);
     ScriptManager::relay("HigraphManager", "setDefaultNodeValueShow", true, CENTER);
     ScriptManager::relay("HigraphManager", "setDefaultNodeValueColor", WHITE);
     ScriptManager::relay("HigraphManager", "setDefaultNodeNameShow", true, EAST);
     ScriptManager::relay("HigraphManager", "setDefaultNodeNameColor", BLUE);
       
     /*#/TS*/
     const int rows = 2;
     const int cols = 3;
     int matrix1[rows][cols];/*#TS*/int **mat = new int*[rows];
     for(int i=0;i<rows;i++)
        mat[i]=matrix1[i];
     makeMatrix(mat,rows,cols,true,"matrix1");/*#/TS*/
     int matrix2[rows*cols];
     /*#TS*/makeArray(matrix2,(rows*cols),true,"matrix2");/*#/TS*/
     for(int i = 0; i < rows; i++)
     {
        for(int j = 0; j < cols; j++)
            {
                matrix1[i][j]=i+j;
                
            }
     }
     
     for(int i = 0; i < rows; i++)
     {
        for(int j = 0; j < cols; j++)
            {
                matrix2[(i*cols)+j]=i+j;
                
            }
     }
     
     /*#TS*/
     //ScriptManager::relay("HigraphManager","placeNode", testm, false, 0, 0);
     /*#/TS*/
     return 0;
}