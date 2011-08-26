/*#HA*/ /******************************************************************
 * Memorial University of Newfoundland
 * Engineering 2420 Structured Programming
 * average_better.cpp -- Better program to compute the average of an array of nos. 
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
const int SIZE = 10;

int main(){
	double numbers[SIZE];
	double sum = 0;
	int i;			// array index

// Fill up the array
	for (i = 0; i < SIZE; i++)
		numbers[i] = fabs(sin(2*3.14159*i/SIZE));

	cout << "There are " << SIZE << " numbers as follows: " << endl;
	
	for (i = 0; i < SIZE; i++) {
		cout << numbers[i] << ' ';
		sum += numbers[i];
	}

	cout << "\n\nTheir average is " << sum/SIZE << endl;
	return 0;
}

