/*******  if demonstration ********
    
	In this demo we sort marks
	into letter grade bins

*******************************************/

#include <iostream>
using namespace std;

int main(){
	int mark;

	cout<< "Enter your mark: ";
	cin >> mark;

	cout << "\nThis is a";

	if (mark < 48) {
		cout << "n F.\n";
	}

	// if (48 <= mark < 52)   ***** NOT LEGAL
	if (mark >= 48 && mark < 52) {
		cout << " D.\n";
	}

	if (mark >= 52 && mark < 63) {
		cout << " C.\n";
	}

	if (mark >= 63 && mark < 78) {
		cout << " B.\n";
	}

	if (mark >= 78 ) {
		cout << "n A.\n";
	}
	return 0;
}
