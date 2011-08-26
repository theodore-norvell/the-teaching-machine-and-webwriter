// A variation on our do nothing program which illustrates how the stack works
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
	if (c < 2) return 1;
	return c*factorial(c-1);
}