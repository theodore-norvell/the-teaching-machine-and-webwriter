#include <iostream>
using namespace std;

/********* Functions Returning void *********************

	A program to compute points on a straight line

********************************************************/

void pointOut(double xc, double yc);

double straightLine(double x, double slope, double intercept);

void main(){
	double slope;
	double intercept;
	cout << "Please input the slope and the intercept: ";
	cin >> slope >> intercept;

	cout << "The first point is at ";
	pointOut(-10, straightLine(-10,slope,intercept) );
	cout << "\nThe interior points are\n";

	for (int x = -9; x <10; x++) {
		pointOut(x, straightLine(x,slope,intercept) );
		cout <<' ';
	}
	cout <<"\nand the last point is at ";
	pointOut(10, straightLine(10,slope,intercept) );
	cout <<'\n';
}






void pointOut(double xc, double yc){
	cout << '(' << xc << ',' << yc << ')';
}

double straightLine(double x, double slope, double intercept){
	return slope * x + intercept;
}

