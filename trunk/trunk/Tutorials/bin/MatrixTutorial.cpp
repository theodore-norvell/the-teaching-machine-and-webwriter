#include <iostream>
#include<string>
/*#TS*/
#include"PDV_final.h"
double **mat;
int *p1, *p2;
/*#/TS*/

void scale(double B[], int rows, int cols, double factor);

int main()
{    /*#TS*/
     makeView("mainView", "wholeGraph","Higraph.PDV", "PlacedNode");
     setTitle("mainView", "Matrices PDV");
     setDefaultNodeValueShow(true, CENTER);
     setDefaultNodeValueColor(BLACK);
     setDefaultNodeShape(RECTANGLE);
	 ScriptManager::relay("HigraphManager", "setDefaultNodeFillColor", WHITE);
     ScriptManager::relay("HigraphManager", "setDefaultNodeNameShow", true, WEST);
     ScriptManager::relay("HigraphManager", "setDefaultNodeNameColor", MAGENTA);
	 ScriptManager::relay("HigraphManager","setDefaultNodeSize", 40, 40);
       
     /*#/TS*/
     const int rows = 2;
     const int columns = 3;
	 double val = 10.0;/*#TS*/ setupval(val);
	 /*#/TS*/
     
     double A[rows*columns];/*#TS*/makeArray(A,rows,columns,true,"A[]", 320, 0);/*#/TS*/
     
	 int i,j;/*#TS*//*nodes for i & j*/setup_i_j(i,j);/*#/TS*//*#TS*/mat = new double*[rows];
     for(int k=0;k<rows;k++)
        mat[k]=new double[columns];
     makeMatrix(mat,rows,columns,true,"Matrix Form of A[]");
	 for(int k=0;k<rows;k++)
		for(int l=0;l<columns;l++)
			mat[k][l]=A[(k*columns)+l];/*#/TS*/
	 for(i = 0; i < rows; i++)
     {
        for(j = 0; j < columns; j++)
            {	/*#TS*/ScriptManager::relay("HigraphManager","setNodeFillColor", mat[i][j], YELLOW);
					ScriptManager::relay("HigraphManager","setNodeFillColor", A[(i*columns)+j], YELLOW);/*#/TS*/
				/*#TS*/	mat[i][j]=val;/*#/TS*/
                A[(i*columns)+j]=val;
				val+=1.5;/*#TS*/ScriptManager::relay("HigraphManager","setNodeFillColor", mat[i][j], color_lookup(i));
					ScriptManager::relay("HigraphManager","setNodeFillColor", A[(i*columns)+j], color_lookup(i));/*#/TS*/
			}
     }
	 
	 double scale_factor = .25;
	 /*#TS*/ScriptManager::relay("HigraphManager","makeNode", scale_factor);
	 ScriptManager::relay("HigraphManager","placeNode", scale_factor, 5, 320);
	 ScriptManager::relay("HigraphManager","setNodeValueShow", scale_factor, true);
	 ScriptManager::relay("HigraphManager","setNodeNameLabel", scale_factor, "scale_factor");
	 ScriptManager::relay("HigraphManager","setNodeNameNudge",scale_factor,-5,0);
	 /*#/TS*//*#TS*/p1=&i;p2=&j;/*#/TS*/
	 scale(A, rows, columns, scale_factor);
	   /*#TS*/ //UnHiding  i  and j 
	 ScriptManager::relay("HigraphManager","setNodeValueShow",i, true);
	 ScriptManager::relay("HigraphManager","setNodeNameShow", i, true);
	 ScriptManager::relay("HigraphManager","setNodeColor", i, BLACK);
	 ScriptManager::relay("HigraphManager","setNodeValueShow",j, true);
	 ScriptManager::relay("HigraphManager","setNodeNameShow", j, true);
	 ScriptManager::relay("HigraphManager","setNodeColor", j, BLACK);/*#/TS*/
	 double C[2*2];
	 /*#TS*/makeArray(C,2,2,true,"C[]", 400, 0);
	 ScriptManager::relay("HigraphManager","deleteNode", temp);
	 delete[] mat;
	 mat = new double*[2];
     for(int k=0;k<2;k++)
        mat[k]=new double[2];
     makeMatrix(mat,2,2,true,"Matrix Form of C[]");
	 for(int k=0;k<2;k++)
		for(int l=0;l<2;l++)
			mat[k][l]=C[(k*2)+l];/*#/TS*/
	 for(i = 0; i < 2; i++)
     {
        for(j = 0; j < 2; j++)
            {	/*#TS*/ScriptManager::relay("HigraphManager","setNodeFillColor", mat[i][j], YELLOW);ScriptManager::relay("HigraphManager","setNodeFillColor", C[(i*2)+j], YELLOW);/*#/TS*/ /*#TS*/	mat[i][j]=val;/*#/TS*/
                C[(i*2)+j]=val;
				val+=1.5; /*#TS*/ScriptManager::relay("HigraphManager","setNodeFillColor", mat[i][j], color_lookup(i));
					ScriptManager::relay("HigraphManager","setNodeFillColor", C[(i*2)+j], color_lookup(i));/*#/TS*/
			}
     }
	 
	 scale(C, 2, 2, scale_factor);  /*#TS*/ //UnHiding  i  and j 
	 ScriptManager::relay("HigraphManager","setNodeValueShow",i, true);
	 ScriptManager::relay("HigraphManager","setNodeNameShow", i, true);
	 ScriptManager::relay("HigraphManager","setNodeColor", i, BLACK);
	 ScriptManager::relay("HigraphManager","setNodeValueShow",j, true);
	 ScriptManager::relay("HigraphManager","setNodeNameShow", j, true);
	 ScriptManager::relay("HigraphManager","setNodeColor", j, BLACK);/*#/TS*/ /*#TS*/  ScriptManager::relay("HigraphManager","deleteNode", temp); /*#/TS*/
	 return 0;
}

