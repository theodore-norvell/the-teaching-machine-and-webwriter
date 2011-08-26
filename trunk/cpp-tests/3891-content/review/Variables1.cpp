//! Run.
// A do nothing program which illustrates various attributes of variables
// copyright(c) 2002 by Michael Bruce-Lockhart

#include <iostream>
using namespace std;

int factorial(int c);

double x;		// These are external variables
int count = 3;

int main(){
	int myCount;
	x = 3.14159;
	myCount = count++;
	int fact = factorial(myCount);
	cout << "The factorial of " << myCount << " is " << fact << endl;
	return 0;
}

int factorial(int c){
	if (c < 0) return -1;
	int fact = 1;
	for (int i = 2; i <= c; i++ )
		fact *=i;
	return fact;
}