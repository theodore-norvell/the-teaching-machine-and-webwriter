/*#H*/ /**** factorial.cpp **************
*
* Michael Bruce-Lockhart
* Feb. 12, 2008
*
* A program to demonstrate pass-by-value
*   
*******************************************/

#include <iostream>
using namespace std; 

/*#DA*/
int factorial(int num);

int main(){
    int num = 8;

    cout << "The factorial of " << num << " is ";
	cout << factorial(num) << endl;
	return 0;
}
/*#HA*/


/*#DB*//** factorial **************************************************
*
* @param: r @pre: r must be a non-negative integer
*
* @returns: the factorial of r
****************************************************************/
int factorial(int r){
    int fact = 1;
    for ( ; r > 1; r--)
		fact *=r;
	return fact;
}/*#HB*/

        
