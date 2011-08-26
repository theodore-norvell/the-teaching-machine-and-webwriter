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
int main(){
	int size;
	double sum = 0;
	int i;			// array index

	cout << "Please input the size of the array: ";
	cin >> size;

	double numbers[size];

// Fill up the array
	for (i = 0; i < size; i++)
		numbers[i] = fabs(sin(2*3.14159*i/size));

	cout << "There are " << size << " numbers as follows: " << endl;
	
	for (i = 0; i < size; i++) {
		cout << numbers[i] << ' ';
		sum += numbers[i];
	}

	cout << "\n\nTheir average is " << sum/size << endl;
	return 0;
}

