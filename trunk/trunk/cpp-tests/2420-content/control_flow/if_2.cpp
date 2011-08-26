/*******  if demonstration ********
    
	In this demo we sort marks
	into letter grade bins a little
	more efficiently than in the
	first demo.

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
	} else {

		if (mark < 52) {
			cout << " D.\n";
		} else {

			if (mark < 63) {
				cout << " C.\n";
			} else {

				if (mark < 78) {
					cout << " B.\n";
				} else {
					cout << "n A.\n";
				}
			}
		}
	}
return 0;
}

