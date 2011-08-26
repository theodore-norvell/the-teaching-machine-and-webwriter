#include <iostream>
using namespace std;

/*********** a while loop ****************

  Using a loop to generate a conversion table
  between degrees C and degrees F.

*******************************************/

int main(){
	int tempC = 0;

	cout << "A temperature conversion table\n\n";
	cout << "centigrade\tfahrenheit\n";
	cout << "--------------------------------------\n";

	while (tempC <= 100) {
		cout << "   " << tempC << "\t\t\t";

		cout << (9*tempC )/5 + 32 << '\n';

		tempC += 5;
	}

	cout << "--------------------------------------\n";
	return 0;
}
