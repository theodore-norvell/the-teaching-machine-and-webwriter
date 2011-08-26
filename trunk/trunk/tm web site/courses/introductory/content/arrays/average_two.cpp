/*#H*/ /******************************************************************
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
using namespace std;
#include <cmath>

double average(double data[], int size);

// Use constants for array sizes
const int SIZE_S = 10;
const int SIZE_C = 5;

int main(){
    // array sizes must be known at declaration
	double sines[SIZE_S];
	double cosines[SIZE_C];
	int i;    // used to index through both arrays

// Fill up the arrays
	for (i = 0; i < SIZE_S; i++)
		sines[i] = fabs(sin(2*3.14159*i/SIZE_S));

	for (i = 0; i < SIZE_C; i++)
		cosines[i] = fabs(cos(2*3.14159*i/SIZE_C));

	cout << "The average of the sines is " << average(sines, SIZE_S) << endl;
	cout << "& the average of the cosines is " << average(cosines, SIZE_C) << endl;

	return 0;
}

/*#DA*/
/** average ************************************************
 *
 *  @params: data - the array of data to be averaged
 *           size: the number of elements in the data array
 *
 * Modifies: nothing
 *
 * @returns: the average of the data
 ***********************************************************/

double average(double data[], int size){
	double sum = 0.;
	for (int i = 0; i < size; i++)
		sum += data[i];
	return sum/size;
}/*#HA*/

