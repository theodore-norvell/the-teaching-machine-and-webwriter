/******************************************************************
 * Memorial University of Newfoundland
 * Engineering 2420 Structured Programming
 * trapezoidal.cpp -- Program to compute an estimate of
 * an integral using the trapezoidal rule.
 *
 * Input: Limit points a and b on x axis
 *
 * Output: Area of the trapezoid
 *
 * Author: Michael Bruce-Lockhart
 *
 * Date: feb. 9, 2005
 *
 *******************************************************************/

#include <iostream>
#include <cmath>
using namespace std;

const double PI = acos(-1.0);

double trapezoid(double left, double right); // Return trapezoidal approx of integral
double theFunction(double x);	// The function being integrated

/******************************************************************
 * main --
 *
 * Parameters: none
 * Modifies: nothing
 *
 * Returns: 0
 *******************************************************************/
int main(){
	double left;	//The leftmost limit of integration
	double right;	//The rightmost limit of integration

	cout << "Please input the left and right limits of integration: ";
	cin >> left >> right;


	cout << "\nThe area under the integral is approximately " << trapezoid(left, right) << endl;
	return 0;
}


// Function implementations



/******************************************************************
 * trapezoid -- compute the trapezoidal approx of an integral
 *
 * Parameters: left and right limits of the integral on the x axis
 *
 * Modifies: nothing
 *
 * Precondition: none
 *
 * Returns: tre area of the trapezoid
 *******************************************************************/


double trapezoid(double left, double right){
	return (right - left) * (theFunction(left) + theFunction(right) ) /2;
}


/******************************************************************
 * theFunction -- compute the trapezoidal approx of an integral
 *
 * Parameters: x - the point on the x axis where the function is to be evaluated
 *
 * Modifies: nothing
 *
 * Precondition: none
 *
 * Returns: the value of the function at x
 *******************************************************************/

double theFunction(double x){
	return x*sin(x*x) - cos(x);
}
