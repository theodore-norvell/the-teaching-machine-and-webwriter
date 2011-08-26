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
using namespace std;


/******************************************************************
 * main
 *
 * Parameters: none
 *
 * Returns: 0
 *******************************************************************/
void toPolar(double& xMag, double& yAngle);
void toRect(double& magX, double& angleY);
double pi();

int main() {
	double x = 1.0;
	double y = 1.0;
	cout << "Converting(" << x << ", " << y << ") to polar co-ordinates gives ";
	toPolar(x,y);
	cout << x << 'L' << y << endl;
	toRect(x,y);
	cout << "Converting back gives (" << x << ", " << y << ")" << endl;
  return 0;
}
/*#DA*/
#include <cmath>

/*** toPolar  *******************************************************
 *
 * @params: xMag - ref to real x co-ordinate
 *          yAngle - ref to real y co-ordinate
 *
 * @modifies: xMag is the polar mag of the original co-ordinates
 *            yAngle is the polar angle of the original co-ordinates
 *
 * @returns: nothing
 *******************************************************************/

void toPolar(double& xMag, double& yAngle){
	double mag = sqrt(pow(xMag,2) + pow(yAngle,2)); // save xMag until angle computed
	yAngle = atan2(yAngle, xMag);	// in radians
	yAngle = yAngle * 180./pi();	// convert to degrees
	xMag = mag;
}



/*** toRect  *******************************************************
 *
 * @params: magX - polar magnitude 
 *          angleY - polar angle in degrees
 *
 * @modifies: magX is the rectangular x value of the original co-ordinates
 *            angleY is the rectangular y value of the original co-ordinates
 *
 * @returns: nothing
 *******************************************************************/
void toRect(double& magX, double& angleY){
	double theta = angleY * pi()/180; // angle in radians
	angleY = magX * sin(theta);
	magX = magX * cos(theta);
}


/*** pi  *******************************************************
 *
 * @params: none
 *
 *           Strangely, neither C nor C++ define a value for pi
 *           This is not a very efficient way to provide it since
 *           it has to be recalculated every time we need it.
 *           We'll show you how to do better later on.
 *
 * @returns: the value of pi to the full precision of the underlying machine
 *******************************************************************/

double pi(){
	return 4 * atan(1.0);
}

