#include <iostream>
using namespace std;

/********* Functions Returning void *********************

	A program to compute points on a straight line

********************************************************/

void pointOut(double, double);

void setSlope(double);
void setIntercept(double);

// Computes the y value of a straight line given x
// precondition: setSlope and setIntercept have been called first
double straightLine(double);

void main(){
	double slope;
	double intercept;
	cout << "Please input the slope and the intercept: ";
	cin >> slope >> intercept;

	setSlope(slope);
	setIntercept(intercept);

	cout << "The first point is at ";
	pointOut(-10, straightLine(-10) );
	cout << "\nThe interior points are\n";

	for (int x = -9; x <10; x++) {
		pointOut(x, straightLine(x) );
		cout <<' ';
	}
	cout <<"\nand the last point is at ";
	pointOut(10, straightLine(10) );
	cout <<'\n';
}






void pointOut(double xc, double yc){
	cout << '(' << xc << ',' << yc << ')';
}


/************* straight line package ****************

	setSlope & setIntercept should be called before
	straightLine is called

*****************************************************/

double mySlope = 0.0;
double myIntercept = 0.0;

void setSlope(double slope){
	mySlope = slope;
}

void setIntercept(double intercept){
	myIntercept = intercept;
}

double straightLine(double x){
	return mySlope * x + myIntercept;
}

