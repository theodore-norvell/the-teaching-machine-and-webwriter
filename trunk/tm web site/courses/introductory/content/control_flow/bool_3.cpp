#include <iostream>
using namespace std;

/***** odd or even numbers ********

  In this example we utilize the properties
  of integer arithmetic to determine if a
  number is odd or even.

**********************************************/



int main(){
	bool even;
	int number;

	cout << "Please input an integer: ";
	cin >> number;

	even = (2*(number/2) == number);

	cout << "The number is ";
	if (even)
			cout << "even.\n";
	else
			cout << "odd.\n";

	return 0;
}
