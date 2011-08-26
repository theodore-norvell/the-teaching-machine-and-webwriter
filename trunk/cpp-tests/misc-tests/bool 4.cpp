#include <iostream>
using namespace std;

/***** odd or even numbers ********

  A little better version of using
  integer arithmetic to determine if a
  number is odd or even.

**********************************************/



void main(){
	int number;

	cout << "Please input an integer: ";
	cin >> number;

	cout << "The number is ";

	if (2*(number/2) == number)
			cout << "even.\n";
	else
			cout << "odd.\n";
}
