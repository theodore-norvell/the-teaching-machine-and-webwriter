#include <iostream>
using namespace std;

/*********** a while loop ****************

	We continue our temp conversion table
	using all integer arithmetic but
	getting the correct rounding

*/

void main(){
	int tempC = 0;

	cout << "A temperature conversion table\n\n";
	cout << "centigrade\tfahrenheit\n";
	cout << "--------------------------------------\n";

	while (tempC <= 100) {
		cout << "   " << tempC << "\t\t\t";

		cout << (18*tempC + 5)/10 + 32 << '\n';

		tempC += 2;
	}

	cout << "--------------------------------------\n";
}
