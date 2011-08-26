#include <iostream>
using namespace std;

/********* Functions Returning void *********************

	A program to compute points on a straight line

********************************************************/

void pointOut(double xc, double yc);

void main(){
	double slope;
	double intercept;
	cout << "Please input the slope and the intercept: ";
	cin >> slope >> intercept;

	cout << "The first point is at ";
	pointOut(-10, slope*10 + intercept);
	cout << "\nThe interior points are\n";

	for (int x = -9; x <10; x++) {
		pointOut(x, slope * x + intercept);
		cout <<' ';
	}
	cout <<"\nand the last point is at ";
	pointOut(10, slope* (-10) + intercept);
	cout <<'\n';
}





void pointOut(double xc, double yc){
	cout << '(' << xc << ',' << yc << ')';
}
