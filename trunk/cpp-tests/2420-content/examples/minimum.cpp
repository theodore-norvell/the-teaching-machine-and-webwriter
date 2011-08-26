
#include <iostream>
using namespace std;

// function declaration
double minimum(double left, double mid, double right);

int main(){
	double x1 = 3.4;
	double x2 = -7.1;
	double x3 = 3.14159;

// a function call is in the middle of this line
	cout << "The minimum is " << minimum(x1, x2, x3);

	return 0;
}

//function implementation
double minimum(double left, double mid, double right){
	if (left < mid)
		return (left < right ? left : right);
	else
		return (mid < right ? mid : right);
}
