/*#HA*//******************************************************************
 * Memorial University of Newfoundland
 * Engineering 2420 Structured Programming
 * expression_eval_int.cpp 
 * Demonstrate integer arithmetic.
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
	int i = 5;
	int j = 3;
	
	cout << "A demonstration of integer arithmetic." << endl;
	cout << "i is " << i << " & j is " << j << endl;
	cout << "i + j = " << i + j << endl;
	cout << "i + -j = " << i + -j << endl;
	cout << "i * j = " << i * j << endl;
	cout << "i / j = " << i / j << endl;
	cout << "i % j = " << i % j << endl;
	return 0;
}
        