void scale(double B[], int rows, int cols, double scale_factor)
{	/*#TS*/ //Hiding  i  and j for now
	 ScriptManager::relay("HigraphManager","setNodeValueShow",p1, true, false);
	 ScriptManager::relay("HigraphManager","setNodeNameShow", p1, true, false);
	 ScriptManager::relay("HigraphManager","setNodeColor", p1, true, TRANSPARENT);
	 ScriptManager::relay("HigraphManager","setNodeValueShow",p2, true,  false);
	 ScriptManager::relay("HigraphManager","setNodeNameShow", p2, true, false);
	 ScriptManager::relay("HigraphManager","setNodeColor", p2, true, TRANSPARENT);
	int name;
	int target;
	ScriptManager::relay("HigraphManager","makeNode",name); 
	ScriptManager::relay("HigraphManager","setNodeColor",name,WHITE);
	ScriptManager::relay("HigraphManager","setNodeValueShow",name,false);
	ScriptManager::relay("HigraphManager","setNodeNamePosition",name,CENTER);
	ScriptManager::relay("HigraphManager","setNodeNameLabel",name,"B[]");
	ScriptManager::relay("HigraphManager","placeNode",name,400,380);
	ScriptManager::relay("HigraphManager","makeNode",target); 
	ScriptManager::relay("HigraphManager","setNodeSize", target, 1, 1);
	ScriptManager::relay("HigraphManager","setNodeColor",target,WHITE);
	ScriptManager::relay("HigraphManager","setNodeNameShow",target,false);
	ScriptManager::relay("HigraphManager","setNodeValueShow",target,false);
	if(cols==3)
		ScriptManager::relay("HigraphManager","placeNode",target,320,367);
	else
		ScriptManager::relay("HigraphManager","placeNode",target,389,260);
	ScriptManager::relay("HigraphManager","makeEdge", name, target);
	ScriptManager::relay("HigraphManager", "setTargetDecorator", name, target, ARROWHEAD);
	ScriptManager::relay("HigraphManager", "setEdgeColor", name, target, BLACK);
	
	 ScriptManager::relay("HigraphManager","makeNode", rows);
	 ScriptManager::relay("HigraphManager","placeNode", rows, 125, 320);
	 ScriptManager::relay("HigraphManager","setNodeValueShow", rows, true);
	 ScriptManager::relay("HigraphManager","setNodeNameLabel", rows, "rows");
	 ScriptManager::relay("HigraphManager","setNodeNameNudge",rows,-5,0);
	 
	 ScriptManager::relay("HigraphManager","makeNode", cols);
	 ScriptManager::relay("HigraphManager","placeNode", cols, 210, 320);
	 ScriptManager::relay("HigraphManager","setNodeValueShow", cols, true);
	 ScriptManager::relay("HigraphManager","setNodeNameLabel", cols, "cols");
	 ScriptManager::relay("HigraphManager","setNodeNameNudge",cols,-5,0);
	/*#/TS*/
	int i,j;/*#TS*//*nodes for i & j*/setup_i_j(i,j);/*#/TS*/
	for(i=0;i<rows;i++)
	{
		for(j=0;j<cols;j++)
		{	/*#TS*/ScriptManager::relay("HigraphManager","setNodeFillColor", mat[i][j], YELLOW); ScriptManager::relay("HigraphManager","setNodeFillColor", (B+(i*cols)+j), true, YELLOW);/*#/TS*/ /*#TS*/mat[i][j]=mat[i][j]*scale_factor;/*#/TS*/
			B[(i*cols)+j] = (B[(i*cols)+j])  *  (scale_factor);/*#TS*/ScriptManager::relay("HigraphManager","setNodeFillColor", mat[i][j], color_lookup(i));
			ScriptManager::relay("HigraphManager","setNodeFillColor", (B+(i*cols)+j), true, color_lookup(i));/*#/TS*/
		}
	}
	/*#TS*/ScriptManager::relay("HigraphManager", "deleteNode", target);	ScriptManager::relay("HigraphManager", "deleteNode", name);
	ScriptManager::relay("HigraphManager", "deleteNode", rows);		ScriptManager::relay("HigraphManager", "deleteNode", cols);
	ScriptManager::relay("HigraphManager", "deleteNode", i);		ScriptManager::relay("HigraphManager", "deleteNode", j);/*#/TS*/
}