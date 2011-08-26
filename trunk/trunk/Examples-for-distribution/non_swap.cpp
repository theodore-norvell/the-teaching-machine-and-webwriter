/*#H*/ /***********************************************
 * Memorial University of Newfoundland
 * Engineering 2420 Structured Programming
 * non_swap.cpp -- pass by value
 *
 * Author: Michael Bruce-Lockhart
 * Date: 2006.02.09
 *
 ******************************************************/

#include <iostream>
using namespace std;

/*#DM*/ void intSwap(int arg1, int arg2);

int main(){
	int a = 5;
	int b = 3;
	intSwap(a,b);
	cout << "a is " << a << " and b is " << b << endl;
	return 0;
}/*#HM*/


/*#DA*/ /** intSwap **********************************************
*
* @params: arg1 - any integer
*          arg2 - any integer
*
*          A totally useless function!!!
*
* @returns: nothing - it doesn't work
**********************************************************/

void intSwap(int arg1, int arg2){
	int hold;

	hold = arg1;
	arg1 = arg2;
	arg2 = hold;
}/*#H*/
