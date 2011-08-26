/*#H*//******************************************************************
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

	cout << endl << "The difference between the first number and itself is ";
	printComplex(re1 - re1, im1 - im1);

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
	cout << '(';
	/*#b="then1"*/if/*#/b*/ /*#B="then1"*/(re == 0) {
		/*#b="then2"*/if/*#/b*/ (im == 0) /*#B="then2"*/{
			cout << "0";
		}/*#/B*/ /*#b="else1"*/else/*#/b*/ /*#B="else1"*/{
			cout << im << 'j';
		}/*#/B*/
	}/*#/B*/ /*#b="else2"*/else/*#/b*/ /*#B="else2"*/{
		cout << re;
		/*#b="then3"*/if/*#/b*/ (im < 0) /*#B="then3"*/{
			cout << " - ";
			cout << -im << 'j';
		}/*#/B*/
		/*#b="then4"*/if/*#/b*/ (im > 0) /*#B="then4"*/{
			cout << " + ";
			cout << im << 'j';
		}/*#/B*/
	}/*#/B*/
	cout << ')';
}
/*#HA*/ /*#HB*/