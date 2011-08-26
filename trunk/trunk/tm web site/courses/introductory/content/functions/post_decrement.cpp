/*#HA*/ /*#HB*/ /******************************************************************
 * Memorial University of Newfoundland
 * Engineering 2420 Structured Programming
 * side_effects.cpp -- Demonstrate pass-by-value
 *
 * Author: Michael Bruce-Lockhart
 * Date: 2004.03.02
 *
 *******************************************************************/
#include <iostream>
using namespace std;

int factorial(int n);

/******************************************************************
 * main
 *
 * Parameters: none
 * Modifies: cout -- outputs some results
 *
 * Returns: 0
 *******************************************************************/
/*#DB*/
int main(){
	int n;	// the number whose factorial is to be computed
	int fact; // its factorial

	cout << "A program to compute factorials." << endl;
	cout << "Please enter a non-negative integer: ";
	cin >> n;
	while ( n < 0) {
		cout << "Factorials can only be computed for "
			 << "non-negative integers. Please enter again: ";
		cin >> n;
	}
	fact = factorial(n);
	cout << "The factorial of " << n << " is " << fact << endl;
	return 0;
}
/*#HB*/

/*#DA*/
 /******************************************************************
 * factorial
 * 
 * Parameters: n: the integer whose factorial is to be computed
 *                 (assert non-negative)

 * Modifies: nothing
 *
 * Returns: the factorial of n
 *******************************************************************/

int factorial(int n){
	int result = 1;
	while (n > 1) 
		result *= n--;
	return result;
}
