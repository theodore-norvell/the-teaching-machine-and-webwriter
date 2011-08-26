/*#HA*//******************************************************************
 * Memorial University of Newfoundland
 * Engineering 2420 Structured Programming
 * libfunc.cpp -- Demonstrate using library functions.
 *
 *
 * Author: Michael Bruce-Lockhart
 * Date: 2006.01.18
 *
 *******************************************************************/

#include <iostream>
#include <cmath>
using namespace std;

double polarMag(double x, double y);
double polarAngle(double x, double y);
double rectangularX(double r, double theta);
double rectangularY(double r, double theta);

const double PI = 4*atan(1.0);

int main() {   
    cout << "Converting(1,1) to polar co-ordinates gives ";
	cout << polarMag(1,1) << " L" << polarAngle(1,1);
	cout << "\nConverting 22.14 L54 to rectangular co-ordinates gives (";
	cout << rectangularX(22.14,54) << ", " << rectangularY(22.14,54) << ")\n";
  return 0;
}
/*#DA*/
/*** polarMag  *******************************************************
 *
 * @params: x - real x co-ordinate
 *          y - real y co-ordinate
 *
 * @returns: the polar equivalent magnitude
 *******************************************************************/

double polarMag(double x, double y){
	return sqrt(pow(x,2) + pow(y,2));
}


/*** polarAngle  *******************************************************
 *
 * @params: x - real x co-ordinate
 *          y - real y co-ordinate
 *
 * @returns: the polar co-ordinate angle in degrees
 *******************************************************************/

double polarAngle(double x, double y){
	return atan2(x,y)*180/PI;
}


/*** rectangularX  *******************************************************
 *
 * @params: r - polar magnitude co-ordinate
 *          theta - polar angular co-ordinate in degrees
 *
 * @returns: the equivalent x co-ordinate
 *******************************************************************/

double rectangularX(double r, double theta){
	theta *= PI/180;
	return r * cos(theta);
}


/*** rectangularY  *******************************************************
 *
 * @params: r - polar magnitude co-ordinate
 *          theta - polar angular co-ordinate in degrees
 *
 * @returns: the equivalent y co-ordinate
 *******************************************************************/

double rectangularY(double r, double theta){
	theta *= PI/180;
	return r * sin(theta);
}/*#HA*/
