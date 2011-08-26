/*#HA*/ /*******  for loop demonstration ********
    A little program to output a table
    of factorials

*******************************************/

#include <iostream>
using namespace std;    // cout is in the std namespace
int factorial(int num);



int main(){
// Output a table heading
    cout << "Table of Factorials\n  i\t  i!\n\n";

    for (int i = 0; i < 10; i++)  {
        cout << i << "\t" << factorial(i) << endl;
    }
	
	cout << "That's all folks!\n";
	return 0;
}

/*#DA*/ /** factorial **************************************************
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
