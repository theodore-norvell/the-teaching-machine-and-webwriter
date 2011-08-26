/******************************************************************
 * Memorial University of Newfoundland
 * Engineering 2420 Structured Programming
 * washer.cpp -- Program to compute the weight of a number of washers.
 *
 * Input: Washer outer diameter in mm.
 *        Washer hole diameter in mm.
 *        Washer thickness in mm.
 *        Number of washers in a batch
 *        Material density in kg/m cubed
 * Output: Weight of the batch. in gms
 *
 * Author: Michael Bruce-Lockhart
 *
 *******************************************************************/

#include <iostream>
#include <string>

using namespace std;

// Function declarations

void printExplanation();	// Give an explanation of the program
float getFloat(string what);	// get a floating point value for what
int getInt(string what);		// get an int value for what
float getWeight(float density, float thickness, float innerR, float outerR);  // weight for a single washer
float washerArea(float innerR, float outerR);									// area of a single washer
float circleArea(float radius);													// area of a circle

/******************************************************************
 * main --
 *
 * Parameters: none
 * Modifies: nothing
 *
 * Returns: 0
 *******************************************************************/
int main(){
	int number;	// the total number of washers in the batch

	// Washer dimensions
	float innerR;
	float outerR;
	float thickness;

	float density;	// of the washer material

	// Prompt and get values for n, t, d, and the inner and outer radii

	printExplanation();
	innerR = .001 * getFloat("inner radius in mm."); // Get radii in mm & convert to m.
	outerR = .001 * getFloat("outer radius in mm.");
	density = getFloat("density in kg/m cubed");
	thickness = .001 * getFloat("thickness in mm");	// Get thickness in mm & convert to meters
	number = getInt("number of washers");

	cout << "The weight of the batch of washers is ";
	cout << number * getWeight(density, thickness, innerR, outerR) * 1000 << " gms.";
	cout << endl;

	return 0;
}


// Function implementations

/******************************************************************
 * printExplanation -- Give user an explanation of the program
 *
 * Parameters: none
 *
 * Modifies: none
 *
 * Precondition: none
 *
 * Returns: nothing
 *******************************************************************/
void printExplanation(){ 
	cout << "This program will compute the total weight of a batch of identical washers.\n\n"<< endl;
}



/******************************************************************
 * getFloat -- Fetch a floating point number from input stream
 *
 * Parameters: string what: used to help prompt the user as to what the number is for
 *
 * Modifies: cin
 *
 * Precondition: none
 *
 * Returns: the floating point number input by the user
 *******************************************************************/
float getFloat(string what){
	float entryValue;
	cout << "Please input a value for the " << what << ": ";
	cin >> entryValue;
	return entryValue;
}



/******************************************************************
 * getInt -- Fetch an integer number from input stream
 *
 * Parameters: string what: used to help prompt the user as to what the number is for
 *
 * Modifies: cin
 *
 * Precondition: none
 *
 * Returns: the integer number input by the user
 *******************************************************************/

int getInt(string what){
	int entryValue;
	cout << "Please input a value for the " << what << ": ";
	cin >> entryValue;
	return entryValue;
}


/******************************************************************
 * getWeight -- compute the weight of a washer
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

float getWeight(float density, float thickness, float innerR, float outerR){
	return density * thickness * washerArea(innerR, outerR);
}



/******************************************************************
 * washerArea -- compute the area of a washer
 *
 * Parameters: float innerR: inner radius in m.
 *             float outerR: outer radius in m.
 *
 * Modifies: nothing
 *
 * Precondition: all parameters should be non-negative
 *
 * Returns: the area of the washer in m. squared.
 *******************************************************************/

float washerArea(float innerR, float outerR){
	return circleArea(outerR) - circleArea(innerR);
}




/******************************************************************
 * circleArea -- compute the area of a circle
 *
 * Parameters: float radius: in m.
 *
 * Modifies: nothing
 *
 * Precondition: radius should be non-negative
 *
 * Returns: the area of the circle in m. squared.
 *******************************************************************/

float circleArea(float radius){
	return 3.14159 * radius * radius;
}