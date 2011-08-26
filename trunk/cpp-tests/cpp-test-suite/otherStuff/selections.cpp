//! Run.

// Selections and tagging.

#include <iostream>
using namespace std;

int main(){
	int i ;
	i = 0 ;	    /*#TAlice*/
	i = 1 ; 	/*#/T alice*//*#T Bob*/
	i = 2 ; 	/*#T ALICE*/
	i = 3 ; 	/*#/TBOB*//*#/T alice*/
	int j ;
	j = 0 ; /*#TC*/j = 1; /*#/TC*//*#TD*/j = 2; /*#TC*/j = 3 ;/*#/TC*//*#/TD*/
	return 0;
}

