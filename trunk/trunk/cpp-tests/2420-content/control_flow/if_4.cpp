/*#H*/ /*******  if demonstration ********
    
	In this demo we sort marks
	into letter grade bins a little
	more efficiently than in the
	first demo.

*******************************************/
#include <iostream>
using namespace std;
/*#D*/
int main(){
	int mark;

	cout<< "Enter your mark: ";
	cin >> mark;

	cout << "\nThis is a";

	if (mark < 48)
		cout << "n F.\n";
	/*#b="else1"*/else/*#/b*/ /*#B="else1"*/if (mark < 52)
		cout << " D.\n";
	/*#b="else2"*/else/*#/b*/ /*#B="else2"*/if (mark < 63)
		cout << " C.\n";
	/*#b="else3"*/else/*#/b*/ /*#B="else3"*/if (mark < 78)
		cout << " B.\n";
	/*#b="else4"*/else/*#/b*/ /*#B="else4"*/
		cout << "n A.\n";/*#/B*/ /*#/B*/ /*#/B*/ /*#/B*/
	return 0;
}
