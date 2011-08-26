/*#H*/ /******************************************************************
 * Memorial University of Newfoundland
 * Engineering 2420 Structured Programming
 * int_swap.cpp -- pass by reference
 *
 * Author: Michael Bruce-Lockhart
 * Date: 2006.02.09
 *
 *******************************************************************/

#include <iostream>
using namespace std;

void intOrder(int x1, int x2);

int main(){
	int a = 5;
	int b = 3;
	intOrder(a,b);
	cout << "a is " << a << " and b is " << b << endl;
	return 0;
}
/*#DB*/ /*#DC*/
void intSwap(int& arg1, int& arg2);

/** intOrder **********************************************************
*
* @params: x1 - any integer
*          x2 - any integer
*
*          Tries to put x1 and x2 into ascending order so x1 <= x2
*
* @returns: nothing - it doesn't work
***********************************************************************/
void intOrder(int x1, int x2){
	if (x1 > x2)
		intSwap(x1,x2);
}/*#HB*/
/*#DA*/
/** intSwap **********************************************************
*
* @params: arg1 - a reference to any integer
*          arg2 - a ref to any integer
*
*          The values referred to by arg1 and arg2 get swapped
*
* @returns: nothing 
***********************************************************************/

void intSwap(int& arg1, int& arg2){
	int hold;

	hold = arg1;
	arg1 = arg2;
	arg2 = hold;
}
