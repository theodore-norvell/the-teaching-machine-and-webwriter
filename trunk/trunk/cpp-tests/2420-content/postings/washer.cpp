/******************************************************************
 * Memorial University of Newfoundland
 * Engineering 2420 Structured Programming
 * washer.cpp -- Program to compute the weight of a number of washers.
 *
 * Input: Washer outer diameter in mm.
 *        Washer hole diameter in mm.
 *        Washer thickness in mm.
 *        Number of washers in a batch
 *        Material density in gm/cm cubed
 * Output: Weight of the batch. in gms
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

void printOutInstructions();
double washerWeight(double thickness, double density, double inner, double outer);
double washerArea(double inner, double outer);
double circleArea(double radius);

/******************************************************************
 * main --
 *
 * Parameters: none
 * Modifies: nothing
 *
 * Returns: 0
 *******************************************************************/
int main(){
	int number;	// number of washers
	double thickness;	// Washer characteristics
	double density;
	double inner;		// inner radius
	double outer;		// outer radius

	printOutInstructions();

// Fetch all the required data from the user.
	cout << "Please enter the number of washers: ";
	cin >> number;
	cout << "\nPlease enter the thickness of the washers in mm.: ";
	cin >> thickness;
	cout << "\nPlease enter the density of the washers in gm./cc: ";
	cin >> density;
	cout << "\nPlease enter the inner and outer radii of the washers in mm.: ";
	cin >> inner >> outer;

/*Find the weight, w, of an individual washer 
batch weight = n x w */

	cout << "The weight of the entire batch is " 
		 << number * washerWeight(thickness, density, inner, outer)
		 << " grams." << endl;

	return 0;
}


// Function implementations

/******************************************************************
 * printOutInstructions -- Give user an explanation of the program
 *
 * Parameters: none
 *
 * Modifies: cout
 *
 * Precondition: none
 *
 * Returns: nothing
 *******************************************************************/
void printOutInstructions(){
	cout << "This ia a program to calculate the weight of a batch of identical washers." << endl;
	cout << "You will be asked to input the number of washers and a washer's characteristics." << endl;
}



/******************************************************************
 * washerWeight -- compute the weight of a washer
 *
 * Parameters: float density: of washer material in kg/m cubed
 *             float thickness: of washer in m.
 *             float innerR: inner radius in m.
 *             float outerR: outer radius in m.
 *
 * Modifies: nothing
 *
 * Precondition: all parameters should be non-negative
 *
 * Returns: the weight of the washer in kg.
 *******************************************************************/

/* 1. Find the washer's area, a
2. Find the washer's volume, v = a x thickness, t.
3. weight: w = v x d ( the density )
*/


double washerWeight(double thickness, double density, double inner, double outer) {
	return density * 0.1 * thickness * washerArea(inner, outer);
}

/******************************************************************
 * washerArea -- compute the area of a washer
 *
 * Parameters: float inner: inner radius in mm.
 *             float outer: outer radius in mm.
 *
 * Modifies: nothing
 *
 * Precondition: all parameters should be non-negative
 *
 * Returns: the area of the washer in cm. squared.
 *******************************************************************/
double washerArea(double inner, double outer){
	return circleArea(outer) - circleArea(inner);
}


/******************************************************************
 * circleArea -- compute the area of a circle
 *
 * Parameters: float radius: in mm.
 *
 * Modifies: nothing
 *
 * Precondition: radius should be non-negative
 *
 * Returns: the area of the circle in cm. squared.
 *******************************************************************/

double circleArea(double radius){
	return PI * radius * radius * .01;
}



