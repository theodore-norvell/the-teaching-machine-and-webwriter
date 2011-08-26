//Passing by reference
#include <iostream>
using namespace std;


void intswap(int& x1, int& x2);

int main(){
	int a = 2;
	int b = 3;
	intswap(a,b);
	cout << "a is " << a << " and b is " << b << endl;
	return 0;
}

/*#TA*/ // A useful function!!!

void intswap(int& x1, int& x2){
	int temp;

	temp = x1;
	x1 = x2;
	x2 = temp;
}/*#/TA*/
