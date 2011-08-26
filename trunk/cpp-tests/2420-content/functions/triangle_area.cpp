/*#HA*/ /*#HB*/ /******************************************************************
 * Memorial University of Newfoundland
 * Engineering 2420 Structured Programming
 * triangle_area.cpp -- Demonstrate returning two pieces of data from a function
 *
 * Author: Michael Bruce-Lockhart
 * Date: 2004.03.02
 *
 *******************************************************************/

#include <iostream>
#include <string>
using namespace std;

void getValues(double& base, double& height);
double getDouble(const string& what);

/******************************************************************
 * main
 *
 * Parameters: none
 * Modifies: cout -- outputs some results
 *
 * Returns: 0
 *******************************************************************/
/*#DA*/
int main(){
	double tBase;
	double tHeight;

	cout << "A program to calculate the area of a triangle." << endl;

	getValues(tBase, tHeight);
	cout << "The area is " << tBase * tHeight / 2 << endl;
	return 0;
}


/*#HA*/
/******************************************************************
 * getValues
 *
 * Parameters: base: the base of a triangle
 *             height: the height of a triangle
 *
 * Modifies: base, height
 *
 * Returns: nothing
 *******************************************************************/
/*#DA*/
void getValues(double& base, double& height) {
	string prompt;
	prompt = "base";
	base = getDouble(prompt);
	prompt = "height";
	height = getDouble(prompt);
}


/*#HA*/
/******************************************************************
 * getDouble
 *
 * Parameters: what: a string representing what value is being gotten
 *             
 * Modifies: cout -- outputs a prompt
 *           cin -- fetches one double
 *
 * Returns: the double fetched
 *******************************************************************/
/*#DB*/
double getDouble(const string& what){
	double theInput;

	cout << "\nPlease input a value for " << what << ": ";
	cin >> theInput;
	return theInput;
}


