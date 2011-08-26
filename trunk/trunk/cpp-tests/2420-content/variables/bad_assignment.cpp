/*#HA*//******************************************************************
 * Memorial University of Newfoundland
 * Engineering 2420 Structured Programming
 * bad_assignment.cpp -- an example of an assignment statement used incorrectly
 *
 * Author: Michael Bruce-Lockhart
 * Date: 2004.01.02
 *
 *******************************************************************/
#include <iostream>
using namespace std;


/******************************************************************
 * main
 *
 * Parameters: none
 * Modifies: cout -- outputs some results
 *
 * Returns: 0
 *******************************************************************/
/*#DA*/int main() {

	double x;
	double y;
	cout << "Please input a value for x: ";
	cin >> x;
	x = y;  // Here is where the error occurs
	cout << "\nAfter setting the variables equal to each other, y is " << y << endl;
	return 0;
}
