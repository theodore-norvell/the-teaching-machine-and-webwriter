/*#HA*/ /*#HB*/ /******************************************************************
 * Memorial University of Newfoundland
 * Engineering 2420 Structured Programming
 * average_function.cpp -- Computing the average of an array
 *                         is moved to a function. 
 *
 * Input: none
 *
 * Output: the average
 *
 * Author: Michael Bruce-Lockhart
 *
 *******************************************************************/

#include <iostream>
#include <cmath>


using namespace std;


double average(double data[], int size);


/******************************************************************
 * main --
 *
 * Parameters: none
 * Modifies:  output streams
 *
 * Returns: 0
 *******************************************************************/
/*#DA*/	
const int SIZE_S = 10;
const int SIZE_C = 5;

int main(){
	double sines[SIZE_S];
	double cosines[SIZE_C];
	int i;    // used to index through arrays


// Fill up the arrays
	for (i = 0; i < SIZE_S; i++)
		sines[i] = fabs(sin(2*3.14159*i/SIZE_S));

	for (i = 0; i < SIZE_C; i++)
		cosines[i] = fabs(cos(2*3.14159*i/SIZE_C));
		
		ScriptManager.relay("MUN.PlugIn.ArrayBar", "addToGenerator", A);
		// drop a snapshot of A's initial state for the first porthole
		ScriptManager.snapShot("MUN.PlugIn.ArrayBar","first"); 

	cout << "The average of the sines is ";
	cout << average(sines, SIZE_S) << endl;
	cout << "& the average of the cosines is " ;
	cout << average(cosines, SIZE_C) << endl;

	return 0;
}

/*#HA*/

/******************************************************************
 * average -- compute the average of an array of doubles
 *
 * Parameters: data: the array of data to be averaged
 *             size: the number of elements in the data array
 *
 * Modifies: nothing
 *
 * Precondition: none
 *
 * Returns: the average of the data
 *******************************************************************/
/*#DB*/
double average(double data[], int size){
	double sum = 0.;
	for (int i = 0; i < size; i++)
		sum += data[i];
	return sum/size;
}

