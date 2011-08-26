#include <iostream>
using namespace std;

void pointOut(double xc, double yc);



void main(){
	double slope;
	double intercept;
	cout << "please input the slope and the intercept: ";
	cin >> slope;
	cin >> intercept;

	for (int x = -10; x <=10; x++) {
		cout << "The point is at ";
		pointOut(x, slope * x + intercept);
		cout <<'\n';
	}
}


void pointOut(double xc, double yc){
	cout << '(' << xc << ',' << yc << ')';
}
