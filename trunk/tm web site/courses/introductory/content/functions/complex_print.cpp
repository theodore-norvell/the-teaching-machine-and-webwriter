/*#HA*/ /*#HB*/ /*#HC*/ /*#HD*/ /******************************************************************
 * Memorial University of Newfoundland
 * Engineering 2420 Structured Programming
 * complex_print.cpp -- Demonstrate functions
 *
 * Author: Michael Bruce-Lockhart
 * Date: 2004.01.02
 *
 *******************************************************************/
/*#DA*/#include <iostream>
using namespace std;

/*#DC*/void printComplex(double re, double im);  // function DECLARATION
/*#HC*/
/*#HA*//******************************************************************
 * main
 *
 * Parameters: none
 * Modifies: cout -- outputs some results
 *
 * Returns: 0
 *******************************************************************/
/*#DA*/ /*#DD*/
int main(){
	double re1 = 2.4;
	double im1 = 3.1;
	double re2 = -1.;
	double im2 = -2.9;

	cout << "The first number is ";
	printComplex(re1, im1);

	cout << endl << "The second number is ";
	printComplex(re2, im2);		              // function CALL

	cout << endl << "Their sum is ";
	printComplex(re1 + re2, im1 + im2);       // function CALL

	cout << endl << "Their difference is ";
	printComplex(re1 - re2, im1 - im2);       // function CALL

	cout << endl;
	return 0;
}
/*#HD*/ /*#HA*/
/*#DB*/
/******************************************************************
 * printComplex
 *
 * Parameters: re: the real part
 *             im: the imaginary part
 * Modifies: cout -- outputs the complex no. whose
 *             real part is re and imaginary part is im
 *
 * Returns: nothing
 *******************************************************************/
/*#DA*/void printComplex(double re, double im){    // function DEFINITION
	cout << '(' << re ;
	cout <<" + " << im;
	cout << "j)";
}
/*#HA*/ /*#HB*/