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
	bool isPrime(int num){ /*#TS*/setupval(temp,"num",50,80,temp); /*#/TS*/
	
	bool isPrime = true ;/*#TS*/ int i=1;  setupBool(i,"isPrime",102,200,i); displayString(id1,True,157,210,BLACK);   int remainder=0;/*#/TS*/
	for(int div=2; div<num; div++){ /*#TS*/setupval(div,"div",200,80,div); remainder=num%div;setupval(remainder,"num%div",320,80,remainder); /*#/TS*/
		isPrime = isPrime && num%div; /*#TS*/if (div==6) {ScriptManager::relay("HigraphManager","deleteNode", div);}/*#/TS*/
	} /*#TS*/ScriptManager::relay("HigraphManager","deleteNode", remainder);/*#/TS*/
	/*#TS*/ removeString(id1);ScriptManager::relay("HigraphManager","deleteNode", temp);ScriptManager::relay("HigraphManager","deleteNode",i);/*#/TS*/
	return isPrime ; 
}


int calcSum(int n){/*#TS*/ int i=10; setupval(i,"n",200,80,i); /*#/TS*/

	int sum=0;/*#TS*/setupval(sum,"sum",320,80,sum); /*#/TS*/
	
	for(int i=1;i<=n;i++)
	{
		sum=sum+i; /*#TS*/setupval(sum,"sum",320,80,sum); /*#/TS*/
	}
	
	return sum;
}




