/*#HA*/ /*#HB*/ /******************************************************************
 * Memorial University of Newfoundland
 * Engineering 2420 Structured Programming
 * int_swap.cpp -- pass by reference
 *
 * Author: Michael Bruce-Lockhart
 * Date: 2004.03.09
 *
 *******************************************************************/

#include <iostream>
using namespace std;

void intswap(int& x1, int& x2);

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
	int a = 2;
	int b = 3;
	intswap(a,b);
	cout << "a is " << a << " and b is " << b << endl;
	return 0;
}
/*#HB*/
/******************************************************************
 * intSwap
 *
 * Parameters: x1, x2: the variables to be swapped
 *
 * Modifies: x1 and x2
 *           
 *
 * Returns: nothing
 *******************************************************************/
/*#DA*/
 // A useful function!!!

void intswap(int& x1, int& x2){
	int temp;

	temp = x1;
	x1 = x2;
	x2 = temp;
}
