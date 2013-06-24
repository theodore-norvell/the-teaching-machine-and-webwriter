#include<iostream>
#include<string>
using namespace std;
/*#TS*/#include"PDV_final.h" /*#/TS*/

void intSwapPassByVal(int,int);
void intSwapPassByRef(int&,int&);

/*#TS*/int a=5;  /*#/TS*/
/*#TS*/int b=3;  /*#/TS*/
int main() {/*#TS*/makeView("mainView", "wholeGraph","Higraph.PDV", "PlacedNode");
     setTitle("mainView", "intSwap PDV");
     setDefaultNodeValueShow(true, CENTER);
     setDefaultNodeValueColor(BLACK);
     setDefaultNodeShape(RECTANGLE);
	 ScriptManager::relay("HigraphManager", "setDefaultNodeFillColor", YELLOW);
     ScriptManager::relay("HigraphManager", "setDefaultNodeNameShow", true, WEST);
     ScriptManager::relay("HigraphManager", "setDefaultNodeNameColor", MAGENTA);
	 ScriptManager::relay("HigraphManager","setDefaultNodeSize", 40, 40); /*#/TS*/
	int a = 5 ;/*#TS*/ setupval(a,"a",102,20,a); /*#/TS*/ 
	int b = 3 ; /*#TS*/ setupval(b,"b",102,85,b); /*#/TS*/
	
	intSwapPassByVal(a,b);
	intSwapPassByRef(a,b);/*#TS*/ ScriptManager::relay("HigraphManager","deleteNode", a);ScriptManager::relay("HigraphManager","deleteNode", b);/*#/TS*/
	return 0 ;
}

void intSwapPassByVal(int arg1, int arg2){ /*#TS*/ setupval(arg1,"arg1",202,20,arg1); setupval(arg2,"arg2",202,85,arg2); /*#/TS*/
	int temp;  /*#TS*/ setupval(temp,"temp",302,20,temp); /*#/TS*/
	temp=arg1; /*#TS*/ setupval(temp,"temp",302,20,arg1); /*#/TS*/
	arg1=arg2; /*#TS*/ setupval(arg1,"arg1",202,20,arg2); /*#/TS*/
	arg2=temp; /*#TS*/ setupval(arg2,"arg2",202,85,temp); /*#/TS*/
	/*#TS*/ ScriptManager::relay("HigraphManager","deleteNode", temp);ScriptManager::relay("HigraphManager","deleteNode", arg1);ScriptManager::relay("HigraphManager","deleteNode", arg2);/*#/TS*/}

void intSwapPassByRef(int& arg1, int& arg2) { int x,y; /*#TS*/  setupRef(x,"arg1",202,6,x); makeEdge(arg1,x,GREEN); setupRef(y,"arg2",202,71,y); makeEdge(arg2,y,GREEN);  /*#/TS*/
	int temp  ; /*#TS*/ setupval(temp,"temp",302,20,temp); /*#/TS*/
	temp = arg1 ;    /*#TS*/ setupval(temp,"temp",302,20,arg1); /*#/TS*/
	arg1 = arg2 ; /*#TS*/ setupval(arg1,"a",102,20,arg2); /*#/TS*/
	arg2 = temp ; /*#TS*/ setupval(arg2,"b",102,85,temp); /*#/TS*//*#TS*/ScriptManager::relay("HigraphManager","deleteNode", temp);ScriptManager::relay("HigraphManager","deleteNode", x);ScriptManager::relay("HigraphManager","deleteNode", y);/*#/TS*/
	}




