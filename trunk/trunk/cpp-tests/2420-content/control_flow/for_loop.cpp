/*******  for loop demonstration ********
    A little program to output a table
    of factorials

NOTE: The for statement's style is a little unusual
	it would normally be written out on one line
	for (int i = 0; i < 10; i++)

	I've done it this way to focus on the three
	separate expressions in the statement.

*******************************************/

#include <iostream>
using namespace std;    // cout is in the std namespace

int main(){
    int factorial = 1;

// Output a table heading
    cout << "Table of Factorials\n  i\t  i!\n\n";


    for (int i = 0;
			i < 10;
				i++)  {
        if (i !=0)
            factorial *= i;
        cout << i << "\t" << factorial << '\n';
    }
	
	cout << "That's all folks!\n";
	return 0;
}
        