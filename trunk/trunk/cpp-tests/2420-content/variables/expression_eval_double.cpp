/*#HA*/ /******************************************************************
 * Memorial University of Newfoundland
 * Engineering 2420 Structured Programming
 * expression_eval_double.cpp -- A simple line equation
 * Demonstrate double arithmetic.
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
/*#DA*/

int main(){
	double x = 2.4;
	double y;
	
	y = x*x + 2.0 * x + 1.5;
	cout << "y is " << y << " when x is " << x << '\n';
	return 0;
}
        