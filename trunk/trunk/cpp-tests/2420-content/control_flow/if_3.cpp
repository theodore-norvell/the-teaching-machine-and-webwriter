/*******  if demonstration ********
    
	In this complex if demo we
	output properly formatted
	complex nos.

*******************************************/

#include <iostream> 
using namespace std;

int main(){
	int real;
	int imag;

	cout << "Enter the real part: ";
	cin >> real;
	cout << "& the imaginary part: ";
	cin >> imag;

	cout << "\nThe complex no. is (";

	if (real == 0) {
		if (imag == 0) {
			cout << "0";
		} else {
			cout << imag << 'i';
		}
	} else {
		cout << real;
		if (imag < 0) {
			cout << " - ";
			cout << -imag << 'i';
		}
		if (imag > 0) {
			cout << " + ";
			cout << imag << 'i';
		}
	}
	cout << ")\n";
	return 0;
}

        