/*#H*/#include <iostream>
#include <cmath>
using namespace std;

// standard test functions
void constantClient();
void anxiousClient();
void independentClient();
void expressiveClient();
void confusedClient();

void toPolar(double& xMag, double& yAngle);
void toRect(double& magX, double& angleY);

const double PI = 4*atan(1.0);

/*#DA*/ int main(){
    constantClient();
    anxiousClient();
    independentClient();
    expressiveClient();
    confusedClient();
}/*#HA*/
/*#DB*/ // These are all tests of our service function
void constantClient(){
     /* This is what constant clients want to do and it
     isn't legal as the calling arguments must be variables */
     // toPolar(3,4);
}

void anxiousClient(){
     double xMag = -1.0;
     double yAngle = 3.5;
     cout << '(' << xMag << ", " << yAngle << ") in polar co-ordinates is ";
     toPolar(xMag, yAngle);
     cout << xMag << " L" << yAngle << endl;
}
void independentClient(){
     double kayla = 1.0;
     double allyson = 1.0;
     cout << '(' << kayla << ", " << allyson << ") in polar co-ordinates is ";
     toPolar(kayla, allyson);
     cout << kayla << " L" << allyson << endl;
}
void expressiveClient(){
     int x = 6;
     int y = 23;
     /* This is what expressive clients want to do and it
     isn't legal as the calling arguments must be variables */
     //toPolar(x*x/12, y-x);
}
void confusedClient(){
     double xMag = 1.0;
     double yAngle = 2.0;
     cout << '(' << xMag << ", " << yAngle << ") in polar co-ordinates is ";
     toPolar(yAngle, xMag);
     cout << xMag << " L" << yAngle << endl;
}/*#HB*/

/*#DC*/ /*** toPolar  *******************************************************
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
	yAngle = yAngle * 180./PI;	// convert to degrees
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
	double theta = angleY * PI/180; // angle in radians
	angleY = magX * sin(theta);
	magX = magX * cos(theta);
}/*#HC*/


    
