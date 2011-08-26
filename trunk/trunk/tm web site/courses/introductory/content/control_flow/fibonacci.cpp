/*#HA*/ /*******  for loop demonstration ********
    A little program to output a table
    of fibonacci nos.

*******************************************/

#include <iostream>
using namespace std;    // cout is in the std namespace
int fibonacci(int num);

int n ;

int main(){

    cout << "n\tfib(n)" << endl;
    cout << 0 << '\t' << 1 << endl;
    cout << 1 << '\t' << 2 << endl;
	for (n = 2;n < 20; n++)
		cout << n << '\t' << fibonacci(n) << endl;
	return 0;
}
/*#DA*/
/** fibonacci *************************************************
*
* @params: n - an integer @pre: n >= 2
*
* @returns: the n'th fibonacci number
****************************************************************/
int fibonacci(int n) {
	int fLast = 1;
	int f2Last = 0;
	int fi;
	for (int i = 2; i <= n; i++) {
		fi = fLast + f2Last;
		f2Last = fLast;
		fLast = fi;
	}
	return fi;
}


        
