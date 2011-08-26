#include <iostream>
using namespace std;

void main(){
	int mark;

	cout<< "Enter your mark: ";
	cin >> mark;

	cout << "\nThis is a";

	if (mark < 48) {
		cout << "n F.\n";
	}

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
}