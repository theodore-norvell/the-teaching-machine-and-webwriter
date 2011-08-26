/*#HA*//******************************************************************
 * Memorial University of Newfoundland
 * Engineering 2420 Structured Programming
 * bad_assignment.cpp -- an example of an assignment statement used incorrectly
 *
 * Author: Michael Bruce-Lockhart
 * Date: 2006.01.19
 *
 *******************************************************************/
#include <iostream>
using namespace std;

void badError(double x);

int main() {

	badError(3.47);
	return 0;
}
/*#DA*/
/** badError ********************************************************
 * 
 * @params: x - any double
 *
 *          demonstrates incorrect use of assignment
 * Returns: nothing
 *******************************************************************/

void badError(double x){
	double hold;
	x = hold;	// remember x
	x = x + 1;
	//... etc.
}
