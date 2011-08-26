/*#HA*/ /******************************************************************
 * Memorial University of Newfoundland
 * Engineering 2420 Structured Programming
 * hopot.cpp -- Program to compute the hypoteneuse of a right angled triangle.
 *
 * Input: two adjacent sides of the triangle
 *
 * Output: length of the hypoteneuse
 *
 * Author: Michael Bruce-Lockhart
 *
 *******************************************************************/ /*#DA*/
#include <iostream>
#include <cmath>
using namespace std;


double hypot(double x, double y); // computes the hypoteneuse of a right triangle

/*#HA*/								  
/******************************************************************
 * main --
 *
 * Parameters: none
 * Modifies: nothing
 *
 * Returns: 0
 *******************************************************************/
/*#DA*/int main(){
	double side1;	// The two sides of the triangle adjacent to the right angle
	double side2;

	cout << "Please enter, separated by a space, the lengths of the "
		 << "two adjacent sides of the right triangle: ";
	cin >> side1 >> side2;

	cout << "The hypoteneuse is: " << hypot(side1, side2)<< endl;
	cout << "The hypoteneuse of a triangle whose sides are one longer is: " <<
		hypot(side1 + 1.0, side2 + 1.0) << endl;
	return 0;
}


/*#HA*/
/******************************************************************
 * hypot -- computes the hypoteneuse of a right triangle
 *
 * Parameters: x: the length of one adjacent side of the right triangle
 *             y: the length of the other adjacent side
 *
 * Modifies: none
 *
 * Precondition: none
 *
 * Returns: the length of the hypoteneuse
 *******************************************************************/
/*#DA*/double hypot(double x, double y) {
	return sqrt(x*x + y*y);
}
