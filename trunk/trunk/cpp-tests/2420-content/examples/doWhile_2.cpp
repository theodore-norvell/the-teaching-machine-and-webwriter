#include <iostream>
using namespace std;

/*********** a do - while loop ****************

  A typical application of a do-while loop is to 
  make sure that an input is VALID (which is not
  the same as CORRECT).
*/

void main(){
	int mark;
	bool valid;

	cout << "Please input your mark: ";

	do {
		valid = true;
		cin >> mark;

		if (mark < 0) {
			cout << "\nMarks cannot be negative. ";
			cout << "Please input a non-negative mark: ";
			valid = false;
		}

		if (mark > 100) {
			cout << "\nThe highest possible mark is 100. ";
			cout << "Please input a mark of 100 or lower: ";
			valid = false;
		}

	}while (!valid);
	cout << "\nYour mark of " << mark;
	cout <<" was accepted. Thank-you.\n";
}
