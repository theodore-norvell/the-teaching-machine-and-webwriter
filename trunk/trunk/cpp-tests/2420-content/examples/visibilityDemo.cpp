/*************************************************
	a (silly)demonstration of Visibility

**************************************************/
#include <iostream>
using namespace std;

/*#B="john1Visibility"*/int /*#b="john1Visibility"*/john/*#/b*/ = 3;
/*#B="mary1Visibility"*/int /*#b="mary1Visibility"*/mary/*#/b*/ = 4;

/*#B="fooVisibility"*/void /*#b="fooVisibility"*/functionFoo()/*#/b*/;
/*#B="faaVisibility"*/void /*#b="faaVisibility"*/functionFaa()/*#/b*/;
/*#/B*/ /*#/B*/ /*#/B*/ /*#/B*/ /*#B="fooVisibility"*/ /*#B="faaVisibility"*/
/*#B="mainVisibility"*/int /*#b="mainVisibility"*/main()/*#/b*/{
/*#B="john2Visibility"*/	int /*#b="john2Visibility"*/john/*#/b*/ = 2;
/*#B="mary2Visibility"*/	int /*#b="mary2Visibility"*/mary/*#/b*/ = 1;

	for (/*#B="julesVisibility"*/int /*#b="julesVisibility"*/jules/*#/b*/ = 0; jules < 5; jules++){
		mary += jules;
		cout << "Inside loop: jules is " << jules << " and mary is " << mary;
		cout <<endl;
	/*#/B*/}
	cout << "\nAfter loop: john is " << john << " and mary is " << mary;
	cout <<endl;
	functionFoo();
	cout << "\nAfter function: john is " << john << " and mary is " << mary;
	cout <<endl;
	return 0;
/*#/B*/ /*#/B*/}

void functionFoo(){
/*#B="john3Visibility"*/	int /*#b="john3Visibility"*/john/*#/b*/ = 17;
/*#B="mary3Visibility"*/	int /*#b="mary3Visibility"*/mary/*#/b*/ = 13;
	cout << "\nInside functionFoo: john is " << john << " and mary is " << mary;
	cout << endl;
/*#/B*/ /*#/B*/}
/*#B="john1Visibility"*/
void functionFaa(){
/*#B="mary4Visibility"*/	int /*#b="mary4Visibility"*/mary/*#/b*/ = 29;
	cout << "\nInside functionFaa: john is " << john << " and mary is " << mary;
	cout << endl;
/*#/B*/}

/*#/B*/ /*#/B*/ /*#/B*/ /*#/B*/ /*#/B*/ /*#/B*/

