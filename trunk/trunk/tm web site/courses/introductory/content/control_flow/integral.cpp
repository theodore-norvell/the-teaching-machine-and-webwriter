/*#HA*/ /*******  for loop exercise 4 ********
    A little program to compute integrals.

*******************************************/

#include <iostream>
using namespace std;    // cout is in the std namespace
#include <cmath>

double integral(double a, double b, int n);
double f(double x);

double PI = 4 * atan(1.0);

int main(){

    cout << "integral of sin(x) from 0 to pi is ";
    cout << integral(0, PI, 10) << endl;
    
    cout << "integral of sin(x) from 0 to 2pi is ";
    cout << integral(0, 2*PI, 10) << endl;
//    system("PAUSE");
	return 0;
}
/*#DA*/
/** integral *************************************************
*
* @params: a - left edge of integral
           b - right edge @pre: b > a
           n - number of trapezoids @pre: n > 0
*
* @returns: estimate of the integral of f(x) where
*           f(x) is declared, using n trapezoids
****************************************************************/
double integral(double a, double b, int n) {
	double deltaX = (b-a) / n;
    double xLeft = a;
    double area = 0;
    for (int i = 0; i < n; i++) { // n trapezoids
        double xRight = xLeft + deltaX;
        area += deltaX * (f(xLeft) + f(xRight))/ 2.0;
        xLeft = xRight; // update xLeft for next pass
    }
	return area;
}
/** f *************************************************
*
* @params: x - value on x axis
*
* @returns: f(x) - change function for different integral
****************************************************************/

double f(double x){
       return sin(x);
}



        
