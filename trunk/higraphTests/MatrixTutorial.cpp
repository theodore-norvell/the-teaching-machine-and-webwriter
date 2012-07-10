#include <iostream>
#include<string>
/*#TS*/
#include"PDV_final.h"
/*#/TS*/

void add1(int func_matrix[], int rows, int columns);

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
	 ScriptManager::relay("HigraphManager","setDefaultNodeSize", 50, 50);
       
     /*#/TS*/
     const int rows = 2;
     const int cols = 3;
	 int val = 10;/*#TS*/ setupval(val);
	 /*#/TS*/
     
     int matrix[rows*cols];
     /*#TS*/makeArray(matrix,(rows*cols),true,"matrix[]");/*#/TS*/
	 int i,j;/*#TS*//*nodes for i & j*/setup_i_j(i,j);/*#/TS*/
	 
	 /*#TS*/int **mat = new int*[rows];
     for(int k=0;k<rows;k++)
        mat[k]=new int[cols];
     makeMatrix(mat,rows,cols,true,"Theoretical representation");/*#/TS*/
	 
     for(i = 0; i < rows; i++)
     {
        for(j = 0; j < cols; j++)
            {	/*#TS*/ScriptManager::relay("HigraphManager","setNodeFillColor", mat[i][j], YELLOW);
					ScriptManager::relay("HigraphManager","setNodeFillColor", matrix[(i*cols)+j], YELLOW);/*#/TS*/
				/*#TS*/	mat[i][j]=val;/*#/TS*/
                matrix[(i*cols)+j]=val++;
				/*#TS*/ScriptManager::relay("HigraphManager","setNodeFillColor", mat[i][j], WHITE);
					ScriptManager::relay("HigraphManager","setNodeFillColor", matrix[(i*cols)+j], WHITE);/*#/TS*/
			}
     }
	 
	 //Call module to add 1 to each element of matrix
	
	 add1(matrix, rows, cols);
	 
	 //See that the values of the original matrix have changed
     
     /*#TS*/
     /*#/TS*/
     return 0;
}

void add1(int func_matrix[], int rows, int columns)
{
	/* char name[15]="func_matrix";
	ScriptManager::relay("HigraphManager","createString",name); 
	ScriptManager::relay("HigraphManager","setStringBaseColor",name,MAGENTA);
	*/
	
	/*#TS*///ScriptManager::relay("HigraphManager","setNodeNameLabel", matrix, "func_matrix");
    /*#/TS*/
		
	int i,j;
	for(i=0;i<rows;i++)
	{
		for(j=0;j<columns;j++)
		{
			func_matrix[(i*columns)+j]++;
		}
	}
}