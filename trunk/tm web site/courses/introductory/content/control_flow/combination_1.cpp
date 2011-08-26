/*#HA*/ /*******  for loop demonstration ********
    A little program to output a table
    of factorials

*******************************************/

#include <iostream>
using namespace std;    // cout is in the std namespace
int factorial(int num);
int combinations(int n, int r);


int main(){

    cout << "The number of possible poker hands is ";
	cout << combinations(52,5);
	return 0;
}
/*#DA*/
/** combinations *************************************************
*
* @params: n - a postive integer
*          r - a positive integer @pre: r <= n
*
* @returns: the number of combinations of n things taken r at a time
****************************************************************/
int combinations(int n, int r) {
	return factorial(n) / (factorial(n-r) * factorial(r));
}

/** factorial **************************************************
*
* @param: num @pre: num must be a non-negative integer
*
* @returns: the factorial of num
****************************************************************/
int factorial(int num){
	int fact = 1;
	for(int i = 2; i <= num; i++)
		fact *=i;
	return fact;
}

        