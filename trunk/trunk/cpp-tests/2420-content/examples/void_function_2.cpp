#include <iostream>
using namespace std;

void pointOut(double xc, double yc);

void main(){
	double slope;
	double intercept;
	cout << "Please input the slope and the intercept: ";
	cin >> slope >> intercept;

	cout << "The first point is at ";
	pointOut(10, slope*10 + intercept);
	cout << "\nThe interior points are\n";

	for (int x = -9; x <10; x++) {
		pointOut(x, slope * x + intercept);
		cout <<' ';
	}
	cout <<"\nand the last point is at ";
	pointOut(-10, slope* (-10) + intercept);
	cout <<'\n';
}

// output xc and yc as (xc,yc)
void pointOut(double xc, double yc){
	cout << '(' << xc << ',' << yc << ')';
}
