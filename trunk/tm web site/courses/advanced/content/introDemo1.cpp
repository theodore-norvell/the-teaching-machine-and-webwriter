/* The mandatory "Hello world!" program */

/* In C & C++ input and output (i/o) are not part of the
	language. Instead they are included in the standard
	library of functions. The next line of code tells
	the precompiler to include the iostream part of
	the library */
#include <iostream>

/* There are now thousands of names in the libraries so
 they need to be qualified. Just like there are lots of
 Johns, Marys & Michaels in the world. This is like saying
 "use lastName Bruce-Lockhart". Then Michael always refers to
 Michael Bruce-Lockhart*/ 	
using namespace std;


void main() {
	cout << "Hello, world!\n";
}

