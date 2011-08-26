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
	int mark = getMark();
	while (mark != -1) {
		cout << mark
	return 0;
}
/*#HD*/ /*#HA*/
/*#DB*/
/******************************************************************
 * getMark
 *
 * Parameters: none
 *             
 * Modifies: nothing
 *             
 *
 * Returns: a mark between 0 and 100 or -1
 *******************************************************************/
/*#DA*/int getMark(){
	int mark;
	cout << "Please input a mark: ";
	cin >> mark;
	while (mark < -1 || mark > 100) {
		cout << "\nMark must be between 0 and 100 or -1 to end input. Please input again: ";
		cin >> mark;
	}
	return mark;
}
/*#HA*/