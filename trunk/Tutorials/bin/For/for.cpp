#include<iostream>
#include<string>
using namespace std;
/*#TS*/#include"PDV_final.h" /*#/TS*/

	bool isPrime(int);
	int calcSum(int);
	
/*#TS*/char* id1="str1";/*#TS*/
/*#TS*/char* id2="str2";/*#TS*/
/*#TS*/ char* True="true";/*#/TS*/
/*#TS*/ char* False="false";/*#/TS*/
/*#TS*/int temp=7; /*#/TS*/

int main() {/*#TS*/makeView("mainView", "wholeGraph","Higraph.PDV", "PlacedNode");setTitle("mainView", "intSwap PDV");setDefaultNodeValueShow(true, CENTER);setDefaultNodeValueColor(BLACK);setDefaultNodeShape(RECTANGLE);ScriptManager::relay("HigraphManager", "setDefaultNodeFillColor", YELLOW);
     ScriptManager::relay("HigraphManager", "setDefaultNodeNameShow", true, WEST);ScriptManager::relay("HigraphManager", "setDefaultNodeNameColor", MAGENTA);ScriptManager::relay("HigraphManager","setDefaultNodeSize", 40, 40); /*#/TS*/
	//for loop example1
	bool b=isPrime(7);
	//for loop example2
	//calculating the sum of first n natural numbers
	int sum=0;
	sum = calcSum(10);

}/** Precondition: num > 1 */
	bool isPrime(int num){ 
	/*#TS*/setupval(temp,"num",102,20,temp); /*#/TS*/
	bool isPrime = true ;/*#TS*/ int i=1;  setupBool(i,"isPrime",102,200,i); displayString(id1,True,157,210,BLACK);   int remainder=0;/*#/TS*/
	for(int div=2; div<num; div++){ /*#TS*/setupval(div,"div",102,100,div); /*#/TS*//*#TS*/remainder=num%div;/*#/TS*//*#TS*/setupval(remainder,"num%div",252,200,remainder); /*#/TS*/
		isPrime = isPrime && num%div; /*#TS*/if (isPrime==true){i=1; } else {i=0; }/*#/TS*/
	} /*#TS*/ removeString(id1);ScriptManager::relay("HigraphManager","deleteNode", temp);ScriptManager::relay("HigraphManager","deleteNode",i);ScriptManager::relay("HigraphManager","deleteNode", num);/*#/TS*/
	return isPrime ; 
}


int calcSum(int n)
{
	int sum=0;
	
	for(int i=1;i<=n;i++)
	{
		sum=sum+i;
	}
	
	return sum;
}




