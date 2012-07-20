#include <iostream>
#include<string>
/*#TS*/
#include"PDV_final.h"
double **mat;
/*#/TS*/

void scale(double B[], int rows, int columns, double factor);

int main()
{
     /*#TS*/
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
     const int cols = 3;
	 double val = 10.0;/*#TS*/ setupval(val);
	 /*#/TS*/
     
     double A[rows*cols];/*#TS*/makeArray(A,rows,cols,true,"A[]", 320, 0);/*#/TS*/
     
	 int i,j;/*#TS*//*nodes for i & j*/setup_i_j(i,j);/*#/TS*/
	 
	 /*#TS*/mat = new double*[rows];
     for(int k=0;k<rows;k++)
        mat[k]=new double[cols];
     makeMatrix(mat,rows,cols,true,"Matrix Form of A[]");
	 for(int k=0;k<rows;k++)
		for(int l=0;l<cols;l++)
			mat[k][l]=A[(k*cols)+l];/*#/TS*/
	 
     for(i = 0; i < rows; i++)
     {
        for(j = 0; j < cols; j++)
            {	/*#TS*/ScriptManager::relay("HigraphManager","setNodeFillColor", mat[i][j], YELLOW);
					ScriptManager::relay("HigraphManager","setNodeFillColor", A[(i*cols)+j], YELLOW);/*#/TS*/
				/*#TS*/	mat[i][j]=val;/*#/TS*/
                A[(i*cols)+j]=val;
				val+=1.5;
				/*#TS*/ScriptManager::relay("HigraphManager","setNodeFillColor", mat[i][j], color_lookup(i));
					ScriptManager::relay("HigraphManager","setNodeFillColor", A[(i*cols)+j], color_lookup(i));/*#/TS*/
			}
     }
	 
	 //Call module to add 1 to each element of matrix
	 double scale_factor = .25;
	 /*#TS*/ScriptManager::relay("HigraphManager","makeNode", scale_factor);
	 ScriptManager::relay("HigraphManager","placeNode", scale_factor, 5, 320);
	 ScriptManager::relay("HigraphManager","setNodeValueShow", scale_factor, true);
	 ScriptManager::relay("HigraphManager","setNodeNameLabel", scale_factor, "scale_factor");
	 ScriptManager::relay("HigraphManager","setNodeNameNudge",scale_factor,-10,0);/*#/TS*/
	 scale(A, rows, cols, scale_factor);
	 //See that the values of the original matrix have changed
	
	 //Let's pass another 2*2 matrix into the scale function
	 double C[2*2];
	 /*#TS*/makeArray(C,2,2,true,"C[]", 400, 0);
	 //for(int k=0;k<rows;k++)
		//for(int l=0;l<cols;l++)
			ScriptManager::relay("HigraphManager","deleteNode", temp);
	 //ScriptManager::relay("HigraphManager","deleteNode",mat[0], true);
	 //ScriptManager::relay("HigraphManager","deleteNode",mat[1], true);
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
            {	/*#TS*/ScriptManager::relay("HigraphManager","setNodeFillColor", mat[i][j], YELLOW);
					ScriptManager::relay("HigraphManager","setNodeFillColor", C[(i*2)+j], YELLOW);/*#/TS*/
				/*#TS*/	mat[i][j]=val;/*#/TS*/
                C[(i*2)+j]=val;
				val+=1.5;
				/*#TS*/ScriptManager::relay("HigraphManager","setNodeFillColor", mat[i][j], color_lookup(i));
					ScriptManager::relay("HigraphManager","setNodeFillColor", C[(i*2)+j], color_lookup(i));/*#/TS*/
			}
     }
	 
	 scale(C, 2, 2, scale_factor);
	 
	 /*#TS*/  ScriptManager::relay("HigraphManager","deleteNode", temp); /*#/TS*/
	 return 0;
}

void scale(double B[], int rows, int columns, double factor)
{	/*#TS*/ int name;
	int target;
	ScriptManager::relay("HigraphManager","makeNode",name); 
	ScriptManager::relay("HigraphManager","setNodeColor",name,WHITE);
	
	ScriptManager::relay("HigraphManager","setNodeNameShow",name,true);
	ScriptManager::relay("HigraphManager","setNodeValueShow",name,false);
	ScriptManager::relay("HigraphManager","setNodeNamePosition",name,CENTER);
	ScriptManager::relay("HigraphManager","setNodeNameLabel",name,"B[]");
	ScriptManager::relay("HigraphManager","placeNode",name,400,380);
	ScriptManager::relay("HigraphManager","makeNode",target); 
	ScriptManager::relay("HigraphManager","setNodeSize", target, 1, 1);
	ScriptManager::relay("HigraphManager","setNodeColor",target,WHITE);
	ScriptManager::relay("HigraphManager","setNodeNameShow",target,false);
	ScriptManager::relay("HigraphManager","setNodeValueShow",target,false);
	if(columns==3)
		ScriptManager::relay("HigraphManager","placeNode",target,320,367);
	else
		ScriptManager::relay("HigraphManager","placeNode",target,389,260);
	ScriptManager::relay("HigraphManager","makeEdge", name, target);
	ScriptManager::relay("HigraphManager", "setTargetDecorator", name, target, ARROWHEAD);
	ScriptManager::relay("HigraphManager", "setEdgeColor", name, target, BLACK);
	//ScriptManager::relay("HigraphManager","setStringBaseColor",name,MAGENTA);
	/*ScriptManager::relay("HigraphManager","setNodeNameLabel",B,true,"B[]");*/
	/*#/TS*/
	int i,j;
	for(i=0;i<rows;i++)
	{
		for(j=0;j<columns;j++)
		{
			/*#TS*/ScriptManager::relay("HigraphManager","setNodeFillColor", mat[i][j], YELLOW);
			ScriptManager::relay("HigraphManager","setNodeFillColor", (B+(i*columns)+j), true, YELLOW);/*#/TS*/
			/*#TS*/	mat[i][j]=mat[i][j]*factor;/*#/TS*/
			B[(i*columns)+j] = (B[(i*columns)+j])  *  (factor);
			/*#TS*/ScriptManager::relay("HigraphManager","setNodeFillColor", mat[i][j], color_lookup(i));
			ScriptManager::relay("HigraphManager","setNodeFillColor", (B+(i*columns)+j), true, color_lookup(i));/*#/TS*/
		}
	}
	
	/*#TS*/ScriptManager::relay("HigraphManager", "deleteNode", target);	ScriptManager::relay("HigraphManager", "deleteNode", name);/*#/TS*/
}