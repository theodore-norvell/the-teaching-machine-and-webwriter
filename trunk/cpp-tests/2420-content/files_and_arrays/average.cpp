/*#HA*/ /******************************************************************
 * Memorial University of Newfoundland
 * Engineering 2420 Structured Programming
 * average.cpp -- Program to compute the average of an array of nos. 
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


/******************************************************************
 * main --
 *
 * Parameters: none
 * Modifies:  output streams
 *
 * Returns: 0
 *******************************************************************/
/*#DA*/	
int main(){
	double numbers[10];
	double sum = 0;
	int i;			// array index

// Fill up the array
	for (i = 0; i < 10; i++)
		numbers[i] = fabs(sin(2*3.14159*i/10));

	cout << "There are 10 numbers as follows: " << endl;
	
	for (i = 0; i < 10; i++) {
		cout << numbers[i] << ' ';
		sum += numbers[i];
	}

	cout << "\n\nTheir average is " << sum/10 << endl;
	return 0;
}

