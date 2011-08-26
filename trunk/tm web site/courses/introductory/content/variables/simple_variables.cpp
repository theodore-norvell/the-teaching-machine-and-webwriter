/*******  Simple Variables Demonstration ***********
    To demonstrate on the teaching machine the four
    fundamental attributes of simple C variables
    name, type, value, location

***************************************************/

#include <iostream>
using namespace std;

int main(){
// variable declarations: have the effect of setting space aside for... 
	int i = 110;	// ... an integer called i. Inititialise i to 110
	long j = 98765432199; // ... a long integer initialized to a BIG number
	char letter;	// ... a char called letter. Unitialised.
	float pi;        // ... a single precision real variable. Unitialised.
	double e;		// ... a double precision real variable. Unitialised.
    
	letter = 'n';
	pi = 3.14159;      // a crude approximation of pi
	e = 1.60217646e-19;    // The charge on an electron
    
    return 0;
}
        
