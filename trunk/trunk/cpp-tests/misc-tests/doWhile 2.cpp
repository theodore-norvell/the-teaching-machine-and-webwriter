//! Run with input "200" endl "-99" endl "50" endl.
//! Expect output = "Please input your mark: \n"
//! "The highest possible mark is 100. Please input a mark of 100 or lower: "
//! "\nMarks cannot be negative. Please input a non-negative mark: "
//! "\nYour mark of 50.0 was accepted. Thank-you.\n"

#include <iostream>
using namespace std;

/*********** a do - while loop ****************

  A typical application of a do-while loop is to 
  make sure that an input is VALID (which is not
  the same as CORRECT).
*/

void main(){
	double mark;
	bool valid = true;

	cout << "Please input your mark: ";

	do {
		cin >> mark;

		if (mark < 0.) {
			cout << "\nMarks cannot be negative. ";
			cout << "Please input a non-negative mark: ";
			continue ;
		}

		if (mark > 100.) {
			cout << "\nThe highest possible mark is 100. ";
			cout << "Please input a mark of 100 or lower: ";
			continue ;
		}
		break ;

	}while (true);
	cout << "\nYour mark of " << mark;
	cout <<" was accepted. Thank-you.\n";
}
