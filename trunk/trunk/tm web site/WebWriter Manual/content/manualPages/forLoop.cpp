/*******  for loop demonstration ********
	A program to output a table of
		FACTORIALS
 *****************************************/
 
#include <iostream.h>  
using namespace std; 

int main(){
    int factorial = 1;

// Output a table heading
    cout << "Table of Factorials\n  i\t  i!\n\n";

    for (int i = 0; i < 10; i++) {
        if (i !=0)
            factorial *= i;
        cout << i << "\t" << factorial << '\n';
    }
	return 0;
}
        