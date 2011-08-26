/*#HA*/ /** div_remainder.cpp **********************************************
*
 * Memorial University of Newfoundland
 * Engineering 2420 Structured Programming
 *
 * 
 * Author: Michael Bruce-Lockhart
 *   Date: 2006.03.03
 *
 *******************************************************************/

#include <iostream>
using namespace std;


/** main **********************************************************
 *
 * @params: none
 *
 * @modifies: cout -- outputs a message
 *
 * @returns: 0
 *******************************************************************/
/*#DA*/
int main() {
	int numer = 8;
	int denom = 3;
    cout << "numerator is " << numer << " and denominator is " << denom << endl;
	cout << "denominator goes into numerator " << numer/denom << " times with ";
	cout << numer%denom << " left over." << endl;
    return 0;
}

