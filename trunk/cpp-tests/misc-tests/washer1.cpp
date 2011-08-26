#include <iostream>
#include <cmath>
using namespace std;

const double PI = 3.14159;

double getDouble (char* msg);
int getInteger (char*  msg);
double getTotalWeight (int num, double density, double thickness, double rinner, double router);
double unitWeight(double density, double thickness, double rinner, double router);
double area(double radius);

int main() {

	int num = 0;
	double totalWeight,
		density,
		thickness,
		router,			//
		rinner;			//

	rinner = getDouble(" inner radius: ");
	router = getDouble(" outer radius: ");
	thickness = getDouble(" thickness: ");
	density = getDouble(" density: ");
	num = getInteger(" number of pieces: ");

	totalWeight = getTotalWeight(num, density, thickness, rinner, router);

	cout << "\nThe total weight is: " << totalWeight << endl;

	return 0;
}

double getDouble (char* msg) {
	
	double value;

	cout << "\nPlease enter the value for " << msg << endl;
	cin >> value;

	return value;
}

int getInteger (char* msg) {
	
	int value;

	cout << "\nPlease enter the value for " << msg << endl;
	cin >> value;

	return value;
}

double getTotalWeight (int num, double density, double thickness, double rinner, double router){

	return unitWeight(density, thickness, rinner, router) * num;
}

double unitWeight(double density, double thickness, double rinner, double router){

	double weight;
	weight = (area(router) * thickness - area(rinner) * thickness) * density;
	return weight;
}

double area(double radius) {
	return (PI * radius * radius);
}

