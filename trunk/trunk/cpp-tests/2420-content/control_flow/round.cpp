/*#H*//******************************************************************
 * Memorial University of Newfoundland
 * Engineering 2420 Structured Programming
 * round.cpp -- rounding positive & negative numbers
 *
 * Author: Michael Bruce-Lockhart
 * Date: 2004.01.16
 *
 *******************************************************************/
#include <iostream>
using namespace std;

int round(double x);

/******************************************************************
 * main
 *
 * Parameters: none
 * Modifies: cout -- outputs some results
 *
 * Returns: 0
 *******************************************************************/
int main(){
	double number;
	cout << "Please input a real number: ";
	cin >> number;
	cout << "\nIt rounds to " << round(number) << endl;
	return 0;
}


/*#DA*/
/******************************************************************
 * round
 *
 * Parameters: x: a real number
 *
 * Modifies: nothing
 *
 * Returns: x rounded to the nearest integer
 *******************************************************************/
int round(double x){
	if (x < 0)
		return (int)(x-.5);
	return (int)(x+.5);
}
