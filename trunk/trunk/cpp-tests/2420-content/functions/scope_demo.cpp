/******************************************************************
 * Memorial University of Newfoundland
 * Engineering 2420 Structured Programming
 * scopeDemo.cpp -- A silly demo of scope
 *
 * Input: none
 * Output: none
 *
 * Author: Michael Bruce-Lockhart
 *
 *******************************************************************/
#include <iostream>
using namespace std;

/*#B="john1Scope"*/int /*#b="john1Scope"*/john/*#/b*/ = 3;
/*#B="mary1Scope"*/int /*#b="mary1Scope"*/mary/*#/b*/ = 4;

/*#B="fooScope"*/void /*#b="fooScope"*/functionFoo()/*#/b*/;
/*#B="faaScope"*/void /*#b="faaScope"*/functionFaa()/*#/b*/;

/*#B="mainScope"*/int /*#b="mainScope"*/main()/*#/b*/ {
/*#B="john2Scope"*/	  int /*#b="john2Scope"*/john/*#/b*/ = 2;
/*#B="mary2Scope"*/	  int /*#b="mary2Scope"*/mary/*#/b*/ = 1;

	{/*#B="julesScope"*/    int /*#b="julesScope"*/jules/*#/b*/ = 0;
		mary += jules;
		cout << "Inside block: jules is " << jules << " and mary is " << mary;
		cout <<endl;
	/*#/B*/}
	cout << "\nAfter block: john is " << john << " and mary is " << mary;
	cout <<endl;
	functionFoo();
	cout << "\nAfter function: john is " << john << " and mary is " << mary;
	cout <<endl;
	return 0;
/*#/B*/ /*#/B*/}

void functionFoo(){
/*#B="john3Scope"*/	int /*#b="john3Scope"*/john/*#/b*/ = 17;
/*#B="mary3Scope"*/	int /*#b="mary3Scope"*/mary/*#/b*/ = 13;
	cout << "\nInside functionFoo: john is " << john << " and mary is " << mary;
	cout << endl;
/*#/B*/ /*#/B*/}

void functionFaa(){
/*#B="mary4Scope"*/	int /*#b="mary4Scope"*/mary/*#/b*/ = 29;
	cout << "\nInside functionFaa: john is " << john << " and mary is " << mary;
	cout << endl;
/*#/B*/}

/*#/B*/ /*#/B*/ /*#/B*/ /*#/B*/ /*#/B*/ 

