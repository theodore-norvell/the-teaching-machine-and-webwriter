/*#H*/ /*******  for loop demonstration ********
    A little program to output a table
    of factorials

*******************************************/

#include <iostream>
using namespace std;    // cout is in the std namespace

int combinations(int n, int r);

int main(){

    cout << "The number of possible poker hands is ";
	cout << combinations(52,5);
	return 0;
}
/*#DA*/
int factorial(int num);

/** combinations *************************************************
*
* @params: n - a postive integer
*          r - a positive integer @pre: r <= n
*
* @returns: the number of combinations of n things taken r at a time
****************************************************************/
/*#DB*/int combinations(int n, int r) {
/*#HB*/	if (r > n-r)
		r = n-r;
	int comb = 1;
	for (int i = n; i > n-r; i--)
		comb *= i;
/*#DB*/	return comb / factorial(r);
}

/** factorial **************************************************
*
* @param: num @pre: num must be a non-negative integer
*
* @returns: the factorial of num
****************************************************************/
int factorial(int num){
	int fact = 1;
	while (num > 1){
		fact *=num;
		num--;
	}
	return fact;
}

        
